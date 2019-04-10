package com.onlineMIS.unit_testing;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.onlineMIS.ORM.entity.shared.expense.ExpenseType;
import com.onlineMIS.common.FileOperation;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.Import;

public class ImportHeadqExpense {
	List<String> records = new ArrayList<String>();
	public ImportHeadqExpense(){
		records.add("运营费,运费快递费");
		records.add("运营费,拉包费");
		records.add("运营费,餐费");
		records.add("运营费,办公费");
		records.add("运营费,广告费");
		records.add("运营费,房租管理费");
		records.add("运营费,业务费");
		records.add("运营费,订货会费用");
		records.add("运营费,年会费用");
		records.add("运营费,水费电费");
		records.add("运营费,汽车油费");
		records.add("运营费,代垫费用");
		records.add("运营费,三方费用");
		records.add("运营费,公益捐赠");
		records.add("运营费,汽车保险费用");
		records.add("运营费,业务违规罚款");
		records.add("资产费用,装修费");
		records.add("资产费用,固定资产投资");
		records.add("资产费用,汽车等大额交易");
		records.add("资产费用,汽车保养修理");
		records.add("税费手续费,国税地税");
		records.add("税费手续费,三方支付费用");
		records.add("总部财务,调帐亏损");
		records.add("总部财务,固定资产折旧");
		records.add("总部财务,现金亏损");
		records.add("总部财务,客户返利");
		records.add("总部财务,销售折让");
		records.add("总部财务,商场抽成");
		records.add("差旅费,厂家订货差旅费");
		records.add("差旅费,连锁店差旅费");
		records.add("差旅费,其他差旅费");
		records.add("工资福利,基本工资");
		records.add("工资福利,奖金及分红");
		records.add("工资福利,零售返利");
		records.add("工资福利,培训费用");
		records.add("工资福利,员工社保");
		records.add("工资福利,连锁店铺奖金");
		records.add("工资福利,其他福利");
		records.add("其他,总部其他费用");
		records.add("其他,连锁店其他费用");
	}
	public void process(){
		List<String> parents = new ArrayList<String>();
		Map<String, Integer> parentMap = new HashMap<String, Integer>();
		for (String record: records){
			String parent = record.split(",")[0];
			if (!parents.contains(parent)){
				parents.add(parent);
			}
		}
		System.out.println(records.size());
		Configuration configuration = new Configuration().configure();
		SessionFactory sFactory = configuration.buildSessionFactory();
		Session session = sFactory.openSession();
		Transaction transaction = session.beginTransaction();
		for (String record: parents){
			ExpenseType type = new ExpenseType();
			type.setBelong(null);
			type.setName(record);
			type.setParentId(null);
			
			session.saveOrUpdate(type);
			
			parentMap.put(record, type.getId());
		}
		
		for (String record: records){
			String parent = record.split(",")[0];
			String child = record.split(",")[1];
			
			ExpenseType type = new ExpenseType();
			type.setBelong(null);
			type.setName(child);
			type.setParentId(parentMap.get(parent));
			
			session.saveOrUpdate(type);

		}
		

		transaction.commit();
	    session.close();
		System.out.println("compl");
	}
	public static void main(String[] args) {
		ImportHeadqExpense importHeadqExpense = new ImportHeadqExpense();
		importHeadqExpense.process();
	}

}
