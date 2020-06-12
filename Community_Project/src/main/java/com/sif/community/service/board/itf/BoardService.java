package com.sif.community.service.board.itf;

import java.util.List;

import org.springframework.ui.Model;

import com.sif.community.model.BoardInfoVO;
import com.sif.community.model.BoardVO;
import com.sif.community.model.CategoryVO;
import com.sif.community.model.PaginationVO;

public interface BoardService {
	
	public List<BoardVO> selectAllByPage(BoardVO boardVO, PaginationVO pageVO);

	public long countAll(BoardVO boardVO);
	
	public String saveView(BoardVO boardOptionVO, Model model);
	
	public List<CategoryVO> selectCategoryByBoard(BoardVO boardOptionVO);

	public int save(BoardVO boardVO);

	public String delete(long board_no, Integer currPage);

	public BoardVO findByNo(long board_no);
	
	public List<BoardInfoVO> selectAllBoardInfo();
	
	public BoardInfoVO findByBoardInfo(long board_info);

	public String admin(long board_no, Integer currPage, String order);

}
