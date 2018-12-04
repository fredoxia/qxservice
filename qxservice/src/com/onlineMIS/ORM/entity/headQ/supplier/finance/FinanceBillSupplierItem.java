package com.onlineMIS.ORM.entity.headQ.supplier.finance;

import com.onlineMIS.ORM.entity.base.BaseProduct;

public class FinanceBillSupplierItem extends BaseProduct {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6859472438543243109L;

	private FinanceCategorySupplier financeCategorySupplier;
	private double total;
	private String comment;
	private FinanceBillSupplier financeBillSupplier;
	
	public FinanceCategorySupplier getFinanceCategorySupplier() {
		return financeCategorySupplier;
	}
	public void setFinanceCategorySupplier(
			FinanceCategorySupplier financeCategorySupplier) {
		this.financeCategorySupplier = financeCategorySupplier;
	}
	public FinanceBillSupplier getFinanceBillSupplier() {
		return financeBillSupplier;
	}
	public void setFinanceBillSupplier(FinanceBillSupplier financeBillSupplier) {
		this.financeBillSupplier = financeBillSupplier;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	
}
