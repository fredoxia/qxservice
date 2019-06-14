package com.onlineMIS.ORM.DAO.headQ.report;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.chainS.user.ChainStoreDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.BrandDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.ProductBarcodeDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.QuarterDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.YearDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.custMgmt.HeadQCustDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.finance.FinanceBillImpl;
import com.onlineMIS.ORM.DAO.headQ.finance.FinanceService;
import com.onlineMIS.ORM.DAO.headQ.finance.HeadQAcctFlowDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.inventory.InventoryOrderDAOImpl;
import com.onlineMIS.ORM.DAO.headQ.inventory.InventoryOrderProductDAOImpl;
import com.onlineMIS.ORM.DAO.headQ.supplier.finance.FinanceSupplierService;
import com.onlineMIS.ORM.DAO.headQ.supplier.purchase.PurchaseOrderProductDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.supplier.purchase.SupplierPurchaseService;
import com.onlineMIS.ORM.DAO.headQ.supplier.supplierMgmt.HeadQSupplierDaoImpl;
import com.onlineMIS.ORM.DAO.shared.expense.ExpenseDaoImpl;
import com.onlineMIS.ORM.DAO.shared.expense.ExpenseTypeDaoImpl;
import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.chainS.user.ChainUserInfor;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Brand;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Category;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Color;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Product;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Quarter;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Year;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.finance.ChainAcctFlowReportItem;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBill;
import com.onlineMIS.ORM.entity.headQ.finance.HeadQAcctFlow;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrder;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrderProduct;
import com.onlineMIS.ORM.entity.headQ.report.HeadQCustAcctFlowReportItem;
import com.onlineMIS.ORM.entity.headQ.report.HeadQCustAcctFlowReportTemplate;
import com.onlineMIS.ORM.entity.headQ.report.HeadQCustAcctFlowTemplate;
import com.onlineMIS.ORM.entity.headQ.report.HeadQCustInforTemplate;
import com.onlineMIS.ORM.entity.headQ.report.HeadQExpenseReportTemplate;
import com.onlineMIS.ORM.entity.headQ.report.HeadQExpenseRptElesVO;
import com.onlineMIS.ORM.entity.headQ.report.HeadQPurchaseStatisticReportItem;
import com.onlineMIS.ORM.entity.headQ.report.HeadQPurchaseStatisticReportItemVO;
import com.onlineMIS.ORM.entity.headQ.report.HeadQPurchaseStatisticsReportTemplate;
import com.onlineMIS.ORM.entity.headQ.report.HeadQReportItemVO;
import com.onlineMIS.ORM.entity.headQ.report.HeadQSalesStatisticReportItem;
import com.onlineMIS.ORM.entity.headQ.report.HeadQSalesStatisticReportItemVO;
import com.onlineMIS.ORM.entity.headQ.report.HeadQSalesStatisticsReportTemplate;
import com.onlineMIS.ORM.entity.headQ.report.HeadQSupplierAcctFlowTemplate;
import com.onlineMIS.ORM.entity.headQ.report.HeadQSupplierInforTemplate;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.SupplierAcctFlowReportItem;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.ORM.entity.shared.expense.Expense;
import com.onlineMIS.ORM.entity.shared.expense.ExpenseType;
import com.onlineMIS.action.headQ.report.HeadQReportFormBean;
import com.onlineMIS.action.headQ.report.HeadQReportUIBean;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.ExcelUtil;
import com.onlineMIS.filter.SystemParm;
import com.onlineMIS.sorter.ChainStatisticReportItemVOSorter;
import com.onlineMIS.sorter.HeadQExpenseReportSort;
import com.onlineMIS.sorter.HeadQStatisticReportItemSorter;
import com.onlineMIS.sorter.HeadQStatisticReportItemVOSorter;

@Service
public class HeadQReportService {

	@Autowired
	private HeadQSupplierDaoImpl headQSupplierDaoImpl;
	
	@Autowired
	private PurchaseOrderProductDaoImpl purchaseOrderProductDaoImpl;
	
	
	@Autowired
	private YearDaoImpl yearDaoImpl;
	
	@Autowired
	private ChainStoreDaoImpl chainStoreDaoImpl;
	
	@Autowired
	private HeadQCustDaoImpl headQCustDaoImpl;
	
	@Autowired
	private QuarterDaoImpl quarterDaoImpl;
	
	@Autowired
	private BrandDaoImpl brandDaoImpl;
	
	@Autowired
	private ProductBarcodeDaoImpl productBarcodeDaoImpl;
	
	@Autowired
	private InventoryOrderProductDAOImpl inventoryOrderProductDAOImpl;
	
	@Autowired
	private InventoryOrderDAOImpl inventoryOrderDAOImpl;
	
	@Autowired
	private FinanceSupplierService financeSupplierService;
	
	@Autowired
	private FinanceService financeService;
	
	@Autowired
	private HeadQAcctFlowDaoImpl headQAcctFlowDaoImpl;
	
	@Autowired
	private FinanceBillImpl financeBillImpl;
	
	@Autowired
	private ExpenseDaoImpl expenseDaoImpl;
	
