package com.onlineMIS.ORM.entity.headQ.report;

import com.onlineMIS.ORM.entity.shared.expense.Expense;
import com.onlineMIS.common.Common_util;

public class HeadQExpenseRptElesVO extends HeadQReportItemVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3839655558463048663L;
	private String parentExpenseTypeName = "";
	private int expenseTypeParentId = 0;
	private int expenseTypeId = 0;
	private double cash = 0;
	private double card = 0;
	private double alipay = 0;
	private double wechat = 0;
	private double total = 0;
	
	public HeadQExpenseRptElesVO(){
		this.setId(Common_util.getUUID());
	}
	
	public HeadQExpenseRptElesVO(String name, int parentId, String stateClosed) {
		this.setId(Common_util.getUUID());
		this.setName(name);
		this.setParentId(parentId);
		this.setState(stateClosed);
	}
	
	


	public String getParentExpenseTypeName() {
		return parentExpenseTypeName;
	}

	public void setParentExpenseTypeName(String parentExpenseTypeName) {
		this.parentExpenseTypeName = parentExpenseTypeName;
	}

	public int getExpenseTypeId() {
		return expenseTypeId;
	}

	public void setExpenseTypeId(int expenseTypeId) {
		this.expenseTypeId = expenseTypeId;
	}

	public int getExpenseTypeParentId() {
		return expenseTypeParentId;
	}

	public void setExpenseTypeParentId(int expenseTypeParentId) {
		this.expenseTypeParentId = expenseTypeParentId;
	}

	public double getCash() {
		return cash;
	}
	public void setCash(double cash) {
		this.cash = cash;
	}
	public double getCard() {
		return card;
	}
	public void setCard(double card) {
		this.card = card;
	}
	public double getAlipay() {
		return alipay;
	}
	public void setAlipay(double alipay) {
		this.alipay = alipay;
	}
	public double getWechat() {
		return wechat;
	}
	public void setWechat(double wechat) {
		this.wechat = wechat;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}

	public void putValue(int feeType, double amount) {
		if (feeType == Expense.feeTypeE.CASH.getValue())
			this.setCash(amount);
		else if (feeType == Expense.feeTypeE.CARD.getValue())
			this.setCard(amount);
		else if (feeType == Expense.feeTypeE.ALIPAY.getValue())
			this.setAlipay(amount);
		else if (feeType == Expense.feeTypeE.WECHAT.getValue())
			this.setWechat(amount);
		
		this.total += amount;
	}
	
	
}
