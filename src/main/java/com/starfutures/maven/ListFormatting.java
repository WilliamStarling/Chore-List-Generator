package com.starfutures.maven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ListFormatting 
{
	final Font headerFont;
	final Font highlightFont;
	
	final CellStyle headerStyle; //cell style for the header cells.
	final CellStyle topPlainStyle; //top of a group, normal cell.
	final CellStyle plainStyle; //normal cell
	final CellStyle bottomPlainStyle; // bottom of a group, normal cell.
	final CellStyle topHighlightStyle; //top of a group, highlight cell.
	final CellStyle highlightStyle; //highlighted cell
	final CellStyle bottomHighlightStyle; //bottom of a group, highlight cell
	
	ListFormatting(Workbook workbook, Sheet sheet, List<ChoreBoy> choreBoyList)
	{
		/*---------------------- !^ Style Definitions !^ ---------------------------------------*/
		headerFont = workbook.createFont();
		headerFont.setBold(true);
		
		highlightFont = workbook.createFont();
		highlightFont.setColor(IndexedColors.RED.getIndex());
		
		
		headerStyle = workbook.createCellStyle();
		headerStyle.setWrapText(true);
		headerStyle.setFont(headerFont);
		//headerStyle.setBorderTop(BorderStyle.DASHED);
		//headerStyle.setBorderBottom(BorderStyle.DASHED);
		headerStyle.setVerticalAlignment(VerticalAlignment.TOP);
		
		topPlainStyle = workbook.createCellStyle();
		topPlainStyle.setWrapText(true);
		topPlainStyle.setBorderTop(BorderStyle.THICK);
		topPlainStyle.setBorderBottom(BorderStyle.DASHED);
		topPlainStyle.setVerticalAlignment(VerticalAlignment.TOP);
		
		plainStyle = workbook.createCellStyle();
		plainStyle.setWrapText(true);
		plainStyle.setBorderTop(BorderStyle.DASHED);
		plainStyle.setBorderBottom(BorderStyle.DASHED);
		plainStyle.setVerticalAlignment(VerticalAlignment.TOP);
		
		bottomPlainStyle = workbook.createCellStyle();
		bottomPlainStyle.setWrapText(true);
		bottomPlainStyle.setBorderTop(BorderStyle.DASHED);
		bottomPlainStyle.setBorderBottom(BorderStyle.THICK);
		bottomPlainStyle.setVerticalAlignment(VerticalAlignment.TOP);
		
		topHighlightStyle = workbook.createCellStyle();
		topHighlightStyle.setWrapText(true);
		topHighlightStyle.setFont(highlightFont);
		topHighlightStyle.setBorderTop(BorderStyle.THICK);
		topHighlightStyle.setBorderBottom(BorderStyle.DASHED);
		topHighlightStyle.setVerticalAlignment(VerticalAlignment.TOP);
		
		highlightStyle = workbook.createCellStyle();
		highlightStyle.setWrapText(true);
		highlightStyle.setFont(highlightFont);
		highlightStyle.setBorderTop(BorderStyle.DASHED);
		highlightStyle.setBorderBottom(BorderStyle.DASHED);
		highlightStyle.setVerticalAlignment(VerticalAlignment.TOP);
		
		bottomHighlightStyle = workbook.createCellStyle();
		bottomHighlightStyle.setWrapText(true);
		bottomHighlightStyle.setFont(highlightFont);
		bottomHighlightStyle.setBorderTop(BorderStyle.DASHED);
		bottomHighlightStyle.setBorderBottom(BorderStyle.THICK);
		bottomHighlightStyle.setVerticalAlignment(VerticalAlignment.TOP);
		
		/*---------------------- ^ Style Definitions ^ ---------------------------------------*/
		
		//set the column widths, so that everything fits on one page.
		sheet.setColumnWidth(0, 42 * 256); //the size in excel was 42, and POI uses 1/256th of a character unit.
		sheet.setColumnWidth(1, 36 * 256);
		sheet.setColumnWidth(2, 12 * 256);
		sheet.setColumnWidth(3, (int) (0.5 * 256)); //shh
		
		List<String> listHeader = List.of("Chores To Do", "Extra Description", "Housekeeper");
		List<String> blankHeader = List.of("", "", "", "");
		
		//tack on the header row and blank row.
		writeRow(sheet, listHeader, headerStyle, headerStyle);
		writeRow(sheet, blankHeader, headerStyle, headerStyle); //use header style for the blanks, since it doesn't have borders and it being bold doesn't matter cause blank.
		
		// add all the chores for each chore boy.
		for(ChoreBoy choreBoy: choreBoyList)
		{
			writeChoreBoy(sheet, choreBoy);
		}
	}
	
	//overloaded function to work with lists instead of string arrays.
	//function to write a list of strings to a row on a spreadsheet.
	private static void writeRow(Sheet sheet, List<String> values, CellStyle plainStyle, CellStyle highlightStyle)
	{
		//get the number for the next row.
		int nextRowNumb = sheet.getLastRowNum() + 1;
		//create a new row
		Row newRow = sheet.createRow(nextRowNumb);
		//iteratively fill in each cell of the new row
	    for (int col = 0; col < values.size(); col++) {
	    	Cell nextCell = newRow.createCell(col);
	    	nextCell.setCellValue(values.get(col)); //create and set the cell
	    	
	    	if(col == 1) //the second column is the one with notes, hard coding this in.
	    	{
	    		nextCell.setCellStyle(highlightStyle);
	    	}
	    	else
	    	{
	    		nextCell.setCellStyle(plainStyle);
	    	}
	    }
	}
	
	private void writeChoreBoy(Sheet sheet, ChoreBoy choreBoy)
	{
		List<String[]> chores = choreBoy.getPersonalList();
		String[] choreBoyDetails = choreBoy.getBoyDetails();
		
		for(int currentChore = 0; currentChore < chores.size(); currentChore++)
		{
			int lastChore = chores.size();
			String[] chore = chores.get(currentChore);
			
			//create a chore entry with the chore as well as the person details.
			List<String> choreEntry = new ArrayList<>();
			choreEntry.addAll(Arrays.asList(chore));
			choreEntry.addAll(Arrays.asList(choreBoyDetails));
			
			//if else if to handle the first chore (top), last chore (bottom), and the normal chores (plain).
			if(currentChore == 0)
			{
				//use top style
				writeRow(sheet, choreEntry, topPlainStyle, topHighlightStyle);
			}
			else if (currentChore == chores.size() -1)
			{
				//use bottom style
				writeRow(sheet, choreEntry, bottomPlainStyle, bottomHighlightStyle);
			}
			else
			{
				//use plain style
				writeRow(sheet, choreEntry, plainStyle, highlightStyle);
			}
		}
	}
	
}
