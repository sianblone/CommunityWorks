package com.sif.community.service.board;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sif.community.dao.CommentDao;
import com.sif.community.model.CommentVO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	
	private final CommentDao cmtDao;
	
//	private List<CommentVO> findByBoardNoRepl(CommentVO cmtVO, int depth) {
//		
//	}

	@Override
	public List<CommentVO> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommentVO findByNo(long cmt_no) {
		CommentVO cmtVO = cmtDao.findByNo(cmt_no);
		return cmtVO;
	}

	@Override
	public List<CommentVO> findByBoardNo(long cmt_board_no) {
		List<CommentVO> cmtList = cmtDao.findByBoardNo(cmt_board_no);
		List<CommentVO> retList = new ArrayList<CommentVO>();
		for(CommentVO vo : cmtList) {
//			retList.addAll(this.findByBoardNoRepl(vo,0));
		}
		return retList;
	}

	@Override
	public List<CommentVO> findByPNo(long cmt_p_no) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(CommentVO commentVO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(CommentVO commentVO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(long cmt_no) {
		// TODO Auto-generated method stub
		return 0;
	}

	
	
}
