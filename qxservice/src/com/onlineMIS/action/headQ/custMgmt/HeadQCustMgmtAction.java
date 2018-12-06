package com.onlineMIS.action.headQ.custMgmt;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.BasicDataService;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.ProductBarcodeService;
import com.onlineMIS.ORM.DAO.headQ.custMgmt.HeadQCustMgmtService;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Year;
import com.onlineMIS.action.BaseAction;
import com.onlineMIS.common.loggerLocal;

@Controller
public class HeadQCustMgmtAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	protected HeadQCustMgmtService headQSalesService;


	protected HeadQCustMgmtFormBean formBean = new HeadQCustMgmtFormBean();
	protected HeadQCustMgmtActionUIBean uiBean = new HeadQCustMgmtActionUIBean();

	public HeadQCustMgmtFormBean getFormBean() {
		return formBean;
	}

	public void setFormBean(HeadQCustMgmtFormBean formBean) {
		this.formBean = formBean;
	}

	public HeadQCustMgmtActionUIBean getUiBean() {
		return uiBean;
	}

	public void setUiBean(HeadQCustMgmtActionUIBean uiBean) {
		this.uiBean = uiBean;
	}

}
