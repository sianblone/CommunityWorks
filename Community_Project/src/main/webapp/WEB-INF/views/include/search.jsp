<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}" />
<style>
	#search_form {
		margin-top: 10px;
		display: flex;
		justify-content: center;
	}
	
	#search_type, #search_txt {
		font-size: 0.9rem;
		margin-right: 10px;
	}
</style>
<form:form id="search_form" method="GET" action="${rootPath}/board/list" autocomplete="${FORM_AUTOCOMPLETE}">
	<input type="hidden" name="board_info" value="<c:out value='${param.board_info}' default='0'/>" />
	<select id="search_type" name="search_type">
		<option value="sc" <c:if test="${param.search_type == 'sc'}">selected="selected"</c:if>>제목+내용</option>
		<option value="subject" <c:if test="${param.search_type == 'subject'}">selected="selected"</c:if>>제목</option>
		<option value="content" <c:if test="${param.search_type == 'content'}">selected="selected"</c:if>>내용</option>
		<option value="nickname" <c:if test="${param.search_type == 'nickname'}">selected="selected"</c:if>>닉네임</option>
		<option value="writer" <c:if test="${param.search_type == 'writer'}">selected="selected"</c:if>>아이디</option>
	</select>
	<input name="search_txt" id="search_txt" placeholder="검색어를 입력하세요" value="${param.search_txt}"/>
	<button id="search_btn" class="btn_confirm">검색</button>
</form:form>