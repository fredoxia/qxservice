package com.onlineMIS.ORM.DAO.headQ.inventory;

import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadQInventoryStore;

public class HeadqInventoryReportItem {
	private HeadQInventoryStore store;
	private ProductBarcode productBarcode;
	/**
	 * 总数量
	 */
	private int totalQuantity;
	/**
	 * 总成本
	 */
	private double totalCostAmt;
	/**
	 * 总销售额
	 */
	private double totalWholeSalesAmt;
	
	public HeadqInventoryReportItem(ProductBarcode pb, int totalQuantity, double totalCost){
		this.setProductBarcode(pb);
		this.setTotalQuantity(totalQuantity);
		this.setTotalCostAmt(totalCost);
	}
	public HeadqInventoryReportItem(){
		
	}
	public HeadQInventoryStore getStore() {
		return store;
	}
	public void setStore(HeadQInventoryStore store) {
		this.store = store;
	}
	public ProductBarcode getProductBarcode() {
		return productBarcode;
	}
	public void setProductBarcode(ProductBarcode productBarcode) {
		this.productBarcode = productBarcode;
	}
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public double getTotalCostAmt() {
		return totalCostAmt;
	}
	public void setTotalCostAmt(double totalCostAmt) {
		this.totalCostAmt = totalCostAmt;
	}
	public double getTotalWholeSalesAmt() {
		return totalWholeSalesAmt;
	}
	public void setTotalWholeSalesAmt(double totalWholeSalesAmt) {
		this.totalWholeSalesAmt = totalWholeSalesAmt;
	}
	
	
}
