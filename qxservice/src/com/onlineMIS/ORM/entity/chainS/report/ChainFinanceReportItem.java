package com.onlineMIS.ORM.entity.chainS.report;

import com.onlineMIS.ORM.entity.headQ.finance.FinanceCategory;

public class ChainFinanceReportItem {
	    private String category;
	    private double amount;
	    
	    public ChainFinanceReportItem(){
	    	
	    }
	    
	    public ChainFinanceReportItem(String category, double amount){
	    	this.category = category;
	    	this.amount = amount;
	    }
	    
	    
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public double getAmount() {
			return amount;
		}
		public void setAmount(double amount) {
			this.amount = amount;
		}
	    
}
