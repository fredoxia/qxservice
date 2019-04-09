package com.onlineMIS.ORM.entity.headQ.supplier.finance;

import java.util.Date;

import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;

public class SupplierAcctFlowReportItem {
	public static final String ITEM_TYPE_FINANCE = "F";
	public static final String ITEM_TYPE_PURCHASE = "P";
	
	public static final String ACCT_FLOW_TYPE_INCREASE = "I";
	public static final String ACCT_FLOW_TYPE_DECREASE = "D";
	
	private HeadQSupplier supplier = new HeadQSupplier();
	private Date date = new Date();
	private String itemTypeName = "";
	private String itemType = "";
	private String acctFlowType = "";
	private int quantity;
	private double amount;
	private double amtFlow;
	private int id;
	private String comment;
	private double acctIncrease ;
	private double acctDecrease;
	private double postAcct;
	
	public SupplierAcctFlowReportItem(){
		
	}
	
	public SupplierAcctFlowReportItem(HeadQSupplier supplier, Date date,String itemTypeName, String itemType, String acctFlowType, int quantity, double amount, double amtFlow, int id, String comment, double preAcct, double postAcct){
		this.supplier = supplier;
		this.date = date;
		this.itemTypeName = itemTypeName;
		this.itemType = itemType;
		this.quantity = quantity;
		this.amount = amount;
		this.amtFlow = amtFlow;
		this.id = id;
		this.comment = comment;
		this.acctFlowType = acctFlowType;
	}
	


	public double getAmtFlow() {
		return amtFlow;
	}

	public void setAmtFlow(double amtFlow) {
		this.amtFlow = amtFlow;
	}

	public String getAcctFlowType() {
		return acctFlowType;
	}

	public void setAcctFlowType(String acctFlowType) {
		this.acctFlowType = acctFlowType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getItemTypeName() {
		return itemTypeName;
	}

	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}

	public HeadQSupplier getSupplier() {
		return supplier;
	}

	public void setSupplier(HeadQSupplier supplier) {
		this.supplier = supplier;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public double getAcctIncrease() {
		return acctIncrease;
	}

	public void setAcctIncrease(double acctIncrease) {
		this.acctIncrease = acctIncrease;
	}

	public double getAcctDecrease() {
		return acctDecrease;
	}

	public void setAcctDecrease(double acctDecrease) {
		this.acctDecrease = acctDecrease;
	}

	public double getPostAcct() {
		return postAcct;
	}
	public void setPostAcct(double postAcct) {
		this.postAcct = postAcct;
	}
	
	
}
