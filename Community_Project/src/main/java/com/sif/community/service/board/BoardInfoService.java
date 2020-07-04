package com.sif.community.service.board;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sif.community.dao.BoardInfoDao;
import com.sif.community.model.BoardInfoVO;
import com.sif.community.model.PaginationDTO;
import com.sif.community.service.board.itf.PaginationService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service(value = "boardInfoSvc")
public class BoardInfoService {
	
	private final BoardInfoDao boardInfoDao;
	private final PaginationService pageSvc;
	
	// 관리자 페이지에서 사용할 메소드
	public List<BoardInfoVO> selectAll() {
		return boardInfoDao.selectAll();
	}
	
	// 메인 페이지에서 사용할 메소드
	public List<BoardInfoVO> selectMainPage() {
		int limit_value = 5;
		PaginationDTO pageDTO = pageSvc.findByBoardInfo(null, "main");
		if(pageDTO != null) limit_value = pageDTO.getPage_data_cnt();
		return boardInfoDao.selectMainPage(limit_value);
	}
	
	public BoardInfoVO findByBiId(long bi_id) {
		return boardInfoDao.findByBiId(bi_id);
	}

	public int insert(BoardInfoVO boardInfoVO) {
		return boardInfoDao.insert(boardInfoVO);
	}
	
	public int update(BoardInfoVO boardInfoVO) {
		return boardInfoDao.update(boardInfoVO);
	}
	
	public int delete(long bi_id) {
		return boardInfoDao.delete(bi_id);
	}
	
}
