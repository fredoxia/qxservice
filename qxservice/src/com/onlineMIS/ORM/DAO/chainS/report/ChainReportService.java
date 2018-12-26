package com.onlineMIS.ORM.DAO.chainS.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.naming.java.javaURLContextFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.chainS.inventoryFlow.ChainInOutStockDaoImpl;
import com.onlineMIS.ORM.DAO.chainS.sales.ChainDailySalesDaoImpl;
import com.onlineMIS.ORM.DAO.chainS.sales.ChainStoreSalesOrderDaoImpl;
import com.onlineMIS.ORM.DAO.chainS.user.ChainStoreDaoImpl;
import com.onlineMIS.ORM.DAO.chainS.user.ChainStoreService;
import com.onlineMIS.ORM.DAO.chainS.user.ChainUserInforDaoImpl;
import com.onlineMIS.ORM.DAO.chainS.user.ChainUserInforService;
import com.onlineMIS.ORM.DAO.chainS.vip.ChainVIPCardImpl;
import com.onlineMIS.ORM.DAO.chainS.vip.ChainVIPPrepaidImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.BrandDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.ProductBarcodeDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.ProductDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.QuarterDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.YearDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.finance.HeadQFinanceTraceImpl;
import com.onlineMIS.ORM.DAO.headQ.finance.FinanceCategoryImpl;
import com.onlineMIS.ORM.DAO.headQ.inventory.InventoryOrderDAOImpl;
import com.onlineMIS.ORM.DAO.headQ.inventory.InventoryOrderProductDAOImpl;
import com.onlineMIS.ORM.entity.base.Pager;
import com.onlineMIS.ORM.entity.chainS.inventoryFlow.ChainInOutStock;
import com.onlineMIS.ORM.entity.chainS.inventoryFlow.ChainInventoryItemVO;
import com.onlineMIS.ORM.entity.chainS.inventoryFlow.ChainInventoryReportTemplate;
import com.onlineMIS.ORM.entity.chainS.report.ChainAllInOneReportItemLevelFour;
import com.onlineMIS.ORM.entity.chainS.report.ChainAllInOneReportItemLevelOne;
import com.onlineMIS.ORM.entity.chainS.report.ChainAllInOneReportItemLevelThree;
import com.onlineMIS.ORM.entity.chainS.report.ChainAllInOneReportItemLevelTwo;
import com.onlineMIS.ORM.entity.chainS.report.ChainBatchRptRepositoty;
import com.onlineMIS.ORM.entity.chainS.report.ChainDailySalesAnalysis;
import com.onlineMIS.ORM.entity.chainS.report.ChainFinanceReport;
import com.onlineMIS.ORM.entity.chainS.report.ChainFinanceReportItem;
import com.onlineMIS.ORM.entity.chainS.report.ChainPurchaseReport;
import com.onlineMIS.ORM.entity.chainS.report.ChainPurchaseStatisReportItem;

import com.onlineMIS.ORM.entity.chainS.report.ChainPurchaseStatisticReportItemVO;
import com.onlineMIS.ORM.entity.chainS.report.ChainReport;
import com.onlineMIS.ORM.entity.chainS.report.ChainSalesReport;
import com.onlineMIS.ORM.entity.chainS.report.ChainSalesStatisReportItem;
import com.onlineMIS.ORM.entity.chainS.report.ChainSalesStatisticReportItemVO;
import com.onlineMIS.ORM.entity.chainS.report.ChainWMRank;
import com.onlineMIS.ORM.entity.chainS.report.ChainWeeklySales;
import com.onlineMIS.ORM.entity.chainS.report.VIPReportVO;
import com.onlineMIS.ORM.entity.chainS.report.rptTemplate.ChainSalesReportTemplate;
import com.onlineMIS.ORM.entity.chainS.report.rptTemplate.ChainSalesReportVIPPercentageTemplate;
import com.onlineMIS.ORM.entity.chainS.report.rptTemplate.ChainSalesStatisticsReportTemplate;
import com.onlineMIS.ORM.entity.chainS.sales.ChainDailySales;
import com.onlineMIS.ORM.entity.chainS.sales.ChainStoreSalesOrder;
import com.onlineMIS.ORM.entity.chainS.sales.PurchaseOrderTemplate;
import com.onlineMIS.ORM.entity.chainS.user.ChainRoleType;
import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.chainS.user.ChainUserInfor;
import com.onlineMIS.ORM.entity.chainS.vip.ChainVIPCard;
import com.onlineMIS.ORM.entity.chainS.vip.ChainVIPPrepaidFlow;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Brand;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Category;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Color;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Product;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Quarter;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Year;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceCategory;
import com.onlineMIS.ORM.entity.headQ.finance.HeadQFinanceTrace;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrder;
import com.onlineMIS.action.chainS.report.ChainReportActionFormBean;
import com.onlineMIS.action.chainS.report.ChainReportActionUIBean;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.ExcelUtil;
import com.onlineMIS.common.loggerLocal;
import com.onlineMIS.filter.SystemParm;
import com.onlineMIS.sorter.ChainInventoryReportSort;
import com.sun.net.httpserver.Filter.Chain;

@Service
public class ChainReportService {
	private final int lezhixile = 213;
	@Autowired
	private ChainStoreService chainStoreService;
	
	@Autowired
	private ChainInOutStockDaoImpl chainInOutStockDaoImpl;
	
	@Autowired
	private ChainStoreSalesOrderDaoImpl chainSalesOrderDaoImpl;
	
	@Autowired
	private InventoryOrderDAOImpl inventoryOrderDAOImpl;
	
	@Autowired
	private InventoryOrderProductDAOImpl inventoryOrderProductDAOImpl;
	
	@Autowired
	private HeadQFinanceTraceImpl chainFinanceTraceImpl;
	
	@Autowired
	private FinanceCategoryImpl financeCategoryImpl;
	
	@Autowired
	private ChainUserInforDaoImpl chainUserInforDaoImpl;
	
	@Autowired
	private YearDaoImpl yearDaoImpl;
	
	@Autowired
	private ChainStoreDaoImpl chainStoreDaoImpl;
	
	@Autowired
	private QuarterDaoImpl quarterDaoImpl;
	
	@Autowired
	private BrandDaoImpl brandDaoImpl;
	
	@Autowired
	private ProductBarcodeDaoImpl productBarcodeDaoImpl;
	
	@Autowired
	private ChainWeeklySalesDaoImpl chainWeeklySalesDaoImpl;
	
	@Autowired
	private ProductDaoImpl productDaoImpl;
	
	@Autowired
	private ChainVIPCardImpl chainVIPCardImpl;
	
	@Autowired
	private ChainVIPPrepaidImpl chainVIPPrepaidImpl;
	
	@Autowired
	private ChainBatchRptRepositotyDaoImpl chainAutoRptRepositoryDaoImpl;
	
	@Autowired
	private ChainDailySalesDaoImpl chainDailySalesDaoImpl;
	
	/**
	 * to prepare the generate the report UI
	 * @param uiBean
	 * @param loginUser
	 */
	public void prepareGenReportUI(ChainReportActionFormBean formBean,ChainReportActionUIBean uiBean, ChainUserInfor loginUser){
		if (!ChainUserInforService.isMgmtFromHQ(loginUser)){
			ChainStore chainStore = chainStoreService.getChainStoreByID(loginUser.getMyChainStore().getChain_id());
			formBean.setChainStore(chainStore);
		} 
		
	}
	
	/**
	 * 准备按照员工分类的销售报表
	 * @param formBean
	 * @param uiBean
	 * @param userInfor
	 */
	public void prepareGenSalesReportBySalerUI(
			ChainReportActionFormBean formBean, ChainReportActionUIBean uiBean,
			ChainUserInfor loginUser) {
//		uiBean.setChainStores(chainStoreService.getChainStoreList(loginUser));
		
		if (!ChainUserInforService.isMgmtFromHQ(loginUser)){
			ChainStore chainStore = chainStoreService.getChainStoreByID(loginUser.getMyChainStore().getChain_id());
			formBean.setChainStore(chainStore);
		}
	}

	/**
	 * to generate the report
	 * @param formBean
	 */
	public Response generateChainReport(ChainReportActionFormBean formBean) {
		Response response = new Response();
		
		int chainId = formBean.getChainStore().getChain_id();
		int salerId = formBean.getSaler().getUser_id();
		Date startDate = Common_util.formStartDate(formBean.getStartDate());
		Date endDate = Common_util.formEndDate(formBean.getEndDate());
		int reportType = formBean.getReportType();
		
		ChainReport report = new ChainReport();
		report.setChainStore(formBean.getChainStore());
		
		/**
		 * 销售报表, 采购报表，财务报表
		 * type 1 : 直接表达出来，比如只求某个连锁店
		 *      2   ： 求销售的总数
		 */
		if (reportType == ChainReport.TYPE_SALES_REPORT){
			
			ChainSalesReport salesReport = generateSalesTotal(chainId, salerId, startDate, endDate, false);
			response.setReturnValue(salesReport);
			response.setAction(1);
		} else if (reportType == ChainReport.TYPE_PURCHASE_REPORT){
			int clientId = chainStoreService.getChainStoreByID(chainId).getClient_id();
			report = generatePurchaseReport(clientId, startDate, endDate);
			response.setReturnValue(report);
			response.setAction(1);
		} else if (reportType == ChainReport.TYPE_FINANCE_REPORT){
			int clientId = chainStoreService.getChainStoreByID(chainId).getClient_id();
			report = generateFinanceReport(clientId, startDate, endDate);
			response.setReturnValue(report);
			response.setAction(1);
		}
		
		return response;
	}

	/**
	 * to generate the 财务报表
	 * @param chainId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ChainReport generateFinanceReport(int clientId, Date startDate,
			Date endDate) {
		List<ChainFinanceReportItem> reportItems = new ArrayList<ChainFinanceReportItem>();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(HeadQFinanceTrace.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.groupProperty("categoryId"));
		projList.add(Projections.sum("amount"));
		criteria.setProjection(projList);
		
		criteria.add(Restrictions.between("date", startDate, endDate));
		if (clientId != 0){
			criteria.add(Restrictions.eq("clientId", clientId));
		}
		
		List<Object> result = chainFinanceTraceImpl.getByCriteriaProjection(criteria,  false);
		
		Map<Integer, FinanceCategory> categoryMap = financeCategoryImpl.getFinanceCategoryMap();
		
		for (int i = 0; i < result.size(); i++){
			  Object object = result.get(i);
			  if (object != null){
				 Object[] recordResult = (Object[])object;
				 int categoryId = Common_util.getInt(recordResult[0]);
				 double amount = Common_util.getDouble(recordResult[1]);
				 
					FinanceCategory category = categoryMap.get(categoryId);
					if (category != null){
						ChainFinanceReportItem reportItem = new ChainFinanceReportItem(category.getItemName(), amount);
						reportItems.add(reportItem);
					}
			  }
		}
		
		ChainFinanceReport financeReport = new ChainFinanceReport();
		financeReport.setReportItems(reportItems);
		financeReport.setType(ChainReport.TYPE_FINANCE_REPORT);
		
		return financeReport;
	}

	/**
	 * to generate the purchase report 采购报表
	 * @param clientId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private ChainReport generatePurchaseReport(int clientId, Date startDate,
			Date endDate) {
		Object[] valuesPurchase = new Object[]{startDate, endDate, InventoryOrder.STATUS_ACCOUNT_COMPLETE,clientId, InventoryOrder.TYPE_SALES_ORDER_W};
		Object[] valuesReturn = new Object[]{startDate, endDate, InventoryOrder.STATUS_ACCOUNT_COMPLETE,clientId, InventoryOrder.TYPE_SALES_RETURN_ORDER_W};
		
		String hql_purchase = "select sum(totalQuantity), sum(totalWholePrice) from InventoryOrder where order_EndTime between ? and ? and order_Status = ? and client_id=? and order_type =?";
		String hql_return = "select sum(totalQuantity), sum(totalWholePrice) from InventoryOrder where order_EndTime between ? and ? and order_Status = ? and client_id=? and order_type =?";
		
		Object[] purchase =  (Object[])inventoryOrderDAOImpl.executeHQLSelect(hql_purchase, valuesPurchase,null, false).get(0);
		Object[] returns = (Object[])inventoryOrderDAOImpl.executeHQLSelect(hql_return, valuesReturn,null, false).get(0);
		
		int purchaseQ = Common_util.getInt(purchase[0]);
		int returnQ = Common_util.getInt(returns[0]);
		double purchaseP = Common_util.getDouble(purchase[1]);
		double returnP = Common_util.getDouble(returns[1]);
		
		ChainPurchaseReport purchaseReport = new ChainPurchaseReport(ChainReport.TYPE_PURCHASE_REPORT, purchaseQ, returnQ, purchaseP, returnP);

		return purchaseReport;
	}
	
	/**
	 * 连锁店总部获取信息
	 * @param formBean
	 * @return
	 */
	public Response generateSalesReport(ChainReportActionFormBean formBean, Integer page, Integer rowPerPage, String sortName, String sortOrder) {
		Response response = new Response();
		
		int chainId = formBean.getChainStore().getChain_id();
		Date startDate = Common_util.formStartDate(formBean.getStartDate());
		Date endDate = Common_util.formEndDate(formBean.getEndDate());
		int salerId = Common_util.ALL_RECORD;
		
		ChainReport report = new ChainReport();
		report.setChainStore(formBean.getChainStore());
		
		Map data = new HashMap<String, Object>();
		
		try {
			generateSalesReportByHQ(data, chainId, salerId, startDate, endDate, page, rowPerPage, sortName, sortOrder);
			response.setReturnCode(Response.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			response.setReturnCode(Response.FAIL);
		}
		response.setReturnValue(data);
		
		return response;
	}
	
	/**
	 * 获取sales report的总数,或者连锁店获取自己的总数
	 * @param chainId
	 * @param salerId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private ChainSalesReport generateSalesTotal(int chainId, int salerId, Date startDate, Date endDate, boolean skipTotalPrepaid){
		/**1. to get the information from the sales order and exchange order
		 * 收入, 销售额， 销售总量，退货额，退货总量,,
		 */
		Object[] value_sale = new Object[]{startDate, endDate, ChainStoreSalesOrder.STATUS_COMPLETE};
		String chainCriteria = "";
		String chainCriteriaPrepaid = "";
		
		if (chainId == Common_util.ALL_RECORD) {
			chainCriteria = " chainStore.chain_id <> " + SystemParm.getTestChainId() + " AND chainStore.chain_id <> " + lezhixile;
			chainCriteriaPrepaid = " c.chainStore.chain_id <> " + SystemParm.getTestChainId()  + " AND chainStore.chain_id <> " + lezhixile;
		} else {
			chainCriteria = " chainStore.chain_id = " + chainId;
			chainCriteriaPrepaid = " c.chainStore.chain_id = " + chainId;
		}
		
		
		if (salerId != Common_util.ALL_RECORD){
			chainCriteria += " AND saler.user_id = " + salerId;
			chainCriteriaPrepaid += " AND c.operator.user_id = " + salerId;
		}

		String hql_sale = "select sum(totalQuantity), sum(netAmount),  sum(totalQuantityR), " +
				"sum(netAmountR), sum(totalCost), sum(totalQuantityF) ,sum(totalCostF), sum(discountAmount), " +
				"sum(coupon), sum (cardAmount), sum(cashAmount - returnAmount), sum(vipScore), sum(totalAmount - netAmount), sum(qxQuantity), sum(qxAmount), sum(qxCost), sum(myQuantity), sum(myAmount), sum(myCost), sum(chainPrepaidAmt), sum(wechatAmount), sum(alipayAmount) from ChainStoreSalesOrder where orderDate between ? and ? and status = ? and " + chainCriteria;

		Object[] sales = (Object[])chainSalesOrderDaoImpl.executeHQLSelect(hql_sale, value_sale,null, true).get(0);

		int totalQ = Common_util.getInt(sales[0]);
		double netAmt = Common_util.getDouble(sales[1]);
		int totalQR = Common_util.getInt(sales[2]);
		double netAmtR = Common_util.getDouble(sales[3]);
		double totalCost = Common_util.getDouble(sales[4]);
		int totalQF = Common_util.getInt(sales[5]);
		double totalCostF = Common_util.getDouble(sales[6]);
		double discountAmt = Common_util.getDouble(sales[7]);
		double coupon = Common_util.getDouble(sales[8]);
		double cardAmt = Common_util.getDouble(sales[9]);
		double cashAmt = Common_util.getDouble(sales[10]);
		double vipScoreAmt = Common_util.getDouble(sales[11]);
		double totalSalesDiscountAmt = Common_util.getDouble(sales[12]);
		int qxQuantity = Common_util.getInt(sales[13]);
		double qxAmount = Common_util.getDouble(sales[14]);
		double qxCost = Common_util.getDouble(sales[15]);
		int myQuantity = Common_util.getInt(sales[16]);
		double myAmount = Common_util.getDouble(sales[17]);
		double myCost = Common_util.getDouble(sales[18]);
		double vipPrepaidAmt = Common_util.getDouble(sales[19]);
		double wechatAmt = Common_util.getDouble(sales[20]);
		double alipayAmt = Common_util.getDouble(sales[21]);
		
		//2. 计算vip的数量
		String hql_sale_vipString = "SELECT SUM(totalQuantity - totalQuantityR), SUM(netAmount - netAmountR)   FROM ChainStoreSalesOrder WHERE orderDate between ? and ? and status = ? AND vipCard IS NOT NULL AND " + chainCriteria; 
		Object[] salesVip = (Object[])chainSalesOrderDaoImpl.executeHQLSelect(hql_sale_vipString, value_sale,null, true).get(0);
		int vipQ = Common_util.getInt(salesVip[0]);
		double vipAmt = Common_util.getDouble(salesVip[1]);
		
		//3。 计算 vip prepaid desposit
		double vipPrepaidDepositCash = 0;
		double vipPrepaidDepositCard = 0;

		String hql = "SELECT c.depositType, sum(amount) FROM ChainVIPPrepaidFlow c WHERE c.operationType = ? AND c.status="+ ChainVIPPrepaidFlow.STATUS_SUCCESS +" AND "+ chainCriteriaPrepaid +" AND c.dateD BETWEEN ? AND ? GROUP BY c.depositType";
	    Object[] values = new Object[]{ChainVIPPrepaidFlow.OPERATION_TYPE_DEPOSIT, startDate, endDate };
	    List<Object> prepaid = chainVIPPrepaidImpl.executeHQLSelect(hql, values,null, true);
	    
	    if (prepaid != null && prepaid.size() > 0)
		  for (Object object: prepaid){
			  Object[] object2 = (Object[])object;
			  if (object2[0] == null || object2[1] == null)
				  continue;
			  String depositType = object2[0].toString();
			  double amount = Common_util.getDouble(object2[1]);

			  if (depositType.equalsIgnoreCase(ChainVIPPrepaidFlow.DEPOSIT_TYPE_CARD))
				  vipPrepaidDepositCard += amount;
			  else if (depositType.equalsIgnoreCase(ChainVIPPrepaidFlow.DEPOSIT_TYPE_CASH))
				  vipPrepaidDepositCash += amount;
		   }
	    
	    //4. 计算累计还有多少预存款
	    double prepaidTotal = 0;
	    if (!skipTotalPrepaid){
	    	String hqlPreaidSum = "SELECT sum(calculatedAmt) FROM ChainVIPPrepaidFlow c WHERE c.status="+ ChainVIPPrepaidFlow.STATUS_SUCCESS + " AND " + chainCriteriaPrepaid;
	    	List<Object> prepaidTotalObj = chainVIPPrepaidImpl.executeHQLSelect(hqlPreaidSum, null, null, true);
			for (Object record : prepaidTotalObj){
				prepaidTotal = Common_util.getDouble(record);
			}
	    }

		
		ChainStore chainStore = new ChainStore();
		chainStore.setChain_id(chainId);
		ChainSalesReport chainReport = new ChainSalesReport(ChainReport.TYPE_SALES_REPORT, totalQ, totalQR,
				totalQF, netAmt,totalSalesDiscountAmt, netAmtR,totalCost, totalCostF, discountAmt,
				coupon, cardAmt, cashAmt, vipScoreAmt, qxQuantity,qxAmount, qxCost, myQuantity, myAmount, myCost,vipQ, vipAmt,vipPrepaidAmt,vipPrepaidDepositCash, vipPrepaidDepositCard,prepaidTotal, wechatAmt, alipayAmt);
		chainReport.setChainStore(chainStore);
		
		return chainReport;
	}
	