	@Autowired
	private ExpenseTypeDaoImpl expenseTypeDaoImpl;
	/**
	 * 获取总部的采购报表数据
	 * @param parentId
	 * @param startDate
	 * @param endDate
	 * @param id
	 * @param year_ID
	 * @param quarter_ID
	 * @param brand_ID
	 * @return
	 */
	@Transactional
	public Response getPurchaseStatisticReptEles(int parentId, Date startDate,
			Date endDate, int supplierId, int yearId, int quarterId, int brandId) {
		Response response = new Response();
		List<HeadQPurchaseStatisticReportItemVO> reportItems = new ArrayList<HeadQPurchaseStatisticReportItemVO>();
		String name = "";
		
		if (startDate == null){
			response.setReturnValue(reportItems);
			return response;
		}
		
		List<Object> value_sale = new ArrayList<Object>();
		value_sale.add(PurchaseOrder.STATUS_COMPLETE);
		value_sale.add(Common_util.formStartDate(startDate));
		value_sale.add(Common_util.formEndDate(endDate));


		
		String whereClause = "";
		HeadQSupplier supplier = headQSupplierDaoImpl.getAllSupplierObj();
		if (supplierId != Common_util.ALL_RECORD_NEW){
			supplier = headQSupplierDaoImpl.get(supplierId, true);
			whereClause = " AND p.order.supplier.id = " + supplier.getId();
		}

		if (parentId == 0){
			//@2. 根节点
			StringBuffer sql = new StringBuffer("SELECT SUM(quantity), SUM(recCost * quantity), p.order.type FROM PurchaseOrderProduct p " + 
			         " WHERE p.order.status = ? AND p.order.creationTime BETWEEN ? AND ?");

			sql.append(whereClause);
			sql.append(" GROUP BY p.order.type");
		
			List<Object> values = purchaseOrderProductDaoImpl.executeHQLSelect(sql.toString(), value_sale.toArray(), null, true);

			name = supplier.getName();
			HeadQPurchaseStatisticReportItemVO rootItem = new HeadQPurchaseStatisticReportItemVO(name, 1, supplierId, yearId, quarterId, brandId,0, HeadQReportItemVO.STATE_CLOSED);
			
			if (values != null){
				for (Object record : values ){
					Object[] records = (Object[])record;
					int quantity = Common_util.getInt(records[0]);
					double cost = Common_util.getDouble(records[1]);
					//double wholeSales = Common_util.getDouble(records[2]);
					int type = Common_util.getInt(records[2]);
					
					rootItem.putValue(type, quantity, cost);
				}
				
				rootItem.reCalculate();
			}
			
			reportItems.add(rootItem);
		} else if (yearId == 0){
			//@2. 展开所有年份的库存信息
			StringBuffer sql = new StringBuffer("SELECT SUM(quantity), SUM(recCost * quantity), p.pb.product.year.year_ID, p.order.type   FROM PurchaseOrderProduct p " + 
			         " WHERE p.order.status = ? AND p.order.creationTime BETWEEN ? AND ?");

			sql.append(whereClause);
			sql.append(" GROUP BY p.pb.product.year.year_ID, p.order.type");
			
			List<Object> values = purchaseOrderProductDaoImpl.executeHQLSelect(sql.toString(), value_sale.toArray(), null, true);
			
			Map<Integer, HeadQPurchaseStatisticReportItemVO> dataMap = new HashMap<Integer, HeadQPurchaseStatisticReportItemVO>();
			for (Object record : values ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				double cost = Common_util.getDouble(records[1]);
				int yearIdDB = Common_util.getInt(records[2]);
				int type = Common_util.getInt(records[3]);
				
				HeadQPurchaseStatisticReportItemVO levelOneItem = dataMap.get(yearIdDB);
				if (levelOneItem != null){
					levelOneItem.putValue(type, quantity, cost);
				} else {
					Year year = yearDaoImpl.get(yearIdDB, true);
					name = year.getYear() + "年";
					
					levelOneItem = new HeadQPurchaseStatisticReportItemVO(name, parentId, supplierId, yearIdDB, quarterId, brandId,0, HeadQReportItemVO.STATE_CLOSED);
					levelOneItem.putValue(type, quantity, cost);
					
					dataMap.put(yearIdDB, levelOneItem);
				}
			}
			
			List<Integer> yearKey = new ArrayList<Integer>(dataMap.keySet());
			
			//1. 把基本对象放入
			//2. 计算总数
			for (Integer id : yearKey){
				HeadQPurchaseStatisticReportItemVO levelOneItem = dataMap.get(id);
				levelOneItem.reCalculate();

				reportItems.add(levelOneItem);
			}		
			
		} else if (quarterId == 0){
			//@2. 展开所有季的库存信息
			StringBuffer sql = new StringBuffer("SELECT SUM(quantity), SUM(recCost * quantity), p.pb.product.quarter.quarter_ID, p.order.type  FROM PurchaseOrderProduct p " + 
			         " WHERE p.order.status = ? AND p.order.creationTime BETWEEN ? AND ? AND p.pb.product.year.year_ID =" + yearId);

			sql.append(whereClause);
			sql.append(" GROUP BY p.pb.product.quarter.quarter_ID, p.order.type");
			
			List<Object> values = purchaseOrderProductDaoImpl.executeHQLSelect(sql.toString(), value_sale.toArray(), null, true);
			
			Map<Integer, HeadQPurchaseStatisticReportItemVO> dataMap = new HashMap<Integer, HeadQPurchaseStatisticReportItemVO>();
			for (Object record : values ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				double amount = Common_util.getDouble(records[1]);
				int quarterIdDB = Common_util.getInt(records[2]);
				int type = Common_util.getInt(records[3]);
				
				HeadQPurchaseStatisticReportItemVO levelOneItem = dataMap.get(quarterIdDB);
				if (levelOneItem != null){
					levelOneItem.putValue(type, quantity, amount);
				} else {
					Year year = yearDaoImpl.get(yearId, true);
					Quarter quarter = quarterDaoImpl.get(quarterIdDB, true);
					name = year.getYear() + "年 " + quarter.getQuarter_Name();
					
					levelOneItem = new HeadQPurchaseStatisticReportItemVO(name, parentId, supplierId, yearId, quarterIdDB, brandId,0, HeadQReportItemVO.STATE_CLOSED);
					levelOneItem.putValue(type, quantity, amount);
					
					dataMap.put(quarterIdDB, levelOneItem);
				}
			}
			
			List<Integer> quarterKey = new ArrayList<Integer>(dataMap.keySet());
			
			//1. 把基本对象放入
			//2. 计算总数
			for (Integer id : quarterKey){
				HeadQPurchaseStatisticReportItemVO levelOneItem = dataMap.get(id);
				levelOneItem.reCalculate();

				reportItems.add(levelOneItem);
			}		
		} else if (brandId == 0){
			//@2. 展开所有品牌的库存信息
			StringBuffer sql = new StringBuffer("SELECT SUM(quantity), SUM(recCost * quantity), p.pb.product.brand.brand_ID, p.order.type FROM PurchaseOrderProduct p " + 
			         " WHERE p.order.status = ? AND p.order.creationTime BETWEEN ? AND ? AND p.pb.product.year.year_ID =" + yearId + " AND p.pb.product.quarter.quarter_ID =" + quarterId);

			sql.append(whereClause);
			sql.append(" GROUP BY p.pb.product.brand.brand_ID, p.order.type");
			
			List<Object> values = purchaseOrderProductDaoImpl.executeHQLSelect(sql.toString(), value_sale.toArray(), null, true);
			
			Map<Integer, HeadQPurchaseStatisticReportItemVO> dataMap = new HashMap<Integer, HeadQPurchaseStatisticReportItemVO>();
			for (Object record : values ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				double amount = Common_util.getDouble(records[1]);
				int brandIdDB = Common_util.getInt(records[2]);
				int type = Common_util.getInt(records[3]);
				
				HeadQPurchaseStatisticReportItemVO levelOneItem = dataMap.get(brandIdDB);
				if (levelOneItem != null){
					levelOneItem.putValue(type, quantity, amount);
				} else {
					Brand brand = brandDaoImpl.get(brandIdDB, true);
					
					String pinyin = brand.getPinyin();
					if (!StringUtils.isEmpty(pinyin)){
						name = pinyin.substring(0, 1) + " ";
					}
					
					 name += brand.getBrand_Name();
					
					levelOneItem = new HeadQPurchaseStatisticReportItemVO(name, parentId, supplierId, yearId, quarterId, brandIdDB,0, HeadQReportItemVO.STATE_CLOSED);
					levelOneItem.putValue(type, quantity, amount);
					
					dataMap.put(brandIdDB, levelOneItem);
				}
			}
			
			List<Integer> brandKey = new ArrayList<Integer>(dataMap.keySet());
			
			//1. 把基本对象放入
			//2. 计算总数
			for (Integer id : brandKey){
				HeadQPurchaseStatisticReportItemVO levelOneItem = dataMap.get(id);
				levelOneItem.reCalculate();

				reportItems.add(levelOneItem);
			}	
			
			
		} else if (brandId != 0) {
			//@2. 展开所有品牌的库存信息
			StringBuffer sql = new StringBuffer("SELECT SUM(quantity), SUM(recCost * quantity), p.pb.id, p.order.type FROM PurchaseOrderProduct p " + 
			         " WHERE p.order.status = ? AND p.order.creationTime BETWEEN ? AND ? AND p.pb.product.year.year_ID =" + yearId + " AND p.pb.product.quarter.quarter_ID =" + quarterId+ " AND p.pb.product.brand.brand_ID =" + brandId);

			sql.append(whereClause);
			sql.append(" GROUP BY p.pb.id, p.order.type");
			
			List<Object> values = purchaseOrderProductDaoImpl.executeHQLSelect(sql.toString(), value_sale.toArray(), null, true);
			
			Map<Integer, HeadQPurchaseStatisticReportItemVO> dataMap = new HashMap<Integer, HeadQPurchaseStatisticReportItemVO>();
			for (Object record : values ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				double amount = Common_util.getDouble(records[1]);
				int pbId = Common_util.getInt(records[2]);
				int type = Common_util.getInt(records[3]);
				
				HeadQPurchaseStatisticReportItemVO levelOneItem = dataMap.get(pbId);
				if (levelOneItem != null){
					levelOneItem.putValue(type, quantity, amount);
				} else {
					ProductBarcode pb = productBarcodeDaoImpl.get(pbId, true);
					Color color = pb.getColor();
					String colorName = "";
					if (color != null)
						colorName = color.getName();
					
					Product product = pb.getProduct();
					String gender = product.getGenderS();
					String sizeRange = product.getSizeRangeS();
					
					Category category = pb.getProduct().getCategory();
					
					name = Common_util.cutProductCode(pb.getProduct().getProductCode()) + colorName  + " " + gender + sizeRange +  category.getCategory_Name();
					
					levelOneItem = new HeadQPurchaseStatisticReportItemVO(name, parentId, supplierId, yearId, quarterId, brandId, pbId, HeadQReportItemVO.STATE_OPEN);
					levelOneItem.putValue(type, quantity, amount);
					
					dataMap.put(pbId, levelOneItem);
				}
			}
			
			List<Integer> pbKey = new ArrayList<Integer>(dataMap.keySet());
			Collections.sort(pbKey);
			
			//1. 把基本对象放入
			//2. 计算总数
			for (Integer id : pbKey){
				HeadQPurchaseStatisticReportItemVO levelOneItem = dataMap.get(id);
				levelOneItem.reCalculate();

				reportItems.add(levelOneItem);
			}
		}

		Collections.sort(reportItems, new HeadQStatisticReportItemVOSorter());
	    response.setReturnValue(reportItems);
	    return response;
	}
	
