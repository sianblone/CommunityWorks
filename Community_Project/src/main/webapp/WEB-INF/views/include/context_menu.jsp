<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<link rel="stylesheet" href="${rootPath}/resources/css/context_menu.css"/>
<script src="${rootPath}/resources/js/context_menu.js"></script>

<div class="context context_inactive">
	<ul>
		<li class="search_nickname">작성 글 보기</li>
	</ul>
</div>