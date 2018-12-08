package com.onlineMIS.ORM.DAO.headQ.supplier.purchase;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.ProductBarcodeDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.inventory.HeadQInventoryStockDAOImpl;
import com.onlineMIS.ORM.DAO.headQ.supplier.finance.SupplierAcctFlowDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.supplier.supplierMgmt.HeadQSupplierDaoImpl;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadQInventoryStock;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadQInventoryStore;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.SupplierAcctFlow;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrderProduct;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.filter.SystemParm;


@Service
public class SupplierPurchaseService {
 
	@Autowired
	private PurchaseOrderDaoImpl purchaseOrderDaoImpl;
	@Autowired
	private ProductBarcodeDaoImpl ProductBarcodeDaoImpl;
	@Autowired
	private HeadQSupplierDaoImpl headQSupplierDaoImpl;
	@Autowired
	private SupplierAcctFlowDaoImpl supplierAcctFlowDaoImpl;
	@Autowired
	private HeadQInventoryStockDAOImpl headQInventoryStockDAOImpl;
	
	/**
	 * 获取单据信息
	 * @param id
	 * @param loginUser
	 * @return
	 */
	@Transactional
	public Response getPurchaseOrderById(int id, UserInfor loginUser){
		Response response = new Response();
		
		PurchaseOrder order = purchaseOrderDaoImpl.get(id, true);
		if (order == null){
			response.setFail("无法找到 采购单据 : " + id);
		} else {
			UserInfor orderOwner = order.getOrderAuditor();
			if (orderOwner.getUser_id() != loginUser.getUser_id()){
				response.setFail("你没有权限修改 采购单据 .");
			} else {
				purchaseOrderDaoImpl.initialize(order);
				
				order.putSetToList();
				
				response.setReturnValue(order);
			}
		}
		
		return response;
	}
	
	/**
	 * 删除单据
	 * @param id
	 * @param loginUser
	 * @return
	 */
	@Transactional
	public Response deletePurchaseOrderById(int id, UserInfor loginUser){
		Response response = new Response();
		
		PurchaseOrder order = purchaseOrderDaoImpl.get(id, true);
		if (order == null){
			response.setFail("无法找到 采购单据 : " + id);
		} else {
			UserInfor orderOwner = order.getOrderAuditor();
			if (orderOwner.getUser_id() != loginUser.getUser_id()){
				response.setFail("你没有权限删除 采购单据 .");
			} else {
				order.setLastUpdateTime(Common_util.getToday());
				order.setStatus(PurchaseOrder.STATUS_DELETED);
				purchaseOrderDaoImpl.update(order, true);
			}
		}
		
		return response;
	}
	
	/**
	 * @tobecheck
	 * 保存purchase order
	 * @param order
	 * @param loginUser
	 * @return
	 */
	@Transactional
	public Response savePurchaseOrderToDraft(PurchaseOrder order, UserInfor loginUser){
		Response response = new Response();
		
		if (!validatePurchaseOrder(order).isSuccess()){
			return response;
		}
		
		recalculatePurchaseOrder(order);
		
		PurchaseOrder orderOriginal = purchaseOrderDaoImpl.get(order.getId(), true);
		if (orderOriginal == null){
			order.setLastUpdateTime(Common_util.getToday());
			order.setOrderAuditor(loginUser);
			order.setStatus(PurchaseOrder.STATUS_DRAFT);
			order.putListToSet();
			
			purchaseOrderDaoImpl.save(order, true);
		} else {
			purchaseOrderDaoImpl.evict(orderOriginal);
			
			order.setLastUpdateTime(Common_util.getToday());
			order.setOrderAuditor(loginUser);
			order.setStatus(PurchaseOrder.STATUS_DRAFT);
			order.putListToSet();
			
			purchaseOrderDaoImpl.save(order, true);
		}
		
		return response;
	}
	
