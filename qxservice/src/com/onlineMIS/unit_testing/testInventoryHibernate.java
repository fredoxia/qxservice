package com.onlineMIS.unit_testing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.onlineMIS.ORM.entity.headQ.barcodeGentor.Product;
import com.onlineMIS.ORM.entity.headQ.barcodeGentor.ProductBarcode;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadQInventoryStock;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadQSalesHistory;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrder;
import com.onlineMIS.ORM.entity.headQ.inventory.InventoryOrderProduct;
import com.onlineMIS.ORM.entity.headQ.preOrder.CustPreOrder;
import com.onlineMIS.ORM.entity.headQ.user.UserInfor;

public class testInventoryHibernate {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Configuration configuration = new Configuration().configure();
		
		SessionFactory sFactory = configuration.buildSessionFactory();
		
		Session session = sFactory.openSession();
		Transaction transaction = session.beginTransaction();

        //获取年份
        String hql = "SELECT his.productBarcode.product.year.year_ID, SUM(costTotal), SUM(quantity) FROM HeadQInventoryStock AS his WHERE his.storeId = 1 GROUP BY his.productBarcode.product.year.year_ID ORDER BY his.productBarcode.product.year.year_ID ASC";
        Query query = session.createQuery(hql);
        List<Object> objects = query.list();
        for (Object ob: objects){
        	if (ob != null){
        		
        	    Object[] objects2 = (Object[])ob;
        		for (Object ob2: objects2){
        			System.out.print(ob2 + " , ");
        		}
        	}
        	System.out.println();
        	
        }
        
        //获取季度
        String hql2 = "SELECT his.productBarcode.product.quarter.quarter_ID, SUM(costTotal), SUM(quantity) FROM HeadQInventoryStock AS his WHERE his.storeId = 1 AND his.productBarcode.product.year.year_ID=7 GROUP BY his.productBarcode.product.quarter.quarter_ID ORDER BY his.productBarcode.product.quarter.quarter_ID ASC";
        Query query2 = session.createQuery(hql2);
        List<Object> objects2 = query2.list();
        for (Object ob: objects2){
        	if (ob != null){
        		
        	    Object[] objects22 = (Object[])ob;
        		for (Object ob2: objects22){
        			System.out.print(ob2 + " , ");
        		}
        	}
        	System.out.println();
        	
        }
        
        //获取品牌
        String hql3 = "SELECT his.productBarcode.product.brand.brand_ID, SUM(costTotal), SUM(quantity) FROM HeadQInventoryStock AS his WHERE his.storeId = 1 AND his.productBarcode.product.year.year_ID=8 AND his.productBarcode.product.quarter.quarter_ID=2 GROUP BY his.productBarcode.product.brand.brand_ID";
        Query query3 = session.createQuery(hql3);
        List<Object> objects3= query3.list();
        for (Object ob: objects3){
        	if (ob != null){
        		
        	    Object[] objects22 = (Object[])ob;
        		for (Object ob2: objects22){
        			System.out.print(ob2 + " , ");
        		}
        	}
        	System.out.println();
        	
        }

		transaction.commit();
		session.close();
		
	}
}
