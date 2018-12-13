package com.onlineMIS.ORM.entity.headQ.supplier.purchase;

import java.io.Serializable;

import com.onlineMIS.ORM.entity.headQ.user.News;

public class HeadqPurchaseHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6985004647601265953L;
    private HeadqPurchaseHistoryId id = new HeadqPurchaseHistoryId();
	private double recCost;
	private double wholePrice;
	private int quantity;
	
	public HeadqPurchaseHistory(){	}
	
	public HeadqPurchaseHistory(int productId, int supplierId, double recCost, double wholePrice,  int quantity){
		this.id.setProductId(productId);
		this.id.setSupplierId(supplierId);
		this.recCost = recCost;
		this.wholePrice = wholePrice;
		this.quantity = quantity;
	}
	

	public HeadqPurchaseHistoryId getId() {
		return id;
	}

	public void setId(HeadqPurchaseHistoryId id) {
		this.id = id;
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
		
		result = prime * result + ((id == null) ? 0 : id.hashCode());

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

		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "[id=" + id + "]";
	} 
	
	
}
