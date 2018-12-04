package com.onlineMIS.ORM.entity.headQ.barcodeGentor;

import java.io.Serializable;

public class ColorGroup implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7959732544493720384L;
	private int id;
	private String comment;
	private String name;
	private int deleted ;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDeleted() {
		return deleted;
	}
	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}
	@Override
	public String toString() {
		return "ColorGroup [id=" + id + ", comment=" + comment + ", name="
				+ name + ", deleted=" + deleted + "]";
	}
	
}
