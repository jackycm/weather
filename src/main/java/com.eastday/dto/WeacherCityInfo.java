package com.eastday.dto;

import lombok.Data;

/**
 * @author: zengzhewen
 * @create: 2020-04-20 09:32
 **/
@Data
public class WeacherCityInfo {

    private String currentCity;

    private String pm25;

    private Object weather_data;

}
