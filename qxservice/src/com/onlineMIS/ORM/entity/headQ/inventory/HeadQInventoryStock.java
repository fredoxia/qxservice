package com.onlineMIS.ORM.entity.headQ.inventory;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.sql.Update;

import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.common.Common_util;


public class HeadQInventoryStock implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3535346499179201903L;
	public static final String SUPPLIER_PURCHASE = "SP";
	public static final String SUPPLIER_RETURN = "SR";
	public static final String RETAIL_SALES = "RS";
	public static final String RETAIL_RETURN = "RR";

	public static final String CHAIN_OVERFLOW = "CO";
	public static final String CHAIN_FLOWLOSS= "CL";
	public static final String AUTO_BAR_ACCT = "ABA";
	

    private int storeId;
    private ProductBarcode productBarcode;
    private String orderId;

    private double cost;
    private double costTotal;
    //whole sales price
    private double salePrice;
    private double salePriceTotal;
    private int quantity;
    private Date date;
    
    
    public HeadQInventoryStock(){
    	
    }
    
    public HeadQInventoryStock(int store, String orderId, double cost, double costTotal, double salePrice, double salePriceTotal,  int quantity, ProductBarcode productBarcode){
    	this.cost = cost;
    	this.costTotal = Common_util.roundDouble(costTotal,2);
    	this.salePrice = Common_util.roundDouble(salePrice,2);
    	this.salePriceTotal = Common_util.roundDouble(salePriceTotal,2);
    	this.quantity = quantity;
    	date = new Date();
    	this.productBarcode = productBarcode;
    	this.storeId = store;
    	this.orderId = orderId;
    }
 

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public ProductBarcode getProductBarcode() {
		return productBarcode;
	}

	public void setProductBarcode(ProductBarcode productBarcode) {
		this.productBarcode = productBarcode;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public double getCostTotal() {
		return costTotal;
	}

	public void setCostTotal(double costTotal) {
		this.costTotal = costTotal;
	}

	public double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(double salePrice) {
		this.salePrice = salePrice;
	}

	public double getSalePriceTotal() {
		return salePriceTotal;
	}

	public void setSalePriceTotal(double salePriceTotal) {
		this.salePriceTotal = salePriceTotal;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
//	public void addTo(HeadQInventoryStock stock){
//		this.quantity += stock.getQuantity();
//		this.cost = stock.getCost();
//		this.costTotal += stock.getCostTotal();
//		this.salePrice = stock.getSalePrice();
//		this.salePriceTotal += stock.getSalePriceTotal();
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(cost);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((productBarcode == null) ? 0 : productBarcode.hashCode());
		result = prime * result + quantity;
		temp = Double.doubleToLongBits(salePrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + storeId;
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
		HeadQInventoryStock other = (HeadQInventoryStock) obj;
		if (Double.doubleToLongBits(cost) != Double
				.doubleToLongBits(other.cost))
			return false;
		if (productBarcode == null) {
			if (other.productBarcode != null)
				return false;
		} else if (!productBarcode.equals(other.productBarcode))
			return false;
		if (quantity != other.quantity)
			return false;
		if (Double.doubleToLongBits(salePrice) != Double
				.doubleToLongBits(other.salePrice))
			return false;
		if (storeId != other.storeId)
			return false;
		return true;
	}





    
}
