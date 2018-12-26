package com.onlineMIS.ORM.DAO.headQ.inventory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
















import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.chainS.chainMgmt.ChainPriceIncrementDaoImpl;
import com.onlineMIS.ORM.DAO.chainS.inventoryFlow.ChainInOutStockDaoImpl;
import com.onlineMIS.ORM.DAO.chainS.user.ChainStoreDaoImpl;
import com.onlineMIS.ORM.DAO.chainS.user.ChainStoreService;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.ProductBarcodeDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.ProductDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.custMgmt.HeadQCustDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.finance.FinanceBillImpl;
import com.onlineMIS.ORM.DAO.headQ.finance.FinanceCategoryImpl;
import com.onlineMIS.ORM.DAO.headQ.finance.FinanceService;
import com.onlineMIS.ORM.DAO.headQ.finance.HeadQAcctFlowDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.user.NewsService;
import com.onlineMIS.ORM.DAO.headQ.user.UserInforService;
import com.onlineMIS.ORM.entity.chainS.chainMgmt.ChainPriceIncrement;
import com.onlineMIS.ORM.entity.chainS.inventoryFlow.ChainInOutStock;
import com.onlineMIS.ORM.entity.chainS.user.ChainRoleType;
import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.chainS.user.ChainUserInfor;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Brand;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Product;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.ORM.entity.headQ.custMgmt.HeadQCust;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBill;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceBillItem;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceCategory;
import com.onlineMIS.ORM.entity.headQ.finance.HeadQAcctFlow;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadQInventoryStock;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadQInventoryStore;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadQSalesHistory;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrder;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrderProduct;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrderTemplate;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrderVO;
import com.onlineMIS.ORM.entity.headQ.inventory.JinSuanOrderTemplate;
import com.onlineMIS.ORM.entity.headQ.inventory.PDAOrderTemplate;
import com.onlineMIS.ORM.entity.headQ.user.UserFunctionality;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.action.headQ.inventory.InventoryOrderActionFormBean;
import com.onlineMIS.action.headQ.inventory.InventoryOrderActionUIBean;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.HttpUtil;
import com.onlineMIS.common.loggerLocal;
import com.onlineMIS.filter.SystemParm;
import com.onlineMIS.sorter.SortByBrandProductCode;
import com.opensymphony.xwork2.ActionContext;

@Service
public class WholeSalesService {
	@Autowired
    private InventoryOrderProductDAOImpl inventoryOrderProductDAOImpl;
	@Autowired
	private InventoryOrderDAOImpl inventoryOrderDAOImpl;

	@Autowired
	private HeadQSalesHisDAOImpl headQSalesHisDAOImpl;

	@Autowired
	private ProductBarcodeDaoImpl productBarcodeDaoImpl;
	@Autowired
	private UserInforService userInforService;
	@Autowired
	private HeadQAcctFlowDaoImpl chainAcctFlowDaoImpl;
	@Autowired
	private ChainStoreDaoImpl chainStoreDaoImpl;
	@Autowired
	private ChainPriceIncrementDaoImpl chainPriceIncrementDaoImpl;
	@Autowired
	private HeadQCustDaoImpl headQCustDaoImpl;
	@Autowired
    private HeadQInventoryStoreDAOImpl headQInventoryStoreDAOImpl;
	@Autowired
	private HeadQInventoryStockDAOImpl headQInventoryStockDAOImpl;
	@Autowired
	private FinanceBillImpl financeBillImpl;
	@Autowired
	private FinanceCategoryImpl financeCategoryImpl;
	@Autowired
	private FinanceService financeService;
	/**
	 * function to preview the inventory order
	 * @param formBean
	 * @return
	 */
	public InventoryOrder previewOrder(InventoryOrderActionFormBean formBean){
	       InventoryOrder order = groupInventoryOrder(formBean.getOrder(), formBean.getSorting());
	       
	       formBean.setIsPreview(true);
	       formBean.setOrder(order);
	       
	       return order;
	}

