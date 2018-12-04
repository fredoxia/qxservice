package com.onlineMIS.ORM.DAO.headQ.supplier.finance;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.headQ.finance.HeadQFinanceTrace;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.SupplierFinanceTrace;
import com.onlineMIS.common.Common_util;

@Repository
public class SupplierFinanceTraceImpl extends BaseDAO<SupplierFinanceTrace> {


	public double getSumOfFinanceCategory(int categoryId, int supplierId){
		Object[] values = new Object[]{supplierId, categoryId};
		
		String hql = "SELECT SUM(amount) from SupplierFinanceTrace WHERE supplierId =? AND categoryId=?";
		
		List<Object> results = this.executeHQLSelect(hql, values,null, true);
		
		if (results != null && results.size() > 0){
			double sum = Common_util.getDouble(results.get(0));
			return sum;
		} else 
			return 0;
	}


}
