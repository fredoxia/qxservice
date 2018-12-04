package com.onlineMIS.action.headQ.custMgmt;

import java.util.HashMap;
import java.util.Map;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.common.loggerLocal;

public class HeadQCustMgmtJSPAction extends HeadQCustMgmtAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4965927215338212593L;


	/**
	 * 进入list cust 页面
	 * @return
	 */
	public String preCust(){
		loggerLocal.info("HeadQSalesJSPAction.preCust");
		
		return "listCust";
		
	}
	
	/**
	 * 准备添加或者修改某个客户
	 * @param headQCust
	 * @return
	 */
	public String preEditCust(){
		loggerLocal.info("HeadQSalesJSPAction.preEditCust");
		
		Response response = headQSalesService.preEditCust(formBean.getCust());
		if (response.isSuccess()){
			HeadQCust cust = (HeadQCust)response.getReturnValue();
			formBean.setCust(cust);
		} else 
			addActionError(response.getMessage());
		
		return "editCust";
	}





}
