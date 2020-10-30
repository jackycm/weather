package com.eastday.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author eastd
 */
@Data
public class CityBo implements Serializable {

    private String city;
}
