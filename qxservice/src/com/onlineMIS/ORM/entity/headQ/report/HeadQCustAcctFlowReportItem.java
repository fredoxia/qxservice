package com.onlineMIS.ORM.entity.headQ.report;

import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;

public class HeadQCustAcctFlowReportItem {
	private HeadQCust cust ;
	private ChainStore chainStore;
	//上期欠款
	private double lastAcctBalance = 0; 
	//本期发生
	//这个时间段产生的采购金额-付款
	private double currentAcctFlow = 0; 
	//期末欠款
	private double currentAcctBalance = 0; 
	//本期付款
	private double currentPay = 0; 
	//本期返点/折让
	private double currentDiscount = 0; 
	//当季采购
	private double currentQuarterPurchase = 0; 
	//上季欠款
	private double lastQuarterAcctBalance = 0; 
	
	public HeadQCustAcctFlowReportItem(){
		
	}
	
	public HeadQCustAcctFlowReportItem(Double lastAcctBalance, Double currentNetPurchase, Double currentAcctBalance, Double currentPay, Double currentQuarterPurchase){
		if (lastAcctBalance != null)
			this.setLastAcctBalance(lastAcctBalance);
		
		if (currentAcctBalance != null)
			this.setCurrentAcctBalance(currentAcctBalance);
		
		if (currentPay !=null)
			this.setCurrentPay(currentPay);
		
		if (currentQuarterPurchase != null)
			this.setCurrentQuarterPurchase(currentQuarterPurchase);
		
		if (currentNetPurchase == null)
			this.setCurrentAcctFlow(0-this.getCurrentPay());
		else 
			this.setCurrentAcctFlow(currentNetPurchase-this.getCurrentPay());
	}
	
	public HeadQCust getCust() {
		return cust;
	}
	public void setCust(HeadQCust cust) {
		this.cust = cust;
	}
	public double getLastAcctBalance() {
		return lastAcctBalance;
	}
	public void setLastAcctBalance(double lastAcctBalance) {
		this.lastAcctBalance = lastAcctBalance;
	}
	public double getCurrentAcctFlow() {
		return currentAcctFlow;
	}
	public void setCurrentAcctFlow(double currentAcctFlow) {
		this.currentAcctFlow = currentAcctFlow;
	}
	public double getCurrentAcctBalance() {
		return currentAcctBalance;
	}
	public void setCurrentAcctBalance(double currentAcctBalance) {
		this.currentAcctBalance = currentAcctBalance;
	}
	public double getCurrentPay() {
		return currentPay;
	}
	public void setCurrentPay(double currentPay) {
		this.currentPay = currentPay;
	}
	public double getCurrentDiscount() {
		return currentDiscount;
	}
	public void setCurrentDiscount(double currentDiscount) {
		this.currentDiscount = currentDiscount;
	}
	public double getCurrentQuarterPurchase() {
		return currentQuarterPurchase;
	}
	public void setCurrentQuarterPurchase(double currentQuarterPurchase) {
		this.currentQuarterPurchase = currentQuarterPurchase;
	}
	public double getLastQuarterAcctBalance() {
		return lastQuarterAcctBalance;
	}
	public void setLastQuarterAcctBalance(double lastQuarterAcctBalance) {
		this.lastQuarterAcctBalance = lastQuarterAcctBalance;
	}
	public ChainStore getChainStore() {
		return chainStore;
	}
	public void setChainStore(ChainStore chainStore) {
		this.chainStore = chainStore;
	}
	
	
}
