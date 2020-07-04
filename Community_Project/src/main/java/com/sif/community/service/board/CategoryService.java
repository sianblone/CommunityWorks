package com.sif.community.service.board;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sif.community.dao.CategoryDao;
import com.sif.community.model.CategoryVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service(value = "cateSvc")
public class CategoryService {
	
	private final CategoryDao cateDao;
	
	public List<CategoryVO> findByBiId(long bi_id) {
		return cateDao.findByBiId(bi_id);
	}
	
	public int insert(List<CategoryVO> categoryList) {
		return cateDao.insert(categoryList);
	}
	
	public int update(CategoryVO categoryVO) {
		return cateDao.update(categoryVO);
	}
	
	public int delete(long cate_id) {
		return cateDao.delete(cate_id);
	}
	
}
