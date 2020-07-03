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
public class PaginationVO {
	
	private long dataCount;// 전체 데이터 개수
	private int pageCount;// 전체 페이지 수
	
	private int offset;// 시작 레코드
	private int limit;// 끝 레코드
	
	private int dataPerPage;// 한 페이지에 보여줄 데이터(로우) 개수
	private int pageRange;// 페이지 하단에 보여줄 페이지 범위
	
	private int startPageNo;// 범위의 시작 페이지. 현재 페이지 하단에 5~10페이지를 보여준다고 할 때 5
	private int endPageNo;// 범위의 끝 페이지. 현재 페이지 하단에 5~10페이지를 보여준다고 할 때 10
	
	private int prevPageNo;// 이전 페이지 번호
	private int nextPageNo;// 다음 페이지 번호
	
	private int pageNo;// 현재 페이지 번호

}