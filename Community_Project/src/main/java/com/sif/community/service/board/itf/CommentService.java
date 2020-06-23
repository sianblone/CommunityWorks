package com.sif.community.service.board.itf;

import java.util.List;

import com.sif.community.model.CommentVO;
import com.sif.community.model.PaginationVO;

public interface CommentService {

	public long countAll(CommentVO commentVO);
	public List<CommentVO> selectAllByPage(CommentVO commentVO, PaginationVO pageVO);
	public CommentVO findByCmtNo(long comment_no);
	
	public int save(CommentVO commentVO);
	public String delete(long cmt_no, Integer currPage);
}
