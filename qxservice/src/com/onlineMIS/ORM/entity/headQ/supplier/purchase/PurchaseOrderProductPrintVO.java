package com.onlineMIS.ORM.entity.headQ.supplier.purchase;

import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;


public class PurchaseOrderProductPrintVO {
	private String productCode= "";
	private String color = "";
	private String quantity = "";

	private String recCost = "";
	private String totalRecCost = "";
	private String brand = "";
	
	public PurchaseOrderProductPrintVO(){
		
	}
	
	public PurchaseOrderProductPrintVO(PurchaseOrderProduct product){
		
		
		ProductBarcode pb = product.getPb();

		this.setProductCode(pb.getProduct().getProductCode());
		
		if (pb.getColor() != null)
		   this.setColor(pb.getColor().getName());
		
		this.setBrand(pb.getProduct().getBrand().getBrand_Name());
		
		this.setQuantity(String.valueOf(product.getQuantity()));
		this.setRecCost(String.valueOf(product.getRecCost()));
		double totalRecCost = product.getRecCost() * product.getQuantity();
		this.setTotalRecCost(String.valueOf(totalRecCost));
	}

	public String getTotalRecCost() {
		return totalRecCost;
	}

	public void setTotalRecCost(String totalRecCost) {
		this.totalRecCost = totalRecCost;
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

	public String getRecCost() {
		return recCost;
	}

	public void setRecCost(String recCost) {
		this.recCost = recCost;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	
}
