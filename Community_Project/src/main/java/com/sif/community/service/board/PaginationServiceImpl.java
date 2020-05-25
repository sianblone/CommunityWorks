package com.sif.community.service.board;

import org.springframework.stereotype.Service;

import com.sif.community.model.PaginationVO;

@Service(value = "pageSvc")
public class PaginationServiceImpl implements PaginationService {
	
	private int dataPerPage = 10;// 한 페이지에 보여줄 데이터 개수
	private int pageLength = 10;// 한 번에 보여줄 페이지 (1 ~ 10)
	
	// 한 페이지 당 보여줄 데이터(게시물) 수 세팅 메소드
	public void setListPerPage(int dataPerPage) {
		this.dataPerPage = dataPerPage;
	}
	// 하단에 보여줄 페이지 길이 세팅 메소드
	public void setPageCount(int pageLength) {
		this.pageLength = pageLength;
	}

	public PaginationVO makePageInfo(long totalCount, int currPage) {
		
		if(totalCount < 1) {
			PaginationVO pageVO = PaginationVO.builder()
					.startPageNo(1)
					.endPageNo(1)
					.currentPageNo(1)
					.lastPageNo(1)
					.build();
					
			return pageVO ;// 데이터가 없으면 시작페이지, 현재 페이지만 1로 세팅 후 넘겨줌
		}
		
//		lastPageNo(끝 페이지) : (전체 데이터 개수 + 페이지당 보여줄 데이터 개수 - 1) / 페이지당 보여줄 데이터 개수
//		만약 lastPageNo = (totalCount/listPerPage) + 1로 만들면 데이터 13개/페이지당 5개 같은 경우 3페이지가 되지만 데이터 15개/페이지당 5개 같이 나누어 떨어질 때 의미없이 페이지가 +1 된다
//		(기본적으로 프로그래밍 언어에서 나눗셈은 소수점 내림이므로)
//		따라서 totalCount에 listPerPage-1을 추가해준 뒤 나누면 나누어 떨어지는 경우까지 정확히 원하는 값을 얻을 수 있다
		int lastPageNo = (int)((totalCount + dataPerPage - 1) / dataPerPage);
		
//		실제 마지막 페이지(ex:10)보다 큰 값(ex:12)을 현재 페이지로 전달받았다면, 마지막 페이지(10)를 현재 페이지(10)로 만들기
		if(currPage > lastPageNo) currPage = lastPageNo;
//		1 미만의 값을 현재 페이지로 전달받았다면 현재 페이지를 1로 만들기
		if(currPage < 1) currPage = 1;
		
//		현재 페이지(currPageNo)가 3이면 1 ~ 5, 10이면 8 ~ 12까지 이런 식으로 현재페이지를 한 가운데로 만들기 위한 설정
		int startPageNo = 1;
//		새로운 시작페이지 : 현재 페이지(8) - 페이지 길이 절반(5) = 새로운 시작페이지(3)
		int newStartPage = currPage - pageLength / 2;
//		새로운 시작페이지가 음수면 시작페이지 1로 고정, 양수일 때만 시작페이지 바꿔주기
		if (newStartPage > 0) {
			startPageNo = newStartPage;
		}
		
//		마지막 페이지 : 시작페이지(3) + 페이지 길이(10) - 1 = 마지막 페이지(12)
		int endPageNo = startPageNo + pageLength - 1;
//		현재 페이지를 기준으로 계산한 마지막 페이지가 끝페이지보다 커지는 경우 : 마지막 페이지는 끝페이지로 고정
		if(endPageNo > lastPageNo) endPageNo = lastPageNo;
		
//		이전 페이지 : 현재 페이지가 2 이상인 경우 = 현재 페이지 - 1
		int prePageNo = 1;
		if(currPage > 1) prePageNo = currPage - 1;
		
//		다음 페이지 : 현재 페이지가 끝페이지보다 작은 경우 = 현재 페이지 + 1
		int nextPageNo = lastPageNo;
		if(currPage < lastPageNo) {
			nextPageNo = currPage + 1;
		}
		
//		끝페이지가 보이는 시점 시작페이지 처리하기
//		끝페이지:35, 31 32 33 34 35에서 34 클릭 시
//		32 33 34 35에서 시작페이지 31로 만들기
//		마지막페이지(35)-시작페이지(32) + 1 = (4) < 페이지길이(5)일때
		if(endPageNo - startPageNo + 1 < pageLength) {
//			시작페이지 = 끝페이지(35) - 페이지길이(5) + 1 = (31)
//			만약 끝페이지(4) - 페이지길이(5) + 1 = (0) 인 경우 <= 0이 되므로 시작페이지는 1로 고정
			startPageNo = lastPageNo - pageLength + 1 > 0 ? lastPageNo - pageLength + 1 : 1; 
		}
		
//		MySQL DB에서 데이터 가져올 값 설정
//		1페이지 선택시 offset:0,limit:10, 2페이지 선택시 offset:10,limit:10, 3페이지 선택시 offset:20,limit:10
		int offset = (currPage - 1) * dataPerPage;
		int limit = dataPerPage;
		
		PaginationVO paginationVO = PaginationVO.builder()
				.totalCount(totalCount)
				.dataPerPage(dataPerPage)
				.pageLength(pageLength)
				.offset(offset)
				.limit(limit)
				
				.firstPageNo(1)
				.lastPageNo(lastPageNo)
				
				.prePageNo(prePageNo)
				.nextPageNo(nextPageNo)
				
				.startPageNo(startPageNo)
				.endPageNo(endPageNo)
				
				.currentPageNo(currPage)
				.build();
		
		return paginationVO;
	}

}
