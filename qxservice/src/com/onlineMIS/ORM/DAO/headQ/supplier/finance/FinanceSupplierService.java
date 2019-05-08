package com.onlineMIS.ORM.DAO.headQ.supplier.finance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.headQ.supplier.finance.sorter.FinanceSummaryRptVOElesSorter;
import com.onlineMIS.ORM.DAO.headQ.supplier.purchase.PurchaseOrderDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.supplier.supplierMgmt.HeadQSupplierDaoImpl;
import com.onlineMIS.ORM.entity.base.Pager;
import com.onlineMIS.ORM.entity.chainS.report.ChainFinanceReport;
import com.onlineMIS.ORM.entity.chainS.report.ChainFinanceReportItem;
import com.onlineMIS.ORM.entity.chainS.report.ChainReport;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.finance.ChainAcctFlowReportItem;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBill;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBillItem;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceCategory;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.FinanceSummaryRptVOEles;
import com.onlineMIS.ORM.entity.headQ.finance.HeadQAcctFlow;
import com.onlineMIS.ORM.entity.headQ.finance.HeadQFinanceTrace;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrder;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.FinanceBillSupplier;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.FinanceBillSupplierItem;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.FinanceCategorySupplier;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.SupplierAcctFlow;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.SupplierAcctFlowReportItem;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.SupplierFinanceTrace;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.action.headQ.finance.FinanceActionFormBean;
import com.onlineMIS.action.headQ.supplier.finance.FinanceSupplierActionFormBean;
import com.onlineMIS.action.headQ.supplier.finance.FinanceSupplierActionUIBean;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.sorter.ChainAcctFlowReportItemSort;
import com.onlineMIS.sorter.SupplierAcctFlowReportItemSort;


@Service
public class FinanceSupplierService {
    @Autowired
	private FinanceCategorySupplierImpl financeCategorySupplierImpl;
    @Autowired
    private HeadQSupplierDaoImpl headQSupplierDaoImpl;
    @Autowired
    private SupplierAcctFlowDaoImpl supplierAcctFlowDaoImpl;
    @Autowired
    private FinanceBillSupplierDaoImpl financeBillSupplierDaoImpl;
    @Autowired
    private SupplierFinanceTraceImpl financeTraceImpl;
    @Autowired
    private PurchaseOrderDaoImpl purchaseOrderDaoImpl;
    @Autowired
    private SupplierFinanceTraceImpl supplierFinanceTraceImpl;
	
	/**
	 * to prepare the bean when create the finance bill
	 * @param formBean
	 * @param uiBean
	 */
	public void prepareCreateBill(FinanceSupplierActionFormBean formBean, FinanceSupplierActionUIBean uiBean){
		FinanceBillSupplier financeBill = formBean.getOrder();
		initializeFianceBill(financeBill);

	}
	
	/**
	 * initialize the finance bill
	 */
	private void initializeFianceBill(FinanceBillSupplier financeBill){
		List<FinanceBillSupplierItem> billItems = new ArrayList<FinanceBillSupplierItem>();
		List<FinanceCategorySupplier> allCategories = null;
		try {
		allCategories = financeCategorySupplierImpl.getAll(false);
		} catch (Exception e){
			e.printStackTrace();
		}
		if (allCategories != null){
			for (int i = 0; i < allCategories.size(); i++){
				FinanceBillSupplierItem billItem = new FinanceBillSupplierItem();
				billItem.setFinanceCategorySupplier(allCategories.get(i));
				billItems.add(billItem);
			}
			financeBill.setFinanceBillItemList(billItems);
		}

		financeBill.setBillDate(new java.sql.Date(new Date().getTime()));
	}
	
	/**
	 * 获取连锁店当前欠款
	 * @param chainId
	 * @return
	 */
	public Response getSupplierCurrentFinance(int supplierId) {
		Response response = new Response();
		
		HeadQSupplier supplier = headQSupplierDaoImpl.get(supplierId, true);
		
		//1. Get the current finance
		double initialAcct = supplier.getInitialAcctBalance();
		double currentFinance = initialAcct + supplierAcctFlowDaoImpl.getAccumulateAcctFlow(supplierId);
		currentFinance = Common_util.roundDouble(currentFinance, 2);

		
		response.setReturnCode(Response.SUCCESS);
		
		Map<String, Double> financeDataMap = new HashMap<String, Double>();
		financeDataMap.put("cf", currentFinance);
		
		response.setReturnValue(financeDataMap);
		
		return response;
	}

