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
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.SupplierAcctFlow;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.SupplierAcctFlowReportItem;
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

public class HeadQSupplierAcctFlowTemplate extends ExcelTemplate{
	private final static String TEMPLATE_FILE_NAME = "HeadQSupplierAcctFlowTemplate.xls";
	private List<SupplierAcctFlowReportItem> items = new ArrayList<SupplierAcctFlowReportItem>();
	private HeadQSupplier supplier = null;
	private Date startDate = null;
	private Date endDate = null;
	protected int data_row = 5;

	protected final int ORDER_DATE_COLUMN = 0;
	protected final int ORDER_TYPE_COLUMN = 1;
	protected final int ORDER_ID_COLUMN = 2;
	protected final int QUANRITY_COLUMN = 3;
	protected final int AMOUNT_COLUMN = 4;
	protected final int COMMENT_COLUMN =5;
	protected final int ACCT_INCREASE_COLUMN =6;
	protected final int ACCT_DECREASE_COLUMN =7;
	protected final int ACCT_BALANCE_COLUMN =8;
	

    public HeadQSupplierAcctFlowTemplate(File file) throws IOException{
    	super(file);
    }
	
	public HeadQSupplierAcctFlowTemplate(List<SupplierAcctFlowReportItem> items, String templateWorkbookPath, HeadQSupplier supplier, Date startDate, Date endDate) throws IOException{
		super(templateWorkbookPath + "\\" + TEMPLATE_FILE_NAME);	
		this.items = items;
		this.supplier = supplier;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	/**
	 *  
	 * @return
	 */
	public HSSFWorkbook process(){
		HSSFSheet sheet = templateWorkbook.getSheetAt(0);
		
		Row dateRow = sheet.getRow(1);
		dateRow.createCell(1).setCellValue(Common_util.dateFormat.format(startDate));
		dateRow.createCell(3).setCellValue(Common_util.dateFormat.format(endDate));

		Row reportDateRow = sheet.getRow(2);
		reportDateRow.createCell(1).setCellValue(Common_util.dateFormat_f.format(Common_util.getToday()));
		
		Row custRow = sheet.getRow(3);
		custRow.createCell(1).setCellValue(supplier.getName() + " " + supplier.getComment());

		//write cust infmration
		int totalDataRow = items.size();

		for (int i = 0; i < totalDataRow; i++){
			Row row = sheet.createRow(data_row + i);

			SupplierAcctFlowReportItem item = items.get(i);
			
			row.createCell(ORDER_DATE_COLUMN).setCellValue(Common_util.dateFormat.format(item.getDate()));
			row.createCell(ORDER_TYPE_COLUMN).setCellValue(item.getItemTypeName());
			row.createCell(ORDER_ID_COLUMN).setCellValue(item.getId());
			row.createCell(QUANRITY_COLUMN).setCellValue(item.getQuantity());
			row.createCell(AMOUNT_COLUMN).setCellValue(item.getAmount());
			row.createCell(COMMENT_COLUMN).setCellValue(item.getComment());
			if (item.getAcctIncrease() != 0)
				row.createCell(ACCT_INCREASE_COLUMN).setCellValue(item.getAcctIncrease());
			if (item.getAcctDecrease() != 0)
				row.createCell(ACCT_DECREASE_COLUMN).setCellValue(item.getAcctDecrease());
			row.createCell(ACCT_BALANCE_COLUMN).setCellValue(item.getPostAcct());
		}

		return templateWorkbook;
	}
	

}
