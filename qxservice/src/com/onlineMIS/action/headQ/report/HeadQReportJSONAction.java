package com.onlineMIS.action.headQ.report;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.entity.chainS.user.ChainUserInfor;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.loggerLocal;
import com.opensymphony.xwork2.ActionContext;

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
	 * 获取采购统计报表的详细信息
	 * @return
	 */
	public String getPurchaseStatisticReptEles(){
		loggerLocal.info(this.getClass().getName()+ ".getPurchaseStatisticReptEles");
		Response response = new Response();

		int supplierId = 0;
		if (formBean.getOrder().getSupplier() == null)
			supplierId = 0;
		else 
			supplierId = formBean.getOrder().getSupplier().getId();
		
		try {
		    response = headQReportService.getPurchaseStatisticReptEles(formBean.getParentId(), formBean.getStartDate(), formBean.getEndDate(), supplierId, formBean.getYear().getYear_ID(), formBean.getQuarter().getQuarter_ID(), formBean.getBrand().getBrand_ID());
		} catch (Exception e){
			e.printStackTrace();
		}	
		
		
		try{
			   jsonArray = JSONArray.fromObject(response.getReturnValue());
//			   System.out.println(jsonArray);
			} catch (Exception e){
				e.printStackTrace();
			}	
		
		return "jsonArray";
	}

	/**
	 * 获取销售统计报表的详细信息
	 * @return
	 */
	public String getSalesStatisticReptEles(){
		loggerLocal.info(this.getClass().getName()+ ".getSalesStatisticReptEles");
		Response response = new Response();

		int custId = 0;
		if (formBean.getOrder().getCust() == null)
			custId = 0;
		else 
			custId = formBean.getOrder().getCust().getId();
		
		try {
		    response = headQReportService.getSalesStatisticReptEles(formBean.getParentId(), formBean.getStartDate(), formBean.getEndDate(), custId, formBean.getYear().getYear_ID(), formBean.getQuarter().getQuarter_ID(), formBean.getBrand().getBrand_ID());
		} catch (Exception e){
			e.printStackTrace();
		}	

		try{
			   jsonArray = JSONArray.fromObject(response.getReturnValue());
//			   System.out.println(jsonArray);
			} catch (Exception e){
				e.printStackTrace();
			}	
		
		return "jsonArray";
	}
	
	/**
	 * 获取消费报表数据
	 * @return
	 */
	public String getHQExpenseReptEles(){
		loggerLocal.info(this.getClass().getName()+ ".getHQExpenseReptEles");
		Response response = new Response();

		
		try {
		    response = headQReportService.getHQExpenseRptEles(formBean.getParentId(), formBean.getStartDate(), formBean.getEndDate(), formBean.getExpenseTypeParentId());
		} catch (Exception e){
			e.printStackTrace();
		}	

		try{
			   jsonArray = JSONArray.fromObject(response.getReturnValue());
//			   System.out.println(jsonArray);
			} catch (Exception e){
				e.printStackTrace();
			}	
		
		return "jsonArray";
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
