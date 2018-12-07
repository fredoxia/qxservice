package com.onlineMIS.ORM.DAO.headQ.supplier.purchase;

import org.springframework.stereotype.Repository;
import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;

@Repository
public class PurchaseOrderDaoImpl extends BaseDAO<PurchaseOrder> {

	/**
	 * to initialize the sales order
	 */
	public void initialize(PurchaseOrder purchaseOrder){
		this.getHibernateTemplate().initialize(purchaseOrder.getProductSet());
	}

}
