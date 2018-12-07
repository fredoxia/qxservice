package com.onlineMIS.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.onlineMIS.common.loggerLocal;

public class SystemParm {

	public static final Properties SystemParm = new Properties();
	
	public static void load() {
		try {
			InputStream is = SystemParm.class.getClassLoader()
					 .getResourceAsStream("sysParms/SystemParm.properties");
			
			SystemParm.load(is);
			
			
		} catch (IOException e) {
			loggerLocal.error(e);
		}
	}
	
	public static String getParm(String parmName){
		String value = SystemParm.getProperty(parmName);
		if (value == null)
			value = "";
		return value.trim();
	}
	
	public static int getTestSupplierId(){
		String TEST_ID_OBJ = getParm("TEST_SUPPLIER_ID");
		if (!StringUtils.isEmpty(TEST_ID_OBJ)){
			int TEST_ID = Integer.parseInt(TEST_ID_OBJ);
			return TEST_ID;
		} else 
			return -1;
	}
	
	public static int getTestClientId(){
		String TEST_ID_OBJ = getParm("TEST_CLIENT_ID");
		if (!StringUtils.isEmpty(TEST_ID_OBJ)){
			int TEST_ID = Integer.parseInt(TEST_ID_OBJ);
			return TEST_ID;
		} else 
			return -1;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
