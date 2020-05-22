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
	

	private long board_no;//	BIGINT
	private long board_p_no;//	BIGINT
	private String board_name;//	VARCHAR(20)
	private String board_writer;//	VARCHAR(50)
	private String board_date;//	DATE
	private String board_time;//	VARCHAR(10)
	private String board_subject;//	VARCHAR(125)
	private String board_content;//	VARCHAR(1000)
	private long board_count;//	BIGINT
	private String board_filename;//	VARCHAR(256)
	private int board_delete;//	TINYINT
	private long board_recommend;//	BIGINT
	private String board_category;//	VARCHAR(20)
	
	private List<BoardVO> board_reply;
	
	private String search_type;
	private String search_txt;

}
