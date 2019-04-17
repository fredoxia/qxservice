package com.onlineMIS.action.headQ.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.onlineMIS.ORM.DAO.Response;

import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrder;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrderProduct;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.loggerLocal;
import com.opensymphony.xwork2.ActionContext;

public class InventoryOrderJSPAction  extends InventoryOrderAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2577947504617094207L;

	/**
	 * inventory peopole save the order to draft
	 * @return
	 */
	public String saveToDraft(){
		
		String uuid = Common_util.getUUID();
		loggerLocal.info(logInventory("saveToDraft", formBean.getOrder().getCust().getId(), formBean.getOrder().getOrder_ID(), uuid));

		inventoryService.saveToDraft(formBean.getOrder(), formBean.getSorting());
		
		loggerLocal.infoR(logInventory("saveToDraft", formBean.getOrder().getCust().getId(), formBean.getOrder().getOrder_ID(), uuid));
		
		if (formBean.getOrder().getOrder_type() == InventoryOrder.TYPE_SALES_ORDER_W)
		   return SUCCESS;
		else 
		   return "returnSuccess";
	}
	
	/**
	 * 会计继续修改单据
	 * @return
	 */
	public String acctUpdate(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		
		int orderId = formBean.getOrder().getOrder_ID();
		String uuid = Common_util.getUUID();
		String log = logInventory("acctUpdate", formBean.getOrder().getCust().getId(), orderId, uuid);
		loggerLocal.info(log);

		Response response = new Response();
		
		try {
		    response = inventoryService.loadOrder(orderId, loginUserInfor);
		} catch (Exception e) {
			response.setQuickValue(Response.FAIL, "发生错误 : " + e.getMessage());
			loggerLocal.error(e);
		}
		
		int returnCode = response.getReturnCode();
		
		if (returnCode != Response.SUCCESS){
			addActionError(response.getMessage());
			return "search";
		} else {
			InventoryOrder order = (InventoryOrder)response.getReturnValue();
			uiBean = inventoryService.prepareUIBean();
			formBean.setOrder(order);
			return "edit";
		}
	}
	
	/**
	 * 处理inventory order的通用接口，根据数据库判断当前order状态然后做出适当判断
	 * 1: edit
	 * 2. display
	 * @return
	 */
	public String loadOrder(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		
		int orderId = formBean.getOrder().getOrder_ID();
		String uuid = Common_util.getUUID();
		String log = logInventory("loadOrder", "-", orderId, uuid);
		loggerLocal.info(log);
		
		Response response = new Response();
		
		try {
		    response = inventoryService.loadOrder(orderId, loginUserInfor);
		} catch (Exception e) {
			response.setQuickValue(Response.FAIL, "发生错误 : " + e.getMessage());
			loggerLocal.error(e);
		}
		
		int returnCode = response.getReturnCode();
		int action = response.getAction();
		
		loggerLocal.infoR(log);
		if (returnCode != Response.SUCCESS){
			addActionError(response.getMessage());
			return "search";
		} else {
			InventoryOrder order = (InventoryOrder)response.getReturnValue();
			switch (action) {
				case 1:
					uiBean = inventoryService.prepareUIBean();
					formBean.setOrder(order);
					return "edit";
				case 2:
					formBean.setOrder(order);
					return "retrieveSuccess";						
				default:
					addActionError("无法找到对应页面");
					return "search";
			}
		}
	}


