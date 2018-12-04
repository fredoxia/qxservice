package com.onlineMIS.ORM.DAO.headQ.custMgmt;

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
import com.onlineMIS.common.Common_util;

@Service
public class HeadQSalesService {
	@Autowired
	private HeadQCustDaoImpl headQCustDaoImpl;
	
	/**
	 * 获取当前页所有客户的信息
	 * @param uiBean
	 * @param basicData
	 */
	public Response listCust(HeadQCust cust,Integer page, Integer rowPerPage, String sort, String sortOrder) {
		Response response = new Response();
		Map dataMap = new HashMap();
		
		//获取总条数的 criteria
		DetachedCriteria custTotalCriteria = DetachedCriteria.forClass(HeadQCust.class);
		//获取list的 criteria
		DetachedCriteria custCriteria = DetachedCriteria.forClass(HeadQCust.class);
		
		if (cust != null){
			String custName = StringUtils.trim(cust.getName());
			if (StringUtils.isNotEmpty(custName)){
				custTotalCriteria.add(Restrictions.like("name", custName, MatchMode.ANYWHERE));
				custCriteria.add(Restrictions.like("name", custName, MatchMode.ANYWHERE));
			}
		}

		//1. 获取总条数
		custTotalCriteria.setProjection(Projections.rowCount());
		int total = Common_util.getProjectionSingleValue(headQCustDaoImpl.getByCriteriaProjection(custTotalCriteria, true));
		
		//2. 获取当页数据
		custCriteria.addOrder(Order.asc("status"));
		custCriteria.addOrder(Order.asc("pinyin"));
		List<HeadQCust> custs = headQCustDaoImpl.getByCritera(custCriteria, Common_util.getFirstRecord(page, rowPerPage), rowPerPage, true);
		
		dataMap.put("rows", custs);
		dataMap.put("total", total);

		response.setReturnValue(dataMap);
		response.setReturnCode(Response.SUCCESS);
		return response;
	}
	
	/**
	 * 搜索客户信息不分页
	 * @param cust
	 * @return
	 */
	public Response searchCust(HeadQCust cust){
		Response response = new Response();

		//获取list的 criteria
		DetachedCriteria custCriteria = DetachedCriteria.forClass(HeadQCust.class);
		
		if (cust != null){
			String pinyin = StringUtils.trim(cust.getPinyin());
			if (StringUtils.isNotEmpty(pinyin)){
				custCriteria.add(Restrictions.like("pinyin", pinyin, MatchMode.ANYWHERE));
			}
			
			String custName = StringUtils.trim(cust.getName());
			if (StringUtils.isNotEmpty(custName)){
				custCriteria.add(Restrictions.like("name", custName, MatchMode.ANYWHERE));
			}
		}

		//2. 获取当页数据
		custCriteria.addOrder(Order.asc("status"));
		custCriteria.addOrder(Order.asc("pinyin"));
		List<HeadQCust> custs = headQCustDaoImpl.getByCritera(custCriteria, true);


		response.setReturnValue(custs);
		response.setReturnCode(Response.SUCCESS);
		return response;
	}
	
	/**
	 * 更新或者创建的客户信息
	 * @param headQCust
	 * @return
	 */
	public Response createOrUpdateCust(HeadQCust headQCust){
		Response response = new Response();
		
		try {
			//创建新的
			if (headQCust.getId() == 0){
				headQCustDaoImpl.save(headQCust, true);
				response.setSuccess("成功创建新的客户");
			} else {
				//更新旧的客户信息
				HeadQCust headQCust2 = headQCustDaoImpl.get(headQCust.getId(), true);
				if (headQCust2 == null){
					response.setFail("查找原客户信息失败 : 查找Id " + headQCust.getId());
				} else {
					headQCust2.copyInforFromOther(headQCust);
					headQCust2.setPinyin(Common_util.getPinyinCode(headQCust2.getName(), false));
					headQCustDaoImpl.saveOrUpdate(headQCust2, true);
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
	public Response disableCust(int custId){
		Response response = new Response();
		String hql = "UPDATE HeadQCust SET status = ? WHERE id = ?";
		try {
		    Object[] values = {HeadQCust.CustStatusEnum.DELETED.getKey(), custId};
		
		    int record = headQCustDaoImpl.executeHQLUpdateDelete(hql, values, true);
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
	public Response preEditCust(HeadQCust cust) {
		Response response = new Response();
		
		//当前是添加新客户
		if (cust == null || cust.getId() == 0){
			return response;
		} else {
			int id = cust.getId();
			HeadQCust cust2 = headQCustDaoImpl.get(id, true);
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
