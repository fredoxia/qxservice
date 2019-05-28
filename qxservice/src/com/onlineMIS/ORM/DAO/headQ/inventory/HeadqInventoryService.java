package com.onlineMIS.ORM.DAO.headQ.inventory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlineMIS.ORM.DAO.Response;
import com.onlineMIS.ORM.DAO.chainS.user.ChainUserInforService;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.BrandDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.ProductBarcodeDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.QuarterDaoImpl;
import com.onlineMIS.ORM.DAO.headQ.barCodeGentor.YearDaoImpl;
import com.onlineMIS.ORM.entity.chainS.inventoryFlow.ChainInOutStock;
import com.onlineMIS.ORM.entity.chainS.inventoryFlow.ChainInOutStockArchive;
import com.onlineMIS.ORM.entity.chainS.inventoryFlow.ChainInvenTraceInfoVO;
import com.onlineMIS.ORM.entity.chainS.inventoryFlow.ChainInventoryReportTemplate;
import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Brand;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Category;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Color;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Product;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Quarter;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Year;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadQInventoryStock;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadQInventoryStore;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadqInvenTraceInfoVO;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadqInventoryReportTemplate;
import com.onlineMIS.common.Common_util;
import com.onlineMIS.common.ExcelUtil;
import com.onlineMIS.sorter.ChainInventoryReportSort;

