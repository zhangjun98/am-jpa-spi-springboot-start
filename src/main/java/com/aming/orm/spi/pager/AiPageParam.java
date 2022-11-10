package com.aming.orm.spi.pager;



import java.io.Serializable;

/**
 * 
 * 包装分页请求参数的接口
 * 
 * @author zhangjun
 *
 */
public interface AiPageParam extends Serializable {
	
	
	//每页的数量 limit
	public int pageSize();
	//游标开始行
	public long startRow();
	//当前页码
	public int pageNum();
	
	/**
     * 是否包含count查询
     */
    public boolean countTotal();

	
	public AiPageParam putParam(Integer pageSize, Integer pageNum, Long startRow);
	

	
	public static AiPageParam of(Integer pageSize, Integer pageNum, Long startRow) {
		
		AiPageParam pp = AcPageConfig.getDefaultPageParam();
		
		pp.putParam(pageSize, pageNum, startRow);
		
		return pp;
	}
	
}
