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
	$(function() {
		
		$.ajax({
			url: "${rootPath}/comment/list?board_no=${BOARD_VO.board_no}",
			type: "GET",
			success: function(result) {
				$(".cmt_list").html(result)
			},
			error: function(error) {
				console.log("댓글 불러오기 실패")
			}
		})
		
		$(document).on("click","button",function() {
			let id = $(this).attr("id")
			if(id == "btn_delete_complete") {
				if(confirm("정말 이 글을 완전히 삭제하시겠습니까?"))
				document.location.replace("${rootPath}/board/admin?board_no=${BOARD_VO.board_no}&currPage=${param.currPage}&order=delete")
			} else if(id == "btn_restore") {
				if(confirm("이 글을 복구하시겠습니까?"))
				document.location.href="${rootPath}/board/admin?board_no=${BOARD_VO.board_no}&currPage=${param.currPage}&order=restore"
			} else if(id == "btn_edit") {
				document.location.href="${rootPath}/board/save?board_info=${BOARD_VO.board_info}&board_no=${BOARD_VO.board_no}&currPage=${param.currPage}"
			} else if(id == "btn_delete") {
				if(confirm("정말 삭제하시겠습니까?"))
				document.location.replace("${rootPath}/board/delete?board_no=${BOARD_VO.board_no}&currPage=${param.currPage}")
			} else if(id == "btn_reply") {
				document.location.href = "${rootPath}/board/save?board_info=${BOARD_VO.board_info}&board_p_no=${BOARD_VO.board_no}"
				return false
			} else if(id == "btn_list") {
				document.location.href = "${rootPath}/board/list?board_info=${BOARD_VO.board_info}"
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
	<header>
		<h2 class="p-1 <c:if test="${BOARD_VO.board_delete == 1}">deleted</c:if>"><c:if test="${BOARD_VO.board_delete == 1}">[삭제됨] </c:if>${BOARD_VO.board_subject}</h2>
	</header>
	<main>
		<section class="container-fluid">
			<div class="text-right">
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
				<button id="btn_delete_complete" class="btn btn-warning mr-3">완전삭제</button>
				<button id="btn_restore" class="btn btn-warning mr-3">글 복구</button>
			</c:if>
			<c:if test="${IS_WRITER || IS_ADMIN}">
				<button id="btn_edit" class="btn btn-primary mr-3">수정</button>
				<button id="btn_delete" class="btn btn-danger mr-3">삭제</button>
			</c:if>
			<button id="btn_reply" class="btn btn-info mr-3">답글</button>
			<button id="btn_list" class="btn btn-success">목록</button>
		</div>
		<hr/>
		<section class="container-fluid p-4">
			<div class="p-2">
				<b>댓글</b>
			</div>
			<div class="p-4 cmt_write">
				<%@ include file="/WEB-INF/views/comment/comment_write.jsp" %>
			</div>
			<div class="p-2">
				<b>댓글 리스트</b>
			</div>
			<%@ include file="/WEB-INF/views/comment/comment_list.jsp" %>
		</section>
	</main>
</body>
</html>