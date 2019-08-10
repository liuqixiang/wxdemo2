package com.example.wxdemo.utils;

/**
 * 分页参数
 */
public class PageUtil {

	// 指定的或是页面参数
	private int currentPage; // 当前页
	private int pageSize; // 每页显示多少条

	// 查询数据库获取
	private int recordCount; // 总记录数

	// 计算获取
	private int pageCount; // 总页数
	private int beginPageIndex; // 页码列表的开始索引（包含）
	private int endPageIndex; // 页码列表的结束索引（包含）
	
	/**
	 * 只接受前4个必要的属性，会自动的计算出其他3个属生的值
	 * 
	 * @param currentPage 当前页
	 * @param pageSize 每页显示多少条
	 * @param recordCount 总记录数
	 * @param recordList 本页的数据列表
	 */
	public PageUtil(int currentPage, int pageSize, int recordCount) {
		
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.recordCount = recordCount;
		
		//计算总页码
		pageCount = (recordCount + pageSize  -1) / pageSize ;
		
		if(pageCount<1){
			pageCount=1;
		}
		
		if(this.currentPage<1){
			this.currentPage=1;
		}
		
		if(this.currentPage>pageCount){
			this.currentPage=pageCount;
		}
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getBeginPageIndex() {
		return beginPageIndex;
	}
	public void setBeginPageIndex(int beginPageIndex) {
		this.beginPageIndex = beginPageIndex;
	}
	public int getEndPageIndex() {
		return endPageIndex;
	}
	public void setEndPageIndex(int endPageIndex) {
		this.endPageIndex = endPageIndex;
	}
	
}
