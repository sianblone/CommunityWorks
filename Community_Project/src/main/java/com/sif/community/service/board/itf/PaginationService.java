package com.sif.community.service.board.itf;

import com.sif.community.model.PaginationVO;

public interface PaginationService {
	
	// 한 페이지에 보여줄 데이터(로우) 개수
	public void setDataPerPage(int dataPerPage);
	
	// 페이지 하단에 보여줄 페이지 개수(범위)
	public void setPageRange(int pageRange);
	
	// DB, API에서 받아온 dataCount(총 게시물 수)
	// 페이지에서 받아온 pageNo(현재 페이지 번호)를 가지고 PaginationVO 세팅하기
	public PaginationVO makePageInfoMiddle(long dataCount, int pageNo);
	
	public PaginationVO makePageInfo(long dataCount, int pageNo, boolean isMiddlePagination);
	
}