package com.onlineMIS.ORM.entity.headQ.supplier.finance;

import java.io.Serializable;
import java.util.Date;

public class SupplierAcctFlow implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3186805427385812468L;
	private int id;
	private int supplierId;
	private double acctAmt;
	private Date date;
	private String comment;
	
	/**
	 * 
	 */
	
	public SupplierAcctFlow(){
		
	}
	
	public SupplierAcctFlow(int supplierId, double acctAmt, String comment){
		this.supplierId = supplierId;
		this.acctAmt = acctAmt;
		this.comment = comment;
		this.date = new Date();
	}
	
	public SupplierAcctFlow(int supplierId, double acctAmt, String comment, Date changeDate){
		this.supplierId = supplierId;
		this.acctAmt = acctAmt;
		this.comment = comment;
		
		if (changeDate == null)
		    this.date = new Date();
		else 
			this.date = changeDate;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public double getAcctAmt() {
		return acctAmt;
	}
	public void setAcctAmt(double acctAmt) {
		this.acctAmt = acctAmt;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
