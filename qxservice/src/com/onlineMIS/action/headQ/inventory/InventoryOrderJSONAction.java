package com.onlineMIS.action.headQ.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrder;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrderVO;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.loggerLocal;
import com.onlineMIS.converter.JSONUtil2SQLDateConverter;
import com.onlineMIS.converter.JSONUtilDateConverter;
import com.opensymphony.xwork2.ActionContext;

public class InventoryOrderJSONAction extends InventoryOrderAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1720262593350813729L;
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * the inventory submit the order to the accountants
	 * @return
	 */
	public String save(){
		String uuid = Common_util.getUUID();
		loggerLocal.info(logInventory("save", formBean.getOrder().getCust().getId(), formBean.getOrder().getOrder_ID(), uuid));
		
		if (formBean.getOrder().getCust().getId() == 0){
			addFieldError("clientID.empty", "客户名字必填");
			return INPUT;
		} else if (formBean.getOrder().getOrder_Keeper() == null){
			addFieldError("orderKeeper.empty", "订单输入人员必填");
			return INPUT;
		} else if (formBean.getOrder().getOrder_Counter() == null){
			addFieldError("orderCounter.empty", "订单点数人员必填");
			return INPUT;
		} else if (formBean.getOrder().getOrder_scanner() == null){
			addFieldError("orderScanner.empty", "订单扫描人员必填");
			return INPUT;
		} 
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		
		
		boolean isSuccess = inventoryService.inventoryComplsave(formBean, loginUserInfor);
		
		Response response = new Response();
		if (isSuccess){
			response = inventoryService.getInventoryOrderForPrint(formBean.getOrder().getOrder_ID());
		} else 
			response.setFail("单据保存失败 ");
		
		try{
			   jsonObject = JSONObject.fromObject(response);
		   } catch (Exception e){
				loggerLocal.error(e);
			}

		return "successful";
	}
	
	/**
	 * copy the 
	 * @return
	 */
	public String copyOrder(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		
		String uuid = Common_util.getUUID();
		String log = logInventory("copyOrder", "-", formBean.getOrder().getOrder_ID(), uuid);
		loggerLocal.info(log);
		
		Response response = new Response();
		try {
		    response = inventoryService.copyOrder(loginUserInfor, formBean.getOrder());
		} catch (Exception e) {
			loggerLocal.error(e);
			response.setReturnCode(Response.FAIL);
			response.setMessage(e.getMessage());
		}
		jsonMap.put("response", response);
		
		loggerLocal.infoR(log +"," + response.getReturnCode());
		

		try{
		    jsonObject = JSONObject.fromObject(jsonMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "successful";
	}
	
	
	/**
	 * function to submitt the order from the PDA
	 * @return
	 */
	public String pdaSubmitOrder(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);

		String uuid = Common_util.getUUID();
		String log = logInventory("pdaSubmitOrder", formBean.getOrder().getCust().getId(), formBean.getOrder().getOrder_ID(), uuid);
		loggerLocal.info(log);
		
		Response response = new Response();
		try{
			inventoryService.pdaSaveToInventory(formBean.getOrder(), loginUserInfor);
		    response.setReturnCode(Response.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			loggerLocal.error(formBean.getOrder().getCust().getId() + "");
			loggerLocal.error(e);
			response.setQuickValue(Response.FAIL, e.getMessage());
		}
		
		loggerLocal.info(log + "," + response.getReturnCode());
		jsonMap.put("response", response);
		
		try{
		    jsonObject = JSONObject.fromObject(jsonMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "successful";
	}
	
	/**
	 * PDA submit the draft
	 * @return
	 */
	public String pdaSubmitDraft(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		
		String uuid = Common_util.getUUID();
		String log = logInventory("pdaSubmitDraft", formBean.getOrder().getCust().getId(), formBean.getOrder().getOrder_ID(), uuid);
		loggerLocal.info(log);
		
		Response response = new Response();
		try{
		    response = inventoryService.pdaSaveToDraft(formBean.getOrder(), loginUserInfor);
		    if (formBean.getOrder().getOrder_ID() == 0)
		    	response.setQuickValue(Response.FAIL, "保存出现错误，请重新输入名字");
		} catch (Exception e) {
			loggerLocal.error(formBean.getOrder().getCust().getId() + "," );
			loggerLocal.error(e);
			response.setQuickValue(Response.FAIL, e.getMessage());
		}
		jsonMap.put("response", response);
		
		loggerLocal.info(log + "," + response.getReturnCode());
		
		try{
		    jsonObject = JSONObject.fromObject(jsonMap);
		} catch (Exception e) {
			e.printStackTrace();
			loggerLocal.error(e);
		}
		
		return "successful";
	}
	
	/**
	 * to delete the order by the confirmation
	 * @return
	 */
	public String deleteOrder(){
		int orderId = formBean.getOrder().getOrder_ID();
		String userName = formBean.getUser().getUser_name();
		String password = formBean.getUser().getPassword();
		
		String uuid = Common_util.getUUID();
		String log = logInventory("pdaSubmitDraft", "-", orderId, uuid);
		loggerLocal.info(log);
		
		Response  response = new Response();
		try {
			response = inventoryService.deleteByAuthorization(orderId, userName, password);
		} catch (Exception e) {
			loggerLocal.error(e);
			response.setQuickValue(Response.FAIL, e.getMessage());
		}
		
		loggerLocal.info(log + "," + response.getReturnCode());
		
		jsonMap.put("response", response);
		try{
		    jsonObject = JSONObject.fromObject(jsonMap);
		} catch (Exception e) {
			e.printStackTrace();
			loggerLocal.error(e);
		}
		
		return "successful";
	}
	
	/**
	 * To search the order based on the search criteria
	 * @return
	 */
	public String search(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		
		String uuid = Common_util.getUUID();
		String log = logInventory("search", "-", "-", uuid);
		loggerLocal.info(log);
		
		List<InventoryOrder> orderList = inventoryService.search(formBean);
		jsonMap = inventoryService.constructInventoryOrderVOMap(orderList, loginUserInfor);

		//to excludes the set and list inforamtion
		JsonConfig jsonConfig = new JsonConfig();
		//jsonConfig.setExcludes( new String[]{"issueChainStore"} );
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JSONUtilDateConverter());  

		try{
			   jsonObject = JSONObject.fromObject(jsonMap,jsonConfig);
			   //System.out.println(jsonObject.toString());
		   } catch (Exception e){
				loggerLocal.error(e);
			}
		
		loggerLocal.infoR(log);
		
		return "successful";
	}
	
	public String updateOrderComment(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		
		String uuid = Common_util.getUUID();
		String log = logInventory("search", "-", "-", uuid);
		loggerLocal.info(log);
		
		Response response = inventoryService.updateOrderComment(formBean.getOrder());
		try{
			   jsonObject = JSONObject.fromObject(response);
			   //System.out.println(jsonObject.toString());
		   } catch (Exception e){
				loggerLocal.error(e);
			}
		
		loggerLocal.infoR(log);
		
		return "successful";
	}
	
	/**
	 * 因为单据具有权限控制,仓库分开操作以后必须有权限转移功能
	 * @return
	 */
	public String transferOrderToOther(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		String uuid = Common_util.getUUID();
		String log = logInventory("transferOrderToOther", "-", "-", uuid);
		loggerLocal.info(log);
		
		Response response = inventoryService.transferOrderToOther(formBean.getOrder().getOrder_ID(), loginUserInfor, formBean.getUser());
		try{
			   jsonObject = JSONObject.fromObject(response);
		   } catch (Exception e){
				loggerLocal.error(e);
			}
		
		
		return "successful";
	}
	
	/**
	 * 打印单据
	 * @return
	 */
	public String printOrder(){
		Response response = inventoryService.getInventoryOrderForPrint(formBean.getOrder().getOrder_ID());
		try{
			   jsonObject = JSONObject.fromObject(response);
		   } catch (Exception e){
				loggerLocal.error(e);
			}

		return "successful";
	}
	
	private String logInventory(String action, Object clientId, Object orderId, String uuid){
		return super.logInventory(this.getClass().getSimpleName() , action, clientId, orderId, uuid);		
	}
}
