package com.onlineMIS.action.headQ.custMgmt;



import com.onlineMIS.ORM.entity.base.Pager;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Brand;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Category;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Color;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.NumPerHand;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductUnit;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Quarter;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Size;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Year;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;

public class HeadQCustMgmtFormBean {
	private HeadQCust cust = new HeadQCust();

	
	public HeadQCust getCust() {
		return cust;
	}

	public void setCust(HeadQCust cust) {
		this.cust = cust;
	}
	
}