	/**
	 * 获取hq expense rpt eles
	 * @param parentId
	 * @param startDate
	 * @param endDate
	 * @param parentExpenseId
	 * @return
	 */
	public Response getHQExpenseRptEles(int parentId,Date startDate, Date endDate, int parentExpenseId ){
		Response response = new Response();
		List<HeadQExpenseRptElesVO> reportItems = new ArrayList<HeadQExpenseRptElesVO>();
		
		if (startDate == null){
			response.setReturnValue(reportItems);
			return response;
		}
		
		List<Object> value_sale = new ArrayList<Object>();
		value_sale.add(startDate);
		value_sale.add(endDate);
		value_sale.add(Expense.statusE.NORMAL.getValue());
	
		
		String name = "总计";
		
		if (parentId == 0){
			String hql = "SELECT feeType, sum(amount)  FROM Expense WHERE expenseDate BETWEEN ? AND ? AND status =? AND entity IS null GROUP BY feeType";
			
			List<Object> values = expenseDaoImpl.executeHQLSelect(hql, value_sale.toArray(), null, true);
			
			HeadQExpenseRptElesVO rootItem = new HeadQExpenseRptElesVO(name, 1,  HeadQSalesStatisticReportItemVO.STATE_CLOSED);
			
			if (values != null){
				for (Object record : values ){
					Object[] records = (Object[])record;
					int feeType = Common_util.getInt(records[0]);
					double amount = Common_util.getDouble(records[1]);

					rootItem.putValue(feeType, amount);
				}
			}
			
			reportItems.add(rootItem);
		} else if (parentExpenseId == 0){
			String hql = "SELECT expenseType.parentId, feeType, sum(amount)  FROM Expense WHERE expenseDate BETWEEN ? AND ? AND status =? AND entity IS null  GROUP BY expenseType.parentId,feeType";
			
			List<Object> values = expenseDaoImpl.executeHQLSelect(hql, value_sale.toArray(), null, true);
			
			Map<Integer, HeadQExpenseRptElesVO> resultMap = new HashMap<Integer, HeadQExpenseRptElesVO>();
			
			if (values != null){
				for (Object record : values ){
					Object[] records = (Object[])record;
					int expenseParentId = Common_util.getInt(records[0]);
					int feeType = Common_util.getInt(records[1]);
					double amount = Common_util.getDouble(records[2]);

					HeadQExpenseRptElesVO rootItem = resultMap.get(expenseParentId);
					if (rootItem == null){
						rootItem = new HeadQExpenseRptElesVO(name, 1,  HeadQSalesStatisticReportItemVO.STATE_CLOSED);
						ExpenseType type = expenseTypeDaoImpl.get(expenseParentId, true);
						rootItem.setName(type.getName());
						rootItem.setExpenseTypeParentId(expenseParentId);
					}
						
					rootItem.putValue(feeType, amount);
					
					resultMap.put(expenseParentId, rootItem);
				}
			}
			
			for (HeadQExpenseRptElesVO rootItem : resultMap.values())
			     reportItems.add(rootItem);
			
			Collections.sort(reportItems, new HeadQExpenseReportSort());
		} else {
			value_sale.add(parentExpenseId);
			String hql = "SELECT expenseType.id, feeType, sum(amount)  FROM Expense WHERE expenseDate BETWEEN ? AND ? AND status =? AND entity IS null AND expenseType.parentId = ?   GROUP BY expenseType.id,feeType";
			
			List<Object> values = expenseDaoImpl.executeHQLSelect(hql, value_sale.toArray(), null, true);
			
			Map<Integer, HeadQExpenseRptElesVO> resultMap = new HashMap<Integer, HeadQExpenseRptElesVO>();
			
			if (values != null){
				for (Object record : values ){
					Object[] records = (Object[])record;
					int expenseTypeId = Common_util.getInt(records[0]);
					int feeType = Common_util.getInt(records[1]);
					double amount = Common_util.getDouble(records[2]);

					HeadQExpenseRptElesVO rootItem = resultMap.get(expenseTypeId);
					if (rootItem == null){
						rootItem = new HeadQExpenseRptElesVO(name, 1,  HeadQSalesStatisticReportItemVO.STATE_OPEN);
						ExpenseType type = expenseTypeDaoImpl.get(expenseTypeId, true);
						rootItem.setName(type.getName());
						rootItem.setExpenseTypeId(expenseTypeId);
					}
						
					rootItem.putValue(feeType, amount);
					
					resultMap.put(expenseTypeId, rootItem);
				}
			}
			
			for (HeadQExpenseRptElesVO rootItem : resultMap.values())
			     reportItems.add(rootItem);
			
			Collections.sort(reportItems, new HeadQExpenseReportSort());
		}
		
	    response.setReturnValue(reportItems);
	    return response;
	}
	
