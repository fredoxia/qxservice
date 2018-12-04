package com.onlineMIS.ORM.DAO.headQ.supplier.finance;

import org.springframework.stereotype.Repository;

import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.FinanceBillSupplier;

@Repository
public class FinanceBillSupplierDaoImpl extends BaseDAO<FinanceBillSupplier> {
	
	public void initialize(FinanceBillSupplier financeBill){
	      this.getHibernateTemplate().initialize(financeBill.getFinanceBillItemSet());
	}

}
