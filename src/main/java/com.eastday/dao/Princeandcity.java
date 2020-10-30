package com.eastday.dao;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author: zengzhewen
 * @create: 2020-04-17 11:00
 **/
@Data
@NoArgsConstructor
@Entity
@Component
@Table(name = "princeandcity")
public class Princeandcity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "parent")
    private Integer parent;

    @Column(name = "province")
    private String province;

    @Column(name = "city")
    private String city;

    @Column(name = "status")
    private Integer status;

    @Column(name = "msg")
    private String msg;

    @Column(name = "update_time")
    private Integer updateTime;
}
