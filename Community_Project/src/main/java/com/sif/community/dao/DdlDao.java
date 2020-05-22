package com.sif.community.dao;

import org.apache.ibatis.annotations.Select;

public interface DdlDao {
	
	@Select("${create_table}")
	public void create_table(String create_table);
	
}
