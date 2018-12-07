package com.onlineMIS.action.headQ.supplier.purchase;

import java.util.ArrayList;
import java.util.List;

import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBill;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.FinanceBillSupplier;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;

public class SupplierPurchaseActionUIBean {
	private List<PurchaseOrder> orders = new ArrayList<PurchaseOrder>();

	private List<ChainStore> chainStores = new ArrayList<ChainStore>();

	public List<PurchaseOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<PurchaseOrder> orders) {
		this.orders = orders;
	}

	public List<ChainStore> getChainStores() {
		return chainStores;
	}

	public void setChainStores(List<ChainStore> chainStores) {
		this.chainStores = chainStores;
	}



}
