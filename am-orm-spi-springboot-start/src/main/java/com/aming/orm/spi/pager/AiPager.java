package com.aming.orm.spi.pager;



public interface AiPager<T>  {
	
	//总条数
	public long total();

	public Iterable<T> value();

	public AiPageParam pageParam();
	
	public AiPager<T> put(Iterable<T> list, long total, AiPageParam pageParam);
	

}
