package com.sif.community.service.board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	
	private List<CommentVO> findByBoardNoRepl(CommentVO cmtVO, int depth) {
		List<CommentVO> retList = new ArrayList<CommentVO>();
		if(depth > 0) {
			String cmt_content = "&nbsp;";
			for(int i = 0 ; i < depth; i++) {
				cmt_content += "re:";
			}
			cmt_content += cmtVO.getCmt_content();
			cmtVO.setCmt_content(cmt_content);
		}
		retList.add(cmtVO);
		
		List<CommentVO> tempList = cmtDao.findByPNo(cmtVO.getCmt_no());
		if(tempList.size() < 1) return retList;
		
		for(CommentVO vo : tempList) {
			retList.addAll(this.findByBoardNoRepl(vo, depth + 1));
		}
		return retList;
	}

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
			retList.addAll(this.findByBoardNoRepl(vo,0));
		}
		return retList;
	}

	@Override
	public List<CommentVO> findByPNo(long cmt_p_no) {
		List<CommentVO> cmtList = cmtDao.findByPNo(cmt_p_no);
		return cmtList;
	}

	@Override
	public int insert(CommentVO commentVO) {
		if(commentVO.getCmt_no() > 0) {
			int ret = cmtDao.update(commentVO);
			return ret;
		} else {
			// 작성일자를 현재 날짜및 시간으로 세팅
			LocalDateTime ldt = LocalDateTime.now();
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			DateTimeFormatter dt = DateTimeFormatter.ofPattern("HH:mm:ss");
			commentVO.setCmt_date(ldt.format(df).toString());
			commentVO.setCmt_time(ldt.format(dt).toString());
			int ret = cmtDao.insert(commentVO);
			return ret;
		}
	}

	@Override
	public int update(CommentVO commentVO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(long cmt_no) {
		int ret = cmtDao.delete(cmt_no);
		return ret;
	}

	
	
}
