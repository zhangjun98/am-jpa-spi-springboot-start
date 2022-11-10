package com.aming.orm.spi.pager.support;


import com.aming.orm.spi.pager.AiPageParam;


/**
 * 创建人: 张俊
 *  创建时间: 2022/11/10 15:22
 * 描述: 简单分页参数
 */

public class SimplePageParam implements AiPageParam {
	
	

	private int pageSize = 0;//分页大小
	

	private long startRow = 0;
	

	private int pageNum = 0;
	
	


	public SimplePageParam() {
		
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public long getStartRow() {
		return startRow;
	}

	public int getPageNum() {
		return pageNum;
	}

	@Override
	public int pageSize() {
		return pageSize;
	}
	
	@Override
	public long startRow() {
		return startRow;
	}
	@Override
	public int pageNum() {
		return pageNum;
	}

	@Override
	public AiPageParam putParam(Integer pagesize, Integer pagenum, Long startrow) {
		if(pagesize != null) {
        	this.pageSize = pagesize.intValue();
        }else {
        	this.pageSize = 25;
        }
        if(pagenum != null) {
        	this.pageNum = pagenum.intValue();
        	this.startRow = this.pageNum * this.pageSize;
        }else if(startrow != null) {
        	this.startRow = startrow.intValue();
        	this.pageNum = (int) ((startRow / pageSize) + 1);
        }else {
        	this.pageNum = 1;
        	this.startRow = 0;
        }
        
        return this;
	}

	@Override
	public boolean countTotal() {
		return true;
	}
	
}
