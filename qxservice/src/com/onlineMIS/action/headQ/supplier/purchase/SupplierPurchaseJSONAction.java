package com.onlineMIS.action.headQ.supplier.purchase;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.objectweb.asm.xwork.tree.TryCatchBlockNode;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.ProductBarcodeService;
import com.onlineMIS.ORM.DAO.headQ.finance.FinanceService;
import com.onlineMIS.ORM.entity.chainS.report.ChainReport;
import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.chainS.user.ChainUserInfor;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBill;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBillItem;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrder;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.FinanceBillSupplier;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.loggerLocal;
import com.onlineMIS.converter.JSONSQLDateConverter;
import com.onlineMIS.converter.JSONUtilDateConverter;
import com.opensymphony.xwork2.ActionContext;

import antlr.CommonToken;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class SupplierPurchaseJSONAction extends SupplierPurchaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -507154282584622240L;
	protected JSONObject jsonObject;
	protected Map<String,Object> jsonMap = new HashMap<String, Object>();

	public JSONObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	
	/**
	 * 保存单据到草稿
	 * @return
	 */
	public String saveToDraft(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		loggerLocal.info(loginUserInfor.getUser_name() + " : SupplierPurchaseJSONAction.saveToDraft");
		
		Response response = new Response();
		try	{
			response = supplierPurchaseService.savePurchaseOrderToDraft(formBean.getOrder(), loginUserInfor);
	    } catch (Exception exception ){
		    exception.printStackTrace();
		    response.setFail(exception.getMessage());
	    }
		try{
			   jsonObject = JSONObject.fromObject(response);
//			   System.out.println(jsonObject.toString());
			} catch (Exception e){
				loggerLocal.error(e);
			}
		
		return SUCCESS;
	}
	
	/**
	 * 单据过账
	 * @return
	 */
	public String saveToComplete(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		loggerLocal.info(loginUserInfor.getUser_name() + " : SupplierPurchaseJSONAction.saveToComplete");
		
		Response response = supplierPurchaseService.savePurchaseOrderToComplete(formBean.getOrder(), loginUserInfor);
		
		try{
			   jsonObject = JSONObject.fromObject(response);
//			   System.out.println(jsonObject.toString());
			} catch (Exception e){
				loggerLocal.error(e);
			}
		
		return SUCCESS;
	}
	
	/**
	 * 红冲过账单据
	 * @return
	 */
	public String cancelPurchase(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		loggerLocal.info(loginUserInfor.getUser_name() + " : SupplierPurchaseJSONAction.cancelPurchase");
		
		Response response = supplierPurchaseService.cancelPurchaseOrder(formBean.getOrder().getId(), loginUserInfor);
		
		try{
			   jsonObject = JSONObject.fromObject(response);
//			   System.out.println(jsonObject.toString());
			} catch (Exception e){
				loggerLocal.error(e);
			}
		
		return SUCCESS;
	}
	
	/**
	 * 删除草稿单据
	 * @return
	 */
	public String deletePurchase(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		loggerLocal.info(loginUserInfor.getUser_name() + " : SupplierPurchaseJSONAction.deletePurchase");
		
		Response response = supplierPurchaseService.deletePurchaseOrderById(formBean.getOrder().getId(), loginUserInfor);
		
		try{
			   jsonObject = JSONObject.fromObject(response);
//			   System.out.println(jsonObject.toString());
			} catch (Exception e){
				loggerLocal.error(e);
			}
		
		return SUCCESS;
	}	
	
	/**
	 * 在采购单据中，通过条码查找货品
	 * @return
	 */
	public String scanByBarcodePurchaseOrder(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		loggerLocal.info(loginUserInfor.getUser_name() + " : SupplierPurchaseJSONAction.scanByBarcodePurchaseOrder");
		
		Response  response = supplierPurchaseService.scanByBarcodePurchaseOrder(formBean.getPb().getBarcode(), formBean.getSupplier().getId(), formBean.getOrder().getType(), formBean.getIndexPage(), formBean.getFromSrc());

		jsonMap = (Map<String, Object>)response.getReturnValue();

		jsonObject = ProductBarcodeService.transferProductBarcode(jsonMap, null);	
		
		return SUCCESS;
	}
	
	/**
	 * 查询采购单据
	 * @return
	 */
	public String searchPurchaseOrder(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		loggerLocal.info(loginUserInfor.getUser_name() + " : SupplierPurchaseJSONAction.searchPurchaseOrder");
		
		
		Response response = supplierPurchaseService.searchPurchaseOrder(formBean);
		
		if (response.isSuccess()){
			jsonMap = (Map<String, Object>)response.getReturnValue();
		}
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(java.util.Date.class, new JSONUtilDateConverter());  

		try{
			   jsonObject = JSONObject.fromObject(jsonMap,jsonConfig);
			   //System.out.println(jsonObject.toString());
		   } catch (Exception e){
				loggerLocal.error(e);
			}

		
		return SUCCESS;
	}
	
}
