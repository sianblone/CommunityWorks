package com.sif.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sif.community.model.CommentVO;
import com.sif.community.model.PaginationDTO;

public interface CommentDao {
	
	public long countAll(@Param("commentVO") CommentVO commentVO, @Param("isAdmin") boolean isAdmin);
	public List<CommentVO> selectAllByPage(@Param("commentVO") CommentVO commentVO, @Param("pageDTO") PaginationDTO pageDTO, @Param("isAdmin") boolean isAdmin);
	public CommentVO findByCmtNo(long comment_no);
	public int maxOrderByCmtGroup(long cmt_group);
	
	public int insert(CommentVO commentVO);
	public int update(CommentVO commentVO);
	public int updateDelete(CommentVO commentVO);
	public int delete(long comment_no);
	

}