	/**
	 * 计算sale report 分页
	 * @param chainId
	 * @param salerId
	 * @param startDate
	 * @param endDate
	 * @param pager
	 */
	private int calculateSaleReportCount(int chainId, Object[] value_sale){
		//2.1 计算pager
		String criteria = "";
		if (chainId == Common_util.ALL_RECORD)
			criteria = " FROM ChainStoreSalesOrder WHERE orderDate BETWEEN ? AND ? AND status = ?";
		else 
			criteria = "FROM ChainStoreSalesOrder WHERE orderDate BETWEEN ? AND ? AND status = ? AND chainStore.chain_id = " + chainId;

		String criteria2 = "SELECT COUNT(DISTINCT chainStore.chain_id) " + criteria;
		    
	    return chainSalesOrderDaoImpl.executeHQLCount(criteria2, value_sale, true);
	}


	/**
	 * to generate the sales report 销售报表
	 * @param chainId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private void generateSalesReportByHQ(Map saleReport, int chainId, int salerId, Date startDate,
			Date endDate, Integer page, Integer rowPerPage, String sortName, String sortOrder) {
		/**
		 * 1. 获取total
		 */
		Object[] value_sale = new Object[]{startDate, endDate, ChainStoreSalesOrder.STATUS_COMPLETE};
		ChainSalesReport totalReport = generateSalesTotal(chainId, salerId, startDate, endDate, true);
		
		/**
		 * 2. 实现分页,如果是搜索所有连锁店
		 */
		int total = 0;
		if (page != null && rowPerPage != null)
			total = calculateSaleReportCount(chainId, value_sale);
		
		/**
		 * 获取数据列表
		 */
		List<Integer> chainIdList = new ArrayList<Integer>(); 
		List<ChainSalesReport> reports = new ArrayList<ChainSalesReport>();
		String criteria = "";
		if (chainId == Common_util.ALL_RECORD)
			criteria = " FROM ChainStoreSalesOrder WHERE orderDate BETWEEN ? AND ? AND status = ?";
		else 
			criteria = "FROM ChainStoreSalesOrder WHERE orderDate BETWEEN ? AND ? AND status = ? AND chainStore.chain_id = " + chainId;

		criteria += " GROUP BY chainStore.chain_id ";
		
		String orderBy = generateSalesOrderBy(sortName, sortOrder);
		
		criteria += orderBy;
		
		String hql_sale2 = "SELECT sum(totalQuantity), sum(netAmount), sum(totalQuantityR), " +
				"sum(netAmountR), sum(totalCost), sum(totalQuantityF) ,sum(totalCostF), sum(discountAmount), " +
				"sum(coupon), sum (cardAmount), sum(cashAmount - returnAmount), sum(vipScore), chainStore.chain_id, sum(totalAmount - netAmount), sum(qxQuantity), sum(qxAmount), sum(qxCost), sum(myQuantity), sum(myAmount), sum(myCost),sum(chainPrepaidAmt), sum(wechatAmount), sum(alipayAmount)  " + criteria;

		Integer[] pagerArray = null;
		if (page != null && rowPerPage != null)
		      pagerArray = new Integer[]{Common_util.getFirstRecord(page, rowPerPage), rowPerPage};
		
		List<Object> sales2 = (List<Object>)chainSalesOrderDaoImpl.executeHQLSelect(hql_sale2, value_sale,pagerArray, true);

		for (Object resultObject : sales2){
			Object[] sales3 = (Object[])resultObject;
			int totalQ2 = Common_util.getInt(sales3[0]);
			double netAmt2 = Common_util.getDouble(sales3[1]);
			int totalQR2 = Common_util.getInt(sales3[2]);
			double netAmtR2 = Common_util.getDouble(sales3[3]);
			double totalCost2 = Common_util.getDouble(sales3[4]);
			int totalQF2 = Common_util.getInt(sales3[5]);
			double totalCostF2 = Common_util.getDouble(sales3[6]);
			double discountAmt2 = Common_util.getDouble(sales3[7]);
			double coupon2 = Common_util.getDouble(sales3[8]);
			double cardAmt2 = Common_util.getDouble(sales3[9]);
			double cashAmt2 = Common_util.getDouble(sales3[10]);
			double vipScoreAmt2 = Common_util.getDouble(sales3[11]);
			int chainStoreId =  Common_util.getInt(sales3[12]);
			chainIdList.add(chainStoreId);
			
			double totalSalesDiscountAmt2 = Common_util.getDouble(sales3[13]);
			int qxQuantity = Common_util.getInt(sales3[14]);
			double qxAmount = Common_util.getDouble(sales3[15]);
			double qxCost = Common_util.getDouble(sales3[16]);
			int myQuantity = Common_util.getInt(sales3[17]);
			double myAmount = Common_util.getDouble(sales3[18]);
			double myCost = Common_util.getDouble(sales3[19]);
			double vipPrepaidAmt = Common_util.getDouble(sales3[20]);
			double wechatAmt =  Common_util.getDouble(sales3[21]);
			double alipayAmt =  Common_util.getDouble(sales3[22]);
			
			ChainStore store = chainStoreDaoImpl.get(chainStoreId, true);
			
			ChainSalesReport chainReport2 = new ChainSalesReport(ChainReport.TYPE_SALES_REPORT, totalQ2, totalQR2,
					totalQF2, netAmt2,totalSalesDiscountAmt2, netAmtR2,totalCost2, totalCostF2, discountAmt2,
					coupon2, cardAmt2, cashAmt2, vipScoreAmt2, qxQuantity,qxAmount, qxCost, myQuantity, myAmount, myCost, 0, 0,vipPrepaidAmt,0,0,0,store,wechatAmt, alipayAmt);
			reports.add(chainReport2);
		}
		
		//4. 将prepaid deposit 计算出来
		Map<Integer, Double> prepaidMap = new HashMap<Integer, Double>();
		String chainCriteriaPrepaid = "";
		
		if (chainIdList.size() > 0){
			chainCriteriaPrepaid = " AND c.chainStore.chain_id IN (";
			for (int i = 0; i < chainIdList.size(); i++){
				if (i == chainIdList.size() -1)
					chainCriteriaPrepaid += chainIdList.get(i) + ")";
				else 
					chainCriteriaPrepaid += chainIdList.get(i) + ",";
			}
		}
		String hql = "SELECT c.chainStore.chain_id, sum(amount) FROM ChainVIPPrepaidFlow c WHERE c.operationType = ? AND c.status = "+ ChainVIPPrepaidFlow.STATUS_SUCCESS+" "+ chainCriteriaPrepaid +" AND c.dateD BETWEEN ? AND ? GROUP BY c.chainStore.chain_id";
	    Object[] values = new Object[]{ChainVIPPrepaidFlow.OPERATION_TYPE_DEPOSIT,startDate, endDate };
	    List<Object> prepaid = chainVIPPrepaidImpl.executeHQLSelect(hql, values,null, true);
	    
	    if (prepaid != null && prepaid.size() > 0)
		  for (Object object: prepaid){
			  Object[] object2 = (Object[])object;
			  if (object2[0] == null || object2[1] == null)
				  continue;
			  int chainIdPrepaid = Common_util.getInt(object2[0]);
			  Double amount = Common_util.getDouble(object2[1]);

			  prepaidMap.put(chainIdPrepaid, amount);
		   }
	    
	    //5. 把预付金放进去
	    int removeId = -1;
	    for (int i = 0; i < reports.size(); i++){
	    	ChainSalesReport rpt = reports.get(i);
	    	int chainIdRpt = rpt.getChainStore().getChain_id();
	    	
	    	//剔除乐至喜乐仓
	    	if (chainIdRpt == lezhixile){
	    		removeId = i;
	    	}
	    	
	    	Double prepaidAmt = prepaidMap.get(chainIdRpt);
	    	if (prepaidAmt == null)
	    		rpt.setVipPrepaidDepositCash(0);
	    	else
	    		rpt.setVipPrepaidDepositCash(prepaidAmt);
	    }
	    
	    if (removeId != -1)
	    	reports.remove(removeId);

		List<ChainSalesReport> footer = new ArrayList<ChainSalesReport>();
		footer.add(totalReport);
		saleReport.put("footer", footer);
		saleReport.put("rows", reports);
		saleReport.put("total", total);
	}
	
	/**
	 * 生成销售分析报表
	 * @param formBean
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	public Response generateSalesAnalysisReport(
			ChainReportActionFormBean formBean, int page, int rows,
			String sort, String order,ChainUserInfor userInfor) {
		Response response = new Response();
		/**
		 * 如果是非连锁店经理，只能在6点以后查询
		 */
