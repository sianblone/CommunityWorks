package com.sif.community.dao;

import java.util.List;

import com.sif.community.model.CategoryVO;

public interface CategoryDao {
	
	public List<CategoryVO> findByBiId(long bi_id);
	
	public int insert(List<CategoryVO> categoryList);
	public int update(CategoryVO categoryVO);
	public int delete(long cate_id);

}
