package com.sif.community.service.board;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sif.community.model.BoardVO;
import com.sif.community.model.PaginationVO;

@Service(value = "boardSvc")
public class BoardServiceImpl implements BoardService {

	@Override
	public long countAll() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<BoardVO> selectAllByPage(String boardName, String searchTxt, PaginationVO pageDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(BoardVO boardVO) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	

}
