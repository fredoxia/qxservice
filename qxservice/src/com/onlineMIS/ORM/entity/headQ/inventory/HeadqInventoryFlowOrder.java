package com.onlineMIS.ORM.entity.headQ.inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.onlineMIS.ORM.entity.base.BaseOrder;
import com.onlineMIS.ORM.entity.base.BaseProduct;
import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.chainS.user.ChainUserInfor;
import com.onlineMIS.sorter.ChainInventoryOrderProductSorter;
import com.onlineMIS.sorter.ProductSortByIndex;
/**
 * This class is 
 * @author fredo
 *
 */
public class HeadqInventoryFlowOrder extends BaseOrder {
	/**
	 * 1. overflow order 报溢单, value=1 (电脑是4，实际是5 -> 报溢单添加到电脑)
	 * 2. flowloss order 报损单, value=2 (电脑时4，实际是3 -> 报损单在电脑存储减除)
	 * 3. inventory order 库存单， value=3
	 */
	public final static int OVER_FLOW_ORDER = 1;
	public final static int FLOW_LOSS_ORDER = 2;
	public final static int INVENTORY_ORDER = 3;

	/**
	 * 
	 */
	private static final long serialVersionUID = 7484584112025972658L;
	{
		typeHQMap.put(OVER_FLOW_ORDER, "报溢单据");
		typeHQMap.put(FLOW_LOSS_ORDER, "报损单据");
		typeHQMap.put(INVENTORY_ORDER, "库存单据");
	}
	
	private int id;
	private int totalQuantity;
	private int totalInventoryQ;
	private int totalQuantityDiff;
	private double totalWholePrice = 0;
	private double totalCost = 0;
	private Date orderDate;
	private String comment;
	private ChainUserInfor creator;
	private Set<HeadqInventoryFlowOrderProduct> productSet = new HashSet<HeadqInventoryFlowOrderProduct>();
	private List<HeadqInventoryFlowOrderProduct> productList = new ArrayList<HeadqInventoryFlowOrderProduct>();


	public double getTotalWholePrice() {
		return totalWholePrice;
	}

	public void setTotalWholePrice(double totalWholePrice) {
		this.totalWholePrice = totalWholePrice;
	}

	public double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(double totalCost) {
		this.totalCost = totalCost;
	}

	public int getTotalQuantityDiff() {
		return totalQuantityDiff;
	}

	public void setTotalQuantityDiff(int totalQuantityDiff) {
		this.totalQuantityDiff = totalQuantityDiff;
	}

	public int getTotalInventoryQ() {
		return totalInventoryQ;
	}

	public void setTotalInventoryQ(int totalInventoryQ) {
		this.totalInventoryQ = totalInventoryQ;
	}


	public ChainUserInfor getCreator() {
		return creator;
	}

	public void setCreator(ChainUserInfor creator) {
		this.creator = creator;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(int totalQuantity) {
		this.totalQuantity = totalQuantity;
	}


	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Set<HeadqInventoryFlowOrderProduct> getProductSet() {
		return productSet;
	}

	public void setProductSet(Set<HeadqInventoryFlowOrderProduct> productSet) {
		this.productSet = productSet;
	}

	public List<HeadqInventoryFlowOrderProduct> getProductList() {
		return productList;
	}

	public void setProductList(List<HeadqInventoryFlowOrderProduct> productList) {
		this.productList = productList;
	}



	/**
     * to build the index for each order product
     * index is to tell the sequence of the products scanned
     */
	public void buildIndex() {
		for(int i =0; i< productList.size();i++){
			HeadqInventoryFlowOrderProduct product = productList.get(i);
			if (product!= null && product.getProductBarcode() != null && product.getProductBarcode().getBarcode() != null && !product.getProductBarcode().getBarcode().trim().equals("") && product.getProductBarcode().getId() != 0){
				product.setIndex(i);		
			}
		}
	}
	
	/**
	 * function to put the list to set
	 * from web to database
	 */
	public void putListToSet(){
		
		if (productList != null)
			for (int i = 0; i < productList.size(); i++){
				if (productList.get(i)!= null && productList.get(i).getProductBarcode() != null && productList.get(i).getProductBarcode().getBarcode() != null && !productList.get(i).getProductBarcode().getBarcode().trim().equals("")&& productList.get(i).getProductBarcode().getId() != 0){
					HeadqInventoryFlowOrderProduct orderProduct = productList.get(i);
				    orderProduct.setFlowOrder(this);
				    productSet.add(orderProduct);
				}
			}
	}

	/**
	 * function to put the 报损，报溢，调货单 product list to set
	 * from web to database
	 */
	public void putSetToList(){
		if (productSet != null && !productSet.isEmpty()){
			for (Iterator<HeadqInventoryFlowOrderProduct> flowOrders = productSet.iterator();flowOrders.hasNext();){
				productList.add(flowOrders.next());
			}
			
			Collections.sort(productList, new ProductSortByIndex());
		}
	}
	
	/**
	 * function to put the product set to list 指定其他
	 * from web to database
	 */
	public void putSetToList(Comparator<HeadqInventoryFlowOrderProduct> comparator){
		if (productSet != null && !productSet.isEmpty()){
			for (Iterator<HeadqInventoryFlowOrderProduct> flowOrders = productSet.iterator();flowOrders.hasNext();){
				productList.add(flowOrders.next());
			}
			
			Collections.sort(productList, comparator);
		}
	}
	
	@Override
	public String toString() {
		return "ChainInventoryFlowOrder [id=" + id + ", totalQuantity="
				+ totalQuantity + ", totalWholePrice=" + totalWholePrice
				+ ", orderDate=" + orderDate + ", orderType=" + type
				+ ", comment=" + comment + ", creator=" + creator
			    + ", status=" + status
				+ "]";
	}
	
	

}
