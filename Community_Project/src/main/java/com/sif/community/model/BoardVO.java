package com.sif.community.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class BoardVO {
	
	private long board_no;// BIGINT PRIMARY KEY AUTO_INCREMENT 
	private long board_p_no;// BIGINT NOT NULL DEFAULT 0
	private long board_info;// BIGINT
	private String board_writer;// VARCHAR(50) NOT NULL
	private String board_date;// DATE
	private String board_time;// VARCHAR(10)
	private String board_subject;// VARCHAR(125) NOT NULL
	private String board_content;// VARCHAR(1000) NOT NULL
	private long board_count;// BIGINT DEFAULT 0
	private String board_filename;// VARCHAR(256)
	private int board_delete;// TINYINT NOT NULL DEFAULT 0
	private long board_recommend;// BIGINT NOT NULL DEFAULT 0
	private Integer board_category;// INT // int가 아니라 Integer인 이유는 외래키에 null값을 세팅할 수 있도록
	
	private String board_nickname;
	private String board_name;
	private List<BoardVO> board_reply;
	
	private String search_type;
	private String search_txt;

}
