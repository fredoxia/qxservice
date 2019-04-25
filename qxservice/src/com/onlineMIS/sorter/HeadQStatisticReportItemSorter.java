package com.onlineMIS.sorter;

import java.util.Comparator;

import com.onlineMIS.ORM.entity.chainS.report.ChainReportItemVO;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Brand;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
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
		
		if (brand0.getBrand_ID() != brand1.getBrand_ID()){
			return brand0.getBrand_ID() - brand1.getBrand_ID();
		} else {
			String productCode0 = pb0.getProduct().getProductCode();
			String productCode1 = pb1.getProduct().getProductCode();
			
			return productCode0.compareTo(productCode1);
		}
	}

}
