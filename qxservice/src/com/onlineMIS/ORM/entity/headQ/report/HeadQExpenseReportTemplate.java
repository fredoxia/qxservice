package com.onlineMIS.ORM.entity.headQ.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.record.ExtendedFormatRecord;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.finance.ChainAcctFlowReportItem;

import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.ExcelTemplate;

/**
 * 连锁店acct flow report
 * @author Administrator
 *
 */
public class HeadQExpenseReportTemplate extends ExcelTemplate{
	private final static String TEMPLATE_FILE_NAME = "HeadQExpenseReportTemplate.xls";
	private List<HeadQExpenseRptElesVO> items = new ArrayList<HeadQExpenseRptElesVO>();


	private Date startDate = null;
	private Date endDate = null;
	protected int data_row = 3;
	protected int date_row = 1;

	protected final int CATEGORY_LEVEL_1_COL = 0;
	protected final int CATEGORY_LEVEL_2_COL = 1;
	protected final int CASH_COL = 2;
	protected final int CARD_COL = 3;
	protected final int ALIPAY_COL =4;
	protected final int WECHAT_COL =5;
	protected final int TOTAL_COL =6;
	

    public HeadQExpenseReportTemplate(File file) throws IOException{
    	super(file);
    }
	
	public HeadQExpenseReportTemplate(List<HeadQExpenseRptElesVO> items, String templateWorkbookPath, Date startDate, Date endDate) throws IOException{
		super(templateWorkbookPath + "\\" + TEMPLATE_FILE_NAME);	
		this.items = items;
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	/**
	 *  
	 * @return
	 */
	public HSSFWorkbook process(){
		HSSFSheet sheet = templateWorkbook.getSheetAt(0);
		
		HSSFFont font = templateWorkbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short)11);
		HSSFCellStyle cStyle = templateWorkbook.createCellStyle();  
		cStyle.setFont(font);
		cStyle.setBorderBottom(ExtendedFormatRecord.THIN);
		cStyle.setAlignment(ExtendedFormatRecord.RIGHT);

		
		Row dateRow = sheet.getRow(date_row);
		dateRow.createCell(1).setCellValue(Common_util.dateFormat.format(startDate));
		dateRow.createCell(3).setCellValue(Common_util.dateFormat.format(endDate));

		//write cust infmration
		int totalDataRow = items.size();

		for (int i = 0; i < totalDataRow; i++){
			Row row = sheet.createRow(data_row + i);

			HeadQExpenseRptElesVO item = items.get(i);
			
			row.createCell(CATEGORY_LEVEL_1_COL).setCellValue(item.getParentExpenseTypeName());
			row.createCell(CATEGORY_LEVEL_2_COL).setCellValue(item.getName());
			if (item.getCash() != 0)
			   row.createCell(CASH_COL).setCellValue(item.getCash());
			else 
			   row.createCell(CASH_COL).setCellValue("-");

			if (item.getCard() != 0)
				   row.createCell(CARD_COL).setCellValue(item.getCard());
			else 
				   row.createCell(CARD_COL).setCellValue("-");
			
			if (item.getAlipay() != 0)
				   row.createCell(ALIPAY_COL).setCellValue(item.getAlipay());
			else 
				   row.createCell(ALIPAY_COL).setCellValue("-");
			
			if (item.getWechat() != 0)
				   row.createCell(WECHAT_COL).setCellValue(item.getWechat());
			else 
				   row.createCell(WECHAT_COL).setCellValue("-");
			
			if (item.getTotal() != 0)
				   row.createCell(TOTAL_COL).setCellValue(item.getTotal());
			else 
				   row.createCell(TOTAL_COL).setCellValue("-");

			
			if (item.getParentId() == 0 || item.getParentId() == 1)
				for (int j = 0; j <= TOTAL_COL; j++)
				    row.getCell(j).setCellStyle(cStyle);
		}

		return templateWorkbook;
	}
	
}
