package com.sif.community.dao.sql;

public class CreateTableSQL {
	
	public static String create_tbl_users
		= "CREATE TABLE IF NOT EXISTS tbl_users ( "
		+ " username VARCHAR(50) PRIMARY KEY, "
		+ " password VARCHAR(125), "
		+ " enabled BOOLEAN DEFAULT true, "
		+ " accountNonExpired BOOLEAN DEFAULT true, "
		+ " accountNonLocked BOOLEAN DEFAULT true, "
		+ " credentialsNonExpired BOOLEAN DEFAULT true, "
		+ " nickname VARCHAR(30) NOT NULL, "
		+ " email VARCHAR(50), "
		+ " phone VARCHAR(20), "
		+ " age DATE "
		+ " ) "
	;
	
	public static String create_authorities
		= "CREATE TABLE IF NOT EXISTS authorities ( " 
		+ " id BIGINT PRIMARY KEY AUTO_INCREMENT, "
		+ " username VARCHAR(50) NOT NULL, "
		+ " authority VARCHAR(50), "
		
		+ " CONSTRAINT FK_USERS_AUTH_username "
		+ " FOREIGN KEY (username) "
		+ " REFERENCES tbl_users(username) "
		+ " ON DELETE CASCADE "
		+ " ) "
	;
	
	public static String create_tbl_board_info
		= "CREATE TABLE IF NOT EXISTS tbl_board_info ( "
		+ " bi_id BIGINT PRIMARY KEY AUTO_INCREMENT, "
		+ " bi_name VARCHAR(20) NOT NULL "
		+ " ) "
	;
	
	public static String create_tbl_category
		= "CREATE TABLE IF NOT EXISTS tbl_category ( "
		+ " cate_id INT UNIQUE AUTO_INCREMENT, "
		+ " cate_bi_id BIGINT NOT NULL, "
		+ " cate_text VARCHAR(20), "
		
		+ " CONSTRAINT FK_BI_CATEGORY_bi_id "
		+ " FOREIGN KEY (cate_bi_id) "
		+ " REFERENCES tbl_board_info(bi_id) "
		+ " ON DELETE CASCADE "
		+ " ) "
	;
	
	public static String create_tbl_board
		= "CREATE TABLE IF NOT EXISTS tbl_board ( " 
		+ " board_no BIGINT PRIMARY KEY AUTO_INCREMENT, " 
		+ " board_p_no BIGINT NOT NULL DEFAULT 0, " 
		+ " board_info BIGINT, "
		+ " board_writer VARCHAR(50) NOT NULL, "
		+ " board_date DATE, "
		+ " board_time VARCHAR(10), "
		+ " board_subject VARCHAR(125) NOT NULL, "
		+ " board_content VARCHAR(1000) NOT NULL, "
		+ " board_count BIGINT DEFAULT 0, "
		+ " board_filename VARCHAR(256), "
		+ " board_delete TINYINT NOT NULL DEFAULT 0, "
		+ " board_recommend TINYINT NOT NULL DEFAULT 0, "
		+ " board_category INT, "
		
		+ " CONSTRAINT FK_BI_BOARD_board_info "
		+ " FOREIGN KEY (board_info) "
		+ " REFERENCES tbl_board_info(bi_id), "
		
		+ " CONSTRAINT FK_USERS_BOARD_board_writer "
		+ " FOREIGN KEY (board_writer) "
		+ " REFERENCES tbl_users(username), "
		
		+ " CONSTRAINT FK_CATEGORY_BOARD_board_category "
		+ " FOREIGN KEY (board_category) "
		+ " REFERENCES tbl_category(cate_id) "
		+ " ) "
	;
	
	public static String create_tbl_comment
		= "CREATE TABLE IF NOT EXISTS tbl_comment ( "
		+ " cmt_no BIGINT PRIMARY KEY AUTO_INCREMENT, "
		+ " cmt_board_no BIGINT	NOT NULL, "
		+ " cmt_p_no VARCHAR(20) NOT NULL, "
		+ " cmt_writer VARCHAR(50) NOT NULL, "
		+ " cmt_date DATE, "
		+ " cmt_time VARCHAR(10), "
		+ " cmt_content	VARCHAR(1000) NOT NULL,	" 
		+ " cmt_delete TINYINT NOT NULL DEFAULT 0, "
		+ " cmt_recommend BIGINT NOT NULL DEFAULT 0, "
		
		+ " CONSTRAINT FK_BOARD_COMMENT_board_no "
		+ " FOREIGN KEY (cmt_board_no) "
		+ " REFERENCES tbl_board(board_no) "
		+ " ) "
	;
	
}