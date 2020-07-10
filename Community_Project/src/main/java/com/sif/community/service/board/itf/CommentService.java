package com.sif.community.service.board.itf;

import java.util.List;

import com.sif.community.model.CommentVO;
import com.sif.community.model.PaginationDTO;

public interface CommentService {

	public long countAll(CommentVO commentVO);
	public List<CommentVO> selectAllByPage(CommentVO commentVO, PaginationDTO pageDTO);
	public CommentVO findByCmtNo(long cmt_no);
	
	public int save(CommentVO commentVO);
	public int updateDeleteFlag(long cmt_no);
	public int delete(long cmt_no);
}
