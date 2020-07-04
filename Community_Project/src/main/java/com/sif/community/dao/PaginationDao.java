package com.sif.community.dao;

import org.apache.ibatis.annotations.Param;

import com.sif.community.model.PaginationDTO;

public interface PaginationDao {
	
	public PaginationDTO findByBiId(@Param("bi_id") Long bi_id, @Param("page_location") String page_location);

	public int insert(PaginationDTO pageDTO);

	public int update(PaginationDTO pageDTO);

}
