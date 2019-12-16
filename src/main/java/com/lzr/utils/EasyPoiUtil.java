package com.lzr.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class EasyPoiUtil {
    /**
     * 功能描述：复杂导出Excel，包括文件名以及表名。创建表头
     *
     * @param list           导出的实体类
     * @param title          表头名称
     * @param sheetName      sheet表名
     * @param pojoClass      映射的实体类
     * @param isCreateHeader 是否创建表头
     * @param fileName
     * @param response
     * @return
     * @date 2018/8/22 13:07
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, boolean isCreateHeader, HttpServletResponse response) throws Exception{
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);
    }


    /**
     * 功能描述：复杂导出Excel，包括文件名以及表名,不创建表头
     *
     * @param list      导出的实体类
     * @param title     表头名称
     * @param sheetName sheet表名
     * @param pojoClass 映射的实体类
     * @param fileName
     * @param response
     * @return
     * @date 2018/8/22 13:07
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response) throws Exception{
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
    }


    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response, MyExcelHandler handler)throws Exception {
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName), handler);
    }

    /**
     * 功能描述：Map 集合导出
     *
     * @param list     实体集合
     * @param fileName 导出的文件名称
     * @param response
     * @return
     * @date 2018/8/22 16:14
     */
    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response) throws Exception{
        defaultExport(list, fileName, response);
    }

    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams) throws Exception{
        defaultExport(list, pojoClass, fileName, response, exportParams, null);
    }

    /**
     * 功能描述：默认导出方法
     *
     * @param list         导出的实体集合
     * @param fileName     导出的文件名
     * @param pojoClass    pojo实体
     * @param exportParams ExportParams封装实体
     * @param response
     * @return
     * @date 2018/8/22 15:33
     */
    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams, MyExcelHandler handler) throws Exception{
        if (handler != null) {
            exportParams.setDataHandler(handler);
        }
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        if (workbook != null) {
            downLoadExcel(fileName, response, workbook);
        }
    }

    /**
     * 功能描述：Excel导出
     *
     * @param fileName 文件名称
     * @param response
     * @param workbook Excel对象
     * @return
     * @date 2018/8/22 15:35
     */
    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) throws Exception{
        try {
            response.setHeader("Content-Type", "application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            throw new Exception("excel操作异常");
        }
    }

    /**
     * 功能描述：默认导出方法
     *
     * @param list     导出的实体集合
     * @param fileName 导出的文件名
     * @param response
     * @return
     * @date 2018/8/22 15:33
     */
    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response)throws Exception {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        if (workbook != null) ;
        downLoadExcel(fileName, response, workbook);
    }


    /**
     * 功能描述：根据文件路径来导入Excel
     *
     * @param filePath   文件路径
     * @param titleRows  表标题的行数
     * @param headerRows 表头行数
     * @param pojoClass  Excel实体类
     * @return
     * @date 2018/8/22 14:17
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws Exception{
        //判断文件是否存在
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (Exception e) {
            throw new Exception("excel操作异常");
        }
        return list;
    }

    /**
     * 功能描述：根据文件路径来导入Excel
     *
     * @param filePath   文件路径
     * @param titleRows  表标题的行数
     * @param headerRows 表头行数
     * @param pojoClass  Excel实体类
     * @return
     * @date 2018/8/22 14:17
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass, MyExcelHandler handler) throws Exception{
        //判断文件是否存在
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setDataHandler(handler);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (Exception e) {
            throw new Exception("excel操作异常");
        }
        return list;
    }

    /**
     * 功能描述：根据文件路径来导入Excel
     *
     * @param file       文件
     * @param titleRows  表标题的行数
     * @param headerRows 表头行数
     * @param pojoClass  Excel实体类
     * @return
     * @date 2018/8/22 14:17
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass, MyExcelHandler handler) throws Exception{
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setDataHandler(handler);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        } catch (Exception e) {
            throw new Exception("excel操作异常");
        }
        return list;
    }

    /**
     * 功能描述：根据接收的Excel文件来导入Excel,并封装成实体类
     *
     * @param file       上传的文件
     * @param titleRows  表标题的行数
     * @param headerRows 表头行数
     * @param pojoClass  Excel实体类
     * @return
     * @date 2018/8/22 14:17
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws Exception{
        if (file == null) {
            return null;
        }
        try {
            return importExcel(file.getInputStream(), titleRows, headerRows, pojoClass);
        } catch (IOException e) {
            throw new Exception("excel操作异常");
        }
    }

    public static <T> List<T> importExcel(InputStream inputStream, Integer titleRows, Integer headerRows, Class<T> pojoClass) throws Exception{
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
//        params.setStartSheetIndex(2);
        params.setSheetNum(1);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(inputStream, pojoClass, params);
        } catch (Exception e) {
            throw new Exception("excel操作异常");
        }
        return list;
    }


    /**
     * 功能描述：根据接收的Excel文件来导入Excel,并封装成实体类
     *
     * @param file       上传的文件
     * @param titleRows  表标题的行数
     * @param headerRows 表头行数
     * @param pojoClass  Excel实体类
     * @return
     * @date 2018/8/22 14:17
     */
    public static void importExcelBySax(MultipartFile file, Integer titleRows, Integer headerRows, Class<?> pojoClass, IReadHandler hanlder) throws IOException {
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setSheetNum(1);
        ExcelImportUtil.importExcelBySax(file.getInputStream(), pojoClass, params, hanlder);
    }

    private static void exportExcelTemplate(String fileName, HttpServletResponse response, String title, String sheetName, List<String> list) {
//        ExportParams exportParams = new ExportParams(title, sheetName);
////        exportParams.setCreateHeadRows(isCreateHeader);
//        List<Map<String, Object>> mapList = new ArrayList<>();
//        Map<String, Object> map = new HashMap();
//        for (String columnName : list) {
//            map.put(columnName, "columnName");
//            mapList.add(map);
//        }
////        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, map);
//        if (workbook != null) {
//            downLoadExcel(fileName, response, workbook);
//        }
    }

}
