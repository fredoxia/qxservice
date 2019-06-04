package com.onlineMIS.ORM.entity.shared.expense;

import java.io.Serializable;

import com.onlineMIS.ORM.entity.chainS.user.ChainStore;

public class Expense implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3632171643209176686L;
	private int id;
	private ChainStore entity = new ChainStore();
	private ExpenseType expenseType;
	//1:'现金',2:'银行',3:'支付宝',4:'微信'
	private int feeType;
	private double amount;
	private String comment;
	private int userId;
	private String userName;
	private int status;
	private java.sql.Date expenseDate;
	private java.util.Date lastUpdateTime;

	public int getFeeType() {
		return feeType;
	}
	public void setFeeType(int feeType) {
		this.feeType = feeType;
	}
	public ChainStore getEntity() {
		return entity;
	}
	public void setEntity(ChainStore entity) {
		this.entity = entity;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public ExpenseType getExpenseType() {
		return expenseType;
	}
	public void setExpenseType(ExpenseType expenseType) {
		this.expenseType = expenseType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public java.sql.Date getExpenseDate() {
		return expenseDate;
	}
	public void setExpenseDate(java.sql.Date expenseDate) {
		this.expenseDate = expenseDate;
	}
	public java.util.Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(java.util.Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	//1:'现金',2:'银行',3:'支付宝',4:'微信'
	public enum feeTypeE{
		CASH (1, "现金"), 
		CARD (2,"银行"),
		ALIPAY (3,"支付宝"),
		WECHAT (4,"微信");
		
		private int value;
		private String name;

		private feeTypeE(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}
		public String getName() {
			return name;
		}

		public static String getName(int value ){
			if (value == CASH.getValue())
				return CASH.getName();
			else if (value == CARD.getValue())
				return CARD.getName();
			else if (value == ALIPAY.getValue())
				return ALIPAY.getName();
			else if (value == WECHAT.getValue())
				return WECHAT.getName();
			else 
				return "";
		}
	}
	
	public enum statusE{
		NORMAL (0, "正常"), 
		DELETED (-1,"删除");
		
		private int value;
		private String status;

		private statusE(int value, String status) {
			this.value = value;
			this.status = status;
		}

		public int getValue() {
			return value;
		}
		public String getStatus() {
			return status;
		}

		public static String getStatusS(int value ){
			if (value == NORMAL.getValue())
				return NORMAL.getStatus();
			else if (value == DELETED.getValue())
				return DELETED.getStatus();
			else 
				return "";
		}
	}

}
