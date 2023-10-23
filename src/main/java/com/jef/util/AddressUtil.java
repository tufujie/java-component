package com.jef.util;

import com.google.common.collect.Lists;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 省份、城市地址获取、校验工具
 * @author Jef
 * @create 2018/7/6 12:20
 */
public class AddressUtil {
    private static final String ADDRESS_FILE_PATH = "src/main/java/com/jef/common/AddressList.xml"; // 地址文件路径
    private static final List<String> COUNTRY_REGION = Lists.newArrayList(); // 所有国家名称List
    private static AddressUtil addressUtil;
    private SAXReader reader;
    private Document document;
    private Element rootElement; // 根元素

    /**
     * 初始化
     * @author
     */
    private AddressUtil() {
        reader = new SAXReader(); // 读取
        try {
            document = reader.read(ADDRESS_FILE_PATH);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        rootElement =  document.getRootElement(); // 获得根元素
        Iterator it =  rootElement.elementIterator(); // 初始化所有国家名称列表
        Element ele = null;
        while (it.hasNext()) {
            ele = (Element)it.next();
            COUNTRY_REGION.add(ele.attributeValue("Name"));
        }
    }

    /**
     * 获取所有国家名称
     * @return String[]
     */
    public List<String> getCountry() {
        return COUNTRY_REGION;
    }

    /**
     * 根据国家名获取该国所有省份
     * @param countryName	国家名，从getCountry()从取出
     * @return List<Element>
     */
    private List<Element> provinces(String countryName) {
        Iterator it =  rootElement.elementIterator();
        List<Element> provinces = new ArrayList<Element>();
        while(it.hasNext()) {
            Element ele = (Element)it.next();
            COUNTRY_REGION.add(ele.attributeValue("Name"));
            if (ele.attributeValue("Name").equals(countryName)) {
                provinces = ele.elements();
                break;
            }
        }
        return provinces;
    }

    /**
     *
     * 根据国家名获取该国所有省份
     * @param countryName 国家名，从getCountry()从取出
     * @return List<Element>
     */
    public List<String> getProvinces(String countryName) {
        List<Element> tmp = this.provinces(countryName);
        List<String> list = new ArrayList<String>();
        for (int i=0; i<tmp.size(); i++) {
            list.add(tmp.get(i).attributeValue("Name"));
        }
        return list;
    }

    /**
     *
     * 根据国家名和省份名，获取该省城市名列表
     * @param countryName
     * @param provinceName
     * @return
     */
    private List<Element> cities(String countryName, String provinceName){
        List<Element> provinces =  this.provinces(countryName);
        List<Element> cities = new ArrayList<Element>();
        if (provinces==null || provinces.size()==0) {		//没有这个城市
            return cities;
        }

        for (int i=0; i<provinces.size(); i++) {
            if (provinces.get(i).attributeValue("Name").equals(provinceName)) {
                cities = provinces.get(i).elements();
                break;
            }
        }
        return cities;
    }

    /**
     *
     * 根据国家名和省份名获取城市列表
     * @param countryName
     * @param provinceName
     * @return List<String>
     */
    public List<String> getCities(String countryName, String provinceName) {
        if (provinceName.contains("省")) {
            provinceName = provinceName.replace("省", "");
        }
        List<Element> tmp =  this.cities(countryName, provinceName);
        List<String> cities = new ArrayList<String>();
        for (int i=0; i<tmp.size(); i++){
            cities.add(tmp.get(i).attributeValue("Name"));
        }
        return cities;
    }

    public static AddressUtil getInstance() {
        if (addressUtil ==null) {
            addressUtil = new AddressUtil();
        }
        return addressUtil;
    }
}
