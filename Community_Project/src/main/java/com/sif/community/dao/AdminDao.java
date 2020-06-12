package com.sif.community.dao;

import com.sif.community.model.BoardInfoVO;
import com.sif.community.model.UserDetailsVO;

public interface AdminDao {
	
	public int update_user_from_admin(UserDetailsVO dbUserVO);
	public int create_board(BoardInfoVO boardInfoVO);
	public int update_tbl_board_info(BoardInfoVO dbBoardInfoVO);

}
