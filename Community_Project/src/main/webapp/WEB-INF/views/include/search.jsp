<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}" />
<style>
	#search_form {
		margin-top: 100px;
		display: flex;
		justify-content: center;
	}
</style>
<form:form id="search_form" method="GET" action="${rootPath}/board/list">
	<input type="hidden" name="board_info" value="<c:out value='${param.board_info}' default='0'/>" />
	<select id="search_type" name="search_type">
		<option value="sc" <c:out value=""/>>제목+내용</option>
		<option value="subject" <c:out value=""/>>제목</option>
		<option value="content" <c:out value=""/>>내용</option>
		<option value="writer" <c:out value=""/>>작성자</option>
	</select>
	<input name="search_txt" id="search_txt" placeholder="검색어를 입력하세요" />
	<button id="search_btn">검색</button>
</form:form>