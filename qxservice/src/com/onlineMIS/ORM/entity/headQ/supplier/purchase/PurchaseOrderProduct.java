package com.onlineMIS.ORM.entity.headQ.supplier.purchase;

import java.io.Serializable;

import com.onlineMIS.ORM.entity.base.BaseProduct;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;

public class PurchaseOrderProduct extends BaseProduct{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2866650360885118809L;
	private ProductBarcode pb;
	private PurchaseOrder order;
	private int quantity;
	private double wholeSalePrice;
	private double recCost;
	private double discount;
	private double salePriceSelected;
	
	public ProductBarcode getPb() {
		return pb;
	}
	public void setPb(ProductBarcode pb) {
		this.pb = pb;
	}
	public PurchaseOrder getOrder() {
		return order;
	}
	public void setOrder(PurchaseOrder order) {
		this.order = order;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getWholeSalePrice() {
		return wholeSalePrice;
	}
	public void setWholeSalePrice(double wholeSalePrice) {
		this.wholeSalePrice = wholeSalePrice;
	}
	public double getRecCost() {
		return recCost;
	}
	public void setRecCost(double recCost) {
		this.recCost = recCost;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getSalePriceSelected() {
		return salePriceSelected;
	}
	public void setSalePriceSelected(double salePriceSelected) {
		this.salePriceSelected = salePriceSelected;
	}
	
	
}
