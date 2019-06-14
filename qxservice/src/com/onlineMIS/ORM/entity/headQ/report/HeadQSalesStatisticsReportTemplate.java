package com.onlineMIS.ORM.entity.headQ.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Year;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;
import com.onlineMIS.ORM.entity.chainS.report.ChainSalesStatisReportItem;
import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.chainS.user.ChainUserInfor;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Brand;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Color;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Product;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Quarter;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.ExcelTemplate;
import com.onlineMIS.common.loggerLocal;
import com.sun.jndi.toolkit.ctx.StringHeadTail;

public class HeadQSalesStatisticsReportTemplate  extends ExcelTemplate{
	private final static String TEMPLATE_FILE_NAME = "HeadQSalesStatisticsReportTemplate.xls";
	private List<HeadQSalesStatisticReportItem> items = new ArrayList<HeadQSalesStatisticReportItem>();
	private HeadQSalesStatisticReportItem totalItem = new HeadQSalesStatisticReportItem();

	private int data_row = 6;

	private final int BRAND_COLUMN = 0;
	private final int PRODUCT_CODE_COLUMN = 1;
	private final int COLOR_COLUMN = 2;
	private final int QUARTER_COLUMN = 3;
	private final int CATEGORY_COLUMN =4;
	private final int GENDER_COLUMN =5;
	private final int SIZE_COLUMN =6;
	private final int SIZE_MIN_COLUMN =7;
	private final int SIZE_MAX_COLUMN =8;
	
	private final int SALES_Q_COLUMN =9;
	private final int SALES_COST_COLUMN =10;
	private final int SALES_AMT_COLUMN = 11;
	private final int SALES_REVENUE_COLUMN =12;
	
	private final int RETURN_Q_COLUMN = 13;
	private final int RETURN_COST_COLUMN = 14;
	private final int RETURN_AMT_COLUMN = 15;
	private final int RETURN_REVENUE_COLUMN = 16;
	
	private final int NET_Q_COLUMN = 17;
	private final int NET_COST_COLUMN = 18;
	private final int NET_AMT_COLUMN = 19;
	private final int PROFIT_COLUMN = 20;
	private String rptDesp ;
	private Date startDate = new Date();
	private Date endDate = new Date();


    public HeadQSalesStatisticsReportTemplate(File file) throws IOException{
    	super(file);
    }
	
	public HeadQSalesStatisticsReportTemplate(List<HeadQSalesStatisticReportItem> items, HeadQSalesStatisticReportItem totalItem, String rptDesp, String templateWorkbookPath, Date startDate, Date endDate) throws IOException{
		super(templateWorkbookPath + "\\" + TEMPLATE_FILE_NAME);	
		this.items = items;
		this.rptDesp = rptDesp;
        this.totalItem = totalItem;
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalItem = totalItem;
	}
	
