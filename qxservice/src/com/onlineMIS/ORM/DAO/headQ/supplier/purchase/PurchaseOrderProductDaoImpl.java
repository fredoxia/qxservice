package com.onlineMIS.ORM.DAO.headQ.supplier.purchase;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrderProduct;

@Repository
public class PurchaseOrderProductDaoImpl extends BaseDAO<PurchaseOrderProduct> {

	public int removeItems(int orderId){
		String hql ="DELETE FROM PurchaseOrderProduct WHERE order.id = ?";
		
		Object[] value = {orderId};
		
		int rows = this.executeHQLUpdateDelete(hql, value, true);
		
		return rows;
	}


}