@Service
public class HeadqInventoryService {
	@Autowired
	private HeadQInventoryStockDAOImpl headQInventoryStockDAOImpl;
	@Autowired
	private YearDaoImpl yearDaoImpl;
	@Autowired
	private QuarterDaoImpl quarterDaoImpl;
	@Autowired
	private BrandDaoImpl brandDaoImpl;
	@Autowired
	private ProductBarcodeDaoImpl productBarcodeDaoImpl;
    @Autowired
	private HeadQInventoryStoreDAOImpl headQInventoryStoreDAOImpl;
	/**
	 * 获取总部的库存状况
	 * @param parentId
	 * @param storeId
	 * @param yearId
	 * @param quarterId
	 * @param brandId
	 * @return
	 */
	@Transactional
	public Response getHeadqInventory(int parentId, int storeId, int yearId, int quarterId, int brandId) {
		System.out.println(yearId + "," + quarterId + "," + brandId);
		Response response = new Response();
		List<HeadqInventoryVO> headqInventoryVOList = new ArrayList<HeadqInventoryVO>();
		
		if (storeId  == 0){
			//@1. 展开所有的存储仓库的信息
			
		} else if (yearId == 0){
			//@2. 展开所有年份的库存信息
			String hql = "SELECT his.productBarcode.product.year.year_ID, SUM(costTotal), SUM(quantity) FROM HeadQInventoryStock AS his WHERE his.storeId = ? GROUP BY his.productBarcode.product.year.year_ID ORDER BY his.productBarcode.product.year.year_ID ASC";
			Object[] values = {storeId};
			
			List<Object> inventoryData = headQInventoryStockDAOImpl.executeHQLSelect(hql, values, null, true);
			
		    if (inventoryData != null){
				for (Object object: inventoryData){
						Object[] object2 = (Object[])object;
						int yearIdDB = Common_util.getInt(object2[0]);
						double costTotal = Common_util.getDouble(object2[1]);
						int quantity = Common_util.getInt(object2[2]);
						
						Year year = yearDaoImpl.get(yearIdDB, true);
						
						HeadqInventoryVO headqInventoryVO = new HeadqInventoryVO(parentId, year.getYear() + "年", quantity, costTotal, HeadqInventoryVO.STATE_CLOSED, storeId, yearIdDB, quarterId, brandId);
						headqInventoryVOList.add(headqInventoryVO);
				}
		    }
		} else if (quarterId == 0){
			//@2. 展开所有季的库存信息
			String hql = "SELECT his.productBarcode.product.quarter.quarter_ID, SUM(costTotal), SUM(quantity) FROM HeadQInventoryStock AS his WHERE his.storeId = ? AND his.productBarcode.product.year.year_ID=? GROUP BY his.productBarcode.product.quarter.quarter_ID ORDER BY his.productBarcode.product.quarter.quarter_ID ASC";
			Object[] values = {storeId, yearId};
			
			List<Object> inventoryData = headQInventoryStockDAOImpl.executeHQLSelect(hql, values, null, true);
			
		    if (inventoryData != null){
				for (Object object: inventoryData){
						Object[] object2 = (Object[])object;
						int quarterIdDB = Common_util.getInt(object2[0]);
						double costTotal = Common_util.getDouble(object2[1]);
						int quantity = Common_util.getInt(object2[2]);
						
						Year year = yearDaoImpl.get(yearId, true);
						Quarter quarter = quarterDaoImpl.get(quarterIdDB, true);
						
						String name = year.getYear() + "年" + quarter.getQuarter_Name();
						
						HeadqInventoryVO headqInventoryVO = new HeadqInventoryVO(parentId, name, quantity, costTotal, HeadqInventoryVO.STATE_CLOSED, storeId, yearId, quarterIdDB, brandId);
						headqInventoryVOList.add(headqInventoryVO);
				}
		    }
		} else if (brandId == 0){
			//@2. 展开所有品霞的库存信息
			String hql = "SELECT his.productBarcode.product.brand.brand_ID, SUM(costTotal), SUM(quantity) FROM HeadQInventoryStock AS his WHERE his.storeId = ? AND his.productBarcode.product.year.year_ID=? AND his.productBarcode.product.quarter.quarter_ID=? GROUP BY his.productBarcode.product.brand.brand_ID ORDER BY his.productBarcode.product.brand.brand_ID ASC";
			Object[] values = {storeId, yearId, quarterId};
			
			List<Object> inventoryData = headQInventoryStockDAOImpl.executeHQLSelect(hql, values, null, true);
			
		    if (inventoryData != null){
				for (Object object: inventoryData){
						Object[] object2 = (Object[])object;
						int brandIdDB = Common_util.getInt(object2[0]);
						double costTotal = Common_util.getDouble(object2[1]);
						int quantity = Common_util.getInt(object2[2]);
						
						Brand brand = brandDaoImpl.get(brandIdDB, true);
						
						String name = "";
						String pinyin = brand.getPinyin();
						if (!StringUtils.isEmpty(pinyin)){
							name = pinyin.substring(0, 1) + " ";
						}
						
						name = brand.getBrand_Name();
						
						HeadqInventoryVO headqInventoryVO = new HeadqInventoryVO(parentId, name, quantity, costTotal, HeadqInventoryVO.STATE_CLOSED, storeId, yearId, quarterId, brandIdDB);
						headqInventoryVOList.add(headqInventoryVO);
				}
		    }
		} else if (brandId != 0) {
			//@2. 展开当前品霞的库存信息
			String hql = "SELECT his.productBarcode.id, SUM(costTotal), SUM(quantity) FROM HeadQInventoryStock AS his WHERE his.storeId = ? AND his.productBarcode.product.year.year_ID=? AND his.productBarcode.product.quarter.quarter_ID=? AND his.productBarcode.product.brand.brand_ID=?  GROUP BY his.productBarcode.id ORDER BY his.productBarcode.product.productCode ASC ";
			Object[] values = {storeId, yearId, quarterId, brandId};
			
			List<Object> inventoryData = headQInventoryStockDAOImpl.executeHQLSelect(hql, values, null, true);
			
		    if (inventoryData != null){
				for (Object object: inventoryData){
						Object[] object2 = (Object[])object;
						int pbId = Common_util.getInt(object2[0]);
						double costTotal = Common_util.getDouble(object2[1]);
						int quantity = Common_util.getInt(object2[2]);
						
						ProductBarcode pb = productBarcodeDaoImpl.get(pbId, true);
						Color color = pb.getColor();
						String colorName = "";
						if (color != null)
							colorName = color.getName();
						Product product = pb.getProduct();
						String gender = product.getGenderS();
						String sizeRange = product.getSizeRangeS();
						
						Category category = product.getCategory();
						String name = Common_util.cutProductCode(pb.getProduct().getProductCode()) + colorName + " "  + gender + sizeRange +  category.getCategory_Name();
						
						
						HeadqInventoryVO headqInventoryVO = new HeadqInventoryVO(parentId, name, quantity, costTotal, HeadqInventoryVO.STATE_OPEN, storeId, yearId, quarterId, brandId);
						headqInventoryVO.setPbId(pbId);
						headqInventoryVOList.add(headqInventoryVO);
				}
		    }
		}
		
		response.setReturnValue(headqInventoryVOList);
		return response;
	}

