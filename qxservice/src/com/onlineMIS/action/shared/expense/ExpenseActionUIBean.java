package com.onlineMIS.action.shared.expense;

import java.util.ArrayList;
import java.util.List;

import com.onlineMIS.ORM.entity.chainS.user.ChainStore;
import com.onlineMIS.ORM.entity.chainS.user.ChainUserInfor;
import com.onlineMIS.ORM.entity.shared.expense.ExpenseType;

public class ExpenseActionUIBean {
	/**
	 * headq 二级菜单，
	 * chain的expense type菜单
	 */
	private List<ExpenseType> expenseTypes = new ArrayList<ExpenseType>();
	/**
	 * headq 的一级菜单
	 */
	private List<ExpenseType> parentExpenseTypes = new ArrayList<ExpenseType>();

	
	
	public List<ExpenseType> getParentExpenseTypes() {
		return parentExpenseTypes;
	}

	public void setParentExpenseTypes(List<ExpenseType> parentExpenseTypes) {
		this.parentExpenseTypes = parentExpenseTypes;
	}

	public List<ExpenseType> getExpenseTypes() {
		return expenseTypes;
	}

	public void setExpenseTypes(List<ExpenseType> expenseTypes) {
		this.expenseTypes = expenseTypes;
	}
	
}