	/**
	 * 获取销售统计报表的信息
	 * @param parentId
	 * @param chain_id
	 * @param year_ID
	 * @param quarter_ID
	 * @param brand_ID
	 * @param userInfor
	 * @return
	 */
	public Response getSalesStatisticReptEles(int parentId,Date startDate, Date endDate, int custId, int yearId, int quarterId, int brandId) {
		Response response = new Response();
		List<HeadQSalesStatisticReportItemVO> reportItems = new ArrayList<HeadQSalesStatisticReportItemVO>();
		
		if (startDate == null){
			response.setReturnValue(reportItems);
			return response;
		}
		
		List<Object> value_sale = new ArrayList<Object>();
		value_sale.add(InventoryOrder.STATUS_ACCOUNT_COMPLETE);
		value_sale.add(Common_util.formStartDate(startDate));
		value_sale.add(Common_util.formEndDate(endDate));

		String whereClause = "";
		
		HeadQCust cust = headQCustDaoImpl.getAllCustObj();
		if (custId != Common_util.ALL_RECORD_NEW){
			cust = headQCustDaoImpl.get(custId, true);
			whereClause = " AND p.order.cust.id = " + cust.getId();
		}

		String name = "";

		if (parentId == 0){
			//@2. 根节点
			name = cust.getName();
			String criteria = "SELECT SUM(quantity), SUM(recCost * quantity), SUM(wholeSalePrice * quantity), SUM(p.order.totalDiscount), p.order.order_type FROM InventoryOrderProduct p WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ? "+ whereClause + " GROUP BY p.order.order_type";
			
			List<Object> values = inventoryOrderProductDAOImpl.executeHQLSelect(criteria, value_sale.toArray(), null, true);
			
			HeadQSalesStatisticReportItemVO rootItem = new HeadQSalesStatisticReportItemVO(name, 1, custId, yearId, quarterId, brandId,0, HeadQSalesStatisticReportItemVO.STATE_CLOSED);
			
			if (values != null){
				for (Object record : values ){
					Object[] records = (Object[])record;
					int quantity = Common_util.getInt(records[0]);
					double cost = Common_util.getDouble(records[1]);
					double wholeSale = Common_util.getDouble(records[2]);
					double totalDiscount = Common_util.getDouble(records[3]);
					int type = Common_util.getInt(records[4]);
					
					rootItem.putValue(quantity, type, wholeSale, cost, totalDiscount);
				}
				
				rootItem.reCalculate();
			}
			
			reportItems.add(rootItem);
		} else if (yearId == 0){
			//@2. 展开所有年份的库存信息
			String criteria = "SELECT SUM(quantity), SUM(recCost * quantity), SUM(wholeSalePrice * quantity), p.productBarcode.product.year.year_ID, p.order.order_type FROM InventoryOrderProduct p WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ?  "+ whereClause + " GROUP BY p.productBarcode.product.year.year_ID, p.order.order_type";
			List<Object> values = inventoryOrderProductDAOImpl.executeHQLSelect(criteria, value_sale.toArray(), null, true);
			
			
			if (values != null){
				Map<Integer, HeadQSalesStatisticReportItemVO> dataMap = new HashMap<Integer, HeadQSalesStatisticReportItemVO>();
				for (Object record : values ){
					Object[] records = (Object[])record;
					int quantity = Common_util.getInt(records[0]);
					double cost = Common_util.getDouble(records[1]);
					double wholeSale = Common_util.getDouble(records[2]);
					int yearIdDB = Common_util.getInt(records[3]);
					int type = Common_util.getInt(records[4]);
					
					HeadQSalesStatisticReportItemVO levelFour = dataMap.get(yearIdDB);
					if (levelFour != null){
						levelFour.putValue(quantity, type, wholeSale, cost,0);
					} else {
						Year year = yearDaoImpl.get(yearIdDB, true);
						
						name = year.getYear() + "年";
						
						levelFour = new HeadQSalesStatisticReportItemVO(name, parentId, custId, yearIdDB, quarterId, brandId,0, HeadQSalesStatisticReportItemVO.STATE_CLOSED);

						levelFour.putValue(quantity, type, wholeSale, cost,0);
					}
					
					dataMap.put(yearIdDB, levelFour);
				}
				
				List<Integer> yearIds = new ArrayList<Integer>(dataMap.keySet());
				
				//1. 把基本对象放入
				for (Integer id : yearIds){
					HeadQSalesStatisticReportItemVO levelFourItem = dataMap.get(id);
					levelFourItem.reCalculate();

					reportItems.add(levelFourItem);
				}	
			}
			
			
		} else if (quarterId == 0){
			//@2. 展开所有季的库存信息
			String criteria = "SELECT SUM(quantity), SUM(recCost * quantity), SUM(wholeSalePrice * quantity), p.productBarcode.product.quarter.quarter_ID, p.order.order_type FROM InventoryOrderProduct p WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ?  AND p.productBarcode.product.year.year_ID = " + yearId + whereClause + " GROUP BY p.productBarcode.product.quarter.quarter_ID, p.order.order_type";
			List<Object> values = inventoryOrderProductDAOImpl.executeHQLSelect(criteria, value_sale.toArray(), null, true);
			
			
			if (values != null){
				Map<Integer, HeadQSalesStatisticReportItemVO> dataMap = new HashMap<Integer, HeadQSalesStatisticReportItemVO>();
				for (Object record : values ){
					Object[] records = (Object[])record;
					int quantity = Common_util.getInt(records[0]);
					double cost = Common_util.getDouble(records[1]);
					double wholeSale = Common_util.getDouble(records[2]);
					int quarterIdDB = Common_util.getInt(records[3]);
					int type = Common_util.getInt(records[4]);
					
					HeadQSalesStatisticReportItemVO levelFour = dataMap.get(quarterIdDB);
					if (levelFour != null){
						levelFour.putValue(quantity, type, wholeSale, cost,0);
					} else {
						Year year = yearDaoImpl.get(yearId, true);
						Quarter quarter = quarterDaoImpl.get(quarterIdDB, true);
						name = year.getYear() + "年" + quarter.getQuarter_Name();
						
						levelFour = new HeadQSalesStatisticReportItemVO(name, parentId, custId, yearId, quarterIdDB, brandId,0,  HeadQSalesStatisticReportItemVO.STATE_CLOSED);

						levelFour.putValue(quantity, type, wholeSale, cost,0);
					}
					
					dataMap.put(quarterIdDB, levelFour);
				}
				
				List<Integer> quarterIds = new ArrayList<Integer>(dataMap.keySet());
				
				//1. 把基本对象放入
				for (Integer id : quarterIds){
					HeadQSalesStatisticReportItemVO levelFourItem = dataMap.get(id);
					levelFourItem.reCalculate();

					reportItems.add(levelFourItem);
				}	
			}
		} else if (brandId == 0){
			//@2. 展开所有品牌的库存信息
			String criteria = "SELECT SUM(quantity), SUM(recCost * quantity), SUM(wholeSalePrice * quantity),  p.productBarcode.product.brand.brand_ID, p.order.order_type FROM InventoryOrderProduct p WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ?  AND p.productBarcode.product.year.year_ID = " + yearId + " AND p.productBarcode.product.quarter.quarter_ID = " + quarterId + whereClause + " GROUP BY p.productBarcode.product.brand.brand_ID, p.order.order_type";
			List<Object> values = inventoryOrderProductDAOImpl.executeHQLSelect(criteria, value_sale.toArray(), null, true);
			
			
			if (values != null){
				Map<Integer, HeadQSalesStatisticReportItemVO> dataMap = new HashMap<Integer, HeadQSalesStatisticReportItemVO>();
				for (Object record : values ){
					Object[] records = (Object[])record;
					int quantity = Common_util.getInt(records[0]);
					double cost = Common_util.getDouble(records[1]);
					double wholeSale = Common_util.getDouble(records[2]);
					int brandIdDB = Common_util.getInt(records[3]);
					int type = Common_util.getInt(records[4]);
					
					HeadQSalesStatisticReportItemVO levelFour = dataMap.get(brandIdDB);
					if (levelFour != null){
						levelFour.putValue(quantity, type, wholeSale, cost,0);
					} else {
						Brand brand = brandDaoImpl.get(brandIdDB, true);

						String pinyin = brand.getPinyin();
						if (!StringUtils.isEmpty(pinyin)){
							name = pinyin.substring(0, 1) + " ";
						}
						
						 name += brand.getBrand_Name();
						
						levelFour = new HeadQSalesStatisticReportItemVO(name, parentId, custId, yearId, quarterId, brandIdDB,0, HeadQSalesStatisticReportItemVO.STATE_CLOSED);
						levelFour.putValue(quantity, type, wholeSale, cost,0);
					}
					
					dataMap.put(brandIdDB, levelFour);
				}
				
				List<Integer> brandIds = new ArrayList<Integer>(dataMap.keySet());
				
				//1. 把基本对象放入
				for (Integer id : brandIds){
					HeadQSalesStatisticReportItemVO levelFourItem = dataMap.get(id);
					levelFourItem.reCalculate();

					reportItems.add(levelFourItem);
				}	

			}
		} else if (brandId != 0) {
			//@2. 展开所有品牌的库存信息
			String criteria = "SELECT SUM(quantity), SUM(recCost * quantity), SUM(wholeSalePrice * quantity), p.productBarcode.id, p.order.order_type FROM InventoryOrderProduct p WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ? AND p.productBarcode.product.year.year_ID = " + yearId + " AND p.productBarcode.product.quarter.quarter_ID = " + quarterId + whereClause + " AND p.productBarcode.product.brand.brand_ID = " + brandId + " GROUP BY p.productBarcode.id, p.order.order_type";
			List<Object> values = inventoryOrderProductDAOImpl.executeHQLSelect(criteria, value_sale.toArray(), null, true);

			if (values != null){
				Map<Integer, HeadQSalesStatisticReportItemVO> dataMap = new HashMap<Integer, HeadQSalesStatisticReportItemVO>();
				for (Object record : values ){
					Object[] records = (Object[])record;
					int quantity = Common_util.getInt(records[0]);
					double cost = Common_util.getDouble(records[1]);
					double wholeSale = Common_util.getDouble(records[2]);
					int pbId = Common_util.getInt(records[3]);
					int type = Common_util.getInt(records[4]);
					
					HeadQSalesStatisticReportItemVO levelFour = dataMap.get(pbId);
					if (levelFour != null){
						levelFour.putValue(quantity, type, wholeSale, cost,0);
					} else {
						ProductBarcode pb = productBarcodeDaoImpl.get(pbId, true);
						String barcode = pb.getBarcode();
						Color color = pb.getColor();
						String colorName = "";
						if (color != null)
							colorName = color.getName();
						
						Product product = pb.getProduct();
						
						String gender = product.getGenderS();
						String sizeRange = product.getSizeRangeS();
						
						Category category = product.getCategory();
						name = Common_util.cutProductCode(pb.getProduct().getProductCode()) + colorName + " "  + gender + sizeRange +  category.getCategory_Name();
						
						levelFour = new HeadQSalesStatisticReportItemVO(name, parentId, custId, yearId, quarterId, brandId, pbId, HeadQSalesStatisticReportItemVO.STATE_OPEN);
						levelFour.putValue(quantity, type, wholeSale, cost,0);
						levelFour.setBarcode(barcode);
					}
					
					dataMap.put(pbId, levelFour);
				}
				
				List<Integer> pbIds = new ArrayList<Integer>(dataMap.keySet());
				
				//1. 把基本对象放入
				for (Integer id : pbIds){
					HeadQSalesStatisticReportItemVO levelFourItem = dataMap.get(id);
					levelFourItem.reCalculate();

					reportItems.add(levelFourItem);
				}	
			}
		}
		Collections.sort(reportItems, new HeadQStatisticReportItemVOSorter());
	    response.setReturnValue(reportItems);
	    return response;
	}


