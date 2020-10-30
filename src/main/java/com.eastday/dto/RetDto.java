package com.eastday.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RetDto {

    private Boolean success;
    private Integer code;
    private String message;
    private Object data;



}
