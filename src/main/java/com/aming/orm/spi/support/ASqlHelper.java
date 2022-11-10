package com.aming.orm.spi.support;

/**
 * @author ：张俊
 * @date ：Created in 2022/6/30 19:00
 * @description： 拼常用SQL的工具类
 */

import com.aming.orm.spi.entity.AEntityColumn;

import javafx.util.Pair;
import org.springframework.util.StringUtils;

import java.util.*;

public class ASqlHelper {


    /**
     * 获取所有查询列，如id,name,code...
     *
     * @param entityClass
     * @return
     */
    public static String getAllColumns(Class<?> entityClass) {
        Set<AEntityColumn> columnSet = AEntityHelper.getColumns(entityClass);
        StringBuilder sql = new StringBuilder();
        for (AEntityColumn AEntityColumn : columnSet) {
            sql.append(AEntityColumn.getSelectColumn()).append(",");
        }
        return sql.substring(0, sql.length() - 1);
    }

    /**
     * select xxx,xxx...
     *
     * @param entityClass
     * @return
     */
    public static String selectAllColumns(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(getAllColumns(entityClass));
        sql.append(" ");
        return sql.toString();
    }

    /**
     * select count(x)
     *
     * @param entityClass
     * @return
     */
    public static String selectCount(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        Set<AEntityColumn> pkColumns = AEntityHelper.getPKColumns(entityClass);
        if (pkColumns.size() == 1) {
            sql.append("COUNT(").append(pkColumns.iterator().next().getColumn()).append(") ");
        } else {
            sql.append("COUNT(*) ");
        }
        return sql.toString();
    }

    /**
     * select case when count(x) > 0 then 1 else 0 end
     *
     * @param entityClass
     * @return
     */
    public static String selectCountExists(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CASE WHEN ");
        Set<AEntityColumn> pkColumns = AEntityHelper.getPKColumns(entityClass);
        if (pkColumns.size() == 1) {
            sql.append("COUNT(").append(pkColumns.iterator().next().getColumn()).append(") ");
        } else {
            sql.append("COUNT(*) ");
        }
        sql.append(" > 0 THEN 1 ELSE 0 END AS result ");
        return sql.toString();
    }

