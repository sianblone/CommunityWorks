package com.sif.community.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AccessLevel;
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
	private long cmt_p_no;//	VARCHAR(20)	NOT NULL
	private long cmt_group;//	BIGINT	NOT NULL
	private int cmt_order;//	INT	NOT NULL
	private int cmt_depth;//	INT	NOT NULL
	private String cmt_writer;//	VARCHAR(50)	NOT NULL		
	private Date db_insert_datetime;// TIMESTAMP, DB에 INSERT 시 board_datetime에 넣을 칼럼
	@Getter(AccessLevel.NONE) private Date cmt_datetime;// TIMESTAMP, DB에서 SELECT할 때 형식 변경, lombok에서 getter 생성 안함	
	private String cmt_content;//	VARCHAR(1000)	NOT NULL		
	private int cmt_delete;//	TINYINT	NOT NULL		DEFAULT 0
	private long cmt_recommend;//	BIGINT	NOT NULL		DEFAULT 0
	
	private String cmt_nickname;
	
	// datetime 가공
	public String getCmt_datetime() {
		String datetime = "";
		
		if(this.cmt_datetime == null) {
			datetime = null;
		} else {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			
			// 오늘 쓴 글이면 시간만 표시
			if( dateFormat.format(date).equals( dateFormat.format(this.cmt_datetime) ) ) {
				datetime = timeFormat.format(this.cmt_datetime);
			} else {
				// 오늘 전에 쓴 글이면 날짜만 표시
				datetime = dateFormat.format(this.cmt_datetime);
			}
		}
		return datetime;
	}

}