	/**
	 * function to group the products with same barcode
	 * 
	 * @param order
	 * @return
	 */
	private InventoryOrder groupInventoryOrder(InventoryOrder order, String sorting){
		/**
		 * this is the barcode list with a sequence
		 */
		Set<String> barcodes = new LinkedHashSet<String>();
		Set<String> barcodeDis = new LinkedHashSet<String>();
		
		/**
		 * 1. to store the non-duplicated product code in a sequence list
		 */
		List<InventoryOrderProduct> orderProducts = order.getProduct_List();
		for (InventoryOrderProduct orderProduct: orderProducts){
			if (orderProduct!= null && orderProduct.getProductBarcode() != null && !orderProduct.getProductBarcode().getBarcode().equals("")){
				String discount = String.valueOf(orderProduct.getDiscount());
				String barcode = orderProduct.getProductBarcode().getBarcode();
				String mapKey = barcode + discount;
			    barcodes.add(barcode);
			    barcodeDis.add(mapKey);
			}
		}
		Map<String, ProductBarcode> productMap = productBarcodeDaoImpl.getProductMapByBarcode(barcodes);
		
		
		/**
		 * 2. to group the products' quantity, recost, whole sales, retail sales
		 */
		HashMap<String,InventoryOrderProduct> orderProductMap = new HashMap<String,InventoryOrderProduct>();
		for (InventoryOrderProduct orderProduct: orderProducts){
			if (orderProduct!= null && orderProduct.getProductBarcode() != null && !orderProduct.getProductBarcode().getBarcode().equals("")){
				String discount = String.valueOf(orderProduct.getDiscount());
				String barcode = orderProduct.getProductBarcode().getBarcode();
				
				String mapKey = barcode + discount;
				
				int boughtBefore = orderProduct.getProductBarcode().getBoughtBefore();
				if (orderProductMap.containsKey(mapKey)){
					InventoryOrderProduct original_orderProduct = orderProductMap.get(mapKey);
					int addQuantity = orderProduct.getQuantity();
					int orignalQuantity = original_orderProduct.getQuantity();
					original_orderProduct.setQuantity(addQuantity + orignalQuantity);
					if (boughtBefore != 0){
						original_orderProduct.getProductBarcode().setBoughtBefore(boughtBefore);
					}
				} else {
					orderProductMap.put(mapKey, orderProduct);
				}
			}
		}
		order.setOrder_ComplTime(new Date());
		
		/**
		 * 3. to set the more than two hands
		 */
		Iterator<String> keys = orderProductMap.keySet().iterator();
		while (keys.hasNext()){
			String key = keys.next();
			InventoryOrderProduct orderProduct = orderProductMap.get(key);

			int q = orderProduct.getQuantity();
			int numPerHand = orderProduct.getProductBarcode().getProduct().getNumPerHand();
			String barcode = orderProduct.getProductBarcode().getBarcode();
			String discount = String.valueOf(orderProduct.getDiscount());

			String mapKey = barcode + discount;
			
			ProductBarcode pb = productMap.get(barcode);
			Product product = pb.getProduct();
			if (pb == null){
				loggerLocal.error("Could not get the product from Map. Order Id :" + order.getOrder_ID() +", barcode :" + barcode);
			} else {
				orderProduct.setProductBarcode(pb);
				orderProduct.setRecCost(product.getRecCost());
			}
			
			if (q >= numPerHand*2){
				orderProduct.setMoreThanTwoHan(true);
			} else {
				orderProduct.setMoreThanTwoHan(false);
			}
			orderProductMap.put(mapKey, orderProduct);
		}
		
		/**
		 * 4. rebuild the product list
		 */
		if (sorting.equalsIgnoreCase("true")){
			Collection<InventoryOrderProduct> OrderProducts_c = orderProductMap.values();
			List<InventoryOrderProduct> newOrderProducts = new ArrayList<InventoryOrderProduct>();
			newOrderProducts.addAll(OrderProducts_c);
			Collections.sort(newOrderProducts, new SortByBrandProductCode());
		    order.setProduct_List(newOrderProducts);
    	    return order;
		}else {
			List<InventoryOrderProduct> OrderProducts_org = new ArrayList<InventoryOrderProduct>();
			for (String barcodeD: barcodeDis){
				OrderProducts_org.add(orderProductMap.get(barcodeD));
			}
		    order.setProduct_List(OrderProducts_org);
    	    return order;
		}
		
	}
	
	
    /**
     * inventroy submit the order to account
     * @param order
     * @return
     * @throws Exception 
     */
	@Transactional
    public boolean inventoryComplsave(InventoryOrderActionFormBean formBean, UserInfor auditor){
    	
    	/**
    	 * 1. save the order
    	 */
    	InventoryOrder order = groupInventoryOrder(formBean.getOrder(), formBean.getSorting());

		order.setOrder_Status(InventoryOrder.STATUS_ACCOUNT_PROCESS);
		
		order.setOrder_ComplTime(new Date());
		
//		order.setOrder_Auditor(auditor);
		
		boolean saveOrder = false;
		try{
			saveOrder =  save(order);
		} catch (Exception e) {
			loggerLocal.error(e);
			saveOrder = false;
		}

    	return saveOrder;
    }
   

	/**
     * inventroy submit the order to account
     * @param order
     * @return
	 * @throws Exception 
     */
    @Transactional
    public boolean saveToDraft(InventoryOrder order, String sorting){

		order.setOrder_Status(InventoryOrder.STATUS_DRAFT);

		order = groupInventoryOrder(order, sorting);
		
		return save(order);
    }
    
    
    /**
     * search the order by the search criteria from the web page
     * @param formBean
     * @return
     */
	public List<InventoryOrder> search(InventoryOrderActionFormBean formBean) {
		DetachedCriteria criteria = DetachedCriteria.forClass(InventoryOrder.class,"order");
		
		InventoryOrder searchBean = formBean.getOrder();
		
		if (searchBean.getOrder_ID() != 0){
			criteria.add(Restrictions.eq("order.order_ID", searchBean.getOrder_ID()));
			
			criteria.add(Restrictions.ne("order.order_Status", InventoryOrder.STATUS_DELETED));
		} else {
			Date startTime = formBean.getSearch_Start_Time();
			Date endTime = formBean.getSearch_End_Time();
	
			if (searchBean.getOrder_Status() != Common_util.ALL_RECORD)
				criteria.add(Restrictions.eq("order.order_Status", searchBean.getOrder_Status()));
			else
				criteria.add(Restrictions.ne("order.order_Status", InventoryOrder.STATUS_DELETED));
			
			if (searchBean.getCust().getId() != 0 && !searchBean.getCust().getName().equals("")){
				int clientid = searchBean.getCust().getId();
	
				criteria.add(Restrictions.eq("order.cust.id", clientid));
			}
			
			if (searchBean.getOrder_Auditor() != null && searchBean.getOrder_Auditor().getUser_id() != Common_util.ALL_RECORD)
				criteria.add(Restrictions.eq("order.order_Auditor.user_id", searchBean.getOrder_Auditor().getUser_id()));
			
			if (startTime != null && endTime != null){
				Date end_date = Common_util.formEndDate(endTime);
				criteria.add(Restrictions.between("order.order_StartTime",startTime,end_date));
			}
			
			if (searchBean.getOrder_type() != Common_util.ALL_RECORD)
				criteria.add(Restrictions.eq("order.order_type",searchBean.getOrder_type()));
			
			criteria.addOrder(Order.asc("order.order_StartTime"));
		}
		return inventoryOrderDAOImpl.search(criteria);
	}

	/**
	 * function to get the order by id
	 * @param order_ID
	 * @return
	 */
	@Transactional
	public InventoryOrder searchByID(int order_ID) {
		InventoryOrder order_r =  inventoryOrderDAOImpl.retrieveOrder(order_ID);
		
		order_r.putSetToList();
		
		return order_r;
	}



	/**
	 * This is a common function that will be used by other save functions
	 * it will build the index and put the list to set when save
	 * @param order
	 * @return
	 * @throws Exception 
	 */
	private boolean save(InventoryOrder order) {
		boolean isSucess = true;
		/**
		 * if order_org == null, it is a newly inserted draft order.
		 * or it is drafted before, we need check whether we shall delete
		 * records
		 */
		int orderId = order.getOrder_ID();
				
		if (orderId != 0){
			//1. to check the non exist products
			InventoryOrder order_org = inventoryOrderDAOImpl.retrieveOrder(orderId);
			order_org.putSetToList();
			List<Integer> Products_org = retrieveProductID(order_org.getProduct_List());
			List<Integer> products_form = retrieveProductID(order.getProduct_List());
            Set<Integer> delete_id = new HashSet<Integer>();
            for (int i: Products_org){
            	if (!products_form.contains(i))
            		delete_id.add(i);
            }
            
            //2. to check the quantity is zero
            List<InventoryOrderProduct> products = order.getProduct_List();
            for (InventoryOrderProduct product : products){
            	if (product != null){
	            	int quantity = product.getQuantity();
	            	if (quantity == 0)
	            		delete_id.add(product.getID());
            	}
            }
            
            if (delete_id.size() > 0)
               inventoryOrderProductDAOImpl.deleteInventoryProducts(delete_id);
            
            inventoryOrderDAOImpl.evict(order_org);
		}
		
		if (order.getPdaScanner() != null && order.getPdaScanner().getUser_id() == 0){
			order.setPdaScanner(null);
		}
		
		order.buildIndex();
		order.putListToSet();
		inventoryOrderDAOImpl.saveOrUpdate(order,true);

		return isSucess;
	}
	
