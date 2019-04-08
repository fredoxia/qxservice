package com.onlineMIS.ORM.DAO.shared.expense;


import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.shared.expense.ExpenseType;

@Repository
public class ExpenseTypeDaoImpl extends BaseDAO<ExpenseType>{
	public List<ExpenseType> getExpenseTypes(Integer belong){
		DetachedCriteria criteria = DetachedCriteria.forClass(ExpenseType.class);	
        if (belong == null || belong == 0)
		   criteria.add(Restrictions.isNull("belong"));
        else 
           criteria.add(Restrictions.eq("belong", belong));
		
		List<ExpenseType> resultList = this.getByCritera(criteria, true); 
		
		return resultList;
	}

	/**
	 * 获取总部的菜单
	 * @return
	 */
	public List<ExpenseType> getHeadqExpenseType(Integer parentId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ExpenseType.class);	
        criteria.add(Restrictions.isNull("belong"));
        
        if (parentId == null)
        	criteria.add(Restrictions.isNull("parentId"));
        else 
            criteria.add(Restrictions.eq("parentId", parentId));
		
		List<ExpenseType> resultList = this.getByCritera(criteria, true); 
		
		return resultList;
	}
}
