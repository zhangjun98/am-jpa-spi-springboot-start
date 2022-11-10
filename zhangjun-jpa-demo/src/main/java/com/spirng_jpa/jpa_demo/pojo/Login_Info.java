package com.spirng_jpa.jpa_demo.pojo;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ：张俊
 * @date ：Created in 2022/4/29 17:06
 * @description： TODO
 */
@Entity
//用来对应数据库中那张表，如果不写就代表user表
@Table(name = "login_info")
@Data
public class Login_Info  {
    /**
     * 主键
     */
    @Id
    private String id;
    /**
     * 线程id
     */
    @Column
    private String theadId;

    /**
     * 开始时间
     */
    @Column
    private String startTime;
    /**
     * 结束时间
     */
    @Column
    private String endTime;

    /**
     * 总时间
     */
    @Column
    private String allTime;

    /**
     * 测试因子  sql ，或者接口名称
     */
    @Column
    private String thing;

    /**
     * 测试因子  sql ，或者接口名称
     */
    @Column
    private String thing_message;

    /**
     * 返回消息
     */
    @Column
    private String return_message;


}
