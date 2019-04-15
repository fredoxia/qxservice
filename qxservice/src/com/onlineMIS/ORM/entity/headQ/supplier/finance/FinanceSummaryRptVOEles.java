package com.onlineMIS.ORM.entity.headQ.supplier.finance;

import java.util.Map;
import java.util.Set;

public class FinanceSummaryRptVOEles {
	private String supplier;
	private double cash = 0;
	private double card = 0;
	private double alipay = 0;
	private double wechat = 0;
	private double sum = 0;
	private double discount = 0;
	private double acctFlow = 0;
	
	public FinanceSummaryRptVOEles(){
		
	}
	
	public void add(FinanceBillSupplier bill,Map<Integer, FinanceCategorySupplier> categoryIdMapToItem){
		Set<FinanceBillSupplierItem> items = bill.getFinanceBillItemSet();
		int offset = 1;
		
		switch (bill.getType()) {
			case FinanceBillSupplier.FINANCE_PAID_HQ:
			case FinanceBillSupplier.FINANCE_INCREASE_HQ:
				offset = 1;
				break;				
				
			case FinanceBillSupplier.FINANCE_INCOME_HQ:
			case FinanceBillSupplier.FINANCE_DECREASE_HQ:
				offset = -1;
				break;
	
			default:
				break;
		}
		
		discount += bill.getInvoiceDiscount()*offset;

		for (FinanceBillSupplierItem item : items){
			int categoryId = item.getFinanceCategorySupplier().getId();
			
			int itemId = categoryIdMapToItem.get(categoryId).getType();
			
			double amount = item.getTotal();
			switch (itemId) {
			    case FinanceCategorySupplier.CASH_ACCT_TYPE:
				   cash += offset*amount;
				   sum += offset*amount;
				break;
			    case FinanceCategorySupplier.CARD_ACCT_TYPE:
			       card += offset*amount;
			       sum += offset*amount;
				break;
			    case FinanceCategorySupplier.ALIPAY_ACCT_TYPE:
				   alipay += offset*amount;
				   sum += offset*amount;
				break;
			    case FinanceCategorySupplier.WECHAT_ACCT_TYPE:
			       wechat += offset*amount;
			       sum += offset*amount;
				break;
			    case FinanceCategorySupplier.INCREASE_DECREASE_ACCT_TYPE:
			    	acctFlow += offset*amount;
				break;				
			default:
				break;
			}

		}
	}
	
	public void calculateSum(FinanceSummaryRptVOEles ele){
		cash += ele.getCash();
		card += ele.getCard();
		alipay += ele.getAlipay();
		wechat += ele.getWechat();
		discount += ele.getDiscount();
		acctFlow += ele.getAcctFlow();
		sum += ele.getSum();
	}
	
	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getAcctFlow() {
		return acctFlow;
	}

	public void setAcctFlow(double acctFlow) {
		this.acctFlow = acctFlow;
	}

	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String cust) {
		this.supplier = cust;
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

	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	
	
}
