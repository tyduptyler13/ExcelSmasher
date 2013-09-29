
package com.myuplay.excelsmasher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Writter{

	private XSSFWorkbook wb;
	private Sheet s;

	private static final String[] header = {
		"FilePart 1",
		"FilePart 2",
		"FilePart 3",
		"Trial",
		"Q1",
		"Q1 CL",
		"Q2",
		"Q2 CL",
		"Q3",
		"Q3 CL",
		"Q4",
		"Q5",
	};

	public Writter(){

		wb = new XSSFWorkbook();
		s = wb.createSheet("Final Master Sheet");
		//CellStyle headerStyle = wb.createCellStyle(); BROKEN

		//Font f = wb.createFont();
		//f.setFontHeight((short)14);
		//f.setBoldweight((short)700);

		Row r = s.createRow(0);

		for (int i = 0; i < header.length; ++i){

			Cell c = r.createCell(i);
			c.setCellValue(header[i]);
			//c.setCellStyle(headerStyle);

		}

	}

	public Row createRow(){

		return s.createRow(s.getLastRowNum()+1);

	}

	public CellStyle createCellStyle(){
		return wb.createCellStyle();
	}

	public void write(File f) throws IOException{
		OutputStream s = new FileOutputStream(f);

		wb.write(s);

		s.close();
	}


}
