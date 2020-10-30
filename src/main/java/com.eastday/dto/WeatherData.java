package com.eastday.dto;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.Data;

/**
 * @author: zengzhewen
 * @create: 2020-04-17 13:53
 **/
@Data
@JSONType(orders={"date","dayPictureUrl","nightPictureUrl","weather","wind","temperature"})
public class WeatherData {

    private String date;

    private String dayPictureUrl;

    private String nightPictureUrl;

    private String weather;

    private String wind;

    private String temperature;
}
