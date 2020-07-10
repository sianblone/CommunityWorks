package com.sif.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sif.community.model.CommentVO;
import com.sif.community.model.PaginationDTO;

public interface CommentDao {
	
	public long countAll(@Param("commentVO") CommentVO commentVO, @Param("isAdmin") boolean isAdmin);
	public List<CommentVO> selectAllByPage(@Param("commentVO") CommentVO commentVO, @Param("pageDTO") PaginationDTO pageDTO, @Param("isAdmin") boolean isAdmin);
	public CommentVO findByCmtNo(long cmt_no);
	public int maxOrderByCmtGroup(long cmt_group);
	
	public int insert(CommentVO commentVO);
	public int update(CommentVO commentVO);
	public int updateDeleteFlag(CommentVO commentVO);
	public int orderMinusOneWhenDelete(long cmt_no);
	public int delete(long cmt_no);

}
