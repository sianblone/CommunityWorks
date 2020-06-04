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
	
	private long cmt_no;//	BIGINT		PRIMARY KEY	AUTO_INCREMENT
	private long cmt_board_no;//	BIGINT	NOT NULL		
	private String cmt_p_no;//	VARCHAR(20)	NOT NULL		
	private String cmt_writer;//	VARCHAR(50)	NOT NULL		
	private String cmt_date;//	DATE			
	private String cmt_time;//	VARCHAR(10)			
	private String cmt_content;//	VARCHAR(1000)	NOT NULL		
	private int cmt_delete;//	TINYINT	NOT NULL		DEFAULT 0
	private long cmt_recommend;//	BIGINT	NOT NULL		DEFAULT 0

}