	/**
	 * 保存到 草稿
	 * @param order
	 * @return
	 */
	public Response saveFBToDraft(FinanceBillSupplier financeBill, UserInfor user) {
		Response response = new Response();
		int billId = financeBill.getId();
		
		//Validate the bill
        validateFB(financeBill, response);
        if (!response.isSuccess())
        	return response;
        
        
		FinanceBillSupplier originalBill = financeBillSupplierDaoImpl.get(billId, true);
		
		if (originalBill == null || originalBill.getStatus() == FinanceBill.STATUS_DRAFT){
			financeBillSupplierDaoImpl.evict(originalBill);
			
			financeBill.setStatus(FinanceBill.STATUS_DRAFT);
			financeBill.setCreateDate(new Date());
			financeBill.setCreatorHq(user);
			
			financeBill.putListToSet();
			
			financeBillSupplierDaoImpl.saveOrUpdate(financeBill, true);
			
			response.setQuickValue(Response.SUCCESS, "单据成功保存草稿");
		} else {
			response.setReturnCode(Response.ERROR);
			response.setMessage("保存草稿出现错误  : 已经过账或者红冲的单据不能重复保存草稿");
		}
		
		return response;
	}
	
	/**
	 * 单据过账
	 * @param financeBill
	 * @return
	 */
	public Response postFB(FinanceBillSupplier financeBill, UserInfor user){
		Response response = new Response();
		
		//Validate the bill
        validateFB(financeBill, response);
        if (!response.isSuccess())
        	return response;
		
		int billId = financeBill.getId();
		FinanceBillSupplier originalBill = financeBillSupplierDaoImpl.get(billId, true);
		
		if (originalBill == null || originalBill.getStatus() == FinanceBill.STATUS_DRAFT){
			financeBillSupplierDaoImpl.evict(originalBill);

			//1. change the bill status
			financeBill.setStatus(FinanceBill.STATUS_COMPLETE);
			financeBill.setCreateDate(new Date());
			financeBill.setCreatorHq(user);
			financeBill.putListToSet();
			financeBillSupplierDaoImpl.saveOrUpdate(financeBill, true);
			
			//2.0 updat the finance trace
			updateFinanceTrace(financeBill, false);
			
			//3.0 update the acct flow
			updateChainAcctFlow(financeBill, false);
			
			response.setReturnCode(Response.SUCCESS);
			response.setMessage("单据成功过账");
		} else {
			response.setReturnCode(Response.ERROR);
			response.setMessage("单据过账出现错误");
		}
		
		return response;
	}
	
	/**
	 * 验证财务单据
	 * @param financeBill
	 * @param response
	 */
	private void validateFB(FinanceBillSupplier financeBill, Response response) {
		if (financeBill.getSupplier() == null || financeBill.getSupplier().getId() ==0){
			response.setFail("供应商是必填项");
			return;
		}
		for (FinanceBillSupplierItem billItem: financeBill.getFinanceBillItemList()){
			if (billItem.getTotal() < 0){
				response.setFail("金额必须是大于或等于零的数字");
				return;
			}   
		}
		
	}

	/**
	 * to update the finance trace
	 * @param bill
	 * @param isCancel
	 */
	private void updateFinanceTrace(FinanceBillSupplier bill, boolean isCancel){
		int supplierId = bill.getSupplier().getId();
		String billId = String.valueOf(bill.getId());
		int offset = isCancel ? -1 : 1;
		billId= isCancel ? "C" + billId : billId;
		
		int billType = bill.getType();
		
		//if it is 总部的付款单需要*-1
		if (billType == FinanceBillSupplier.FINANCE_PAID_HQ || billType == FinanceBillSupplier.FINANCE_DECREASE_HQ)
			offset *= -1;
		
		//1. to update the bill items
		for (FinanceBillSupplierItem billItem : bill.getFinanceBillItemSet()){
			int categoryId = billItem.getFinanceCategorySupplier().getId();
			int type = financeCategorySupplierImpl.get(categoryId, true).getType();
			double amount = billItem.getTotal();
			
			if (amount != 0){	
			    SupplierFinanceTrace financeTrace = new SupplierFinanceTrace(supplierId, type, billId, amount * offset, new java.sql.Date(bill.getCreateDate().getTime()));
			    financeTraceImpl.save(financeTrace, false);
			} 
		}

		
	}
	
