package com.jef.util;

import org.apache.poi.ss.usermodel.Cell;

/**
 * @author Jef
 * @date 2022/1/15
 */
public class ExcelUtil {

    /**
     * 从表格获取值
     *
     * @return java.lang.String
     * @author Jef
     * @date 2022/1/6
     */
    public static String getValueFromCell(Cell cell) {
        String cellStringValue = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    cellStringValue += cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    cellStringValue += NumberUtils.formatByPattern(cell.getNumericCellValue(), "############.############");
                    break;
                case Cell.CELL_TYPE_ERROR:
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_STRING:
                    cellStringValue += cell.getStringCellValue().trim();
                    //.replaceAll("[\\t\\n\\r\"\'\\\\]", "") 天骄客户名称带有 \ , 取消这个字符的替换
                    cellStringValue = cellStringValue.replaceAll("[\\t\\n\\r]", "");

                    break;
                case Cell.CELL_TYPE_FORMULA:
                    try {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                    } catch (Exception e) {
					/*
					cell.setCellType(Cell.CELL_TYPE_STRING); 这句可能遇到空指针,做异常捕捉
					生产遇到过如下公式报了空指针: =MID(C20, 1, 1)
					 */
                    }
                    cellStringValue += cell.getRichStringCellValue();
                    cellStringValue = cellStringValue.replaceAll("[\\t\\n\\r\"\']", "");
                    break;
                default:
                    if (cell.getStringCellValue() != null && !cell.getStringCellValue().equals("")) {
                        cellStringValue += cell.getStringCellValue();
                    } else {
                        cellStringValue += (int) cell.getNumericCellValue();
                    }
                    cellStringValue = "";
                    break;
            }
        }
        return cellStringValue.trim();
    }
    
}