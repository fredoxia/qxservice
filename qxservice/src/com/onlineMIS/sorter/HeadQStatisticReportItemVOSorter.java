package com.onlineMIS.sorter;

import java.util.Comparator;

import com.onlineMIS.ORM.entity.chainS.report.ChainPurchaseStatisticReportItemVO;
import com.onlineMIS.ORM.entity.chainS.report.ChainReportItemVO;
import com.onlineMIS.ORM.entity.headQ.report.HeadQReportItemVO;

public class HeadQStatisticReportItemVOSorter implements
		Comparator<HeadQReportItemVO> {

	@Override
	public int compare(HeadQReportItemVO o1,
			HeadQReportItemVO o2) {

		int yearIdO1 = o1.getYearId();
		int yearIdO2 = o2.getYearId();
		
		int quarterIdO1 = o1.getQuarterId();
		int quarterIdO2 = o2.getQuarterId();
		
		String nameO1 = o1.getName();
		String nameO2 = o2.getName();
		
		if (yearIdO1 != yearIdO2){
			return yearIdO1 - yearIdO2;
	    } else if (quarterIdO1 != quarterIdO2){
			return quarterIdO1 - quarterIdO2;
	    } else {
			return nameO1.compareTo(nameO2);
		}
	}

}
