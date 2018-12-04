package com.onlineMIS.ORM.DAO.headQ.finance;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.chainS.inventoryFlow.ChainInOutStock;
import com.onlineMIS.ORM.entity.headQ.finance.HeadQAcctFlow;
import com.onlineMIS.common.Common_util;

@Repository
public class HeadQAcctFlowDaoImpl  extends BaseDAO<HeadQAcctFlow> {
	/**
	 * get the accumulate acct flow
	 * @return
	 */
	public double getAccumulateAcctFlow(int clientId){
		DetachedCriteria criteria = DetachedCriteria.forClass(HeadQAcctFlow.class);

		criteria.add(Restrictions.eq("clientId", clientId));
		
		criteria.setProjection(Projections.sum("acctAmt"));
		
		List<Object> result = getByCriteriaProjection(criteria, true);

        return Common_util.getProjectionDoubleValue(result);
	}
	
	/**
	 * get the accumulate acct flow
	 * @return
	 */
	public double getAccumulateAcctFlowBefore(int clientId, Date date){
		DetachedCriteria criteria = DetachedCriteria.forClass(HeadQAcctFlow.class);

		criteria.add(Restrictions.eq("clientId", clientId));
		
		criteria.add(Restrictions.lt("date", date));
		
		criteria.setProjection(Projections.sum("acctAmt"));
		
		List<Object> result = getByCriteriaProjection(criteria, true);

        return Common_util.getProjectionDoubleValue(result);
	}
}
