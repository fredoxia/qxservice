package com.onlineMIS.sorter;

import java.util.Comparator;

import com.onlineMIS.ORM.entity.chainS.report.ChainReportItemVO;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Brand;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Quarter;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Year;
import com.onlineMIS.ORM.entity.headQ.report.HeadQPurchaseStatisticReportItem;
import com.onlineMIS.ORM.entity.headQ.report.HeadQStatisticReportItem;

public class HeadQStatisticReportItemSorter  implements
Comparator<HeadQStatisticReportItem> {

	@Override
	public int compare(HeadQStatisticReportItem arg0, HeadQStatisticReportItem arg1) {
		ProductBarcode pb0 = arg0.getPb();
		ProductBarcode pb1 = arg1.getPb();
		
		Brand brand0 = pb0.getProduct().getBrand();
		Brand brand1 = pb1.getProduct().getBrand();
		
		Year year0 = pb0.getProduct().getYear();
		Year year1 = pb1.getProduct().getYear();
		
		Quarter quarter0 = pb0.getProduct().getQuarter();
		Quarter quarter1 = pb1.getProduct().getQuarter();
		
		
		if (year0.getYear_ID() != year1.getYear_ID()){
			return year0.getYear_ID() - year1.getYear_ID();
		} else if (quarter0.getQuarter_ID() != quarter1.getQuarter_ID()){
			return quarter0.getQuarter_ID() - quarter1.getQuarter_ID();
		} else if (brand0.getBrand_ID() != brand1.getBrand_ID()){
			return brand0.getBrand_ID() - brand1.getBrand_ID();
		} else {
			String productCode0 = pb0.getProduct().getProductCode();
			String productCode1 = pb1.getProduct().getProductCode();
			
			return productCode0.compareTo(productCode1);
		}
	}

}
