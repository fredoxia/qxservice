package com.onlineMIS.ORM.entity.headQ.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
public class HeadQCustAcctFlowReportTemplate extends ExcelTemplate{
	private final static String TEMPLATE_FILE_NAME = "HeadQCustAcctFlowReportTemplate.xls";
	private List<HeadQCustAcctFlowReportItem> items = new ArrayList<HeadQCustAcctFlowReportItem>();

	
	private String curretnYearQuarter = "";
	private String lastQuarter = "";
	private Date startDate = null;
	private Date endDate = null;
	protected int data_row = 5;

	protected final int INDEX = 0;
	protected final int CUSTOMER_NAME = 1;
	protected final int LAST_ACCT_BALANCE = 2;
	protected final int CURRENT_ACCT_FLOW = 3;
	protected final int CURRENT_ACCT_BALANCE = 4;
	protected final int CURRENT_PAY =5;
	protected final int CURRENT_DISCOUNT =6;
	protected final int CURRENT_QUARTER_PURCHASE =7;
	protected final int LAST_QUARTER_ACCT_BALANCE =8;
	

    public HeadQCustAcctFlowReportTemplate(File file) throws IOException{
    	super(file);
    }
	
	public HeadQCustAcctFlowReportTemplate(List<HeadQCustAcctFlowReportItem> items, String templateWorkbookPath, Date startDate, Date endDate, String curretnYearQuarter) throws IOException{
		super(templateWorkbookPath + "\\" + TEMPLATE_FILE_NAME);	
		this.items = items;
		this.curretnYearQuarter = curretnYearQuarter;
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

		Row currentYQRow = sheet.getRow(2);
		currentYQRow.createCell(1).setCellValue(curretnYearQuarter);

		
		Row reportDateRow = sheet.getRow(3);
		reportDateRow.createCell(1).setCellValue(Common_util.dateFormat_f.format(Common_util.getToday()));

		//write cust infmration
		int totalDataRow = items.size();

		for (int i = 0; i < totalDataRow; i++){
			Row row = sheet.createRow(data_row + i);

			HeadQCustAcctFlowReportItem item = items.get(i);
			
			row.createCell(INDEX).setCellValue(i + 1);
			row.createCell(CUSTOMER_NAME).setCellValue(item.getChainStore().getChain_name());
			row.createCell(LAST_ACCT_BALANCE).setCellValue(item.getLastAcctBalance());
			row.createCell(CURRENT_ACCT_FLOW).setCellValue(item.getCurrentAcctFlow());
			row.createCell(CURRENT_ACCT_BALANCE).setCellValue(item.getCurrentAcctBalance());
			row.createCell(CURRENT_PAY).setCellValue(item.getCurrentPay());
			//row.createCell(CURRENT_DISCOUNT).setCellValue(item.getCurrentDiscount());
			row.createCell(CURRENT_QUARTER_PURCHASE).setCellValue(item.getCurrentQuarterPurchase());
			//row.createCell(LAST_QUARTER_ACCT_BALANCE).setCellValue(item.getLastQuarterAcctBalance());
		}

		return templateWorkbook;
	}
	
}
