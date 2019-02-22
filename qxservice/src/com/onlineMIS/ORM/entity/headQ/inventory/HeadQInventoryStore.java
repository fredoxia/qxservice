package com.onlineMIS.ORM.entity.headQ.inventory;

import java.io.Serializable;

public class HeadQInventoryStore implements Serializable {
   /**
	 * 
	 */
	private static final long serialVersionUID = 4192031352894390928L;
	public static final int INVENTORY_STORE_DEFAULT_ID =1;
    private int id;
    private String name;
    private String comment;
    
    public HeadQInventoryStore(){
    	
    }
    
    public HeadQInventoryStore(int id){
    	this.setId(id);
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	   
}