	private List<Integer> retrieveProductID(
			List<InventoryOrderProduct> products) {
		List<Integer> ids =new ArrayList<Integer>();
		for (InventoryOrderProduct product:products){
			if (product != null)
			  ids.add(product.getID());
		}
		return ids;
	}
	

	/**
	 * to delete a order by mark it as deleted status
	 * but if the order has been finished by acct, it can only be 红冲
	 * @param order_ID
	 */
	public Response delete(int order_ID) {
        InventoryOrder order = inventoryOrderDAOImpl.get(order_ID, true);
        Response response = new Response();
		
        if (order!= null && order.getOrder_Status() != InventoryOrder.STATUS_ACCOUNT_COMPLETE){
			String hql_order = "UPDATE InventoryOrder i set i.order_Status = ? where order_ID = ?";
			Object[] values = {InventoryOrder.STATUS_DELETED,order_ID};
			
			inventoryOrderDAOImpl.executeHQLUpdateDelete(hql_order, values, true);
			response.setReturnCode(Response.SUCCESS);
        } else {
        	response.setReturnCode(Response.FAIL);
        }
        
        return response;
	}
	
	/**
	 * 通过授权删除单据
	 * but if the order has been finished by acct, it can only be 红冲
	 * @param order_ID
	 */
	@Transactional
	public Response deleteByAuthorization(int order_ID, String userName, String password) {
		//1. 验证用户名和密码
		Response response = userInforService.validateUser(userName, password);
		
		if (response.getReturnCode() == Response.SUCCESS){
			UserInfor user = (UserInfor)response.getReturnValue();
			
			if (!user.containFunction(UserFunctionality.CONFIRM_DELETE_ORDER)){
				response.setQuickValue(Response.FAIL, userName + " 还没有删除单据权限");
			} else {
		        InventoryOrder order = inventoryOrderDAOImpl.get(order_ID, true);
				
		        if (order!= null && order.getOrder_Status() != InventoryOrder.STATUS_ACCOUNT_COMPLETE){
					String hql_order = "UPDATE InventoryOrder i set i.order_Status = ? where order_ID = ?";
					Object[] values = {InventoryOrder.STATUS_DELETED,order_ID};
					
					inventoryOrderDAOImpl.executeHQLUpdateDelete(hql_order, values, true);
					response.setReturnCode(Response.SUCCESS);
		        } else {
		        	response.setQuickValue(Response.FAIL, order_ID + " 单据已经会计过账,不能删除");
		        }
			}
		}     
        return response;
	}
	
	/**
	 * the service to cancel the order 红冲单据
	 * @param orderId
	 * @return
	 */
	@Transactional
	public Response cancel(UserInfor userInfor, int orderId){
        InventoryOrder order = inventoryOrderDAOImpl.get(orderId, true);
        Response response = new Response();
        
        int clientId = order.getCust().getId();
        
        
        if ( order.getOrder_Status() == InventoryOrder.STATUS_ACCOUNT_COMPLETE){
        	ChainStore store = chainStoreDaoImpl.getByClientId(clientId);
        	if (store != null && (order.getChainConfirmStatus() == InventoryOrder.STATUS_CHAIN_CONFIRM || order.getChainConfirmStatus() == InventoryOrder.STATUS_SYSTEM_CONFIRM)){
    			response.setQuickValue(Response.WARNING, "客户已经确认此单据收货，无法红冲。请与管理员联系");
    			response.setReturnValue(order);
    			return response;
    		} 

        	//1. to update the order status
			String hql_order = "UPDATE InventoryOrder i set i.order_Status = ?, i.order_Auditor.user_id=? where order_ID = ?";
			Object[] values = {InventoryOrder.STATUS_CANCELLED,userInfor.getUser_id(), orderId};
			inventoryOrderDAOImpl.executeHQLUpdateDelete(hql_order, values, true);

			//3. to update the acct flow
			updateChainAcctFlow(order, order.getOrder_EndTime(), true);
 
			//4. update inventory io
			updateInventoryStock(order, true);
			
			//5. update financebill
			updateFinanceBill(order, userInfor, true);
			
			response.setReturnCode(Response.SUCCESS);
        } else {
        	response.setReturnCode(Response.FAIL);
        	response.setMessage("会计未过账的单据不能红冲,只能删除");
        }
        
        return response;
	}
	


	/**
     * function to copy the order
     * @param loginUserInfor
     * @param order
     * @return
	 * @throws Exception 
     */
	@Transactional
	public Response copyOrder(UserInfor loginUserInfor,
			InventoryOrder order) {
		Response response = new Response();
		int orderId = order.getOrder_ID();
		
		if (orderId > 0){
			InventoryOrder orderInDB = inventoryOrderDAOImpl.retrieveOrder(orderId);
			order = inventoryOrderDAOImpl.copy(orderInDB);
            String comment = order.getComment();
            
            order.setComment(comment + "\n 复制于单据" + orderId);
			order.setOrder_Auditor(loginUserInfor);
			order.setOrder_Status(InventoryOrder.STATUS_ACCOUNT_PROCESS);
			
			inventoryOrderDAOImpl.save(order, true);

			response.setReturnCode(Response.SUCCESS);
			response.setReturnValue(order.getOrder_ID());
		} else 
			response.setReturnCode(Response.FAIL);
		
		return response;
	}

