package com.spirng_jpa.jpa_demo;

import com.alibaba.fastjson.JSONObject;
import com.aming.orm.spi.jpa.AJpaSimpleExampleExecutor;
import com.aming.orm.spi.json.AJsonHelper;
import com.aming.orm.spi.json.AiJSON;
import com.aming.orm.spi.support.AExample;
import com.spirng_jpa.jpa_demo.pojo.Login_Info;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ：张俊
 * @date ：Created in 2022/7/1 13:53
 * @description： TODO
 */
@SpringBootTest
public class ExampleTest {

    @Resource
    AJpaSimpleExampleExecutor aJpaSimpleExampleExecutor;

    @Test
    public void selectAndUpdate() {
        AExample gwExample1 = new AExample(Login_Info.class);
        AExample.Criteria criteria2 = gwExample1.createCriteria();
        List<String> prefixOverrides = new ArrayList<>();
        prefixOverrides.add("1522418585261957123");
        prefixOverrides.add("1522418585261957124");
        prefixOverrides.add("1522418585266151426");
        criteria2.andIn("id", prefixOverrides);
        AExample.Criteria criteria1 = gwExample1.createCriteria();
        gwExample1.or(criteria1);
        Login_Info login_info = new Login_Info();
        login_info.setThing_message("更新更新222");
        List<Object> objects = aJpaSimpleExampleExecutor.selectByExample(gwExample1);
        Object o = JSONObject.toJSON(objects);
        System.out.println(o);
        Integer integer = aJpaSimpleExampleExecutor.updateByExampleSelective(login_info, gwExample1);
        System.out.println(integer);
    }

    @Test
    public void selectCount() {
        AExample gwExample1 = new AExample(Login_Info.class);
        AExample.Criteria criteria2 = gwExample1.createCriteria();
        List<String> prefixOverrides = new ArrayList<>();
        prefixOverrides.add("1522418585261957123");
        prefixOverrides.add("1522418585261957124");
        prefixOverrides.add("1522418585266151426");
        criteria2.andIn("id", prefixOverrides);
        AExample.Criteria criteria1 = gwExample1.createCriteria();
        gwExample1.or(criteria1);
        System.out.println(aJpaSimpleExampleExecutor.selectCountByExample(gwExample1));
    }



    @Test
    public void deleteByExample() {
        AExample gwExample1 = new AExample(Login_Info.class);
        AExample.Criteria criteria2 = gwExample1.createCriteria();
        Login_Info login_info = new Login_Info();
        login_info.setThing_message("更新更新222");
        criteria2.andEqualTo(login_info);
        Integer integer = aJpaSimpleExampleExecutor.deleteByExample(gwExample1);
        System.out.println(integer);
    }

    @Test
    public void selectByExample1() {
        AExample gwExample1 = new AExample(Login_Info.class);
        AExample.Criteria criteria2 = gwExample1.createCriteria();
        List<String> prefixOverrides = new ArrayList<>();
        prefixOverrides.add("1522418585261957123");
        prefixOverrides.add("1522418585261957124");
        prefixOverrides.add("1522418585266151426");
        criteria2.andIn("id", prefixOverrides);
        AExample.Criteria criteria1 = gwExample1.createCriteria();
        gwExample1.or(criteria1);
  ;
        List<Object> objects = aJpaSimpleExampleExecutor.selectByExample(gwExample1);
        System.out.println(objects);
    }

    @Test
    public void selectByExample() {
        AExample gwExample1 = new AExample(Login_Info.class);
        AExample.Criteria criteria2 = gwExample1.createCriteria();
        List<String> prefixOverrides = new ArrayList<>();
        prefixOverrides.add("1522418585261957122");
        prefixOverrides.add("1522418585266151427");
        prefixOverrides.add("1522418585266151428");
        criteria2.andIn("id", prefixOverrides);
        gwExample1.or(criteria2);
        List<Login_Info> login_infos = aJpaSimpleExampleExecutor.selectByExample(new Login_Info(), gwExample1);
        for (Login_Info login_info : login_infos) {
            String s = AJsonHelper.toJson(login_info).toJSONString();
            System.out.println(s);
        }
    }

    @Test
    public void selectPageByExample() {

    }

    @Test
    public void selectPageByExample1() {

    }

    @Test
    public void selectByExampleAndRowBounds() {

    }

    @Test
    public void updateByExampleSelective() {

    }

    @Test
    public void updateByExample() {

    }
}
