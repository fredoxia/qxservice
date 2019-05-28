package com.onlineMIS.ORM.entity.headQ.finance;

import java.util.Map;
import java.util.Set;

public class FinanceSummaryRptVOEles {
	private String cust;
	private double cash = 0;
	private double card = 0;
	private double alipay = 0;
	private double wechat = 0;
	private double prepay = 0;
	private double sum = 0;
	private double discount = 0;
	private double acctFlow = 0;
	
	public FinanceSummaryRptVOEles(){
		
	}
	
	public void add(FinanceBill bill,Map<Integer, FinanceCategory> categoryIdMapToItem){
		Set<FinanceBillItem> items = bill.getFinanceBillItemSet();
		int offset = 1;
		
		switch (bill.getType()) {
			case FinanceBill.FINANCE_INCOME_HQ:
			case FinanceBill.FINANCE_INCREASE_HQ:
				offset = 1;
				break;				
			case FinanceBill.FINANCE_PREINCOME_HQ:
				offset = 1;
				prepay += bill.getInvoiceTotal();
				break;
			case FinanceBill.FINANCE_PREINCOME_HQ_R:
				offset = -1;
				prepay -= bill.getInvoiceTotal();
				break;				
			case FinanceBill.FINANCE_PAID_HQ:
			case FinanceBill.FINANCE_DECREASE_HQ:
				offset = -1;
				break;
	
			default:
				break;
		}
		
		discount += bill.getInvoiceDiscount()*offset;

		
		for (FinanceBillItem item : items){
			int categoryId = item.getFinanceCategory().getId();
			
			int itemId = categoryIdMapToItem.get(categoryId).getType();
			
			double amount = item.getTotal();
			switch (itemId) {
			    case FinanceCategory.CASH_ACCT_TYPE:
				   cash += offset*amount;
				   sum += offset*amount;
				break;
			    case FinanceCategory.CARD_ACCT_TYPE:
			       card += offset*amount;
			       sum += offset*amount;
				break;
			    case FinanceCategory.ALIPAY_ACCT_TYPE:
				   alipay += offset*amount;
				   sum += offset*amount;
				break;
			    case FinanceCategory.WECHAT_ACCT_TYPE:
			       wechat += offset*amount;
			       sum += offset*amount;
				break;
			    case FinanceCategory.PREPAY_ACCT_TYPE:
				   prepay -= offset*amount;
				break;
			    case FinanceCategory.INCREASE_DECREASE_ACCT_TYPE:
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
		prepay += ele.getPrepay();
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

	public String getCust() {
		return cust;
	}
	public void setCust(String cust) {
		this.cust = cust;
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
	public double getPrepay() {
		return prepay;
	}
	public void setPrepay(double prepay) {
		this.prepay = prepay;
	}
	public double getSum() {
		return sum;
	}
	public void setSum(double sum) {
		this.sum = sum;
	}
	
	
}
