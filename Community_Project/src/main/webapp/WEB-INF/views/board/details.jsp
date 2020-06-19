<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/include_head.jspf" %>
<style>
	.details_info {
		display: flex;
	}
	small {
		display: block;
	}
	.details_info small:nth-child(3) {
		margin-left: auto !important;
	}
	.deleted {
		color: gray;
	}
</style>
<script>
$(function(){
	$(document).on("click","cmt-item",function(){
		let id = $(this).data("id")
		let writer = $(this).find("div.writer").find("b").text()
		let subject = $(this).find("div.subject").text()
		
		$("#c_id").val(id)
		$("#c_writer").val(writer)
		$("#c_subject").val(subject)
	})
	
	$(document).on("click","div.cmt-item-del",function(event){
		event.stopPropagation()
		if(!confirm("삭제할까요?")) {
			return false
		}
		let c_id = $(this).parent("div").data("id")
		$.ajax({
			url : "${rootPath}/comment/delete/",
			data : {
				c_id : c_id,
				g_id : "${B.board_no}"
			},
			type : "POST",
			success : function(result) {
				$("div.cmt-list").html(result)
			},
			error : function() {
				alert("서버통신오류")
			}
		})
	})
	
	$(document).on("click",".cmt-item-repl",function(event){
		let board_no = "${B.board_no}"
		let cmt_no = $(this).parent("div").data("id")
		let data = {cmt_board_no:board_no,cmt_p_no:cmt_no}
		
		ebent.stopPropagation()
		
		$.get("${rootPath}/comment/repl",data,function(result){
			$(".modal-body").html(result)
			$(".modal-main").css("display","block")
		})
	})
	
	$(document).on("click","button",function(){
		let txt = $(this).text()
		if(txt == '수정') {
			document.location.href="${rootPath}/board/save?board_info=${BOARD_VO.board_info}&board_no=${BOARD_VO.board_no}&currPage=${param.currPage}"
		} else if(txt == '삭제') {
			if(confirm("정말 삭제하시겠습니까?")) {
				document.location.replace("${rootPath}/board/delete?board_no=${BOARD_VO.board_no}&currPage=${param.currPage}")
			}
		} else if(txt == '답변저장') {
			var formData = $("form.repl").serialize()
			$.ajax({
				url : "${rootPath}/comment/insert",
				data : formData,
				type : "POST",
				success : function(result) {
					$(".modal-main").css("display","none")
					$("div.cmt-list").html(result)
				},
				error : function() {
					alert("서버통신오류")
				}
			})
		} else if(txt == '저장') {
			$.ajax({
				url : "${rootPath}/comment/insert",
				data : {
					"cmt_writer" : $("#cmt_writer").val(),
					"cmt_content" : $("#cmt_content").val()
			},
				type : "POST",
				beforeSend: function(ajx) {
				ajx.setRequestHeader("${_csrf.headerName}", "${_csrf.token}")
				},
				success : function(result) {
					$("div.cmt-list").html(result)
				},
				error : function() {
					alert("서버통신오류")
				}
			})
			return true
		} else if(txt == "답글") {
			document.location.href = "${rootPath}/board/save?board_info=${BOARD_VO.board_info}&board_p_no=${BOARD_VO.board_no}"
			return false
		} else if(txt == "목록"){
			document.location.href="${rootPath}/board/list?board_info=${BOARD_VO.board_info}"
		} else if(txt == "완전삭제"){
			if(confirm("정말 이 글을 완전히 삭제하시겠습니까?"))
			document.location.replace("${rootPath}/board/admin?board_no=${BOARD_VO.board_no}&currPage=${param.currPage}&order=delete")
		} else if(txt == "글 복구"){
			if(confirm("이 글을 복구하시겠습니까?"))
			document.location.href="${rootPath}/board/admin?board_no=${BOARD_VO.board_no}&currPage=${param.currPage}&order=restore"
		}
	})
})
</script>
</head>
<style>
.cmt-item-del {
	cursor: pointer;
}
</style>
<body>
<%@ include file="/WEB-INF/views/include/include_nav.jspf" %>
	<main>
		<section class="container-fluid">
			<div class="text-right">
				<h2 class="p-1 <c:if test="${BOARD_VO.board_delete == 1}">deleted</c:if>"><c:if test="${BOARD_VO.board_delete == 1}">[삭제됨] </c:if>${BOARD_VO.board_subject}</h2>
				<small class="m-3">작성일시 : ${BOARD_VO.board_datetime}</small>
			</div>
			<hr/>
			<div class="details_info">
				<small class="m-3">카테고리 : <c:if test="${BOARD_VO.board_category == null}">없음</c:if><c:if test="${BOARD_VO.board_category != null}">${BOARD_VO.board_category}</c:if></small>
				<small class="m-3">작성자 : ${BOARD_VO.board_nickname}</small>
				<small class="m-3">조회수 : ${BOARD_VO.board_count}</small>
				<small class="m-3">추천수 : ${BOARD_VO.board_recommend}</small>
			</div>
			<hr/>
			<div class="p-3">
				${BOARD_VO.board_content}
			</div>
		</section>
		<div class="form-group d-flex justify-content-end">
			<c:if test="${IS_ADMIN}">
				<button class="btn btn-warning mr-3">완전삭제</button>
				<button class="btn btn-warning mr-3">글 복구</button>
			</c:if>
			<c:if test="${IS_WRITER || IS_ADMIN}">
				<button class="btn btn-primary mr-3">수정</button>
				<button class="btn btn-danger mr-3">삭제</button>
			</c:if>
			<button class="btn btn-info mr-3">답글</button>
			<button class="btn btn-success">목록</button>
		</div>
		<hr/>
		<section class="container-fluid p-4">
			<div class="p-2">
				<b>댓글을 남겨주세요</b>
			</div>
			<form method="POST" class="main">
				<div class="row p-4 bg-light">
					<input type="hidden" name="cmt_no" id="cmt_no" value="0">
					<input type="hidden" name="cmt_board_no" value="${B.board_no}">
					<div class="col-2">
						<input name="cmt_writer" id="cmt_writer"
								class="form-control" placeholder="작성자">
					</div>
					<div class="col-8">
						<input name="cmt_content" id="cmt_content"
								class="form-control" placeholder="댓글을 입력하세요">
					</div>
					<div class="col-2 d-flex justify-content-center">
						<button type="button" class="btn btn-success btn-cmt-save">저장</button>
					</div>
				</div>
			</form>
			<div class="p-2">
				<b>댓글 리스트</b>
			</div>
			<div class="p-4 cmt-list">
				<%@ include file="/WEB-INF/views/comment/comment_list.jsp" %>
			</div>
		</section>
	</main>
</body>
</html>