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
public class CommentVO {
	
	private int cmt_board_no;//	BIGINT
	private int cmt_no;//	BIGINT
	private String cmt_p_no;//	VARCHAR(20)
	private String cmt_writer;//	VARCHAR(50)
	private String cmt_date;//	DATE
	private String cmt_time;//	VARCHAR(10)
	private String cmt_content;//	VARCHAR(1000)
	private int cmt_delete;//	TINYINT
	private int cmt_recommend;//	BIGINT

}
