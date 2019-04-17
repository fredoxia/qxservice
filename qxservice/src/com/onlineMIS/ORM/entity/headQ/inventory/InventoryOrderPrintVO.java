package com.onlineMIS.ORM.entity.headQ.inventory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.onlineMIS.common.Common_util;

public class InventoryOrderPrintVO {
	private int id;
	private String clientName = "";
	private String clientArea = "";
	private String orderTime;
	private String PDAUserName ="";
	private String keeperName = "";
	private String auditorName = "";
	private int totalQ ;
	private String totalWholeSales;
	private String discount;
	private String comment = "";
	private String orderType = "";
    private String preAcctAmt = "";
    private String postAcctAmt = "";
    private String cash = "";
    private String card = "";
    private String alipay = "";
    private String wechat = "";
    
    private List<InventoryOrderProdPrintVO> products =new  ArrayList<InventoryOrderProdPrintVO>();
    
    public InventoryOrderPrintVO(){
    	
    }
    
    public InventoryOrderPrintVO(InventoryOrder order, double preAcctAmt, double postAcctAmt){
    	this.setId(order.getOrder_ID());
    	this.setClientName(order.getCust().getName());
    	this.setClientArea(order.getCust().getArea());
    	this.setOrderTime(Common_util.dateFormat_f.format(order.getOrder_ComplTime()));
    	if (order.getPdaScanner() != null)
    	    this.setPDAUserName(order.getPdaScanner().getName());
    	if (order.getOrder_Keeper() != null)
    		this.setKeeperName(order.getOrder_Keeper().getName());
    	if (order.getOrder_Auditor() != null)
    		this.setAuditorName(order.getOrder_Auditor().getName());
    	this.setTotalQ(order.getTotalQuantity());
    	this.setTotalWholeSales(Common_util.df2.format(order.getTotalWholePrice()));
    	this.setComment(order.getComment());
    	this.setOrderType(order.getOrder_type_ws());
    	this.setPreAcctAmt(Common_util.df.format(preAcctAmt));
    	this.setPostAcctAmt(Common_util.df.format(postAcctAmt));
    	this.setDiscount(Common_util.df2.format(order.getTotalDiscount()));
    	this.setCash(String.valueOf((int)order.getCash()));
    	this.setCard(String.valueOf((int)order.getCard()));
    	this.setAlipay(String.valueOf((int)order.getAlipay()));
    	this.setWechat(String.valueOf((int)order.getWechat()));
    	
    	List<InventoryOrderProduct> orderProducts = order.getProduct_List();
    	for (InventoryOrderProduct product : orderProducts){
    		InventoryOrderProdPrintVO orderProdPrintVO = new InventoryOrderProdPrintVO(product);
    		products.add(orderProdPrintVO);
    	}
    }
    
	public String getClientArea() {
		return clientArea;
	}

	public void setClientArea(String clientArea) {
		this.clientArea = clientArea;
	}

	public String getCash() {
		return cash;
	}

	public void setCash(String cash) {
		this.cash = cash;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getPDAUserName() {
		return PDAUserName;
	}
	public void setPDAUserName(String pDAUserName) {
		PDAUserName = pDAUserName;
	}
	public String getKeeperName() {
		return keeperName;
	}
	public void setKeeperName(String keeperName) {
		this.keeperName = keeperName;
	}
	public String getAuditorName() {
		return auditorName;
	}
	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	public int getTotalQ() {
		return totalQ;
	}
	public void setTotalQ(int totalQ) {
		this.totalQ = totalQ;
	}
	public String getTotalWholeSales() {
		return totalWholeSales;
	}
	public void setTotalWholeSales(String totalWholeSales) {
		this.totalWholeSales = totalWholeSales;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getPreAcctAmt() {
		return preAcctAmt;
	}
	public void setPreAcctAmt(String preAcctAmt) {
		this.preAcctAmt = preAcctAmt;
	}
	public String getPostAcctAmt() {
		return postAcctAmt;
	}
	public void setPostAcctAmt(String postAcctAmt) {
		this.postAcctAmt = postAcctAmt;
	}
	public List<InventoryOrderProdPrintVO> getProducts() {
		return products;
	}
	public void setProducts(List<InventoryOrderProdPrintVO> products) {
		this.products = products;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
