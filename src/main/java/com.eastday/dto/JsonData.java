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
public class JsonData implements Serializable {

    @JsonProperty(value="error")
    private Integer error;

    @JsonProperty(value="status")
    private String status;

    @JsonProperty(value="date")
    private String date;

    @JsonProperty(value="results")
    private List<Result> results;

}
