package com.onlineMIS.ORM.entity.headQ.barcodeGentor;

import java.util.Date;

import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.ProductBarcodeDaoImpl;

public class ProductBarcodeVO {
	private int id;
	private String barcode = "";
	private String year = "";
	private String quarter = "";
	private String brand = "";
	private String category = "";
	private String productCode = "";
	private String color = "";
	private String qOneHand = "";
	private String unit = "";
	private double factorySalesPrice = 0;
	private double discount = 0;
	private double salesPrice = 0;
	private double cost = 0;
	private double wholePrice1 = 0;
	private double wholePrice2 = 0;
	private double wholePrice3 = 0;
	private Date createTime;
	private int inventoryLevel = 0;
	private int boughtBefore = 0;
    /**
     * 历史输入 折扣价
     */
    private double lastInputPrice;
    
    /**
     * 历史选择价格
     */
    private double lastChoosePrice;
	
	
	public ProductBarcodeVO(ProductBarcode pb){
		this.id = pb.getId();
		this.barcode = pb.getBarcode();
		Product product = pb.getProduct();
		if (product != null){
			this.year = product.getYear().getYear();
			this.quarter = product.getQuarter().getQuarter_Name();
			this.brand = product.getBrand().getBrand_Name();
			this.category = product.getCategory().getCategory_Name();
			this.productCode = product.getProductCode();
			Color color = pb.getColor();
			if (color != null)
				this.color = color.getName();
			this.qOneHand = String.valueOf(product.getNumPerHand());
			this.unit = product.getUnit();
			this.factorySalesPrice = product.getSalesPriceFactory();
			this.discount = product.getDiscount();
			this.salesPrice = product.getSalesPrice();
			this.cost = ProductBarcodeDaoImpl.getRecCost(pb);
			this.wholePrice1 = product.getWholeSalePrice();
			this.wholePrice2 = product.getWholeSalePrice2();
			this.wholePrice3 = product.getWholeSalePrice3();
			this.createTime = pb.getCreateDate();
		}
	}
	
	public double getLastInputPrice() {
		return lastInputPrice;
	}

	public void setLastInputPrice(double lastInputPrice) {
		this.lastInputPrice = lastInputPrice;
	}

	public double getLastChoosePrice() {
		return lastChoosePrice;
	}

	public void setLastChoosePrice(double lastChoosePrice) {
		this.lastChoosePrice = lastChoosePrice;
	}

	public int getBoughtBefore() {
		return boughtBefore;
	}

	public void setBoughtBefore(int boughtBefore) {
		this.boughtBefore = boughtBefore;
	}

	public int getInventoryLevel() {
		return inventoryLevel;
	}

	public void setInventoryLevel(int inventoryLevel) {
		this.inventoryLevel = inventoryLevel;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public String getqOneHand() {
		return qOneHand;
	}
	public void setqOneHand(String qOneHand) {
		this.qOneHand = qOneHand;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getFactorySalesPrice() {
		return factorySalesPrice;
	}
	public void setFactorySalesPrice(double factorySalesPrice) {
		this.factorySalesPrice = factorySalesPrice;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public double getSalesPrice() {
		return salesPrice;
	}
	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getWholePrice1() {
		return wholePrice1;
	}
	public void setWholePrice1(double wholePrice1) {
		this.wholePrice1 = wholePrice1;
	}
	public double getWholePrice2() {
		return wholePrice2;
	}
	public void setWholePrice2(double wholePrice2) {
		this.wholePrice2 = wholePrice2;
	}
	public double getWholePrice3() {
		return wholePrice3;
	}
	public void setWholePrice3(double wholePrice3) {
		this.wholePrice3 = wholePrice3;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
