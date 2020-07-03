package com.sif.community.service.board;

import org.springframework.stereotype.Service;

import com.sif.community.model.PaginationVO;
import com.sif.community.service.board.itf.PaginationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service(value = "pageSvc")
public class PaginationServiceImpl implements PaginationService {
	
	private int dataPerPage = 10;// 한 페이지에 보여줄 데이터(로우) 개수
	private int pageRange = 10;// 페이지 하단에 보여줄 페이지 개수(범위)
	
	// 한 페이지에 보여줄 데이터(로우) 개수 세팅 메소드
	public void setDataPerPage(int dataPerPage) {
		this.dataPerPage = dataPerPage;
	}
	// 페이지 하단에 보여줄 페이지 개수(범위) 세팅 메소드
	public void setPageRange(int pageRange) {
		this.pageRange = pageRange;
	}
	
	// 가운데 정렬 방식 페이지네이션
	// 처음 < ··· 3 4 [5] 6 7 ··· > 끝
	// 처음 < ··· 5 6 [7] 8 9 ··· > 끝 
	public PaginationVO makePageInfoMiddle(long dataCount, int pageNo, boolean isReversePagination) {
		PaginationVO paginationVO = this.makePageInfo(dataCount, pageNo, isReversePagination, true);
		return paginationVO;
	}
	
