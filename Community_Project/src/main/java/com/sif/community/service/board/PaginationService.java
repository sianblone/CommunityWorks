package com.sif.community.service.board;

import com.sif.community.model.PaginationVO;

public interface PaginationService {
	
	// 페이지 당 보여줄 게시물 수
	public void setListPerPage(int listPerPage);
	
	// 하단에서 한 번에 보여줄 페이지 수(ex: 1 ~ 10)
	public void setPageCount(int pageCount);
	
	// DB, API에서 받아온 totalCount(총 게시물 수)
	// 페이지에서 받아온 currPageNo(현재 페이지 번호)를 가지고 페이지네이션 세팅하기(PaginationVO)
	public PaginationVO makePageInfo(long totalCount, int currPageNo);
	
}
