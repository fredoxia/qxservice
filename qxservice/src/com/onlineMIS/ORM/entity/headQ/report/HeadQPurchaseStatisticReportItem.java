package com.onlineMIS.ORM.entity.headQ.report;

import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrder;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;


public class HeadQPurchaseStatisticReportItem extends HeadQStatisticReportItem{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4430014455314999157L;


	/**
	 * 总采购货数量
	 */
	protected int purchaseQuantity = 0;
	/**
	 * 采购退货数量
	 */
	protected int returnQuantity = 0;
	/**
	 * 净采购数量
	 */
	protected int netQuantity = 0;
	
	/**
	 * 净采购总额
	 */
	protected double purchaseTotalAmt = 0;
	/**
	 * 净采购总额
	 */
	protected double returnTotalAmt = 0;
	
	/**
	 * 净采购总额
	 */
	protected double netTotalAmt = 0;
	



	public HeadQPurchaseStatisticReportItem(){
		
	}
	
	public HeadQPurchaseStatisticReportItem(int orderType,  int quantity, double amount, ProductBarcode pb){
		this.setPb(pb);
	    add(orderType, quantity, amount);
	}
	
	public void add(int orderType,  int quantity, double amount){
	    switch (orderType) {
		   case PurchaseOrder.TYPE_PURCHASE:
			   this.purchaseQuantity += quantity;
			   this.purchaseTotalAmt += amount;
	
			   break;
		   case PurchaseOrder.TYPE_RETURN:
			   this.returnQuantity += quantity;
			   this.returnTotalAmt += amount;
			   break;
		   default:
			   break;
		}
	    
	    netQuantity = this.purchaseQuantity - this.returnQuantity;
	    netTotalAmt = this.purchaseTotalAmt - this.returnTotalAmt;
	}


	


	public int getPurchaseQuantity() {
		return purchaseQuantity;
	}

	public void setPurchaseQuantity(int purchaseQuantity) {
		this.purchaseQuantity = purchaseQuantity;
	}


	public double getPurchaseTotalAmt() {
		return purchaseTotalAmt;
	}

	public void setPurchaseTotalAmt(double purchaseTotalAmt) {
		this.purchaseTotalAmt = purchaseTotalAmt;
	}

	public int getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(int returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public int getNetQuantity() {
		return netQuantity;
	}

	public void setNetQuantity(int netQuantity) {
		this.netQuantity = netQuantity;
	}

	public double getReturnTotalAmt() {
		return returnTotalAmt;
	}

	public void setReturnTotalAmt(double returnTotalAmt) {
		this.returnTotalAmt = returnTotalAmt;
	}

	public double getNetTotalAmt() {
		return netTotalAmt;
	}

	public void setNetTotalAmt(double netTotalAmt) {
		this.netTotalAmt = netTotalAmt;
	}


}
