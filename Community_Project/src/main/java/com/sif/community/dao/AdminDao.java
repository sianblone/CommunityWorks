package com.sif.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationVO;
import com.sif.community.model.UserDetailsVO;

public interface AdminDao {
	
	public List<BoardVO> selectAllByPageAdmin(@Param("boardVO") BoardVO boardVO, @Param("pageVO")PaginationVO pageVO);
	public long countAll(BoardVO boardVO);
	
	public int update_user_from_admin(UserDetailsVO dbUserVO);

}
