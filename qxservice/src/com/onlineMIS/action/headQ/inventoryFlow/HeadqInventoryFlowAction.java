package com.onlineMIS.action.headQ.inventoryFlow;

import org.springframework.beans.factory.annotation.Autowired;
import com.onlineMIS.ORM.DAO.chainS.inventoryFlow.ChainInventoryFlowOrderService;
import com.onlineMIS.ORM.DAO.headQ.inventory.HeadqInventoryService;
import com.onlineMIS.action.BaseAction;

/**
 * action of the chain inventory flow 
 * @author fredo
 *
 */
@SuppressWarnings("serial")
public class HeadqInventoryFlowAction extends BaseAction{
	protected HeadqInventoryFlowFormBean formBean = new HeadqInventoryFlowFormBean();
	protected HeadqInventoryFlowUIBean uiBean = new HeadqInventoryFlowUIBean();

	@Autowired
	protected HeadqInventoryService headqInventoryService;

	public HeadqInventoryFlowFormBean getFormBean() {
		return formBean;
	}
	public void setFormBean(HeadqInventoryFlowFormBean formBean) {
		this.formBean = formBean;
	}
	public HeadqInventoryFlowUIBean getUiBean() {
		return uiBean;
	}
	public void setUiBean(HeadqInventoryFlowUIBean uiBean) {
		this.uiBean = uiBean;
	}

	
}
