package com.onlineMIS.ORM.entity.headQ.inventory;

import java.text.DecimalFormat;

import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.common.Common_util;

public class InventoryOrderProdPrintVO {
	private String barcode= "";
	private String productCode= "";
	private String color = "";
	private String quantity = "";
	
	private String selectPrice = "";
	private String discountRate = "";
	private String wholeSales = "";
	private String totalWholeSales = "";
	private String brand = "";
	
	public InventoryOrderProdPrintVO(){
		
	}
	
	public InventoryOrderProdPrintVO(InventoryOrderProduct product){
		
		
		ProductBarcode pb = product.getProductBarcode();
		this.setBarcode(pb.getBarcode());
		this.setProductCode(pb.getProduct().getProductCode());
		
		if (pb.getColor() != null)
		   this.setColor(pb.getColor().getName());
		
		this.setBrand(pb.getProduct().getBrand().getBrand_Name());
		
		this.setQuantity(String.valueOf(product.getQuantity()));
		this.setSelectPrice(Common_util.df2.format(product.getSalePriceSelected()));
		this.setDiscountRate(Common_util.df.format(product.getDiscount()));
		this.setWholeSales(Common_util.df2.format(product.getWholeSalePrice()));
		double totalWhole = product.getWholeSalePrice() * product.getQuantity();
		this.setTotalWholeSales(String.valueOf((int)totalWhole));
	}
	
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getSelectPrice() {
		return selectPrice;
	}
	public void setSelectPrice(String selectPrice) {
		this.selectPrice = selectPrice;
	}
	public String getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(String discountRate) {
		this.discountRate = discountRate;
	}
	public String getWholeSales() {
		return wholeSales;
	}
	public void setWholeSales(String wholeSales) {
		this.wholeSales = wholeSales;
	}
	public String getTotalWholeSales() {
		return totalWholeSales;
	}
	public void setTotalWholeSales(String totalWholeSales) {
		this.totalWholeSales = totalWholeSales;
	}
	
	
	
}
