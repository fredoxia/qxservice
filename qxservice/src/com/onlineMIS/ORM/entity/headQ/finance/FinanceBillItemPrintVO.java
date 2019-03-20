package com.onlineMIS.ORM.entity.headQ.finance;

import com.onlineMIS.common.Common_util;

public class FinanceBillItemPrintVO {
	private String financeCategory;
	private String total;
	private String comment;
	
	public FinanceBillItemPrintVO(){
		
	}
	
	public FinanceBillItemPrintVO(FinanceBillItem item){
		this.setTotal(Common_util.df.format(item.getTotal()));
		this.setComment(item.getComment());
		this.setFinanceCategory(item.getFinanceCategory().getItemName());
	}
	
	public String getFinanceCategory() {
		return financeCategory;
	}
	public void setFinanceCategory(String financeCategory) {
		this.financeCategory = financeCategory;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
