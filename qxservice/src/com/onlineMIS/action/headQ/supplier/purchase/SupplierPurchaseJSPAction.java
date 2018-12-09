package com.onlineMIS.action.headQ.supplier.purchase;

import org.aspectj.weaver.ResolvedMemberImpl;
import org.springframework.stereotype.Controller;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.chainS.ChainUtility;
import com.onlineMIS.ORM.entity.base.BaseOrder;
import com.onlineMIS.ORM.entity.chainS.user.ChainUserInfor;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBill;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBillItem;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.FinanceBillSupplier;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.loggerLocal;
import com.opensymphony.xwork2.ActionContext;

@Controller
public class SupplierPurchaseJSPAction extends SupplierPurchaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1120066931973798941L;
	
	/**
	 * 获取单据
	 * @return
	 */
	public String getPurchaseOrder(){
	    UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		int orderId = formBean.getOrder().getId();
		loggerLocal.info(loginUserInfor.getUser_name() + " : SupplierPurchaseJSPAction.getPurchaseOrder , " + orderId);
		
		Response response = supplierPurchaseService.loadPurchaseOrder(orderId, loginUserInfor);
		
		if (!response.isSuccess()){
			addActionError(response.getMessage());
			return ERROR;
		} else {
		    int action = response.getAction();
		    switch (action) {
				case 1:
					PurchaseOrder order = (PurchaseOrder)response.getReturnValue();
					formBean.setOrder(order);
					
					supplierPurchaseService.prepareEditPurchasePage(uiBean);
					
					return preEditPurchase();
				case 2:
					PurchaseOrder order2 = (PurchaseOrder)response.getReturnValue();
					formBean.setOrder(order2);
					return "viewPurchase";
				default:
					return ERROR;
			}
		}
	}
	
	/**
	 * 进入purchase order搜索页面
	 * @return
	 */
	public String preSearchPurchase(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		loggerLocal.info(loginUserInfor.getUser_name() + " : SupplierPurchaseJSPAction.preSearchPurchase");
		
		supplierPurchaseService.prepareSearchPurchasePage(uiBean);
		
		return "searchPurchase";
	}
	
	/**
	 * 
	 * @return
	 */
	public String preEditPurchase(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		loggerLocal.info(loginUserInfor.getUser_name() + " : SupplierPurchaseJSPAction.preEditPurchase");
		
		supplierPurchaseService.prepareEditPurchasePage(uiBean);
		
		return "editPurchase";
	}

}
