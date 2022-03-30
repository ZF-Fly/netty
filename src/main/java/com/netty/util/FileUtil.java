package com.netty.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.DateFormat;
import java.util.*;


/**
 * 2019-01-26 操作文件相关工具类
 *
 * @author ulin
 */
public class FileUtil {

    /**
     * 解析文本文件 行解析
     *
     * @return
     */
    public static ArrayList<String> readFile(BufferedReader reader) {
        ArrayList<String> fileContent = new ArrayList<String>();
        try {
            String row = null;
            while ((row = reader.readLine()) != null) {
                fileContent.add(row);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }

    /**
     * 解析Excel文件
     */
    public static List<List<String>> readXlsx(InputStream inputStream) {

        List<List<String>> result = new ArrayList<List<String>>();
        XSSFWorkbook xssfWorkbook = null;
        try {

            xssfWorkbook = new XSSFWorkbook(inputStream);
            //循环每一页，并处理当前循环页
            for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;

                }
                //处理当前页，循环读取每一行
                for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);

                    int minColIx = xssfRow.getFirstCellNum();
                    int maxColIx = xssfRow.getLastCellNum();
                    List<String> rowList = new ArrayList<String>();
                    //遍历该行获取处理每个cell元素
                    for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                        XSSFCell cell = xssfRow.getCell(colIx);
                        if (cell == null) {
                            continue;
                        }

                        rowList.add(getStringVal(cell));
                    }

                    result.add(rowList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeInputIo(inputStream);
            if (xssfWorkbook != null) {

                try {
                    xssfWorkbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    xssfWorkbook = null;
                }
            }
        }
        return result;
    }
    /**
     * 解析Excel文件 获取标题头
     */
    public static List<String> readXlsxTitle(InputStream is){
        XSSFWorkbook xssfWorkbook = null;
        List<String> rowList = new ArrayList<String>();
        try {

            xssfWorkbook = new XSSFWorkbook(is);

            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);

            XSSFRow xssfRow = xssfSheet.getRow(0);
            if (xssfRow == null) {
                return rowList;
            }
            int minColIx = xssfRow.getFirstCellNum();
            int maxColIx = xssfRow.getLastCellNum();
            //遍历该行获取处理每个cell元素
            for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                XSSFCell cell = xssfRow.getCell(colIx);
                if (cell == null) {
                    rowList.add(colIx + "");
                    continue;
                }
                String stringVal = getStringVal(cell);
                rowList.add(stringVal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeInputIo(is);
        }
        return rowList;
    }

    /**
     * 解析Excel文件
     */
    public static JSONArray readXlsx(InputStream is, Boolean haveTitle) {

        JSONArray resultArray = new JSONArray();
        XSSFWorkbook xssfWorkbook = null;
        try {
            xssfWorkbook = new XSSFWorkbook(is);
            //循环每一页，并处理当前循环页
            for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;

                }
                //处理当前页，循环读取每一行
                int rowNum = 0;
                List<String> rowList = new ArrayList<String>();
                if (haveTitle) {
                    XSSFRow xssfRow = xssfSheet.getRow(0);
                    if (xssfRow == null) {
                        continue;
                    }
                    int minColIx = xssfRow.getFirstCellNum();
                    int maxColIx = xssfRow.getLastCellNum();
                    //遍历该行获取处理每个cell元素
                    for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                        XSSFCell cell = xssfRow.getCell(colIx);
                        if (cell == null) {
                            rowList.add(colIx + "");
                            continue;
                        }
                        String stringVal = getStringVal(cell);
                        rowList.add(stringVal);
                    }
                    rowNum = 1;
                } else {
                    rowNum = 0;
                }
                for (; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if (xssfRow == null) {
                        continue;
                    }
                    int minColIx = xssfRow.getFirstCellNum();
                    int maxColIx = xssfRow.getLastCellNum();
                    JSONObject rowJson = new JSONObject();
                    //遍历该行获取处理每个cell元素
                    for (int colIx = minColIx; colIx < maxColIx; colIx++) {
                        XSSFCell cell = xssfRow.getCell(colIx);
                        if (cell == null) {
                            continue;
                        }
                        String stringVal = getStringVal(cell);
                        String key ;
                        if(colIx<rowList.size() ){

                            key = rowList.get(colIx);
                        }else {
                            key = colIx+"";
                        }
                        rowJson.put(key, stringVal);
                    }

                    resultArray.add(rowJson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeInputIo(is);
        }
        return resultArray;
    }

    public static void closeInputIo(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                inputStream = null;
            }
        }

    }

    public static void closeOutputIo(OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                outputStream = null;
            }
        }

    }


    private static String getStringVal(XSSFCell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue() ? "TRUE" : "FALSE";
            case Cell.CELL_TYPE_FORMULA://公式格式
                return cell.getCellFormula();
            case Cell.CELL_TYPE_NUMERIC://数字格式
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    return date.toString();
                }
                cell.setCellType(Cell.CELL_TYPE_STRING);
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            default:
                return "????";
        }
    }

    /**
     * Export excel file
     * @param sheetName sheet name
     * @param title     title name
     * @param values    content
     * @param workbook  HSSFWorkbook
     * @return
     */
    public static XSSFWorkbook getXSSFWorkbook(String sheetName, String[] title, String[][] values, XSSFWorkbook workbook) {
        if (workbook == null) {
            workbook = new XSSFWorkbook();
        }
        XSSFSheet sheet = workbook.createSheet(sheetName);
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        XSSFCell cell;
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return workbook;
    }

}
