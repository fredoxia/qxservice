package com.onlineMIS.action.headQ.finance;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.headQ.finance.FinanceService;
import com.onlineMIS.ORM.entity.chainS.report.ChainReport;
import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.chainS.user.ChainUserInfor;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBill;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBillItem;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.loggerLocal;
import com.onlineMIS.converter.JSONSQLDateConverter;
import com.onlineMIS.converter.JSONUtilDateConverter;
import com.opensymphony.xwork2.ActionContext;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class FinanceJSONAction extends FinanceAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -507154282584622240L;
	protected JSONObject jsonObject;
	protected Map<String,Object> jsonMap = new HashMap<String, Object>();

	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	
	/**
	 * search the finance bills 
	 * @return
	 */
	public String searchFHQBill(){
		List<FinanceBill> fianBills = financeService.searchFHQBills(formBean);
		
		jsonMap.put("bills", fianBills);
		jsonMap.put("pager", formBean.getPager());
		
		//to excludes the set and list inforamtion
		JsonConfig jsonConfig = new JsonConfig();
		
		jsonConfig.setExcludes( new String[]{"financeBillItemSet","financeBillItemList","creatorHq"} );
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JSONUtilDateConverter());  
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new JSONSQLDateConverter());  
		
		try{
			   jsonObject = JSONObject.fromObject(jsonMap,jsonConfig);
//			   System.out.println(jsonObject.toString());
			} catch (Exception e){
				loggerLocal.error(e);
			}
		
		return SUCCESS;
	}
	

	
	/**
	 * json 保存草稿
	 * @return
	 */
	public String saveFHQToDraft(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		Response response = new Response();
		
		if (validateFinanceHQBill(response)){
			
			response = financeService.saveFHQToDraft(formBean.getOrder(),loginUserInfor);
		}
		
		jsonMap.put("response", response);
		try{
			   jsonObject = JSONObject.fromObject(jsonMap);
//			   System.out.println(jsonObject.toString());
			} catch (Exception e){
				loggerLocal.error(e);
			}
		
		return SUCCESS;
		
	}
	
	/**
	 * 单据过账action
	 * @return
	 */
	public String postFHQBill(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		Response response = new Response();
		
		if (validateFinanceHQBill(response)){

			try {
			    response = financeService.postFHQBill(formBean.getOrder(), loginUserInfor);
			} catch (Exception e) {
				response.setQuickValue(Response.ERROR, e.getMessage());
				loggerLocal.error(e);
			}
		} 
		
		jsonMap.put("response", response);
		try{
			   jsonObject = JSONObject.fromObject(jsonMap);
			} catch (Exception e){
				loggerLocal.error(e);
			}
		
		return SUCCESS;
		
	}
	
	/**
	 * 取得连锁店当前欠款
	 * @return
	 */
	public String getChainAcctFinance(){
		int custId = formBean.getOrder().getCust().getId();
		Response response = new Response();
		
		try {
		    response = financeService.getChainCurrentFinance(custId);
		} catch (Exception e) {
			response.setQuickValue(Response.FAIL, "获取欠款发生错误，" + e.getMessage());
		}
		
		jsonMap.put("response", response);
		try{
			   jsonObject = JSONObject.fromObject(jsonMap);
			} catch (Exception e){
				loggerLocal.error(e);
			}
		
		return SUCCESS;
	}
	
	/**
	 * to validate the finance bill
	 * @return
	 */
	private boolean validateFinanceHQBill(Response response){
		FinanceBill bill = formBean.getOrder();
		HeadQCust cust =  bill.getCust();
		if (cust == null || cust.getId() == 0){
			response.setFail("请选择一个店铺再继续");
			return false;
		}
		
		for (FinanceBillItem billItem: bill.getFinanceBillItemList()){
			if (billItem.getTotal() < 0){
				response.setQuickValue(Response.FAIL,"金额必须是大于或等于零的数字");
				return false;
			}   
		}
		return true;
	}
	
	/**
	 * 查询帐户流水
	 * @return
	 */
	public String searchAcctFlow(){
  
		Date startDate = Common_util.formStartDate(formBean.getSearchStartTime());
		Date endDate = Common_util.formEndDate(formBean.getSearchEndTime());
		int clientId = formBean.getOrder().getCust().getId();
		
		Response response = new Response();
		
		try {
			response = financeService.searchAcctFlow(startDate, endDate, clientId, false);
		} catch (Exception e) {
			response.setQuickValue(Response.FAIL, e.getMessage());
		}

		jsonMap =  (Map<String, Object>)response.getReturnValue();
		JsonConfig jsonConfig = new JsonConfig();

		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JSONUtilDateConverter());  
		jsonConfig.registerJsonValueProcessor(java.sql.Date.class, new JSONSQLDateConverter());
		
		try{
			   jsonObject = JSONObject.fromObject(jsonMap,jsonConfig);
			} catch (Exception e){
				loggerLocal.error(e);
			}
		
		return SUCCESS;
	}

	
	/**
	 * the action to generate the finance report
	 * @return
	 */
	public String generateFinanceReport(){
		Response response = new Response();
		try {
			//ChainReport chainReport = chainReportService.generateFinanceReport(formBean.getOrder().getCust().getId(), formBean.getSearchStartTime(), formBean.getSearchEndTime());
		    
			response = financeService.generateFinanceSummaryRpt(formBean);
		} catch (Exception e) {
			loggerLocal.error(e);
			response.setReturnCode(Response.FAIL);
		}
		try{
			jsonMap = (Map<String, Object>)response.getReturnValue();

			jsonObject = JSONObject.fromObject(jsonMap);
		} catch (Exception e){

			loggerLocal.error(e);
		}
		
		return SUCCESS;
	}
	
	/**
	 * 打印单据
	 * @return
	 */
	public String printOrder(){
		Response response = financeService.printFinanceBill(formBean.getOrder().getId());
		try{
			   jsonObject = JSONObject.fromObject(response);
		   } catch (Exception e){
				loggerLocal.error(e);
			}

		return SUCCESS;
	}
}
