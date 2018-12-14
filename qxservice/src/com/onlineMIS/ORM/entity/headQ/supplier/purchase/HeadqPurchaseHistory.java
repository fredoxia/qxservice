package com.onlineMIS.ORM.entity.headQ.supplier.purchase;

import java.io.Serializable;

import com.onlineMIS.ORM.entity.headQ.user.News;

public class HeadqPurchaseHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6985004647601265953L;
    private int productId;
	private double recCost;
	private double wholePrice;
	private int quantity;
	
	public HeadqPurchaseHistory(){	}
	
	public HeadqPurchaseHistory(int productId, double recCost, double wholePrice,  int quantity){
		this.setProductId(productId);
		this.recCost = recCost;
		this.wholePrice = wholePrice;
		this.quantity = quantity;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public double getRecCost() {
		return recCost;
	}
	public void setRecCost(double recCost) {
		this.recCost = recCost;
	}
	public double getWholePrice() {
		return wholePrice;
	}
	public void setWholePrice(double wholePrice) {
		this.wholePrice = wholePrice;
	}

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + productId;
		result = prime * result + quantity;
		long temp;
		temp = Double.doubleToLongBits(recCost);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(wholePrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HeadqPurchaseHistory other = (HeadqPurchaseHistory) obj;
		if (productId != other.productId)
			return false;
		if (quantity != other.quantity)
			return false;
		if (Double.doubleToLongBits(recCost) != Double.doubleToLongBits(other.recCost))
			return false;
		if (Double.doubleToLongBits(wholePrice) != Double.doubleToLongBits(other.wholePrice))
			return false;
		return true;
	}



	
}
