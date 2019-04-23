package com.onlineMIS.action.headQ.report;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.BasicDataService;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.ProductBarcodeService;
import com.onlineMIS.ORM.DAO.headQ.custMgmt.HeadQCustMgmtService;
import com.onlineMIS.ORM.DAO.headQ.report.HeadQReportService;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Year;
import com.onlineMIS.action.BaseAction;
import com.onlineMIS.common.loggerLocal;

@Controller
public class HeadQReportAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Autowired
	protected HeadQReportService headQReportService;

	protected HeadQReportFormBean formBean = new HeadQReportFormBean();
	protected HeadQReportUIBean uiBean = new HeadQReportUIBean();

	public HeadQReportFormBean getFormBean() {
		return formBean;
	}

	public void setFormBean(HeadQReportFormBean formBean) {
		this.formBean = formBean;
	}

	public HeadQReportUIBean getUiBean() {
		return uiBean;
	}

	public void setUiBean(HeadQReportUIBean uiBean) {
		this.uiBean = uiBean;
	}

}
