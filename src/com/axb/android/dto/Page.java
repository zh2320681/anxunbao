package com.axb.android.dto;

public class Page {
	private int pageIndex; //��ǰ�ǵڼ�ҳ ��0��ʼ
	private int pageSize; //ÿҳ�ĸ���
	private int totalIndex; //�ܹ��ж���ҳ
	private int totalSize; //һ���ж��ټ�¼
	
	public Page(int pageIndex, int pageSize) {
		super();
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}
	
	
	
	public Page() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalIndex() {
		return totalIndex;
	}
	public void setTotalIndex(int totalIndex) {
		this.totalIndex = totalIndex;
	}
	public int getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	
	//�������ǰ �Ӷ��ٿ�ʼ
	public int getCurrentStartSize(){
		return pageIndex*pageSize;
	}
	
	//�������ǰ �ж���ҳ
	public int computeTotalIndex(){
		return pageSize == 0?0:
			(totalIndex=(totalSize%pageSize==0?totalSize/pageSize:totalSize/pageSize+1));
	}
	
	
	public boolean addPage(){
		if(getTotalIndex() == 0){
			pageIndex = 0;
			return false;
		}
		if(pageIndex >= getTotalIndex()-1){
			pageIndex = (getTotalIndex()-1);
			return false;
		}
		pageIndex++;
		return true;
	}
	
}
