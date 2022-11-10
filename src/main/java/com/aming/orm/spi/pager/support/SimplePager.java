package com.aming.orm.spi.pager.support;


import com.aming.orm.spi.pager.AiPager;
import com.aming.orm.spi.pager.AiPageParam;

@SuppressWarnings("serial")

public class SimplePager<T> implements AiPager<T> {
	
	//@GwModelField(text="版本号")
	//protected String version = PRODUCT_VERSION;//版本号
	

	private int pageSize = 0;//状态码
	

	private long startRow = 0;
	

	private int pageNum = 0;
	
	
	

	private AiPageParam pageParam;
	

	private long total = 0;//消息类型
	

	private Iterable<T> list;// 数据

	
	//
	public SimplePager() {
	}
	

	public int getPageSize() {
		return pageSize;
	}
	@Override
	public long total() {
		return total;
	}

	@Override
	public Iterable<T> value() {
		return list;
	}
	@Override
	public AiPager<T> put(Iterable<T> list, long total, AiPageParam pageParam) {
		this.list = list;
		this.total = total;
		this.pageParam = pageParam;
		this.pageNum = pageParam.pageNum();
		this.startRow = pageParam.startRow();
		this.pageSize = pageParam.pageSize();
		
		return this;
	}
	
	
	public AiPageParam getPageParam() {
		return pageParam;
	}

	public long getTotal() {
		return total;
	}

	public long getStartRow() {
		return startRow;
	}
	
	
	public int getPageNum() {
		return pageNum;
	}
	
	public Iterable<T> getList() {
		return list;
	}

	@Override
	public AiPageParam pageParam() {
		return pageParam;
	}
}
