package com.sif.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationVO;

public interface BoardDao {

	public List<BoardVO> selectAllByPage(@Param("boardVO") BoardVO boardVO, @Param("pageVO")PaginationVO pageVO);
	public long countAll(BoardVO boardVO);
	public BoardVO findByNo(long board_no);
	
	public int insert(BoardVO boardVO);
	public int update(BoardVO boardVO);
	public int delete(BoardVO boardVO);
	
}
