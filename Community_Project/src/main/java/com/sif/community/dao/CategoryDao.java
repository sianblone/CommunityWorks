package com.sif.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.sif.community.model.CategoryVO;

public interface CategoryDao {
	
	// 게시판 ID를 기준으로 카테고리 SELECT
	@Select("SELECT * FROM tbl_category WHERE cate_bi_id = #{bi_id}")
	public List<CategoryVO> findByBoardId(long bi_id);
	
	// 카테고리 테이블 INSERT
	public int insert(List<CategoryVO> categoryList);
	// 카테고리 테이블 UPDATE
	public int update(CategoryVO categoryVO);
	
	// 카테고리 삭제
	@Delete("DELETE FROM tbl_category WHERE cate_id = #{cate_id}")
	public int delete(long cate_id);

}