	/**
	 * 生成excel的inventory 报表
	 * @param storeId
	 * @param yearId
	 * @param quarterId
	 * @param brandId
	 * @param string
	 * @return
	 * @throws IOException 
	 */
	public Response generateInventoryExcelReport(int storeId, int yearId, int quarterId, int brandId, String templatePath)  {
		Response response = new Response();
		
		HeadQInventoryStore store = null;
		if (storeId != 0){
			store = headQInventoryStoreDAOImpl.get(storeId, true);
		}
		
		/**
		 * @1. 准备数据库数据
		 */
		List<HeadqInventoryReportItem> reportItems = new ArrayList<HeadqInventoryReportItem>();
		
		
		StringBuffer hql = new StringBuffer("SELECT his.productBarcode.id, SUM(quantity), SUM(costTotal) FROM HeadQInventoryStock AS his WHERE 1=1");
		
		
		if (brandId != 0){
			hql.append(" AND his.productBarcode.product.brand.brand_ID=" + brandId);
		} 

		if (quarterId != 0){
			hql.append(" AND his.productBarcode.product.quarter.quarter_ID=" + quarterId);
		} 
		
		if (yearId != 0){
			hql.append(" AND his.productBarcode.product.year.year_ID=" + yearId);
		} 
		
		if (storeId != 0){
			hql.append(" AND his.storeId=" + storeId);
		}
		
		hql.append("GROUP BY his.productBarcode.id");

		List<Object> result = headQInventoryStockDAOImpl.executeHQLSelect(hql.toString(), null, null, true);
		for (int i = 0; i < result.size(); i++){
			  Object object = result.get(i);
			  if (object != null){
				  HeadqInventoryReportItem item = new HeadqInventoryReportItem();
				  
				 Object[] recordResult = (Object[])object;
				 int productId = Common_util.getInt(recordResult[0]);
				 int quantity = Common_util.getInt(recordResult[1]);
				 double cost = Common_util.getDouble(recordResult[2]);
				 
				 if (quantity == 0)
					 continue;
				 item.setTotalQuantity(quantity);
				 item.setTotalCostAmt(cost);
				 
				 ProductBarcode productBarcode = productBarcodeDaoImpl.get(productId, true);

				 item.setProductBarcode(productBarcode);
				 
				 reportItems.add(item);
			  }
		}
		
		//Collections.sort(reportItems, new HeadqInventoryReportSort());
		HeadqInventoryReportTemplate headqInventoryReportTemplate;
		try {
			headqInventoryReportTemplate = new HeadqInventoryReportTemplate(reportItems, store, templatePath);
			HSSFWorkbook wb = headqInventoryReportTemplate.process();
			
			ByteArrayInputStream byteArrayInputStream = ExcelUtil.convertExcelToInputStream(wb);
			
			response.setReturnValue(byteArrayInputStream);
			response.setReturnCode(Response.SUCCESS);
		} catch (IOException e) {
			response.setFail(e.getMessage());
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 获取库存跟踪信息
	 * @param storeId
	 * @param pbId
	 * @return
	 */
	public Response getInventoryTraceInfor(int storeId, int pbId) {
		Response response = new Response();
		Map data = new HashMap<String, List>();
		List<HeadqInvenTraceInfoVO> traceVOs = new ArrayList<HeadqInvenTraceInfoVO>();
		List<HeadqInvenTraceInfoVO> footers = new ArrayList<HeadqInvenTraceInfoVO>();
		
		if (pbId != 0){
        	DetachedCriteria criteria1 = DetachedCriteria.forClass(HeadQInventoryStock.class);		
        	criteria1.add(Restrictions.eq("storeId", storeId));
    		criteria1.add(Restrictions.eq("productBarcode.id", pbId));
    		criteria1.addOrder(Order.asc("date"));	
			List<HeadQInventoryStock> stocks = headQInventoryStockDAOImpl.getByCritera(criteria1, true);

			for (HeadQInventoryStock stock : stocks){
				HeadqInvenTraceInfoVO traceVO = new HeadqInvenTraceInfoVO(stock);
				traceVOs.add(traceVO);
			}

			//建设foot
			HeadqInvenTraceInfoVO foot = new HeadqInvenTraceInfoVO();
			int stockInventory = 0;
			for (HeadqInvenTraceInfoVO ele : traceVOs){
				stockInventory += ele.getQuantity();
			}
			
			ProductBarcode pb = productBarcodeDaoImpl.get(pbId, true);
			String productName = pb.getProduct().getBrand().getBrand_Name() + " " + pb.getProduct().getProductCode();
			Color color = pb.getColor();
			if (color != null)
				productName += color.getName();
			
			foot.setDate(productName);
			foot.setQuantity(stockInventory);
			footers.add(foot);

			response.setReturnCode(Response.SUCCESS);
        }

		data.put("rows", traceVOs);
		data.put("footer", footers);
		response.setReturnValue(data);
		
		return response;
	}
	

}
