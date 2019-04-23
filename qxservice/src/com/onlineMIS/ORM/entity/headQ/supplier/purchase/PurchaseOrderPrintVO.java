package com.onlineMIS.ORM.entity.headQ.supplier.purchase;

import java.util.ArrayList;
import java.util.List;

import com.onlineMIS.common.Common_util;

import sun.nio.cs.ext.TIS_620;

public class PurchaseOrderPrintVO {
	private int id;
	private String store;
	private String comment;
	private String creationTime;
	private String lastUpdateTime;
	private int totalQuantity;
	private double totalRecCost;
	private double totalWholePrice;
	private double totalDiscount;  
	private double preAcctAmt;
	private double postAcctAmt;
	private String orderAuditor;
	private String orderCounter;
	private String supplier;
	private int status;
	private int type;
	private String typeS;
	private String statusS;
	
	private List<PurchaseOrderProductPrintVO> products = new ArrayList<PurchaseOrderProductPrintVO>();
	
	public PurchaseOrderPrintVO(){
		
	}
	
	public PurchaseOrderPrintVO(PurchaseOrder order){
		this.setId(order.getId());
		this.setComment(order.getComment());
		this.setCreationTime(Common_util.dateFormat_f.format(order.getCreationTime()));
		this.setLastUpdateTime(Common_util.dateFormat_f.format(order.getLastUpdateTime()));
		this.setOrderAuditor(order.getOrderAuditor().getName());
		this.setOrderCounter(order.getOrderCounter().getName());
		this.setStatusS(order.getStatusS());
		this.setSupplier(order.getSupplier().getName());
		this.setTotalDiscount(order.getTotalDiscount());
		this.setTotalQuantity(order.getTotalQuantity());
		this.setTotalRecCost(order.getTotalRecCost());
		this.setTotalWholePrice(order.getTotalWholePrice());
		this.setStatus(order.getStatus());
		this.setType(order.getType());
		this.setTypeS(order.getTypeS());
		this.setPreAcctAmt(order.getPreAcctAmt());
		this.setPostAcctAmt(order.getPostAcctAmt());
		
		List<PurchaseOrderProduct> orderProducts = order.getProductList();
		for (PurchaseOrderProduct product : orderProducts){
			PurchaseOrderProductPrintVO printVO = new PurchaseOrderProductPrintVO(product);
			products.add(printVO);
		}
	}
	
	
	public List<PurchaseOrderProductPrintVO> getProducts() {
		return products;
	}

	public void setProducts(List<PurchaseOrderProductPrintVO> products) {
		this.products = products;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public double getTotalRecCost() {
		return totalRecCost;
	}
	public void setTotalRecCost(double totalRecCost) {
		this.totalRecCost = totalRecCost;
	}
	public double getTotalWholePrice() {
		return totalWholePrice;
	}
	public void setTotalWholePrice(double totalWholePrice) {
		this.totalWholePrice = totalWholePrice;
	}
	public double getTotalDiscount() {
		return totalDiscount;
	}
	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}
	public double getPreAcctAmt() {
		return preAcctAmt;
	}
	public void setPreAcctAmt(double preAcctAmt) {
		this.preAcctAmt = preAcctAmt;
	}
	public double getPostAcctAmt() {
		return postAcctAmt;
	}
	public void setPostAcctAmt(double postAcctAmt) {
		this.postAcctAmt = postAcctAmt;
	}
	public String getOrderAuditor() {
		return orderAuditor;
	}
	public void setOrderAuditor(String orderAuditor) {
		this.orderAuditor = orderAuditor;
	}
	public String getOrderCounter() {
		return orderCounter;
	}
	public void setOrderCounter(String orderCounter) {
		this.orderCounter = orderCounter;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getTypeS() {
		return typeS;
	}
	public void setTypeS(String typeS) {
		this.typeS = typeS;
	}
	public String getStatusS() {
		return statusS;
	}
	public void setStatusS(String statusS) {
		this.statusS = statusS;
	}
	
	
}
