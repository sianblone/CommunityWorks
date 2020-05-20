package com.sif.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sif.community.model.BoardVO;

public interface BoardDao {

	public List<BoardVO> selectAllByPage(BoardVO boardVO);
	public long countAll(BoardVO boardVO);
	public int insert(BoardVO boardVO);
	public int update(BoardVO boardVO);
	public BoardVO findByNo(Long no);
	
	
}