	private void updateChainAcctFlow(FinanceBillSupplier bill, boolean isCancel){
		int supplierId = bill.getSupplier().getId();
		
		HeadQSupplier supplier = headQSupplierDaoImpl.get(supplierId, true);
		
		if (supplier !=  null){
			int billType = bill.getType();
			int offset = 1;
			
			//1. check the finance bill type
			if (billType == FinanceBillSupplier.FINANCE_PAID_HQ || billType == FinanceBillSupplier.FINANCE_DECREASE_HQ){
				offset *= -1;
			} else if (billType == FinanceBillSupplier.FINANCE_INCOME_HQ || billType == FinanceBillSupplier.FINANCE_INCREASE_HQ){
				offset *= 1;
			} else 
				return ;
			
			//2. check is cancel
			if (isCancel)
				offset *= -1;
			
			//3. calculate the pre-acct and postAcctAmt
    		double initialAcctAmt = supplier.getInitialAcctBalance();
    		double acctAmtFlow = supplierAcctFlowDaoImpl.getAccumulateAcctFlow(supplierId);
    		double preAcctAmt = Common_util.getDecimalDouble(initialAcctAmt + acctAmtFlow);
    		
    		//4. 设计折扣
    		double invoiceTotal = bill.getInvoiceTotal();
			if (billType == FinanceBillSupplier.FINANCE_DECREASE_HQ || billType == FinanceBillSupplier.FINANCE_PAID_HQ) {
				invoiceTotal += bill.getInvoiceDiscount();
			} else if (billType == FinanceBillSupplier.FINANCE_INCREASE_HQ || billType == FinanceBillSupplier.FINANCE_INCOME_HQ){
				invoiceTotal -= bill.getInvoiceDiscount();
			}
			double netAmt = offset * invoiceTotal;
			double postAcctAmt = Common_util.getDecimalDouble(preAcctAmt + netAmt);
    		
    		//5.update the finance bill's pre and post 
    		if (!isCancel){
    		   bill.setPreAcctAmt(preAcctAmt);
    		   bill.setPostAcctAmt(postAcctAmt);
    		   financeBillSupplierDaoImpl.update(bill, true);
    		}
    		
    		//6。更新supplier信息
    		supplier.setCurrentAcctBalance(postAcctAmt);
		    headQSupplierDaoImpl.update(supplier, true);
    		
    		//7. 更新acct folow
			SupplierAcctFlow supplierAcctFlow = new SupplierAcctFlow(supplierId, netAmt, "F," + bill.getId() + "," + isCancel, new java.sql.Date(bill.getCreateDate().getTime()));
			supplierAcctFlowDaoImpl.save(supplierAcctFlow, true);
		}
	}
	
	/**
	 * function to search the finance bills based on the search criteria
	 * @param formBean
	 * @return
	 */
	public List<FinanceBillSupplier> searchFHQBills(FinanceSupplierActionFormBean formBean) {
		boolean cached = false;
		Pager pager = formBean.getPager();

		if (pager.getTotalPage() == 0){
		    DetachedCriteria criteria = buildSearchFHQBills(formBean);
			criteria.setProjection(Projections.rowCount());
			int totalRecord = Common_util.getProjectionSingleValue(financeBillSupplierDaoImpl.getByCriteriaProjection(criteria, false));
			pager.initialize(totalRecord);
		} else {
			pager.calFirstResult();
			cached = true;
		}
		
		DetachedCriteria criteria2 = buildSearchFHQBills(formBean);
		criteria2.addOrder(Order.desc("createDate"));
		
		return financeBillSupplierDaoImpl.getByCritera(criteria2, pager.getFirstResult(), pager.getRecordPerPage(), cached);
	}
	
