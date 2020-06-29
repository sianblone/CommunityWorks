<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="rootPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/include_head.jspf"%>
<style>
	header {
		cursor: pointer;
	}
	table {
		table-layout: fixed;
		font-family: gulim, 굴림;
		font-size: 13px;
		width: 100%;
		padding: 0px 4px;
		border-collapse: collapse;
	}
	tr {
		border-bottom: 1px solid skyblue;
	}
	th {
		border-top: 2px solid var(--button-bg-color);
		border-bottom: 1px solid var(--button-bg-color);
		
		height: 40px;
		text-align: center;
	}
	td {
		padding: 6px 4px;
	}
	.b_num, .b_date, .b_count, .b_recommend {
		font-size: 11px;
		vertical-align: middle;
	}
	tr:not(.deleted) a {
		color: black;
	}
	tr.deleted a {
		color: gray;
	}
	
	span.board_p_no {
		color: gray;
		font-size: 11px;
	}
	
	.deleted {
		color: gray;
	}
	
	.btn_write {
		margin: 10px;
	}
</style>
<script>
	let rootPath = "${rootPath}"
	let bi_id = "${BOARD_INFO.bi_id}"
	
	$(function() {
		$(document).off("click", ".btn_write").on("click", ".btn_write", function() {
			let url = "${rootPath}/board/save?board_info=${BOARD_INFO.bi_id}";

			let currPage = "${param.currPage}"
			if (currPage != "")
				url += "&currPage=" + currPage

			document.location.href = url
		})
		
		$(document).off("click", "header").on("click", "header", function() {
			document.location.href = "${rootPath}/board/list?board_info=${BOARD_INFO.bi_id}"
		})
	})
</script>
</head>

<body>
	<%@ include file="/WEB-INF/views/include/include_nav.jspf"%>
	<header>
		<h2>${BOARD_INFO.bi_name}</h2>
	</header>
	<main>
		<article>
			<table>
				<colgroup>
					<col style="width: 7%">
					<col>
					<col style="width: 18%">
					<col style="width: 6%">
					<col style="width: 6%">
					<col style="width: 6%">
				</colgroup>
				<thead>
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>작성자</th>
						<th>등록일</th>
						<th>조회</th>
						<th>추천</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${empty BOARD_LIST}">
							<tr>
								<td class="text-center" colspan="6">등록된 게시물이 없습니다</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach items="${BOARD_LIST}" var="B" varStatus="i">
								<tr class="context_parent<c:if test="${B.board_delete == 1}"> deleted</c:if>" data-id="${B.board_no}"
								data-nickname="${B.board_nickname}" <c:if test="${B.board_depth > 0}">data-reply="${B.board_depth}"</c:if>>
									<td class="b_num text-center">${B.board_no}</td>
									<td class="b_subject"><a href="${rootPath}/board/details?board_info=${B.board_info}&board_no=${B.board_no}&currPage=${param.currPage}"><c:if
									test="${B.board_depth > 0}">└<span class="board_p_no">[${B.board_p_no}] </span></c:if><c:if test="${B.board_delete == 1}">[삭제됨] </c:if><c:if
									test="${B.board_cate_text != null}">[${B.board_cate_text}] </c:if>${B.board_subject}<c:if
									test="${B.board_cmt_count > 0}"> [${B.board_cmt_count}]</c:if></a></td>
									<td class="b_nickname text-center"><span class="use_context context_nickname">${B.board_nickname}</span></td>
									<td class="b_date text-center">${B.board_custom_datetime}</td>
									<td class="b_count text-center">${B.board_count}</td>
									<td class="b_recommend text-center">${B.board_recommend}</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</article>
		<article class="d-flex justify-content-end">
			<button class="btn_write">글쓰기</button>
		</article>
		<%@ include file="/WEB-INF/views/include/search.jsp" %>
	</main>

	<%@ include file="/WEB-INF/views/include/pagination.jsp"%>
	<%@ include file="/WEB-INF/views/include/context_menu.jsp" %>
	<%@ include file="/WEB-INF/views/include/include_footer.jsp" %>
</body>
</html>