	// 일반 방식 페이지네이션
	// 처음 < ··· 1 [2] 3 4 5 ··· > 끝
	// 처음 < ··· 6 7 8 9 [10] ··· > 끝
	public PaginationVO makePageInfo(long dataCount, int pageNo, boolean isReversePagination, boolean isMiddlePagination) {
		// 데이터가 없으면 전체 페이지 수, 범위의 시작 페이지, 범위의 끝 페이지, 현재 페이지만 1로 세팅 후 return
		if(dataCount < 1) {
			PaginationVO pageVO = PaginationVO.builder()
					.pageCount(1)
					.startPageNo(1)
					.endPageNo(1)
					.pageNo(1)
					.build();
					
			return pageVO ;// 데이터가 없으면 전체 페이지 수, 범위의 시작 페이지, 범위의 끝 페이지, 현재 페이지만 1로 세팅 후 return
		}
		
//		pageCount(전체 페이지 수) : (전체 데이터 개수 + 한 페이지에 보여줄 데이터 개수 - 1) / 한 페이지에 보여줄 데이터 개수
//		만약 pageCount = (dataCount/dataPerPage)로 만들면 데이터 15개/페이지당 5개 같이 나누어 떨어질 때는 3페이지가 되지만
//		데이터 13개/페이지당 5개 같은 경우 2페이지가 된다 (기본적으로 프로그래밍 언어에서 나눗셈은 소수점 내림이기 때문)
//		따라서 dataCount에 dataPerPage-1을 추가해준 뒤 나누면, 나누어 떨어지는 경우는 항상 내림이 되고 소수점이 남는 경우는 페이지가 +1이 되어 정확히 원하는 값을 얻을 수 있게 된다
//		올림 메소드인 Math.ceil 메소드를 이용해도 된다
		int pageCount = (int)((dataCount + dataPerPage - 1) / dataPerPage);
		
//		전체 페이지 수(Ex. 10)보다 큰 페이지(Ex. 12)를 현재 페이지로 전달받았다면, 현재 페이지를 전체 페이지 수(10)로 만들기
		if(pageNo > pageCount) pageNo = pageCount;
//		1 미만의 값을 현재 페이지로 전달받았다면 현재 페이지를 1로 만들기
		if(pageNo < 1) pageNo = 1;
		
		int startPageNo = 1; // 범위의 시작 페이지 선언 및 초기화
		int newStartPageNo = 0;
		
//		가운데 정렬인 경우
		if(isMiddlePagination) {
//			현재 페이지(pageNo)가 3이면 범위는 1 2 [3] 4 5, 현재 페이지가 10이면 범위는 8 9 [10] 11 12 형식으로 현재 페이지를 가운데로 정렬하기 위한 설정
//			새로운 범위 시작 페이지 : Ex. 현재 페이지(8) - 페이지 길이 절반(5/2) = 새로운 시작페이지(6) -> 6 7 [8] 9 10 
			newStartPageNo = pageNo - pageRange / 2;
		} else {
//			현재 페이지(pageNo)가 3이면 범위는 1 2 [3] 4 5, 현재 페이지가 7이면 범위는 6 [7] 8 9 10 형식으로 보여주기 위한 설정
//			새로운 범위 시작 페이지 : Ex. (현재 페이지(3) - 1) / 페이지 범위(5) * 페이지 범위 + 1
			newStartPageNo = (pageNo - 1) / pageRange * pageRange + 1;
		}
//		새로운 범위 시작 페이지가 음수가 나온다면 1로 고정, 양수일 때만 바꿔주기
		if (newStartPageNo > 0) startPageNo = newStartPageNo;
		
//		범위의 마지막 페이지 : Ex. 범위의 시작 페이지(11) + 페이지 범위(5) - 1 = (15)
		int endPageNo = startPageNo + pageRange - 1;
//		현재 페이지를 기준으로 계산한 범위의 마지막 페이지가 전체 페이지 수보다 커지는 경우 전체 페이지 수로 고정
		if(endPageNo > pageCount) endPageNo = pageCount;
		
		int prevPageNo = 0;
		int nextPageNo = 0;
		
//		가운데 정렬인 경우
		if(isMiddlePagination) {
//			이전 페이지 : 현재 페이지 - 페이지 범위
			prevPageNo = pageNo - pageRange;
//			다음 페이지 : 현재 페이지 + 페이지 범위
			nextPageNo = pageNo + pageRange;
		} else {
//			이전 페이지 : 범위의 시작 페이지 - 페이지 범위
			prevPageNo = startPageNo - pageRange;
//			다음 페이지 : 범위의 마지막 페이지 + 1
			nextPageNo = endPageNo + 1;
		}
//		이전 페이지가 1 미만이면 1로 만들기
		if(prevPageNo < 1) prevPageNo = 1;
//			다음 페이지가 전체 페이지 수보다 커지면 전체 페이지 수로 고정
		if(nextPageNo > pageCount) nextPageNo = pageCount;
		
//		가운데 정렬인 경우
		if(isMiddlePagination) {
//			끝 페이지(=전체 페이지 수)가 보이는 시점에 범위의 시작 페이지 처리하기
//			끝 페이지가 35일 때, 31 32 33 34 35에서 34 클릭 시 32 33 34 35가 보이게 된다
//			이 때 범위의 시작 페이지를 32가 아닌 31로 만들기
//			범위의 마지막 페이지(35)-범위의 시작 페이지(32) + 1 = (4) < 페이지 길이(5)일 때
			if(endPageNo - startPageNo + 1 < pageRange) {
//				전체 페이지 수가 페이지 범위보다 적은 경우 : 끝 페이지(4) - 페이지 길이(5) + 1 = 0
//				범위 시작 페이지가 0 이하가 되므로 1로 고정
				if(pageCount - pageRange + 1 <= 0) {
					startPageNo = 1;
				} else {
//					범위의 시작 페이지 = 끝 페이지(35) - 페이지길이(5) + 1 = (31)
					startPageNo = pageCount - pageRange + 1;
				}
			}
		}
		
//		MySQL DB에서 데이터 가져올 값 설정
//		1페이지 선택시 offset=0,limit=10, 2페이지 선택시 offset=10,limit=10, 3페이지 선택시 offset=20,limit=10
		int offset = 0;
		if(isReversePagination) {
			offset = (pageCount - pageNo) * dataPerPage;
		} else {
			offset = (pageNo - 1) * dataPerPage;
		}
		int limit = dataPerPage;
		
		PaginationVO paginationVO = PaginationVO.builder()
				.dataCount(dataCount)
				.pageCount(pageCount)
				
				.offset(offset)
				.limit(limit)
				
				.dataPerPage(dataPerPage)
				.pageRange(pageRange)
				
				.startPageNo(startPageNo)
				.endPageNo(endPageNo)
				
				.prevPageNo(prevPageNo)
				.nextPageNo(nextPageNo)
				
				.pageNo(pageNo)
				.build();
		
		return paginationVO;
	}

}
