package com.sif.community.service.board.itf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sif.community.dao.TopMenuDao;
import com.sif.community.model.BoardInfoVO;

@Service
public class TopMenuService {
	
	@Autowired
	private TopMenuDao topMenuDao;
	
	public List<BoardInfoVO> getTopMenuList(){
		List<BoardInfoVO> topMenuList = topMenuDao.getTopMenuList();
		
		return topMenuList;
	}
	
}
