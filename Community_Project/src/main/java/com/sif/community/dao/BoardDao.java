package com.sif.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sif.community.model.BoardVO;

public interface BoardDao {

	@Select("SELECT * FROM tbl_board WHERE board_name = #{board_name}")
	public List<BoardVO> selectAllByBoardName(String board_name);
	
}
