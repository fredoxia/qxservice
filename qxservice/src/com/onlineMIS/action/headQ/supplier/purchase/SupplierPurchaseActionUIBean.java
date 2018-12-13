package com.onlineMIS.action.headQ.supplier.purchase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;

public class SupplierPurchaseActionUIBean {
	private List<PurchaseOrder> orders = new ArrayList<PurchaseOrder>();

	private List<UserInfor> users = new ArrayList<UserInfor>();
	private Map<Integer, String> orderStatusMap = new HashMap<Integer, String>();
	private Map<Integer, String> orderTypeMap = new HashMap<Integer, String>();
	
	
	public Map<Integer, String> getOrderStatusMap() {
		return orderStatusMap;
	}

	public void setOrderStatusMap(Map<Integer, String> orderStatusMap) {
		this.orderStatusMap = orderStatusMap;
	}

	public Map<Integer, String> getOrderTypeMap() {
		return orderTypeMap;
	}

	public void setOrderTypeMap(Map<Integer, String> orderTypeMap) {
		this.orderTypeMap = orderTypeMap;
	}

	public List<PurchaseOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<PurchaseOrder> orders) {
		this.orders = orders;
	}

	public List<UserInfor> getUsers() {
		return users;
	}

	public void setUsers(List<UserInfor> users) {
		this.users = users;
	}

	

}
