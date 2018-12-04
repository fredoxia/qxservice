package com.onlineMIS.action.headQ.custMgmt;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.common.loggerLocal;

public class HeadQCustMgmtJSONAction extends HeadQCustMgmtAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5466084514657131241L;
	private JSONObject jsonObject;
	private JSONArray jsonArray;
	private Map<String,Object> jsonMap = new HashMap<String, Object>();
	public JSONArray getJsonArray() {
		return jsonArray;
	}
	public void setJsonArray(JSONArray jsonArray) {
		this.jsonArray = jsonArray;
	}
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	/**
	 * 翻页的时候查询客户信息
	 * @return
	 */
	public String listCustData(){
		loggerLocal.info("HeadQSalesJSONAction.listCustData");
		
		Response response = new Response();
		try {
		    response = headQSalesService.listCust(formBean.getCust(), this.getPage(), this.getRows(), this.getSort(), this.getOrder());
		} catch (Exception e) {
			loggerLocal.error(e);
			response.setReturnCode(Response.FAIL);
		}
		
		if (response.getReturnCode() == Response.SUCCESS){
//			JsonConfig jsonConfig = new JsonConfig();
//			
//			jsonConfig.setExcludes( new String[]{"activeDate"} );
			
			jsonMap = (Map)response.getReturnValue();
			try {
				jsonObject = JSONObject.fromObject(jsonMap);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * 新增或者更新客户信息
	 * @return
	 */
	public String createUpdateCust(){
		loggerLocal.info("HeadQSalesJSONAction.createUpdateCust");
		Response response = new Response();
		
		try {
		    response = headQSalesService.createOrUpdateCust(formBean.getCust());
		} catch (Exception e) {
			loggerLocal.error(e);
			response.setReturnCode(Response.FAIL);
		}

	    jsonObject = JSONObject.fromObject(response);
		
		return SUCCESS;
	}
	
	/**
	 * 查询客户信息不需要翻页
	 * @return
	 */
	public String searchCustData(){
		loggerLocal.info("HeadQSalesJSONAction.listCustData");
		
		Response response = new Response();
		try {
		    response = headQSalesService.searchCust(formBean.getCust());
		} catch (Exception e) {
			loggerLocal.error(e);
			response.setReturnCode(Response.FAIL);
		}
		
		if (response.getReturnCode() == Response.SUCCESS){

			try {
				jsonObject = JSONObject.fromObject(response);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		return SUCCESS;
	}
	
	/**
	 * 冻结某个账户
	 * @return
	 */
//	public String disbleCust(){
//		loggerLocal.info("HeadQSalesJSONAction.disbleCust");
//		Response response = new Response();
//		
//		try {
//		    response = headQSalesService.disableCust(formBean.getCust().getId());
//		} catch (Exception e) {
//			loggerLocal.error(e);
//			response.setReturnCode(Response.FAIL);
//		}
//
//	    jsonObject = JSONObject.fromObject(response);
//		
//		return SUCCESS;
//	}
	
	
}
