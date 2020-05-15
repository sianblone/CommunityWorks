package com.sif.community.service.board;

import org.springframework.stereotype.Service;

import com.sif.community.model.PaginationVO;

@Service(value = "pageSvc")
public class PaginationServiceImpl implements PaginationService {
	
	private int listPerPage = 10;// 한 페이지에 보여줄 데이터 개수
	private int pageCount = 10;// 한 번에 보여줄 페이지 (1 ~ 10)

	public void setListPerPage(int listPerPage) {
		this.listPerPage = listPerPage;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public PaginationVO makePageInfo(long totalCount, int currPageNo) {
		
		if(totalCount < 1) {
			return null;// 데이터가 없으면 아무 일도 하지 않음
		}
		
//		lastPageNo(끝 페이지) : (전체 데이터 개수 + 페이지당 보여줄 데이터 개수 - 1) / 페이지당 보여줄 데이터 개수
//		만약 lastPageNo = (totalCount/listPerPage) + 1로 만들면 데이터 13개/페이지당 5개 같은 경우 3페이지가 되지만 데이터 15개/페이지당 5개 같이 나누어 떨어질 때 의미없이 페이지가 +1 된다
//		(기본적으로 프로그래밍 언어에서 나눗셈은 소수점 내림이므로)
//		따라서 totalCount에 listPerPage-1을 추가해준 뒤 나누면 나누어 떨어지는 경우까지 정확히 원하는 값을 얻을 수 있다
		int lastPageNo = (int)((totalCount + listPerPage - 1) / listPerPage);
		
//		값으로 전달받은 현재 페이지가 실제 마지막 페이지보다 크면
		if(currPageNo > lastPageNo) currPageNo = lastPageNo;
//		값으로 전달받은 현재 페이지가 1 미만이면
		if(currPageNo < 1) currPageNo = 1;
		
//		값으로 전달받은 현재 페이지(currPageNo)가 3이면 1 ~ 5, 10이면 8 ~ 12까지 이런 식으로 보여주기 위한 설정
//		(현재 페이지가 페이지 당 보여줄 데이터 개수보다 큰 경우)
		int startPageNo = 1;
		int midPage = currPageNo - pageCount / 2;
		if (midPage > 0) {			
			startPageNo = midPage;
		}
		
		int endPageNo = startPageNo + pageCount - 1;
		if(endPageNo > lastPageNo) endPageNo = lastPageNo;
		
		int prePageNo = 1;
		if(currPageNo > 1) prePageNo = currPageNo - 1;
		
		int nextPageNo = lastPageNo;
		if(currPageNo < lastPageNo) {
			nextPageNo = currPageNo + 1;
		}
		
		if(endPageNo - startPageNo + 1 < pageCount) {
			startPageNo = lastPageNo - pageCount + 1 > 0 ? lastPageNo - pageCount + 1 : 1; 
		}
		
		// MySQL DB에서 데이터 가져올 값 설정
		// 1페이지 선택시 offset:0,limit:10, 2페이지 선택시 offset:10,limit:10, 3페이지 선택시 offset:20,limit:10
		int offset = (currPageNo - 1) * listPerPage;
		int limit = listPerPage;
		
		PaginationVO paginationDTO = PaginationVO.builder()
				.totalCount(totalCount)
				.listPerPage(listPerPage)
				.pageCount(pageCount)
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
		
		return paginationDTO;
	}

}
