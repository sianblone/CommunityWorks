package com.sif.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.sif.community.model.CommentVO;

public interface CommentDao {
	
	public List<CommentVO> selectAll();
	public CommentVO findByNo(long cmt_no);
	
	@Select("SELECT * FROM tbl_comment "
			+ " WHERE cmt_board_no = #{cmt_board_no} "
			+ " AND cmt_p_no = 0 ORDER BY cmt_date, cmt_time DESC")
	public List<CommentVO> findByBoardNo(long cmt_board_no);
	
	@Select("SELECT * FROM tbl_comment "
			+ " WHERE cmt_p_no = #{cmt_p_no} "
			+ " ORDER BY cmt_date, cmt_time DESC")
	public List<CommentVO> findByPNo(long cmt_p_no);
	
//	@Insert("INSERT INTO tbl_comment (cmt_no,cmt_writer,cmt_content ) "
//			+ " VALUES (#{cmt_no}, #{cmt_writer}, #{cmt_content} ) " )
	public int insert(CommentVO commentVO);
	
	public int update(CommentVO commentVO);
	
	@Delete("DELETE FROM tbl_comment WHERE cmt_no = #{cmt_no}")
	public int delete(long cmt_no);

}
