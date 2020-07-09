package com.sif.community.dao;

import java.util.List;

import com.sif.community.model.AuthorityVO;

public interface AuthoritiesDao {
	
	public List<AuthorityVO> findByUsername(String username);
	
	public int insert(List<AuthorityVO> authList);
	public int delete(String username);

}
