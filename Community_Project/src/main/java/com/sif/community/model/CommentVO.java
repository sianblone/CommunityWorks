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
	@Setter(AccessLevel.NONE) private Date cmt_datetime;// TIMESTAMP, lombok에서 setter 생성 안함, 날짜 데이터 주입 시 cmt_custom_datetime, cmt_custom_full_datetime 에도 가공된 날짜 데이터 주입	
	private String cmt_content;//	VARCHAR(1000)	NOT NULL		
	private int cmt_delete;//	TINYINT	NOT NULL		DEFAULT 0
	private long cmt_recommend;//	BIGINT	NOT NULL		DEFAULT 0
	
	private String cmt_nickname;
	private String cmt_custom_datetime;
	private String cmt_custom_full_datetime;
	
	// custom_datetime 가공
	public void setCmt_datetime(Date cmt_datetime) {
		this.cmt_datetime = cmt_datetime;
		
		String custom_datetime = "";
		String custom_full_datetime = "";
		
		if(cmt_datetime == null) {
			custom_datetime = null;
			custom_full_datetime = null;
		} else {
			// 현재 시간
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			
			// 오늘 쓴 글이면 시간만 표시
			if( dateFormat.format(date).equals( dateFormat.format(cmt_datetime) ) ) {
				custom_datetime = timeFormat.format(cmt_datetime);
			} else {
				// 오늘 전에 쓴 글이면 날짜만 표시
				custom_datetime = dateFormat.format(cmt_datetime);
			}
			
			SimpleDateFormat fullDateTimeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			custom_full_datetime = fullDateTimeFormat.format(cmt_datetime);
		}
		this.cmt_custom_datetime = custom_datetime;
		this.cmt_custom_full_datetime = custom_full_datetime;
	}

}
