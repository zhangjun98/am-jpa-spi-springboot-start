package com.aming.orm.spi.pager;


import com.aming.orm.spi.pager.support.SimplePageParam;
import com.aming.orm.spi.pager.support.SimplePager;

import java.lang.reflect.Constructor;

/**
 * 分页工具类
 * @author zhangjun
 *
 */
public class AcPageConfig {
	
	
	private AcPageConfig() {
		
	}
	
	private static Class<? extends AiPageParam> defaultPageParamWrapClass = SimplePageParam.class;
	
	public static<K> void setDefaultPageParamClass(Class<? extends AiPageParam> cusWrapClass) {
		if(cusWrapClass != null)
			AcPageConfig.defaultPageParamWrapClass = cusWrapClass;
	}
	
	public static Class<? extends AiPageParam> getDefaultPageParamClass() {
		return AcPageConfig.defaultPageParamWrapClass;
	}
	
	public static AiPageParam getDefaultPageParam(){
		Constructor<? extends AiPageParam> cons = null;
		try {
			cons = getDefaultPageParamClass().getConstructor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		AiPageParam res = null;
		try {
			res = cons.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	
	@SuppressWarnings("rawtypes")
	private static Class<? extends AiPager> defaultPagerWrapClass = SimplePager.class;
	
	@SuppressWarnings("rawtypes")
	public static<K> void setDefaultPagerClass(Class<? extends AiPager> cusWrapClass) {
		if(cusWrapClass != null)
			AcPageConfig.defaultPagerWrapClass = cusWrapClass;
	}
	
	@SuppressWarnings("rawtypes")
	public static Class<? extends AiPager> getDefaultPagerClass() {
		return AcPageConfig.defaultPagerWrapClass;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static<K> AiPager<K> getDefaultPager(Class<K> resultType){
		Constructor<? extends AiPager> cons = null;
		try {
			cons = getDefaultPagerClass().getConstructor(Class.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		AiPager<K> res = null;
		try {
			res = cons.newInstance(resultType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	

}