	/**
	 * 下
	 * @param parentId
	 * @param supplier
	 * @param year_ID
	 * @param quarter_ID
	 * @param brand_ID
	 * @param string
	 * @return
	 */
	@Transactional
	public Response downloadPurchaseExcelReport(int parentId, int supplierId, int yearId,
			int quarterId, int brandId, Date startDate,Date endDate,String excelPath) {
		Response response = new Response();
		
		List<Object> value_sale = new ArrayList<Object>();
		value_sale.add(PurchaseOrder.STATUS_COMPLETE);
		value_sale.add(Common_util.formStartDate(startDate));
		value_sale.add(Common_util.formEndDate(endDate));

		String whereClause = "";
		String rptDesp = "";

		
		StringBuffer sql = new StringBuffer("SELECT SUM(quantity), SUM(recCost * quantity), p.pb.id, p.order.type FROM PurchaseOrderProduct p WHERE p.order.status = ? AND p.order.creationTime BETWEEN ? AND ?") ;

		if (yearId != Common_util.ALL_RECORD_NEW){
			sql.append(" AND p.pb.product.year.year_ID =" + yearId);
			Year year = yearDaoImpl.get(yearId, true);
			
			rptDesp += " " + year.getYear();
		}
		
		if (quarterId != Common_util.ALL_RECORD_NEW){
			sql.append(" AND p.pb.product.quarter.quarter_ID =" + quarterId);
			Quarter quarter = quarterDaoImpl.get(quarterId, true);
			
			rptDesp += " " + quarter.getQuarter_Name();
		}
		
		if (brandId != Common_util.ALL_RECORD_NEW){
			sql.append(" AND p.pb.product.brand.brand_ID =" + brandId);
			Brand brand = brandDaoImpl.get(brandId, true);
			
			rptDesp += " " + brand.getBrand_Name();
		}

		HeadQSupplier supplier = headQSupplierDaoImpl.getAllSupplierObj();
		if (supplierId != Common_util.ALL_RECORD_NEW){
			supplier = headQSupplierDaoImpl.get(supplierId, true);
			whereClause = " AND p.order.supplier.id = " + supplier.getId();
		}
		rptDesp = supplier.getName() + " " + rptDesp;
		
		sql.append(whereClause);
		sql.append(" GROUP BY p.pb.id, p.order.type");
		
		List<Object> values = purchaseOrderProductDaoImpl.executeHQLSelect(sql.toString(), value_sale.toArray(), null, true);
		
		Map<Integer, HeadQPurchaseStatisticReportItem> dataMap = new HashMap<Integer, HeadQPurchaseStatisticReportItem>();
		HeadQPurchaseStatisticReportItem totalItem = new HeadQPurchaseStatisticReportItem();
		for (Object record : values ){
			Object[] records = (Object[])record;
			int quantity = Common_util.getInt(records[0]);
			double amount = Common_util.getDouble(records[1]);
			int pbId = Common_util.getInt(records[2]);
			int type = Common_util.getInt(records[3]);
			
			totalItem.add(type, quantity, amount);
			
			HeadQPurchaseStatisticReportItem levelOneItem = dataMap.get(pbId);
			if (levelOneItem != null){
				levelOneItem.add(type, quantity, amount);
			} else {
				ProductBarcode pb = productBarcodeDaoImpl.get(pbId, true);
				
				levelOneItem = new HeadQPurchaseStatisticReportItem(type, quantity, amount, pb);
				
				dataMap.put(pbId, levelOneItem);
			}
		}
		
		List<HeadQPurchaseStatisticReportItem> items = new ArrayList<HeadQPurchaseStatisticReportItem>(dataMap.values());
		
		Collections.sort(items, new HeadQStatisticReportItemSorter());
		//2. 准备excel 报表
		try {
		    HeadQPurchaseStatisticsReportTemplate rptTemplate = new HeadQPurchaseStatisticsReportTemplate(items, totalItem, rptDesp, excelPath, startDate, endDate);
			HSSFWorkbook wb = rptTemplate.process();
			
			ByteArrayInputStream byteArrayInputStream = ExcelUtil.convertExcelToInputStream(wb);
			
			response.setReturnValue(byteArrayInputStream);
			response.setReturnCode(Response.SUCCESS);
		} catch (IOException e){
			e.printStackTrace();
			response.setFail(e.getMessage());
		}
		return response;
	}
	
