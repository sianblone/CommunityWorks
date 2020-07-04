package com.sif.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sif.community.model.BoardInfoVO;

public interface BoardInfoDao {
	
	public List<BoardInfoVO> selectAll();
	public BoardInfoVO findByBiId(long bi_id);
	public List<BoardInfoVO> selectMainPage(@Param("limit_value") int limit_value);
	
	public int insert(BoardInfoVO boardInfoVO);
	public int update(BoardInfoVO boardInfoVO);
	public int delete(long bi_id);
}