	/**
	 * when accoutant view the order and wants to edit it
	 * @param order
	 * @return
	 * @throws Exception 
	 */
	@Transactional
	public boolean acctEditAndSave(InventoryOrderActionFormBean formBean, UserInfor order_Auditor) {
		InventoryOrder order = formBean.getOrder();
		
		
		InventoryOrder order_r = inventoryOrderDAOImpl.retrieveOrder(order.getOrder_ID());
		
		if (order_r.getOrder_Status() == InventoryOrder.STATUS_ACCOUNT_PROCESS){
			order = groupInventoryOrder(formBean.getOrder(), formBean.getSorting());

			order.setOrder_ComplTime(order_r.getOrder_ComplTime());
			order.setOrder_EndTime(order_r.getOrder_EndTime());
			order.setOrder_Auditor(order_Auditor);
		} 
//		else {
//	    	order = groupInventoryOrder(formBean.getOrder(), formBean.getSorting());
//	
//			order.setOrder_Status(InventoryOrder.STATUS_ACCOUNT_PROCESS);
//
//			order.setOrder_Auditor(order_Auditor);
//			
//			order.setOrder_ComplTime(new Date());
//		}
		
		try{
			inventoryOrderDAOImpl.evict(order_r);
			return save(order);
		} catch (Exception e) {
			return false;
		}	

	} 
	
    /**
     * 完成精算导入之后，只要修改状态和sales history
     * @param Status
     * @param order_id
     */
//	private void orderCompleteByImportJinsuan(InventoryOrder order) {
//		//1. 检查是否需要自动审核
//		int clientId = order.getClient_id();
//		ChainStore chainStore = chainStoreDaoImpl.getByClientId(clientId);
//		int orderStatus = InventoryOrder.STATUS_WAITING_AUDIT;
//		if (chainStore == null)
//			orderStatus = InventoryOrder.STATUS_ACCOUNT_COMPLETE;
//		
//		//2. update the order information
//		int order_id = order.getOrder_ID();
//		int importTimes  = order.getImportTimes() + 1;
//		Date now = new Date();
//		String hql = "update InventoryOrder set order_Status =?, order_EndTime=?, importTimes=? where order_ID=?";
//		Object[] values = {orderStatus, now, importTimes,order_id};
//		inventoryOrderDAOImpl.executeHQLUpdateDelete(hql, values, false);
//		
//		//3. update the sales history
//		updateSalesHistory(order);
//
//	}



	/**
     * to change the order status 
     * @param Status
     * @param order_id
     */
	@Transactional
	public Response orderCompleteAudit(int orderId, UserInfor user) {
		Response response = new Response();
		
		InventoryOrder order = inventoryOrderDAOImpl.retrieveOrder(orderId);
		
		Date now = new Date();
		String hql = "update InventoryOrder set order_Status =?, order_EndTime=? where order_ID=?";
		Object[] values = {InventoryOrder.STATUS_ACCOUNT_COMPLETE, now, orderId};
		
		inventoryOrderDAOImpl.executeHQLUpdateDelete(hql, values, false);

		//2. update the sales history
		updateSalesHistory(order);
		
		//3. 更新总部的库存
		updateInventoryStock(order, false);
		
		//4. update the acct flow
		updateChainAcctFlow(order,now, false);
		
		//5. 如果单据有付款，就创建财物单据
		int financeBillId = updateFinanceBill(order, user, false);
		if (financeBillId != 0){
			String hql2 = "update InventoryOrder set financeBillId=? where order_ID=?";
			Object[] values2 = {financeBillId, orderId};
			
			inventoryOrderDAOImpl.executeHQLUpdateDelete(hql2, values2, false);
		}
		
		response.setReturnCode(Response.SUCCESS);
		response.setReturnValue(order);

		
		return response;
	}
	
	private int updateFinanceBill(InventoryOrder order, UserInfor user, boolean isCancel) {
		if (isCancel){
			int financeBillId = order.getFinanceBillId();
			if (financeBillId != 0){
				FinanceBill financeBill = new FinanceBill();
				financeBill.setId(financeBillId);
				
				financeService.cancelFHQBill(financeBill, user, 2);
			}
			return 0;
		} else {
			double cash = order.getCash();
			double card = order.getCard();
			double alipay = order.getAlipay();
			double wechat = order.getWechat();
			double invoiceTotal = cash + card + alipay+ wechat;
			
			if (invoiceTotal != 0) {
				HeadQCust cust = order.getCust();
				UserInfor creator = order.getOrder_Auditor();
				int billType = FinanceBill.FINANCE_INCOME_HQ;
				int inventoryOrderId = order.getOrder_ID();
				String comment = "销售单据" + inventoryOrderId + "付款";
				
				double invoiceDiscount = 0;
				
				FinanceBill financeBill = new FinanceBill(billType, creator, cust, invoiceTotal, invoiceDiscount, new java.sql.Date(order.getOrder_EndTime().getTime()), comment,inventoryOrderId );
				List<FinanceBillItem> financeBillList = new ArrayList<FinanceBillItem>();
				
				//添加cash
				FinanceCategory cashCategory = financeCategoryImpl.getByTypeId(FinanceCategory.CASH_ACCT_TYPE);
				FinanceBillItem cashItem = new FinanceBillItem(cashCategory, cash, "");
				financeBillList.add(cashItem);
				
				//添加card
				FinanceCategory cardCategory = financeCategoryImpl.getByTypeId(FinanceCategory.CARD_ACCT_TYPE);
				FinanceBillItem cardItem = new FinanceBillItem(cardCategory, card, "");
				financeBillList.add(cardItem);
				
				//添加alipay
				FinanceCategory alipayCategory = financeCategoryImpl.getByTypeId(FinanceCategory.ALIPAY_ACCT_TYPE);
				FinanceBillItem alipayItem = new FinanceBillItem(alipayCategory, alipay, "");
				financeBillList.add(alipayItem);
				
				//添加wechat
				FinanceCategory wechatCategory = financeCategoryImpl.getByTypeId(FinanceCategory.WECHAT_ACCT_TYPE);
				FinanceBillItem wechatItem = new FinanceBillItem(wechatCategory, wechat, "");
				financeBillList.add(wechatItem);
				
				financeBill.setFinanceBillItemList(financeBillList);
				
				financeService.postFHQBill(financeBill, creator);
				
				return financeBill.getId();
			} else {
				return 0;
			}
		}
	}

