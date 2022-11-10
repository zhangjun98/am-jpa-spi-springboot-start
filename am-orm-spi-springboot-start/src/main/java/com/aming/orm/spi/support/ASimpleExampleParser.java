package com.aming.orm.spi.support;


import com.aming.orm.spi.entity.AEntityColumn;

import com.aming.orm.spi.json.AJsonHelper;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ：张俊
 * @date ：Created in 2022/7/1 9:33
 * @description： Example 简单解析器
 */
public class ASimpleExampleParser {

    /**
     * 根据Example查询总数
     *
     * @param
     * @return Pair<String, List<Object>>  key 是 sql  value 是 值 （sql 里面使用 预编译 ？ 占位）
     */
    public Pair<String, List<Object>> selectCountByExample(AExample aExample) {
        List<Object> objects = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT ");
        sql.append(ASqlHelper.exampleCountColumn(aExample, objects));
        sql.append(ASqlHelper.fromTable(aExample, aExample.tableName));
        sql.append(ASqlHelper.exampleWhereClause(aExample, objects).getKey());
        sql.append(ASqlHelper.exampleForUpdate(aExample));
        return new Pair<>(sql.toString(), objects);
    }

    /**
     * 根据Example删除
     *
     * @param aExample
     * @return Pair<String, List<Object>>  key 是 sql  value 是 值 （sql 里面使用 预编译 ？ 占位）
     */
    public Pair<String, List<Object>> deleteByExample(AExample aExample) {
        Pair<String, List<Object>> sqlAndContition = null;
        List<Object> objects = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        //不允许执行不带查询条件的 delete 方法
        ASqlHelper.exampleHasAtLeastOneCriteriaCheck(aExample);
        sql.append(ASqlHelper.deleteFromTable(aExample, aExample.tableName));
        sql.append(ASqlHelper.exampleWhereClause(aExample, objects).getKey());
        return new Pair<>(sql.toString(), objects);
    }


    /**
     * 根据Example查询
     *
     * @param aExample
     * @return Pair<String, List<Object>>  key 是 sql  value 是 值 （sql 里面使用 预编译 ？ 占位）
     */
    public Pair<String, List<Object>> selectByExample(AExample aExample) {

        StringBuilder sql = new StringBuilder("SELECT ");
        List<Object> objects = new ArrayList<>();
        if (aExample.distinct) {
            sql.append("distinct");
        }
        //支持查询指定列
        sql.append(ASqlHelper.exampleSelectColumns(aExample));
        sql.append(ASqlHelper.fromTable(aExample, aExample.tableName));
        sql.append(ASqlHelper.exampleWhereClause(aExample, objects).getKey());
        sql.append(ASqlHelper.exampleOrderBy(aExample));
        sql.append(ASqlHelper.exampleForUpdate(aExample));
        return new Pair<>(sql.toString(), objects);
    }

    /**
     * 根据Example查询
     *
     * @param aExample
     * @return Pair<String, List<Object>>  key 是 sql  value 是 值 （sql 里面使用 预编译 ？ 占位）
     */
    public Pair<String, List<Object>> selectByExampleAndRowBounds(AExample aExample) {
        return selectByExample(aExample);
    }

    /**
     * 根据Example更新非null字段
     *
     * @param
     * @return Pair<String, List<Object>>  key 是 sql  value 是 值 （sql 里面使用 预编译 ？ 占位）
     */
    public Pair<String, List<Object>> updateByExampleSelective(Object updateEntity, AExample aExample) {
        List<Object> objects = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        //安全更新，Example 必须包含条件
        ASqlHelper.exampleHasAtLeastOneCriteriaCheck(aExample);
        sql.append(ASqlHelper.updateTable(aExample, aExample.tableName));
        sql.append(updateSetColumns(updateEntity, aExample, objects, true));
        sql.append(ASqlHelper.updateByExampleWhereClause(aExample, objects).getKey());
        return new Pair<>(sql.toString(), objects);
    }

    /**
     * 根据Example更新
     *
     * @param
     * @return Pair<String, List<Object>>  key 是 sql  value 是 值 （sql 里面使用 预编译 ？ 占位）
     */
    public Pair<String, List<Object>> updateByExample(Object updateEntity, AExample aExample) {
        List<Object> objects = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        //安全更新，Example 必须包含条件
        ASqlHelper.exampleHasAtLeastOneCriteriaCheck(aExample);
        sql.append(ASqlHelper.updateTable(aExample, aExample.tableName));
        sql.append(updateSetColumns(updateEntity, aExample, objects, false));
        sql.append(ASqlHelper.updateByExampleWhereClause(aExample, objects).getKey());
        return new Pair<>(sql.toString(), objects);
    }


    private String updateSetColumns(Object updateEntity, AExample aExample, List<Object> objects, boolean notNull) {
        // 这里判断是否为json类型
    	
    	Map<String, Object> stringObjectMap = AJsonHelper.toJson(updateEntity).toBean(HashMap.class,false);
    	
        //Map<String, Object> stringObjectMap = JSONObject.parseObject(JSONObject.toJSONString(updateEntity)).getInnerMap();
        StringBuilder sql = new StringBuilder();
        //获取全部列
        Set<AEntityColumn> columnSet = AEntityHelper.getColumns(aExample.getEntityClass());
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        StringBuilder updatesql = new StringBuilder();
        for (AEntityColumn column : columnSet) {
            if (!column.isId() && column.isUpdatable()) {
                Object columnValue = stringObjectMap.get(column.getProperty());
                if (notNull) {
                    // 判断 这个column 对应的 有没有 值
                    if (columnValue != null) {
                        if (columnValue instanceof String) {
                            if (columnValue != "") {
                                updatesql.append(column.getColumn()).append(" = ").append("?").append(",");
                                objects.add(column.wrapColumnValue(columnValue));
                            }
                        } else {
                            updatesql.append(column.getColumn()).append(" = ").append("?").append(",");
                            objects.add(column.wrapColumnValue(columnValue));
                        }
                    }
                } else {
                    updatesql.append(column.getColumn()).append(" = ").append("?").append(",");
                    objects.add(column.wrapColumnValue(columnValue));
                }
            } else if (column.isId() && column.isUpdatable()) {
                updatesql.append(column.getColumn()).append(" = ").append(column.getSelectColumn()).append(",");
            }
        }
        if (updatesql.length() != 0) {
            sql.append("set ");
            sql.append(updatesql.substring(0, updatesql.length() - 1));
        }
        return sql.toString();
    }

}
