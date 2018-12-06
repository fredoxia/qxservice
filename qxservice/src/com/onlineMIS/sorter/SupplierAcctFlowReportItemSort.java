package com.onlineMIS.sorter;

import java.util.Date;

import com.onlineMIS.ORM.entity.chainS.inventoryFlow.ChainInventoryFlowOrderProduct;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Product;
import com.onlineMIS.ORM.entity.headQ.finance.ChainAcctFlowReportItem;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.SupplierAcctFlowReportItem;
import com.onlineMIS.common.loggerLocal;
/**
 * the chain inventory product sort by year, quarter, brand, product code
 * @author fredo
 *
 */
public 	class SupplierAcctFlowReportItemSort  implements java.util.Comparator<SupplierAcctFlowReportItem>{
		 public int compare(SupplierAcctFlowReportItem p1,SupplierAcctFlowReportItem p2){
	         Date date1 = p1.getDate();
	         Date date2 = p2.getDate();

	         
	         return date1.compareTo(date2);
		 }
	}
