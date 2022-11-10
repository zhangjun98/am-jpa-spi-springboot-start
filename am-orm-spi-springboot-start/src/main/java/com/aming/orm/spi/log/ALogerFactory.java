package com.aming.orm.spi.log;

public class ALogerFactory {


	
	public static ALoger getLoger(Class<?> clazz) {
		return getLoger(clazz.getName());
	}

 
	public static ALoger getLoger(String name) {
		return   ALog4Provider.getLoger(name);
	}
	
	

}