	/**
	 * 单据被确认或者被红冲时需要更新总部的库存
	 * @param order
	 * @param b
	 */
	private void updateInventoryStock(InventoryOrder order, boolean isCancel) {
		//如果是测试连锁店不需要
		int TEST_CLIENT_ID = SystemParm.getTestClientId();
		
		if (order.getCust().getId() == TEST_CLIENT_ID)
			return;
		
		//更新库存数据
		HeadQInventoryStore store = order.getStore();
		int storeId = store.getId();
		
		int offset = isCancel ? -1 : 1;

		if (order.getOrder_type() == InventoryOrder.TYPE_SALES_ORDER_W){
			offset *= -1;			
		}
		
		Set<HeadQInventoryStock> stocks = new HashSet<HeadQInventoryStock>();
		 Iterator<InventoryOrderProduct> orderProducts = order.getProduct_Set().iterator();
		 while (orderProducts.hasNext()){
			 InventoryOrderProduct orderProduct = orderProducts.next();
			 int pbId = orderProduct.getProductBarcode().getId();
			 double cost = orderProduct.getRecCost();
			 double wholeSalePrice = orderProduct.getWholeSalePrice();
			 int quantity = orderProduct.getQuantity() * offset;
			 double costTotal = cost * quantity;
			 double wholeSalesTotal = wholeSalePrice * quantity;

			 ProductBarcode pb = productBarcodeDaoImpl.get(pbId, true);
			 
			 HeadQInventoryStock stock = new HeadQInventoryStock(storeId, cost, costTotal, wholeSalePrice, wholeSalesTotal, quantity, pb);
			 stocks.add(stock);
		 }
			
		 headQInventoryStockDAOImpl.updateInventoryStocks(stocks);
	}

	/**
	 * to update the inventory order's sales history
	 * @param order
	 */
	
	private void updateSalesHistory(InventoryOrder order){
		 //save the product sales price information to history table, if it is a sales order
		 if (order.getOrder_type() == InventoryOrder.TYPE_SALES_ORDER_W){
			 Set<HeadQSalesHistory> historySet = new HashSet<HeadQSalesHistory>();
			 Iterator<InventoryOrderProduct> orderProducts = order.getProduct_Set().iterator();
			 while (orderProducts.hasNext()){
				 InventoryOrderProduct orderProduct = orderProducts.next();

				 HeadQSalesHistory salesHistory = new HeadQSalesHistory(orderProduct.getProductBarcode().getId(), order.getCust().getId(), orderProduct.getRecCost(), orderProduct.getWholeSalePrice(), orderProduct.getSalesPrice(), orderProduct.getQuantity(), orderProduct.getSalePriceSelected(), orderProduct.getDiscount());
				 
				 historySet.add(salesHistory);
			 }
			 
			 Iterator<HeadQSalesHistory> historyIt = historySet.iterator();
			 while (historyIt.hasNext()){
				 HeadQSalesHistory history = historyIt.next();
				 headQSalesHisDAOImpl.saveOrUpdate(history, false);
			 }
		 }
	}
	
