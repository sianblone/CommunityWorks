package com.sif.community.service.board.itf;

import java.util.List;

import org.springframework.ui.Model;

import com.sif.community.model.BoardInfoVO;
import com.sif.community.model.BoardVO;
import com.sif.community.model.CategoryVO;
import com.sif.community.model.PaginationVO;

public interface BoardService {
	
	public long countAll(BoardVO boardVO);
	public List<BoardVO> selectAllByPage(BoardVO boardVO, PaginationVO pageVO);
	public BoardVO findByBoardNo(long board_no);
	public List<CategoryVO> selectCategoryByBoard(BoardVO boardOptionVO);
	public String saveView(BoardVO boardOptionVO, Model model);
	public List<BoardInfoVO> selectMainPage(int limit);// 메인 페이지에서 사용할 메소드
	public List<BoardInfoVO> selectAllBoardInfo();// 관리자 페이지에서 사용할 메소드
	public BoardInfoVO findByBoardInfo(long board_info);
	
	public int save(BoardVO boardVO);
	public String delete(long board_no, Integer currPage);
	public String admin(long board_no, Integer currPage, String order);
	
}
