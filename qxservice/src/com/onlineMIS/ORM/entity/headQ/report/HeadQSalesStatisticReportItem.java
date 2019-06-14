package com.onlineMIS.ORM.entity.headQ.report;

import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrder;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;


public class HeadQSalesStatisticReportItem extends HeadQStatisticReportItem{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4430014455314999157L;

	protected int salesQ = 0;
	//销售额
	protected double salesPrice = 0;
	//销售成本
	protected double salesCost = 0;
	//销售利润
	protected double salesProfit = 0;

	protected int returnQ = 0;
	//退货额
	protected double returnPrice = 0;
	//退货成本
	protected double returnCost = 0;
	//退货利润
	protected double returnProfit = 0;

	protected int netQ = 0;
	//净销售额
	protected double netPrice =0;
	//净销售成本
	protected double netCost = 0;
	//销售利润
	//净利润
	protected double netProfit = 0;
	



	public HeadQSalesStatisticReportItem(){
		
	}
	
	public HeadQSalesStatisticReportItem(int orderType,  int quantity, double amount, double cost,  ProductBarcode pb){
		this.setPb(pb);
	    add(orderType, quantity, amount, cost);
	}
	
	public void add(int type,int quantity, double sales, double cost){
		switch (type) {
		case InventoryOrder.TYPE_SALES_ORDER_W:
			this.salesPrice += sales;
			this.salesCost += cost;
			this.salesQ += quantity;
//			this.setSalesDiscount(discount + this.getSalesDiscount());
			break;
		case InventoryOrder.TYPE_SALES_RETURN_ORDER_W:
			this.returnPrice += sales;
			this.returnCost += cost;
			this.returnQ += quantity;
			break;
		default:
			break;
		}
		
		netQ = salesQ - returnQ;
		salesProfit = salesPrice - salesCost;
		returnProfit = returnPrice - returnCost;
		netPrice = salesPrice - returnPrice;
		netCost = salesCost - returnCost;
		netProfit = netPrice - netCost;
	}


	public double getSalesProfit() {
		return salesProfit;
	}

	public void setSalesProfit(double salesProfit) {
		this.salesProfit = salesProfit;
	}

	public double getReturnProfit() {
		return returnProfit;
	}

	public void setReturnProfit(double returnProfit) {
		this.returnProfit = returnProfit;
	}

	public int getSalesQ() {
		return salesQ;
	}

	public void setSalesQ(int salesQ) {
		this.salesQ = salesQ;
	}

	public int getReturnQ() {
		return returnQ;
	}

	public void setReturnQ(int returnQ) {
		this.returnQ = returnQ;
	}

	public int getNetQ() {
		return netQ;
	}

	public void setNetQ(int netQ) {
		this.netQ = netQ;
	}

	public double getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}

	public double getReturnPrice() {
		return returnPrice;
	}

	public void setReturnPrice(double returnPrice) {
		this.returnPrice = returnPrice;
	}

	public double getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(double netPrice) {
		this.netPrice = netPrice;
	}


	public double getSalesCost() {
		return salesCost;
	}

	public void setSalesCost(double salesCost) {
		this.salesCost = salesCost;
	}

	public double getReturnCost() {
		return returnCost;
	}

	public void setReturnCost(double returnCost) {
		this.returnCost = returnCost;
	}

	public double getNetCost() {
		return netCost;
	}

	public void setNetCost(double netCost) {
		this.netCost = netCost;
	}

	public double getNetProfit() {
		return netProfit;
	}

	public void setNetProfit(double netProfit) {
		this.netProfit = netProfit;
	}



}
