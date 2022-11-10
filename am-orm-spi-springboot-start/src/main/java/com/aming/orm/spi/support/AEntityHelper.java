package com.aming.orm.spi.support;

/**
 * @author ：张俊
 * @date ：Created in 2022/6/30 15:02
 * @description： TODO
 */

import com.aming.orm.spi.entity.AEntityColumn;
import com.aming.orm.spi.log.ALoger;
import com.aming.orm.spi.log.ALogerFactory;
import com.aming.orm.spi.AEntityResolve;
import com.aming.orm.spi.entity.AEntityTable;
import com.aming.orm.spi.jpa.AJpaAEntityResolve;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实体类工具类 - 处理实体和数据库表以及字段关键的一个类
 * <p/>
 *
 * @author zhangjun
 */
public class AEntityHelper {

    private static ALoger logger = ALogerFactory.getLoger(AEntityHelper.class);
    /**
     * 实体类 => 表对象
     */
    private static final Map<Class<?>, AEntityTable> entityTableMap = new ConcurrentHashMap<Class<?>, AEntityTable>();
    /**
     * 实体类解析器
     */
    private static AEntityResolve resolve = new AJpaAEntityResolve();


    /**
     * 获取表对象l
     *
     * @param entityClass
     * @return
     */
    public static AEntityTable getEntityTable(Class<?> entityClass) {
        initEntityNameMap(entityClass);
        AEntityTable aEntityTable = entityTableMap.get(entityClass);
        if (aEntityTable == null) {
            throw new RuntimeException("无法获取实体类" + entityClass.getCanonicalName() + "对应的表名!");
        }
        return aEntityTable;
    }

    /**
     * 获取表对象l
     *
     * @param entityClass
     * @return
     */
    public static AEntityTable getEntityTable(Class<?> entityClass, AEntityResolve resolve) {
        AEntityTable aEntityTable = resolve.resolveEntity(entityClass);
        if (aEntityTable == null) {
            throw new RuntimeException("无法通过该解析器" + resolve.getClass().getName() + "获取实体对象!");
        } else {
            entityTableMap.put(entityClass, aEntityTable);
        }
        return aEntityTable;
    }

    /**
     * 更新表对象
     * @param entityClass
     * @param resolve
     * @return
     */
    public static AEntityTable updateEntityTable(Class<?> entityClass, AEntityResolve resolve) {
        AEntityTable aEntityTable = resolve.resolveEntity(entityClass);
        if (aEntityTable == null) {
            throw new RuntimeException("无法通过该解析器" + resolve.getClass().getName() + "获取实体对象!");
        } else {
            entityTableMap.put(entityClass, aEntityTable);
        }
        return aEntityTable;
    }

    /**
     * 更新表对象
     * @param entityClass
     * @return
     */
    public static AEntityTable updateEntityTable(Class<?> entityClass) {
        AEntityTable aEntityTable = resolve.resolveEntity(entityClass);
        if (aEntityTable == null) {
            throw new RuntimeException("无法通过该解析器" + resolve.getClass().getName() + "获取实体对象!");
        } else {
            entityTableMap.put(entityClass, aEntityTable);
        }
        return aEntityTable;
    }

    /**
     * 获取默认的orderby语句
     *
     * @param entityClass
     * @return
     */
    public static String getOrderByClause(Class<?> entityClass) {
        AEntityTable table = getEntityTable(entityClass);
        if (table.getOrderByClause() != null) {
            return table.getOrderByClause();
        }
        StringBuilder orderBy = new StringBuilder();
        for (AEntityColumn column : table.getEntityClassColumns()) {
            if (column.getOrderBy() != null) {
                if (orderBy.length() != 0) {
                    orderBy.append(",");
                }
                orderBy.append(column.getColumn()).append(" ").append(column.getOrderBy());
            }
        }
        table.setOrderByClause(orderBy.toString());
        return table.getOrderByClause();
    }

    /**
     * 获取全部列
     *
     * @param entityClass
     * @return
     */
    public static Set<AEntityColumn> getColumns(Class<?> entityClass) {
        return getEntityTable(entityClass).getEntityClassColumns();
    }

    /**
     * 获取主键信息
     *
     * @param entityClass
     * @return
     */
    public static Set<AEntityColumn> getPKColumns(Class<?> entityClass) {
        return getEntityTable(entityClass).getEntityClassPKColumns();
    }

    /**
     * 获取查询的Select
     *
     * @param entityClass
     * @return
     */
    public static String getSelectColumns(Class<?> entityClass) {
        AEntityTable aEntityTable = getEntityTable(entityClass);
        if (aEntityTable.getBaseSelect() != null) {
            return aEntityTable.getBaseSelect();
        }
        Set<AEntityColumn> columnList = getColumns(entityClass);
        StringBuilder selectBuilder = new StringBuilder();
        boolean skipAlias = Map.class.isAssignableFrom(entityClass);
        for (AEntityColumn aEntityColumn : columnList) {
            selectBuilder.append(aEntityColumn.getSelectColumn());
            if (!skipAlias && !aEntityColumn.getSelectColumn().equalsIgnoreCase(aEntityColumn.getProperty())) {
                //不等的时候分几种情况，例如`DESC`
                if (aEntityColumn.getSelectColumn().substring(1, aEntityColumn.getSelectColumn().length() - 1).equalsIgnoreCase(aEntityColumn.getProperty())) {
                    selectBuilder.append(",");
                } else {
                    selectBuilder.append(" AS ").append(aEntityColumn.getProperty()).append(",");
                }
            } else {
                selectBuilder.append(",");
            }
        }
        aEntityTable.setBaseSelect(selectBuilder.substring(0, selectBuilder.length() - 1));
        return aEntityTable.getBaseSelect();
    }

    /**
     * 初始化实体属性
     *
     * @param entityClass
     */
    public static synchronized void initEntityNameMap(Class<?> entityClass) {
        if (entityTableMap.get(entityClass) != null) {
            return;
        }
        //创建并缓存EntityTable
        AEntityTable aEntityTable = resolve.resolveEntity(entityClass);
        entityTableMap.put(entityClass, aEntityTable);
    }

    /**
     * 设置实体类解析器
     *
     * @param resolve
     */
    public static void setResolve(AEntityResolve resolve) {
        AEntityHelper.resolve = resolve;
    }
}
