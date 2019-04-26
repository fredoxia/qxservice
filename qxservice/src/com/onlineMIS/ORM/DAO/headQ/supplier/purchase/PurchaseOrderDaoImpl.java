package com.onlineMIS.ORM.DAO.headQ.supplier.purchase;

import java.util.Set;

import org.springframework.stereotype.Repository;
import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrder;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrderProduct;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrderProduct;

@Repository
public class PurchaseOrderDaoImpl extends BaseDAO<PurchaseOrder> {

	/**
	 * to initialize the sales order
	 */
	public void initialize(PurchaseOrder purchaseOrder){
		this.getHibernateTemplate().initialize(purchaseOrder.getProductSet());
	}


}
