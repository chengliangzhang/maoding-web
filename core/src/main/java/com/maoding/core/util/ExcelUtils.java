package com.maoding.core.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ExcelUtils
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2016年4月15日-下午2:11:25
 */
public class ExcelUtils {

	/** 日志对象 */
	private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);
	
	/**
	 * 描述：读取所有的Sheet
	 * 作者：Chenxj
	 * 日期：2016年3月27日 - 上午9:38:59
	 * @param excelFile
	 * @return
	 */
	public static Map<String, List<List<Object>>>readAllSheetFromExcel(File excelFile){
		Map<String, List<List<Object>>> dataMap = new HashMap<String, List<List<Object>>>();
		InputStream in = null;
		Workbook wb = null;
		Sheet sheet;
		try {
			in=new FileInputStream(excelFile);
			wb=WorkbookFactory.create(in);
		} catch (InvalidFormatException e) {
			log.error("文件格式错误", e);
		} catch (IOException e) {
			log.error("读取失败", e);
		}
		
		int sheetNum = wb.getNumberOfSheets();
		for (int i = 0; i < sheetNum; i++) {
			sheet = wb.getSheetAt(i);
			String sheetName = sheet.getSheetName();
			dataMap.put(sheetName, readSheet(sheet, 0));
		}
		return dataMap;
	}
	/**
	 * 描述：读取第一个sheet
	 * 作者：Chenxj
	 * 日期：2016年3月27日 - 上午9:39:41
	 * @param excelFile
	 * @return
	 */
	public static List<List<Object>>readOneSheetFromExcel(File excelFile){
		return readSheetBySheetIndexFromExcel(excelFile, 0, 0);
	}
	
	/**
	 * 描述：读取第一个sheet
	 * 作者：Chenxj
	 * 日期：2016年3月27日 - 上午9:39:41
	 * @return
	 */
	public static List<List<Object>>readOneSheetFromExcel(InputStream in){
		return readSheetBySheetIndexFromExcel(in, 0, 0);
	}
	/**
	 * 描述：读取指定Sheet名称，返回数据集
	 * 作者：Chenxj
	 * 日期：2016年3月27日 - 上午9:32:04
	 * @param excelFile Excel文件
	 * @param sheetName Sheet名称
	 * @param startIndex 开始行号
	 * @return CSV list
	 */
	public static List<List<Object>>readSheetBySheetNameFromExcel(File excelFile,String sheetName,int startIndex){
		InputStream in = null;
		Workbook wb = null;
		Sheet sheet;
		try {
			in=new FileInputStream(excelFile);
			wb=WorkbookFactory.create(in);
		} catch (InvalidFormatException e) {
			log.error("文件格式错误", e);
		} catch (IOException e) {
			log.error("读取失败", e);
		}
		sheet = wb.getSheet(sheetName);
		return readSheet(sheet, startIndex);
	}
	/**
	 * 描述：读取指定索引Sheet，返回数据集
	 * 作者：Chenxj
	 * 日期：2016年3月27日 - 上午9:30:49
	 * @param excelFile Excel文件
	 * @param sheetIndex Sheet索引号
	 * @param startIndex 开始行号
	 * @return CSV list
	 */
	public static List<List<Object>>readSheetBySheetIndexFromExcel(File excelFile,int sheetIndex,int startIndex){
		InputStream in = null;
		Workbook wb = null;
		Sheet sheet;
		try {
			in=new FileInputStream(excelFile);
			wb=WorkbookFactory.create(in);
		} catch (InvalidFormatException e) {
			log.error("文件格式错误", e);
		} catch (IOException e) {
			log.error("读取失败", e);
		}
		sheet = wb.getSheetAt(sheetIndex);
		return readSheet(sheet, startIndex);
	}
	
	/**
	 * 描述：读取指定索引Sheet，返回数据集
	 * 作者：Chenxj
	 * 日期：2016年3月27日 - 上午9:30:49
	 * @param sheetIndex Sheet索引号
	 * @param startIndex 开始行号
	 * @return CSV list
	 */
	public static List<List<Object>>readSheetBySheetIndexFromExcel(InputStream in,int sheetIndex,int startIndex){
		Workbook wb = null;
		Sheet sheet;
		try {
			wb=WorkbookFactory.create(in);
		} catch (InvalidFormatException e) {
			log.error("文件格式错误", e);
		} catch (IOException e) {
			log.error("读取失败", e);
		}
		sheet = wb.getSheetAt(sheetIndex);
		return readSheet(sheet, startIndex);
	}
	/**
	 * 读取指定sheet
	 * @param sheet
	 * @param startIndex 开始行号
	 * @return list
	 */
	private  static List<List<Object>> readSheet(Sheet sheet,int startIndex){
		List<List<Object>> dataList = new ArrayList<List<Object>>();
		Row row;
		int rowNum = sheet.getPhysicalNumberOfRows();
		int firstRowColNum=-1;
		row=sheet.getRow(0);
		if(row!=null){
			firstRowColNum=row.getPhysicalNumberOfCells();
			if(startIndex==0){
				for(int i=startIndex;i<rowNum;i++){
					row=sheet.getRow(i);
					List<Object> rowDataList = new ArrayList<Object>();
					for(int j=0;j<firstRowColNum;j++){
						rowDataList.add(readCellValu(row.getCell(j)));
					}
					dataList.add(rowDataList);
				}
			}else if(startIndex>0&&startIndex<=rowNum){
				//读取第一行数据
				row = sheet.getRow(0);
				if(row!=null){
					List<Object> rowDataList = new ArrayList<Object>();
					for(int j=0;j<firstRowColNum;j++){
						rowDataList.add(readCellValu(row.getCell(j)));
					}
					dataList.add(rowDataList);
				}
				//从startIndex处开始读取数据
				for(int i=startIndex;i<rowNum;i++){
					row=sheet.getRow(i);
					List<Object> rowDataList = new ArrayList<Object>();
					for(int j=0;j<firstRowColNum;j++){
						rowDataList.add(readCellValu(row.getCell(j)));
					}
					dataList.add(rowDataList);
				}
			}
		}
		return dataList;
	}
	/**
	 * 描述：读取Excel格子中的值
	 * 作者：Chenxj
	 * 日期：2016年3月27日 - 上午9:16:31
	 * @param c Excel中的格子对象
	 * @return Object
	 */
	private  static Object readCellValu(Cell c) {
		if(c==null) return "";

		switch (c.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				return "";
			case Cell.CELL_TYPE_BOOLEAN:
				return c.getBooleanCellValue();
			case Cell.CELL_TYPE_ERROR:
				return c.getErrorCellValue();
			case Cell.CELL_TYPE_FORMULA:
				return c.getCellFormula().replaceAll("\"", "");
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(c)) {
					return c.getDateCellValue();
				}
				DecimalFormat df = new DecimalFormat("#");
				return df.format(c.getNumericCellValue());
			case Cell.CELL_TYPE_STRING:
				return c.getStringCellValue();
		}
		return "Unknown Cell Type:" + c.getCellType();
	}

	/**
	 * 作用：关闭输入流
	 * 作者: Zhangchengliang
	 * 日期：2017/7/19
     */
	public static void closeInputSteam(InputStream in){
		if (in != null){
			try {
				in.close();
			} catch (IOException e) {
				log.error("关闭时产生错误", e);
			}
		}
	}

	/**
	 * 作用：关闭工作簿
	 * 作者: Zhangchengliang
	 * 日期：2017/7/19
	 */
	public static void closeWorkbook(Workbook workbook){
		if (workbook != null){
			try {
				workbook.close();
			} catch (IOException e) {
				log.error("关闭工作簿时产生错误", e);
			}
		}		
	}

	/**
	 * 作用：根据输入流获取工作簿对象
	 * 作者：Zhangchengliang
	 * 日期：2017/7/19
	 */
	public static Workbook getWorkbook(InputStream in) {
		if (in == null) return null;

		Workbook wb = null;

		try {
			wb = WorkbookFactory.create(in);
		} catch (InvalidFormatException e) {
			log.error("Excel文件格式错误",e);
		} catch (IOException e) {
			log.error("Excel文件读取错误",e);
		}
		return wb;
	}

	/**
	 * 作用：根据文件名获取工作簿对象
	 * 作者：Zhangchengliang
	 * 日期：2017/7/19
	 */	
	public static Workbook getWorkbook(String fileName){
		if (fileName == null) return null;

		InputStream in = null;
		Workbook wb = null;
		try {
			in = new FileInputStream((new File(fileName))); 
			wb = getWorkbook(in);
		} catch (FileNotFoundException e) {
			log.error("无法找到"+fileName, e);
		} catch (Exception e) {
			log.error("读取文件"+fileName+"错误",e);
			closeInputSteam(in);
		}
		return wb;
	}

	/**
	 * 作用：获取一个空的工作簿对象
	 * 作者：Zhangchengliang
	 * 日期：2017/7/19
     */
	public static Workbook getWorkbook(){
		return new HSSFWorkbook();
	}

	/**
	 * 作用：读取单元格内容
	 * 作者：Zhangchengliang
	 * 日期：2017/7/19
     */
	public static Object readFrom(Cell cell){
		if(cell==null) return null;

		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				return null;
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			case Cell.CELL_TYPE_ERROR:
				return cell.getErrorCellValue();
			case Cell.CELL_TYPE_FORMULA:
				return cell.getCellFormula().replaceAll("\"", "");
			case Cell.CELL_TYPE_NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					return cell.getDateCellValue();
				} else {
					DecimalFormat df = new DecimalFormat("#");
					return df.format(cell.getNumericCellValue());
				}
			case Cell.CELL_TYPE_STRING:
				return StringUtil.isEmpty(cell.getStringCellValue()) ? null : cell.getStringCellValue();
		}
		return "Unknown Cell Type:" + cell.getCellType();
	}

	/**
	 * 作用：读取一行内容
	 * 作者：Zhangchengliang
	 * 日期：2017/7/19
	 */
	public static Map<String,Object> readFrom(Row row, Map<Short,String> titleMap, Short startColumn, Short endColumn){
		if (row == null) return null;
		if ((startColumn == null) || (startColumn == -1)) startColumn = row.getFirstCellNum();
		if ((endColumn == null) || (endColumn == -1)) endColumn = row.getLastCellNum();
		
		//从startColumn处开始读取数据
		Map<String,Object> dataMap = null;
		for(Short i=startColumn; i<=endColumn; i++){
			String name = ((titleMap != null) && (titleMap.containsKey(i))) ? titleMap.get(i) : i.toString();
			Object data = readFrom(row.getCell(i));
			if (data != null) {
				if (dataMap == null) dataMap = new HashMap<>();
				dataMap.put(name, data);
			}
		}
		return dataMap;
	}

	/**
	 * 作用：读取一页内容
	 * 作者：Zhangchengliang
	 * 日期：2017/7/19
	 */
	public static List<Map<String,Object>> readFrom(Sheet sheet, Integer titleRow, Integer startRow,Short startColumn,Short endColumn){
		if (sheet == null) return null;
		if ((titleRow == null) || (titleRow == -1)) titleRow = sheet.getFirstRowNum();

		//读取标题数据,从titleRow倒序读取第一个不为空的值，实现多标题行合并
		Map<Short,String> titleMap = new HashMap<>();
		if ((sheet.getFirstRowNum() <= titleRow) && (titleRow <= sheet.getLastRowNum())) {
			Row row = sheet.getRow(titleRow);
			if ((startColumn == null) || (startColumn == -1)) startColumn = row.getFirstCellNum();
			if ((endColumn == null) || (endColumn == -1)) endColumn = row.getLastCellNum();
			for(Short i=startColumn; i<=endColumn; i++){
				Object data = readFrom(row.getCell(i));
				if ((data == null) || StringUtils.isEmpty(data.toString())){
					for (Integer k=titleRow; k>sheet.getFirstRowNum(); k--){
						Row rowPrev = sheet.getRow(k);
						data = readFrom(rowPrev.getCell(i));
						if ((data != null) && !StringUtils.isEmpty(data.toString())) break;
					}
				}
				if ((data != null) && !StringUtils.isEmpty(data.toString())) {
					titleMap.put(i, data.toString());
				}
			}
		}
		
		//读取数据行
		List<Map<String,Object>> dataList = null;
		if ((startRow == null) || (startRow == -1)) startRow = titleRow + 1;
		Integer endRow = sheet.getLastRowNum();
		for (Integer i=startRow; i<=endRow; i++){
			Map<String,Object> r = readFrom(sheet.getRow(i),titleMap,startColumn,endColumn);
			if (r != null) {
				if (dataList == null) dataList = new ArrayList<>();
				dataList.add(readFrom(sheet.getRow(i), titleMap, startColumn, endColumn));
			}
		}
		return dataList;
	}

	/**
	 * 作用：读取工作簿内指定页内容
	 * 作者：Zhangchengliang
	 * 日期：2017/7/19
	 */
	public static List<Map<String,Object>> readFrom(Workbook workbook,Integer sheetIndex,Integer titleRow,Integer startRow,Short startColumn,Short endColumn){
		if (workbook == null) return null;
		if ((sheetIndex == null) || (sheetIndex == -1)) sheetIndex = workbook.getActiveSheetIndex();
		
		return ((0<=sheetIndex) && (sheetIndex<workbook.getNumberOfSheets())) ?
				readFrom(workbook.getSheetAt(sheetIndex),titleRow,startRow,startColumn,endColumn) : null;
	}
	public static List<Map<String,Object>> readFrom(Workbook workbook,String sheetName,Integer titleRow,Integer startRow,Short startColumn,Short endColumn){
		if ((workbook == null) || (sheetName == null)) return null;
		return readFrom(workbook.getSheet(sheetName),titleRow,startRow,startColumn,endColumn);
	}
	public static List<Map<String,Object>> readFrom(Workbook workbook,Integer sheetIndex,Integer titleRow){
		return readFrom(workbook,sheetIndex,titleRow,null,null,null);
	}
	public static List<Map<String,Object>> readFrom(Workbook workbook,String sheetName,Integer titleRow){
		return readFrom(workbook,sheetName,titleRow,null,null,null);
	}
	public static List<Map<String,Object>> readFrom(Workbook workbook,Integer sheetIndex,Integer titleRow,Short startColumn){
		return readFrom(workbook,sheetIndex,titleRow,null,startColumn,null);
	}
	public static List<Map<String,Object>> readFrom(Workbook workbook,String sheetName,Integer titleRow,Short startColumn){
		return readFrom(workbook,sheetName,titleRow,null,startColumn,null);
	}
}
