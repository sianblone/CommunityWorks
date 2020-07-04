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
public class BoardInfoVO {
	
	private long bi_id;//	BIGINT		PRIMARY KEY	AUTO_INCREMENT
	private String bi_name;//	VARCHAR(100)	NOT NULL
	private boolean bi_enabled;
		
	private List<CategoryVO> bi_category;
	private List<BoardVO> bi_board_list;
	
}
