package com.aming.orm.spi.jpa;


import com.aming.orm.spi.AEntityResolve;
import com.aming.orm.spi.entity.AEntityColumn;
import com.aming.orm.spi.entity.AEntityField;
import com.aming.orm.spi.log.ALoger;
import com.aming.orm.spi.log.ALogerFactory;
import com.aming.orm.spi.support.AFieldHelper;
import com.aming.orm.spi.support.ASimpleTypeUtil;
import com.aming.orm.spi.utils.ASpringUtils;
import com.aming.orm.spi.entity.AEntityTable;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * jpa实体类的解析器
 *
 * @author zhangjun
 */
public class AJpaAEntityResolve implements AEntityResolve {

    private final ALoger log = ALogerFactory.getLoger(AJpaAEntityResolve.class);

    @Override
    public AEntityTable resolveEntity(Class<?> entityClass) {
        //创建并缓存EntityTable
        AEntityTable aEntityTable = null;
        if (entityClass.isAnnotationPresent(Table.class)) {
            Table table = entityClass.getAnnotation(Table.class);
            if (!"".equals(table.name())) {
                aEntityTable = new AEntityTable(entityClass);
                aEntityTable.setTable(table);
            }
        }
        if (aEntityTable == null) {
            throw new RuntimeException("非po对象");
        }
        aEntityTable.setEntityClassColumns(new LinkedHashSet<AEntityColumn>());
        aEntityTable.setEntityClassPKColumns(new LinkedHashSet<AEntityColumn>());
        //处理所有列
        List<AEntityField> fields = null;
        fields = AFieldHelper.getFields(entityClass);
        for (AEntityField field : fields) {
            //如果启用了简单类型，就做简单类型校验，如果不是简单类型，直接跳过
            //3.5.0 如果启用了枚举作为简单类型，就不会自动忽略枚举类型
            //4.0 如果标记了 Column 或 ColumnType 注解，也不忽略
            if (!field.isAnnotationPresent(Column.class) && !(ASimpleTypeUtil.isSimpleType(field.getJavaType()) || (Enum.class.isAssignableFrom(field.getJavaType())))) {
                continue;
            }
            processField(aEntityTable, field);
        }
        //当pk.size=0的时候使用所有列作为主键
        if (aEntityTable.getEntityClassPKColumns().size() == 0) {
            aEntityTable.setEntityClassPKColumns(aEntityTable.getEntityClassColumns());
        }
        aEntityTable.initPropertyMap();
        return aEntityTable;
    }

    @Override
    public AEntityTable resolveEntity(Object entityidentification) {
        return null;
    }


    /**
     * 处理字段
     *
     * @param aEntityTable
     * @param field
     */
    protected void processField(AEntityTable aEntityTable, AEntityField field) {
        //排除字段
        if (field.isAnnotationPresent(Transient.class)) {
            return;
        }
        //Id
        AEntityColumn aEntityColumn = new AEntityColumn(aEntityTable);

        aEntityColumn.setEntityField(field);
        if (field.isAnnotationPresent(Id.class)) {
            aEntityColumn.setId(true);
        }
        //Column
        String columnName = null;
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            columnName = column.name();
            aEntityColumn.setUpdatable(column.updatable());
            aEntityColumn.setInsertable(column.insertable());
        }

        aEntityColumn.setProperty(field.getName());
        aEntityColumn.setColumn(columnName);
        if (columnName == null || columnName.equals("")) {
            String entityTableField = ASpringUtils.getBean(EntityManagerProvider.class).getEntityTableField(aEntityTable.getEntityClass(), field.getName());
            aEntityColumn.setColumn(entityTableField);
        }
        aEntityColumn.setJavaType(field.getJavaType());
        if (field.getJavaType().isPrimitive()) {
            log.warn("警告信息: <[" + aEntityColumn + "]> 使用了基本类型，基本类型在动态 SQL 中由于存在默认值，因此任何时候都不等于 null，建议修改基本类型为对应的包装类型!");
        }
        //OrderBy
        processOrderBy(aEntityTable, field, aEntityColumn);
        //处理主键策略
        aEntityTable.getEntityClassColumns().add(aEntityColumn);
        if (aEntityColumn.isId()) {
            aEntityTable.getEntityClassPKColumns().add(aEntityColumn);
        }
    }

    /**
     * 处理排序
     *
     * @param aEntityTable
     * @param field
     * @param aEntityColumn
     */
    protected void processOrderBy(AEntityTable aEntityTable, AEntityField field, AEntityColumn aEntityColumn) {
        if (field.isAnnotationPresent(OrderBy.class)) {
            OrderBy orderBy = field.getAnnotation(OrderBy.class);
            if ("".equals(orderBy.value())) {
                aEntityColumn.setOrderBy("ASC");
            } else {
                aEntityColumn.setOrderBy(orderBy.value());
            }
        }
    }
}