	/**
	 * to update the chain's acct flow
	 * @param order
	 * @param b
	 */
    private void updateChainAcctFlow(InventoryOrder order, Date date,  boolean isCancel) {
    	int clientId = order.getCust().getId();
		
    	HeadQCust cust = headQCustDaoImpl.get(clientId, true);
    	
		int orderId = order.getOrder_ID();
    	
    	int orderType = order.getOrder_type();
    	double totalAmt = order.getTotalWholePrice();
    	double totalDis = order.getTotalDiscount();
    	double netAmt = totalAmt - totalDis;
    	
    	//1. update the offset
    	int offset = 1;
		if (orderType == InventoryOrder.TYPE_SALES_RETURN_ORDER_W)
			offset *= -1;
			
		if (isCancel)
			offset *= -1;
    	
		netAmt *= offset;
		
		//2.update the order's preAcctAmt and postAcctAmt
		double initialAcctAmt = cust.getInitialAcctBalance();  		
		double acctAmtFlow = chainAcctFlowDaoImpl.getAccumulateAcctFlow(clientId);
		double preAcctAmt = Common_util.getDecimalDouble(initialAcctAmt + acctAmtFlow);
		double postAcctAmt = Common_util.getDecimalDouble(preAcctAmt + netAmt);
		if (!isCancel){  
			String hql = "update InventoryOrder set preAcctAmt =?, postAcctAmt=? where order_ID=?";
			Object[] values = {preAcctAmt, postAcctAmt, orderId};
			
			inventoryOrderDAOImpl.executeHQLUpdateDelete(hql, values, false);
		}
		
		cust.setCurrentAcctBalance(postAcctAmt);
		headQCustDaoImpl.update(cust, true);
		
		HeadQAcctFlow chainAcctFlow = new HeadQAcctFlow(clientId, netAmt, "S," + orderId + "," + isCancel, date);
		chainAcctFlowDaoImpl.save(chainAcctFlow, true);
	
	}
	

	
	/**
	 * to generate the excel order
	 * @param order
	 * @param templatePosition
	 * @return
	 */
	@Transactional
	public Map<String,Object> generateExcelReport(InventoryOrder order,String templatePosition) {   
		Map<String,Object> returnMap=new HashMap<String, Object>();   

        
		ByteArrayInputStream byteArrayInputStream;   
		try {     
			HSSFWorkbook wb = null;   
			
			//to get the order information from database
			order = inventoryOrderDAOImpl.retrieveOrder(order.getOrder_ID());
			InventoryOrderTemplate orderTemplate = new InventoryOrderTemplate(order, templatePosition);
			
			wb = orderTemplate.process();

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
	 * to generate the Jinsuan order to print the label
	 * @param order
	 * @param string
	 * @return
	 */
	@Transactional
	public Map<String, Object> generateJinsuanExcelReport(InventoryOrder order,
			String templatePosition) {
		Map<String,Object> returnMap=new HashMap<String, Object>();   
        
		ByteArrayInputStream byteArrayInputStream;   
		try {     
			HSSFWorkbook wb = null;   
			
			//to get the order information from database
			order = inventoryOrderDAOImpl.retrieveOrder(order.getOrder_ID());
			JinSuanOrderTemplate orderTemplate = new JinSuanOrderTemplate(order, templatePosition);
			
			wb = orderTemplate.process();

			ByteArrayOutputStream os = new ByteArrayOutputStream();   
			try {   
			    wb.write(os);   
			} catch (IOException e) {   
				loggerLocal.error(e);
		    }   
		    byte[] content = os.toByteArray();   
		    byteArrayInputStream = new ByteArrayInputStream(content);   
		    returnMap.put("stream", byteArrayInputStream);   
		    
		    //2. check the file name
		    int clientId = order.getCust().getId();
		    ChainStore store = chainStoreDaoImpl.getByClientId(clientId);
		    if (store != null) {
		    	ChainPriceIncrement priceIncrement = store.getPriceIncrement();
		    	if (priceIncrement != null){
		    		String fileExt = "";
		    		priceIncrement = chainPriceIncrementDaoImpl.get(priceIncrement.getId(), true);
		    		if (priceIncrement.getIncrementType() == ChainPriceIncrement.TYPE_MULTIPLE){
		    			fileExt += "_m_" + "1." + String.valueOf((int)priceIncrement.getIncrement());
		    		} else if (priceIncrement.getIncrementType() == ChainPriceIncrement.TYPE_ADD){
		    			fileExt += "_a_" + String.valueOf((int)priceIncrement.getIncrement());
		    		}
		    		returnMap.put("fileExt", fileExt);
		    	}
		    }
         
		    return returnMap;   
		 } catch (Exception ex) {   
			 loggerLocal.error(ex);
		 }   
		return null;   
	}
	
	/**
	 * 生成pda订单，
	 * @param order
	 * @param string
	 * @return
	 */
	@Transactional
	public Map<String, Object> generatePDAOrder(InventoryOrder order,
			String templatePosition) {
		Map<String,Object> returnMap=new HashMap<String, Object>();   
        
		ByteArrayInputStream byteArrayInputStream;   
		try {     
			HSSFWorkbook wb = null;   
			
			//to get the order information from database
			order = inventoryOrderDAOImpl.retrieveOrder(order.getOrder_ID());
			PDAOrderTemplate orderTemplate = new PDAOrderTemplate(order, templatePosition);
			
			wb = orderTemplate.process();

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
	 * to prepare the inventory order uiBean
	 * @return
	 */
    public InventoryOrderActionUIBean prepareUIBean(){
    	//1. prepare the user list
        List<UserInfor> users  = userInforService.getAllNormalUsers();	
        
        InventoryOrderActionUIBean uiBean = new InventoryOrderActionUIBean();
        uiBean.setUsers(users);
        
        return uiBean;
    }

    /**
     * before search the inventory order
     * @return
     */
	public InventoryOrderActionUIBean preparePreSearchUIBean() {
		InventoryOrderActionUIBean uiBean = new InventoryOrderActionUIBean();
		
		Map<Integer, String> typesMap = InventoryOrder.getTypesMap_wholeSaler();
		Map<Integer, String> orderStatusMap = InventoryOrder.getOrderStatusMap();
		Map<Integer, String> orderTypeMap = InventoryOrder.getTypesMap_wholeSaler();
		
		List<UserInfor> accounts = userInforService.getUsersByDept(UserInfor.ACCOUNTANT_CODE);
		
		uiBean.setTypesMap(typesMap);
		uiBean.setOrderStatusMap(orderStatusMap);
		uiBean.setOrderTypeMap(orderTypeMap);
		uiBean.setUsers(accounts);
		
		return uiBean;
	}

	/**
	 * the function to transfer the excel to inventory order 
	 * @param orderExcel
	 * @return
	 * @throws IOException 
	 */
	public InventoryOrder transferJinSuanToObject(File orderExcel) throws IOException{
		JinSuanOrderTemplate jinSuanOrderTemplate = new JinSuanOrderTemplate(orderExcel);
		
		InventoryOrder order = jinSuanOrderTemplate.transferExcelToObj();
		
		Set<String> barcodes = new HashSet<String>();
		List<InventoryOrderProduct> orderProducts = order.getProduct_List();
		
		//to get the barcodes and transfer to objects
		for (InventoryOrderProduct orderProduct: orderProducts)
			barcodes.add(orderProduct.getProductBarcode().getBarcode());

		List<ProductBarcode> ProductBarcodes = productBarcodeDaoImpl.getProductBarcodes(barcodes);
		HashMap<String, ProductBarcode> proHashMap = new HashMap<String, ProductBarcode>();
		
		for (ProductBarcode product: ProductBarcodes){
			proHashMap.put(product.getBarcode(), product);
		}
		
		//to set the product record
		for (InventoryOrderProduct orderProduct: orderProducts){
			String barcode  = orderProduct.getProductBarcode().getBarcode();
			ProductBarcode productBarcode = proHashMap.get(barcode);
			if (productBarcode != null){
                Product product = productBarcode.getProduct();
                
				orderProduct.setProductBarcode(productBarcode);
				orderProduct.setRecCost(product.getRecCost());
	            orderProduct.setSalesPrice(product.getSalesPrice());
	            
	            //check whether the whole price is changed manually
	            calculateSalePriceDiscount(orderProduct, product);
			} else {
				orderProduct.setSalesPrice(0);
				orderProduct.setWholeSalePrice(0);
				orderProduct.setSalesPrice(0);
			}
		}
		
		return order;
	}
	
//	private InventoryOrder getById(int order_ID){
//		InventoryOrder order = inventoryOrderDAOImpl.retrieveOrder(order_ID);
//		inventoryOrderDAOImpl.initialize(order);
//		
//		return order;
//	}

    /**
     * to determine the price selected and discount in the system
     * @param orderProduct
     * @param product
     */
	private void calculateSalePriceDiscount(InventoryOrderProduct orderProduct,
			Product product) {
		//check whether the whole price is changed manually
        double salePriceSelect_import = orderProduct.getSalePriceSelected();
        double wholePrice_import = orderProduct.getWholeSalePrice();
        
        double wholePrice1 = product.getWholeSalePrice();
        double wholePrice2 = product.getWholeSalePrice2();
        double wholePrice3 = product.getWholeSalePrice3();
        double retailPriceFactory = product.getSalesPriceFactory();
        
        if (salePriceSelect_import != wholePrice1 && salePriceSelect_import != wholePrice2 && salePriceSelect_import != wholePrice3 && salePriceSelect_import != retailPriceFactory){
        	BigDecimal decimal = null;
        	double discount = 0;
        	if (wholePrice3 != 0){
        		decimal = new BigDecimal(wholePrice_import / wholePrice3);
        		discount = decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        		orderProduct.setDiscount(discount);
        		orderProduct.setSalePriceSelected(wholePrice3);
        	} else if (wholePrice2 != 0){
        		decimal = new BigDecimal(wholePrice_import / wholePrice2);
        		discount = decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        		orderProduct.setDiscount(discount);
        		orderProduct.setSalePriceSelected(wholePrice2);
        	} else if (wholePrice1 != 0){
        		decimal = new BigDecimal(wholePrice_import / wholePrice1);
        		discount = decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        		orderProduct.setDiscount(discount);
        		orderProduct.setSalePriceSelected(wholePrice1);
        	} else if (retailPriceFactory != 0){
        		decimal = new BigDecimal(wholePrice_import / retailPriceFactory);
        		discount = decimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        		orderProduct.setDiscount(discount);
        		orderProduct.setSalePriceSelected(retailPriceFactory);
        	} else 
        		loggerLocal.error("Error to import the product : " + product.getSerialNum() +"," + wholePrice_import +"," +salePriceSelect_import);
        }
		
	}
	


	/**
	 * 员工通过pda提交给仓库配货订单
	 * PDA提交
	 * @param order
	 * @param loginUserInfor
	 */
	private Response saveToInventory(InventoryOrder order, UserInfor loginUserInfor) {
		Response response = new Response();
		int clientId = order.getCust().getId();
		int orderId = order.getOrder_ID();
		
		HeadQCust client = headQCustDaoImpl.get(clientId, true);
		if (client == null){
			response.setQuickValue(Response.ERROR,"请核对客户名字,重新输入");
		} else {
			
		    //part 1. to set the selected price and whole price
			Set<String> barcodes = new HashSet<String>();
			
			// 1.1 to store the non-duplicated product code in a sequence list for new order
			List<InventoryOrderProduct> orderProducts = order.getProduct_List();
			for (InventoryOrderProduct orderProduct: orderProducts){
				if (orderProduct!= null && orderProduct.getProductBarcode() != null && !orderProduct.getProductBarcode().getBarcode().equals("")){
				   String barcode = orderProduct.getProductBarcode().getBarcode();
				   barcodes.add(barcode);
				}
			}
			
			Map<String, ProductBarcode> productMap = productBarcodeDaoImpl.getProductMapByBarcode(barcodes);
			
			//1.2 to set the re-cost, selected price, whole price, discount
			//               totalQ, totalWhole, totalRecost, totalSalePrice
			int totalQ = 0;
			double totalWholePrice = 0;
			double totalCost = 0;
			double totalSalePrice = 0;

			for (int i =0; i < orderProducts.size(); i++){
				InventoryOrderProduct orderProduct = orderProducts.get(i);
				if (orderProduct == null || orderProduct.getProductBarcode()==null )
					continue;
//				
//				if (orderProduct.getQuantity() == 0)
//					orderProducts.remove(i);
				
				String barcode = orderProduct.getProductBarcode().getBarcode();
				ProductBarcode productBarcode = productMap.get(barcode);
				
				if (productBarcode == null){
					loggerLocal.error("Error to get the product barcode information :" + barcode);
				} else {
					Product product = productBarcode.getProduct();

					double selectedPrice = productBarcodeDaoImpl.getSelectedSalePrice(productBarcode);
					double discount = 1;
					if (selectedPrice == product.getSalesPriceFactory())
						discount = product.getDiscount();
					
					int quantity = orderProduct.getQuantity();
					double wholePrice = selectedPrice * discount;
					double cost = product.getRecCost();
					double salePrice = product.getSalesPrice();
					
					orderProduct.setDiscount(discount);
					orderProduct.setSalePriceSelected(selectedPrice);
					orderProduct.setWholeSalePrice(Common_util.roundDouble(wholePrice,2));
					orderProduct.setSalesPrice(salePrice);
					orderProduct.setProductBarcode(productBarcode);
					orderProduct.setRecCost(cost);
					
					totalQ += quantity;
					totalCost += quantity * cost;
					totalWholePrice += wholePrice * quantity;
					totalSalePrice += salePrice * quantity;
				}

			}

		    order.setPdaScanner(loginUserInfor);
			order.setOrder_scanner(loginUserInfor);
		    order.setComment("");
		    order.setOrder_StartTime(new Date());
		    order.setOrder_type(InventoryOrder.TYPE_SALES_ORDER_W);
		    order.setTotalQuantity(totalQ);
		    order.setTotalRecCost(Common_util.roundDouble(totalCost,2));
		    order.setTotalRetailPrice(Common_util.roundDouble(totalSalePrice,2));
		    order.setTotalWholePrice(Common_util.roundDouble(totalWholePrice,2));
		    
		    save(order);
		    
		    response.setQuickValue(Response.SUCCESS, "");
		}
		
		return response;
	}
	
	/**
	 * the person use the PDA to do the order recording and submit to inventory
	 * PDA提交仓库
	 * @param order
	 * @param loginUserInfor
	 */
	@Transactional
	public Response pdaSaveToInventory(InventoryOrder order,
			UserInfor loginUserInfor){
	    order.setOrder_Status(InventoryOrder.STATUS_PDA_COMPLETE);
	    return saveToInventory(order, loginUserInfor);
	}
	
	/**
	 * the person use the PDA to do the order recording and submit to inventory
	 * PDA提交草稿
	 * @param order
	 * @param loginUserInfor
	 */
	@Transactional
	public Response pdaSaveToDraft(InventoryOrder order,
			UserInfor loginUserInfor) {
	    order.setOrder_Status(InventoryOrder.STATUS_PDA_DRAFT);
	    return saveToInventory(order, loginUserInfor);
	}

	/**
	 * the inventory person edit the orders completed by PDA person
	 * @param order_ID
	 * @param loginUserInfor
	 * @return
	 */
	@Transactional
	public InventoryOrder editPDAComplete(int order_ID, UserInfor loginUserInfor) {
		InventoryOrder order = searchByID(order_ID);
		order.setOrder_Status(InventoryOrder.STATUS_DRAFT);
		order.setOrder_Keeper(loginUserInfor);
		
		inventoryOrderDAOImpl.update(order, true);
		
		return order;
	}
	
	/**
	 * 获取当前order，
	 * action : 
	 * 1 -> edit page
	 * 2 -> display page
	 * @param orderId
	 * @param loginUserInfor
	 * @return
	 */
	@Transactional
	public Response loadOrder(int orderId, UserInfor loginUserInfor) {
		Response response = new Response();
		
		InventoryOrder order = inventoryOrderDAOImpl.get(orderId, true);
		if (order == null){
			response.setQuickValue(Response.FAIL, "数据库无法找到,单据" + orderId);
		} else {
			int status = order.getOrder_Status();
			switch (status) {
				case InventoryOrder.STATUS_DELETED:
					response.setQuickValue(Response.FAIL, "单据" + orderId + " 处于删除状态,无法开启。请联系管理员");
					break;
				case InventoryOrder.STATUS_PDA_COMPLETE:
					order = editPDAComplete(orderId, loginUserInfor);
					response.setReturnValue(order);
					response.setAction(1);
					response.setReturnCode(Response.SUCCESS);
					break;
//				case InventoryOrder.STATUS_STORE_COMPLETE:
//					order = searchByID(orderId);
//					order.setOrder_Auditor(loginUserInfor);
//					order.setOrder_Status(InventoryOrder.STATUS_ACCOUNT_PROCESS);
//					inventoryOrderDAOImpl.update(order,false);
//					response.setAction(2);
//					response.setReturnValue(order);
//					response.setReturnCode(Response.SUCCESS);
//					break;
				case InventoryOrder.STATUS_DRAFT:
					order = searchByID(orderId);
					UserInfor keeper = order.getOrder_Keeper();
					
					if (keeper.getUser_id() != loginUserInfor.getUser_id()){
						response.setQuickValue(Response.FAIL, "单据 正在被 " + keeper.getName() + " 修改,你暂时无法修改");
					} else {
					    response.setAction(1);
					    response.setReturnValue(order);
					    response.setReturnCode(Response.SUCCESS);
					}
					break;
				case InventoryOrder.STATUS_ACCOUNT_PROCESS:		
					order = searchByID(orderId);
					UserInfor auditor2 = order.getOrder_Auditor();
					if (auditor2 == null || auditor2.getUser_id() == 0){
						order.setOrder_Auditor(loginUserInfor);
						inventoryOrderDAOImpl.update(order, true);
						response.setAction(2);
						response.setReturnValue(order);
						response.setReturnCode(Response.SUCCESS);
					} else if (auditor2.getUser_id() != loginUserInfor.getUser_id()){
						response.setQuickValue(Response.FAIL, "单据 正在被 " + auditor2.getName() + " 修改,你暂时无法修改");
					} else {
						response.setAction(2);
						response.setReturnValue(order);
						response.setReturnCode(Response.SUCCESS);
					}
					break;	
				case InventoryOrder.STATUS_ACCOUNT_COMPLETE:
				case InventoryOrder.STATUS_CANCELLED:
//				case InventoryOrder.STATUS_WAITING_AUDIT:
					order = searchByID(orderId);
					response.setAction(2);
					response.setReturnValue(order);
					response.setReturnCode(Response.SUCCESS);
					break;	
				default:
					response.setQuickValue(Response.FAIL, "无法找到对应的资源，请刷新然后继续搜索");
					break;
			}
		}

		return response;
	}


	/**
	 * service function to get the PDA drafts
	 * @param loginUserInfor
	 * @return
	 */
	public List<InventoryOrder> getPDADrafts(UserInfor loginUserInfor) {
		DetachedCriteria criteria = DetachedCriteria.forClass(InventoryOrder.class,"order");
		criteria.add(Restrictions.eq("order.pdaScanner.user_id", loginUserInfor.getUser_id()));
		criteria.add(Restrictions.eq("order.order_Status", InventoryOrder.STATUS_PDA_DRAFT));
		
		return inventoryOrderDAOImpl.getByCritera(criteria, false);
	}

	/**
	 * to get the pda draft order by id
	 * @param loginUserInfor
	 * @param order_ID
	 * @return
	 */
	@Transactional
	public InventoryOrder getPDADraft(UserInfor loginUserInfor, int order_ID) {
		InventoryOrder order = inventoryOrderDAOImpl.retrieveOrder(order_ID);
		if (order == null)
			order = new InventoryOrder();

		order.putSetToList();
		
		Collections.reverse(order.getProduct_List());
		
		return order;
	}

	/**
	 * 准备搜索时的vo map
	 * @param orderList
	 * @param user
	 * @return
	 */
	public Map constructInventoryOrderVOMap(
			List<InventoryOrder> orderList, UserInfor user) {
		Map data = new HashMap<String, Object>();
		List<InventoryOrderVO> inventoryOrderVOs = new ArrayList<InventoryOrderVO>();
		if (orderList != null){
			for (InventoryOrder order : orderList){
				boolean isEditable = false;
				int orderStatus = order.getOrder_Status();
//				if (orderStatus == InventoryOrder.STATUS_COMPLETE && user.containFunction("inventoryOrder!acctProcess"))
//					isEditable = true;
//				else 
				switch (orderStatus) {
				case InventoryOrder.STATUS_DRAFT:
					if (user.equals(order.getOrder_Keeper()) && user.containFunction("inventoryOrder!editDraft"))
						isEditable = true;
					else if ((order.getOrder_Keeper() == null || order.getOrder_Keeper().getUser_id()==0)  && user.containFunction("inventoryOrder!editDraft"))
						isEditable = true;
					break;
				case InventoryOrder.STATUS_ACCOUNT_PROCESS:
					if (user.equals(order.getOrder_Auditor()) || (order.getOrder_Auditor() == null || order.getOrder_Auditor().getUser_id()==0))
						isEditable = true;
					break;
				case InventoryOrder.STATUS_ACCOUNT_COMPLETE :
					isEditable = true;
					break;
				case InventoryOrder.STATUS_CANCELLED  :
					isEditable = true;
					break;
				case InventoryOrder.STATUS_PDA_COMPLETE :
					if (user.containFunction("inventoryOrder!editDraft"))
						isEditable =true;
					break;
				default:
					break;
				}
			
				InventoryOrderVO vo = new InventoryOrderVO(order);
				vo.setIsAuthorizedToEdit(isEditable);
				inventoryOrderVOs.add(vo);
			}
		}
		
		data.put("rows", inventoryOrderVOs);
		return data;
	}

	/**
	 * 更新order 的 comment
	 * @param order
	 * @return
	 */
	public Response updateOrderComment(InventoryOrder order) {
	       Response response = new Response();
			
	       if (order!= null && order.getOrder_ID() != 0){
				String hql_order = "UPDATE InventoryOrder i set i.comment = ? where order_ID = ?";
				Object[] values = {order.getComment(),order.getOrder_ID()};
				try {
				   inventoryOrderDAOImpl.executeHQLUpdateDelete(hql_order, values, true);
				   response.setSuccess("成功更新备注");
				} catch (Exception e){
				   response.setFail(e.getMessage());
				}
				
	        } else {
	        	response.setQuickValue(Response.FAIL, "无法找到当前订单号");
	        }
	        
	        return response;
	}

}