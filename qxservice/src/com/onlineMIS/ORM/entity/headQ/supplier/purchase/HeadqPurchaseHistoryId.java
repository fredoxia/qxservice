package com.onlineMIS.ORM.entity.headQ.supplier.purchase;

import java.io.Serializable;

public class HeadqPurchaseHistoryId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4033701225202404916L;
	private int productId;
	private int supplierId; 
	
	public HeadqPurchaseHistoryId(){
		
	}
	public HeadqPurchaseHistoryId(int productId, int supplierId){
		this.productId = productId;
		this.supplierId = supplierId;
	}
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + productId;
		result = prime * result + supplierId;
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
		HeadqPurchaseHistoryId other = (HeadqPurchaseHistoryId) obj;
		if (productId != other.productId)
			return false;
		if (supplierId != other.supplierId)
			return false;
		return true;
	}
	
	
}
