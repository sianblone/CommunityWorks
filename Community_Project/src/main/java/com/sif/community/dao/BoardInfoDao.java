package com.sif.community.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sif.community.model.BoardInfoVO;

public interface BoardInfoDao {
	// 메인 페이지에서 사용할 메소드
	public List<BoardInfoVO> selectMainPage(@Param("limit_value") int limit_value);
	// checkEnabled에 따라 활성화된 게시판만 가져오거나 비활성화된 게시판도 전부 가져오기
	public List<BoardInfoVO> selectAll(boolean checkEnabled);
	public BoardInfoVO findByBiId(Long bi_id);
	public BoardInfoVO findByOrder(long bi_order);
	public long maxOrderFromBoardInfo();
	
	public int insert(BoardInfoVO boardInfoVO);
	public int update(BoardInfoVO boardInfoVO);
	public int delete(long bi_id);
}