	private DetachedCriteria buildSearchFHQBills(FinanceSupplierActionFormBean formBean){
        DetachedCriteria criteria = DetachedCriteria.forClass(FinanceBillSupplier.class);
        FinanceBillSupplier financeBill = formBean.getOrder();
		
		//1. get the status
		int status = financeBill.getStatus();
		if (status != Common_util.ALL_RECORD)
			criteria.add(Restrictions.eq("status", status));
		else 
			criteria.add(Restrictions.ne("status", FinanceBill.STATUS_DELETED));
		
		//2. get the category
		int type = financeBill.getType();
		if (type != Common_util.ALL_RECORD)
			criteria.add(Restrictions.eq("type", type));
		
		
		//2. get the chain
		int supplierId = financeBill.getSupplier().getId();
		if (supplierId != Common_util.ALL_RECORD_NEW)
			criteria.add(Restrictions.eq("supplier.id", supplierId));
		
		//4. set the date
		Date startDate = Common_util.formStartDate(formBean.getSearchStartTime());
		Date endDate = Common_util.formEndDate(formBean.getSearchEndTime());
		criteria.add(Restrictions.between("createDate", startDate, endDate));
		
		return criteria;
	}

	/**
	 * 根据financeBill id获取financeBill 
	 * @param id
	 * @return
	 */
	@Transactional
	public Response getFinanceBillById(int id) {
		Response response = new Response();
		
		try {
		
			FinanceBillSupplier financeBill = financeBillSupplierDaoImpl.get(id, true);
			financeBillSupplierDaoImpl.initialize(financeBill);
			financeBill.putSetToList();
			
			response.setReturnValue(financeBill);
		} catch (Exception e){
			response.setFail("获取单据错误 : " + e.getMessage());
		}
		
		return response;
	}

	public void prepareEditBill(FinanceSupplierActionFormBean formBean,
			HeadQSupplier supplier) {
		formBean.getOrder().setSupplier(supplier);
		
	}
	
	@Transactional
	public Response cancelFB(FinanceBillSupplier order, UserInfor user){
		Response response = new Response();
		int billId = order.getId();
		FinanceBillSupplier originalBill = financeBillSupplierDaoImpl.get(billId, true);
		
		if (originalBill == null || originalBill.getStatus() != FinanceBill.STATUS_COMPLETE){
			response.setReturnCode(Response.ERROR);
			response.setMessage("单据不存在或者尚未过账无法红冲");
		} else {
			//1.0 change the bill status 
			originalBill.setStatus(FinanceBill.STATUS_CANCEL);
			originalBill.setCreatorHq(user);
			
			//originalBill.setCreateDate(new Date());	
			financeBillSupplierDaoImpl.update(originalBill, true);

			//2.0 updat the finance trace
			updateFinanceTrace(originalBill, true);

			//3.0 update the acct flow
			updateChainAcctFlow(originalBill, true);

			response.setReturnCode(Response.SUCCESS);
		}
		
		return response;
	}

	public Response deleteFB(FinanceBillSupplier order, UserInfor user) {
		Response response = new Response();
		int billId = order.getId();
		FinanceBillSupplier originalBill = financeBillSupplierDaoImpl.get(billId, true);
		
		if (originalBill != null && originalBill.getStatus() == FinanceBill.STATUS_DRAFT){
			originalBill.setStatus(FinanceBill.STATUS_DELETED);
			originalBill.setCreateDate(new Date());
			originalBill.setCreatorHq(user);
		
			financeBillSupplierDaoImpl.saveOrUpdate(originalBill, true);
		    
		    response.setReturnCode(Response.SUCCESS);
		} else {
			response.setReturnCode(Response.ERROR);
			response.setMessage("删除单据出错");
		}
		
		return response;
	}

