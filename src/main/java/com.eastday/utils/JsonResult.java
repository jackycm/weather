package com.eastday.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author eastd
 */
public class JsonResult {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private Integer status;

    private String message;

    private Object data;

    @JsonIgnore
    private String ok;

    public static JsonResult build(Integer status, String message, Object data) {
        return new JsonResult(status, message, data);
    }

    public static JsonResult build(Integer status, String message, Object data, String ok) {
        return new JsonResult(status, message, data, ok);
    }


    
    public static JsonResult ok(Object data) {
        return new JsonResult(data);
    }

    public static JsonResult ok() {
        return new JsonResult(null);
    }
    
    public static JsonResult errorMsg(String msg) {
        return new JsonResult(500, msg, null);
    }

    public static JsonResult errorMsg(Integer status,String message) {

        return new JsonResult(status,message);
    }
    
    public static JsonResult errorMap(Object data) {

        return new JsonResult(501, "error", data);
    }
    
    public static JsonResult errorTokenMsg(String message) {

        return new JsonResult(502, message, null);
    }
    
    public static JsonResult errorException(String message) {

        return new JsonResult(555, message, null);
    }

    public JsonResult() {

    }

    public JsonResult(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public JsonResult(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public JsonResult(Integer status, String message, Object data, String ok) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.ok = ok;
    }

    public JsonResult(Object data) {
        this.status = 200;
        this.message = "OK";
        this.data = data;
    }

    public Boolean isOk() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

	public String getOk() {
		return ok;
	}

	public void setOk(String ok) {
		this.ok = ok;
	}

}
