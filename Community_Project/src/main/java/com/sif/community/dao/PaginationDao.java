package com.sif.community.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.sif.community.model.PaginationDTO;

public interface PaginationDao {
	
	@Select("SELECT * FROM tbl_pagination WHERE page_bi_id = #{bi_id} AND page_location = #{page_location}")
	public PaginationDTO findByBoardInfo(@Param("bi_id") Long bi_id, @Param("page_location") String page_location);

}
