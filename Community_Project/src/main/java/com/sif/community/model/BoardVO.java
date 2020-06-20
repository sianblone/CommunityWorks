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
	private long board_group;//	NOT NULL
	private int board_order;//	NOT NULL
	private int board_depth;//	NOT NULL
	private long board_info;// BIGINT
	private String board_writer;// VARCHAR(50) NOT NULL
	private Date db_insert_datetime;// TIMESTAMP, DB에 INSERT 시 board_datetime에 넣을 칼럼
	@Getter(AccessLevel.NONE) private Date board_datetime;// TIMESTAMP, DB에서 SELECT할 때 형식 변경, lombok에서 getter 생성 안함
	private String board_subject;// VARCHAR(125) NOT NULL
	private String board_content;// VARCHAR(1000) NOT NULL
	private long board_count;// BIGINT DEFAULT 0
	private String board_filename;// VARCHAR(256)
	private int board_delete;// TINYINT NOT NULL DEFAULT 0
	private long board_recommend;// BIGINT NOT NULL DEFAULT 0
	private Integer board_category;// INT // int가 아니라 Integer인 이유는 외래키에 null값을 세팅할 수 있도록

	private String board_nickname;
	private String board_name;

	private String search_type;
	private String search_txt;
	
	// datetime 가공
	public String getBoard_datetime() {
		String datetime = "";
		
		if(this.board_datetime == null) {
			datetime = null;
		} else {
			Date date = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
			
			// 오늘 쓴 글이면 시간만 표시
			if( dateFormat.format(date).equals( dateFormat.format(this.board_datetime) ) ) {
				datetime = timeFormat.format(this.board_datetime);
			} else {
				// 오늘 전에 쓴 글이면 날짜만 표시
				datetime = dateFormat.format(this.board_datetime);
			}
		}
		return datetime;
	}

}