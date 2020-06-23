package com.sif.community.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
public class BoardVO {

	private long board_no;// BIGINT PRIMARY KEY AUTO_INCREMENT
	private long board_p_no;// BIGINT NOT NULL DEFAULT 0
	private long board_group;//	BIGINT	NOT NULL
	private int board_order;//	INT	NOT NULL
	private int board_depth;//	INT	NOT NULL
	private long board_info;// BIGINT
	private String board_writer;// VARCHAR(50) NOT NULL
	@Setter(AccessLevel.NONE) private Date board_datetime;// TIMESTAMP, lombok에서 setter 생성 안함, 날짜 데이터 주입 시 board_custom_datetime, board_custom_full_datetime 에도 가공된 날짜 데이터 주입
	private String board_subject;// VARCHAR(125) NOT NULL
	private String board_content;// VARCHAR(1000) NOT NULL
	private long board_count;// BIGINT DEFAULT 0
	private String board_filename;// VARCHAR(256)
	private int board_delete;// TINYINT NOT NULL DEFAULT 0
	private long board_recommend;// BIGINT NOT NULL DEFAULT 0
	private Integer board_category;// INT // int가 아니라 Integer인 이유는 0 대신 null값을 세팅할 수 있도록

	private String board_nickname;
	private String board_name;
	private String board_custom_datetime;
	private String board_custom_full_datetime;
	private String board_cate_text;
	private Integer board_cmt_count;

	private String search_type;
	private String search_txt;
	
	// custom_datetime 가공
	public void setBoard_datetime(Date board_datetime) {
		this.board_datetime = board_datetime;
		
		String custom_datetime = "";
		String custom_full_datetime = "";
		
		if(board_datetime == null) {
			custom_datetime = null;
			custom_full_datetime = null;
		} else {
			// 현재 시간
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			
			// 오늘 쓴 글이면 시간만 표시
			if( dateFormat.format(date).equals( dateFormat.format(board_datetime) ) ) {
				custom_datetime = timeFormat.format(board_datetime);
			} else {
				// 오늘 전에 쓴 글이면 날짜만 표시
				custom_datetime = dateFormat.format(board_datetime);
			}
			
			SimpleDateFormat fullDateTimeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			custom_full_datetime = fullDateTimeFormat.format(board_datetime);
		}
		this.board_custom_datetime = custom_datetime;
		this.board_custom_full_datetime = custom_full_datetime;
	}

}