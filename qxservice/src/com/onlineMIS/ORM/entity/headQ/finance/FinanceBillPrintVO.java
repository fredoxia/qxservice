package com.onlineMIS.ORM.entity.headQ.finance;

import java.util.ArrayList;
import java.util.List;

import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrderProduct;
import com.onlineMIS.common.Common_util;
import com.sun.jndi.toolkit.ctx.StringHeadTail;

public class FinanceBillPrintVO {
	private int id;
	private String creator;
	private String cust;
	private String area;
	private String invoiceTotal;
	private String invoiceDiscount;
	private String billDate;
	private String createDate;
	private String comment;
    private String preAcctAmt;
    private String postAcctAmt;
    private String type;
    private List<FinanceBillItemPrintVO> items = new ArrayList<FinanceBillItemPrintVO>();
    
    
    public FinanceBillPrintVO(){
    	
    }
    
    public FinanceBillPrintVO(FinanceBill financeBill){
    	this.setId(financeBill.getId());
    	this.setCreator(financeBill.getCreatorHq().getName());
    	this.setCust(financeBill.getCust().getName());
    	this.setArea(financeBill.getCust().getArea());
    	this.setInvoiceTotal(Common_util.df.format(financeBill.getInvoiceTotal()));
    	this.setInvoiceDiscount(Common_util.df.format(financeBill.getInvoiceDiscount()));
    	this.setBillDate(Common_util.dateFormat.format(financeBill.getBillDate()));
    	this.setCreateDate(Common_util.dateFormat_f.format(financeBill.getCreateDate()));
    	this.setComment(financeBill.getComment());
    	this.setPreAcctAmt(Common_util.df.format(financeBill.getPreAcctAmt()));
    	this.setPostAcctAmt(Common_util.df.format(financeBill.getPostAcctAmt()));
    	this.setType(financeBill.getTypeHQS());
    	
    	List<FinanceBillItem> financeBillItems = financeBill.getFinanceBillItemList();
    	for (FinanceBillItem item : financeBillItems){
    		FinanceBillItemPrintVO financeBillItemPrintVO = new FinanceBillItemPrintVO(item);
    		items.add(financeBillItemPrintVO);
    	}
    }
    
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public List<FinanceBillItemPrintVO> getItems() {
		return items;
	}

	public void setItems(List<FinanceBillItemPrintVO> items) {
		this.items = items;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCust() {
		return cust;
	}
	public void setCust(String cust) {
		this.cust = cust;
	}
	public String getInvoiceTotal() {
		return invoiceTotal;
	}
	public void setInvoiceTotal(String invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}
	public String getInvoiceDiscount() {
		return invoiceDiscount;
	}
	public void setInvoiceDiscount(String invoiceDiscount) {
		this.invoiceDiscount = invoiceDiscount;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getPreAcctAmt() {
		return preAcctAmt;
	}
	public void setPreAcctAmt(String preAcctAmt) {
		this.preAcctAmt = preAcctAmt;
	}
	public String getPostAcctAmt() {
		return postAcctAmt;
	}
	public void setPostAcctAmt(String postAcctAmt) {
		this.postAcctAmt = postAcctAmt;
	}
    
    
    
}
