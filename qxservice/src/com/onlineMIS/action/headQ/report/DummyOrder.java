package com.onlineMIS.action.headQ.report;

import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;

public class DummyOrder {
	private HeadQSupplier supplier;
	private HeadQCust cust;
	public HeadQSupplier getSupplier() {
		return supplier;
	}
	public void setSupplier(HeadQSupplier supplier) {
		this.supplier = supplier;
	}
	public HeadQCust getCust() {
		return cust;
	}
	public void setCust(HeadQCust cust) {
		this.cust = cust;
	}
	
	
}
