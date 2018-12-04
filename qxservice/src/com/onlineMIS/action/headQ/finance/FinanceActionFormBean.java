package com.onlineMIS.action.headQ.finance;

import java.sql.Date;

import com.onlineMIS.ORM.entity.base.Pager;
import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBill;
import com.onlineMIS.action.chainS.ChainActionFormBaseBean;

public class FinanceActionFormBean extends ChainActionFormBaseBean{
	private HeadQCust cust = new HeadQCust();
	private FinanceBill order = new FinanceBill();
	private Date searchStartTime = new Date(new java.util.Date().getTime());
	private Date searchEndTime  = new Date(new java.util.Date().getTime());
	private ChainStore chainStore = new ChainStore();
    private Pager pager = new Pager();
    private int indicator ;
    
    
	public ChainStore getChainStore() {
		return chainStore;
	}
	public void setChainStore(ChainStore chainStore) {
		this.chainStore = chainStore;
	}
	public int getIndicator() {
		return indicator;
	}
	public void setIndicator(int indicator) {
		this.indicator = indicator;
	}

	public HeadQCust getCust() {
		return cust;
	}
	public void setCust(HeadQCust cust) {
		this.cust = cust;
	}
	public Pager getPager() {
		return pager;
	}
	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public Date getSearchStartTime() {
		return searchStartTime;
	}

	public void setSearchStartTime(Date searchStartTime) {
		this.searchStartTime = searchStartTime;
	}

	public Date getSearchEndTime() {
		return searchEndTime;
	}

	public void setSearchEndTime(Date searchEndTime) {
		this.searchEndTime = searchEndTime;
	}

	public FinanceBill getOrder() {
		return order;
	}

	public void setOrder(FinanceBill financeBill) {
		this.order = financeBill;
	}
	
	public void initialize(){
		order = new FinanceBill();
	}
	@Override
	public String toString() {
		return "FinanceActionFormBean [cust=" + cust
				+ ", searchStartTime=" + searchStartTime + ", searchEndTime="
				+ searchEndTime + ", pager=" + pager + "]";
	}
	
	
}
