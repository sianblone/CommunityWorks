<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/include_head.jspf" %>
<style>
	tbody tr {
		cursor: pointer;
	}
</style>
<script>
	$(function(){
		$("button.btn-writer").click(function(){
			document.location.href="${rootPath}/board/save?board_name=${BOARD_NAME}"
		})
		
		$(document).on("click", "tbody tr", function() {
			let board_no = $(this).data("id");
			document.location.href = "${rootPath}/board/details?board_name=${BOARD_NAME}&board_no=" + board_no
		})
	})
</script>
</head>

<body>
<%@ include file="/WEB-INF/views/include/include_nav.jspf" %>
	<section class="container-fluid">
		<article>
			<table class="table table-striped table-hover text-center">
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
							<tr><td colspan="7">등록된 리스트가 없습니다</td></tr>
						</c:when>
						<c:otherwise>
							<c:forEach items="${BOARD_LIST}" var="B" varStatus="i">
								<tr data-id="${B.board_no}">
									<td>${i.count}</td>
									<td>${B.board_category}</td>
									<td>${B.board_subject}</td>
									<td>${B.board_nickname}</td>
									<td>${B.board_date} ${B.board_time}</td>
									<td>${B.board_count}</td>
									<td>${B.board_recommend}</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>
		</article>
		<article class="d-flex justify-content-end">
			<button class="btn btn-primary text-white btn-writer">글쓰기</button>
		</article>
	</section>
	
	<%@ include file="/WEB-INF/views/include/pagination.jsp" %>
</body>
</html>