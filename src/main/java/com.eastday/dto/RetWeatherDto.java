package com.eastday.dto;

import com.alibaba.fastjson.annotation.JSONType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: zengzhewen
 * @create: 2020-04-17 16:02
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@JSONType(orders={"error","status","date","results"})
public class RetWeatherDto {
    private Integer error;
    private String status;
    private String date;
    private Object results;
}
