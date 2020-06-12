<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="rootPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/include_head.jspf"%>
<style>
tbody tr[data-id] {
	cursor: pointer;
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

		$(document)
				.on(
						"click",
						"tbody tr[data-id]",
						function() {
							let board_no = $(this).data("id")
							let url = "${rootPath}/board/details?board_info=${BOARD_INFO.bi_id}&board_no="
									+ board_no;

							let currPage = "${param.currPage}";
							if (currPage != "")
								url += "&currPage=" + currPage;

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
			<table class="table table-hover text-center">
				<thead>
					<tr>
						<th>글번호</th>
						<th>카테고리</th>
						<th>제목</th>
						<th>작성자</th>
						<th>등록일</th>
						<th>조회수</th>
						<th>추천수</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${empty BOARD_LIST}">
							<tr>
								<td colspan="7">등록된 리스트가 없습니다</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach items="${BOARD_LIST}" var="B" varStatus="i">
								<tr class="<c:if test="${B.board_delete == 1}">deleted</c:if>"
									data-id="${B.board_no}">
									<td>${B.board_no}</td>
									<td>${B.board_category}</td>
									<td><c:if test="${B.board_delete == 1}">[삭제됨] </c:if>${B.board_subject}</td>
									<td>${B.board_nickname}</td>
									<td>${B.board_date} ${B.board_time}</td>
									<td>${B.board_count}</td>
									<td>${B.board_recommend}</td>
								</tr>
								<c:if test="${not empty B.board_reply}">
									<c:forEach items="${B.board_reply}" var="RE" varStatus="status">
										<tr
											class="<c:if test="${RE.board_delete == 1}">deleted</c:if>"
											data-id="${RE.board_no}">
											<td>${RE.board_no}</td>
											<td>${RE.board_category}</td>
											<td><c:if test="${RE.board_delete == 1}">[삭제됨] </c:if>
												${RE.board_subject}</td>
											<td>${RE.board_nickname}</td>
											<td>${RE.board_date}${RE.board_time}</td>
											<td>${RE.board_count}</td>
											<td>${RE.board_recommend}</td>
										</tr>

									</c:forEach>
								</c:if>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
			<%@ include file="/WEB-INF/views/include/search.jsp" %>
		</article>
		<article class="d-flex justify-content-end">
			<button class="btn btn-primary text-white btn-writer">글쓰기</button>
		</article>
	</main>

	<%@ include file="/WEB-INF/views/include/pagination.jsp"%>
</body>
</html>