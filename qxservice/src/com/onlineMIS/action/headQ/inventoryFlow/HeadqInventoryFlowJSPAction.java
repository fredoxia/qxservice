package com.onlineMIS.action.headQ.inventoryFlow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.chainS.ChainUtility;
import com.onlineMIS.ORM.DAO.chainS.inventoryFlow.ChainInventoryFlowOrderService;
import com.onlineMIS.ORM.DAO.chainS.user.ChainStoreService;
import com.onlineMIS.ORM.entity.chainS.inventoryFlow.ChainInvenTraceInfoVO;
import com.onlineMIS.ORM.entity.chainS.inventoryFlow.ChainInventoryFlowOrder;
import com.onlineMIS.ORM.entity.chainS.inventoryFlow.ChainInventoryFlowOrderProduct;
import com.onlineMIS.ORM.entity.chainS.sales.ChainStoreSalesOrder;
import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.chainS.user.ChainUserInfor;
import com.onlineMIS.ORM.entity.chainS.vip.ChainVIPCard;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadqInvenTraceInfoVO;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.action.BaseAction;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.loggerLocal;
import com.onlineMIS.converter.JSONUtilDateConverter;
import com.onlineMIS.filter.SystemParm;
import com.onlineMIS.sorter.ChainInveProductSort;
import com.opensymphony.xwork2.ActionContext;

/**
 * action to 
 * @author fredo
 *
 */
@SuppressWarnings("serial")
public class HeadqInventoryFlowJSPAction extends HeadqInventoryFlowAction{
	private final String CHAIN_INVENTORY_REPORT_TEMPLATENAME = "ChainInventoryReportTemplate.xls";
	private final String CHAIN_INVENTORY_REPORT_NAME = "KuCunBiao.xls";
	
	public String preInventoryRpt(){
		
		return "preInventoryRpt";
	}
	
	/**
	 * 下载库存报表
	 * @return
	 */
	public String downloadHeadqInventory(){
		loggerLocal.info("HeadqInventoryFlowJSPAction - downloadFlowOrder");
		HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);   
		String contextPath= request.getRealPath("/"); 

		Response response = new Response();
		try {
			response = headqInventoryService.generateInventoryExcelReport(formBean.getStoreId(), formBean.getYearId(), formBean.getQuarterId(), formBean.getBrandId(), contextPath + "WEB-INF\\template\\" + CHAIN_INVENTORY_REPORT_TEMPLATENAME);     
		} catch (Exception e) {
			response.setReturnCode(Response.FAIL);
			response.setMessage(e.getMessage());
		}
 
		if (response.getReturnCode() == Response.SUCCESS){
		    InputStream excelStream= (InputStream)response.getReturnValue();
		    formBean.setFileStream(excelStream);
		    formBean.setFileName(CHAIN_INVENTORY_REPORT_NAME);
		    return "download"; 
		} else 
			return ERROR;
	}
	
	/**
	 * 打开库存跟踪
	 * @return
	 */
	public String openInvenTracePage(){
		loggerLocal.info("HeadqInventoryFlowJSPAction - openInvenTracePage");
		Response response = headqInventoryService.getInventoryTraceInfor(formBean.getStoreId(), formBean.getPbId());
		if (response.getReturnCode() == Response.SUCCESS){
			Map<String, List> data = (Map<String, List>)response.getReturnValue();
			List<HeadqInvenTraceInfoVO> traceVOs = data.get("rows");
			List<HeadqInvenTraceInfoVO> footers = data.get("footer");
			
			if (footers != null && footers.size() >0)
				uiBean.setTraceFooter(footers.get(0));
			
			uiBean.setTraceItems(traceVOs);
		}
		return "OpenCheckInventoryTracePage";
	}
	
}
