package com.onlineMIS.ORM.DAO.headQ.finance;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.headQ.finance.HeadQFinanceTrace;
import com.onlineMIS.common.Common_util;

@Repository
public class HeadQFinanceTraceImpl extends BaseDAO<HeadQFinanceTrace> {


	public double getSumOfFinanceCategory(int categoryId, int clientId){
		Object[] values = new Object[]{clientId, categoryId};
		
		String hql = "SELECT SUM(amount) from HeadQFinanceTrace WHERE clientId =? AND categoryId=?";
		
		List<Object> results = this.executeHQLSelect(hql, values,null, true);
		
		if (results != null && results.size() > 0){
			double sum = Common_util.getDouble(results.get(0));
			return sum;
		} else 
			return 0;
	}


}
