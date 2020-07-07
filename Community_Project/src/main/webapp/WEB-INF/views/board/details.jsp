<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/include_head.jspf" %>
<style>
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
	.recommend_box {
		text-align: center;
	}
	.btn_recommend {
		padding: 1rem 1.8rem;
		margin-bottom: 10px;
	}
	.board_recommend {
		color: var(--color-primary);
		font-weight: bold;
	}
	.btn_recommend:hover .board_recommend {
		color: white;
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
		margin-bottom: 1rem;
	}
</style>
<script>
	let rootPath = "${rootPath}"
	let bi_id = "${BOARD_VO.board_info}"
	
	$(function() {
		
		$.ajax({
			url: "${rootPath}/comment/list",
			type: "GET",
			data: {
				cmt_board_no : "${BOARD_VO.board_no}"
			},
			success: function(result) {
				$(".cmt_list").html(result)
			},
			error: function(error) {
				console.log("댓글 불러오기 실패")
			}
		})
		
		$(document).on("click", "button", function() {
			let id = $(this).attr("id")
			
			if(id == "btn_delete_complete") {
				if(confirm("정말 이 글을 완전히 삭제하시겠습니까?")) {
					let strHref = "${rootPath}/board/admin?board_info=${BOARD_VO.board_info}&board_no=${BOARD_VO.board_no}&command=delete"
					if("${param.pageNo}") strHref += "&pageNo=${param.pageNo}"
					document.location.replace(strHref)
				}
			} else if(id == "btn_restore") {
				if(confirm("이 글을 복구하시겠습니까?")) {
					let strHref = "${rootPath}/board/admin?board_info=${BOARD_VO.board_info}&board_no=${BOARD_VO.board_no}&command=restore"
					if("${param.pageNo}") strHref += "&pageNo=${param.pageNo}"
					document.location.href = strHref
				}
			} else if(id == "btn_edit") {
				let strHref = "${rootPath}/board/save?board_info=${BOARD_VO.board_info}&board_no=${BOARD_VO.board_no}"
				if("${param.pageNo}") strHref += "&pageNo=${param.pageNo}"
				document.location.href = strHref
			} else if(id == "btn_delete") {
				if(confirm("정말 삭제하시겠습니까?")) {
					let strHref = "${rootPath}/board/delete?board_info=${BOARD_VO.board_info}&board_no=${BOARD_VO.board_no}"
					if("${param.pageNo}") strHref += "&pageNo=${param.pageNo}"
					document.location.replace(strHref)
				}
			} else if(id == "btn_reply") {
				let strHref = "${rootPath}/board/save?board_info=${BOARD_VO.board_info}&board_p_no=${BOARD_VO.board_no}"
				if("${param.pageNo}") strHref += "&pageNo=${param.pageNo}"
				document.location.href = strHref
			} else if(id == "btn_list") {
				let strHref = "${rootPath}/board/list?board_info=${BOARD_VO.board_info}"
				if("${param.pageNo}") strHref += "&pageNo=${param.pageNo}"
				document.location.href = strHref
			}
		})
		
		$(document).on("click", ".btn_recommend", function() {
			$.ajax({
				url: "${rootPath}/board/recommend",
				data: { board_no : "${BOARD_VO.board_no}" },
				type: "GET",
				success: function(result) {
					if(result > 0) {
						document.location.replace(document.location.href)
					} else {
						alert("동일한 게시물에 중복으로 추천할 수 없습니다.")
					}
				},
				error: function() {
					alert("서버 통신 오류")
				}
			})
		})
		
	})
</script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/include_nav.jspf" %>
	<header>
		<h2><a class="header_item" href="${rootPath}/board/list?board_info=${BOARD_VO.board_info}">${BOARD_VO.board_name}</a></h2>
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
			
			<div class="details_info_item context_parent" data-id="${BOARD_VO.board_no}" data-nickname="${BOARD_VO.board_nickname}">
				<span class="use_context context_nickname">${BOARD_VO.board_nickname}</span>
				<span class="info_datetime">${BOARD_VO.board_custom_full_datetime}</span>
			</div>
		</article>
		<hr/>
		<article class="details_content_box">
			${BOARD_VO.board_content}
		</article>
		<article class="recommend_box">
			<button class="btn_recommend btn_blue" type="button">추천<c:if test="${BOARD_VO.board_recommend > 0}"><span
			class="board_recommend"> ${BOARD_VO.board_recommend}</span></c:if></button>
		</article>
		<hr/>
		<article class="details_button_box">
			<c:if test="${BOARD_VO.viewerAdmin}">
				<button id="btn_delete_complete" class="btn_red mr-10px">완전삭제</button>
				<button id="btn_restore" class="btn_blue mr-10px">글 복구</button>
			</c:if>
			<c:if test="${BOARD_VO.viewerAdmin || BOARD_VO.viewerWriter}">
				<button id="btn_edit" class="btn_blue mr-10px">수정</button>
				<button id="btn_delete" class="btn_red mr-10px">삭제</button>
			</c:if>
			<button id="btn_reply" class="btn_blue mr-10px">답글</button>
			<button id="btn_list" class="btn_blue mr-10px">목록</button>
		</article>
		
		<section class="cmt_list">
			<%@ include file="/WEB-INF/views/comment/comment_list.jsp" %>
		</section>
	</main>
	<%@ include file="/WEB-INF/views/include/context_menu.jsp" %>
	<%@ include file="/WEB-INF/views/include/include_footer.jsp" %>
</body>
</html>