package com.onlineMIS.sorter;


import com.onlineMIS.ORM.entity.headQ.report.HeadQExpenseRptElesVO;
import com.onlineMIS.common.loggerLocal;

public 	class HeadQExpenseReportSort implements java.util.Comparator<HeadQExpenseRptElesVO>{
		 public int compare(HeadQExpenseRptElesVO obj1,HeadQExpenseRptElesVO obj2){
         int index1 = 0;
         int index2 = 0;
         
         try{
        	 index1 = obj1.getExpenseTypeParentId();
             index2 = obj2.getExpenseTypeParentId();
         } catch (NullPointerException e) {
        	 loggerLocal.error(e);
		  }
		  if(index1 != index2)
		    return index1 - index2;
		  else{
	        	 index1 = obj1.getExpenseTypeId();
	             index2 = obj2.getExpenseTypeId();
	             
	             return index1 - index2;
		  }
		    
		 }
	}
