package com.sif.community.service.board;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sif.community.dao.CommentDao;
import com.sif.community.model.CommentVO;
import com.sif.community.model.PaginationVO;
import com.sif.community.service.board.itf.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service(value = "cmtSvc")
public class CommentServiceImpl implements CommentService {
	
	private final CommentDao cmtDao;

	@Override
	public long countAll(CommentVO commentVO) {
		long totalCount = 0;
		
		boolean isAdmin = false;
		// 현재 사용자가 관리자 권한일 때 delete = 1인 게시물도 count하기
		if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			isAdmin = true;
		}
		totalCount = cmtDao.countAll(commentVO, isAdmin);
		
		return totalCount;
	}

	@Override
	public List<CommentVO> selectAllByPage(CommentVO commentVO, PaginationVO pageVO) {
		List<CommentVO> cmtList = null;
		
		boolean isAdmin = false;
		// 현재 사용자가 관리자 권한일 때 delete = 1인 게시물도 리스트에 보여주기
		if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			isAdmin = true;
		}
		cmtList = cmtDao.selectAllByPage(commentVO, pageVO, isAdmin);
		
		return cmtList;
	}

	@Override
	public CommentVO findByCmtNo(long comment_no) {
		return cmtDao.findByCmtNo(comment_no);
	}
	
	@Transactional
	@Override
	public int save(CommentVO commentVO) {
		
		String render = "";
		int ret = 0;// MyBatis selectKey로 받아올 auto_increment 값
		
		if(commentVO.getCmt_no() != 0) {
			// 댓글 수정인 경우(컨트롤러에서 넘겨준 cmtVO에 cmt_no가 있는 경우)
			// 1. DB에 게시글번호로 검색한 데이터가 있는지 확인
			CommentVO dbCommentVO = this.findByCmtNo(commentVO.getCmt_no());
			if(dbCommentVO != null) {
				// 2. 로그인한 사용자와 게시글 작성자가 같거나 로그인한 사용자가 관리자면 글 수정
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				if(auth.getName().equals(commentVO.getCmt_writer()) || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
					dbCommentVO.setCmt_content(commentVO.getCmt_content());
					ret = cmtDao.update(dbCommentVO);
				}
			}
			
		} else if(commentVO.getCmt_p_no() != 0) {
			// 대댓글인 경우(컨트롤러에서 넘겨준 commentVO에 cmt_no가 없고 cmt_p_no가 있는 경우)
			// 대댓글인 경우는 GET 쿼리에 cmt_p_no가 있기 때문에 commentVO에 세팅되어 있다
			
			// 1. 부모댓글의 cmt_group 가져와서 group 세팅하기
			CommentVO parentCommentVO = this.findByCmtNo(commentVO.getCmt_p_no());
			long cmt_group = parentCommentVO.getCmt_group();
			commentVO.setCmt_group(cmt_group);
			// 2. 부모댓글의 cmt_group 가져와서 order를 maxOrder + 1로 세팅하기
			int maxOrder = cmtDao.maxOrderByCmtGroup(cmt_group);
			commentVO.setCmt_order(maxOrder + 1);
			// 3. 부모댓글의 cmt_depth 가져와서 depth를 +1로 세팅하기
			commentVO.setCmt_depth(parentCommentVO.getCmt_depth() + 1);
			// 작성자, 날짜+시간 세팅 후 INSERT
			saveSetting(commentVO);
			ret = cmtDao.insert(commentVO);
		} else {
			// 신규작성 댓글인 경우(컨트롤러에서 넘겨준 commentVO에 cmt_no와 cmt_p_no가 없는 경우)
			// 작성자, 날짜+시간 세팅 후 INSERT
			saveSetting(commentVO);
			log.debug("save commentVO : {}", commentVO);
			ret = cmtDao.insert(commentVO);
			log.debug("selectKey : {}", commentVO.getCmt_no());
			// 방금 작성한 댓글을 다시 DB에서 가져와서 댓글 그룹, 댓글 순서, 댓글 깊이 업데이트
			CommentVO insertedCommentVO = cmtDao.findByCmtNo(commentVO.getCmt_no());
			// 1. 댓글 그룹 = 작성한 댓글 번호
			insertedCommentVO.setCmt_group(insertedCommentVO.getCmt_no());
			// 2. 댓글 순서 = 0(업데이트 할 필요 없음)
			// 3. 댓글 깊이 = 0(업데이트 할 필요 없음)
			ret = cmtDao.update(insertedCommentVO);
		}
		
		return ret;
	}
	
	protected CommentVO saveSetting(CommentVO commentVO) {
		// 작성자 세팅
		// 로그인한 경우 작성자 = 로그인한 사용자 이름으로 세팅(모든 권한)
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.isAuthenticated()) {
			commentVO.setCmt_writer(auth.getName());
		}
		
		// 날짜+시간 세팅
		LocalDateTime ldt = LocalDateTime.now();
		Date date = Date.from( ldt.atZone( ZoneId.systemDefault()).toInstant() );
		commentVO.setCmt_datetime(date);
		
		return commentVO;
	}

	@Override
	public String delete(long cmt_no, Integer currPage) {
		String render = "";
		CommentVO commentVO = this.findByCmtNo(cmt_no);
		// DB에 댓글번호로 검색한 데이터가 있으면(이미 있는 댓글이면) 삭제하기
		if(commentVO != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			// 로그인한 사용자와 게시글 작성자가 같거나 로그인한 사용자가 관리자면 글 삭제하기
			if(auth.getName().equals(commentVO.getCmt_writer()) || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
				commentVO.setCmt_delete(1);
				cmtDao.update_delete(commentVO);
				
				long cmt_board_no = commentVO.getCmt_board_no();
				render = "redirect:/comment/list?cmt_board_no=" + cmt_board_no;
				if(currPage != null) render += "&currPage=" + currPage;
			} else {
				// 현재 로그인한 사용자와 게시글 작성자가 다르거나 관리자가 아니면 삭제 불가, 에러페이지 보여주기
				render = "board/error";
			}
		} else {
			// DB에 cmt_no로 검색한 데이터가 없으면 에러페이지 보여주기
			render = "board/error";
		}
		
		return render;
	}
	
	
}
