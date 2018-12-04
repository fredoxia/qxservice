package com.onlineMIS.ORM.entity.headQ.supplier.finance;

import java.io.Serializable;
import java.sql.Date;

public class SupplierFinanceTrace implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1104938379821939888L;
	private int supplierId;
	private int categoryId;
	private String billId;
	private double amount;
	private Date date;
	
	public SupplierFinanceTrace(){
		
	}
	
	public SupplierFinanceTrace(int supplierId, int categoryId, String billId, double amount, Date date){
		this.supplierId = supplierId;
		this.categoryId = categoryId;
		this.billId = billId;
		this.amount = amount;
		this.date = date;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
