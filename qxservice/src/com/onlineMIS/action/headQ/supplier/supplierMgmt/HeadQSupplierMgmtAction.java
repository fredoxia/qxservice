package com.onlineMIS.action.headQ.supplier.supplierMgmt;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.BasicDataService;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.ProductBarcodeService;
import com.onlineMIS.ORM.DAO.headQ.custMgmt.HeadQCustMgmtService;
import com.onlineMIS.ORM.DAO.headQ.supplier.supplierMgmt.HeadQSupplierService;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Year;
import com.onlineMIS.action.BaseAction;
import com.onlineMIS.common.loggerLocal;

@Controller
public class HeadQSupplierMgmtAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	protected HeadQSupplierService headQSupplierService;


	protected HeadQSupplierMgmtFormBean formBean = new HeadQSupplierMgmtFormBean();
	protected HeadQSupplierMgmtActionUIBean uiBean = new HeadQSupplierMgmtActionUIBean();

	public HeadQSupplierMgmtFormBean getFormBean() {
		return formBean;
	}

	public void setFormBean(HeadQSupplierMgmtFormBean formBean) {
		this.formBean = formBean;
	}

	public HeadQSupplierMgmtActionUIBean getUiBean() {
		return uiBean;
	}

	public void setUiBean(HeadQSupplierMgmtActionUIBean uiBean) {
		this.uiBean = uiBean;
	}

}
