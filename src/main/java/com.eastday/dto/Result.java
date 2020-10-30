package com.eastday.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author eastd
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result implements Serializable {

    @JsonProperty(value="currentCity")
    private String currentCity;

    @JsonProperty(value="pm25")
    private String pm25;

    //@JsonProperty(value="index")
    //private List<Index> index;

    @JsonProperty(value="weather_data")
    private List<WeatherData> weatherData;


}
