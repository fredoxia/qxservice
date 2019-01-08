package com.onlineMIS.ORM.DAO.headQ.inventory;

import com.onlineMIS.common.Common_util;

public class HeadqInventoryVO {
	public static final String STATE_CLOSED = "closed";
	public static final String STATE_OPEN = "open";
	
	private String id;
	private String name;

	private int inventory;
	private double wholeSales;
	private String state;
	private String param;
	
	private int parentId = 0;
	private int storeId = 1;
	private int yearId = 0;
	private int quarterId = 0;
	private int brandId = 0;
	private int pbId;
	
	public HeadqInventoryVO() {
		// TODO Auto-generated constructor stub
	}
	
	public HeadqInventoryVO(int parentId, String name, int inventory, double wholeSales, String state, int storeId, int yearId, int quarterId, int brandId){
		this.setId(Common_util.getUUID());
		this.setParentId(parentId);
		this.setName(name);
		this.setInventory(inventory);
		this.setWholeSales(wholeSales);
		this.setState(state);
		this.setStoreId(storeId);
		this.setYearId(yearId);
		this.setQuarterId(quarterId);
		this.setBrandId(brandId);

	}


	public int getPbId() {
		return pbId;
	}

	public void setPbId(int pbId) {
		this.pbId = pbId;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public int getYearId() {
		return yearId;
	}

	public void setYearId(int yearId) {
		this.yearId = yearId;
	}

	public int getQuarterId() {
		return quarterId;
	}

	public void setQuarterId(int quarterId) {
		this.quarterId = quarterId;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getInventory() {
		return inventory;
	}
	public void setInventory(int inventory) {
		this.inventory = inventory;
	}
	public double getWholeSales() {
		return wholeSales;
	}
	public void setWholeSales(double wholeSales) {
		this.wholeSales = wholeSales;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	
	
}
