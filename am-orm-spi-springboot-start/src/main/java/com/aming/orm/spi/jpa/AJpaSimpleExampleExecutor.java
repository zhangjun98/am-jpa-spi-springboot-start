package com.aming.orm.spi.jpa;




import com.aming.orm.spi.AiExampleExecutor;
import com.aming.orm.spi.json.AJsonHelper;
import com.aming.orm.spi.log.ALoger;
import com.aming.orm.spi.log.ALogerFactory;
import com.aming.orm.spi.pager.AiPageParam;
import com.aming.orm.spi.pager.AiPager;
import com.aming.orm.spi.pager.support.SimplePager;
import com.aming.orm.spi.support.AExample;
import com.aming.orm.spi.support.ASimpleExampleParser;
import javafx.util.Pair;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * 简单的 jpa Example 查询条件器的 执行器
 */
@Component
public class AJpaSimpleExampleExecutor implements AiExampleExecutor {

    private final ALoger log = ALogerFactory.getLoger(AJpaSimpleExampleExecutor.class);


    @Resource
    EntityManagerProvider entityManagerProvider;

    // 转换器
    ASimpleExampleParser aSimpleExampleParser = new ASimpleExampleParser();

    @Override
    public Number selectCountByExample(AExample aExample) {
        Pair<String, List<Object>> pair = aSimpleExampleParser.selectCountByExample(aExample);
        String key = pair.getKey();
        log.info(" selectCountByExample sql语句:{}", pair.getKey());
        log.info("执行参数:{}", AJsonHelper.toJson(pair.getValue()).toJSONString());
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Query nativeQuery = entityManager.createNativeQuery(key);
        for (int i = 0; i < pair.getValue().size(); i++) {
            nativeQuery.setParameter(i + 1, pair.getValue().get(i));
        }

        return (Number) nativeQuery.getSingleResult();
    }

    @Override
    @Transactional
    public Integer deleteByExample(AExample aExample) {
        Pair<String, List<Object>> pair = aSimpleExampleParser.deleteByExample(aExample);
        String key = pair.getKey();
        log.info(" deleteByExample sql语句:{}", pair.getKey());
        log.info("执行参数:{}", AJsonHelper.toJson(pair.getValue()).toJSONString());
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Query nativeQuery = entityManager.createNativeQuery(key);
        for (int i = 0; i < pair.getValue().size(); i++) {
            nativeQuery.setParameter(i + 1, pair.getValue().get(i));
        }
        return nativeQuery.executeUpdate();
    }

    @Override
    public <E> List<E> selectByExample(AExample aExample) {
        Pair<String, List<Object>> pair = aSimpleExampleParser.selectByExample(aExample);
        String key = pair.getKey();
        log.info(" selectByExample sql语句:{}", pair.getKey());
        log.info("执行参数:{}", AJsonHelper.toJson(pair.getValue()).toJSONString());
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Query nativeQuery = entityManager.createNativeQuery(key);
        nativeQuery.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        for (int i = 0; i < pair.getValue().size(); i++) {
            nativeQuery.setParameter(i + 1, pair.getValue().get(i));
        }
        return nativeQuery.getResultList();
    }

    @Override
    public <E> List<E> selectByExample(E type, AExample aExample) {
        Pair<String, List<Object>> pair = aSimpleExampleParser.selectByExample(aExample);
        String key = pair.getKey();
        log.info(" selectByExample sql语句:{}", pair.getKey());
        log.info("执行参数:{}", AJsonHelper.toJson(pair.getValue()).toJSONString());
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Query nativeQuery = entityManager.createNativeQuery(key, type.getClass());

        for (int i = 0; i < pair.getValue().size(); i++) {
            nativeQuery.setParameter(i + 1, pair.getValue().get(i));
        }
        List<E> resultList = nativeQuery.getResultList();
        return resultList;
    }

    @Override
    public <E> AiPager<E> selectPageByExample(E type, AExample aExample, AiPageParam pageParam) {
        Number count = selectCountByExample(aExample);
        Pair<String, List<Object>> pair = aSimpleExampleParser.selectByExample(aExample);
        String key = pair.getKey();
        log.info(" selectByExample sql语句:{}", pair.getKey());
        log.info("执行参数:{}", AJsonHelper.toJson(pair.getValue()).toJSONString());
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Query nativeQuery = entityManager.createNativeQuery(key, type.getClass());
        for (int i = 0; i < pair.getValue().size(); i++) {
            nativeQuery.setParameter(i + 1, pair.getValue().get(i));
        }
        nativeQuery.setFirstResult(pageParam.pageSize() * (pageParam.pageNum() - 1));
        nativeQuery.setMaxResults(pageParam.pageSize());
        List resultList = nativeQuery.getResultList();
        return new SimplePager<>().put(resultList, count.longValue(), pageParam);
    }

    @Override
    public <E> AiPager<E> selectPageByExample(AExample aExample, AiPageParam pageParam) {
        Number count = selectCountByExample(aExample);
        Pair<String, List<Object>> pair = aSimpleExampleParser.selectByExample(aExample);
        String key = pair.getKey();
        log.info(" selectByExample sql语句:{}", pair.getKey());
        log.info("执行参数:{}", AJsonHelper.toJson(pair.getValue()).toJSONString());
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Query nativeQuery = entityManager.createNativeQuery(key);
        for (int i = 0; i < pair.getValue().size(); i++) {
            nativeQuery.setParameter(i + 1, pair.getValue().get(i));
        }
        nativeQuery.setFirstResult(pageParam.pageSize() * (pageParam.pageNum() - 1));
        nativeQuery.setMaxResults(pageParam.pageSize());
        nativeQuery.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List resultList = nativeQuery.getResultList();
        return new SimplePager<>().put(resultList, count.longValue(), pageParam);
    }

    @Override
    public <E> List<E> selectByExampleAndRowBounds(AExample aExample) {
        return selectByExample(aExample);
    }

    @Override
    @Transactional
    public Integer updateByExampleSelective(Object updateEntity, AExample aExample) {
        Pair<String, List<Object>> pair = aSimpleExampleParser.updateByExampleSelective(updateEntity, aExample);
        String key = pair.getKey();
        log.info(" updateByExampleSelective sql语句:{}", pair.getKey());
        log.info("执行参数:{}", AJsonHelper.toJson(pair.getValue()).toJSONString());
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Query nativeQuery = entityManager.createNativeQuery(key);
        for (int i = 0; i < pair.getValue().size(); i++) {
            nativeQuery.setParameter(i + 1, pair.getValue().get(i));
        }
        return nativeQuery.executeUpdate();
    }

    @Override
    @Transactional
    public Integer updateByExample(Object updateEntity, AExample aExample) {
        Pair<String, List<Object>> pair = aSimpleExampleParser.updateByExample(updateEntity, aExample);
        String key = pair.getKey();
        log.info(" updateByExample sql语句:{}", pair.getKey());
        log.info("执行参数:{}", AJsonHelper.toJson(pair.getValue()).toJSONString());
        EntityManager entityManager = entityManagerProvider.getEntityManager();
        Query nativeQuery = entityManager.createNativeQuery(key);
        for (int i = 0; i < pair.getValue().size(); i++) {
            nativeQuery.setParameter(i + 1, pair.getValue().get(i));
        }
        return nativeQuery.executeUpdate();
    }
}
