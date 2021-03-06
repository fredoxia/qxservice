package com.onlineMIS.ORM.entity.headQ.supplier.finance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javassist.runtime.Inner;

import com.onlineMIS.ORM.entity.base.BaseOrder;
import com.onlineMIS.ORM.entity.base.BaseProduct;
import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBill;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.common.loggerLocal;
import com.onlineMIS.sorter.ProductSortByIndex;

public class FinanceBillSupplier extends BaseOrder implements Serializable {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1578267557986495659L;{
		typeHQMap.put(FINANCE_PAID_HQ, "付款单");
		typeHQMap.put(FINANCE_DECREASE_HQ, "应付减少单据");
		typeHQMap.put(FINANCE_INCREASE_HQ, "应付增加单据");
		typeHQMap.put(FINANCE_INCOME_HQ, "收款单");
	}
	
	public static final int FINANCE_PAID_HQ = 1;
	public static final int FINANCE_INCOME_HQ = 2;
	public static final int FINANCE_DECREASE_HQ = 3;
	public static final int FINANCE_INCREASE_HQ = 4;
	
	private int id;
	private UserInfor creatorHq;
	private HeadQSupplier supplier = new HeadQSupplier();
	private double invoiceTotal;
	private double invoiceDiscount;
	private java.sql.Date billDate;
	private Date createDate;
	private String comment;
    private double preAcctAmt;
    private double postAcctAmt;
	
	private Set<FinanceBillSupplierItem> financeBillItemSet = new HashSet<FinanceBillSupplierItem>();
	private List<FinanceBillSupplierItem> financeBillItemList = new ArrayList<FinanceBillSupplierItem>();

	public double getInvoiceDiscount() {
		return invoiceDiscount;
	}
	public void setInvoiceDiscount(double invoiceDiscount) {
		this.invoiceDiscount = invoiceDiscount;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UserInfor getCreatorHq() {
		return creatorHq;
	}
	public void setCreatorHq(UserInfor creatorHq) {
		this.creatorHq = creatorHq;
	}

	
	public HeadQSupplier getSupplier() {
		return supplier;
	}
	public void setSupplier(HeadQSupplier supplier) {
		this.supplier = supplier;
	}
	public double getInvoiceTotal() {
		return invoiceTotal;
	}
	public void setInvoiceTotal(double invoiceTotal) {
		this.invoiceTotal = invoiceTotal;
	}
	public java.sql.Date getBillDate() {
		return billDate;
	}
	public void setBillDate(java.sql.Date billDate) {
		this.billDate = billDate;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Set<FinanceBillSupplierItem> getFinanceBillItemSet() {
		return financeBillItemSet;
	}
	public void setFinanceBillItemSet(Set<FinanceBillSupplierItem> financeBillItemSet) {
		this.financeBillItemSet = financeBillItemSet;
	}
	public List<FinanceBillSupplierItem> getFinanceBillItemList() {
		return financeBillItemList;
	}
	public void setFinanceBillItemList(List<FinanceBillSupplierItem> financeBillItemList) {
		this.financeBillItemList = financeBillItemList;
	}
	


	/**
     * to build the index for each order product
     * index is to tell the sequence of the products scanned
     */
	public void buildIndex() {
		for(int i =0; i< financeBillItemList.size();i++){
			FinanceBillSupplierItem financeBillItem = financeBillItemList.get(i);
			if (financeBillItem!= null)
				financeBillItem.setIndex(i);		
		}
	}
	
	public void putListToSet(){
		double invoiceTotal = 0;
		financeBillItemSet.clear();
		if (financeBillItemList != null)
			for (int i = 0; i < financeBillItemList.size(); i++){
				if (financeBillItemList.get(i)!= null && financeBillItemList.get(i).getFinanceCategorySupplier() != null){
					FinanceBillSupplierItem financeBillItem = financeBillItemList.get(i);
					financeBillItem.setFinanceBillSupplier(this);
					invoiceTotal += financeBillItem.getTotal();
				    financeBillItemSet.add(financeBillItem);
				}
			}
		this.setInvoiceTotal(invoiceTotal);
	}
	
    public void putSetToList(){
		if (financeBillItemSet != null){
			financeBillItemList.clear();
			for (Iterator<FinanceBillSupplierItem> billItem_Iterator = financeBillItemSet.iterator();billItem_Iterator.hasNext();){
				financeBillItemList.add(billItem_Iterator.next());
			}
			Collections.sort(financeBillItemList, new FinanceBillItemSort());
		}
    }
    
    class FinanceBillItemSort  implements java.util.Comparator<FinanceBillSupplierItem>{

		@Override
		public int compare(FinanceBillSupplierItem obj1,FinanceBillSupplierItem obj2){
		         int index1 = 0;
		         int index2 = 0;
		         
		         try{
		        	 index1 = obj1.getFinanceCategorySupplier().getType();
		             index2 = obj2.getFinanceCategorySupplier().getType();
		         } catch (NullPointerException e) {
		        	 loggerLocal.error(e);
				  }
				  if(index1 < index2)
				   return -1;
				  else if(index1 > index2)
				   return 1;
				  else
				   return 0;
		}
    }

}
