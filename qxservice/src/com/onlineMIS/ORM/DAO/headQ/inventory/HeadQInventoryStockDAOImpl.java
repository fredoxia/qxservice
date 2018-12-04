package com.onlineMIS.ORM.DAO.headQ.inventory;


import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadQInventoryStock;


@Repository
public class HeadQInventoryStockDAOImpl extends BaseDAO<HeadQInventoryStock> {
      public HeadQInventoryStock getInventoryStock(int storeId, int pbId){
  		DetachedCriteria criteria = DetachedCriteria.forClass(HeadQInventoryStock.class);
  		criteria.add(Restrictions.eq("storeId", storeId));
  		criteria.add(Restrictions.eq("productBarcode.id", pbId));
  		
  		List<HeadQInventoryStock> stocks = this.getByCritera(criteria, true); 
  		if (stocks.size() > 0)
  			return stocks.get(0);
  		else 
  			return null;
      }
   

      public synchronized void updateInventoryStocks(Set<HeadQInventoryStock> stocks){
    	  for (HeadQInventoryStock stock : stocks) {
	    	  int storeId = stock.getStoreId();
	    	  int pbId = stock.getProductBarcode().getId();
	    	  HeadQInventoryStock originalStock = this.getInventoryStock(storeId, pbId);
	    	  
				 if (originalStock == null) {
					 this.save(stock, true);
				 } else {
					 originalStock.addTo(stock);
					 if (originalStock.getQuantity() == 0)
						 this.delete(originalStock, true);
					 else 
					     this.update(originalStock, true);
				 }
    	  }
      }

}
