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
		records.add("办公费,订货会费用");
		records.add("办公费,广告费");
		records.add("办公费,日常管理");
		records.add("差旅费,老总差旅费");
		records.add("差旅费,员工差旅费");
		records.add("车辆使用费,保险费");
		records.add("车辆使用费,保养维修");
		records.add("车辆使用费,年审");
		records.add("车辆使用费,停车费");
		records.add("车辆使用费,违规罚款");
		records.add("车辆使用费,洗车卡");
		records.add("车辆使用费,油费");
		records.add("基础设施费,大成管理费");
		records.add("基础设施费,电费");
		records.add("基础设施费,房屋租赁");
		records.add("基础设施费,网络平台维护");
		records.add("基础设施费,展厅库房维修");
		records.add("经营费,花车摊位费");
		records.add("经营费,缴纳车辆税费");
		records.add("经营费,缴纳地税");
		records.add("经营费,缴纳国税");
		records.add("经营费,投资铺面");
		records.add("生活费,餐费");
		records.add("生活费,家庭生活");
		records.add("生活费,其他餐费");
		records.add("通讯费,电话宽带费");
		records.add("物料消耗费,买水纸巾等");
		records.add("物流费,厂家到货运费");
		records.add("物流费,返厂运费");
		records.add("物流费,客户发货及返货");
		records.add("物流费,快递费");
		records.add("物流费,员工送货费");
		records.add("销售管理费,客户返点");
		records.add("销售管理费,销售折让");
		records.add("业务费,会务费");
		records.add("业务费,日常招待费");
		records.add("业务费,商标及公司注册");
		records.add("银行手续费,汇款手续费");
		records.add("银行手续费,信用卡年费");
		records.add("银行手续费,支付宝/微信提现");
		records.add("员工福利,购买社保");
		records.add("员工福利,基本工资");
		records.add("员工福利,奖金分红");
		records.add("员工福利,聚餐");
		records.add("员工福利,找店铺奖励");
		records.add("折旧损失费,串货损失");
		records.add("折旧损失费,货物丢失");
		records.add("折旧损失费,现金亏损");
		records.add("折旧损失费,资产折旧");
		records.add("职工教育费,高层培训");
		records.add("职工教育费,员工培训");
		records.add("装修费,展厅库房装修");
		records.add("咨询费,法律顾问");
		records.add("咨询费,公益捐赠");

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
