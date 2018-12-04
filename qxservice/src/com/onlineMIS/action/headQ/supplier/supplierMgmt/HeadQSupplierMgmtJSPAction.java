package com.onlineMIS.action.headQ.supplier.supplierMgmt;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;
import com.onlineMIS.common.loggerLocal;

public class HeadQSupplierMgmtJSPAction extends HeadQSupplierMgmtAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4965927215338212593L;


	/**
	 * 进入list cust 页面
	 * @return
	 */
	public String preSupplier(){
		loggerLocal.info("HeadQSupplierMgmtJSPAction.preSupplier");
		
		return "listSupplier";
		
	}
	
	/**
	 * 准备添加或者修改某个客户
	 * @param headQCust
	 * @return
	 */
	public String preEditSupplier(){
		loggerLocal.info("HeadQSupplierMgmtJSPAction.preEditSupplier");
		
		Response response = headQSupplierService.preEditSupplier(formBean.getSupplier());
		if (response.isSuccess()){
			HeadQSupplier supplier = (HeadQSupplier)response.getReturnValue();
			formBean.setSupplier(supplier);
		} else 
			addActionError(response.getMessage());
		
		return "editSupplier";
	}





}
