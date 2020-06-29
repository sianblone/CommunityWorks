package com.sif.community.service.user;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sif.community.dao.AdminDao;
import com.sif.community.dao.AuthoritiesDao;
import com.sif.community.dao.BoardDao;
import com.sif.community.dao.CategoryDao;
import com.sif.community.dao.UserDao;
import com.sif.community.model.AuthorityVO;
import com.sif.community.model.BoardInfoVO;
import com.sif.community.model.CategoryVO;
import com.sif.community.model.UserDetailsVO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminService {
	
	private final UserDao userDao;
	private final AdminDao adminDao;
	private final AuthoritiesDao authDao;
	private final BoardDao boardDao;
	private final CategoryDao cateDao;
	
	// 관리자가 다른 유저 정보 수정하기
	// 유저 정보 form과 권한 정보 form 값을 가져와서
	// 유저 정보는 유저 테이블에, 권한은 권한 테이블에 저장하기
	@Transactional
	public int update_user_from_admin(UserDetailsVO userVO, String[] arrAuth) {
		int ret = this.valid_update_user_from_admin(userVO);
		// 유효성 검사 통과 실패 시 유효성 검사 결과값 바로 리턴
		if(ret < 0) return ret;
		
		// DB의 유저 정보 불러오기
		UserDetailsVO dbUserVO = userDao.findByUsername(userVO.getUsername());
		
		// 기존의 유저정보에 form에서 입력받은 정보 새로 세팅하기
		// 계정활성여부, 닉네임, 이메일, 핸드폰, 생년, 생월, 생일
		dbUserVO.setEnabled(userVO.isEnabled());
		dbUserVO.setNickname(userVO.getNickname());
		dbUserVO.setEmail(userVO.getEmail());
		dbUserVO.setPhone(userVO.getPhone());
		
		dbUserVO.setYear(userVO.getYear());
		dbUserVO.setMonth(userVO.getMonth());
		dbUserVO.setDay(userVO.getDay());
		
		ret = adminDao.update_user_from_admin(dbUserVO);
		
		// 업데이트 성공 시 권한 테이블 업데이트
		if(ret > 0) {
			List<AuthorityVO> authList = new ArrayList<AuthorityVO>();
			for(String auth : arrAuth) {
				//input에서 받은 auth 값이 비어있으면(="") 무시함 
				if(!auth.isEmpty()) {
					AuthorityVO authVO = AuthorityVO.builder()
										.username(dbUserVO.getUsername())
										.authority(auth)
										.build();
					
					authList.add(authVO);
				}
			}
			authDao.delete(dbUserVO.getUsername());
			authDao.insert(authList);
		}
		return ret;
	}
	
	protected int valid_update_user_from_admin(UserDetailsVO userVO) {
		String email = userVO.getEmail();
		String birth = String.format("%s-%s-%s", userVO.getYear(), userVO.getMonth(), userVO.getDay());
		int result = 0;
		
		if (email.isEmpty() || !email.matches("^([a-zA-Z0-9_.+-])+\\@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{1,6})+$")) {
			// 이메일 유효성 검사
			result = -102;
		} else if( !this.dateCheck(birth) ) {
			// 생년월일 유효성 검사
			result = -103;
		} else if(userDao.findByUsername(userVO.getUsername()) == null) {
			// DB에 존재하는 아이디인지 검사
			result = -200;
		}
		
		return result;
	}
	
	protected boolean dateCheck(String date) {
		SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		try {
			dateFormat.parse(date);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public int create_board(BoardInfoVO boardInfoVO) {
		return adminDao.create_board(boardInfoVO);
	}
	
	@Transactional
	public int update_tbl_board_info(BoardInfoVO boardInfoOptionVO, CategoryVO categoryOptionVO) {
		// boardInfoOptionVO에는 게시판 ID(bi_id)와 게시판 이름(bi_name)이 들어있다
		// categoryOptionVO에는 카테고리 목록이 들어있다
		
		// DB의 게시판 정보(tbl_board_info) 불러오기
		BoardInfoVO dbBoardInfoVO = boardDao.findByBoardInfo(boardInfoOptionVO.getBi_id());
		
		// 유효성 검사 시작
		if(boardInfoOptionVO.getBi_name().length() > 20) {
			// 게시판 이름이 20글자를 초과하는 경우
			return -100;
		} else if(dbBoardInfoVO == null) {
			// DB에 게시판 ID가 존재하지 않는 경우
			return -200;
		}
		
		// 유효성 검사 통과 시 
		// 기존의 게시판 정보에 form에서 입력받은 정보(게시판 이름) 새로 세팅하기
		dbBoardInfoVO.setBi_name(boardInfoOptionVO.getBi_name());
		int ret = adminDao.update_tbl_board_info(dbBoardInfoVO);
		
		// 업데이트 성공 시 카테고리 테이블 업데이트
		if(ret > 0) {
			
			List<Long> cate_id_list = categoryOptionVO.getCate_id_list();
			List<String> cate_text_list = categoryOptionVO.getCate_text_list();
			
			// 받은 카테고리 값이 있으면(리스트가 null이 아니면) 카테고리 테이블 업데이트 
			if(cate_id_list != null) {
			
				List<CategoryVO> categoryList = new ArrayList<CategoryVO>();
				
				for(int i = 0; i < cate_id_list.size(); i++) {
					long cate_id = cate_id_list.get(i);
					String cate_text = cate_text_list.get(i);
					
					// cate_id 여부에 따라서 INSERT UPDATE 선택
					if(cate_id == 0) {
						// cate_id를 입력받지 않았으면(0 이면) 게시판에 카테고리 새로 추가
						CategoryVO categoryVO = CategoryVO.builder()
								.cate_bi_id(dbBoardInfoVO.getBi_id())
								.cate_text(cate_text)
								.build();
						categoryList.add(categoryVO);
					} else {
						// cate_id를 입력받았으면 기존 DB 카테고리 내용 업데이트
						CategoryVO categoryVO = CategoryVO.builder()
								.cate_id(cate_id)
								.cate_bi_id(dbBoardInfoVO.getBi_id())
								.cate_text(cate_text)
								.build();
						cateDao.update(categoryVO);
					}
					
				}
				if(categoryList.size() > 0) cateDao.insert(categoryList);
			}
			
		}
		
		return ret;
	}

	public int delete_category(long cate_id) {
		return cateDao.delete(cate_id);
	}

}
