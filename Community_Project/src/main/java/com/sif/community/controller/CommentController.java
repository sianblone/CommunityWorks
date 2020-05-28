package com.sif.community.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sif.community.model.CommentVO;
import com.sif.community.service.board.itf.CommentService;

import lombok.RequiredArgsConstructor;

@RequestMapping(value="/comment")
@Controller
@RequiredArgsConstructor
public class CommentController {
	
	private final CommentService cmtService;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(String board_no, Model model) {
		
		long cmt_board_no = Long.valueOf(board_no);
		List<CommentVO>cmtList = cmtService.findByBoardNo(cmt_board_no);
		model.addAttribute("CMT_LIST",cmtList);	
		return "comment/comment_list";
	}
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public String insert(CommentVO cmtVO, Model model) {
		int ret = cmtService.insert(cmtVO);
		model.addAttribute("cmt_writer",cmtVO.getCmt_writer());
		model.addAttribute("board_no",cmtVO.getCmt_board_no());
		return "redirect:/comment/comment/list";
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String cmt_no, String board_no, Model model) {
		int ret = cmtService.delete(Long.valueOf(cmt_no));
		model.addAttribute("board_no",board_no);
		return "redirect:/comment/list";
	}
	
	@RequestMapping(value="/repl",method=RequestMethod.GET)
	public String repl(CommentVO cmtVO, Model model) {
		model.addAttribute("CMT",cmtVO);
		return "board/comment_write";
	}

}
