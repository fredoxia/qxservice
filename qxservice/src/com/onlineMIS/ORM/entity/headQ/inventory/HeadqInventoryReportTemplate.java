package com.onlineMIS.ORM.entity.headQ.inventory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.interfaces.PBEKey;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.onlineMIS.ORM.DAO.headQ.inventory.HeadqInventoryReportItem;

import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Brand;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Color;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Product;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Quarter;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Year;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.ExcelTemplate;

public class HeadqInventoryReportTemplate extends ExcelTemplate{
	private List<HeadqInventoryReportItem> items = new ArrayList<HeadqInventoryReportItem>();
	private HeadQInventoryStore store;
	
	private int year_column = 0;
	private int quarter_column = 1;
	private int brand_column = 2;
	private int productCode_column = 3;
	private int colour_column = 4;
	private int unit_column = 5;

	private int barcode_column = 6;
	private int category_column = 7;
	private int gender_column = 8;
	private int sizeRange_column = 9;
	private int minSize_column = 10;
	private int maxSize_column = 11;
	private int quantity_column = 12;
	private int totalCost_column = 13;
	private int totalSales_column = 14;
	private int data_row = 3;


    public HeadqInventoryReportTemplate(File file) throws IOException{
    	super(file);
    }
	
	public HeadqInventoryReportTemplate(List<HeadqInventoryReportItem> items, HeadQInventoryStore store, String templateWorkbookPath) throws IOException{
		super(templateWorkbookPath);	
		this.items = items;
		this.store = store;

	}
	
	/**
	 *  this is the function to inject the inventory order to Jinsuan order template
	 * @return
	 */
	public HSSFWorkbook process(){
		HSSFSheet sheet = templateWorkbook.getSheetAt(0);
		//write header
		if (store != null){
			Row header1 = sheet.getRow(0);
			header1.createCell(1).setCellValue(store.getName());
		}
		
		Row header2 = sheet.getRow(1);
		header2.createCell(1).setCellValue(Common_util.dateFormat_f.format(new Date()));
		
		//write product infmration
		int totalDataRow = items.size();
		int totalQuantity = 0;
		double totalCost = 0;
		double totalSale = 0;
		for (int i = 0; i < totalDataRow; i++){

			HeadqInventoryReportItem levelFourItem = items.get(i);
			Row row = sheet.createRow(data_row + i);

			Product product = levelFourItem.getProductBarcode().getProduct();
			
			Year year = product.getYear();
			row.createCell(year_column).setCellValue(year.getYear());
			
			Quarter quarter =  product.getQuarter();
			row.createCell(quarter_column).setCellValue(quarter.getQuarter_Name());
			
			Brand brand = product.getBrand();
			row.createCell(brand_column).setCellValue(brand.getBrand_Name());
			
			row.createCell(productCode_column).setCellValue(product.getProductCode());
			
			Color color = levelFourItem.getProductBarcode().getColor();
			if (color == null)
				row.createCell(colour_column).setCellValue("");
			else 
				row.createCell(colour_column).setCellValue(color.getName());
			
			row.createCell(unit_column).setCellValue(product.getUnit());
			
			row.createCell(barcode_column).setCellValue(levelFourItem.getProductBarcode().getBarcode());
			row.createCell(category_column).setCellValue(product.getCategory().getCategory_Name());

			row.createCell(gender_column).setCellValue(product.getGenderS());
			row.createCell(sizeRange_column).setCellValue(product.getSizeRangeS());
			
			if (product.getSizeMin() != null)
			     row.createCell(minSize_column).setCellValue(product.getSizeMin());
			
			if (product.getSizeMax() != null)
			    row.createCell(maxSize_column).setCellValue(product.getSizeMax());
			
			row.createCell(quantity_column).setCellValue(levelFourItem.getTotalQuantity());
			row.createCell(totalCost_column).setCellValue(levelFourItem.getTotalCostAmt());
			row.createCell(totalSales_column).setCellValue(levelFourItem.getTotalWholeSalesAmt());
			
			totalQuantity += levelFourItem.getTotalQuantity();
			totalCost += levelFourItem.getTotalCostAmt();
			totalSale += levelFourItem.getTotalWholeSalesAmt();
		}
		
		Row row = sheet.createRow(totalDataRow + 3);
		row.createCell(0).setCellValue("合计");
		row.createCell(quantity_column).setCellValue(totalQuantity);
		row.createCell(totalCost_column).setCellValue(totalCost);
		row.createCell(totalSales_column).setCellValue(totalSale);

		
		return templateWorkbook;
	}
}
