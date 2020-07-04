package com.sif.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sif.community.model.UserDetailsVO;

public interface UserDao {
	
	public UserDetailsVO findByUsernameFromAuthorities(String username);

	public List<UserDetailsVO> selectAll();
	public UserDetailsVO findByUsername(String username);
	@Select("SELECT username FROM tbl_users WHERE email = #{email}")
	public List<UserDetailsVO> findByEmail(String email);
	public List<UserDetailsVO> findByNickname(String nickname);
	
	public int insert(UserDetailsVO userVO);
	public int updateUserFromAdmin(UserDetailsVO userVO);
	public int updateUser(UserDetailsVO userVO);
	public int updateEmail(UserDetailsVO userVO);
	public int updatePw(UserDetailsVO userVO);
	
	

}
