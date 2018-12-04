package com.onlineMIS.action.headQ.supplier.finance;

import java.util.ArrayList;
import java.util.List;

import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBill;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.FinanceBillSupplier;

public class FinanceSupplierActionUIBean {
	private List<FinanceBill> financeBills = new ArrayList<FinanceBill>();
	private FinanceBillSupplier order = new FinanceBillSupplier();
	private List<ChainStore> chainStores = new ArrayList<ChainStore>();

	public List<ChainStore> getChainStores() {
		return chainStores;
	}

	public void setChainStores(List<ChainStore> chainStores) {
		this.chainStores = chainStores;
	}

	public List<FinanceBill> getFinanceBills() {
		return financeBills;
	}

	public void setFinanceBills(List<FinanceBill> financeBills) {
		this.financeBills = financeBills;
	}

	public FinanceBillSupplier getOrder() {
		return order;
	}

	public void setOrder(FinanceBillSupplier order) {
		this.order = order;
	}

}
