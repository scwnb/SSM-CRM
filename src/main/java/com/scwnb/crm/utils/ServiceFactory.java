package com.scwnb.crm.utils;

import com.scwnb.crm.utils.TransactionInvocationHandler;

public class ServiceFactory {
	
	public static Object getService(Object service){
		
		return new TransactionInvocationHandler(service).getProxy();
		
	}
	
}