	/**
	 *  
	 * @return
	 */
	public HSSFWorkbook process(){
		HSSFSheet sheet = templateWorkbook.getSheetAt(0);
		//write header
		Row header1 = sheet.getRow(1);
		header1.createCell(1).setCellValue(Common_util.dateFormat.format(startDate));
		header1.createCell(3).setCellValue(Common_util.dateFormat.format(endDate));
		
		Row header2 = sheet.getRow(2);
		header2.createCell(1).setCellValue(Common_util.dateFormat_f.format(Common_util.getToday()));
		
		Row header3 = sheet.getRow(3);
		header3.createCell(1).setCellValue(rptDesp);

		
		//write product infmration
		int totalDataRow = items.size();

		for (int i = 0; i < totalDataRow; i++){

			HeadQSalesStatisticReportItem levelFourItem = items.get(i);
			Row row = sheet.createRow(data_row + i);

			ProductBarcode pb = levelFourItem.getPb();
			Product product = pb.getProduct();
			

			row.createCell(PRODUCT_CODE_COLUMN).setCellValue(product.getProductCode());
			Color color = levelFourItem.getPb().getColor();
			if (color == null)
				row.createCell(COLOR_COLUMN).setCellValue("");
			else 
				row.createCell(COLOR_COLUMN).setCellValue(color.getName());
			
			row.createCell(BRAND_COLUMN).setCellValue(product.getBrand().getBrand_Name());
			
			row.createCell(QUARTER_COLUMN).setCellValue(product.getYear().getYear() + "-" + product.getQuarter().getQuarter_Name());

			row.createCell(CATEGORY_COLUMN).setCellValue(product.getCategory().getCategory_Name());
			
			row.createCell(GENDER_COLUMN).setCellValue(product.getGenderS());
			row.createCell(SIZE_COLUMN).setCellValue(product.getSizeRangeS());
			
			Integer sizeMin = product.getSizeMin();
			if (sizeMin != null && sizeMin != 0)
			    row.createCell(SIZE_MIN_COLUMN).setCellValue(sizeMin);
			
			Integer sizeMax = product.getSizeMin();
			if (sizeMax != null && sizeMax != 0)
			    row.createCell(SIZE_MAX_COLUMN).setCellValue(sizeMax);
			
			if (levelFourItem.getSalesQ() != 0)
			    row.createCell(SALES_Q_COLUMN).setCellValue(levelFourItem.getSalesQ());
			if (levelFourItem.getSalesCost() != 0)
				   row.createCell(SALES_COST_COLUMN).setCellValue(levelFourItem.getSalesCost());
			if (levelFourItem.getSalesPrice() != 0)
				   row.createCell(SALES_AMT_COLUMN).setCellValue(levelFourItem.getSalesPrice());
			if (levelFourItem.getSalesProfit() != 0)
				   row.createCell(SALES_REVENUE_COLUMN).setCellValue(levelFourItem.getSalesProfit());
			
			if (levelFourItem.getReturnQ() != 0)
			    row.createCell(RETURN_Q_COLUMN).setCellValue(levelFourItem.getReturnQ());
			if (levelFourItem.getReturnCost() != 0)
				   row.createCell(RETURN_COST_COLUMN).setCellValue(levelFourItem.getReturnCost());
			if (levelFourItem.getReturnPrice() != 0)
				   row.createCell(RETURN_AMT_COLUMN).setCellValue(levelFourItem.getReturnPrice());
			if (levelFourItem.getReturnProfit() != 0)
				   row.createCell(RETURN_REVENUE_COLUMN).setCellValue(levelFourItem.getReturnProfit());
			
			row.createCell(NET_Q_COLUMN).setCellValue(levelFourItem.getNetQ());
			row.createCell(NET_AMT_COLUMN).setCellValue(levelFourItem.getNetPrice());
			row.createCell(NET_COST_COLUMN).setCellValue(levelFourItem.getNetCost());
			row.createCell(PROFIT_COLUMN).setCellValue(levelFourItem.getNetProfit());
		}
		
		//把总数放进去
		Row row = sheet.createRow(data_row + totalDataRow);
		row.createCell(BRAND_COLUMN).setCellValue("总计");
		row.createCell(SALES_Q_COLUMN).setCellValue(totalItem.getSalesQ());
		row.createCell(RETURN_Q_COLUMN).setCellValue(totalItem.getReturnQ());
		row.createCell(NET_Q_COLUMN).setCellValue(totalItem.getNetQ());
		
		row.createCell(SALES_COST_COLUMN).setCellValue(totalItem.getSalesCost());
		row.createCell(RETURN_COST_COLUMN).setCellValue(totalItem.getReturnCost());
		row.createCell(NET_COST_COLUMN).setCellValue(totalItem.getNetCost());
		
		row.createCell(SALES_AMT_COLUMN).setCellValue(totalItem.getSalesPrice());
		row.createCell(RETURN_AMT_COLUMN).setCellValue(totalItem.getReturnPrice());
		row.createCell(NET_AMT_COLUMN).setCellValue(totalItem.getNetPrice());
		
		row.createCell(SALES_REVENUE_COLUMN).setCellValue(totalItem.getSalesProfit());
		row.createCell(RETURN_REVENUE_COLUMN).setCellValue(totalItem.getReturnProfit());
		row.createCell(PROFIT_COLUMN).setCellValue(totalItem.getNetProfit());
	
		return templateWorkbook;
	}
	

}
