package com.onlineMIS.ORM.DAO.headQ.custMgmt;


import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;

@Repository
public class HeadQCustDaoImpl extends BaseDAO<HeadQCust>{

	public List<HeadQCust> getCustByPinyin(String pinyin, int status) {
		DetachedCriteria criteria = DetachedCriteria.forClass(HeadQCust.class);
		criteria.add(Restrictions.like("pinyin", pinyin +"%"));
		criteria.add(Restrictions.eq("status", status));
		
		return this.getByCritera(criteria, true);
	}

	public HeadQCust getAllCustObj() {
		HeadQCust cust = new HeadQCust();
		cust.setId(0);
		cust.setName("所有客户");
		return cust;
	}
	

}
