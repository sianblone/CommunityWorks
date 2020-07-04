CREATE TABLE IF NOT EXISTS tbl_users (
	username	VARCHAR(50)	PRIMARY KEY,
	password	VARCHAR(125),
	enabled	BOOLEAN	DEFAULT true,
	accountNonExpired	BOOLEAN	DEFAULT true,
	accountNonLocked	BOOLEAN	DEFAULT true,
	credentialsNonExpired	BOOLEAN	DEFAULT true,
	nickname	VARCHAR(30)	NOT NULL,
	email	VARCHAR(125),
	phone	VARCHAR(20),
	birth	DATE
	)
;

CREATE TABLE IF NOT EXISTS authorities ( 
	id	BIGINT	PRIMARY KEY	AUTO_INCREMENT,
	username	VARCHAR(50)	NOT NULL,
	authority	VARCHAR(50),
	
	CONSTRAINT FK_USERS_AUTH_username
	FOREIGN KEY (username)
	REFERENCES tbl_users(username)
	ON DELETE CASCADE
	)
;

CREATE TABLE IF NOT EXISTS tbl_board_info (
	bi_id	BIGINT	PRIMARY KEY	AUTO_INCREMENT,
	bi_name	VARCHAR(100)	NOT NULL,
	bi_enabled	BOOLEAN	DEFAULT true,
	bi_order	BIGINT	NOT NULL
	)
;

CREATE TABLE IF NOT EXISTS tbl_category (
	cate_id	INT	PRIMARY KEY	AUTO_INCREMENT,
	cate_bi_id	BIGINT	NOT NULL,
	cate_text	VARCHAR(20),
	
	CONSTRAINT FK_BI_CATEGORY_bi_id
	FOREIGN KEY (cate_bi_id)
	REFERENCES tbl_board_info(bi_id)
	ON DELETE CASCADE
	)
;

CREATE TABLE IF NOT EXISTS tbl_pagination (
	page_id	BIGINT	PRIMARY KEY	AUTO_INCREMENT,
	page_bi_id	BIGINT,
	page_location	VARCHAR(125)	NOT NULL,
	page_data_cnt	INT	NOT NULL	DEFAULT 10,
	page_range	INT	NOT NULL	DEFAULT 10,
    
    CONSTRAINT FK_BI_PAGINATION_bi_id
    FOREIGN KEY (page_bi_id)
    REFERENCES tbl_board_info(bi_id)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tbl_board ( 
	board_no	BIGINT		PRIMARY KEY	AUTO_INCREMENT,
	board_p_no	BIGINT	NOT NULL		DEFAULT 0,
	board_group	BIGINT	NOT NULL,
	board_order	INT	NOT NULL,
	board_depth	INT	NOT NULL,
	board_info	BIGINT,
	board_writer	VARCHAR(50)	NOT NULL,
	board_datetime	TIMESTAMP	NOT NULL		DEFAULT CURRENT_TIMESTAMP,
	board_subject	VARCHAR(125)	NOT NULL,
	board_content	TEXT	NOT NULL,
	board_count	BIGINT			DEFAULT 0,
	board_filename	VARCHAR(260),
	board_delete	TINYINT	NOT NULL		DEFAULT 0,
	board_recommend	BIGINT	NOT NULL		DEFAULT 0,
	board_category	INT,
	
	CONSTRAINT FK_BI_BOARD_board_info
	FOREIGN KEY (board_info)
	REFERENCES tbl_board_info(bi_id)
	ON DELETE CASCADE,

	CONSTRAINT FK_CATE_BOARD_board_category
	FOREIGN KEY (board_category)
	REFERENCES tbl_category(cate_id)
	ON DELETE SET NULL
	)
;

CREATE TABLE IF NOT EXISTS tbl_comment (
	cmt_no	BIGINT	PRIMARY KEY	AUTO_INCREMENT,
	cmt_board_no	BIGINT	NOT NULL,
	cmt_p_no	VARCHAR(20)	NOT NULL,
	cmt_group	BIGINT	NOT NULL,
	cmt_order	INT	NOT NULL,	
	cmt_depth	INT	NOT NULL,
	cmt_writer	VARCHAR(50)	NOT NULL,
	cmt_datetime	TIMESTAMP	NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	cmt_content	TEXT	NOT NULL,
	cmt_delete	TINYINT	NOT NULL	DEFAULT 0,
	cmt_recommend	BIGINT	NOT NULL	DEFAULT 0,
	
	CONSTRAINT FK_BOARD_COMMENT_board_no
	FOREIGN KEY (cmt_board_no)
	REFERENCES tbl_board(board_no)
	)
;