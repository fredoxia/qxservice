package com.onlineMIS.action.headQ.report;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.loggerLocal;
import com.opensymphony.xwork2.ActionContext;

public class HeadQReportJSPAction extends HeadQReportAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4965927215338212593L;
	private String excelFileName = "";
	private InputStream excelStream;
	
	public InputStream getExcelStream() {
		return excelStream;
	}

	public void setExcelStream(InputStream excelStream) {
		this.excelStream = excelStream;
	}

	public String getExcelFileName() {
		   return this.excelFileName;
	}

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = Common_util.encodeAttachment(excelFileName);
	}
	/**
	 * 总部采购报表
	 * @return
	 */
	public String preGeneratePurchaseReport(){
		loggerLocal.info(this.getClass().getName()+ ".preGeneratePurchaseReport");
    	
		return "purchaseReport";
	}
	
	/**
	 * 总部销售报表
	 * @return
	 */
	public String preGenerateSalesReport(){
		loggerLocal.info(this.getClass().getName()+ ".preGenerateSalesReport");
    	
		return "salesReport";		
	}
	
	/**
	 * 下载 采购统计报表
	 * @return
	 */
	public String downloadPurchaseExcelReport(){
		loggerLocal.info(this.getClass().getName()+ ".downloadPurchaseExcelReport");
		HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);   
		String contextPath= request.getRealPath("/"); 

		Response response = new Response();
		try {
		     response = headQReportService.downloadPurchaseExcelReport(formBean.getParentId(), formBean.getOrder().getSupplier().getId(), formBean.getYear().getYear_ID(), formBean.getQuarter().getQuarter_ID(), formBean.getBrand().getBrand_ID(), formBean.getStartDate(), formBean.getEndDate(),contextPath + "WEB-INF\\template\\headQ");
		} catch (Exception e) {
			response.setReturnCode(Response.FAIL);
			response.setMessage(e.getMessage());
		}
		 
		if (response.getReturnCode() == Response.SUCCESS){
		    InputStream excelStream= (InputStream)response.getReturnValue();
		    this.setExcelStream(excelStream);
		    this.setExcelFileName("CaiGouTongJiBaoBiao.xls");
		    return "report"; 
		} else 
			return ERROR;	
	}
	
	/**
	 * 下载 销售统计报表
	 * @return
	 */
	public String downloadSalesExcelReport(){
		loggerLocal.info(this.getClass().getName()+ ".downloadSalesExcelReport");
		HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);   
		String contextPath= request.getRealPath("/"); 

		Response response = new Response();
		try {
		     response = headQReportService.downloadSalesExcelReport(formBean.getParentId(), formBean.getOrder().getCust().getId(), formBean.getYear().getYear_ID(), formBean.getQuarter().getQuarter_ID(), formBean.getBrand().getBrand_ID(), formBean.getStartDate(), formBean.getEndDate(),contextPath + "WEB-INF\\template\\headQ");
		} catch (Exception e) {
			response.setReturnCode(Response.FAIL);
			response.setMessage(e.getMessage());
		}
		 
		if (response.getReturnCode() == Response.SUCCESS){
		    InputStream excelStream= (InputStream)response.getReturnValue();
		    this.setExcelStream(excelStream);
		    this.setExcelFileName("XiaoShouTongJiBaoBiao.xls");
		    return "report"; 
		} else 
			return ERROR;	
	}
}
