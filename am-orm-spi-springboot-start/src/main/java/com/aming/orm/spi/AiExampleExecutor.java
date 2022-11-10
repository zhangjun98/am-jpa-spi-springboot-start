package com.aming.orm.spi;


import com.aming.orm.spi.pager.AiPageParam;
import com.aming.orm.spi.pager.AiPager;
import com.aming.orm.spi.support.AExample;

import java.util.List;

/**
 * @author ：张俊
 * @date ：Created in 2022/7/2 15:14
 * @description： ExampleExecutor   Example 执行器
 */
public interface AiExampleExecutor {

    /**
     * 根据Example查询总数
     *
     * @param
     * @return
     */
    <T> T selectCountByExample(AExample aExample);

    /**
     * 根据Example删除
     *
     * @param aExample
     * @return
     */
    <T> T deleteByExample(AExample aExample);

    /**
     * 根据Example查询
     *
     * @param aExample
     * @return
     */
     <E> List<E> selectByExample(E type, AExample aExample);

    /**
     * 根据Example查询
     *
     * @param aExample
     * @return
     */
    <E> List<E> selectByExample(AExample aExample);

    /**
     * 根据Example查询 分页查询
     *
     * @param aExample
     * @return
     */
     <E> AiPager<E> selectPageByExample(E type, AExample aExample, AiPageParam pageParam);

    /**
     * 根据Example查询 分页查询
     *
     * @param aExample
     * @return
     */
    <E> AiPager<E> selectPageByExample(AExample aExample, AiPageParam pageParam);

    /**
     * 根据Example查询
     *
     * @param aExample
     * @return
     */
    <E> List<E> selectByExampleAndRowBounds(AExample aExample);

    /**
     * 根据Example更新非null字段
     *
     * @param
     * @return
     */
    <T> T updateByExampleSelective(Object updateEntity, AExample aExample);

    /**
     * 根据Example更新
     *
     * @param
     * @return
     */
    <T> T updateByExample(Object updateEntity, AExample aExample);


}