//		if (!ChainUserInforService.isMgmtFromHQ(userInfor)){
//			Date currentDate = new Date();
//            int hr = currentDate.getHours();
//			if (hr < 18 && hr >9){
//				response.setReturnCode(Response.FAIL);
//				response.setMessage("因为数据收集和计算等原因，普通用户只能在晚上六点以后使用此功能");
//				return response;
//			}
//		}
//		
		
		int chainId = formBean.getChainStore().getChain_id();
		Date startDate = Common_util.formStartDate(formBean.getStartDate());
		Date endDate = Common_util.formEndDate(formBean.getEndDate());
		
		/**
		 * 1. 获取连锁店total
		 */
		Object[] value_sale = new Object[]{startDate, endDate, ChainStoreSalesOrder.STATUS_COMPLETE};
		ChainDailySalesAnalysis totalReport = generateSalesAnalysisTotal(chainId, value_sale);
		
		
		List<ChainDailySalesAnalysis> footer = new ArrayList<ChainDailySalesAnalysis>();
		footer.add(totalReport);
		
		List<ChainDailySalesAnalysis> reports = generateSalesAnalysis(chainId, value_sale);
		
		Map saleReport = new HashMap();
		saleReport.put("footer", footer);
		saleReport.put("rows", reports);
		saleReport.put("total", new Integer(1));
		
		
		
		response.setReturnValue(saleReport);
		response.setReturnCode(Response.SUCCESS);
		return response;
	}

	/**
	 * 生成连锁店销售分析报表的总计
	 * @param chainId
	 * @param value_sale
	 * @return
	 */
	private ChainDailySalesAnalysis generateSalesAnalysisTotal(int chainId,
			Object[] value_sale) {
		String chainCriteria = "";
		//1. 创建parameters
		String chainOneQuantityCritiera = "";
		chainOneQuantityCritiera = " chainStore.chain_id <> " + SystemParm.getTestChainId() + " AND totalQuantityA = 1 ";
		chainCriteria = " chainStore.chain_id <> " + SystemParm.getTestChainId() + " AND totalQuantityA > 0 AND totalQuantityA < " + Common_util.SALES_ANALYSIS_BAD_ORDER;

		String hql_sale_analysis = "select avg(totalQuantityA), max(totalQuantityA),  sum(netAmountA)/sum(totalQuantityA), count(id), chainStore.chain_id from ChainStoreSalesOrder where orderDate between ? and ? and status = ? and " + chainCriteria;
		String hql_sale_analysis_one = "select count(id), chainStore.chain_id from ChainStoreSalesOrder where orderDate between ? and ? and status = ? and " + chainOneQuantityCritiera;
		
		//2. 第一轮select
		List<Object> salesAnalysis = (List<Object>)chainSalesOrderDaoImpl.executeHQLSelect(hql_sale_analysis, value_sale,null, true);

		ChainStore store = ChainStoreDaoImpl.getAllChainStoreObject();
		store.setChain_name("所有店平均水平");
		ChainDailySalesAnalysis dailySalesAnalysis = new ChainDailySalesAnalysis(store, null);
		
		for (Object resultObject : salesAnalysis){
			Object[] sales = (Object[])resultObject;
			double liandai = Common_util.getDouble(sales[0]);
			int largestSalesQ = Common_util.getInt(sales[1]);		
			double kedan = Common_util.getDouble(sales[2]);
			int countArray = Common_util.getInt(sales[3]);
			
			if (countArray < Common_util.SALES_ANALYSIS_BAD_STORE)
				continue;

			dailySalesAnalysis.setKeDanAvg(kedan);
			dailySalesAnalysis.setLargestSalesQ(largestSalesQ);
			dailySalesAnalysis.setLianDaiRatio(liandai);
			
			//3. 第二轮select
			List<Object> salesAnalysis2 = (List<Object>)chainSalesOrderDaoImpl.executeHQLSelect(hql_sale_analysis_one, value_sale,null, true);
			for (Object resultObject2 : salesAnalysis2){
				Object[] sales2 = (Object[])resultObject2;
				int oneQCountArray = Common_util.getInt(sales2[0]);

				if (countArray != 0)
				    dailySalesAnalysis.setKeChiOrderRatio((double)oneQCountArray/countArray);
			}
		}
		
		return dailySalesAnalysis;
	}
	
	private List<ChainDailySalesAnalysis> generateSalesAnalysis(int chainId, Object[] value_sale){
		String chainCriteria = "";
		//单件量
		String chainOneQuantityCritiera = "";
		if (chainId == Common_util.ALL_RECORD){
			chainOneQuantityCritiera = " chainStore.chain_id <> " + SystemParm.getTestChainId() + " AND totalQuantityA = 1  GROUP BY chainStore.chain_id ";
			chainCriteria = " chainStore.chain_id <> " + SystemParm.getTestChainId() + " AND  totalQuantityA > 0  AND totalQuantityA < " + Common_util.SALES_ANALYSIS_BAD_ORDER +" GROUP BY chainStore.chain_id ORDER BY avg(totalQuantityA) DESC";
		} else {
			chainOneQuantityCritiera = " chainStore.chain_id = " + chainId + " AND totalQuantityA = 1";
			chainCriteria = " chainStore.chain_id = " + chainId + " AND totalQuantityA > 0  AND totalQuantityA < " + Common_util.SALES_ANALYSIS_BAD_ORDER;
		}
		

		String hql_sale_analysis = "select avg(totalQuantityA), max(totalQuantityA),  sum(netAmountA)/sum(totalQuantityA) , count(id), chainStore.chain_id from ChainStoreSalesOrder  where orderDate between ? and ? and status = ? and " + chainCriteria;
		String hql_sale_analysis_one = "select count(id), chainStore.chain_id from ChainStoreSalesOrder where orderDate between ? and ? and status = ? and " + chainOneQuantityCritiera;
		
		/**
		 * 1. 生成除开可耻率的数据
		 */
		List<Object> salesAnalysis = (List<Object>)chainSalesOrderDaoImpl.executeHQLSelect(hql_sale_analysis, value_sale,null, true);

		
		Map<Integer, ChainDailySalesAnalysis> salesAnalysisMap = new HashMap<Integer, ChainDailySalesAnalysis>();
		Map<Integer, Integer> orderNumMap= new HashMap<Integer, Integer>();
		int rank = 0;

		for (Object resultObject : salesAnalysis){
			rank++;
			Object[] sales = (Object[])resultObject;
			

			double liandai = Common_util.getDouble(sales[0]);
			int largestSalesQ = Common_util.getInt(sales[1]);		
			double kedan = Common_util.getDouble(sales[2]);
			int countArray = Common_util.getInt(sales[3]);
			int chainIdArray = Common_util.getInt(sales[4]);
			
			if (countArray < Common_util.SALES_ANALYSIS_BAD_STORE)
				continue;
			
			ChainStore store = chainStoreDaoImpl.get(chainIdArray, true);
			ChainDailySalesAnalysis dailySalesAnalysis = new ChainDailySalesAnalysis(store, null);
			

			dailySalesAnalysis.setKeDanAvg(kedan);
			dailySalesAnalysis.setLargestSalesQ(largestSalesQ);
			dailySalesAnalysis.setLianDaiRatio(liandai);
			dailySalesAnalysis.setRank(rank);
			dailySalesAnalysis.setKeChiOrderRatio(0);

			salesAnalysisMap.put(chainIdArray, dailySalesAnalysis);
			
			orderNumMap.put(chainIdArray, countArray);
		}
		
		/**
		 * 2. 生成可耻率数据
		 */
		List<Object> salesAnalysis2 = (List<Object>)chainSalesOrderDaoImpl.executeHQLSelect(hql_sale_analysis_one, value_sale,null, true);
		for (Object resultObject2 : salesAnalysis2){
			Object[] sales2 = (Object[])resultObject2;
			int oneQCountArray = Common_util.getInt(sales2[0]);
			int chainIdArray = Common_util.getInt(sales2[1]);

			ChainDailySalesAnalysis chainDailySalesAnalysis = salesAnalysisMap.get(chainIdArray);
			if (chainDailySalesAnalysis != null){
				int countArray = orderNumMap.get(chainIdArray);
				if (countArray != 0){
					chainDailySalesAnalysis.setKeChiOrderRatio((double)oneQCountArray/countArray);
					salesAnalysisMap.put(chainIdArray, chainDailySalesAnalysis);
				}
			}
		}
		
		List<ChainDailySalesAnalysis> salesAnalysises = new ArrayList<ChainDailySalesAnalysis>(salesAnalysisMap.values());
		
		Collections.sort(salesAnalysises, new Comparator<ChainDailySalesAnalysis>() {
			@Override
			public int compare(ChainDailySalesAnalysis arg0,
					ChainDailySalesAnalysis arg1) {
				return arg0.getRank() - arg1.getRank();
			}	
		});
		
		return salesAnalysises;
	}

	/**
	 * 创建order by
	 * @param sortName
	 * @param sortOrder
	 * @return
	 */
	private String generateSalesOrderBy(String sortName, String sortOrder) {
		String orderBy = " ORDER BY sum(netAmount - netAmountR) desc";
		if (sortName != null && sortOrder != null){
			if (sortName.equals("saleQuantitySum"))
				orderBy = " ORDER BY sum(totalQuantity) " + sortOrder;
			else if (sortName.equals("returnQuantitySum"))
				orderBy = " ORDER BY sum(totalQuantityR) " + sortOrder;
			else if (sortName.equals("netQuantitySum"))
				orderBy = " ORDER BY sum(totalQuantity - totalQuantityR) " + sortOrder;
			else if (sortName.equals("salesAmtSum"))
				orderBy = " ORDER BY sum(netAmount) " + sortOrder;
			else if (sortName.equals("returnAmtSum"))
				orderBy = " ORDER BY sum(netAmountR) " + sortOrder;
			else if (sortName.equals("netAmtSum"))
				orderBy = " ORDER BY sum(netAmount - netAmountR) " + sortOrder;
			else if (sortName.equals("netProfit"))
				orderBy = " ORDER BY sum(netAmount - netAmountR - totalCost - totalCostF - discountAmount) " + sortOrder;
			else if (sortName.equals("receiveAmtSum"))
				orderBy = " ORDER BY sum(cardAmount + cashAmount - returnAmount + alipayAmount + wechatAmount) " + sortOrder;
			else if (sortName.equals("qxAmount"))
				orderBy = " ORDER BY sum(qxAmount) " + sortOrder;
		}
		return orderBy;
	}

	/**
	 * 准备销售统计报表的UI element
	 * @param formBean
	 * @param uiBean
	 */
	public void prepareSalesStatisticReportUIBean(
			ChainReportActionFormBean formBean, ChainReportActionUIBean uiBean, ChainUserInfor userInfor) {
		//1. 检查chain store
		checkChainStoreUsers(formBean, uiBean, userInfor);
		
		//2. 构建year list
		List<Year> years = yearDaoImpl.getAll(true);
		uiBean.setYears(years);
		
		//3. 构建quarter list
		List<Quarter> quarters = quarterDaoImpl.getAll(true);
		uiBean.setQuarters(quarters);	
	}
	

	
	/**
	 * 准备采购统计报表ui
	 * @param formBean
	 * @param uiBean
	 * @param userInfor
	 */
	public void preparePurchaseStatisticReportUIBean(
			ChainReportActionFormBean formBean, ChainReportActionUIBean uiBean,
			ChainUserInfor userInfor) {
		//1. 检查chain store
		if (!ChainUserInforService.isMgmtFromHQ(userInfor)){
			int chainId = userInfor.getMyChainStore().getChain_id();
			ChainStore chainStore = chainStoreService.getChainStoreByID(chainId);
			formBean.setChainStore(chainStore);
		} else {
			ChainStore allChainStore = ChainStoreDaoImpl.getAllChainStoreObject();
			formBean.setChainStore(allChainStore);
		}
		
		//2. 构建year list
		List<Year> years = yearDaoImpl.getAll(true);
		uiBean.setYears(years);
		
		//3. 构建quarter list
		List<Quarter> quarters = quarterDaoImpl.getAll(true);
		uiBean.setQuarters(quarters);	
		
	}

	/**
	 * 准备销售报表的ui
	 * @param formBean
	 * @param uiBean
	 * @param userInfor
	 */
	public void prepareGenSalesReportUI(ChainReportActionFormBean formBean, ChainReportActionUIBean uiBean,
			ChainUserInfor userInfor) {
		//1. 检查chain store
		checkChainStoreUsers(formBean, uiBean, userInfor);
		
	}
	
	private void checkChainStoreUsers(ChainReportActionFormBean formBean, ChainReportActionUIBean uiBean,
			ChainUserInfor userInfor){
		if (!ChainUserInforService.isMgmtFromHQ(userInfor)){
			int chainId = userInfor.getMyChainStore().getChain_id();
			ChainStore chainStore = chainStoreService.getChainStoreByID(chainId);
			formBean.setChainStore(chainStore);
			
			List<ChainUserInfor> salers = new ArrayList<ChainUserInfor>();
			salers.addAll(chainUserInforDaoImpl.getActiveChainUsersByChainStore(chainId));
			uiBean.setChainSalers(salers);
		} else {
			ChainStore allChainStore = ChainStoreDaoImpl.getAllChainStoreObject();
			formBean.setChainStore(allChainStore);
		}
	}


	/**
	 * 本业务的方法，对于chainId=全部，需要另外处理
	 * @param chainId
	 * @return
	 */
	private ChainStore getThisChainStore(int chainId){
		ChainStore chainStore = new ChainStore();
		if (chainId == Common_util.ALL_RECORD)
			chainStore = chainStoreDaoImpl.getAllChainStoreObject();
		else 
			chainStore = chainStoreDaoImpl.get(chainId, true);
		return chainStore;
	}
	
	/**
	 * 本业务的方法，对于userId=全部，需要另外处理
	 * @param chainId
	 * @return
	 */	
	private ChainUserInfor getThisChainUserInfor(int userId){
		ChainUserInfor saler = new ChainUserInfor();

		if (userId == Common_util.ALL_RECORD)
			saler = chainUserInforDaoImpl.getAllChainUserObject();
		else 
			saler = chainUserInforDaoImpl.get(userId, true);
		
		return saler;
	}

	
	
	/**
	 * 获取登录排名信息
	 * @return
	 */
	@Transactional
	public Response getRank(ChainUserInfor userInfor){
		Response response = new Response();
		
		if (userInfor.getMyChainStore() == null || userInfor.getMyChainStore().getChain_id() == Common_util.ALL_RECORD){
			response.setFail("");
			return response;
		}
		
		int myChainId = userInfor.getMyChainStore().getChain_id();
		
		
		/**
		 * 我前周排名
		 */
		List<java.sql.Date> lastWeekDays = Common_util.getLastWeekDays();
		ChainWeeklySales weeklySale = chainWeeklySalesDaoImpl.getWeeklyRankById(lastWeekDays.get(0), myChainId);
		ChainWMRank weeklyRank = new ChainWMRank();
		if (weeklySale != null) {
			int salesQ = weeklySale.getNetSalesQuantity();
			int salesAmt = (int)weeklySale.getNetSalesAmount();
			int rank = weeklySale.getRank();
			String salesQS = String.valueOf(salesQ);
			String salesAmtS = String.valueOf(salesAmt);
	
			weeklyRank.setRank(rank);
			weeklyRank.setSaleQ(salesQS);
			weeklyRank.setSaleAmt(salesAmtS);
	
			weeklyRank.setStartDate(lastWeekDays.get(0));
			weeklyRank.setEndDate(lastWeekDays.get(6));
			weeklyRank.setWeekly(true);
		}
		
		/**
		 * 获取我日名次
		 */
		java.sql.Date lastDate = Common_util.getDate(-3);
		DetachedCriteria criteria2 = DetachedCriteria.forClass(ChainDailySales.class);
		criteria2.add(Restrictions.gt("reportDate", lastDate));
		criteria2.add(Restrictions.le("reportDate", Common_util.getToday()));
		criteria2.add(Restrictions.eq("chainStore.chain_id", myChainId));
			
		List<ChainDailySales> dailySales = chainDailySalesDaoImpl.getByCritera(criteria2, true);
		List<ChainWMRank> dailyRanks = new ArrayList<ChainWMRank>();
		
		if (dailySales != null && dailySales.size()>0){
			for (ChainDailySales dailySales2 : dailySales){
				ChainWMRank dailyRank = new ChainWMRank();

				int salesQ = dailySales2.getSalesQuantity();
				int salesAmt = (int)dailySales2.getSalesAmount();
				int rank = dailySales2.getRank();
				java.sql.Date date = dailySales2.getReportDate();

				dailyRank.setRank(rank);
				dailyRank.setSaleQ(String.valueOf(salesQ));
				dailyRank.setSaleAmt(String.valueOf(salesAmt));
				dailyRank.setRank(rank);
				dailyRank.setStartDate(date);
				
				dailyRanks.add(dailyRank);
			}
		}

		
		List<Object> returnValue = new ArrayList<Object>();
		returnValue.add(weeklyRank);
		returnValue.add(dailyRanks);
		
		response.setReturnValue(returnValue);
		response.setReturnCode(Response.SUCCESS);
		
		return response;
		
	}
	
	/**
	 * 
	 * @return
	 */
	private String constructChainSQL(String prefix,List<Object> value_sale){
		List<ChainStore> stores = chainStoreService.getAvailableClientChainstores();
		prefix += " IN (";
		if (stores != null && stores.size() > 0){
			for (int i = 0; i < stores.size();i++){
				ChainStore chainStore = stores.get(i);
				if (i == stores.size()-1)
				    prefix += chainStore.getClient_id() + ")";
				else 
					prefix += chainStore.getClient_id() + ",";
			}
			return prefix;
		} else 
			return null;
	}

	/**
	 * 创建销售报表到excel
	 * @param dataMap
	 * @param string
	 * @return
	 */
	public Map<String, Object> generateSalesRptExcel(Map dataMap, Map lastYearMap,String path, Date startDate, Date endDate) {
		Map<String,Object> returnMap=new HashMap<String, Object>();   

		ByteArrayInputStream byteArrayInputStream;   
		try {     
			HSSFWorkbook wb = null;   
			
			//to get the order information from database
			List<ChainSalesReport> rptEles = (List<ChainSalesReport>)dataMap.get("rows");
			List<ChainSalesReport> footer =  (List<ChainSalesReport>)dataMap.get("footer");
			
			ChainSalesReportTemplate rptTemplate = new ChainSalesReportTemplate(rptEles, lastYearMap, footer.get(0), startDate, endDate, path);
			wb = rptTemplate.process();

			ByteArrayOutputStream os = new ByteArrayOutputStream();   
			try {   
			    wb.write(os);   
			} catch (IOException e) {   
				loggerLocal.error(e);
		    }   
		    byte[] content = os.toByteArray();   
		    byteArrayInputStream = new ByteArrayInputStream(content);   
		    returnMap.put("stream", byteArrayInputStream);   
         
		    return returnMap;   
		 } catch (Exception ex) {   
			 loggerLocal.error(ex);
		 }   
		return null;   
	}

	/**
	 * 计算综合报表数据
	 * @param startDate
	 * @param endDate
	 * @param chainId
	 * @param yearId
	 * @param quarterId
	 * @param brandId
	 * @param pager
	 * @return
	 */
	public Response generateAllInOneReport(Date startDateSQL,
			Date endDateSQL, int chainId, int yearId, int quarterId,
			int brandId, Pager pager) {
		Date startDate = Common_util.formStartDate(startDateSQL);
		Date endDate = Common_util.formEndDate(endDateSQL);
		Map<String, Object> values = new HashMap<String, Object>();

		Response response = new Response();
		if (brandId != Common_util.ALL_RECORD && brandId > 0){
			values.put("type", ChainPurchaseStatisReportItem.LEVEL_FOUR);
			generateAllInOneReportLevelFour(startDate, endDate, chainId,yearId, quarterId, brandId,values, pager);
		} else if (quarterId != Common_util.ALL_RECORD && quarterId >0){
			values.put("type", ChainPurchaseStatisReportItem.LEVEL_THREE);
			generateAllInOneReportLevelThree(startDate, endDate, chainId,yearId, quarterId,values, pager);
		} else if (yearId != Common_util.ALL_RECORD && yearId >0){
			values.put("type", ChainPurchaseStatisReportItem.LEVEL_TWO);
			generateAllInOneReportLevelTwo(startDate, endDate, chainId,yearId,values);
		} else if (yearId == Common_util.ALL_RECORD && quarterId == Common_util.ALL_RECORD && brandId == Common_util.ALL_RECORD){
			values.put("type", ChainPurchaseStatisReportItem.LEVEL_ONE);
			generateAllInOneReportLevelOne(startDate, endDate, chainId,values);
		} else 
			response.setQuickValue(Response.FAIL, "需求信息不充分，无法完成操作");

		response.setReturnValue(values);
		
		return response;
	}

	/**
	 * 生成综合报表level one数据
	 * @param startDate
	 * @param endDate
	 * @param chainId
	 * @param result
	 * @return
	 */
	private void generateAllInOneReportLevelOne(Date startDate,
			Date endDate, int chainId, Map<String, Object> result) {
		//@1. 生成销售数据
		List<Object> value_sale = new ArrayList<Object>();
		value_sale.add(ChainStoreSalesOrder.STATUS_COMPLETE);
		value_sale.add(startDate);
		value_sale.add(endDate);
		
        //@2.1 组成total not count inventory
        String notCoundBrandId = "(" + Brand.BRAND_NOT_COUNT_INVENTORY[0] + "," + Brand.BRAND_NOT_COUNT_INVENTORY[1]+")";
		
		String sql = "SELECT SUM(quantity), csp.productBarcode.product.year.year_ID, csp.type  FROM ChainStoreSalesOrderProduct csp " + 
		         " WHERE csp.chainSalesOrder.status = ? AND csp.chainSalesOrder.orderDate BETWEEN ? AND ? ";
		String sql_sales_total = "SELECT SUM(quantity), csp.type  FROM ChainStoreSalesOrderProduct csp " + 
		         " WHERE csp.chainSalesOrder.status = ? AND csp.chainSalesOrder.orderDate BETWEEN ? AND ? ";

		if (chainId != Common_util.ALL_RECORD){
			sql += "AND csp.chainSalesOrder.chainStore.chain_id = ? ";
			sql_sales_total += "AND csp.chainSalesOrder.chainStore.chain_id = ? ";
			value_sale.add(chainId);
		} else { 
			sql += "AND csp.chainSalesOrder.chainStore.chain_id <> ? ";
			sql_sales_total += "AND csp.chainSalesOrder.chainStore.chain_id <> ? ";
			value_sale.add(SystemParm.getTestChainId());
		}
		sql += " GROUP BY csp.productBarcode.product.year.year_ID, csp.type ";
		sql_sales_total += " GROUP BY csp.type ";
		
		List<Object> salesResult = chainSalesOrderDaoImpl.executeHQLSelect(sql, value_sale.toArray(), null, true);
		
		//@2. 生成销售总数
		List<Object> salesTotalResult = chainSalesOrderDaoImpl.executeHQLSelect(sql_sales_total, value_sale.toArray(), null, true);
		
		//@3. 生成采购数据
		List<Object> value_sale2 = new ArrayList<Object>();
		value_sale2.add(InventoryOrder.STATUS_ACCOUNT_COMPLETE);
		value_sale2.add(startDate);
		value_sale2.add(endDate);
		
		String sql2 = "SELECT SUM(quantity), p.productBarcode.product.year.year_ID, p.order.order_type FROM InventoryOrderProduct p " + 
		         " WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ? AND p.productBarcode.product.brand.brand_ID NOT IN " + notCoundBrandId;
		String sql_purchase_total = "SELECT SUM(quantity), p.order.order_type FROM InventoryOrderProduct p " + 
		         " WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ? AND p.productBarcode.product.brand.brand_ID NOT IN " + notCoundBrandId;

		if (chainId != Common_util.ALL_RECORD){
			sql2 += "AND p.order.client_id = ? ";
			sql_purchase_total += "AND p.order.client_id = ? ";
			ChainStore chainStore = chainStoreDaoImpl.get(chainId, true);
			
			if (chainStore != null)
				value_sale2.add(chainStore.getClient_id());
			else 
				value_sale2.add(Common_util.ALL_RECORD);
		} else { 
			String chainSQL = constructChainSQL("p.order.client_id", value_sale2);
			if (chainSQL != null ){
			    sql2 += "AND " + chainSQL;
			    sql_purchase_total += "AND " + chainSQL;
			}
		}
		sql2 += " GROUP BY p.productBarcode.product.year.year_ID, p.order.order_type";
		sql_purchase_total += " GROUP BY p.order.order_type";
		List<Object> purchaseResult = inventoryOrderProductDAOImpl.executeHQLSelect(sql2, value_sale2.toArray(), null, true);

		//@4. 生成采购总数
		List<Object> purchaseTotalResult = inventoryOrderProductDAOImpl.executeHQLSelect(sql_purchase_total, value_sale2.toArray(), null, true);

		//@5. 生成库存数据
		String chainCriteria = "";
		ChainStore chainStore = chainStoreDaoImpl.get(chainId, true);
		if (chainId == Common_util.ALL_RECORD){
			chainCriteria = " c.clientId <> " + SystemParm.getTestClientId();
		}else{
			chainCriteria = " c.clientId = " + chainStore.getClient_id();	
		}
		String sql_in_out = "SELECT c.productBarcode.product.year.year_ID, SUM(c.quantity) FROM ChainInOutStock c WHERE "+ chainCriteria +" AND c.productBarcode.product.brand.brand_ID <>? AND c.productBarcode.product.brand.brand_ID <>? GROUP BY c.productBarcode.product.year.year_ID";
        Object[] values = Brand.BRAND_NOT_COUNT_INVENTORY;
		List<Object> inventory = chainInOutStockDaoImpl.executeHQLSelect(sql_in_out, values,null,true);
		
		//@6. 生成库存总数
		String sql_in_out_total = "SELECT SUM(c.quantity) FROM ChainInOutStock c WHERE "+ chainCriteria +" AND c.productBarcode.product.brand.brand_ID <>? AND c.productBarcode.product.brand.brand_ID <>?";
		List<Object> inventory_total = chainInOutStockDaoImpl.executeHQLSelect(sql_in_out_total, values,null,true);
		
		setAllInOneLevelOneItem(salesResult, purchaseResult, salesTotalResult, purchaseTotalResult, inventory, inventory_total, result, chainId, startDate, endDate);
	}



	private  void generateAllInOneReportLevelTwo(Date startDate,
			Date endDate, int chainId, int yearId, Map<String, Object> result) {
		//@1. 生成销售数据
		List<Object> value_sale = new ArrayList<Object>();
		value_sale.add(ChainStoreSalesOrder.STATUS_COMPLETE);
		value_sale.add(startDate);
		value_sale.add(endDate);
		value_sale.add(yearId);
		
        //@2.1 组成total not count inventory
        String notCoundBrandId = "(" + Brand.BRAND_NOT_COUNT_INVENTORY[0] + "," + Brand.BRAND_NOT_COUNT_INVENTORY[1]+")";
		
		String sql = "SELECT SUM(quantity), csp.productBarcode.product.quarter.quarter_ID, csp.type  FROM ChainStoreSalesOrderProduct csp " + 
		         " WHERE csp.chainSalesOrder.status = ? AND csp.chainSalesOrder.orderDate BETWEEN ? AND ? AND csp.productBarcode.product.year.year_ID=?";
		String sql_sales_total = "SELECT SUM(quantity), csp.type  FROM ChainStoreSalesOrderProduct csp " + 
		         " WHERE csp.chainSalesOrder.status = ? AND csp.chainSalesOrder.orderDate BETWEEN ? AND ? AND csp.productBarcode.product.year.year_ID=?";

		if (chainId != Common_util.ALL_RECORD){
			sql += "AND csp.chainSalesOrder.chainStore.chain_id = ? ";
			sql_sales_total += "AND csp.chainSalesOrder.chainStore.chain_id = ? ";
			value_sale.add(chainId);
		} else { 
			sql += "AND csp.chainSalesOrder.chainStore.chain_id <> ? ";
			sql_sales_total += "AND csp.chainSalesOrder.chainStore.chain_id <> ? ";
			value_sale.add(SystemParm.getTestChainId());
		}
		sql += " GROUP BY csp.productBarcode.product.quarter.quarter_ID, csp.type ";
		sql_sales_total += " GROUP BY csp.type ";
		
		List<Object> salesResult = chainSalesOrderDaoImpl.executeHQLSelect(sql, value_sale.toArray(), null, true);
		
		//@2. 生成销售总数
		List<Object> salesTotalResult = chainSalesOrderDaoImpl.executeHQLSelect(sql_sales_total, value_sale.toArray(), null, true);
		
		//@3. 生成采购数据
		List<Object> value_sale2 = new ArrayList<Object>();
		value_sale2.add(InventoryOrder.STATUS_ACCOUNT_COMPLETE);
		value_sale2.add(startDate);
		value_sale2.add(endDate);
		value_sale2.add(yearId);
		
		String sql2 = "SELECT SUM(quantity), p.productBarcode.product.quarter.quarter_ID, p.order.order_type FROM InventoryOrderProduct p " + 
		         " WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ? AND p.productBarcode.product.year.year_ID=? AND p.productBarcode.product.brand.brand_ID NOT IN " + notCoundBrandId;
		String sql_purchase_total = "SELECT SUM(quantity), p.order.order_type FROM InventoryOrderProduct p " + 
		         " WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ? AND p.productBarcode.product.year.year_ID=? AND p.productBarcode.product.brand.brand_ID NOT IN " + notCoundBrandId;

		if (chainId != Common_util.ALL_RECORD){
			sql2 += "AND p.order.client_id = ? ";
			sql_purchase_total += "AND p.order.client_id = ? ";
			ChainStore chainStore = chainStoreDaoImpl.get(chainId, true);
			
			if (chainStore != null)
				value_sale2.add(chainStore.getClient_id());
			else 
				value_sale2.add(Common_util.ALL_RECORD);
		} else { 
			String chainSQL = constructChainSQL("p.order.client_id", value_sale2);
			if (chainSQL != null ){
			    sql2 += "AND " + chainSQL;
			    sql_purchase_total += "AND " + chainSQL;
			}
		}
		sql2 += " GROUP BY p.productBarcode.product.quarter.quarter_ID, p.order.order_type";
		sql_purchase_total += " GROUP BY p.order.order_type";
		List<Object> purchaseResult = inventoryOrderProductDAOImpl.executeHQLSelect(sql2, value_sale2.toArray(), null, true);

		//@4. 生成采购总数
		List<Object> purchaseTotalResult = inventoryOrderProductDAOImpl.executeHQLSelect(sql_purchase_total, value_sale2.toArray(), null, true);

		//@5. 生成库存数据
		String chainCriteria = "";
		ChainStore chainStore = chainStoreDaoImpl.get(chainId, true);
		if (chainId == Common_util.ALL_RECORD){
			chainCriteria = " c.clientId <> " + SystemParm.getTestClientId();
		}else{
			chainCriteria = " c.clientId = " + chainStore.getClient_id();	
		}
		String sql_in_out = "SELECT c.productBarcode.product.quarter.quarter_ID, SUM(c.quantity) FROM ChainInOutStock c WHERE "+ chainCriteria +" AND c.productBarcode.product.brand.brand_ID <>? AND c.productBarcode.product.brand.brand_ID <>? AND  c.productBarcode.product.year.year_ID=? GROUP BY c.productBarcode.product.quarter.quarter_ID";
        Object[] values = new Object[]{Brand.BRAND_NOT_COUNT_INVENTORY[0], Brand.BRAND_NOT_COUNT_INVENTORY[1],yearId};
	    
		List<Object> inventory = chainInOutStockDaoImpl.executeHQLSelect(sql_in_out, values,null,true);
		
		//@6. 生成库存总数
		String sql_in_out_total = "SELECT SUM(c.quantity) FROM ChainInOutStock c WHERE "+ chainCriteria +" AND c.productBarcode.product.brand.brand_ID <>? AND c.productBarcode.product.brand.brand_ID <>? AND  c.productBarcode.product.year.year_ID=?";
		List<Object> inventory_total = chainInOutStockDaoImpl.executeHQLSelect(sql_in_out_total, values,null,true);
		
		setAllInOneLevelTwoItem(salesResult, purchaseResult, salesTotalResult, purchaseTotalResult, inventory, inventory_total, result, chainId, yearId, startDate, endDate);
	}

	
	private void generateAllInOneReportLevelThree(Date startDate,
			Date endDate, int chainId, int yearId, int quarterId,
			Map<String, Object> result, Pager pager) {
		//@1. calculate the pager
		if (pager.getTotalResult() == 0){
			int totalBrand = productDaoImpl.getNumOfBrandsUnderYQ(yearId, quarterId);
			pager.initialize(totalBrand);
		} else {
			pager.calFirstResult();
		}
		
		//@1.1 组成sql parameters
		List<Object> value_sale = new ArrayList<Object>();
		value_sale.add(ChainStoreSalesOrder.STATUS_COMPLETE);
		value_sale.add(startDate);
		value_sale.add(endDate);
		value_sale.add(yearId);
		value_sale.add(quarterId);
		
		List<Object> value_purchase = new ArrayList<Object>();
		value_purchase.add(InventoryOrder.STATUS_ACCOUNT_COMPLETE);
		value_purchase.add(startDate);
		value_purchase.add(endDate);
		value_purchase.add(yearId);
		value_purchase.add(quarterId);
		
        List<Object> value_inventory = new ArrayList<Object>();
        value_inventory.add(yearId);
        value_inventory.add(quarterId);
        
        //@2.1 组成total not count inventory
        String notCoundBrandId = "(" + Brand.BRAND_NOT_COUNT_INVENTORY[0] + "," + Brand.BRAND_NOT_COUNT_INVENTORY[1]+")";
		
		//@2. 获取这页的brand ids
		List<Object> brandIds = productDaoImpl.getBrandIdsUnderYQ(yearId, quarterId, pager.getFirstResult(), pager.getRecordPerPage());
		String brandIdSQL = "(";
		if (brandIds.size() != 0)
			for (int i = 0; i < brandIds.size(); i++){
				Object brandId = brandIds.get(i);
				brandIdSQL += brandId;
				if (i == brandIds.size() -1)
					brandIdSQL += ")";
				else 
					brandIdSQL += ",";
			}
		else 
			brandIdSQL ="(0)";
		
		//@3. 生成销售数据
		String sql = "SELECT SUM(quantity), csp.productBarcode.product.brand.brand_ID, csp.type  FROM ChainStoreSalesOrderProduct csp " + 
		         " WHERE csp.chainSalesOrder.status = ? AND csp.chainSalesOrder.orderDate BETWEEN ? AND ? AND csp.productBarcode.product.year.year_ID=? AND csp.productBarcode.product.quarter.quarter_ID=? AND csp.productBarcode.product.brand.brand_ID IN " + brandIdSQL;
		String sql_sales_total = "SELECT SUM(quantity), csp.type  FROM ChainStoreSalesOrderProduct csp " + 
		         " WHERE csp.chainSalesOrder.status = ? AND csp.chainSalesOrder.orderDate BETWEEN ? AND ? AND csp.productBarcode.product.year.year_ID=? AND csp.productBarcode.product.quarter.quarter_ID=? ";

		if (chainId != Common_util.ALL_RECORD){
			sql += "AND csp.chainSalesOrder.chainStore.chain_id = ? ";
			sql_sales_total += "AND csp.chainSalesOrder.chainStore.chain_id = ? ";
			value_sale.add(chainId);
		} else { 
			sql += "AND csp.chainSalesOrder.chainStore.chain_id <> ? ";
			sql_sales_total += "AND csp.chainSalesOrder.chainStore.chain_id <> ? ";
			value_sale.add(SystemParm.getTestChainId());
		}
		sql += " GROUP BY csp.productBarcode.product.brand.brand_ID, csp.type ";
		sql_sales_total += " GROUP BY csp.type ";
		
		List<Object> salesResult = chainSalesOrderDaoImpl.executeHQLSelect(sql, value_sale.toArray(), null, true);
		
		//@2. 生成销售总数
		List<Object> salesTotalResult = chainSalesOrderDaoImpl.executeHQLSelect(sql_sales_total, value_sale.toArray(), null, true);
		
		//@3. 生成采购数据
		String sql2 = "SELECT SUM(quantity), p.productBarcode.product.brand.brand_ID, p.order.order_type FROM InventoryOrderProduct p " + 
		         " WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ? AND p.productBarcode.product.year.year_ID=?  AND p.productBarcode.product.quarter.quarter_ID=? AND p.productBarcode.product.brand.brand_ID IN " + brandIdSQL;
		String sql_purchase_total = "SELECT SUM(quantity), p.order.order_type FROM InventoryOrderProduct p " + 
		         " WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ? AND p.productBarcode.product.year.year_ID=? AND p.productBarcode.product.quarter.quarter_ID=? AND p.productBarcode.product.brand.brand_ID NOT IN " + notCoundBrandId;

		if (chainId != Common_util.ALL_RECORD){
			sql2 += "AND p.order.client_id = ? ";
			sql_purchase_total += "AND p.order.client_id = ? ";
			ChainStore chainStore = chainStoreDaoImpl.get(chainId, true);
			
			if (chainStore != null)
				value_purchase.add(chainStore.getClient_id());
			else 
				value_purchase.add(Common_util.ALL_RECORD);
		} else { 
			String chainSQL = constructChainSQL("p.order.client_id", value_purchase);
			if (chainSQL != null ){
			    sql2 += "AND " + chainSQL;
			    sql_purchase_total += "AND " + chainSQL;
			}
		}
		sql2 += " GROUP BY p.productBarcode.product.brand.brand_ID, p.order.order_type";
		sql_purchase_total += " GROUP BY p.order.order_type";
		List<Object> purchaseResult = inventoryOrderProductDAOImpl.executeHQLSelect(sql2, value_purchase.toArray(), null, true);

		//@4. 生成采购总数
		List<Object> purchaseTotalResult = inventoryOrderProductDAOImpl.executeHQLSelect(sql_purchase_total, value_purchase.toArray(), null, true);

		//@5. 生成库存数据
		String chainCriteria = "";
		ChainStore chainStore = chainStoreDaoImpl.get(chainId, true);
		if (chainId == Common_util.ALL_RECORD){
			chainCriteria = " c.clientId <> " + SystemParm.getTestClientId();
		}else{
			chainCriteria = " c.clientId = " + chainStore.getClient_id();	
		}
		String sql_in_out = "SELECT c.productBarcode.product.brand.brand_ID, SUM(c.quantity) FROM ChainInOutStock c WHERE "+ chainCriteria +" AND c.productBarcode.product.year.year_ID=? AND c.productBarcode.product.quarter.quarter_ID=? AND c.productBarcode.product.brand.brand_ID IN " + brandIdSQL +" GROUP BY c.productBarcode.product.brand.brand_ID";

		List<Object> inventory = chainInOutStockDaoImpl.executeHQLSelect(sql_in_out, value_inventory.toArray(),null,true);
		
		//@6. 生成库存总数
		String sql_in_out_total = "SELECT SUM(c.quantity) FROM ChainInOutStock c WHERE "+ chainCriteria +" AND  c.productBarcode.product.year.year_ID=?  AND c.productBarcode.product.quarter.quarter_ID=?  AND c.productBarcode.product.brand.brand_ID NOT IN" + notCoundBrandId;
		List<Object> inventory_total = chainInOutStockDaoImpl.executeHQLSelect(sql_in_out_total, value_inventory.toArray(),null,true);
		
		setAllInOneLevelThreeItem(salesResult, purchaseResult, salesTotalResult, purchaseTotalResult, inventory, inventory_total, result, chainId, yearId, quarterId, startDate, endDate);
	}



	private  void generateAllInOneReportLevelFour(Date startDate,
			Date endDate, int chainId, int yearId, int quarterId, int brandId,
			Map<String, Object> result, Pager pager) {
		//@1. calculate the pager
		if (pager.getTotalResult() == 0){
			int totalBarcode = productBarcodeDaoImpl.getNumOfProductsUnderYQB(yearId, quarterId, brandId);
			pager.initialize(totalBarcode);
		} else {
			pager.calFirstResult();
		}
		
		//@1.1 组成sql parameters
		List<Object> value_sale = new ArrayList<Object>();
		value_sale.add(ChainStoreSalesOrder.STATUS_COMPLETE);
		value_sale.add(startDate);
		value_sale.add(endDate);
		
		List<Object> value_purchase = new ArrayList<Object>();
		value_purchase.add(InventoryOrder.STATUS_ACCOUNT_COMPLETE);
		value_purchase.add(startDate);
		value_purchase.add(endDate);

        
		List<Object> value_sale_total = new ArrayList<Object>();
		value_sale_total.add(ChainStoreSalesOrder.STATUS_COMPLETE);
		value_sale_total.add(startDate);
		value_sale_total.add(endDate);
		value_sale_total.add(yearId);
		value_sale_total.add(quarterId);
		value_sale_total.add(brandId);
		
		List<Object> value_purchase_total = new ArrayList<Object>();
		value_purchase_total.add(InventoryOrder.STATUS_ACCOUNT_COMPLETE);
		value_purchase_total.add(startDate);
		value_purchase_total.add(endDate);
		value_purchase_total.add(yearId);
		value_purchase_total.add(quarterId);
		value_purchase_total.add(brandId);
		
        List<Object> value_inventory_total = new ArrayList<Object>();
        value_inventory_total.add(yearId);
        value_inventory_total.add(quarterId);
        value_inventory_total.add(brandId);
 
		//@2. 获取这页的brand ids
		List<Object> pbIds = productBarcodeDaoImpl.getProductsUnderYQB(yearId, quarterId,brandId, pager.getFirstResult(), pager.getRecordPerPage());
		String pbIdSQL = "(";
		if (pbIds.size() != 0)
			for (int i = 0; i < pbIds.size(); i++){
				Object pbId = pbIds.get(i);
				pbIdSQL += pbId;
				if (i == pbIds.size() -1)
					pbIdSQL += ")";
				else 
					pbIdSQL += ",";
			}
		else 
			pbIdSQL ="(0)";
		
		//@3. 生成销售数据
		String sql = "SELECT SUM(quantity), csp.productBarcode.id, csp.type  FROM ChainStoreSalesOrderProduct csp " + 
		         " WHERE csp.chainSalesOrder.status = ? AND csp.chainSalesOrder.orderDate BETWEEN ? AND ? AND csp.productBarcode.id IN " + pbIdSQL;
		String sql_sales_total = "SELECT SUM(quantity), csp.type  FROM ChainStoreSalesOrderProduct csp " + 
		         " WHERE csp.chainSalesOrder.status = ? AND csp.chainSalesOrder.orderDate BETWEEN ? AND ? AND csp.productBarcode.product.year.year_ID=? AND csp.productBarcode.product.quarter.quarter_ID=?  AND csp.productBarcode.product.brand.brand_ID =? ";

		if (chainId != Common_util.ALL_RECORD){
			sql += "AND csp.chainSalesOrder.chainStore.chain_id = ? ";
			sql_sales_total += "AND csp.chainSalesOrder.chainStore.chain_id = ? ";
			value_sale.add(chainId);
			value_sale_total.add(chainId);
		} else { 
			sql += "AND csp.chainSalesOrder.chainStore.chain_id <> ? ";
			sql_sales_total += "AND csp.chainSalesOrder.chainStore.chain_id <> ? ";
			value_sale.add(SystemParm.getTestChainId());
			value_sale_total.add(SystemParm.getTestChainId());
		}
		sql += " GROUP BY csp.productBarcode.id, csp.type ";
		sql_sales_total += " GROUP BY csp.type ";
		
		List<Object> salesResult = chainSalesOrderDaoImpl.executeHQLSelect(sql, value_sale.toArray(), null, true);
		
		//@2. 生成销售总数
		List<Object> salesTotalResult = chainSalesOrderDaoImpl.executeHQLSelect(sql_sales_total, value_sale_total.toArray(), null, true);
		
		//@3. 生成采购数据
		String sql2 = "SELECT SUM(quantity), p.productBarcode.id, p.order.order_type FROM InventoryOrderProduct p " + 
		         " WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ? AND p.productBarcode.id IN " + pbIdSQL;
		String sql_purchase_total = "SELECT SUM(quantity), p.order.order_type FROM InventoryOrderProduct p " + 
		         " WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ? AND p.productBarcode.product.year.year_ID=? AND p.productBarcode.product.quarter.quarter_ID=?  AND p.productBarcode.product.brand.brand_ID =? ";

		if (chainId != Common_util.ALL_RECORD){
			sql2 += "AND p.order.client_id = ? ";
			sql_purchase_total += "AND p.order.client_id = ? ";
			ChainStore chainStore = chainStoreDaoImpl.get(chainId, true);
			
			if (chainStore != null){
				value_purchase.add(chainStore.getClient_id());
				value_purchase_total.add(chainStore.getClient_id());
			} else {
				value_purchase.add(Common_util.ALL_RECORD);
				value_purchase_total.add(Common_util.ALL_RECORD);
			}
		} else { 
			String chainSQL = constructChainSQL("p.order.client_id", value_purchase);
			if (chainSQL != null ){
			    sql2 += "AND " + chainSQL;
			    sql_purchase_total += "AND " + chainSQL;
			}
		}
		sql2 += " GROUP BY p.productBarcode.id, p.order.order_type";
		sql_purchase_total += " GROUP BY p.order.order_type";
		List<Object> purchaseResult = inventoryOrderProductDAOImpl.executeHQLSelect(sql2, value_purchase.toArray(), null, true);

		//@4. 生成采购总数
		List<Object> purchaseTotalResult = inventoryOrderProductDAOImpl.executeHQLSelect(sql_purchase_total, value_purchase_total.toArray(), null, true);

		//@5. 生成库存数据
		String chainCriteria = "";
		ChainStore chainStore = chainStoreDaoImpl.get(chainId, true);
		if (chainId == Common_util.ALL_RECORD){
			chainCriteria = " c.clientId <> " + SystemParm.getTestClientId();
		}else{
			chainCriteria = " c.clientId = " + chainStore.getClient_id();	
		}
		String sql_in_out = "SELECT c.productBarcode.id, SUM(c.quantity) FROM ChainInOutStock c WHERE "+ chainCriteria +" AND c.productBarcode.id IN " + pbIdSQL+" GROUP BY c.productBarcode.id";

		List<Object> inventory = chainInOutStockDaoImpl.executeHQLSelect(sql_in_out, null,null,true);
		
		//@6. 生成库存总数
		String sql_in_out_total = "SELECT SUM(c.quantity) FROM ChainInOutStock c WHERE "+ chainCriteria +" AND  c.productBarcode.product.year.year_ID=?  AND c.productBarcode.product.quarter.quarter_ID=?  AND c.productBarcode.product.brand.brand_ID =? ";
		List<Object> inventory_total = chainInOutStockDaoImpl.executeHQLSelect(sql_in_out_total, value_inventory_total.toArray(),null,true);
		
		setAllInOneLevelFourItem(salesResult, purchaseResult, salesTotalResult, purchaseTotalResult, inventory, inventory_total, result, chainId, yearId, quarterId, brandId, startDate, endDate);	
	}

	private void setAllInOneLevelOneItem(List<Object> salesResult,
			List<Object> purchaseResult, List<Object> salesTotalResult,
			List<Object> purchaseTotalResult, List<Object> inventoryResult,
			List<Object> inventoryTotal, Map<String, Object> result, int chainId, Date startDate, Date endDate) {
		Map<Integer, ChainAllInOneReportItemLevelOne> dataMap = new HashMap<Integer, ChainAllInOneReportItemLevelOne>();
		ChainStore chainStore = this.getThisChainStore(chainId);
		
		if (salesResult != null){
			for (Object record : salesResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int yearId = Common_util.getInt(records[1]);
				int type = Common_util.getInt(records[2]);
				
				ChainAllInOneReportItemLevelOne levelOneItem = dataMap.get(yearId);
				if (levelOneItem != null){
					levelOneItem.putSales(type, quantity);
				} else {
					Year year = yearDaoImpl.get(yearId, true);
					
					levelOneItem = new ChainAllInOneReportItemLevelOne(chainStore, startDate, endDate, year);
					
					levelOneItem.putSales(type, quantity);
					dataMap.put(yearId, levelOneItem);
				}
			}
		}
		
		if (purchaseResult != null){
			for (Object record : purchaseResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int yearId = Common_util.getInt(records[1]);
				int type = Common_util.getInt(records[2]);
				
				ChainAllInOneReportItemLevelOne levelOneItem = dataMap.get(yearId);
				if (levelOneItem != null){
					levelOneItem.putPurchase(type, quantity);
				} else {
					Year year = yearDaoImpl.get(yearId, true);
					
					levelOneItem = new ChainAllInOneReportItemLevelOne(chainStore, startDate, endDate, year);
					
					levelOneItem.putPurchase(type, quantity);
					dataMap.put(yearId, levelOneItem);
				}
			}
		}
		
		if (inventoryResult != null){
			for (Object record : inventoryResult ){
				Object[] records = (Object[])record;
				int yearId = Common_util.getInt(records[0]);
				int quantity = Common_util.getInt(records[1]);
				
				ChainAllInOneReportItemLevelOne levelOneItem = dataMap.get(yearId);
				if (levelOneItem != null){
					levelOneItem.putInventory(quantity);
				} else {
					Year year = yearDaoImpl.get(yearId, true);
					
					levelOneItem = new ChainAllInOneReportItemLevelOne(chainStore, startDate, endDate, year);
					
					levelOneItem.putInventory(quantity);
					dataMap.put(yearId, levelOneItem);
				}
			}
		}
		
		ChainAllInOneReportItemLevelOne totalItem = new ChainAllInOneReportItemLevelOne(chainStore, startDate, endDate, null);
		if (salesTotalResult != null){
			for (Object record : salesTotalResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int type = Common_util.getInt(records[1]);
				
				totalItem.putSales(type, quantity);
			}
		}
		
		if (purchaseTotalResult != null){
			for (Object record : purchaseTotalResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int type = Common_util.getInt(records[1]);
				
				totalItem.putPurchase(type, quantity);
			}
		}
		
		if (inventoryTotal != null){
			for (Object record : inventoryTotal ){
				int quantity = Common_util.getInt(record);
				
				totalItem.putInventory(quantity);
			}
		}
		
		List<Integer> yearKey = new ArrayList<Integer>(dataMap.keySet());
		Collections.sort(yearKey);
		
		List<ChainAllInOneReportItemLevelOne> levelOneItems = new ArrayList<ChainAllInOneReportItemLevelOne>();
		for (Integer yearId: yearKey){
			levelOneItems.add(dataMap.get(yearId));
		}
		
		result.put("data", levelOneItems);
		result.put("total", totalItem);
		
	}
	
	private void setAllInOneLevelTwoItem(List<Object> salesResult,
			List<Object> purchaseResult, List<Object> salesTotalResult,
			List<Object> purchaseTotalResult, List<Object> inventory,
			List<Object> inventory_total, Map<String, Object> result,
			int chainId, int yearId, Date startDate, Date endDate) {
		Map<Integer, ChainAllInOneReportItemLevelTwo> dataMap = new HashMap<Integer, ChainAllInOneReportItemLevelTwo>();
		ChainStore chainStore = this.getThisChainStore(chainId);
		Year year = yearDaoImpl.get(yearId, true);
		
		if (salesResult != null){
			for (Object record : salesResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int quarterId = Common_util.getInt(records[1]);
				int type = Common_util.getInt(records[2]);
				
				ChainAllInOneReportItemLevelTwo levelOneItem = dataMap.get(quarterId);
				if (levelOneItem != null){
					levelOneItem.putSales(type, quantity);
				} else {
					Quarter quarter = quarterDaoImpl.get(quarterId, true);
					
					levelOneItem = new ChainAllInOneReportItemLevelTwo(chainStore, startDate, endDate, year, quarter);
					
					levelOneItem.putSales(type, quantity);
					dataMap.put(quarterId, levelOneItem);
				}
			}
		}
		
		if (purchaseResult != null){
			for (Object record : purchaseResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int quarterId = Common_util.getInt(records[1]);
				int type = Common_util.getInt(records[2]);
				
				ChainAllInOneReportItemLevelTwo levelOneItem = dataMap.get(quarterId);
				if (levelOneItem != null){
					levelOneItem.putPurchase(type, quantity);
				} else {
					Quarter quarter = quarterDaoImpl.get(quarterId, true);
					
					levelOneItem = new ChainAllInOneReportItemLevelTwo(chainStore, startDate, endDate, year, quarter);
					
					levelOneItem.putPurchase(type, quantity);
					dataMap.put(quarterId, levelOneItem);
				}
			}
		}
		
		if (inventory != null){
			for (Object record : inventory ){
				Object[] records = (Object[])record;
				int quarterId = Common_util.getInt(records[0]);
				int quantity = Common_util.getInt(records[1]);
				
				ChainAllInOneReportItemLevelTwo levelOneItem = dataMap.get(quarterId);
				if (levelOneItem != null){
					levelOneItem.putInventory(quantity);
				} else {
					Quarter quarter = quarterDaoImpl.get(quarterId, true);
					
					levelOneItem = new ChainAllInOneReportItemLevelTwo(chainStore, startDate, endDate, year, quarter);
					
					levelOneItem.putInventory(quantity);
					dataMap.put(quarterId, levelOneItem);
				}
			}
		}
		
		ChainAllInOneReportItemLevelTwo totalItem = new ChainAllInOneReportItemLevelTwo(chainStore, startDate, endDate, null, null);
		if (salesTotalResult != null){
			for (Object record : salesTotalResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int type = Common_util.getInt(records[1]);
				
				totalItem.putSales(type, quantity);
			}
		}
		
		if (purchaseTotalResult != null){
			for (Object record : purchaseTotalResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int type = Common_util.getInt(records[1]);
				
				totalItem.putPurchase(type, quantity);
			}
		}
		
		if (inventory_total != null){
			for (Object record : inventory_total ){
				int quantity = Common_util.getInt(record);
				
				totalItem.putInventory(quantity);
			}
		}
		
		List<Integer> quarterKey = new ArrayList<Integer>(dataMap.keySet());
		Collections.sort(quarterKey);
		
		List<ChainAllInOneReportItemLevelTwo> levelOneItems = new ArrayList<ChainAllInOneReportItemLevelTwo>();
		for (Integer quarterId: quarterKey){
			levelOneItems.add(dataMap.get(quarterId));
		}
		
		result.put("data", levelOneItems);
		result.put("total", totalItem);
		
	}

	
	private void setAllInOneLevelThreeItem(List<Object> salesResult,
			List<Object> purchaseResult, List<Object> salesTotalResult,
			List<Object> purchaseTotalResult, List<Object> inventory,
			List<Object> inventory_total, Map<String, Object> result,
			int chainId, int yearId, int quarterId, Date startDate, Date endDate) {
		Map<Integer, ChainAllInOneReportItemLevelThree> dataMap = new HashMap<Integer, ChainAllInOneReportItemLevelThree>();
		ChainStore chainStore = this.getThisChainStore(chainId);
		Year year = yearDaoImpl.get(yearId, true);
		Quarter quarter = quarterDaoImpl.get(quarterId, true);
		
		if (salesResult != null){
			for (Object record : salesResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int brandId = Common_util.getInt(records[1]);
				int type = Common_util.getInt(records[2]);
				
				ChainAllInOneReportItemLevelThree levelOneItem = dataMap.get(brandId);
				if (levelOneItem != null){
					levelOneItem.putSales(type, quantity);
				} else {
					Brand brand = brandDaoImpl.get(brandId, true);
					
					levelOneItem = new ChainAllInOneReportItemLevelThree(chainStore, startDate, endDate, year, quarter,brand);
					
					levelOneItem.putSales(type, quantity);
					dataMap.put(brandId, levelOneItem);
				}
			}
		}
		
		if (purchaseResult != null){
			for (Object record : purchaseResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int brandId = Common_util.getInt(records[1]);
				int type = Common_util.getInt(records[2]);
				
				ChainAllInOneReportItemLevelThree levelOneItem = dataMap.get(brandId);
				if (levelOneItem != null){
					levelOneItem.putPurchase(type, quantity);
				} else {
					Brand brand = brandDaoImpl.get(brandId, true);
					
					levelOneItem = new ChainAllInOneReportItemLevelThree(chainStore, startDate, endDate, year, quarter,brand);
					
					levelOneItem.putPurchase(type, quantity);
					dataMap.put(brandId, levelOneItem);
				}
			}
		}
		
		if (inventory != null){
			for (Object record : inventory ){
				Object[] records = (Object[])record;
				int brandId = Common_util.getInt(records[0]);
				int quantity = Common_util.getInt(records[1]);
				
				ChainAllInOneReportItemLevelThree levelOneItem = dataMap.get(brandId);
				if (levelOneItem != null){
					levelOneItem.putInventory(quantity);
				} else {
					Brand brand = brandDaoImpl.get(brandId, true);
					
					levelOneItem = new ChainAllInOneReportItemLevelThree(chainStore, startDate, endDate, year, quarter,brand);
					
					levelOneItem.putInventory(quantity);
					dataMap.put(brandId, levelOneItem);
				}
			}
		}
		
		ChainAllInOneReportItemLevelThree totalItem = new ChainAllInOneReportItemLevelThree(chainStore, startDate, endDate, year, quarter, null);
		if (salesTotalResult != null){
			for (Object record : salesTotalResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int type = Common_util.getInt(records[1]);
				
				totalItem.putSales(type, quantity);
			}
		}
		
		if (purchaseTotalResult != null){
			for (Object record : purchaseTotalResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int type = Common_util.getInt(records[1]);
				
				totalItem.putPurchase(type, quantity);
			}
		}
		
		if (inventory_total != null){
			for (Object record : inventory_total ){
				int quantity = Common_util.getInt(record);
				
				totalItem.putInventory(quantity);
			}
		}
		
		List<Integer> brandKey = new ArrayList<Integer>(dataMap.keySet());
		Collections.sort(brandKey);
		
		List<ChainAllInOneReportItemLevelThree> levelOneItems = new ArrayList<ChainAllInOneReportItemLevelThree>();
		for (Integer brandId: brandKey){
			levelOneItems.add(dataMap.get(brandId));
		}
		
		result.put("data", levelOneItems);
		result.put("total", totalItem);
	}

	private void setAllInOneLevelFourItem(List<Object> salesResult,
			List<Object> purchaseResult, List<Object> salesTotalResult,
			List<Object> purchaseTotalResult, List<Object> inventory,
			List<Object> inventory_total, Map<String, Object> result,
			int chainId, int yearId, int quarterId, int brandId,
			Date startDate, Date endDate) {
		Map<Integer, ChainAllInOneReportItemLevelFour> dataMap = new HashMap<Integer, ChainAllInOneReportItemLevelFour>();
		ChainStore chainStore = this.getThisChainStore(chainId);
		Year year = yearDaoImpl.get(yearId, true);
		Quarter quarter = quarterDaoImpl.get(quarterId, true);
		Brand brand = brandDaoImpl.get(brandId, true);
		
		if (salesResult != null){
			for (Object record : salesResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int pbId = Common_util.getInt(records[1]);
				int type = Common_util.getInt(records[2]);
				
				ChainAllInOneReportItemLevelFour levelOneItem = dataMap.get(pbId);
				if (levelOneItem != null){
					levelOneItem.putSales(type, quantity);
				} else {
					ProductBarcode pb = productBarcodeDaoImpl.get(pbId, true);
					
					levelOneItem = new ChainAllInOneReportItemLevelFour(chainStore, startDate, endDate, year, quarter,brand,pb);
					
					levelOneItem.putSales(type, quantity);
					dataMap.put(pbId, levelOneItem);
				}
			}
		}
		
		if (purchaseResult != null){
			for (Object record : purchaseResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int pbId = Common_util.getInt(records[1]);
				int type = Common_util.getInt(records[2]);
				
				ChainAllInOneReportItemLevelFour levelOneItem = dataMap.get(pbId);
				if (levelOneItem != null){
					levelOneItem.putPurchase(type, quantity);
				} else {
					ProductBarcode pb = productBarcodeDaoImpl.get(pbId, true);
					
					levelOneItem = new ChainAllInOneReportItemLevelFour(chainStore, startDate, endDate, year, quarter,brand,pb);
					
					levelOneItem.putPurchase(type, quantity);
					dataMap.put(pbId, levelOneItem);
				}
			}
		}
		
		if (inventory != null){
			for (Object record : inventory ){
				Object[] records = (Object[])record;
				int pbId = Common_util.getInt(records[0]);
				int quantity = Common_util.getInt(records[1]);
				
				ChainAllInOneReportItemLevelFour levelOneItem = dataMap.get(pbId);
				if (levelOneItem != null){
					levelOneItem.putInventory(quantity);
				} else {
					ProductBarcode pb = productBarcodeDaoImpl.get(pbId, true);
					
					levelOneItem = new ChainAllInOneReportItemLevelFour(chainStore, startDate, endDate, year, quarter,brand, pb);
					
					levelOneItem.putInventory(quantity);
					dataMap.put(pbId, levelOneItem);
				}
			}
		}
		
		ChainAllInOneReportItemLevelFour totalItem = new ChainAllInOneReportItemLevelFour(chainStore, startDate, endDate, year, quarter, brand, null);
		if (salesTotalResult != null){
			for (Object record : salesTotalResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int type = Common_util.getInt(records[1]);
				
				totalItem.putSales(type, quantity);
			}
		}
		
		if (purchaseTotalResult != null){
			for (Object record : purchaseTotalResult ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				int type = Common_util.getInt(records[1]);
				
				totalItem.putPurchase(type, quantity);
			}
		}
		
		if (inventory_total != null){
			for (Object record : inventory_total ){
				int quantity = Common_util.getInt(record);
				
				totalItem.putInventory(quantity);
			}
		}
		
		List<Integer> brandKey = new ArrayList<Integer>(dataMap.keySet());
		Collections.sort(brandKey);
		
		List<ChainAllInOneReportItemLevelFour> levelOneItems = new ArrayList<ChainAllInOneReportItemLevelFour>();
		for (Integer pbId: brandKey){
			levelOneItems.add(dataMap.get(pbId));
		}
		
		result.put("data", levelOneItems);
		result.put("total", totalItem);
		
	}

	/**
	 * 准备销售分析报表页面
	 * @param formBean
	 */
	public void prepareSalesAnalysisReportUI(ChainReportActionFormBean formBean) {
		formBean.setChainStore(chainStoreDaoImpl.getAllChainStoreObject());
		
	}

	/**
	 * 获取按照销售人员排名的销售报表
	 * @param formBean
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 * @return
	 */
	public Response generateSalesReportBySaler(
			ChainReportActionFormBean formBean, int page, int rows,
			String sort, String order, ChainUserInfor loginUser) {
		Response response = new Response();
		
		int chainId = formBean.getChainStore().getChain_id();
		Date startDate = Common_util.formStartDate(formBean.getStartDate());
		Date endDate = Common_util.formEndDate(formBean.getEndDate());
		
		ChainReport report = new ChainReport();
		report.setChainStore(formBean.getChainStore());
		
		Map saleReport = new HashMap<String, Object>();
		
		try {
			generateSalesReportBySaler(saleReport, chainId, startDate, endDate, page, rows, sort, order, loginUser);
			response.setReturnCode(Response.SUCCESS);
		} catch (Exception e) {
			response.setReturnCode(Response.FAIL);
		}
		response.setReturnValue(saleReport);
		
		return response;
	}

	private void generateSalesReportBySaler(Map saleReport, int chainId,
			Date startDate, Date endDate, Integer page, Integer rowPerPage, String sort,
			String order, ChainUserInfor loginUser) {
		/**
		 * 1. 获取total
		 */
		
		ChainSalesReport totalReport = generateSalesTotal(chainId, Common_util.ALL_RECORD, startDate, endDate, true);
		if (!ChainUserInforService.isMgmtFromHQ(loginUser) && loginUser.getRoleType().getChainRoleTypeId() != ChainRoleType.CHAIN_OWNER){
			totalReport.setFreeCostSum(0);
			totalReport.setNetProfit(0);
			totalReport.setNetSaleCostSum(0);
		}
		
		/**
		 * 2. 实现分页,如果是搜索所有连锁店
		 */
		Object[] value_sale = new Object[]{startDate, endDate, ChainStoreSalesOrder.STATUS_COMPLETE};
		int total = 0;
		if (page != null && rowPerPage != null)
			total = calculateSaleReportBySalerCount(chainId, value_sale);
		
		/**
		 * 获取数据列表
		 */
		List<ChainSalesReport> reports = new ArrayList<ChainSalesReport>();
		String criteria = "";
		if (chainId == Common_util.ALL_RECORD)
			criteria = " FROM ChainStoreSalesOrder WHERE orderDate BETWEEN ? AND ? AND status = ?";
		else 
			criteria = "FROM ChainStoreSalesOrder WHERE orderDate BETWEEN ? AND ? AND status = ? AND chainStore.chain_id = " + chainId;

		criteria += " GROUP BY  saler.user_id ";
		
		String orderBy = generateSalesOrderBy(sort, order);
		
		criteria += orderBy;
		
		String hql_sale2 = "SELECT sum(totalQuantity), sum(netAmount), sum(totalQuantityR), " +
				"sum(netAmountR), sum(totalCost), sum(totalQuantityF) ,sum(totalCostF), sum(discountAmount), " +
				"sum(coupon), sum (cardAmount), sum(cashAmount - returnAmount), sum(vipScore), chainStore.chain_id, sum(totalAmount - netAmount),  saler.user_id, sum(qxQuantity), sum(qxAmount), sum(qxCost), sum(myQuantity), sum(myAmount), sum(myCost),sum(chainPrepaidAmt), sum(wechatAmount), sum(alipayAmount)   " + criteria;

		Integer[] pagerArray = null;
		if (page != null && rowPerPage != null)
		      pagerArray = new Integer[]{Common_util.getFirstRecord(page, rowPerPage), rowPerPage};
		
		List<Object> sales2 = (List<Object>)chainSalesOrderDaoImpl.executeHQLSelect(hql_sale2, value_sale,pagerArray, true);

		for (Object resultObject : sales2){
			Object[] sales3 = (Object[])resultObject;
			int totalQ2 = Common_util.getInt(sales3[0]);
			double netAmt2 = Common_util.getDouble(sales3[1]);
			int totalQR2 = Common_util.getInt(sales3[2]);
			double netAmtR2 = Common_util.getDouble(sales3[3]);
			double totalCost2 = Common_util.getDouble(sales3[4]);
			int totalQF2 = Common_util.getInt(sales3[5]);
			double totalCostF2 = Common_util.getDouble(sales3[6]);
			double discountAmt2 = Common_util.getDouble(sales3[7]);
			double coupon2 = Common_util.getDouble(sales3[8]);
			double cardAmt2 = Common_util.getDouble(sales3[9]);
			double cashAmt2 = Common_util.getDouble(sales3[10]);
			double vipScoreAmt2 = Common_util.getDouble(sales3[11]);
			int chainStoreId =  Common_util.getInt(sales3[12]);
			double totalSalesDiscountAmt2 = Common_util.getDouble(sales3[13]);
			int salerId = Common_util.getInt(sales3[14]);
			int qxQuantity = Common_util.getInt(sales3[15]);
			double qxAmount = Common_util.getDouble(sales3[16]);
			double qxCost = Common_util.getDouble(sales3[17]);
			int myQuantity = Common_util.getInt(sales3[18]);
			double myAmount = Common_util.getDouble(sales3[19]);
			double myCost = Common_util.getDouble(sales3[20]);	
			double vipPrepaidAmt = Common_util.getDouble(sales3[21]);
			double wechatAmt =  Common_util.getDouble(sales3[22]);
			double alipayAmt =  Common_util.getDouble(sales3[23]);

			ChainStore store = chainStoreDaoImpl.get(chainStoreId, true);
			ChainUserInfor user = chainUserInforDaoImpl.get(salerId, true);
			
			ChainSalesReport chainReport2 = new ChainSalesReport(ChainReport.TYPE_SALES_REPORT, totalQ2, totalQR2,
					totalQF2, netAmt2,totalSalesDiscountAmt2, netAmtR2,totalCost2, totalCostF2, discountAmt2,
					coupon2, cardAmt2, cashAmt2, vipScoreAmt2, qxQuantity,qxAmount, qxCost, myQuantity, myAmount, myCost,0 ,0,vipPrepaidAmt,0,0,0, store,user, wechatAmt, alipayAmt);
			
			if (!ChainUserInforService.isMgmtFromHQ(loginUser) && loginUser.getRoleType().getChainRoleTypeId() != ChainRoleType.CHAIN_OWNER){
				chainReport2.setFreeCostSum(0);
				chainReport2.setNetProfit(0);
				chainReport2.setNetSaleCostSum(0);
			}
			
			reports.add(chainReport2);
		}

		List<ChainSalesReport> footer = new ArrayList<ChainSalesReport>();
		footer.add(totalReport);
		saleReport.put("footer", footer);
		saleReport.put("rows", reports);
		saleReport.put("total", total);
		
	}

	private int calculateSaleReportBySalerCount(int chainId, Object[] value_sale) {
		//2.1 计算pager
		String criteria = "";
		if (chainId == Common_util.ALL_RECORD)
			criteria = " FROM ChainStoreSalesOrder WHERE orderDate BETWEEN ? AND ? AND status = ?";
		else 
			criteria = "FROM ChainStoreSalesOrder WHERE orderDate BETWEEN ? AND ? AND status = ? AND chainStore.chain_id = " + chainId;

		String criteria2 = "SELECT COUNT(DISTINCT saler.user_id) " + criteria;
		    
	    return chainSalesOrderDaoImpl.executeHQLCount(criteria2, value_sale, true);
	}

	/**
	 * 获取
	 * @param chainStore
	 * @param startDate
	 * @param endDate
	 * @param year
	 * @param quarter
	 * @param brand
	 * @param loginUserInfor
	 * @param string
	 * @return
	 */
	public Response generateChainSalesStatisticExcelReport(
			ChainStore chainStore, ChainUserInfor saler, java.sql.Date startDate,
			java.sql.Date endDate, Year year, Quarter quarter, Brand brand,
			ChainUserInfor loginUserInfor, String filePath) {
		
            Response response = new Response();
            
            List<Object> result = new ArrayList<Object>();
            
    		int chainId = chainStore.getChain_id();
    		int salerId = saler.getUser_id();
    		int yearId = (year == null) ? 0 : year.getYear_ID();
    		int quarterId = (quarter == null) ? 0 : quarter.getQuarter_ID();
    		int brandId = (brand == null) ? 0 : brand.getBrand_ID();

            //generateSalesStatisticReportLevelFour(startDate, endDate, chainId, salerId, yearId, quarterId, brandId, result, null);

			/**
			 * @2. 准备excel报表
			 */
			boolean showCost = false;
			if (ChainUserInforService.isMgmtFromHQ(loginUserInfor) || loginUserInfor.containFunction("purchaseAction!seeCost"))
				showCost = true;
			
			List<ChainSalesStatisReportItem> reportItems = (List<ChainSalesStatisReportItem>)result.get(0);
			Collections.sort(reportItems, new ChainSalesStatisticComparator());
			
			ChainSalesStatisReportItem totalItem = (ChainSalesStatisReportItem)result.get(1);
			try {
				chainStore = this.getThisChainStore(chainStore.getChain_id());
				if (saler.getUser_id() != Common_util.ALL_RECORD)
					saler = chainUserInforDaoImpl.get(saler.getUser_id(), true);
				ChainSalesStatisticsReportTemplate chainSalesStatisticsReportTemplate = new ChainSalesStatisticsReportTemplate(reportItems,totalItem, chainStore, filePath, showCost, saler, startDate, endDate);
				HSSFWorkbook wb = chainSalesStatisticsReportTemplate.process();
				
				ByteArrayInputStream byteArrayInputStream = ExcelUtil.convertExcelToInputStream(wb);
				
				response.setReturnValue(byteArrayInputStream);
				response.setReturnCode(Response.SUCCESS);
			} catch (IOException e){
				response.setReturnCode(Response.FAIL);
			}
			return response;
	}

	class ChainSalesStatisticComparator implements Comparator<ChainSalesStatisReportItem> {

		@Override
		public int compare(ChainSalesStatisReportItem o1,
				ChainSalesStatisReportItem o2) {
			Product p1 = o1.getProductBarcode().getProduct();
			Product p2 = o2.getProductBarcode().getProduct();
			
			if (p1.getYear().getYear_ID() == p2.getYear().getYear_ID()){
				if (p1.getQuarter().getQuarter_ID() == p2.getQuarter().getQuarter_ID()){
					if (p1.getBrand().getBrand_ID() == p2.getBrand().getBrand_ID()){
						return p1.getProductCode().compareTo(p2.getProductCode());
					} else 
						return p1.getBrand().getBrand_ID() - p2.getBrand().getBrand_ID();
				} else 
					return p1.getQuarter().getQuarter_ID() - p2.getQuarter().getQuarter_ID();
			} else
			    return p1.getYear().getYear_ID() - p2.getYear().getYear_ID();
		}
	}

	public void prepareVIPConsumpReportUI(ChainReportActionFormBean formBean,
			ChainReportActionUIBean uiBean, ChainUserInfor userInfor) {
		if (!ChainUserInforService.isMgmtFromHQ(userInfor)){
			int chainId = userInfor.getMyChainStore().getChain_id();
			ChainStore chainStore = chainStoreService.getChainStoreByID(chainId);
			formBean.setChainStore(chainStore);
		} else {
			ChainStore allChainStore = ChainStoreDaoImpl.getAllChainStoreObject();
			formBean.setChainStore(allChainStore);
		}
		
	}

	@Transactional
	public Response generateVIPConsumpReport(
			ChainReportActionFormBean formBean, Integer page, Integer rowPerPage,
			String sort, String order) {
		Response response = new Response();
		Date startDate = formBean.getStartDate();
		Date endDate = formBean.getEndDate();
		int chainId = formBean.getChainStore().getChain_id();
		Map saleReport = new HashMap();
		
		/**
		 * 1. 获取total
		 */
		Object[] value_sale = new Object[]{startDate, endDate, ChainStoreSalesOrder.STATUS_COMPLETE};

		String chainCriteria = "";
		if (chainId == Common_util.ALL_RECORD)
			chainCriteria = " chainStore.chain_id <> " + SystemParm.getTestChainId();
		else 
			chainCriteria = " chainStore.chain_id = " + chainId;

		String hql_sale = "select sum(totalQuantity), sum(netAmount),  sum(totalQuantityR), " +
				"sum(netAmountR), sum(totalQuantityF), sum(discountAmount), " +
				"sum(coupon), sum (cardAmount + cashAmount - returnAmount) from ChainStoreSalesOrder where vipCard IS NOT NULL and  orderDate between ? and ? and status = ? and " + chainCriteria;

		Object[] sales = (Object[])chainSalesOrderDaoImpl.executeHQLSelect(hql_sale, value_sale,null, true).get(0);

		int saleQ = Common_util.getInt(sales[0]);
		double saleAmt = Common_util.getDouble(sales[1]);
		int returnQ = Common_util.getInt(sales[2]);
		double returnAmt = Common_util.getDouble(sales[3]);
		int freeQ = Common_util.getInt(sales[4]);
		double discountAmt = Common_util.getDouble(sales[5]);
		double coupon = Common_util.getDouble(sales[6]);
		double receiveAmt = Common_util.getDouble(sales[7]);
				
		VIPReportVO totalVO = new VIPReportVO(saleQ, returnQ, freeQ, saleAmt, returnAmt, coupon, receiveAmt, discountAmt, null);
		
		//添加一个dummy的chainStore
		ChainVIPCard vip = new ChainVIPCard();
		vip.setIssueChainStore(new ChainStore());
		totalVO.setVip(vip);
		
		
		/**
		 * 2. 实现分页,如果是搜索所有连锁店
		 */
		int total = 0;
		if (page != null && rowPerPage != null)
			total = calculateVIPConsumpReportTotalRecord(chainId, value_sale);
		
		/**
		 * 获取数据列表
		 */
		List<VIPReportVO> reports = new ArrayList<VIPReportVO>();
		String criteria = "";
		if (chainId == Common_util.ALL_RECORD)
			criteria = " FROM ChainStoreSalesOrder WHERE vipCard IS NOT NULL and  orderDate BETWEEN ? AND ? AND status = ?";
		else 
			criteria = "FROM ChainStoreSalesOrder WHERE vipCard IS NOT NULL and  orderDate BETWEEN ? AND ? AND status = ? AND chainStore.chain_id = " + chainId;

		criteria += " GROUP BY  vipCard.id ";
	
		String orderBy = generateVIPConsumpOrderBy(sort, order);
		criteria += orderBy;
		
		String hql_sale2 = "select sum(totalQuantity), sum(netAmount),  sum(totalQuantityR), " +
				"sum(netAmountR), sum(totalQuantityF), sum(discountAmount), " +
				"sum(coupon), sum (cardAmount + cashAmount - returnAmount), vipCard.id " + criteria;

		Integer[] pagerArray = null;
		if (page != null && rowPerPage != null)
		      pagerArray = new Integer[]{Common_util.getFirstRecord(page, rowPerPage), rowPerPage};
		
		List<Object> sales2 = (List<Object>)chainSalesOrderDaoImpl.executeHQLSelect(hql_sale2, value_sale,pagerArray, true);

		for (Object resultObject : sales2){
			Object[] sales3 = (Object[])resultObject;
			int saleQ2 = Common_util.getInt(sales3[0]);
			double saleAmt2 = Common_util.getDouble(sales3[1]);
			int returnQ2 = Common_util.getInt(sales3[2]);
			double returnAmt2 = Common_util.getDouble(sales3[3]);
			int freeQ2 = Common_util.getInt(sales3[4]);
			double discountAmt2 = Common_util.getDouble(sales3[5]);
			double coupon2 = Common_util.getDouble(sales3[6]);
			double receiveAmt2 = Common_util.getDouble(sales3[7]);
			int vipId = Common_util.getInt(sales3[8]);
					
			ChainVIPCard vipCard = chainVIPCardImpl.get(vipId, true);

			VIPReportVO rptVo = new VIPReportVO(saleQ2, returnQ2, freeQ2, saleAmt2, returnAmt2, coupon2, receiveAmt2, discountAmt2, vipCard);
			
			reports.add(rptVo);
		}

		List<VIPReportVO> footer = new ArrayList<VIPReportVO>();
		footer.add(totalVO);
		saleReport.put("footer", footer);
		saleReport.put("rows", reports);
		saleReport.put("total", total);
		
		response.setReturnValue(saleReport);
		return response;
	}

	private String generateVIPConsumpOrderBy(String sort, String order) {
		String orderBy = " ORDER BY sum(netAmount - netAmountR) desc";

		return orderBy;
	}

	private int calculateVIPConsumpReportTotalRecord(int chainId,
			Object[] value_sale) {
		//2.1 计算pager
		String criteria = "";
		if (chainId == Common_util.ALL_RECORD)
			criteria = " FROM ChainStoreSalesOrder WHERE vipCard IS NOT NULL and  orderDate BETWEEN ? AND ? AND status = ?";
		else 
			criteria = "FROM ChainStoreSalesOrder WHERE vipCard IS NOT NULL and  orderDate BETWEEN ? AND ? AND status = ? AND chainStore.chain_id = " + chainId;

		String criteria2 = "SELECT COUNT(DISTINCT vipCard.id) " + criteria;
		    
	    return chainSalesOrderDaoImpl.executeHQLCount(criteria2, value_sale, true);
	}

	/**
	 * 准备 report repository 数据
	 * @param formBean
	 * @param uiBean
	 */
	public void prepareChainRptRepositoryUI(ChainReportActionFormBean formBean,
			ChainReportActionUIBean uiBean) {
		
		Map<Integer, List<ChainBatchRptRepositoty>> dataMap = chainAutoRptRepositoryDaoImpl.getRptRepositoryDateMap();
		Iterator<Integer> ids = dataMap.keySet().iterator();
		
		while (ids.hasNext()){
			int id = ids.next();
			switch (id) {
				case ChainBatchRptRepositoty.TYPE_WEEKLY_PRODUCT_ANALYSIS_RPT:
				    uiBean.setCurrentSalesDates(dataMap.get(id));
					break;
				case ChainBatchRptRepositoty.TYPE_ACCU_SALES_AWEEKLY_NALYSIS_RPT:
				    uiBean.setAccumulatedSalesDates(dataMap.get(id));
					break;
				case ChainBatchRptRepositoty.TYPE_CHAIN_TRANSFER_ACCT_FLOW_RPT:
				    uiBean.setChainTransferAcctFlowDates(dataMap.get(id));
					break;					
				default:
					break;
			}
		}
	}

	/**
	 * 下载batch report
	 * @param rptRepository
	 * @param userInfor
	 * @return
	 */
	public Response loadBatchRptRepository(
			ChainBatchRptRepositoty rptRepository, ChainUserInfor userInfor) {
		Response response = new Response();
		Map<String, Object> result = new HashMap<String, Object>();
		
		int rptId = rptRepository.getId();
		
		rptRepository = chainAutoRptRepositoryDaoImpl.get(rptId, true);
		if (rptRepository == null){
			response.setFail("无法找到报表");
		} else {
			int typeId = rptRepository.getRptId();
			if (typeId == ChainBatchRptRepositoty.TYPE_ACCU_SALES_AWEEKLY_NALYSIS_RPT || typeId == ChainBatchRptRepositoty.TYPE_WEEKLY_PRODUCT_ANALYSIS_RPT)
				result.put("Type", "zip");
			else 
				result.put("Type", "excel");
			
			File file = new File(rptRepository.getRptPathByType() + rptRepository.getDownloadRptName());
			InputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
				result.put("download", inputStream);
				result.put("name", rptRepository.getDownloadRptName());
				response.setReturnValue(result);
			} catch (FileNotFoundException e) {
				response.setFail("无法找到报表");
			}
		}

		return response;
	}

	/**
	 * 获取vip销售占比分析报表
	 * @param rptDate
	 * @return
	 */
	public Response getVIPSalesAnalysisRpt(java.sql.Date rptDate) {
		Response response = new Response();
		
		String filePath = ChainSalesReportVIPPercentageTemplate.getFilePath(rptDate);
		
		File vipRpt = new File(filePath);
		if (!vipRpt.exists())
			response.setFail("无法找到当前VIP分析报表");
		else {
			response.setReturnValue(vipRpt);
		}
		return response;
	}

	/**
	 * 根据今年日期获取去年的销售数据
	 * @param formBean
	 * @return
	 */
	public Response generateLastYearSalesReport(
			ChainReportActionFormBean formBean) {
		Response response = new Response();
		
		int chainId = formBean.getChainStore().getChain_id();
		Date startDate = Common_util.formStartDate(Common_util.getDateOfLastYear(formBean.getStartDate()));
		Date endDate = Common_util.formEndDate(Common_util.getDateOfLastYear(formBean.getEndDate()));
		int salerId = Common_util.ALL_RECORD;
		
		Object[] value_sale = new Object[]{startDate, endDate, ChainStoreSalesOrder.STATUS_COMPLETE};
		
		ChainReport report = new ChainReport();
		report.setChainStore(formBean.getChainStore());
		
		Map qxDataMap = new HashMap<Integer, Double>();

		try {
			String criteria = " FROM ChainStoreSalesOrder WHERE orderDate BETWEEN ? AND ? AND status = ? GROUP BY chainStore.chain_id ";
			
			String hql_sale2 = "SELECT chainStore.chain_id, sum(qxQuantity), sum(qxAmount) " + criteria;

			List<Object> sales2 = (List<Object>)chainSalesOrderDaoImpl.executeHQLSelect(hql_sale2, value_sale,null, true);

			double totalAmt = 0;
			for (Object resultObject : sales2){
				Object[] sales3 = (Object[])resultObject;
				int chainStoreId =  Common_util.getInt(sales3[0]);
			
				int qxQuantity = Common_util.getInt(sales3[1]);
				double qxAmount = Common_util.getDouble(sales3[2]);
				totalAmt += qxAmount;
				
				qxDataMap.put(chainStoreId, qxAmount);
			}
			
			qxDataMap.put(Common_util.ALL_RECORD, totalAmt);
		} catch (Exception e) {
			e.printStackTrace();
			response.setReturnCode(Response.FAIL);
		}
		response.setReturnValue(qxDataMap);
		
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
	public Response getSalesStatisticReptEles(int parentId,Date startDate, Date endDate, int chainId, int yearId, int quarterId, int brandId, int salerId,
			ChainUserInfor userInfor) {
		Response response = new Response();
		
		List<Object> value_sale = new ArrayList<Object>();
		value_sale.add(ChainStoreSalesOrder.STATUS_COMPLETE);
		value_sale.add(Common_util.formStartDate(startDate));
		value_sale.add(Common_util.formEndDate(endDate));

		
		boolean showCost = userInfor.containFunction("purchaseAction!seeCost");
		ChainStore chainStore = this.getThisChainStore(chainId);
		ChainUserInfor saler = this.getThisChainUserInfor(salerId);
		
		List<ChainSalesStatisticReportItemVO> reportItems = new ArrayList<ChainSalesStatisticReportItemVO>();
		String name = "";
		
		String whereClause = "";
		if (chainId != Common_util.ALL_RECORD){
			whereClause += " AND csp.chainSalesOrder.chainStore.chain_id = " + chainId;
		} else { 
			whereClause += " AND csp.chainSalesOrder.chainStore.chain_id <>  " + SystemParm.getTestChainId();
		}
		
		if (salerId != Common_util.ALL_RECORD){
			whereClause += " AND csp.chainSalesOrder.saler.user_id = " + salerId;
		}	
	 
		
		if (parentId == 0){
			//@2. 根节点
			String criteria = "SELECT SUM(quantity), SUM(retailPrice * discountRate * quantity), SUM(costPrice * quantity), SUM(retailPrice * (1 - discountRate) * quantity), csp.type FROM ChainStoreSalesOrderProduct csp WHERE csp.chainSalesOrder.status = ? AND csp.chainSalesOrder.orderDate BETWEEN ? AND ? "+ whereClause + " GROUP BY csp.type";
			name = chainStore.getChain_name() + " " + saler.getName();
			
			List<Object> values = chainSalesOrderDaoImpl.executeHQLSelect(criteria, value_sale.toArray(), null, true);
			
			ChainSalesStatisticReportItemVO rootItem = new ChainSalesStatisticReportItemVO(name, 1, yearId, quarterId, brandId, showCost, ChainSalesStatisticReportItemVO.STATE_CLOSED);
			
			if (values != null){
				for (Object record : values ){
					Object[] records = (Object[])record;
					int quantity = Common_util.getInt(records[0]);
					double sales = Common_util.getDouble(records[1]);
					double cost = Common_util.getDouble(records[2]);
					double salesDiscount = Common_util.getDouble(records[3]);
					int type = Common_util.getInt(records[4]);
					
					rootItem.putValue(quantity, type, sales, cost, salesDiscount);
				}
				
				rootItem.reCalculate();
			}
			
			reportItems.add(rootItem);
		} else if (yearId == 0){
			//@2. 展开所有年份的库存信息
			String criteria = "SELECT SUM(quantity), SUM(retailPrice * discountRate * quantity), SUM(costPrice * quantity), SUM(retailPrice * (1 - discountRate) * quantity), csp.productBarcode.product.year.year_ID,  csp.type FROM ChainStoreSalesOrderProduct csp WHERE csp.chainSalesOrder.status = ? AND csp.chainSalesOrder.orderDate BETWEEN ? AND ?  "+ whereClause + " GROUP BY csp.productBarcode.product.year.year_ID, csp.type";
			List<Object> values = chainSalesOrderDaoImpl.executeHQLSelect(criteria, value_sale.toArray(), null, true);
			
			
			if (values != null){
				Map<Integer, ChainSalesStatisticReportItemVO> dataMap = new HashMap<Integer, ChainSalesStatisticReportItemVO>();
				for (Object record : values ){
					Object[] records = (Object[])record;
					int quantity = Common_util.getInt(records[0]);
					double sales = Common_util.getDouble(records[1]);
					double cost = Common_util.getDouble(records[2]);
					double salesDiscount = Common_util.getDouble(records[3]);
					int yearIdDB = Common_util.getInt(records[4]);
					int type = Common_util.getInt(records[5]);
					
					ChainSalesStatisticReportItemVO levelFour = dataMap.get(yearIdDB);
					if (levelFour != null){
						levelFour.putValue(quantity, type, sales, cost, salesDiscount);
					} else {
						Year year = yearDaoImpl.get(yearIdDB, true);
						
						name = year.getYear() + "年";
						
						levelFour = new ChainSalesStatisticReportItemVO(name, 2, yearIdDB, quarterId, brandId, showCost, ChainSalesStatisticReportItemVO.STATE_CLOSED);

						levelFour.putValue(quantity, type, sales, cost, salesDiscount);
					}
					
					dataMap.put(yearIdDB, levelFour);
				}
				
				List<Integer> yearIds = new ArrayList<Integer>(dataMap.keySet());
				Collections.sort(yearIds);
				
				//1. 把基本对象放入
				for (Integer id : yearIds){
					ChainSalesStatisticReportItemVO levelFourItem = dataMap.get(id);
					levelFourItem.reCalculate();

					reportItems.add(levelFourItem);
				}	
			}
			
			
		} else if (quarterId == 0){
			//@2. 展开所有季的库存信息
			String criteria = "SELECT SUM(quantity), SUM(retailPrice * discountRate * quantity), SUM(costPrice * quantity), SUM(retailPrice * (1 - discountRate) * quantity), csp.productBarcode.product.quarter.quarter_ID,  csp.type FROM ChainStoreSalesOrderProduct csp WHERE csp.chainSalesOrder.status = ? AND csp.chainSalesOrder.orderDate BETWEEN ? AND ? AND csp.productBarcode.product.year.year_ID = " + yearId + whereClause + " GROUP BY csp.productBarcode.product.quarter.quarter_ID, csp.type";
			List<Object> values = chainSalesOrderDaoImpl.executeHQLSelect(criteria, value_sale.toArray(), null, true);
			
			
			if (values != null){
				Map<Integer, ChainSalesStatisticReportItemVO> dataMap = new HashMap<Integer, ChainSalesStatisticReportItemVO>();
				for (Object record : values ){
					Object[] records = (Object[])record;
					int quantity = Common_util.getInt(records[0]);
					double sales = Common_util.getDouble(records[1]);
					double cost = Common_util.getDouble(records[2]);
					double salesDiscount = Common_util.getDouble(records[3]);
					int quarterIdDB = Common_util.getInt(records[4]);
					int type = Common_util.getInt(records[5]);
					
					ChainSalesStatisticReportItemVO levelFour = dataMap.get(quarterIdDB);
					if (levelFour != null){
						levelFour.putValue(quantity, type, sales, cost, salesDiscount);
					} else {
						Year year = yearDaoImpl.get(yearId, true);
						Quarter quarter = quarterDaoImpl.get(quarterIdDB, true);
						name = year.getYear() + "年" + quarter.getQuarter_Name();
						
						levelFour = new ChainSalesStatisticReportItemVO(name, 3, yearId, quarterIdDB, brandId, showCost, ChainSalesStatisticReportItemVO.STATE_CLOSED);

						levelFour.putValue(quantity, type, sales, cost, salesDiscount);
					}
					
					dataMap.put(quarterIdDB, levelFour);
				}
				
				List<Integer> quarterIds = new ArrayList<Integer>(dataMap.keySet());
				Collections.sort(quarterIds);
				
				//1. 把基本对象放入
				for (Integer id : quarterIds){
					ChainSalesStatisticReportItemVO levelFourItem = dataMap.get(id);
					levelFourItem.reCalculate();

					reportItems.add(levelFourItem);
				}	
			}
		} else if (brandId == 0){
			//@2. 展开所有品牌的库存信息
			String criteria = "SELECT SUM(quantity), SUM(retailPrice * discountRate * quantity), SUM(costPrice * quantity), SUM(retailPrice * (1 - discountRate) * quantity), csp.productBarcode.product.brand.brand_ID,  csp.type FROM ChainStoreSalesOrderProduct csp WHERE csp.chainSalesOrder.status = ? AND csp.chainSalesOrder.orderDate BETWEEN ? AND ? AND csp.productBarcode.product.year.year_ID = " + yearId + " AND csp.productBarcode.product.quarter.quarter_ID = " + quarterId + whereClause + " GROUP BY csp.productBarcode.product.brand.brand_ID, csp.type";
			List<Object> values = chainSalesOrderDaoImpl.executeHQLSelect(criteria, value_sale.toArray(), null, true);
			
			
			if (values != null){
				Map<Integer, ChainSalesStatisticReportItemVO> dataMap = new HashMap<Integer, ChainSalesStatisticReportItemVO>();
				for (Object record : values ){
					Object[] records = (Object[])record;
					int quantity = Common_util.getInt(records[0]);
					double sales = Common_util.getDouble(records[1]);
					double cost = Common_util.getDouble(records[2]);
					double salesDiscount = Common_util.getDouble(records[3]);
					int brandIdDB = Common_util.getInt(records[4]);
					int type = Common_util.getInt(records[5]);
					
					ChainSalesStatisticReportItemVO levelFour = dataMap.get(brandIdDB);
					if (levelFour != null){
						levelFour.putValue(quantity, type, sales, cost, salesDiscount);
					} else {
						Brand brand = brandDaoImpl.get(brandIdDB, true);
						
						name = brand.getBrand_Name();
						
						levelFour = new ChainSalesStatisticReportItemVO(name, 4, yearId, quarterId, brandIdDB, showCost, ChainSalesStatisticReportItemVO.STATE_CLOSED);

						levelFour.putValue(quantity, type, sales, cost, salesDiscount);
					}
					
					dataMap.put(brandIdDB, levelFour);
				}
				
				List<Integer> brandIds = new ArrayList<Integer>(dataMap.keySet());
				Collections.sort(brandIds);
				
				//1. 把基本对象放入
				for (Integer id : brandIds){
					ChainSalesStatisticReportItemVO levelFourItem = dataMap.get(id);
					levelFourItem.reCalculate();

					reportItems.add(levelFourItem);
				}	
			}
		} else if (brandId != 0) {
			//@2. 展开所有品牌的库存信息
			String criteria = "SELECT SUM(quantity), SUM(retailPrice * discountRate * quantity), SUM(costPrice * quantity), SUM(retailPrice * (1 - discountRate) * quantity), csp.productBarcode.id,  csp.type FROM ChainStoreSalesOrderProduct csp WHERE csp.chainSalesOrder.status = ? AND csp.chainSalesOrder.orderDate BETWEEN ? AND ? AND csp.productBarcode.product.year.year_ID = " + yearId + " AND csp.productBarcode.product.quarter.quarter_ID = " + quarterId + " AND csp.productBarcode.product.brand.brand_ID = " + brandId + " GROUP BY csp.productBarcode.id, csp.type";
			List<Object> values = chainSalesOrderDaoImpl.executeHQLSelect(criteria, value_sale.toArray(), null, true);
			
			
			if (values != null){
				Map<Integer, ChainSalesStatisticReportItemVO> dataMap = new HashMap<Integer, ChainSalesStatisticReportItemVO>();
				for (Object record : values ){
					Object[] records = (Object[])record;
					int quantity = Common_util.getInt(records[0]);
					double sales = Common_util.getDouble(records[1]);
					double cost = Common_util.getDouble(records[2]);
					double salesDiscount = Common_util.getDouble(records[3]);
					int pbId = Common_util.getInt(records[4]);
					int type = Common_util.getInt(records[5]);
					
					ChainSalesStatisticReportItemVO levelFour = dataMap.get(pbId);
					if (levelFour != null){
						levelFour.putValue(quantity, type, sales, cost, salesDiscount);
					} else {
						ProductBarcode pb = productBarcodeDaoImpl.get(pbId, true);
						Color color = pb.getColor();
						String colorName = "";
						if (color != null)
							colorName = color.getName();
						
						name = pb.getProduct().getProductCode() + colorName;
						
						levelFour = new ChainSalesStatisticReportItemVO(name, 5, yearId, quarterId, brandId, showCost, ChainSalesStatisticReportItemVO.STATE_OPEN);

						levelFour.putValue(quantity, type, sales, cost, salesDiscount);
					}
					
					dataMap.put(pbId, levelFour);
				}
				
				List<Integer> pbIds = new ArrayList<Integer>(dataMap.keySet());
				Collections.sort(pbIds);
				
				//1. 把基本对象放入
				for (Integer id : pbIds){
					ChainSalesStatisticReportItemVO levelFourItem = dataMap.get(id);
					levelFourItem.reCalculate();

					reportItems.add(levelFourItem);
				}	
			}
		}

	    response.setReturnValue(reportItems);
	    return response;
	}

	/**
	 * 获取采购统计信息的方法
	 * @param parentId
	 * @param startDate
	 * @param endDate
	 * @param chain_id
	 * @param year_ID
	 * @param quarter_ID
	 * @param brand_ID
	 * @param userInfor
	 * @return
	 */
	public Response getPurchaseStatisticReptEles(int parentId, java.sql.Date startDate, java.sql.Date endDate,
			int chainId, int yearId, int quarterId, int brandId, ChainUserInfor userInfor) {
		Response response = new Response();
		
		List<Object> value_sale = new ArrayList<Object>();
		value_sale.add(InventoryOrder.STATUS_ACCOUNT_COMPLETE);
		value_sale.add(Common_util.formStartDate(startDate));
		value_sale.add(Common_util.formEndDate(endDate));

		
		boolean showCost = userInfor.containFunction("purchaseAction!seeCost");
		
		ChainStore chainStore = this.getThisChainStore(chainId);
		
		List<ChainPurchaseStatisticReportItemVO> reportItems = new ArrayList<ChainPurchaseStatisticReportItemVO>();
		String name = "";
		
		String whereClause = "";
		if (chainId != Common_util.ALL_RECORD){
			whereClause += " AND p.order.cust.id = " + chainStore.getClient_id();
		} else { 
			//ChainStore testChainStore = chainStoreDaoImpl.get(ChainStore.CHAIN_ID_TEST_ID, true);
			
			whereClause += " AND p.order.cust.id != " + SystemParm.getTestClientId();
		}

		if (parentId == 0){
			//@2. 根节点
			StringBuffer sql = new StringBuffer("SELECT SUM(quantity), SUM(wholeSalePrice * quantity), p.order.order_type   FROM InventoryOrderProduct p " + 
			         " WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ?");

			sql.append(whereClause);
			sql.append(" GROUP BY p.order.order_type");
		
			List<Object> values = inventoryOrderProductDAOImpl.executeHQLSelect(sql.toString(), value_sale.toArray(), null, true);

			name = chainStore.getChain_name();
			ChainPurchaseStatisticReportItemVO rootItem = new ChainPurchaseStatisticReportItemVO(name, 1, yearId, quarterId, brandId, showCost, ChainPurchaseStatisticReportItemVO.STATE_CLOSED);
			
			if (values != null){
				for (Object record : values ){
					Object[] records = (Object[])record;
					int quantity = Common_util.getInt(records[0]);
					double amount = Common_util.getDouble(records[1]);
					int type = Common_util.getInt(records[2]);
					
					rootItem.putValue(type, quantity, amount);
				}
				
				rootItem.reCalculate();
			}
			
			reportItems.add(rootItem);
		} else if (yearId == 0){
			//@2. 展开所有年份的库存信息
			StringBuffer sql = new StringBuffer("SELECT SUM(quantity), SUM(wholeSalePrice * quantity), p.productBarcode.product.year.year_ID, p.order.order_type   FROM InventoryOrderProduct p " + 
			         " WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ?");

			sql.append(whereClause);
			sql.append(" GROUP BY p.productBarcode.product.year.year_ID, p.order.order_type");
			
			List<Object> values = chainSalesOrderDaoImpl.executeHQLSelect(sql.toString(), value_sale.toArray(), null, true);
			
			Map<Integer, ChainPurchaseStatisticReportItemVO> dataMap = new HashMap<Integer, ChainPurchaseStatisticReportItemVO>();
			for (Object record : values ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				double amount = Common_util.getDouble(records[1]);
				int yearIdDB = Common_util.getInt(records[2]);
				int type = Common_util.getInt(records[3]);
				
				ChainPurchaseStatisticReportItemVO levelOneItem = dataMap.get(yearIdDB);
				if (levelOneItem != null){
					levelOneItem.putValue(type, quantity, amount);
				} else {
					Year year = yearDaoImpl.get(yearIdDB, true);
					name = year.getYear() + "年";
					
					levelOneItem = new ChainPurchaseStatisticReportItemVO(name, parentId, yearIdDB, quarterId, brandId, showCost, ChainPurchaseStatisticReportItemVO.STATE_CLOSED);
					levelOneItem.putValue(type, quantity, amount);
					
					dataMap.put(yearIdDB, levelOneItem);
				}
			}
			
			List<Integer> yearKey = new ArrayList<Integer>(dataMap.keySet());
			Collections.sort(yearKey);
			
			//1. 把基本对象放入
			//2. 计算总数
			for (Integer id : yearKey){
				ChainPurchaseStatisticReportItemVO levelOneItem = dataMap.get(id);
				levelOneItem.reCalculate();

				reportItems.add(levelOneItem);
			}		
			
		} else if (quarterId == 0){
			//@2. 展开所有季的库存信息
			StringBuffer sql = new StringBuffer("SELECT SUM(quantity), SUM(wholeSalePrice * quantity), p.productBarcode.product.quarter.quarter_ID, p.order.order_type   FROM InventoryOrderProduct p " + 
			         " WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ? AND p.productBarcode.product.year.year_ID =" + yearId);

			sql.append(whereClause);
			sql.append(" GROUP BY p.productBarcode.product.quarter.quarter_ID, p.order.order_type");
			
			List<Object> values = chainSalesOrderDaoImpl.executeHQLSelect(sql.toString(), value_sale.toArray(), null, true);
			
			Map<Integer, ChainPurchaseStatisticReportItemVO> dataMap = new HashMap<Integer, ChainPurchaseStatisticReportItemVO>();
			for (Object record : values ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				double amount = Common_util.getDouble(records[1]);
				int quarterIdDB = Common_util.getInt(records[2]);
				int type = Common_util.getInt(records[3]);
				
				ChainPurchaseStatisticReportItemVO levelOneItem = dataMap.get(quarterIdDB);
				if (levelOneItem != null){
					levelOneItem.putValue(type, quantity, amount);
				} else {
					Year year = yearDaoImpl.get(yearId, true);
					Quarter quarter = quarterDaoImpl.get(quarterIdDB, true);
					name = year.getYear() + "年 " + quarter.getQuarter_Name();
					
					levelOneItem = new ChainPurchaseStatisticReportItemVO(name, parentId, yearId, quarterIdDB, brandId, showCost, ChainPurchaseStatisticReportItemVO.STATE_CLOSED);
					levelOneItem.putValue(type, quantity, amount);
					
					dataMap.put(quarterIdDB, levelOneItem);
				}
			}
			
			List<Integer> quarterKey = new ArrayList<Integer>(dataMap.keySet());
			Collections.sort(quarterKey);
			
			//1. 把基本对象放入
			//2. 计算总数
			for (Integer id : quarterKey){
				ChainPurchaseStatisticReportItemVO levelOneItem = dataMap.get(id);
				levelOneItem.reCalculate();

				reportItems.add(levelOneItem);
			}		
		} else if (brandId == 0){
			//@2. 展开所有品牌的库存信息
			StringBuffer sql = new StringBuffer("SELECT SUM(quantity), SUM(wholeSalePrice * quantity), p.productBarcode.product.brand.brand_ID, p.order.order_type FROM InventoryOrderProduct p " + 
			         " WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ? AND p.productBarcode.product.year.year_ID =" + yearId + " AND p.productBarcode.product.quarter.quarter_ID =" + quarterId);

			sql.append(whereClause);
			sql.append(" GROUP BY p.productBarcode.product.brand.brand_ID, p.order.order_type");
			
			List<Object> values = chainSalesOrderDaoImpl.executeHQLSelect(sql.toString(), value_sale.toArray(), null, true);
			
			Map<Integer, ChainPurchaseStatisticReportItemVO> dataMap = new HashMap<Integer, ChainPurchaseStatisticReportItemVO>();
			for (Object record : values ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				double amount = Common_util.getDouble(records[1]);
				int brandIdDB = Common_util.getInt(records[2]);
				int type = Common_util.getInt(records[3]);
				
				ChainPurchaseStatisticReportItemVO levelOneItem = dataMap.get(brandIdDB);
				if (levelOneItem != null){
					levelOneItem.putValue(type, quantity, amount);
				} else {
					Brand brand = brandDaoImpl.get(brandIdDB, true);
					name = brand.getBrand_Name();
					
					levelOneItem = new ChainPurchaseStatisticReportItemVO(name, parentId, yearId, quarterId, brandIdDB, showCost, ChainPurchaseStatisticReportItemVO.STATE_CLOSED);
					levelOneItem.putValue(type, quantity, amount);
					
					dataMap.put(brandIdDB, levelOneItem);
				}
			}
			
			List<Integer> brandKey = new ArrayList<Integer>(dataMap.keySet());
			Collections.sort(brandKey);
			
			//1. 把基本对象放入
			//2. 计算总数
			for (Integer id : brandKey){
				ChainPurchaseStatisticReportItemVO levelOneItem = dataMap.get(id);
				levelOneItem.reCalculate();

				reportItems.add(levelOneItem);
			}		
		} else if (brandId != 0) {
			//@2. 展开所有品牌的库存信息
			StringBuffer sql = new StringBuffer("SELECT SUM(quantity), SUM(wholeSalePrice * quantity), p.productBarcode.id, p.order.order_type FROM InventoryOrderProduct p " + 
			         " WHERE p.order.order_Status = ? AND p.order.order_EndTime BETWEEN ? AND ? AND p.productBarcode.product.year.year_ID =" + yearId + " AND p.productBarcode.product.quarter.quarter_ID =" + quarterId+ " AND p.productBarcode.product.brand.brand_ID =" + brandId);

			sql.append(whereClause);
			sql.append(" GROUP BY p.productBarcode.id, p.order.order_type");
			
			List<Object> values = chainSalesOrderDaoImpl.executeHQLSelect(sql.toString(), value_sale.toArray(), null, true);
			
			Map<Integer, ChainPurchaseStatisticReportItemVO> dataMap = new HashMap<Integer, ChainPurchaseStatisticReportItemVO>();
			for (Object record : values ){
				Object[] records = (Object[])record;
				int quantity = Common_util.getInt(records[0]);
				double amount = Common_util.getDouble(records[1]);
				int pbId = Common_util.getInt(records[2]);
				int type = Common_util.getInt(records[3]);
				
				ChainPurchaseStatisticReportItemVO levelOneItem = dataMap.get(pbId);
				if (levelOneItem != null){
					levelOneItem.putValue(type, quantity, amount);
				} else {
					ProductBarcode pb = productBarcodeDaoImpl.get(pbId, true);
					Color color = pb.getColor();
					String colorName = "";
					if (color != null)
						colorName = color.getName();
					
					Category category = pb.getProduct().getCategory();
					
					name = category.getCategory_Name() + " " + pb.getProduct().getProductCode() + colorName;
					
					levelOneItem = new ChainPurchaseStatisticReportItemVO(name, parentId, yearId, quarterId, brandId, showCost, ChainPurchaseStatisticReportItemVO.STATE_OPEN);
					levelOneItem.putValue(type, quantity, amount);
					
					dataMap.put(pbId, levelOneItem);
				}
			}
			
			List<Integer> pbKey = new ArrayList<Integer>(dataMap.keySet());
			Collections.sort(pbKey);
			
			//1. 把基本对象放入
			//2. 计算总数
			for (Integer id : pbKey){
				ChainPurchaseStatisticReportItemVO levelOneItem = dataMap.get(id);
				levelOneItem.reCalculate();

				reportItems.add(levelOneItem);
			}
		}

	    response.setReturnValue(reportItems);
	    return response;
	}



}
