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
	 * 总部销售报表
	 * @return
	 */
	public String preGenerateCustAcctFlowReport(){
		loggerLocal.info(this.getClass().getName()+ ".preGenerateCustAcctFlowReport");
		
		headQReportService.prepareAcctFlowReportUI(formBean, uiBean);
    	
		return "CustAcctFlowReport";		
	}
	
	/**
	 * 总部销售报表
	 * @return
	 */
	public String preGenerateHQExpenseReport(){
		loggerLocal.info(this.getClass().getName()+ ".preGenerateHQExpenseReport");
		
		headQReportService.prepareHQExpenseReportUI(formBean);
    	
		return "HQExpenseReport";		
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
	
	/**
	 * 下载 客户信息 成excel
	 * @return
	 */
	public String downloadCustExcelReport(){
		loggerLocal.info(this.getClass().getName()+ ".downloadCustExcelReport");
		HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);   
		String contextPath= request.getRealPath("/"); 

		Response response = new Response();
		try {
		     response = headQReportService.downloadCustInforExcelReport(contextPath + "WEB-INF\\template\\headQ");
		} catch (Exception e) {
			response.setReturnCode(Response.FAIL);
			response.setMessage(e.getMessage());
		}
		 
		if (response.getReturnCode() == Response.SUCCESS){
		    InputStream excelStream= (InputStream)response.getReturnValue();
		    this.setExcelStream(excelStream);
		    this.setExcelFileName("KeHuXinXi.xls");
		    return "report"; 
		} else 
			return ERROR;	
	}
	
	/**
	 * 下载 供应砂锅内信息 成excel
	 * @return
	 */
	public String downloadSupplierExcelReport(){
		loggerLocal.info(this.getClass().getName()+ ".downloadSupplierExcelReport");
		HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);   
		String contextPath= request.getRealPath("/"); 

		Response response = new Response();
		try {
		     response = headQReportService.downloadSupplierInforExcelReport(contextPath + "WEB-INF\\template\\headQ");
		} catch (Exception e) {
			response.setReturnCode(Response.FAIL);
			response.setMessage(e.getMessage());
		}
		 
		if (response.getReturnCode() == Response.SUCCESS){
		    InputStream excelStream= (InputStream)response.getReturnValue();
		    this.setExcelStream(excelStream);
		    this.setExcelFileName("GongYinShangXinXi.xls");
		    return "report"; 
		} else 
			return ERROR;	
	}
	
	/**
	 * 下载 供应商acct flow 成excel
	 * @return
	 */
	public String downloadSupplierAcctFlowExcel(){
		loggerLocal.info(this.getClass().getName()+ ".downloadSupplierAcctFlowExcelReport");
		HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);   
		String contextPath= request.getRealPath("/"); 

		Response response = new Response();
		try {
		     response = headQReportService.downloadSupplierAcctFlow(contextPath + "WEB-INF\\template\\headQ", formBean.getSearchStartTime(), formBean.getSearchEndTime(), formBean.getOrder().getSupplier().getId());
		} catch (Exception e) {
			response.setReturnCode(Response.FAIL);
			response.setMessage(e.getMessage());
		}
		 
		if (response.getReturnCode() == Response.SUCCESS){
		    InputStream excelStream= (InputStream)response.getReturnValue();
		    this.setExcelStream(excelStream);
		    this.setExcelFileName("GongYinShangWangLaiZhang.xls");
		    return "report"; 
		} else 
			return ERROR;	
	}
	
	/**
	 * 下载 客户acct flow 成excel
	 * @return
	 */
	public String downloadCustAcctFlowExcel(){
		loggerLocal.info(this.getClass().getName()+ ".downloadCustAcctFlowExcelReport");
		HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);   
		String contextPath= request.getRealPath("/"); 

		Response response = new Response();
		try {
		     response = headQReportService.downloadCustAcctFlow(contextPath + "WEB-INF\\template\\headQ", formBean.getSearchStartTime(), formBean.getSearchEndTime(), formBean.getOrder().getCust().getId());
		} catch (Exception e) {
			response.setReturnCode(Response.FAIL);
			response.setMessage(e.getMessage());
		}
		 
		if (response.getReturnCode() == Response.SUCCESS){
		    InputStream excelStream= (InputStream)response.getReturnValue();
		    this.setExcelStream(excelStream);
		    this.setExcelFileName("KeHuWangLaiZhang.xls");
		    return "report"; 
		} else 
			return ERROR;	
	}

	/**
	 * 下载 客户acct flow 成excel report
	 * @return
	 */
	public String downloadCustAcctFlowExcelReport(){
		loggerLocal.info(this.getClass().getName()+ ".downloadCustAcctFlowExcelReport");
		HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);   
		String contextPath= request.getRealPath("/"); 

		Response response = new Response();
		try {
		     response = headQReportService.downloadCustAcctFlowReport(contextPath + "WEB-INF\\template\\headQ", formBean.getStartDate(), formBean.getEndDate(), formBean.getYear().getYear_ID(), formBean.getQuarter().getQuarter_ID());
		} catch (Exception e) {
			response.setReturnCode(Response.FAIL);
			response.setMessage(e.getMessage());
		}
		 
		if (response.getReturnCode() == Response.SUCCESS){
		    InputStream excelStream= (InputStream)response.getReturnValue();
		    this.setExcelStream(excelStream);
		    this.setExcelFileName("KeHuWangLaiZhang.xls");
		    return "report"; 
		} else 
			return ERROR;	
	}
	
	/**
	 * 下载 客户acct flow 成excel report
	 * @return
	 */
	public String downloadExpenseExcelReport(){
		loggerLocal.info(this.getClass().getName()+ ".downloadExpenseExcelReport");
		HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);   
		String contextPath= request.getRealPath("/"); 

		Response response = new Response();
		try {
		     response = headQReportService.downloadHeadQExpenseReport(contextPath + "WEB-INF\\template\\headQ", formBean.getStartDate(), formBean.getEndDate());
		} catch (Exception e) {
			response.setReturnCode(Response.FAIL);
			response.setMessage(e.getMessage());
		}
		 
		if (response.getReturnCode() == Response.SUCCESS){
		    InputStream excelStream= (InputStream)response.getReturnValue();
		    this.setExcelStream(excelStream);
		    this.setExcelFileName("ZongBuFeiYongBaoBiao.xls");
		    return "report"; 
		} else 
			return ERROR;	
	}
}
