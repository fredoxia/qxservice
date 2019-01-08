package com.onlineMIS.action.headQ.inventoryFlow;

import java.io.InputStream;

import com.onlineMIS.action.chainS.ChainActionFormBaseBean;


public class HeadqInventoryFlowFormBean extends ChainActionFormBaseBean{
	
	private int parentId = 0;
	private int storeId = 1;
	private int yearId = 0;
	private int quarterId = 0;
	private int brandId = 0;
	
    //file download
	private InputStream fileStream;
	private String fileName;
	private int pbId;
	
	
	public int getPbId() {
		return pbId;
	}
	public void setPbId(int pbId) {
		this.pbId = pbId;
	}
	public InputStream getFileStream() {
		return fileStream;
	}
	public void setFileStream(InputStream fileStream) {
		this.fileStream = fileStream;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
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
	
	
	
}
