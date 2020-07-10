package com.sif.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationDTO;
import com.sif.community.model.UserDetailsVO;

public interface BoardDao {
	
	public long countAll(@Param("boardVO") BoardVO boardOptionVO, @Param("usernameList") List<UserDetailsVO> usernameList, @Param("isAdmin") boolean isAdmin);
	public List<BoardVO> selectAllByPage(@Param("boardVO") BoardVO boardOptionVO, @Param("pageDTO") PaginationDTO pageDTO, @Param("usernameList") List<UserDetailsVO> usernameList, @Param("isAdmin") boolean isAdmin);
	public BoardVO findByBoardNo(long board_no);
	public int maxOrderByBoardGroup(long board_group);
	
	public int insert(BoardVO boardVO);
	public int update(BoardVO boardVO);
	public int updateDeleteFlag(BoardVO boardVO);
	public int updateBoardCount(BoardVO boardOptionVO);
	public int updateBoardRecommend(BoardVO boardOptionVO);
	public int orderMinusOneWhenDelete(long board_no);
	public int delete(long board_no);
	
}
