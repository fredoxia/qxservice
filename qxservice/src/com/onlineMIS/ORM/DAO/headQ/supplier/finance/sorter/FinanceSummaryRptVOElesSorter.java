package com.onlineMIS.ORM.DAO.headQ.supplier.finance.sorter;

import com.onlineMIS.ORM.entity.headQ.supplier.finance.FinanceSummaryRptVOEles;

public class FinanceSummaryRptVOElesSorter implements java.util.Comparator<FinanceSummaryRptVOEles>{
	@Override
	public int compare(FinanceSummaryRptVOEles arg0, FinanceSummaryRptVOEles arg1) {
	       Double sum1 = arg0.getSum();
	       Double sum2 = arg1.getSum();

           return sum2.compareTo(sum1);
	 }
}	
