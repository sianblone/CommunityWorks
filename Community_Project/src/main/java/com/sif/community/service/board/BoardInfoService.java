package com.sif.community.service.board;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sif.community.dao.BoardInfoDao;
import com.sif.community.model.BoardInfoVO;
import com.sif.community.model.PaginationDTO;
import com.sif.community.service.board.itf.PaginationService;
import com.sif.util.ProjectUtil;
import com.sif.util.SpSec;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service(value = "boardInfoSvc")
public class BoardInfoService {
	
	private final BoardInfoDao boardInfoDao;
	private final PaginationService pageSvc;
	
	// checkEnabled에 따라 활성화된 게시판만 가져오거나 비활성화된 게시판도 전부 가져오기
	public List<BoardInfoVO> selectAll(boolean checkEnabled) {
		// checkEnabled가 켜져있더라도 관리자는 비활성화된 게시판도 전부 볼 수 있도록 설정
		if(SpSec.isAdmin()) checkEnabled = false;
		return boardInfoDao.selectAll(checkEnabled);
	}
	
	// 메인 페이지에서 사용할 메소드
	public List<BoardInfoVO> selectMainPage() {
		int limit_value = 5;
		PaginationDTO pageDTO = pageSvc.findByBiId(null, ProjectUtil.PAGE_LOCATION_MAIN);
		if(pageDTO != null) limit_value = pageDTO.getPage_data_cnt();
		return boardInfoDao.selectMainPage(limit_value);
	}
	
	public BoardInfoVO findByBiId(Long bi_id) {
		BoardInfoVO boardInfoVO = boardInfoDao.findByBiId(bi_id);
		
		if(boardInfoVO == null) boardInfoVO = new BoardInfoVO();
		
		// 관리자 페이지 게시판 설정에서 사용할 변수들 세팅
		if(boardInfoVO.getData_cnt_board() == 0) boardInfoVO.setData_cnt_board(10);
		if(boardInfoVO.getData_cnt_comment() == 0) boardInfoVO.setData_cnt_comment(10);
		if(boardInfoVO.getPage_range_board() == 0) boardInfoVO.setPage_range_board(10);
		if(boardInfoVO.getPage_range_comment() == 0) boardInfoVO.setPage_range_comment(10);
		return boardInfoVO;
	}
	
	public long maxOrderFromBoardInfo() {
		return boardInfoDao.maxOrderFromBoardInfo();
	}
	
	@Transactional
	public int insert(BoardInfoVO boardInfoVO) {
		// 로그인한 사용자가 관리자가 아니면 진행하지 않음
		if(!SpSec.isAdmin()) return 0;
		
		long maxOrder = this.maxOrderFromBoardInfo();
		boardInfoVO.setBi_enabled(true);
		boardInfoVO.setBi_order(maxOrder + 1);
		return boardInfoDao.insert(boardInfoVO);
	}
	
	public int update(BoardInfoVO boardInfoVO) {
		return boardInfoDao.update(boardInfoVO);
	}
	
	@Transactional
	public int changeOrder(long bi_id, long order) {
		int result = 0;
		// 1. 현재 게시판이 DB에 있는지 확인
		BoardInfoVO boardInfoVO = this.findByBiId(bi_id);
		if(boardInfoVO == null) return -100;
		long nowOrder = boardInfoVO.getBi_order();
		
		// 2. 다음 게시판이 DB에 있는지 확인
		BoardInfoVO nextBoardInfoVO = null;
		if(order == -1) {
			// 2-1. ▲ 클릭 시(-1)
			nextBoardInfoVO = this.findByOrder(nowOrder - 1);
			if(nextBoardInfoVO == null) return -100;
		} else if (order == 1) {
			// 2-2. ▼ 클릭 시(+1)
			nextBoardInfoVO = this.findByOrder(nowOrder + 1);
			if(nextBoardInfoVO == null) return -100;
		}
		
		// 3. 둘 다 있으면 순서 변경
		long nextOrder = nextBoardInfoVO.getBi_order();
		
		boardInfoVO.setBi_order(nextOrder);
		nextBoardInfoVO.setBi_order(nowOrder);
		
		result = this.update(boardInfoVO);
		if(result > 0) result = this.update(nextBoardInfoVO);
		
		return result;
	}
	
	private BoardInfoVO findByOrder(long bi_order) {
		return boardInfoDao.findByOrder(bi_order);
	}

	public int delete(long bi_id) {
		return boardInfoDao.delete(bi_id);
	}
	
}