	public Response searchAcctFlow(Date startDate, Date endDate, int supplierId) {
		Response response = new Response();
		List<SupplierAcctFlowReportItem> rptItems = new ArrayList<SupplierAcctFlowReportItem>();
		
		if (supplierId != Common_util.ALL_RECORD && supplierId != Common_util.ALL_RECORD_NEW){
			/**
			 * 1. 搜索所有采购单据
			 */
			List<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();
			DetachedCriteria criteria = DetachedCriteria.forClass(PurchaseOrder.class,"order");
			criteria.add(Restrictions.eq("order.status", PurchaseOrder.STATUS_COMPLETE));
			
			criteria.add(Restrictions.eq("order.supplier.id", supplierId));
	
			startDate  = Common_util.formStartDate(startDate);
			endDate = Common_util.formEndDate(endDate);
			criteria.add(Restrictions.between("order.lastUpdateTime",startDate,endDate));
			//criteria.addOrder(Order.asc("order.order_StartTime"));
			purchaseOrders = purchaseOrderDaoImpl.getByCritera(criteria, true);
	
			/**
			 * 2. 搜索所有财物单据
			 */
	        DetachedCriteria criteria2 = DetachedCriteria.forClass(FinanceBillSupplier.class);
			criteria2.add(Restrictions.eq("status", FinanceBill.STATUS_COMPLETE));
			criteria2.add(Restrictions.eq("supplier.id", supplierId));

			criteria2.add(Restrictions.between("createDate", startDate, endDate));
			List<FinanceBillSupplier> financeBills = financeBillSupplierDaoImpl.getByCritera(criteria2, true);
			
	
			constructSupplierAcctFlowRptItems(purchaseOrders, financeBills, rptItems, supplierId, startDate);
		}
		
		Map data = new HashMap<String, List>();
		data.put("rows", rptItems);
		response.setReturnValue(data);
		response.setReturnCode(Response.SUCCESS);
		
		return response;
	}

	private void constructSupplierAcctFlowRptItems(List<PurchaseOrder> purchaseOrders,
			List<FinanceBillSupplier> financeBills, List<SupplierAcctFlowReportItem> rptItems, int supplierId,
			Date startDate) {
		//1. 货品采购单
		if (purchaseOrders != null && purchaseOrders.size() >0){
			for (PurchaseOrder order : purchaseOrders){
				HeadQSupplier supplier = order.getSupplier();
				String orderType = order.getTypeS();
				
				String acctFlowType ="";
				if (order.getType() == PurchaseOrder.TYPE_PURCHASE)
					acctFlowType = SupplierAcctFlowReportItem.ACCT_FLOW_TYPE_INCREASE;
				else if (order.getType() == PurchaseOrder.TYPE_RETURN)
					acctFlowType = SupplierAcctFlowReportItem.ACCT_FLOW_TYPE_DECREASE;
				
				SupplierAcctFlowReportItem acctFlowItem = new SupplierAcctFlowReportItem(supplier, order.getLastUpdateTime(), orderType, SupplierAcctFlowReportItem.ITEM_TYPE_PURCHASE,acctFlowType, order.getTotalQuantity(), order.getTotalRecCost(),order.getTotalRecCost() - order.getTotalDiscount(), order.getId(),order.getComment(), 0,0);
				rptItems.add(acctFlowItem);
			}
		}
		
		//2. 财务单据
		if (financeBills != null && financeBills.size() > 0){
			for (FinanceBillSupplier bill : financeBills){
				HeadQSupplier supplier = bill.getSupplier();
				String billTypeName = bill.getTypeHQS();
				
				int billType = bill.getType();
				String acctFlowType = "";
				double invoiceTotal = bill.getInvoiceTotal();
				double amtFlow = invoiceTotal;
				if (billType == FinanceBill.FINANCE_DECREASE_HQ || billType == FinanceBill.FINANCE_PAID_HQ) {
					acctFlowType = SupplierAcctFlowReportItem.ACCT_FLOW_TYPE_DECREASE;
					amtFlow += bill.getInvoiceDiscount();
				} else if (billType == FinanceBill.FINANCE_INCREASE_HQ || billType == FinanceBill.FINANCE_INCOME_HQ){
					acctFlowType = SupplierAcctFlowReportItem.ACCT_FLOW_TYPE_INCREASE;
					amtFlow -= bill.getInvoiceDiscount();
				}
				
				SupplierAcctFlowReportItem acctFlowItem = new SupplierAcctFlowReportItem(supplier, bill.getCreateDate(), billTypeName, SupplierAcctFlowReportItem.ITEM_TYPE_FINANCE, acctFlowType, 0, invoiceTotal, amtFlow, bill.getId(), bill.getComment(), 0,0);
				rptItems.add(acctFlowItem);
			}
		}
		
		//3. 排序一下
		Collections.sort(rptItems, new SupplierAcctFlowReportItemSort());
		
		//4. 获取前面的累计欠款
		Date firstDate = null;
		double acctBefore  = 0;
		if (rptItems.size() > 0){
			firstDate = rptItems.get(0).getDate();
		} else 
			firstDate = startDate;
			
		
		if (firstDate != null)
		    acctBefore = supplierAcctFlowDaoImpl.getAccumulateAcctFlowBefore(supplierId, firstDate);
		
		//5. 准备数据
		HeadQSupplier cust = headQSupplierDaoImpl.get(supplierId, true);
		if (cust != null)
		     acctBefore += cust.getInitialAcctBalance();
		
		double acctAfter = acctBefore;
		for (SupplierAcctFlowReportItem rptItem: rptItems){
			double thisAmt = rptItem.getAmtFlow();
			String acctFlowType = rptItem.getAcctFlowType();
			
			if (acctFlowType.equalsIgnoreCase(ChainAcctFlowReportItem.ACCT_FLOW_TYPE_INCREASE)){
				acctAfter += thisAmt;
				rptItem.setAcctIncrease(thisAmt);
				rptItem.setPostAcct(acctAfter);
			} else if (acctFlowType.equalsIgnoreCase(ChainAcctFlowReportItem.ACCT_FLOW_TYPE_DECREASE)){
				acctAfter = acctAfter - thisAmt;
				rptItem.setAcctDecrease(thisAmt);
				rptItem.setPostAcct(acctAfter);
			} else {
				rptItem.setPostAcct(acctAfter);
			}

		}
		
		//6.0 设置首
		SupplierAcctFlowReportItem firstItem = new SupplierAcctFlowReportItem();
		firstItem.setDate(startDate);
		firstItem.setPostAcct(acctBefore);
		rptItems.add(0, firstItem);
		
	}

