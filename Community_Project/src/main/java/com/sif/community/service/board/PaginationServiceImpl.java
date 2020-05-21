package com.sif.community.service.board;

import org.springframework.stereotype.Service;

import com.sif.community.model.PaginationVO;

@Service(value = "pageSvc")
public class PaginationServiceImpl implements PaginationService {
	
	private int dataPerPage = 10;// 한 페이지에 보여줄 데이터 개수
	private int pageLength = 10;// 한 번에 보여줄 페이지 (1 ~ 10)

	public void setListPerPage(int dataPerPage) {
		this.dataPerPage = dataPerPage;
	}

	public void setPageCount(int pageLength) {
		this.pageLength = pageLength;
	}

	public PaginationVO makePageInfo(long totalCount, int currPageNo) {
		
		if(totalCount < 1) {
			return new PaginationVO();// 데이터가 없으면 아무 일도 하지 않음
		}
		
//		lastPageNo(끝 페이지) : (전체 데이터 개수 + 페이지당 보여줄 데이터 개수 - 1) / 페이지당 보여줄 데이터 개수
//		만약 lastPageNo = (totalCount/listPerPage) + 1로 만들면 데이터 13개/페이지당 5개 같은 경우 3페이지가 되지만 데이터 15개/페이지당 5개 같이 나누어 떨어질 때 의미없이 페이지가 +1 된다
//		(기본적으로 프로그래밍 언어에서 나눗셈은 소수점 내림이므로)
//		따라서 totalCount에 listPerPage-1을 추가해준 뒤 나누면 나누어 떨어지는 경우까지 정확히 원하는 값을 얻을 수 있다
		int lastPageNo = (int)((totalCount + dataPerPage - 1) / dataPerPage);
		
//		실제 마지막 페이지(ex:10)보다 큰 값(ex:12)을 현재 페이지로 전달받았다면, 마지막 페이지(10)를 현재 페이지(10)로 만들기
		if(currPageNo > lastPageNo) currPageNo = lastPageNo;
//		1 미만의 값을 현재 페이지로 전달받았다면 현재 페이지를 1로 만들기
		if(currPageNo < 1) currPageNo = 1;
		
//		현재 페이지(currPageNo)가 3이면 1 ~ 5, 10이면 8 ~ 12까지 이런 식으로 현재페이지를 한 가운데로 만들기 위한 설정
		int startPageNo = 1;
//		가운데 페이지 = 현재페이지(10)에서 페이지길이 절반(*내림)을 뺀 값.
//		현재 페이지 10, 페이지길이 10이면 가운데 페이지는 5번 / 현재 페이지 11, 페이지길이 10이면 가운데 페이지는 6번
		int midPage = currPageNo - pageLength / 2;
//		만약 현재 페이지 3, 페이지 길이 10인 경우처럼 페이지길이 절반보다 현재 페이지가 작다면, 가운데 페이지는 페이
		if (midPage > 0) {
			startPageNo = midPage;
		}
		
		int endPageNo = startPageNo + pageLength - 1;
		if(endPageNo > lastPageNo) endPageNo = lastPageNo;
		
		int prePageNo = 1;
		if(currPageNo > 1) prePageNo = currPageNo - 1;
		
		int nextPageNo = lastPageNo;
		if(currPageNo < lastPageNo) {
			nextPageNo = currPageNo + 1;
		}
		
		if(endPageNo - startPageNo + 1 < pageLength) {
			startPageNo = lastPageNo - pageLength + 1 > 0 ? lastPageNo - pageLength + 1 : 1; 
		}
		
		// MySQL DB에서 데이터 가져올 값 설정
		// 1페이지 선택시 offset:0,limit:10, 2페이지 선택시 offset:10,limit:10, 3페이지 선택시 offset:20,limit:10
		int offset = (currPageNo - 1) * dataPerPage;
		int limit = dataPerPage;
		
		PaginationVO paginationVO = PaginationVO.builder()
				.totalCount(totalCount)
				.listPerPage(dataPerPage)
				.pageCount(pageLength)
				.offset(offset)
				.limit(limit)
				
				.firstPageNo(1)
				.lastPageNo(lastPageNo)
				
				.prePageNo(prePageNo)
				.nextPageNo(nextPageNo)
				
				.startPageNo(startPageNo)
				.endPageNo(endPageNo)
				
				.currentPageNo(currPageNo)
				.build();
		
		return paginationVO;
	}

}
