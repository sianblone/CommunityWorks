package com.sif.community.service.board.itf;

import com.sif.community.model.PaginationDTO;

public interface PaginationService {
	
	// 한 페이지에 보여줄 데이터(로우) 개수
	public void setDataPerPage(int dataPerPage);
	
	// 페이지 하단에 보여줄 페이지 개수(범위)
	public void setPageRange(int pageRange);
	
	public PaginationDTO findByBoardInfo(Long bi_id, String page_location);
	
	// DB, API에서 받아온 dataCount(총 게시물 수)
	// 페이지에서 받아온 pageNo(현재 페이지 번호)를 가지고 PaginationDTO 세팅하기
	public PaginationDTO makePageInfoMiddle(long dataCount, PaginationDTO pageOptionDTO, int pageNo, boolean isReversePagination);
	
	public PaginationDTO makePageInfo(long dataCount, PaginationDTO pageOptionDTO, int pageNo, boolean isReversePagination, boolean isMiddlePagination);

}