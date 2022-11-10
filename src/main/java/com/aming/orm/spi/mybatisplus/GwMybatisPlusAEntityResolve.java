package com.aming.orm.spi.mybatisplus;

import com.aming.orm.spi.AEntityResolve;
import com.aming.orm.spi.entity.AEntityTable;

/**
 * @author ：张俊
 * @date ：Created in 2022/6/30 15:24
 * @description： TODO
 */
public class GwMybatisPlusAEntityResolve implements AEntityResolve {
    @Override
    public AEntityTable resolveEntity(Class<?> entityClass) {
        return null;
    }

    @Override
    public AEntityTable resolveEntity(Object entityidentification) {
        return null;
    }


}
