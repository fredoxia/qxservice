package com.onlineMIS.ORM.DAO.headQ.custMgmt;


import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;

@Repository
public class HeadQCustDaoImpl extends BaseDAO<HeadQCust>{

	public List<HeadQCust> getCustByPinyin(String pinyin, int status) {
		DetachedCriteria criteria = DetachedCriteria.forClass(HeadQCust.class);
		criteria.add(Restrictions.like("pinyin", pinyin +"%"));
		criteria.add(Restrictions.eq("status", status));
		
		return this.getByCritera(criteria, true);
	}
	

}
