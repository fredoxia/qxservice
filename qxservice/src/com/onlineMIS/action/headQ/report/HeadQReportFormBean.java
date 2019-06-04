package com.onlineMIS.action.headQ.report;



import java.sql.Date;

import com.onlineMIS.ORM.entity.base.Pager;
import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.chainS.user.ChainUserInfor;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Brand;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Category;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Color;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.NumPerHand;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductUnit;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Quarter;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Size;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Year;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;

public class HeadQReportFormBean {
	private Date startDate = null;
	private Date endDate = new Date(new java.util.Date().getTime());
	private Date searchStartTime = null;
	private Date searchEndTime = null;
	private HeadQSupplier supplier = new HeadQSupplier();
	private DummyOrder order = new DummyOrder();
	private HeadQCust cust = new HeadQCust();
	private int parentId;
	private int expenseTypeParentId;
	
    //查找品牌
    private Brand brand = new Brand();
    private Year year = new Year();
    private Quarter quarter = new Quarter();
    private Pager pager = new Pager();
    
    
    
	
	public int getExpenseTypeParentId() {
		return expenseTypeParentId;
	}
	public void setExpenseTypeParentId(int expenseTypeParentId) {
		this.expenseTypeParentId = expenseTypeParentId;
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
	public DummyOrder getOrder() {
		return order;
	}
	public void setOrder(DummyOrder order) {
		this.order = order;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
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
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public Brand getBrand() {
		return brand;
	}
	public void setBrand(Brand brand) {
		this.brand = brand;
	}
	public Year getYear() {
		return year;
	}
	public void setYear(Year year) {
		this.year = year;
	}
	public Quarter getQuarter() {
		return quarter;
	}
	public void setQuarter(Quarter quarter) {
		this.quarter = quarter;
	}
	public Pager getPager() {
		return pager;
	}
	public void setPager(Pager pager) {
		this.pager = pager;
	}
    
	
}
