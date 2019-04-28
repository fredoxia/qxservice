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

public class HeadQSupplierInforTemplate  extends ExcelTemplate{
	private final static String TEMPLATE_FILE_NAME = "HeadQSupplierInforTemplate.xls";
	private List<HeadQSupplier> items = new ArrayList<HeadQSupplier>();
	
	private int data_row = 1;

	private final int ID_COLUMN = 0;
	private final int NAME_COLUMN = 1;
	private final int ADDRESS_COLUMN = 2;
	private final int ACCT_BALANCE_COLUMN = 3;
	private final int COMMENT_COLUMN =4;




    public HeadQSupplierInforTemplate(File file) throws IOException{
    	super(file);
    }
	
	public HeadQSupplierInforTemplate(List<HeadQSupplier> items, String templateWorkbookPath) throws IOException{
		super(templateWorkbookPath + "\\" + TEMPLATE_FILE_NAME);	
		this.items = items;
	}
	
	/**
	 *  
	 * @return
	 */
	public HSSFWorkbook process(){
		HSSFSheet sheet = templateWorkbook.getSheetAt(0);

		//write cust infmration
		int totalDataRow = items.size();

		for (int i = 0; i < totalDataRow; i++){
			HeadQSupplier supplier = items.get(i);
			Row row = sheet.createRow(data_row + i);

			row.createCell(ID_COLUMN).setCellValue(supplier.getId());
			row.createCell(NAME_COLUMN).setCellValue(supplier.getName());
			row.createCell(ADDRESS_COLUMN).setCellValue(supplier.getAddress());
			row.createCell(ACCT_BALANCE_COLUMN).setCellValue(supplier.getCurrentAcctBalance());
			row.createCell(COMMENT_COLUMN).setCellValue(supplier.getComment());

		}

		return templateWorkbook;
	}
	

}
