package com.onlineMIS.ORM.DAO.headQ.supplier.finance;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.chainS.inventoryFlow.ChainInOutStock;
import com.onlineMIS.ORM.entity.headQ.finance.HeadQAcctFlow;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.SupplierAcctFlow;
import com.onlineMIS.common.Common_util;

@Repository
public class SupplierAcctFlowDaoImpl  extends BaseDAO<SupplierAcctFlow> {
	/**
	 * get the accumulate acct flow
	 * @return
	 */
	public double getAccumulateAcctFlow(int supplierId){
		DetachedCriteria criteria = DetachedCriteria.forClass(SupplierAcctFlow.class);

		criteria.add(Restrictions.eq("supplierId", supplierId));
		
		criteria.setProjection(Projections.sum("acctAmt"));
		
		List<Object> result = getByCriteriaProjection(criteria, true);

        return Common_util.getProjectionDoubleValue(result);
	}
	
	/**
	 * get the accumulate acct flow
	 * @return
	 */
	public double getAccumulateAcctFlowBefore(int supplierId, Date date){
		DetachedCriteria criteria = DetachedCriteria.forClass(SupplierAcctFlow.class);

		criteria.add(Restrictions.eq("supplierId", supplierId));
		
		criteria.add(Restrictions.lt("date", date));
		
		criteria.setProjection(Projections.sum("acctAmt"));
		
		List<Object> result = getByCriteriaProjection(criteria, true);

        return Common_util.getProjectionDoubleValue(result);
	}
}