	/**
	 * @tobecheck
	 * 保存purchase order过账
	 * @param order
	 * @param loginUser
	 * @return
	 */
	@Transactional
	public Response savePurchaseOrderToComplete(PurchaseOrder order, UserInfor loginUser){
		Response response = new Response();
		
		if (!validatePurchaseOrder(order).isSuccess()){
			return response;
		}
		
		recalculatePurchaseOrder(order);
				
		//1. 保存单据
		PurchaseOrder orderOriginal = purchaseOrderDaoImpl.get(order.getId(), true);
		if (orderOriginal == null){
			order.setLastUpdateTime(Common_util.getToday());
			order.setOrderAuditor(loginUser);
			order.setStatus(PurchaseOrder.STATUS_DRAFT);
			order.putListToSet();
			
			purchaseOrderDaoImpl.save(order, true);
		} else {
			purchaseOrderDaoImpl.evict(orderOriginal);
			
			order.setLastUpdateTime(Common_util.getToday());
			order.setOrderAuditor(loginUser);
			order.setStatus(PurchaseOrder.STATUS_DRAFT);
			order.putListToSet();
			
			purchaseOrderDaoImpl.save(order, true);
		}
		
		//2. 过账账目
		updateSupplierAcctFlow(order, false);
				
		//3. 计算库存
		updateHeadqInventory(order, false);
		
		return response;
	}
	
	/**
	 * @tobecheck
	 * 红虫purchase order过账
	 * @param order
	 * @param loginUser
	 * @return
	 */
	@Transactional
	public Response cancelPurchaseOrder(PurchaseOrder order, UserInfor loginUser){
		Response response = new Response();
				
		//1. 保存单据
		PurchaseOrder orderOriginal = purchaseOrderDaoImpl.get(order.getId(), true);
		if (orderOriginal == null){
			response.setFail("单据还未过账无法红冲");
		} else {
			if (orderOriginal.getStatus() != PurchaseOrder.STATUS_COMPLETE){
				response.setFail("单据还未过账无法红冲");
			} else {
				purchaseOrderDaoImpl.evict(orderOriginal);
				
				order.setLastUpdateTime(Common_util.getToday());
				order.setOrderAuditor(loginUser);
				order.setStatus(PurchaseOrder.STATUS_CANCEL);
				order.putListToSet();
				
				purchaseOrderDaoImpl.save(order, true);
			}
		}
		
		//2. 过账账目
		updateSupplierAcctFlow(order, true);
				
		//3. 计算库存
		updateHeadqInventory(order, true);
		
		return response;
	}
	
	/**
	 * 更新库存信息
	 * @param order
	 * @param b
	 */
	private void updateHeadqInventory(PurchaseOrder order, boolean isCancel) {
		//如果是测试连锁店不需要
		int TEST_SUPPLIER_ID = SystemParm.getTestSupplierId();
		
		if (order.getSupplier().getId() == TEST_SUPPLIER_ID)
			return;
		
		//更新库存数据
		HeadQInventoryStore store = order.getStore();
		int storeId = store.getId();
		
		int offset = isCancel ? -1 : 1;

		if (order.getType() == PurchaseOrder.TYPE_RETURN){
			offset *= -1;			
		}
		
		Set<HeadQInventoryStock> stocks = new HashSet<HeadQInventoryStock>();
		 Iterator<PurchaseOrderProduct> orderProducts = order.getProductSet().iterator();
		 while (orderProducts.hasNext()){
			 PurchaseOrderProduct orderProduct = orderProducts.next();
			 int pbId = orderProduct.getPb().getId();
			 double cost = orderProduct.getRecCost();
			 double wholeSalePrice = orderProduct.getWholeSalePrice();
			 int quantity = orderProduct.getQuantity() * offset;
			 double costTotal = cost * quantity;
			 double wholeSalesTotal = wholeSalePrice * quantity;

			 ProductBarcode pb = ProductBarcodeDaoImpl.get(pbId, true);
			 
			 HeadQInventoryStock stock = new HeadQInventoryStock(storeId, cost, costTotal, wholeSalePrice, wholeSalesTotal, quantity, pb);
			 stocks.add(stock);
		 }
			
		 headQInventoryStockDAOImpl.updateInventoryStocks(stocks);
		
	}

