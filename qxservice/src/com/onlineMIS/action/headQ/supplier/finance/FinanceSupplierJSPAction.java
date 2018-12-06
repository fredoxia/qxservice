package com.onlineMIS.action.headQ.supplier.finance;

import org.aspectj.weaver.ResolvedMemberImpl;
import org.springframework.stereotype.Controller;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.chainS.ChainUtility;
import com.onlineMIS.ORM.entity.base.BaseOrder;
import com.onlineMIS.ORM.entity.chainS.user.ChainUserInfor;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBill;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBillItem;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.FinanceBillSupplier;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.loggerLocal;
import com.opensymphony.xwork2.ActionContext;

@Controller
public class FinanceSupplierJSPAction extends FinanceSupplierAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1120066931973798941L;
	

	
	public String preCreate(){
		
		financeSupplierService.prepareCreateBill(formBean, uiBean);

		//System.out.println("---" + formBean.getOrder().getFinanceBillItemList().size());
		
		ChainUtility.calculateParam(formBean, new FinanceBillSupplier());
		
		return "EditFinanceBill";
	}
	
	/**
	 * 删除单据
	 * @return
	 */
	public String deleteFB(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		FinanceBillSupplier financeBill = formBean.getOrder();
		
		if (financeBill.getId() != 0){
		    formBean.getOrder().setCreatorHq(loginUserInfor);
		
		    Response response = financeSupplierService.deleteFB(formBean.getOrder(), loginUserInfor);
		    if (response.isSuccess())			
			   addActionMessage("成功删除单据");
		    else 
		       addActionError("删除单据出现错误 : " + response.getMessage());
		} else {
			
		}
		formBean.initialize();	
		
		return preCreate();	
	}
	
	/**
	 * 红冲单据
	 * @return
	 */
	public String cancelFB(){
		UserInfor loginUserInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
		FinanceBillSupplier financeBill = formBean.getOrder();
		
		if (financeBill.getId() != 0){
		    formBean.getOrder().setCreatorHq(loginUserInfor);
		
		    Response response = financeSupplierService.cancelFB(formBean.getOrder(),loginUserInfor);
		    if (response.getReturnCode() != Response.SUCCESS){
		    	addActionError(response.getMessage());
				
				formBean.initialize();
				return preCreate();	
		    }
		}

		formBean.initialize();
		addActionMessage("成功红冲单据");
		return preCreate();	
	}
	
	/**
	 * to redirect to the head q search page
	 * @return
	 */
	public String preSearchFH(){
		financeSupplierService.prepareCreateBill(formBean, uiBean);

		return "preSearchFinanceBill";
	}

	
	/**
	 * to get the finance bill from the head quarter part
	 * @return
	 */
	public String getFHQ(){
		String destination = "";
		
		Response response = financeSupplierService.getFinanceBillById(formBean.getOrder().getId());
		
		if (!response.isSuccess()){
			addActionError(response.getMessage());
			return "error";
		}
			
		
		FinanceBillSupplier financeBill = (FinanceBillSupplier)response.getReturnValue();	
		int status = financeBill.getStatus();
		switch (status) {
		    case FinanceBill.STATUS_DRAFT: 
		    	formBean.setOrder(financeBill); 
		    	financeSupplierService.prepareEditBill(formBean, financeBill.getSupplier());
		    	destination = "EditFinanceBill"; break;
		    case FinanceBill.STATUS_COMPLETE: 
		    case FinanceBill.STATUS_CANCEL:
		    	uiBean.setOrder(financeBill); 
		    	destination = "ViewFinanceBill"; break;
		    default: destination = "error"; break;
		}
		
		ChainUtility.calculateParam(formBean, financeBill);
		
		return destination;
	}
	
	/**
	 * the user click the Edit bill button to try to edit the bill
	 * @return
	 */
	public String editFB(){
		Response response = financeSupplierService.getFinanceBillById(formBean.getOrder().getId());
		
		if (response.isSuccess()){
			FinanceBillSupplier financeBill = (FinanceBillSupplier)response.getReturnValue();
			formBean.setOrder(financeBill); 
			financeSupplierService.prepareEditBill(formBean, financeBill.getSupplier());
			
			ChainUtility.calculateParam(formBean, financeBill);
			return "EditFinanceBill";
		} else 
			return "error";

	}
	
	/**
	 * 搜索帐户流
	 * @return
	 */
	public String preSearchAcctFlow(){
    	UserInfor userInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
    	loggerLocal.info(userInfor.getUser_name() + "," + this.getClass().getName() + "." + "preSearchAcctFlow");
//    	
////		financeService.prepareSearchAcctFlowUI(uiBean,formBean, userInfor);

		return "preSearchAcctFlow";
	}
	
	/**
	 * 搜索帐户流
	 * @return
	 */
	public String preFinanceRpt(){
//    	UserInfor userInfor = (UserInfor)ActionContext.getContext().getSession().get(Common_util.LOGIN_USER);
//    	loggerLocal.info(userInfor.getUser_name() + "," + this.getClass().getName() + "." + "preFinanceRpt");
    	
		//financeService.prepareSearchAcctFlowUI(uiBean,formBean, userInfor);

		return "preFinanceRpt";
	}	

}
