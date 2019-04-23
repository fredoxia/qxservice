package com.onlineMIS.ORM.DAO.headQ.supplier.supplierMgmt;


import org.springframework.stereotype.Repository;

import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;

@Repository
public class HeadQSupplierDaoImpl extends BaseDAO<HeadQSupplier>{

	/**
	 * 生成一个所有供应商的对象	
	 * @return
	 */
	public HeadQSupplier getAllSupplierObj() {
		HeadQSupplier supplier = new HeadQSupplier();
		supplier.setId(0);
		supplier.setName("所有供应商");
		return supplier;
	}

}
