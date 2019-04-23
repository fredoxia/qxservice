package com.onlineMIS.action.headQ.report;

import java.util.HashMap;
import java.util.Map;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.common.loggerLocal;

public class HeadQReportJSPAction extends HeadQReportAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4965927215338212593L;

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
}
