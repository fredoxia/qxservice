package com.onlineMIS.action.headQ.report;

import java.util.ArrayList;
import java.util.List;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Brand;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Category;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Color;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Quarter;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Year;

public class HeadQReportUIBean {
	private List<Year> years ;
	private List<Quarter> quarters;
	public List<Year> getYears() {
		return years;
	}
	public void setYears(List<Year> years) {
		this.years = years;
	}
	public List<Quarter> getQuarters() {
		return quarters;
	}
	public void setQuarters(List<Quarter> quarters) {
		this.quarters = quarters;
	}
	
	
}
