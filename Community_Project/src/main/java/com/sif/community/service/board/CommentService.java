package com.sif.community.service.board;

import java.util.List;

import com.sif.community.model.CommentVO;

public interface CommentService {

	public List<CommentVO> selectAll();
	public CommentVO findByNo(long cmt_no);
	
	public List<CommentVO> findByBoardNo(long cmt_board_no);
	public List<CommentVO> findByPNo(long cmt_p_no);
	
	public int insert(CommentVO commentVO);
	public int update(CommentVO commentVO);
	
	public int delete(long cmt_no);
}