	/**
	 * 下
	 * @param parentId
	 * @param supplier
	 * @param year_ID
	 * @param quarter_ID
	 * @param brand_ID
	 * @param string
	 * @return
	 */
	@Transactional
	public Response downloadSalesExcelReport(int parentId, int custId, int yearId,
			int quarterId, int brandId, Date startDate,Date endDate,String excelPath) {
		Response response = new Response();
		
		List<Object> value_sale = new ArrayList<Object>();
		value_sale.add(InventoryOrder.STATUS_ACCOUNT_COMPLETE);
		value_sale.add(Common_util.formStartDate(startDate));
		value_sale.add(Common_util.formEndDate(endDate));

		String whereClause = "";
		String rptDesp = "";

		
		StringBuffer sql = new StringBuffer("SELECT SUM(quantity), SUM(recCost * quantity), SUM(wholeSalePrice * quantity),p.productBarcode.id, p.order.order_type FROM InventoryOrderProduct p WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ? ") ;

		if (yearId != Common_util.ALL_RECORD_NEW){
			sql.append(" AND p.productBarcode.product.year.year_ID =" + yearId);
			Year year = yearDaoImpl.get(yearId, true);
			
			rptDesp += " " + year.getYear();
		}
		
		if (quarterId != Common_util.ALL_RECORD_NEW){
			sql.append(" AND p.productBarcode.product.quarter.quarter_ID =" + quarterId);
			Quarter quarter = quarterDaoImpl.get(quarterId, true);
			
			rptDesp += " " + quarter.getQuarter_Name();
		}
		
		if (brandId != Common_util.ALL_RECORD_NEW){
			sql.append(" AND p.productBarcode.product.brand.brand_ID =" + brandId);
			Brand brand = brandDaoImpl.get(brandId, true);
			
			rptDesp += " " + brand.getBrand_Name();
		}

		HeadQCust cust = headQCustDaoImpl.getAllCustObj();
		if (custId != Common_util.ALL_RECORD_NEW){
			cust = headQCustDaoImpl.get(custId, true);
			whereClause = " AND p.order.cust.id = " + cust.getId();
		}
		rptDesp = cust.getName() + " " + rptDesp;
		
		sql.append(whereClause);
		sql.append(" GROUP BY p.productBarcode.id, p.order.order_type");
		
		List<Object> values = inventoryOrderProductDAOImpl.executeHQLSelect(sql.toString(), value_sale.toArray(), null, true);
		
		Map<Integer, HeadQSalesStatisticReportItem> dataMap = new HashMap<Integer, HeadQSalesStatisticReportItem>();
		HeadQSalesStatisticReportItem totalItem = new HeadQSalesStatisticReportItem();
		for (Object record : values ){
			Object[] records = (Object[])record;
			int quantity = Common_util.getInt(records[0]);
			double cost = Common_util.getDouble(records[1]);
			double amount = Common_util.getDouble(records[2]);
			int pbId = Common_util.getInt(records[3]);
			int type = Common_util.getInt(records[4]);
			
			totalItem.add(type, quantity, amount, cost);
			
			HeadQSalesStatisticReportItem levelOneItem = dataMap.get(pbId);
			if (levelOneItem != null){
				levelOneItem.add(type, quantity, amount, cost);
			} else {
				ProductBarcode pb = productBarcodeDaoImpl.get(pbId, true);
				
				levelOneItem = new HeadQSalesStatisticReportItem(type, quantity, amount, cost, pb);
				
				dataMap.put(pbId, levelOneItem);
			}
		}
		
		List<HeadQSalesStatisticReportItem> items = new ArrayList<HeadQSalesStatisticReportItem>(dataMap.values());
		
		Collections.sort(items, new HeadQStatisticReportItemSorter());
		//2. 准备excel 报表
		try {
		    HeadQSalesStatisticsReportTemplate rptTemplate = new HeadQSalesStatisticsReportTemplate(items, totalItem, rptDesp, excelPath, startDate, endDate);
			HSSFWorkbook wb = rptTemplate.process();
			
			ByteArrayInputStream byteArrayInputStream = ExcelUtil.convertExcelToInputStream(wb);
			
			response.setReturnValue(byteArrayInputStream);
			response.setReturnCode(Response.SUCCESS);
		} catch (IOException e){
			e.printStackTrace();
			response.setFail(e.getMessage());
		}
		return response;
	}
	
	/**
	 * 下在客户信息成excel

	 * @return
	 */
	@Transactional
	public Response downloadCustInforExcelReport(String excelPath) {
		Response response = new Response();

		List<HeadQCust> custs = headQCustDaoImpl.getAll(true);

		//2. 准备excel 报表
		try {
		    HeadQCustInforTemplate rptTemplate = new HeadQCustInforTemplate(custs, excelPath);
			HSSFWorkbook wb = rptTemplate.process();
			
			ByteArrayInputStream byteArrayInputStream = ExcelUtil.convertExcelToInputStream(wb);
			
			response.setReturnValue(byteArrayInputStream);
			response.setReturnCode(Response.SUCCESS);
		} catch (IOException e){
			e.printStackTrace();
			response.setFail(e.getMessage());
		}
		return response;
	}

	/**
	 * 下在供应商信息成excel

	 * @return
	 */
	@Transactional
	public Response downloadSupplierInforExcelReport(String excelPath) {
		Response response = new Response();

		List<HeadQSupplier> suppliers = headQSupplierDaoImpl.getAll(true);

		//2. 准备excel 报表
		try {
		    HeadQSupplierInforTemplate rptTemplate = new HeadQSupplierInforTemplate(suppliers, excelPath);
			HSSFWorkbook wb = rptTemplate.process();
			
			ByteArrayInputStream byteArrayInputStream = ExcelUtil.convertExcelToInputStream(wb);
			
			response.setReturnValue(byteArrayInputStream);
			response.setReturnCode(Response.SUCCESS);
		} catch (IOException e){
			e.printStackTrace();
			response.setFail(e.getMessage());
		}
		return response;
	}
	
	/**
	 * 下在供应商往来账目成excel

	 * @return
	 */
	@Transactional
	public Response downloadSupplierAcctFlow(String excelPath, Date startDate, Date endDate, int supplierId) {
		Response response = new Response();

		response = financeSupplierService.searchAcctFlow(startDate, endDate, supplierId);
	
		Map<String, List> data = (Map)response.getReturnValue();
		List<SupplierAcctFlowReportItem> items = (List<SupplierAcctFlowReportItem>)data.get("rows");
		
		HeadQSupplier supplier = headQSupplierDaoImpl.get(supplierId, true);
		//2. 准备excel 报表
		try {
		    HeadQSupplierAcctFlowTemplate rptTemplate = new HeadQSupplierAcctFlowTemplate(items, excelPath, supplier, startDate, endDate);
		    HSSFWorkbook wb = rptTemplate.process();
			
			ByteArrayInputStream byteArrayInputStream = ExcelUtil.convertExcelToInputStream(wb);
			
			response.setReturnValue(byteArrayInputStream);
			response.setReturnCode(Response.SUCCESS);
		} catch (IOException e){
			e.printStackTrace();
			response.setFail(e.getMessage());
		}
		return response;
	}
	
