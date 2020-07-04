package com.sif.community.service.board;

import org.springframework.stereotype.Service;

import com.sif.community.dao.DdlDao;
import com.sif.community.dao.sql.CreateTableSQL;

@Service
public class DdlService {
	
	private final DdlDao ddlDao;
	
	public DdlService(DdlDao ddlDao) {
		this.ddlDao = ddlDao;
		
		ddlDao.create_table(CreateTableSQL.create_tbl_users);// Spring Security와 연동할 유저 테이블 생성
		ddlDao.create_table(CreateTableSQL.create_authorities);// Spring Security와 연동할 권한 테이블 생성
		
		ddlDao.create_table(CreateTableSQL.create_tbl_board_info);// 게시판 정보 테이블 생성
		ddlDao.create_table(CreateTableSQL.create_tbl_category);// 카테고리 테이블 생성
		ddlDao.create_table(CreateTableSQL.create_tbl_pagination);// 페이지네이션 테이블 생성
		ddlDao.create_table(CreateTableSQL.create_tbl_board);// 게시판 테이블 생성
		ddlDao.create_table(CreateTableSQL.create_tbl_comment);// 댓글 테이블 생성
	}

}