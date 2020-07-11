package com.sif.community.dao;

import org.apache.ibatis.annotations.Param;

import com.sif.community.model.PaginationDTO;

public interface PaginationDao {
	
	/**
	 * @param bi_id 게시판 정보 ID(메인 페이지는 null 사용)
	 * @param page_location 데이터를 보여줄 페이지 위치(메인페이지, 게시판, 댓글)
	 * <br/>
	 * 들어갈 수 있는 값 목록 enum : {@link com.sif.enums.PageLocation}
	 * @return
	 */
	public PaginationDTO findByBiId(@Param("bi_id") Long bi_id, @Param("page_location") String page_location);

	public int insert(PaginationDTO pageDTO);
	public int update(PaginationDTO pageDTO);

}