	/**
	 * to update the chain's acct flow
	 * @param order
	 * @param b
	 */
    private void updateSupplierAcctFlow(PurchaseOrder order,  boolean isCancel) {
    	int supplierId = order.getSupplier().getId();
		
    	HeadQSupplier supplier = headQSupplierDaoImpl.get(supplierId, true);
    	
		int orderId = order.getId();
    	
    	int orderType = order.getType();
    	double totalAmt = order.getTotalRecCost();
    	double totalDis = order.getTotalDiscount();
    	double netAmt = totalAmt - totalDis;
    	
    	//1. update the offset
    	int offset = 1;
		if (orderType == PurchaseOrder.TYPE_RETURN)
			offset *= -1;
			
		if (isCancel)
			offset *= -1;
    	
		netAmt *= offset;
		
		//2.update the order's preAcctAmt and postAcctAmt
		double initialAcctAmt = supplier.getInitialAcctBalance();  		
		double acctAmtFlow = supplierAcctFlowDaoImpl.getAccumulateAcctFlow(supplierId);
		double preAcctAmt = Common_util.getDecimalDouble(initialAcctAmt + acctAmtFlow);
		double postAcctAmt = Common_util.getDecimalDouble(preAcctAmt + netAmt);
		if (!isCancel){  
			String hql = "update PurchaseOrder set preAcctAmt =?, postAcctAmt=? where id=?";
			Object[] values = {preAcctAmt, postAcctAmt, orderId};
			
			purchaseOrderDaoImpl.executeHQLUpdateDelete(hql, values, false);
		}
		
		supplier.setCurrentAcctBalance(postAcctAmt);
		headQSupplierDaoImpl.update(supplier, true);
		
		SupplierAcctFlow supplierAcctFlow = new SupplierAcctFlow(supplierId, netAmt, "S," + orderId + "," + isCancel, Common_util.getToday());
		supplierAcctFlowDaoImpl.save(supplierAcctFlow, true);
	
	}
    
	/**
	 * 再次汇总
	 * 合并重复的产品
	 * @param order
	 */
	private void recalculatePurchaseOrder(PurchaseOrder order) {
		
		/**
		 * this is the barcode list with a sequence
		 */
		Map<Integer, PurchaseOrderProduct> orderProductMap = new HashMap<Integer, PurchaseOrderProduct>();
		
		List<PurchaseOrderProduct> orderProducts = order.getProductList();

		
		/**
		 * 1. to group the products' quantity, recost, whole sales
		 */
        double totalRecCost = 0;
        double totalWholePrice = 0;
        int totalQuantity = 0;
		for (PurchaseOrderProduct orderProduct: orderProducts){
			if (orderProduct!= null && orderProduct.getPb() != null && !orderProduct.getPb().getBarcode().equals("")){
				int pbId = orderProduct.getPb().getId();
				
				if (orderProductMap.containsKey(pbId)){
					PurchaseOrderProduct original_orderProduct = orderProductMap.get(pbId);
					int addQuantity = orderProduct.getQuantity();
					int orignalQuantity = original_orderProduct.getQuantity();
					original_orderProduct.setQuantity(addQuantity + orignalQuantity);
					
				} else {
					orderProductMap.put(pbId, orderProduct);
				}
				
				totalQuantity += orderProduct.getQuantity();
				totalRecCost += orderProduct.getRecCost() * orderProduct.getQuantity();
				totalWholePrice += orderProduct.getWholeSalePrice() * orderProduct.getQuantity();
			}
		}
		
		order.setTotalQuantity(totalQuantity);
		order.setTotalRecCost(totalRecCost);
		order.setTotalWholePrice(totalWholePrice);

	}

	/**
	 * 验证单据的信息是否都正确
	 * @param order
	 * @return
	 */
	private Response validatePurchaseOrder(PurchaseOrder order){
		Response response = new Response();
		boolean success = true;
		Set<PurchaseOrderProduct> productSet = order.getProductSet();
		String errorMsg = "";
		
		for (PurchaseOrderProduct orderProduct : productSet){
			int quantity = orderProduct.getQuantity();
			if (quantity <= 0){
				errorMsg += "条码 " + orderProduct.getPb().getBarcode() + " 的数量必须是>0\n";
				success = false;
			}
			double discount = orderProduct.getDiscount();
			if (discount <=0){
				errorMsg += "条码 " + orderProduct.getPb().getBarcode() + " 的折扣必须是>0\n";
				success = false;
			}
		}
		
		if (!success){
			response.setFail(errorMsg);
		}
		
		return response;
	}
}
