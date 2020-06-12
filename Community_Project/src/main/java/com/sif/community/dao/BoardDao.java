package com.sif.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sif.community.model.BoardInfoVO;
import com.sif.community.model.BoardVO;
import com.sif.community.model.CategoryVO;
import com.sif.community.model.PaginationVO;
import com.sif.community.model.UserDetailsVO;

public interface BoardDao {

	public List<BoardVO> selectAllByPage(@Param("boardVO") BoardVO boardVO, @Param("pageVO") PaginationVO pageVO, @Param("usernameList") List<UserDetailsVO> usernameList);
	public long countAll(@Param("boardVO") BoardVO boardVO, @Param("usernameList") List<UserDetailsVO> usernameList);
	public BoardVO findByNo(long board_no);
	public List<CategoryVO> selectCategoryByBoard(BoardVO boardOptionVO);
	public BoardInfoVO findByBoardInfo(long board_info);
	public List<BoardInfoVO> selectAllBoardInfo();
	
	public int insert(BoardVO boardVO);
	public int update(BoardVO boardVO);
	public int delete(BoardVO boardVO);
	
}
