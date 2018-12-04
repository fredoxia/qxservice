package com.onlineMIS.ORM.DAO.headQ.supplier.finance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.onlineMIS.ORM.DAO.BaseDAO;
import com.onlineMIS.ORM.entity.headQ.finance.FinanceCategory;
import com.onlineMIS.ORM.entity.headQ.supplier.finance.FinanceCategorySupplier;

@Repository
public class FinanceCategorySupplierImpl extends BaseDAO<FinanceCategorySupplier> {
	
	    @Override
	    public List<FinanceCategorySupplier> getAll(boolean cached){
	    	DetachedCriteria criteria = DetachedCriteria.forClass(FinanceCategorySupplier.class);
	    	criteria.addOrder(Order.asc("type"));
	    	
	    	return this.getByCritera(criteria, cached);
	    }
	
	    public FinanceCategorySupplier getByTypeId(int typeId){
			DetachedCriteria criteria = DetachedCriteria.forClass(FinanceCategorySupplier.class);
			criteria.add(Restrictions.eq("type", typeId));
			List<FinanceCategorySupplier> categories =  getByCritera(criteria, true);
			if (categories == null || categories.size() == 0)
				return null;
			else 
				return categories.get(0);
	    }
	    
	    /**
	     * id: typeId;
	     * value: financeCategory
	     * @return
	     */
	    public Map<Integer, FinanceCategorySupplier> getFinanceCategoryMap(){
	    	List<FinanceCategorySupplier> categories = getAll(true);
	    	
	    	Map<Integer, FinanceCategorySupplier> categoryMap = new HashMap<Integer, FinanceCategorySupplier>();
	    	
	    	for (FinanceCategorySupplier financeCategory: categories){
	    		int typeId = financeCategory.getType();
	    		categoryMap.put(typeId, financeCategory);
	    	}
	    	return categoryMap;
	    }
	
}
