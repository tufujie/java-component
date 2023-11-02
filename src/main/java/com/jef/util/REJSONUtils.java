package com.jef.util;

import com.jef.entity.BaseJSONVo;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jef.entity.BaseJSONVo;
import com.jef.entity.Page;

/**
 * JSON工具类
 *
 * @author Jef
 * @date 2019/1/4
 */
public class REJSONUtils {
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final String WARN = "warn";

    /**
     * app 返回结果集
     * @param data
     * @param code
     * @param msg
     * @return BaseJSONVo
     */
    public static BaseJSONVo success(Object data, int code, String msg) {
        return getAppBaseJSONVo(SUCCESS,data,true,code,msg);
    }

    /**
     * 返回结果集 分页-成功
     * @param list
     * @param code
     * @param msg
     * @return BaseJSONVo
     */
    public static BaseJSONVo pageSuccess(List list, int code, String msg) {
        return getPageBaseJSONVo(SUCCESS, list, true, code, msg);
    }
    /**
     * 返回结果集 分页-成功
     * @param list
     * @param code
     * @param msg
     * @return BaseJSONVo
     */
    public static BaseJSONVo pageSuccess(List list,int pageSize,int code,String msg) {
        if (pageSize<=0){
            Map<String, Object> pageMap=new HashMap<String, Object>();
            pageMap.put("rows", list);
            pageMap.put("current", 1);
            pageMap.put("rowCount",list.size());
            pageMap.put("pages",1);
            pageMap.put("total", list.size());
            return getAppBaseJSONVo(SUCCESS,pageMap,true,code,msg);
        }else {
            return getPageBaseJSONVo(SUCCESS, list, true, code, msg);
        }
    }

    /**
     * app 返回结果集
     * @param code
     * @param msg
     * @return BaseJSONVo
     */
    public static BaseJSONVo failure(int code,String msg) {
        return getAppBaseJSONVo(ERROR,"",false,code,msg);
    }

    /**
     * 返回结果集 分页 --失败
     * @param code
     * @param msg
     * @return BaseJSONVo
     */
    public static BaseJSONVo pageFailure(int code,String msg) {
        return getPageBaseJSONVo(ERROR,null,false, code, msg);
    }

    public static BaseJSONVo failureForData(Object data,int code,String msg) {
        return getAppBaseJSONVo(ERROR,data,false,code,msg);
    }

    /**
     * 封装APP返回结果信息
     * @param type
     * @param data
     * @param isSuccess
     * @param code
     * @param msg
     * @return BaseJSONVo
     */
    public static BaseJSONVo getAppBaseJSONVo(String type,Object data, boolean isSuccess, int code, Object msg) {
        BaseJSONVo reData = new BaseJSONVo();
        reData.setData(data);
        reData.setResult(type);
        reData.setSuccess(isSuccess);
        reData.setCode(code);
        reData.setMsg(msg == null ? "" : msg.toString());
        return reData;
    }

    /**
     * 封装分页返回结果信息
     * @param type
     * @param list
     * @param isSuccess
     * @param code
     * @param msg
     * @return BaseJSONVo
     */
    public static BaseJSONVo getPageBaseJSONVo(String type, List<T> list, boolean isSuccess, int code, Object msg) {
        BaseJSONVo data = new BaseJSONVo();
        data.setData(getPageMap(list));
        data.setResult(type);
        data.setSuccess(isSuccess);
        data.setCode(code);
        data.setMsg(msg == null ? "" : msg.toString());
        return data;
    }

    /**
     * 封装返回结果的分页信息
     * @param list
     * @return Map<String, Object>
     */
    public static Map<String, Object> getPageMap(List<T> list) {
        Map<String, Object> pageMap=new HashMap<String, Object>();
        if(list != null && list.size() != 0){
            pageMap.put("rows", list);
            pageMap.put("current", ((Page<T>) list).getPageNum());
            pageMap.put("rowCount",((Page<T>) list).getPageSize());
            pageMap.put("pages", ((Page<T>) list).getPages());
            pageMap.put("total", ((Page<T>) list).getTotal());
        }else{
            pageMap.put("rows", new ArrayList<T>());
            pageMap.put("current", 1);
            pageMap.put("rowCount",10);
            pageMap.put("pages", 0);
            pageMap.put("total", 0);
        }
        return pageMap;
    }


}