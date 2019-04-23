package com.onlineMIS.ORM.entity.headQ.report;

import java.io.Serializable;
import java.util.Date;

import com.onlineMIS.common.Common_util;

public class HeadQReportItemVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -917163636081888918L;
	public static final String STATE_CLOSED = "closed";
	public static final String STATE_OPEN = "open";
	public static final String spliter = "@";
	private int parentId;
	private int supplierId;
	private int clientId;
	private int yearId;
	private int quarterId;
	private int brandId;
	private int pbId;
	private String state;
	private String name;
	private String id;

	private String barcode = "";
	
	public HeadQReportItemVO(){
		
	}
	public HeadQReportItemVO(String name, int parentId, int yearId, int quarterId, int brandId, int pbId, String state){
		super();
		this.setId(Common_util.getUUID());
		this.setName(name);
		this.setParentId(parentId);
		this.setYearId(yearId);
		this.setQuarterId(quarterId);
		this.setBrandId(brandId);
		this.setState(state);
		this.setPbId(pbId);
		
	}
	
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public int getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getPbId() {
		return pbId;
	}
	public void setPbId(int pbId) {
		this.pbId = pbId;
	}

	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
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
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String generateKeys(){
		return 	parentId + spliter + yearId + spliter + quarterId + spliter + brandId +spliter + pbId ;
	}
	
}
