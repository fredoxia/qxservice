package com.onlineMIS.ORM.DAO.headQ.supplier.supplierMgmt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;





import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;
import com.onlineMIS.common.Common_util;

@Service
public class HeadQSupplierService {
	@Autowired
	private HeadQSupplierDaoImpl headQSupplierDaoImpl;
	
	/**
	 * 获取当前页所有客户的信息
	 * @param uiBean
	 * @param basicData
	 */
	public Response listSupplier(HeadQSupplier supplier,Integer page, Integer rowPerPage, String sort, String sortOrder) {
		Response response = new Response();
		Map dataMap = new HashMap();
		
		//获取总条数的 criteria
		DetachedCriteria custTotalCriteria = DetachedCriteria.forClass(HeadQSupplier.class);
		//获取list的 criteria
		DetachedCriteria custCriteria = DetachedCriteria.forClass(HeadQSupplier.class);
		
		if (supplier != null){
			String custName = StringUtils.trim(supplier.getName());
			if (StringUtils.isNotEmpty(custName)){
				custTotalCriteria.add(Restrictions.like("name", custName, MatchMode.ANYWHERE));
				custCriteria.add(Restrictions.like("name", custName, MatchMode.ANYWHERE));
			}
		}

		//1. 获取总条数
		custTotalCriteria.setProjection(Projections.rowCount());
		int total = Common_util.getProjectionSingleValue(headQSupplierDaoImpl.getByCriteriaProjection(custTotalCriteria, true));
		
		//2. 获取当页数据
		custCriteria.addOrder(Order.asc("status"));
		custCriteria.addOrder(Order.asc("pinyin"));
		List<HeadQSupplier> suppliers = headQSupplierDaoImpl.getByCritera(custCriteria, Common_util.getFirstRecord(page, rowPerPage), rowPerPage, true);
		
		dataMap.put("rows", suppliers);
		dataMap.put("total", total);

		response.setReturnValue(dataMap);
		response.setReturnCode(Response.SUCCESS);
		return response;
	}
	
	/**
	 * 在单据上搜索供应商信息，需要自动过滤status 不正常客户
	 * @param cust
	 * @return
	 */
	public Response searchSuppliers(HeadQSupplier supplier){
		Response response = new Response();

		//获取list的 criteria
		DetachedCriteria custCriteria = DetachedCriteria.forClass(HeadQSupplier.class);
		
		custCriteria.add(Restrictions.eq("status", HeadQSupplier.CustStatusEnum.GOOD.getKey()));
		
		if (supplier != null){
			String pinyin = StringUtils.trim(supplier.getPinyin());
			if (StringUtils.isNotEmpty(pinyin)){
				custCriteria.add(Restrictions.like("pinyin", pinyin, MatchMode.ANYWHERE));
			}
		}

		//2. 获取当页数据
		custCriteria.addOrder(Order.asc("status"));
		custCriteria.addOrder(Order.asc("pinyin"));
		List<HeadQSupplier> custs = headQSupplierDaoImpl.getByCritera(custCriteria, true);


		response.setReturnValue(custs);
		response.setReturnCode(Response.SUCCESS);
		return response;
	}
	
	/**
	 * 更新或者创建的客户信息
	 * @param headQCust
	 * @return
	 */
	public Response createOrUpdateSupplier(HeadQSupplier supplier){
		Response response = new Response();
		
		try {
			//创建新的
			if (supplier.getId() == 0){
				headQSupplierDaoImpl.save(supplier, true);
				response.setSuccess("成功创建新的客户");
			} else {
				//更新旧的客户信息
				HeadQSupplier headQCust2 = headQSupplierDaoImpl.get(supplier.getId(), true);
				if (headQCust2 == null){
					response.setFail("查找原客户信息失败 : 查找Id " + supplier.getId());
				} else {
					headQCust2.copyInforFromOther(supplier);
					headQCust2.setPinyin(Common_util.getPinyinCode(headQCust2.getName(), false));
					headQSupplierDaoImpl.saveOrUpdate(headQCust2, true);
					response.setSuccess("成功更新客户信息");
				}
			}
		} catch (Exception e){
			response.setFail("更新客户信息失败 : " + e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 冻结客户信息
	 * @param custId
	 * @return
	 */
	public Response disableSupplier(int supplierId){
		Response response = new Response();
		String hql = "UPDATE HeadQSupplier SET status = ? WHERE id = ?";
		try {
		    Object[] values = {HeadQCust.CustStatusEnum.DELETED.getKey(), supplierId};
		
		    int record = headQSupplierDaoImpl.executeHQLUpdateDelete(hql, values, true);
		    if (record == 1)
		    	response.setSuccess("成功冻结客户数据");
		    else 
		    	response.setSuccess("冻结客户数据失败");
		} catch (Exception e){
			response.setFail("冻结客户信息失败 : " + e.getMessage());
		}
		
		return response;
	}
	
	/**
	 * 准备修改或者添加某个客户时
	 * @param cust
	 */
	public Response preEditSupplier(HeadQSupplier supplier) {
		Response response = new Response();
		
		//当前是添加新客户
		if (supplier == null || supplier.getId() == 0){
			return response;
		} else {
			int id = supplier.getId();
			HeadQSupplier cust2 = headQSupplierDaoImpl.get(id, true);
			if (cust2 != null){
				response.setReturnValue(cust2);
				return response;
			} else {
				response.setFail("无法获取客户信息 : " + id);
				return response;
			}	
		}
		
	}

}