    /**
     * from tableName - 动态表名
     *
     * @param aExample
     * @param defaultTableName
     * @return
     */
    public static String fromTable(AExample aExample, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" FROM ");
        sql.append(defaultTableName);
        sql.append(" ");
        return sql.toString();
    }


    /**
     * update tableName - 动态表名
     *
     * @param aExample
     * @param defaultTableName 默认表名
     * @return
     */
    public static String updateTable(AExample aExample, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append(aExample.tableName);
        sql.append(" ");
        return sql.toString();
    }


    public static String deleteFromTable(AExample aExample, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append(aExample.tableName);
        sql.append(" ");
        return sql.toString();
    }


    /**
     * 获取默认的orderBy，通过注解设置的
     *
     * @param entityClass
     * @return
     */
    public static String orderByDefault(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        String orderByClause = AEntityHelper.getOrderByClause(entityClass);
        if (orderByClause.length() > 0) {
            sql.append(" ORDER BY ");
            sql.append(orderByClause);
        }
        return sql.toString();
    }

    /**
     * example支持查询指定列时
     *
     * @return
     */
    public static String exampleSelectColumns(AExample aExample) {
        StringBuilder sql = new StringBuilder();

        if (aExample.getSelectColumns() != null && aExample.getSelectColumns().size() > 0) {
            sql.append(getSelectColumn(null, aExample.getSelectColumns()));
        } else {
            //不支持指定列的时候查询全部列
            sql.append(getSelectColumn(aExample, null));

        }
        return sql.toString();
    }

    public static String getSelectColumn(AExample aExample, Set<String> selectColumns) {
        StringBuilder allColumn = new StringBuilder();
        if (aExample != null) {
            Set<AEntityColumn> entityClassColumns = aExample.table.getEntityClassColumns();
            for (AEntityColumn entityClassColumn : entityClassColumns) {
                allColumn.append(entityClassColumn.getSelectColumn()).append(",");
            }
            return allColumn.substring(0, allColumn.length() - 1);
        }
        if (selectColumns != null) {
            for (String column : selectColumns) {
                allColumn.append(" ").append(column).append(", ");
            }
            allColumn.deleteCharAt(allColumn.length());
            allColumn.deleteCharAt(allColumn.length());
            return allColumn.toString();
        }
        return null;
    }

    /**
     * example支持查询指定列时
     *
     * @return
     */
    public static String exampleCountColumn(AExample aExample, List<Object> contidion) {
        StringBuilder sql = new StringBuilder();

        if (!StringUtils.isEmpty(aExample.getCountColumn())) {
            if (aExample.distinct) {
                sql.append(" distinct ");
            }
            sql.append(aExample.countColumn);
        } else {
            sql.append("COUNT(*)");
        }

        return sql.toString();
    }

    /**
     * example查询中的orderBy条件，会判断默认orderBy
     *
     * @return
     */
    public static String exampleOrderBy(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"orderByClause != null\">");
        sql.append("order by ${orderByClause}");
        sql.append("</if>");
        String orderByClause = AEntityHelper.getOrderByClause(entityClass);
        if (orderByClause.length() > 0) {
            sql.append("<if test=\"orderByClause == null\">");
            sql.append("ORDER BY " + orderByClause);
            sql.append("</if>");
        }
        return sql.toString();
    }

    public static String exampleOrderBy(AExample aExample) {
        StringBuilder sql = new StringBuilder();
        if (aExample.orderByClause != null) {
            sql.append("order by ");
            sql.append(aExample.orderByClause);
        }
        return sql.toString();
    }


    /**
     * example 支持 for update
     *
     * @return
     */
    public static String exampleForUpdate(AExample aExample) {
        StringBuilder sql = new StringBuilder();
        if (aExample.isForUpdate()) {
            sql.append("FOR UPDATE");
        }

        return sql.toString();
    }


    /**
     * Example查询中的where结构，用于只有一个Example参数时
     *
     * @param aExample
     * @param contidion 查询条件
     * @return
     */
    public static Pair<String, List<Object>> exampleWhereClause(AExample aExample, List<Object> contidion) {
        Pair<String, List<Object>> returnpair = null;
        StringBuilder sql = new StringBuilder();
        if (aExample == null) {
            returnpair = new Pair<>(sql.toString(), contidion);
            return returnpair;
        }
        List<String> prefixOverrides = new ArrayList<>();
        prefixOverrides.add("and");
        prefixOverrides.add("or");
        //  对应条件中的 子sql
        if (aExample.oredCriteria.size() > 0) {
            sql.append(" where ");
        }

        StringBuilder whereSql = new StringBuilder(); //// and ( and  abc = 1 and bcd = 2 ) and  ( and  abc = 1 and bcd = 2 )


        for (AExample.Criteria oredCriterion : aExample.oredCriteria) {
            StringBuilder oredCriterionSql = new StringBuilder();  //and ( and  abc = 1  and bcd = 2 )
            // and ( and  abc = 1 and bcd = 2 )
            if (oredCriterion.isValid()) {
                oredCriterionSql.append(oredCriterion.getAndOr());
                // 子查询条件拼接
                StringBuilder criterionSql = new StringBuilder();  //// 循环走完  and  abc = 1  and bcd = 2
                for (AExample.Criterion criterion : oredCriterion.criteria) {

                    if (criterion.isNoValue()) {
                        criterionSql.append(criterion.getAndOr()).append(" ");
                        criterionSql.append(criterion.getCondition()).append(" ");
                    }

                    if (criterion.isSingleValue()) {
                        criterionSql.append(criterion.getAndOr()).append(" ");
                        criterionSql.append(criterion.getCondition());
                        criterionSql.append(" ? "); // 预编译占位符
                        contidion.add(criterion.getValue());
                    }
                    if (criterion.isBetweenValue()) {
                        criterionSql.append(criterion.getAndOr()).append(" ");
                        ;
                        criterionSql.append(criterion.getCondition());
                        criterionSql.append("( ? ");
                        criterionSql.append(" and ");
                        criterionSql.append(" ? )");
                        contidion.add(criterion.getValue());
                        contidion.add(criterion.getSecondValue());
                    }
                    if (criterion.isListValue()) {
                        criterionSql.append(criterion.getAndOr()).append(" ").append(criterion.getCondition());
                        StringBuilder listValueSql = new StringBuilder();
                        Collection value = (Collection) criterion.getValue();
                        listValueSql.append(" ( ");
                        for (Object o : value) {
                            listValueSql.append(" ?,");
                            contidion.add(o);
                        }
                        String substring = listValueSql.substring(0, listValueSql.length() - 1);
                        criterionSql.append(substring).append(" )");
                    }

                }

                applyPrefix(criterionSql, criterionSql.toString().toUpperCase(Locale.ENGLISH), prefixOverrides, "(");
                applySuffix(criterionSql, criterionSql.toString().toUpperCase(Locale.ENGLISH), null, " ) ");
                oredCriterionSql.append(criterionSql);
            }
            whereSql.append(oredCriterionSql);
        }
        String trimmedUppercaseSql = whereSql.toString().toUpperCase(Locale.ENGLISH);
        if (trimmedUppercaseSql.length() > 0) {
            applyPrefix(whereSql, trimmedUppercaseSql, prefixOverrides, null);

        }
        sql.append(whereSql);
        returnpair = new Pair<>(sql.toString(), contidion);
        return returnpair;
    }


    public static Pair<String, List<Object>> updateByExampleWhereClause(AExample aExample, List<Object> contidion) {
        Pair<String, List<Object>> returnpair = null;
        StringBuilder sql = new StringBuilder();
        if (aExample == null) {
            returnpair = new Pair<>(sql.toString(), contidion);
            return returnpair;
        }
        List<String> prefixOverrides = new ArrayList<>();
        prefixOverrides.add("and");
        prefixOverrides.add("or");
        //  对应条件中的 子sql
        if (aExample.oredCriteria.size() > 0) {
            sql.append(" where ");
        }

        StringBuilder whereSql = new StringBuilder(); //// and ( and  abc = 1 and bcd = 2 ) and  ( and  abc = 1 and bcd = 2 )


        for (AExample.Criteria oredCriterion : aExample.oredCriteria) {
            StringBuilder oredCriterionSql = new StringBuilder();  //and ( and  abc = 1  and bcd = 2 )
            // and ( and  abc = 1 and bcd = 2 )
            if (oredCriterion.isValid()) {
                oredCriterionSql.append(oredCriterion.getAndOr());
                // 子查询条件拼接
                StringBuilder criterionSql = new StringBuilder();  //// 循环走完  and  abc = 1  and bcd = 2
                for (AExample.Criterion criterion : oredCriterion.criteria) {

                    if (criterion.isNoValue()) {
                        criterionSql.append(" ").append(criterion.getAndOr()).append(" ");
                        criterionSql.append(criterion.getCondition()).append(" ");
                    }

                    if (criterion.isSingleValue()) {
                        criterionSql.append(criterion.getAndOr()).append(" ");
                        criterionSql.append(criterion.getCondition());
                        criterionSql.append(" ? "); // 预编译占位符
                        contidion.add(criterion.getValue());
                    }
                    if (criterion.isBetweenValue()) {
                        criterionSql.append(criterion.getAndOr()).append(" ");
                        ;
                        criterionSql.append(criterion.getCondition());
                        criterionSql.append("( ? ");
                        criterionSql.append(" and ");
                        criterionSql.append(" ? )");
                        contidion.add(criterion.getValue());
                        contidion.add(criterion.getSecondValue());
                    }
                    if (criterion.isListValue()) {
                        criterionSql.append(criterion.getAndOr()).append(" ").append(criterion.getCondition());
                        StringBuilder listValueSql = new StringBuilder();
                        Collection value = (Collection) criterion.getValue();
                        listValueSql.append(" ( ");
                        for (Object o : value) {
                            listValueSql.append(" ?,");
                            contidion.add(o);
                        }
                        String substring = listValueSql.substring(0, listValueSql.length() - 1);
                        criterionSql.append(substring).append(" )");
                    }

                }

                applyPrefix(criterionSql, criterionSql.toString().toUpperCase(Locale.ENGLISH), prefixOverrides, "(");
                applySuffix(criterionSql, criterionSql.toString().toUpperCase(Locale.ENGLISH), null, " ) ");
                oredCriterionSql.append(criterionSql);
            }
            whereSql.append(oredCriterionSql);
        }
        String trimmedUppercaseSql = whereSql.toString().toUpperCase(Locale.ENGLISH);
        if (trimmedUppercaseSql.length() > 0) {
            applyPrefix(whereSql, trimmedUppercaseSql, prefixOverrides, null);

        }
        sql.append(whereSql);
        returnpair = new Pair<>(sql.toString(), contidion);
        return returnpair;
    }


    /**
     * from mybatis whereSqlNode
     *
     * @param sql                 需要修剪的sql
     * @param trimmedUppercaseSql 原始sql
     * @param prefixesToOverride  需要修剪的 前缀
     * @param prefix              前缀
     */
    private static void applyPrefix(StringBuilder sql, String trimmedUppercaseSql, List<String> prefixesToOverride, String prefix) {
        if (prefixesToOverride != null) {
            for (String toRemove : prefixesToOverride) {
                String trim = toRemove.trim();
                if (trimmedUppercaseSql.startsWith(trim.toUpperCase())) {
                    sql.delete(0, trim.length());
                    break;
                }
            }
        }
        if (prefix != null) {
            sql.insert(0, " ");
            sql.insert(0, prefix);
        }
    }

    private static void applySuffix(StringBuilder sql, String trimmedUppercaseSql, List<String> suffixesToOverride, String suffix) {
        if (suffixesToOverride != null) {
            for (String toRemove : suffixesToOverride) {
                String trim = toRemove.trim().toUpperCase();
                if (trimmedUppercaseSql.endsWith(trim) || trimmedUppercaseSql.endsWith(trim.trim())) {
                    int start = sql.length() - toRemove.trim().length();
                    int end = sql.length();
                    sql.delete(start, end);
                    break;
                }
            }
        }
        if (suffix != null) {
            sql.append(" ");
            sql.append(suffix);
        }
    }

    /**
     * 检查 paremeter 对象中指定的 fields 是否全是 null，如果是则抛出异常
     *
     * @param aExample
     * @return
     */
    public static boolean exampleHasAtLeastOneCriteriaCheck(AExample aExample) {
        if (aExample != null) {
            try {
                List<AExample.Criteria> criteriaList = ((AExample) aExample).getOredCriteria();
                if (criteriaList != null && criteriaList.size() > 0) {
                    return true;
                }
            } catch (Exception e) {
                throw new RuntimeException("不允许不安全的全部删除操作", e);
            }
        }
        throw new RuntimeException("不允许不安全的全部删除操作");
    }
}

