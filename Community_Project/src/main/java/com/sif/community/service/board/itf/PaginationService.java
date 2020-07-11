package com.sif.community.service.board.itf;

import com.sif.community.model.PaginationDTO;

public interface PaginationService {
	
	// 한 페이지에 보여줄 데이터(로우) 개수
	public void setDataPerPage(int dataPerPage);
	
	// 페이지 하단에 보여줄 페이지 개수(범위)
	public void setPageRange(int pageRange);
	
	/**
	 * @param bi_id 게시판 정보 ID(메인 페이지는 null 사용)
	 * @param page_location 데이터를 보여줄 페이지 위치(메인페이지, 게시판, 댓글)
	 * <br/>
	 * 들어갈 수 있는 값 목록 enum : {@link com.sif.enums.PageLocation}
	 * @return
	 */
	public PaginationDTO findByBiId(Long bi_id, String page_location);
	
	// DB, API에서 받아온 dataCount(총 게시물 수)
	// 페이지에서 받아온 pageNo(현재 페이지 번호)를 가지고 PaginationDTO 세팅하기
	public PaginationDTO makePageInfoMiddle(long dataCount, PaginationDTO pageOptionDTO, int pageNo, boolean isReversePagination);
	
	public PaginationDTO makePageInfo(long dataCount, PaginationDTO pageOptionDTO, int pageNo, boolean isReversePagination, boolean isMiddlePagination);

	public int insert(PaginationDTO pageDTO);
	
	public int update(PaginationDTO pageDTO);

}