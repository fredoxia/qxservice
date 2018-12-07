package com.onlineMIS.action.headQ.supplier.purchase;

import org.springframework.beans.factory.annotation.Autowired;

import com.onlineMIS.ORM.DAO.chainS.report.ChainReportService;
import com.onlineMIS.ORM.DAO.headQ.finance.FinanceService;
import com.onlineMIS.ORM.DAO.headQ.supplier.finance.FinanceSupplierService;
import com.onlineMIS.action.BaseAction;

public class SupplierPurchaseAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2328444611965367761L;
	@Autowired
	protected FinanceSupplierService financeSupplierService;

	
    protected SupplierPurchaseActionFormBean formBean = new SupplierPurchaseActionFormBean();
    protected SupplierPurchaseActionUIBean uiBean = new SupplierPurchaseActionUIBean();
	public SupplierPurchaseActionFormBean getFormBean() {
		return formBean;
	}
	public void setFormBean(SupplierPurchaseActionFormBean formBean) {
		this.formBean = formBean;
	}
	public SupplierPurchaseActionUIBean getUiBean() {
		return uiBean;
	}
	public void setUiBean(SupplierPurchaseActionUIBean uiBean) {
		this.uiBean = uiBean;
	}
    
}
