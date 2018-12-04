package com.onlineMIS.action.headQ.supplier.finance;

import org.springframework.beans.factory.annotation.Autowired;

import com.onlineMIS.ORM.DAO.chainS.report.ChainReportService;
import com.onlineMIS.ORM.DAO.headQ.finance.FinanceService;
import com.onlineMIS.ORM.DAO.headQ.supplier.finance.FinanceSupplierService;
import com.onlineMIS.action.BaseAction;

public class FinanceSupplierAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2328444611965367761L;
	@Autowired
	protected FinanceSupplierService financeSupplierService;

	
    protected FinanceSupplierActionFormBean formBean = new FinanceSupplierActionFormBean();
    protected FinanceSupplierActionUIBean uiBean = new FinanceSupplierActionUIBean();
	public FinanceSupplierActionFormBean getFormBean() {
		return formBean;
	}
	public void setFormBean(FinanceSupplierActionFormBean formBean) {
		this.formBean = formBean;
	}
	public FinanceSupplierActionUIBean getUiBean() {
		return uiBean;
	}
	public void setUiBean(FinanceSupplierActionUIBean uiBean) {
		this.uiBean = uiBean;
	}
    
}
