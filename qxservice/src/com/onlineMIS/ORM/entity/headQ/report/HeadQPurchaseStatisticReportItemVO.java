package com.onlineMIS.ORM.entity.headQ.report;

import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrder;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;


public class HeadQPurchaseStatisticReportItemVO  extends HeadQReportItemVO  {
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
	protected double avgPrice = 0;
	/**
	 * 净采购总额
	 */
	protected double purchaseTotalAmt = 0;
	



	public HeadQPurchaseStatisticReportItemVO(){
		
	}
	
	public HeadQPurchaseStatisticReportItemVO(String name, int parentId, int supplierId, int yearId, int quarterId, int brandId, int pbId, String state){
		super(name, parentId, yearId, quarterId, brandId, pbId, state);
		this.setSupplierId(supplierId);
	}

	
	public int getPurchaseQuantity() {
		return purchaseQuantity;
	}

	public void setPurchaseQuantity(int purchaseQuantity) {
		this.purchaseQuantity = purchaseQuantity;
	}

	public double getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
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

	public void add(HeadQPurchaseStatisticReportItemVO item){
		this.purchaseQuantity += item.getPurchaseQuantity();
		this.purchaseTotalAmt += item.getPurchaseTotalAmt();
		this.returnQuantity += item.getReturnQuantity();
		this.netQuantity += item.getNetQuantity();
	}
	
	public void reCalculate(){
		this.netQuantity = purchaseQuantity - returnQuantity;
		if (netQuantity != 0)
			this.avgPrice = this.purchaseTotalAmt / netQuantity;
	}
	
	public void putValue(int orderType,  int quantity, double purchaseAmt){
	    switch (orderType) {
		   case PurchaseOrder.TYPE_PURCHASE:
			   this.purchaseQuantity = quantity;
			   this.purchaseTotalAmt += purchaseAmt;
	
			   break;
		   case PurchaseOrder.TYPE_RETURN:
			   this.returnQuantity = quantity;
			   this.purchaseTotalAmt -= purchaseAmt;
			   break;
		   default:
			   break;
		}
	}
}
