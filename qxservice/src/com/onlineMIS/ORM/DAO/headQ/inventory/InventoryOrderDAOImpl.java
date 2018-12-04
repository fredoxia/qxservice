package com.onlineMIS.ORM.DAO.headQ.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrder;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrderProduct;
import com.onlineMIS.common.loggerLocal;

@Repository
public class InventoryOrderDAOImpl extends BaseDAO<InventoryOrder> {

    
	@SuppressWarnings("unchecked")
	public List<InventoryOrder> search(DetachedCriteria criteria) {
		List<InventoryOrder> orderList= new ArrayList<InventoryOrder>();
		
	    orderList = getByCritera(criteria,false);

		return orderList;
	}

	public InventoryOrder retrieveOrder(int order_ID) {
        InventoryOrder order =null;

        order = get(order_ID, false);
        
        if (order != null)
            initialize(order);

		return order;
	}
	
	/**
	 * to calculate the statics of the quantity of the products bought before for the customer
	 * @param barcode
	 * @param customerid
	 * @return
	 */
	public int getQuantityBefore(final String barcode,final int client_id){
    	String HQL = "select sum(op.quantity) from InventoryOrderProduct op where op.productBarcode.barcode=? and op.order.cust.id =? and op.order.order_Status = ?";
    	Object[] values = {barcode, client_id, InventoryOrder.STATUS_ACCOUNT_COMPLETE};

    	List<Object> objects = this.executeHQLSelect(HQL, values, null, true);

                Object quantityObj = objects.get(0);
                if (quantityObj == null)
                	return 0;
                else {
                	return ((Long)quantityObj).intValue();
                }

	}
	
	/**
	 * to initialize the order
	 * @param order
	 */
	public void initialize(InventoryOrder order){
		this.getHibernateTemplate().initialize(order.getProduct_Set());
	}

	/**
	 * to copy the inventory order to another one
	 * @param orderInDB
	 * @return
	 */
	public InventoryOrder copy(InventoryOrder orderInDB) {
		evict(orderInDB);
		
		InventoryOrder order = orderInDB;
		
		order.setOrder_ID(0);
		
		Set<InventoryOrderProduct> orderProducts = order.getProduct_Set();
		for (InventoryOrderProduct orderProduct: orderProducts){
			orderProduct.setID(0);
		}
		
		return order;
	}

}
