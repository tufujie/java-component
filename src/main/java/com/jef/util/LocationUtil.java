package com.jef.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jef
 * @date 2020/6/13
 */
public class LocationUtil {
    /** 
     * 省
     */
    private static final String PROVINCE_KEY = "province";
    /**
     * 市
     */
    private static final String CITY_KEY = "city";
    /**
     * 区,县
     */
    private static final String COUNTY_KEY = "county";
    /**
     * 镇,街道
     */
    private static final String TOWN_KEY = "town";
    /**
     * 村,详细地址
     */
    private static final String VILLAGE_KEY = "village";


    /**
     * 解析地址
     * 省|市|区,县|镇,街道|村,详细地址
     * @author Jef
     * @date 2020/6/13
     * @param address 详细地址
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     */
    public static Map<String,String> getAddressSplit(String address) {
        Map<String,String> table = new LinkedHashMap<String, String>();
        if (StringUtils.isEmpty(address)) {
            return table;
        }
        String regex = "(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇|.+街道)?(?<village>.*)";
        boolean contaionProvince = false;
        if (address.contains("省")) {
            contaionProvince = true;
            regex = "(?<province>[^省]+自治区|.*?省|.*?行政区|.*?市)(?<city>[^市]+自治州|.*?地区|.*?行政单位|.+盟|市辖区|.*?市|.*?县)(?<county>[^县]+县|.+区|.+市|.+旗|.+海域|.+岛)?(?<town>[^区]+区|.+镇|.+街道)?(?<village>.*)";
        }
        Matcher m = Pattern.compile(regex).matcher(address);
        String province = "", city = "", county, town, village;
        if (m.find()) {
            if (contaionProvince) {
                province = m.group(PROVINCE_KEY);
            }
            province = StringUtils.getStringByDefault(province);
            table.put(PROVINCE_KEY, province);
            city = m.group(CITY_KEY);
            city = StringUtils.getStringByDefault(city);
            table.put(CITY_KEY, city);
            county = m.group(COUNTY_KEY);
            county = StringUtils.getStringByDefault(county);
            table.put(COUNTY_KEY, county);
            town = m.group(TOWN_KEY);
            table.put(TOWN_KEY, StringUtils.getStringByDefault(town));
            village = m.group(VILLAGE_KEY);
            table.put(VILLAGE_KEY, StringUtils.getStringByDefault(village));
            // 特殊处理
            if (StringUtils.isNotEmpty(county) && county.indexOf("区") != county.lastIndexOf("区")) {
                getAddressAfterCity(table, county);
            }
        } else if (StringUtils.isEmpty(city)) {
            String hongkongKey = "香港特别行政区", hongkongKeyTwo = "香港", macaoKey = "澳门特别行政区", macaoKeyTwo = "澳门", tempKey = "";
            if (address.contains(hongkongKey)) {
                tempKey = hongkongKey;
            } else if (address.contains(hongkongKeyTwo)) {
                tempKey = hongkongKeyTwo;
            } else if (address.contains(macaoKey)) {
                tempKey = macaoKey;
            } else if (address.contains(macaoKeyTwo)) {
                tempKey = macaoKeyTwo;
            }
            if (StringUtils.isNotEmpty(tempKey)) {
                int index = address.indexOf(tempKey);
                city = address.substring(0, index + tempKey.length());
                table.put(CITY_KEY, city);
                String countyTemp = address.substring(index + tempKey.length());
                getAddressAfterCity(table, countyTemp);
            }
        }
        return table;
    }

    /**
     * 解析城市之后的数据
     * @author Jef
     * @date 2020/6/13
     * @param addressMap
     * @param county
     * @return void
     */
    private static void getAddressAfterCity(Map<String, String> addressMap, String county) {
        int index = county.indexOf("区");
        String countrySplit = county.substring(0, index + 1);
        addressMap.put(COUNTY_KEY, countrySplit.trim());
        String townSplit = county.substring(index + 1);
        String townKey = "街道";
        if (townSplit.contains(townKey)) {
            index = townSplit.indexOf(townKey);
            String townSplitTwo = townSplit.substring(0, index + townKey.length());
            String villageSplit = townSplit.substring(index + townKey.length());
            addressMap.put(TOWN_KEY, townSplitTwo);
            addressMap.put(VILLAGE_KEY, villageSplit.trim() + addressMap.get(VILLAGE_KEY));
        } else {
            addressMap.put(VILLAGE_KEY, townSplit.trim());
        }
    }
    
    /** 
     * 获取区名
     * @author Jef
     * @date 2020/6/13
     */
    public static String getCounty(Map<String, String> addressMap) {
        return addressMap.get(COUNTY_KEY);
    }

    /**
     * 获取街道名称
     * @author Jef
     * @date 2020/6/13
     * @param
     * @return java.lang.String
     */
    public static String getTown(Map<String, String> addressMap) {
        return addressMap.get(TOWN_KEY);
    }

}