	/**
	 * 下在客户往来账目成excel

	 * @return
	 */
	@Transactional
	public Response downloadCustAcctFlow(String excelPath, Date startDate, Date endDate, int custId) {
		Response response = new Response();

		response = financeService.searchAcctFlow(startDate, endDate, custId, false);
		
		Map<String, List> data = (Map)response.getReturnValue();
		List<ChainAcctFlowReportItem> items = (List<ChainAcctFlowReportItem>)data.get("rows");
		
		HeadQCust cust = headQCustDaoImpl.get(custId, true);
		//2. 准备excel 报表
		try {
		    HeadQCustAcctFlowTemplate rptTemplate = new HeadQCustAcctFlowTemplate(items, excelPath, cust, startDate, endDate);
		    HSSFWorkbook wb = rptTemplate.process();
			
			ByteArrayInputStream byteArrayInputStream = ExcelUtil.convertExcelToInputStream(wb);
			
			response.setReturnValue(byteArrayInputStream);
			response.setReturnCode(Response.SUCCESS);
		} catch (IOException e){
			e.printStackTrace();
			response.setFail(e.getMessage());
		}
		return response;
	}

	/**
	 * 准备 acct flow report的ui
	 * @param uiBean
	 */
	public void prepareAcctFlowReportUI(HeadQReportFormBean formBean,HeadQReportUIBean uiBean) {
		List<Year> years = yearDaoImpl.getAll(true);
		List<Quarter> quarters = quarterDaoImpl.getAll(true);
		
		uiBean.setQuarters(quarters);
		uiBean.setYears(years);
		
		formBean.setStartDate(Common_util.getToday());
		
	}


	/**
	 * 
	 * @param path
	 * @param startDate
	 * @param endDate
	 * @param year_ID
	 * @param quarter_ID
	 * @return
	 */
	@Transactional
	public Response downloadCustAcctFlowReport(String path, Date startDate, Date endDate, int yearId,int quarterId) {
		Response response = new Response();

		List<HeadQCustAcctFlowReportItem> items = new ArrayList<HeadQCustAcctFlowReportItem>();
		
		Year year = yearDaoImpl.get(yearId, true);
		Quarter quarter = quarterDaoImpl.get(quarterId, true);
		String curretnYearQuarter = year.getYear() + " " + quarter.getQuarter_Name();
		
		//1. 准备客户名单
		Set<Integer> chainStoreClientIds = chainStoreDaoImpl.getAllClientIds();
		chainStoreClientIds.remove(SystemParm.getTestClientId());
		
		//2. 准备日期
		java.util.Date startDate1 = Common_util.formStartDate(startDate);
		java.util.Date endDate1 = Common_util.formEndDate(endDate);
		
		//3.获取上期欠款
		Map<Integer, Double> lastPeriodAcctBalance = headQAcctFlowDaoImpl.getAccumulateAcctFlowBefore(chainStoreClientIds, startDate1);
		
		//4.获取当期期末欠款
		Map<Integer, Double> currentPeriodAcctBalance = headQAcctFlowDaoImpl.getAccumulateAcctFlowBefore(chainStoreClientIds, endDate1);
		
		//5.获取本期付款
		DetachedCriteria financeCriteria = DetachedCriteria.forClass(FinanceBill.class);
		
		financeCriteria.add(Restrictions.in("cust.id", chainStoreClientIds));
		financeCriteria.add(Restrictions.eq("status", FinanceBill.STATUS_COMPLETE));
		financeCriteria.add(Restrictions.eq("type", FinanceBill.FINANCE_INCOME_HQ));
		financeCriteria.add(Restrictions.between("billDate", startDate, endDate));
		
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.groupProperty("cust.id"));
		projList.add(Projections.sum("invoiceTotal"));
		financeCriteria.setProjection(projList);
		
		List<Object> result = financeBillImpl.getByCriteriaProjection(financeCriteria, false);
		Map<Integer, Double> stockMap = new HashMap<Integer, Double>();

		for (Object object : result)
		  if (object != null){
			Object[] recordResult = (Object[])object;
			int clientId = Common_util.getInt(recordResult[0]);
			double amount =  Common_util.getDouble(recordResult[1]);
			stockMap.put(clientId, amount);
		  } 
		
		//6. 获取本期发生 这段时间的销售金额
		DetachedCriteria inventoryCriteria = DetachedCriteria.forClass(InventoryOrder.class);
		
		inventoryCriteria.add(Restrictions.in("cust.id", chainStoreClientIds));
		inventoryCriteria.add(Restrictions.between("order_EndTime", startDate1, endDate1));
		inventoryCriteria.add(Restrictions.eq("order_Status", InventoryOrder.STATUS_ACCOUNT_COMPLETE));
		
		ProjectionList inventoryProjList = Projections.projectionList();
		inventoryProjList.add(Projections.groupProperty("cust.id"));
		inventoryProjList.add(Projections.groupProperty("order_type"));
		inventoryProjList.add(Projections.sum("totalWholePrice"));
		inventoryCriteria.setProjection(inventoryProjList);
		
		List<Object> inventoryResult = inventoryOrderDAOImpl.getByCriteriaProjection(inventoryCriteria, false);
		Map<Integer, Double> inventoryMap = new HashMap<Integer, Double>();

		for (Object object : inventoryResult)
		  if (object != null){
			Object[] recordResult = (Object[])object;
			int clientId = Common_util.getInt(recordResult[0]);
			int type = Common_util.getInt(recordResult[1]);
			double amount =  Common_util.getDouble(recordResult[2]);
			
			Double amountInMap = inventoryMap.get(clientId);
			if (amountInMap == null){
				amountInMap = new Double(0);
			}
			
			switch (type) {
				case InventoryOrder.TYPE_SALES_ORDER_W:
					amountInMap += amount;
					break;
				case InventoryOrder.TYPE_SALES_RETURN_ORDER_W:
					amountInMap -= amount;
					break;
				default:
					break;
			}
			
			inventoryMap.put(clientId, amountInMap);
		  } 
		
		//7. 获取当前季度的采购
		List<Object> value_sale = new ArrayList<Object>();
		value_sale.add(InventoryOrder.STATUS_ACCOUNT_COMPLETE);

		String criteria = "SELECT SUM(wholeSalePrice * quantity),  p.order.cust.id, p.order.order_type FROM InventoryOrderProduct p WHERE p.order.order_Status = ?  AND p.productBarcode.product.year.year_ID = " + yearId + " AND p.productBarcode.product.quarter.quarter_ID = " + quarterId + " GROUP BY p.order.cust.id, p.order.order_type";
		List<Object> values = inventoryOrderProductDAOImpl.executeHQLSelect(criteria, value_sale.toArray(), null, true);
		
		Map<Integer, Double> inventoryOrderProductMap = new HashMap<Integer, Double>();
		if (values != null){
			for (Object record : values ){
				Object[] records = (Object[])record;

				double amount = Common_util.getDouble(records[0]);
				int clientId = Common_util.getInt(records[1]);
				int type = Common_util.getInt(records[2]);
				
				Double amountInMap = inventoryMap.get(clientId);
				if (amountInMap == null){
					amountInMap = new Double(0);
				}
				
				switch (type) {
					case InventoryOrder.TYPE_SALES_ORDER_W:
						amountInMap += amount;
						break;
					case InventoryOrder.TYPE_SALES_RETURN_ORDER_W:
						amountInMap -= amount;
						break;
					default:
						break;
				}
				
				inventoryOrderProductMap.put(clientId, amountInMap);
			}
		}

