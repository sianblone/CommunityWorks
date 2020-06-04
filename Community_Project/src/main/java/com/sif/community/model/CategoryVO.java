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
public class CategoryVO {
	
	private long cate_id;//	BIGINT		PRIMARY KEY	AUTO_INCREMENT
	private long cate_bi_id;//	BIGINT	NOT NULL		
	private String cate_text;//	VARCHAR(20)

}
