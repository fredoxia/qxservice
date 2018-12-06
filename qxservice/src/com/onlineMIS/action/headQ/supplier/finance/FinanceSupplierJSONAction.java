package com.onlineMIS.action.headQ.supplier.finance;

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
import com.onlineMIS.ORM.entity.headQ.supplier.finance.FinanceBillSupplier;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.loggerLocal;
import com.onlineMIS.converter.JSONSQLDateConverter;
import com.onlineMIS.converter.JSONUtilDateConverter;
import com.opensymphony.xwork2.ActionContext;

import antlr.CommonToken;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class FinanceSupplierJSONAction extends FinanceSupplierAction {

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
	 * 取得供应商当前欠款
	 * @return
	 */
	public String getSupplierAcctFinance(){
		int supplierId = formBean.getOrder().getSupplier().getId();
		Response response = new Response();
		
		try {
		    response = financeSupplierService.getSupplierCurrentFinance(supplierId);
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
	 * 保存单据到草稿
	 * @return
	 */
	public String saveFBToDraft(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		
		Response response = new Response();
		
		try {
		    response = financeSupplierService.saveFBToDraft(formBean.getOrder(), loginUserInfor);
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
	 * 单据过账
	 * @return
	 */
	public String postFB(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		
		Response response = new Response();
		

		try {
		    response = financeSupplierService.postFB(formBean.getOrder(), loginUserInfor);
		} catch (Exception e) {
			response.setQuickValue(Response.ERROR, e.getMessage());
			loggerLocal.error(e);
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
	 * 红冲单据
	 * @return
	 */
	public String cancelFB(){
		
		return SUCCESS;
	}
	
	/**
	 * 删除单据
	 * @return
	 */
	public String deleteFB(){
		
		return SUCCESS;
	}
	
	/**
	 * search the finance bills 
	 * @return
	 */
	public String searchFHQBill(){
		List<FinanceBillSupplier> fianBills = financeSupplierService.searchFHQBills(formBean);
		
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
	 * 查询帐户流水
	 * @return
	 */
	public String searchAcctFlow(){
  
		Date startDate = Common_util.formStartDate(formBean.getSearchStartTime());
		Date endDate = Common_util.formEndDate(formBean.getSearchEndTime());
		int supplierId = formBean.getOrder().getSupplier().getId();
		
		Response response = new Response();
		
		try {
			response = financeSupplierService.searchAcctFlow(startDate, endDate, supplierId, false);
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
}