		//8.获取数据
		List<ChainStore> chainStores = chainStoreDaoImpl.getAllChainStoreList();
		for (ChainStore store : chainStores){
			int custId = store.getClient_id();
			
			if (custId == SystemParm.getTestClientId())
				continue;
			
			HeadQCustAcctFlowReportItem item = new HeadQCustAcctFlowReportItem(lastPeriodAcctBalance.get(custId), inventoryMap.get(custId), currentPeriodAcctBalance.get(custId), stockMap.get(custId), inventoryOrderProductMap.get(custId));
			item.setChainStore(store);
			
			items.add(item);
		}
		
		//2. 准备excel 报表
		try {
		    HeadQCustAcctFlowReportTemplate rptTemplate = new HeadQCustAcctFlowReportTemplate(items, path, startDate, endDate, curretnYearQuarter);
		    HSSFWorkbook wb = rptTemplate.process();
			
			ByteArrayInputStream byteArrayInputStream = ExcelUtil.convertExcelToInputStream(wb);
			
			response.setReturnValue(byteArrayInputStream);
			response.setReturnCode(Response.SUCCESS);
		} catch (IOException e){
			e.printStackTrace();
			response.setFail(e.getMessage());
		}
		return response;
	}

	public void prepareHQExpenseReportUI(HeadQReportFormBean formBean) {
		formBean.setStartDate(Common_util.getToday());
		formBean.setEndDate(Common_util.getToday());
	}
	
	/**
	 * 
	 * @param path
	 * @param startDate
	 * @param endDate
	 * @param year_ID
	 * @param quarter_ID
	 * @return
	 */
	@Transactional
	public Response downloadHeadQExpenseReport(String path, Date startDate, Date endDate) {
		Response response = new Response();
 
		//1.1 获取总信息
		List<Object> value_sale = new ArrayList<Object>();
		value_sale.add(startDate);
		value_sale.add(endDate);
		value_sale.add(Expense.statusE.NORMAL.getValue());
		
		String hql = "SELECT feeType, sum(amount)  FROM Expense WHERE expenseDate BETWEEN ? AND ? AND status =? AND entity IS null GROUP BY feeType";
		
		List<Object> values = expenseDaoImpl.executeHQLSelect(hql, value_sale.toArray(), null, true);
		
		HeadQExpenseRptElesVO totalItem = new HeadQExpenseRptElesVO("", 0,  HeadQSalesStatisticReportItemVO.STATE_CLOSED);
		
		if (values != null){
			for (Object record : values ){
				Object[] records = (Object[])record;
				int feeType = Common_util.getInt(records[0]);
				double amount = Common_util.getDouble(records[1]);

				totalItem.putValue(feeType, amount);
			}
		}
		
		totalItem.setParentExpenseTypeName("所有费用汇总");
		
		//1.2 获取汇总信息
		hql = "SELECT expenseType.parentId, feeType, sum(amount)  FROM Expense WHERE expenseDate BETWEEN ? AND ? AND status =? AND entity IS null  GROUP BY expenseType.parentId,feeType";
		
		values = expenseDaoImpl.executeHQLSelect(hql, value_sale.toArray(), null, true);
		
		Map<Integer, HeadQExpenseRptElesVO> sumMap = new HashMap<Integer, HeadQExpenseRptElesVO>();
		
		if (values != null){
			for (Object record : values ){
				Object[] records = (Object[])record;
				int expenseParentId = Common_util.getInt(records[0]);
				int feeType = Common_util.getInt(records[1]);
				double amount = Common_util.getDouble(records[2]);

				HeadQExpenseRptElesVO rootItem = sumMap.get(expenseParentId);
				if (rootItem == null){
					rootItem = new HeadQExpenseRptElesVO("", 1,  HeadQSalesStatisticReportItemVO.STATE_CLOSED);
					ExpenseType type = expenseTypeDaoImpl.get(expenseParentId, true);
					rootItem.setName(type.getName());
					rootItem.setExpenseTypeParentId(expenseParentId);
				}
					
				rootItem.putValue(feeType, amount);
				sumMap.put(expenseParentId, rootItem);
			}
		}
		
		List<HeadQExpenseRptElesVO> sumItems = new ArrayList<HeadQExpenseRptElesVO>();
		for (HeadQExpenseRptElesVO rootItem : sumMap.values())
			sumItems.add(rootItem);
		
		Collections.sort(sumItems, new HeadQExpenseReportSort());
		
		
		//1.3获取子目录

		hql = "SELECT expenseType.parentId, expenseType.id, feeType, sum(amount)  FROM Expense WHERE expenseDate BETWEEN ? AND ? AND status =? AND entity IS null  GROUP BY expenseType.id,feeType";
		
		values = expenseDaoImpl.executeHQLSelect(hql, value_sale.toArray(), null, true);
		
		Map<Integer, HeadQExpenseRptElesVO> resultMap = new HashMap<Integer, HeadQExpenseRptElesVO>();
		
		if (values != null){
			String name = "";
			for (Object record : values ){
				Object[] records = (Object[])record;
				int parentExpenseTypeId = Common_util.getInt(records[0]);
				int expenseTypeId = Common_util.getInt(records[1]);
				int feeType = Common_util.getInt(records[2]);
				double amount = Common_util.getDouble(records[3]);

				HeadQExpenseRptElesVO rootItem = resultMap.get(expenseTypeId);
				if (rootItem == null){
					ExpenseType type = expenseTypeDaoImpl.get(expenseTypeId, true);
					name = type.getName();				
					rootItem = new HeadQExpenseRptElesVO(name, 2,  HeadQSalesStatisticReportItemVO.STATE_OPEN);

					rootItem.setExpenseTypeId(expenseTypeId);
					rootItem.setExpenseTypeParentId(parentExpenseTypeId);
				}
					
				rootItem.putValue(feeType, amount);
				
				resultMap.put(expenseTypeId, rootItem);
			}
		}
		
		Map<Integer, List<HeadQExpenseRptElesVO>> eleMap = new HashMap<Integer, List<HeadQExpenseRptElesVO>>();
		for (HeadQExpenseRptElesVO rootItem : resultMap.values()){
			List<HeadQExpenseRptElesVO> eles = eleMap.get(rootItem.getExpenseTypeParentId());
		    
			if (eles == null)
				eles = new ArrayList<HeadQExpenseRptElesVO>();
			
			eles.add(rootItem);
			eleMap.put(rootItem.getExpenseTypeParentId(), eles);
		}
		
        
		
		//1.2 准备报表细节
		List<HeadQExpenseRptElesVO> reportItems = new ArrayList<HeadQExpenseRptElesVO>();
		int parentExpenseTypeId  = 0;
		for (HeadQExpenseRptElesVO item : sumItems){
			parentExpenseTypeId = item.getExpenseTypeParentId();
			
			List<HeadQExpenseRptElesVO> eles = eleMap.get(parentExpenseTypeId);

            Collections.sort(eles, new HeadQExpenseReportSort());
            eles.get(0).setParentExpenseTypeName(expenseTypeDaoImpl.get(parentExpenseTypeId, true).getName());
            
            reportItems.addAll(eles);
            
            String name = item.getName();
            item.setName(name + " 汇总");
            reportItems.add(item);
		}
		
		reportItems.add(totalItem);
		
		//2. 准备excel 报表
		try {
		    HeadQExpenseReportTemplate rptTemplate = new HeadQExpenseReportTemplate(reportItems, path, startDate, endDate);
		    HSSFWorkbook wb = rptTemplate.process();
			
			ByteArrayInputStream byteArrayInputStream = ExcelUtil.convertExcelToInputStream(wb);
			
			response.setReturnValue(byteArrayInputStream);
			response.setReturnCode(Response.SUCCESS);
		} catch (IOException e){
			e.printStackTrace();
			response.setFail(e.getMessage());
		}
		return response;
	}
	


}
