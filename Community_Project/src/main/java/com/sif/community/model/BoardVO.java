package com.sif.community.model;

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
	
	private int board_no;//	BIGINT
	private int board_p_no;//	BIGINT
	private String board_name;//	VARCHAR(20)
	private String board_writer;//	VARCHAR(50)
	private String board_date;//	DATE
	private String board_time;//	VARCHAR(10)
	private String board_subject;//	VARCHAR(125)
	private String board_content;//	VARCHAR(1000)
	private int board_count;//	BIGINT
	private String board_filename;//	VARCHAR(256)
	private int board_delete;//	TINYINT
	private int board_recommend;//	BIGINTboard_category	VARCHAR(20)
	private String board_category;//	VARCHAR(20)
	
	private String search_type;
	private String search_txt;

}
