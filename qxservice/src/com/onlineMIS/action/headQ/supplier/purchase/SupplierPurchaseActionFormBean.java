package com.onlineMIS.action.headQ.supplier.purchase;

import java.sql.Date;

import com.onlineMIS.ORM.entity.base.Pager;
import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBill;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.FinanceBillSupplier;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;
import com.onlineMIS.action.chainS.ChainActionFormBaseBean;

public class SupplierPurchaseActionFormBean extends ChainActionFormBaseBean{
	private HeadQSupplier supplier = new HeadQSupplier();
	private PurchaseOrder order = new PurchaseOrder();
	private Date searchStartTime = new Date(new java.util.Date().getTime());
	private Date searchEndTime  = new Date(new java.util.Date().getTime());
	private int indexPage;
	private int fromSrc;
	private ProductBarcode pb = new ProductBarcode();
    //搜索时的product ids
    private String productIds = "";
    
    private Pager pager = new Pager();
    private int indicator ;

	public String getProductIds() {
		return productIds;
	}
	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}
	public int getFromSrc() {
		return fromSrc;
	}
	public void setFromSrc(int fromSrc) {
		this.fromSrc = fromSrc;
	}
	public int getIndexPage() {
		return indexPage;
	}
	public void setIndexPage(int indexPage) {
		this.indexPage = indexPage;
	}
	public ProductBarcode getPb() {
		return pb;
	}
	public void setPb(ProductBarcode pb) {
		this.pb = pb;
	}
	public PurchaseOrder getOrder() {
		return order;
	}
	public HeadQSupplier getSupplier() {
		return supplier;
	}
	public void setSupplier(HeadQSupplier supplier) {
		this.supplier = supplier;
	}
	public void setOrder(PurchaseOrder order) {
		this.order = order;
	}
	public int getIndicator() {
		return indicator;
	}
	public void setIndicator(int indicator) {
		this.indicator = indicator;
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


	@Override
	public String toString() {
		return "FinanceActionFormBean [supplier=" + supplier
				+ ", searchStartTime=" + searchStartTime + ", searchEndTime="
				+ searchEndTime + ", pager=" + pager + "]";
	}
	
	
}
