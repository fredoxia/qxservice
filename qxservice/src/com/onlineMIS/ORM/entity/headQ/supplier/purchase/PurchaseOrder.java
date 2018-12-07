package com.onlineMIS.ORM.entity.headQ.supplier.purchase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.onlineMIS.ORM.entity.base.BaseOrder;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust.CustStatusEnum;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadQInventoryStore;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrderProduct;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.sorter.ProductSortByIndex;

public class PurchaseOrder extends BaseOrder {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4158424919552736997L;
	public static final int TYPE_PURCHASE = 0;
	public static final int TYPE_RETURN = 1;
	
	private int id;
	private HeadQInventoryStore store = new HeadQInventoryStore(1);
	private String comment;
	private Date creationTime = new Date();
	private Date lastUpdateTime = new Date();
	private int totalQuantity;
	private double totalRecCost;
	private double totalWholePrice;
	private double totalDiscount;  
	private double preAcctAmt;
	private double postAcctAmt;
	private UserInfor orderAuditor;
	private UserInfor orderCounter;
	private HeadQSupplier supplier;
    private List<PurchaseOrderProduct> productList = new ArrayList<PurchaseOrderProduct>();
    private Set<PurchaseOrderProduct> productSet = new HashSet<PurchaseOrderProduct>();
    {
    	typeHQMap.put(TYPE_PURCHASE, "采购入库单");
    	typeHQMap.put(TYPE_RETURN, "采购退货单");
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public HeadQInventoryStore getStore() {
		return store;
	}
	public void setStore(HeadQInventoryStore store) {
		this.store = store;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public int getTotalQuantity() {
		return totalQuantity;
	}
	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}
	public double getTotalRecCost() {
		return totalRecCost;
	}
	public void setTotalRecCost(double totalRecCost) {
		this.totalRecCost = totalRecCost;
	}
	public double getTotalWholePrice() {
		return totalWholePrice;
	}
	public void setTotalWholePrice(double totalWholePrice) {
		this.totalWholePrice = totalWholePrice;
	}
	public double getTotalDiscount() {
		return totalDiscount;
	}
	public void setTotalDiscount(double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}
	public double getPreAcctAmt() {
		return preAcctAmt;
	}
	public void setPreAcctAmt(double preAcctAmt) {
		this.preAcctAmt = preAcctAmt;
	}
	public double getPostAcctAmt() {
		return postAcctAmt;
	}
	public void setPostAcctAmt(double postAcctAmt) {
		this.postAcctAmt = postAcctAmt;
	}
	public UserInfor getOrderAuditor() {
		return orderAuditor;
	}
	public void setOrderAuditor(UserInfor orderAuditor) {
		this.orderAuditor = orderAuditor;
	}
	public UserInfor getOrderCounter() {
		return orderCounter;
	}
	public void setOrderCounter(UserInfor orderCounter) {
		this.orderCounter = orderCounter;
	}
	public HeadQSupplier getSupplier() {
		return supplier;
	}
	public void setSupplier(HeadQSupplier supplier) {
		this.supplier = supplier;
	}
	public List<PurchaseOrderProduct> getProductList() {
		return productList;
	}
	public void setProductList(List<PurchaseOrderProduct> productList) {
		this.productList = productList;
	}
	public Set<PurchaseOrderProduct> getProductSet() {
		return productSet;
	}
	public void setProductSet(Set<PurchaseOrderProduct> productSet) {
		this.productSet = productSet;
	}
	
	public void putListToSet(){
		if (productList != null)
			for (int i = 0; i < productList.size(); i++){
				if (productList.get(i)!= null && productList.get(i).getPb() != null && productList.get(i).getPb().getBarcode() != null && !productList.get(i).getPb().getBarcode().trim().equals("") && productList.get(i).getQuantity()>0){
					PurchaseOrderProduct orderProduct = productList.get(i);
				    orderProduct.setOrder(this);
				    productSet.add(orderProduct);
				}
			}
    }
    
    public void putSetToList(){
		if (productSet != null){
			for (Iterator<PurchaseOrderProduct> product_Iterator = productSet.iterator();product_Iterator.hasNext();){
				productList.add(product_Iterator.next());
			}
			Collections.sort(productList, new ProductSortByIndex());
		}
    }
}
