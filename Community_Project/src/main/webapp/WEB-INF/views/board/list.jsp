<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/include_head.jspf"%>
<style>

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
.c_num, .c_date, .c_count, .c_recommend {
	font-size: 11px;
	vertical-align: middle;
}
a {
	color: black;
}

span.board_p_no {
	color: gray;
	font-size: 11px;
}
span.board_nickname {
	cursor: pointer;
}

tr[data-reply] .subject {
	margin-left: 20px;
}

.deleted {
	color: gray;
}

</style>
<script>
	$(function() {
		$("button.btn-writer").click(function() {
			let url = "${rootPath}/board/save?board_info=${BOARD_INFO.bi_id}";

			let currPage = "${param.currPage}"
			if (currPage != "")
				url += "&currPage=" + currPage

			document.location.href = url
		})
	})
</script>
</head>

<body>
	<%@ include file="/WEB-INF/views/include/include_nav.jspf"%>
	<main>
	<%@ include file="/WEB-INF/views/include/include_header.jsp"%>
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
								<tr class="<c:if test="${B.board_delete == 1}">deleted</c:if>" data-id="${B.board_no}" <c:if test="${B.board_depth > 0}">data-reply="${B.board_depth}"</c:if>>
									<td class="c_num text-center">${B.board_no}</td>
									<td class="c_subject"><c:if test="${B.board_depth > 0}">└<span class="board_p_no">[${B.board_p_no}]</span> </c:if><c:if test="${B.board_delete == 1}">[삭제됨] </c:if><a
									href="${rootPath}/board/details?board_info=${B.board_info}&board_no=${B.board_no}&currPage=${param.currPage}">${B.board_category} ${B.board_subject}</a></td>
									<td class="c_nickname text-center"><span class="board_nickname">${B.board_nickname}</span></td>
									<td class="c_date text-center">${B.board_datetime}</td>
									<td class="c_count text-center">${B.board_count}</td>
									<td class="c_recommend text-center">${B.board_recommend}</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</article>
		<article class="d-flex justify-content-end">
			<button class="btn btn-primary text-white btn-writer m-2">글쓰기</button>
		</article>
		<%@ include file="/WEB-INF/views/include/search.jsp" %>
	</main>

	<%@ include file="/WEB-INF/views/include/pagination.jsp"%>
</body>
</html>