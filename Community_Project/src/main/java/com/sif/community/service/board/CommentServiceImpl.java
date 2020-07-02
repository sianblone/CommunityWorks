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
import com.sif.community.model.BoardVO;
import com.sif.community.model.CommentVO;
import com.sif.community.model.PaginationVO;
import com.sif.community.service.board.itf.CommentService;
import com.sif.util.SpringSecurityUtil;

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
		// 현재 사용자가 관리자 권한을 가지고 있을 때 delete = 1인 게시물도 count하기
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
		// 현재 사용자가 관리자 권한을 가지고 있을 때 delete = 1인 게시물도 리스트에 보여주기
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
		
		// DB 입력 성공 여부에 따라 컨트롤러로 리턴할 값
		int result = 0;
		
		long cmt_no = commentVO.getCmt_no();
		
		// 1. commentVO에 No가 있는 경우(=> 댓글 수정)
		if(cmt_no != 0) {
			CommentVO dbCommentVO = this.findByCmtNo(cmt_no);
			if(dbCommentVO == null) {
				// 1-1. 댓글번호로 검색한 데이터가 DB에 없는 경우 -100 리턴
				return -100;
			}
			
			// 1-2. 게시글번호로 검색한 데이터가 DB에 있는 경우
			// 로그인한 사용자가 댓글 작성자거나 관리자면 댓글 수정
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if(auth.getName().equals(commentVO.getCmt_writer()) || auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
				dbCommentVO.setCmt_content(commentVO.getCmt_content());
				result = cmtDao.update(dbCommentVO);
			}
		} else if(commentVO.getCmt_p_no() != 0) {
			// 2. 대댓글인 경우(컨트롤러에서 넘겨준 commentVO에 cmt_no가 없고 cmt_p_no가 있는 경우)
			// 대댓글인 경우는 GET URL 쿼리에 cmt_p_no가 있기 때문에 commentVO에 값이 세팅되어 있다
			
			// 2-1. 부모댓글의 cmt_group 가져와서 group 세팅하기
			CommentVO parentCommentVO = this.findByCmtNo(commentVO.getCmt_p_no());
			long cmt_group = parentCommentVO.getCmt_group();
			commentVO.setCmt_group(cmt_group);
			// 2-2. 부모댓글의 cmt_group 가져와서 order를 maxOrder + 1로 세팅하기
			int maxOrder = cmtDao.maxOrderByCmtGroup(cmt_group);
			commentVO.setCmt_order(maxOrder + 1);
			// 2-3. 부모댓글의 cmt_depth 가져와서 depth를 +1로 세팅하기
			commentVO.setCmt_depth(parentCommentVO.getCmt_depth() + 1);
			// 2-4. commentVO에 작성자, 날짜+시간 세팅 후 INSERT
			saveSetting(commentVO);
			result = cmtDao.insert(commentVO);
		} else {
			// 3. 신규작성 댓글인 경우(컨트롤러에서 넘겨준 commentVO에 cmt_no와 cmt_p_no가 없는 경우)
			// 3-1. commentVO에 작성자, 날짜+시간 세팅 후 INSERT
			saveSetting(commentVO);
			result = cmtDao.insert(commentVO);
			// 3-2. 방금 작성한 댓글을 다시 DB에서 가져와서 댓글 그룹, 댓글 순서, 댓글 깊이 업데이트
			// commentVO에는 insert 후 새로운 cmt_no 값이 저장되어 있다(MyBatis의 selectKey 이용)
			CommentVO insertedCommentVO = cmtDao.findByCmtNo(commentVO.getCmt_no());
			// (1) 댓글 그룹 = 작성한 댓글 번호
			insertedCommentVO.setCmt_group(insertedCommentVO.getCmt_no());
			// (2) 댓글 순서 = 0(업데이트 할 필요 없음)
			// (3) 댓글 깊이 = 0(업데이트 할 필요 없음)
			result = cmtDao.update(insertedCommentVO);
		}
		
		return result;
	}
	
	protected CommentVO saveSetting(CommentVO commentVO) {
		// 로그인한 경우 작성자 이름을 로그인한 사용자 이름으로 세팅
		boolean isLoggedIn = SpringSecurityUtil.isLoggedIn();
		if(isLoggedIn) {
			commentVO.setCmt_writer(SecurityContextHolder.getContext().getAuthentication().getName());
		}
		
		// 날짜+시간 세팅
		LocalDateTime ldt = LocalDateTime.now();
		Date date = Date.from( ldt.atZone( ZoneId.systemDefault()).toInstant() );
		commentVO.setCmt_datetime(date);
		
		return commentVO;
	}

	@Override
	public int delete(long cmt_no) {
		int result = 0;
		
		// DB에 댓글번호로 검색한 데이터가 있으면(이미 있는 댓글이면) 삭제하기
		CommentVO commentVO = this.findByCmtNo(cmt_no);
		// DB에 cmt_no로 검색한 데이터가 없으면 에러페이지 보여주기
		if(commentVO == null) {
			return -100;
		}
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// 현재 삭제 버튼을 누른 사용자(로그인한 사용자)가 게시글 작성자가 아니고 관리자도 아니면 에러 페이지 보여주기
		if(!auth.getName().equals(commentVO.getCmt_writer()) && !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
			return -200;
		}
		// 로그인한 사용자가 게시글 작성자거나 관리자면 글 삭제
		commentVO.setCmt_delete(1);
		cmtDao.update_delete(commentVO);
		
		return result;
	}
	
	
}
