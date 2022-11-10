package com.aming.orm.spi;

/**
 * @author ：zhangjun
 * @date ：Created in 2022/6/30 15:14
 * @description： TODO
 */

import com.aming.orm.spi.entity.AEntityTable;

/**
 * 解析实体类接口
 *
 * @author liuzh
 */
public interface AEntityResolve {

    /**
     * 解析类为 EntityTable
     *
     * @param entityClass
     * @param
     * @return
     */
    AEntityTable resolveEntity(Class<?> entityClass);

    /**
     * 解析类为 EntityTable
     *
     * @param entityidentification 实体类标识 用于拓展
     * @param
     * @return
     */
    AEntityTable resolveEntity(Object entityidentification);


}
