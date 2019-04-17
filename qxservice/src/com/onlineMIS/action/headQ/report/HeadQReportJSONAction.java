package com.onlineMIS.action.headQ.report;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.common.loggerLocal;

public class HeadQReportJSONAction extends HeadQReportAction {

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