	/**
	 * 
	 * @param id
	 * @param searchStartTime
	 * @param searchEndTime
	 * @return
	 */
	@Transactional
	public Response generateFinanceReport(int supplierId, java.sql.Date startDate, java.sql.Date endDate) {
		Response response = new Response();
	    List<FinanceSummaryRptVOEles> footers = new ArrayList<FinanceSummaryRptVOEles>();
	    
        //1. 查询数据
	    DetachedCriteria criteria = DetachedCriteria.forClass(FinanceBillSupplier.class);
	    criteria.add(Restrictions.eq("status", FinanceBillSupplier.STATUS_COMPLETE));
	
		if (supplierId != Common_util.ALL_RECORD_NEW)
		   criteria.add(Restrictions.eq("supplier.id", supplierId));

		Date startDate1 = Common_util.formStartDate(new Date(startDate.getTime()));
		Date endDate1 = Common_util.formEndDate(new Date(endDate.getTime()));
		criteria.add(Restrictions.between("billDate", startDate1, endDate1));
		
		List<FinanceBillSupplier> billSuppliers = financeBillSupplierDaoImpl.getByCritera(criteria, true);
		//2. 计算单据
		Map<Integer, FinanceCategorySupplier> categoryIdMapToItem = financeCategorySupplierImpl.getFinanceCategoryMapWithIDKey();
		Map<Integer, FinanceSummaryRptVOEles> rptEles = new HashMap<Integer, FinanceSummaryRptVOEles>();
		for (FinanceBillSupplier bill : billSuppliers){
			supplierId = bill.getSupplier().getId();
			FinanceSummaryRptVOEles rptVOEles = rptEles.get(supplierId);
			
			if (rptVOEles == null){
				rptVOEles = new FinanceSummaryRptVOEles();
				rptVOEles.setSupplier(bill.getSupplier().getName());
			}
			rptVOEles.add(bill, categoryIdMapToItem);
			
			rptEles.put(supplierId, rptVOEles);
		}
		List<FinanceSummaryRptVOEles> financeSummaryRptVOEles =new ArrayList<FinanceSummaryRptVOEles>(rptEles.values());
		Collections.sort(financeSummaryRptVOEles, new FinanceSummaryRptVOElesSorter());
		
		//3.汇总和排序
		FinanceSummaryRptVOEles summaryRptVOEles = new FinanceSummaryRptVOEles();
		summaryRptVOEles.setSupplier("合计");
		for (FinanceSummaryRptVOEles eles : financeSummaryRptVOEles){
			summaryRptVOEles.calculateSum(eles);
		}
		footers.add(summaryRptVOEles);

		Map data = new HashMap<String, List>();
		data.put("rows", financeSummaryRptVOEles);
		data.put("footer", footers);

		response.setReturnValue(data);
		response.setReturnCode(Response.SUCCESS);
		
		
		return response;
	}
}
