package com.jef.entity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jef
 * @date 2019/1/4
 */
public class BaseJSONVo implements Serializable {

    private static final long serialVersionUID = 90259310221246828L;
    private String result;

    private boolean isSuccess;

    private Object data;

    private String msg;

    private int code;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BaseJSONVo() {
        this.isSuccess = true;
        this.code = 0;
        this.data = new LinkedHashMap<String, Object>();
    }

    public void putData(String key, Object value) {
        ((Map<String, Object>)data).put(key, value);
    }

    public void putDataAll(Map<String,Object> map) {
        ((Map<String, Object>)data).putAll(map);
    }

}