package com.onlineMIS.ORM.DAO.headQ.report;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.chainS.user.ChainStoreDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.BrandDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.ProductBarcodeDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.QuarterDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.YearDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.supplier.purchase.PurchaseOrderProductDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.supplier.purchase.SupplierPurchaseService;
import com.onlineMIS.ORM.DAO.headQ.supplier.supplierMgmt.HeadQSupplierDaoImpl;
import com.onlineMIS.ORM.entity.chainS.report.ChainPurchaseStatisticReportItemVO;
import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Brand;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Category;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Color;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Product;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Quarter;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Year;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrder;
import com.onlineMIS.ORM.entity.headQ.report.HeadQPurchaseStatisticReportItemVO;
import com.onlineMIS.ORM.entity.headQ.supplier.purchase.PurchaseOrder;
import com.onlineMIS.ORM.entity.headQ.supplier.supplierMgmt.HeadQSupplier;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.filter.SystemParm;
import com.onlineMIS.sorter.ChainStatisticReportItemVOSorter;
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
	private QuarterDaoImpl quarterDaoImpl;
	
	@Autowired
	private BrandDaoImpl brandDaoImpl;
	
	@Autowired
	private ProductBarcodeDaoImpl productBarcodeDaoImpl;
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
			HeadQPurchaseStatisticReportItemVO rootItem = new HeadQPurchaseStatisticReportItemVO(name, 1, supplierId, yearId, quarterId, brandId,0, ChainPurchaseStatisticReportItemVO.STATE_CLOSED);
			
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
					
					levelOneItem = new HeadQPurchaseStatisticReportItemVO(name, parentId, supplierId, yearIdDB, quarterId, brandId,0, ChainPurchaseStatisticReportItemVO.STATE_CLOSED);
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
					
					levelOneItem = new HeadQPurchaseStatisticReportItemVO(name, parentId, supplierId, yearId, quarterIdDB, brandId,0, ChainPurchaseStatisticReportItemVO.STATE_CLOSED);
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
					
					levelOneItem = new HeadQPurchaseStatisticReportItemVO(name, parentId, supplierId, yearId, quarterId, brandIdDB,0, ChainPurchaseStatisticReportItemVO.STATE_CLOSED);
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
					
					levelOneItem = new HeadQPurchaseStatisticReportItemVO(name, parentId, supplierId, yearId, quarterId, brandId, pbId, ChainPurchaseStatisticReportItemVO.STATE_OPEN);
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

}
