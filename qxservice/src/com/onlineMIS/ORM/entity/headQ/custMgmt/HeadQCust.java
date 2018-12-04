package com.onlineMIS.ORM.entity.headQ.custMgmt;

import java.io.Serializable;

public class HeadQCust  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5845972252957273099L;
	private int id;
	private String name;
	private String phone;
	private String area;
	private String address;
	private double initialAcctBalance;
	private double currentAcctBalance;
	private String comment;
	private String pinyin;
	
	//0:正常, 1:删除
	private int status;
	
	public enum CustStatusEnum {
		GOOD(0, "正常"), DELETED(1,"禁用");
		private final int key;
	    private final String value;
	    
	    private CustStatusEnum(int key,String value){
	        this.key = key;
	        this.value = value;
	    }
	    //根据key获取枚举
	    public static CustStatusEnum getEnumByKey(int key){
	        for(CustStatusEnum temp:CustStatusEnum.values()){
	            if(temp.getKey() == key){
	                return temp;
	            }
	        }
	        return null;
	    }
	    public int getKey() {
	        return key;
	    }
	    public String getValue() {
	        return value;
	    }
	}
	
	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getArea() {
		return area;
	}
	
	
	public void setArea(String area) {
		this.area = area;
	}
	
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public double getCurrentAcctBalance() {
		return currentAcctBalance;
	}
	
	public void setCurrentAcctBalance(double currentAcctBalance) {
		this.currentAcctBalance = currentAcctBalance;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getInitialAcctBalance() {
		return initialAcctBalance;
	}

	public void setInitialAcctBalance(double initialAcctBalance) {
		this.initialAcctBalance = initialAcctBalance;
	}

	/**
	 * 当要更改一个cust时， currentAcctBalance是不能修改的
	 * @param cust
	 */
	public void copyInforFromOther(HeadQCust cust){
		this.setAddress(cust.getAddress());
		this.setArea(cust.getArea());
		this.setComment(cust.getComment());
		this.setName(cust.getName());
		this.setPhone(cust.getPhone());
		this.setStatus(cust.getStatus());
		this.setInitialAcctBalance(cust.getInitialAcctBalance());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}