//	public String previewOrder(){
//		
//		int orderId = formBean.getOrder().getOrder_ID();
//		String uuid = Common_util.getUUID();
//		String log = logInventory("previewOrder", formBean.getOrder().getClient_id(), orderId, uuid);
//		loggerLocal.info(log);
//		
//		if (formBean.getOrder().getClient_id() == 0){
//			addFieldError("clientID.empty", "客户名字必填");
//			return INPUT;
//		} else if (formBean.getOrder().getOrder_Keeper() == null){
//			addFieldError("orderKeeper.empty", "订单输入人员必填");
//			return INPUT;
//		} else if (formBean.getOrder().getOrder_Counter() == null){
//			addFieldError("orderCounter.empty", "订单点数人员必填");
//			return INPUT;
//		} else if (formBean.getOrder().getOrder_scanner() == null){
//			addFieldError("orderScanner.empty", "订单扫描人员必填");
//			return INPUT;
//		} 
//		
//		inventoryService.previewOrder(formBean);
//
//		uiBean = inventoryService.prepareUIBean();
//
//		loggerLocal.infoR(log);
//		
//		return "preview";
//	}
	
	/**
	 * pre create the new inventory sales order
	 * @return
	 */
	public String create(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		
		String uuid = Common_util.getUUID();
		String log = logInventory("create", formBean.getOrder().getCust().getId(), formBean.getOrder().getOrder_ID(), uuid);
		loggerLocal.info(log);
		
		uiBean = inventoryService.prepareUIBean();
		
		formBean.getOrder().setOrder_Keeper(loginUserInfor);
		//formBean.getOrder().setOrder_scanner(loginUserInfor);
		formBean.getOrder().setOrder_type(InventoryOrder.TYPE_SALES_ORDER_W);
		formBean.getOrder().setOrder_Status(InventoryOrder.STATUS_INITIAL);

		loggerLocal.infoR(log);
		return "newOrder";
	}
	
	/**
	 * pre create the new inventory sales return order
	 * @return
	 */
	public String createReturnOrder(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		String uuid = Common_util.getUUID();
		String log = logInventory("createReturnOrder", formBean.getOrder().getCust().getId(), formBean.getOrder().getOrder_ID(), uuid);
		loggerLocal.info(log);
		
		uiBean = inventoryService.prepareUIBean();
		
		formBean.getOrder().setOrder_Keeper(loginUserInfor);
		formBean.getOrder().setOrder_type(InventoryOrder.TYPE_SALES_RETURN_ORDER_W);
		
		loggerLocal.infoR(log);
		return "newOrder";
	}
	
	public String preSearch(){		
		
		String uuid = Common_util.getUUID();
		String log = logInventory("preSearch", "-", "-", uuid);
		loggerLocal.info(log);

		uiBean = inventoryService.preparePreSearchUIBean();
		
		loggerLocal.infoR(log);
		return "search";
	}

	/**
	 * 会计保存单据
	 * @return
	 */
	public String acctSave(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		String uuid = Common_util.getUUID();
		String log = logInventory("acctSave", formBean.getOrder().getCust().getId(), formBean.getOrder().getOrder_ID(), uuid);
		loggerLocal.info(log);
		
		boolean isSuccess = inventoryService.acctEditAndSave(formBean,loginUserInfor);
		
		InventoryOrder order_r = inventoryService.searchByID(formBean.getOrder().getOrder_ID());

		formBean.setOrder(order_r);
		
		loggerLocal.infoR(log);
		return "retrieveSuccess";
	}	

	
	
	/**
	 * 会计审核单据，完成账目和库存的导入
	 * @return
	 */
	public String acctAuditOrder(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		
		int orderId = formBean.getOrder().getOrder_ID();
		
		String uuid = Common_util.getUUID();
		String log = logInventory("acctAuditOrder", formBean.getOrder().getCust().getId(), orderId, uuid);
		loggerLocal.info(log);
		
		Response response = inventoryService.orderCompleteAudit(orderId,loginUserInfor);
		if (response.getReturnCode() != Response.SUCCESS){
			addActionError(response.getMessage());
			uiBean = inventoryService.prepareUIBean();
			formBean.setOrder((InventoryOrder)response.getReturnValue());
			return "retrieveSuccess";
		}

		loggerLocal.infoR(log);
		
		return "inventoryComplete";
	}
	
	/**
	 * action to hong chong order
	 */
	public String cancelOrder(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		int orderId = formBean.getOrder().getOrder_ID();
		String uuid = Common_util.getUUID();
		String log = logInventory("cancelOrder", "-", orderId, uuid);
		loggerLocal.info(log);
		
		if (orderId != 0){
		   Response  response = inventoryService.cancel(loginUserInfor, orderId);
		   loggerLocal.infoR(log + "," + response.getReturnCode());
		   if (response.getReturnCode() ==  Response.FAIL){
			   addActionError(response.getMessage());
			   return "operationError";
		   } else if (response.getReturnCode() == Response.WARNING){
			   addActionError(response.getMessage());
			   return loadOrder();
		   }
		}
		
		loggerLocal.infoR(log);
		return "successToSearch";
	}

	/**
	 * get the PDA draft orders
	 */
	public String getPDADrafts(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		String uuid = Common_util.getUUID();
		String log = logInventory("getPDADrafts","-", "-", uuid);
		loggerLocal.info(log);
		
		List<InventoryOrder> inventoryOrders = inventoryService.getPDADrafts(loginUserInfor);
		
		if (inventoryOrders != null)
			uiBean.setOrders(inventoryOrders);
		
		loggerLocal.infoR(log);
		
		return "pdaDraftList";
	}
	
	/**
	 * get the particular PDA draft order by id
	 */
	public String getPDADraft(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		String uuid = Common_util.getUUID();
		String log = logInventory("getPDADraft", "-", formBean.getOrder().getOrder_ID(), uuid);
		loggerLocal.info(log);
		
		InventoryOrder inventoryOrder = inventoryService.getPDADraft(loginUserInfor, formBean.getOrder().getOrder_ID());
		
		String clientName = inventoryOrder.getCust().getName();
		if (clientName.indexOf(",") != -1)
			clientName = clientName.split(",")[0];
		
		formBean.setOrder(inventoryOrder);
		Map<Integer, String> clientMap = new HashMap<Integer, String>();
		clientMap.put(inventoryOrder.getCust().getId(), clientName);
		formBean.setClientMap(clientMap);
		
		loggerLocal.infoR(log);
		return "pdaOrder";
	}
	

	
	public String createPDAOrder(){
		String uuid = Common_util.getUUID();
		String log = logInventory("createPDAOrder", "-", "-", uuid);
		loggerLocal.info(log);
		
		return "pdaOrder";
	}
	
	/**
	 * pre upload the file
	 * @return
	 */
	public String preUploadFile(){
		return "preUpload";
	}

	private String logInventory(String action, Object clientId, Object orderId, String uuid){
		return super.logInventory(this.getClass().getSimpleName() , action, clientId, orderId, uuid);		
	}

}
