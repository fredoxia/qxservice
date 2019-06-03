package com.onlineMIS.ORM.entity.headQ.inventory;

import com.onlineMIS.ORM.entity.base.BaseProduct;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Product;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;


public class HeadqInventoryFlowOrderProduct  extends BaseProduct  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5036399563895853707L;
	private int id;
	private HeadqInventoryFlowOrder flowOrder = new HeadqInventoryFlowOrder();
	private ProductBarcode productBarcode = new ProductBarcode();
	private int quantity;
	private int inventoryQ;
	private int quantityDiff;
	private String comment = "";
	private double cost = 0;
	private double totalCost = 0;
	private double wholePrice = 0;
	private double totalWholePrice = 0;


	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}
	public double getWholePrice() {
		return wholePrice;
	}
	public void setWholePrice(double wholePrice) {
		this.wholePrice = wholePrice;
	}
	public double getTotalWholePrice() {
		return totalWholePrice;
	}
	public void setTotalWholePrice(double totalWholePrice) {
		this.totalWholePrice = totalWholePrice;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getInventoryQ() {
		return inventoryQ;
	}
	public void setInventoryQ(int inventoryQ) {
		this.inventoryQ = inventoryQ;
	}
	public HeadqInventoryFlowOrder getFlowOrder() {
		return flowOrder;
	}
	public void setFlowOrder(HeadqInventoryFlowOrder flowOrder) {
		this.flowOrder = flowOrder;
	}

	public ProductBarcode getProductBarcode() {
		return productBarcode;
	}
	public void setProductBarcode(ProductBarcode productBarcode) {
		this.productBarcode = productBarcode;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantityDiff() {
		return quantityDiff;
	}
	public void setQuantityDiff(int quantityDiff) {
		this.quantityDiff = quantityDiff;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "ChainInventoryFlowOrderProduct [id=" + id + ", orderId="
				+ flowOrder.getId() + ", product=" + productBarcode.getId() + ", quantity=" + quantity
			    + ", comment=" + comment + "]";
	}

}
