
package com.myuplay.excelsmasher;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Reader implements AutoCloseable{

	private final Workbook workbook;
	private final String[] id;
	private static final int[] importantRows = { 5, 7, 10, 12, 15, 17, 20, 23 };
	private InputStream inp;

	public Reader(File file) throws Exception{

		id = file.getName().replaceAll("\\.xlsx?", "").split("-");

		if (id.length < 3){
			Console.warn("The file descriptor doesn't appear to be correct. This WILL result in missing fields. "+
					"Please always use format *-*-*.xls(x)");
		}

		inp = new FileInputStream(file);

		if (file.getName().endsWith(".xls")){

			POIFSFileSystem fs = new POIFSFileSystem(inp);
			workbook = new HSSFWorkbook(fs);

		} else if (file.getName().endsWith(".xlsx")){

			workbook = new XSSFWorkbook(inp);

		} else {
			throw new Exception("This file type isn't recognized.");
		}

	}

	public void parse(Writter w){

		for (int i=0; i < workbook.getNumberOfSheets(); ++i){

			if (workbook.getSheetName(i).equals("Master Sheet")){
				continue;//Skip this sheet.
			}

			parseSheet(w, workbook.getSheetAt(i));

		}

	}

	private void parseSheet(Writter w, Sheet s){

		int trial = getTrial(s);

		Row out = w.createRow();

		for (int i = 0; i < id.length; i++){
			Cell c = out.createCell(i);
			c.setCellValue(id[i]);
		}

		Cell c = out.createCell(3);
		c.setCellValue(trial);

		int outIndex = 4;

		for (int i : importantRows){

			Cell old = s.getRow(i-1).getCell(0);
			Cell newCell = out.createCell(outIndex);
			copyCell(old, newCell, w);
			outIndex++;

		}

	}

	private Cell copyCell(Cell oldCell, Cell newCell, Writter w){

		// If the old cell is null jump to next cell
		if (oldCell == null) {
			newCell = null;
			return newCell;
		}

		// Copy style from old cell and apply to new cell
		//CellStyle newCellStyle = w.createCellStyle();
		//newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
		// * BROKEN *

		//newCell.setCellStyle(newCellStyle);

		// If there is a cell comment, copy
		if (oldCell.getCellComment() != null) {
			newCell.setCellComment(oldCell.getCellComment());
		}

		// If there is a cell hyperlink, copy
		if (oldCell.getHyperlink() != null) {
			newCell.setHyperlink(oldCell.getHyperlink());
		}

		// Set the cell data type
		newCell.setCellType(oldCell.getCellType());

		// Set the cell data value
		switch (oldCell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			newCell.setCellValue(oldCell.getStringCellValue());
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			newCell.setCellValue(oldCell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			newCell.setCellErrorValue(oldCell.getErrorCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			newCell.setCellFormula(oldCell.getCellFormula());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			newCell.setCellValue(oldCell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			newCell.setCellValue(oldCell.getRichStringCellValue());
			break;
		}

		return newCell;

	}

	private int getTrial(Sheet s){
		String tmp = s.getRow(0).getCell(0).getStringCellValue();
		return Integer.parseInt(getMatch(tmp, "(\\d)+$"));
	}

	private String getMatch(String target, String regex){
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(target);

		if (m.find()){
			return m.group();
		} else {
			return "";
		}

	}
	
	@Override
	public void close() throws Exception {
		inp.close();
	}


}
