<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/include_head.jspf" %>
<style>
	header {
		cursor: pointer;
	}
	.details_info_hr {
		margin-top: 0;
	}
	.details_info {
		display: flex;
		flex-direction: column;
		padding: 0px 10px;
	}
	.details_info_item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		flex: 1;
	}
	.details_content_box {
		min-height: 300px;
		padding: 10px 20px;
		margin-bottom: 10px;
	}
	.info_cate, .info_count, .info_recommend, .info_datetime {
		font-size: 12px;
	}
	.info_subject {
		font-weight: bold;
	}
	.deleted {
		color: gray;
	}
	
	.details_button_box {
		display: flex;
		justify-content: flex-end;
		margin: 0px 10px 1rem 0px;
	}
	.details_button_box button {
		margin-left: 10px;
		border: 1px solid var(--color-dodgerblue);
	    background-color: white;
	    color: black;
	}
	.details_button_box button:hover {
		background-color: var(--color-dodgerblue);
		color: white;
	}
	
	
</style>
<script>
	$(function() {
		
		$.ajax({
			url: "${rootPath}/comment/list",
			type: "GET",
			data: { cmt_board_no : "${BOARD_VO.board_no}" },
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
		
		$("header").click(function() {
			document.location.href = "${rootPath}/board/list?board_info=${BOARD_VO.board_info}"
		})
	})
</script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/include_nav.jspf" %>
	<header>
		<h2>${BOARD_VO.board_name}</h2>
	</header>
	<main>
		<hr class="details_info_hr"/>
		<article class="details_info">
			<div class="details_info_item">
				<span class="info_cate"><c:if test="${BOARD_VO.board_category != null}">[${BOARD_VO.board_cate_text}]</c:if></span>
				<span class="info_count">조회 ${BOARD_VO.board_count}</span>
			</div>
			
			<div class="details_info_item">
				<span class="info_subject" class="<c:if test="${BOARD_VO.board_delete == 1}">deleted</c:if>"><c:if test="${BOARD_VO.board_delete == 1}">[삭제됨] </c:if>${BOARD_VO.board_subject}</span>
				<span class="info_recommend">추천 ${BOARD_VO.board_recommend}</span>
			</div>
			
			<div class="details_info_item">
				<span class="info_nickname">${BOARD_VO.board_nickname}</span>
				<span class="info_datetime">${BOARD_VO.board_custom_full_datetime}</span>
			</div>
		</article>
		<hr/>
		<article class="details_content_box">
			<p>${BOARD_VO.board_content}</p>
		</article>
		<hr/>
		<article class="details_button_box">
			<c:if test="${BOARD_VO.viewerAdmin}">
				<button id="btn_delete_complete">완전삭제</button>
				<button id="btn_restore">글 복구</button>
			</c:if>
			<c:if test="${BOARD_VO.viewerAdmin || BOARD_VO.viewerWriter}">
				<button id="btn_edit">수정</button>
				<button id="btn_delete">삭제</button>
			</c:if>
			<button id="btn_reply">답글</button>
			<button id="btn_list">목록</button>
		</article>
		
		<section class="cmt_list">
			<%@ include file="/WEB-INF/views/comment/comment_list.jsp" %>
		</section>
		
		<%@ include file="/WEB-INF/views/comment/comment_write.jsp" %>
	</main>
	<%@ include file="/WEB-INF/views/include/include_footer.jsp" %>
</body>
</html>