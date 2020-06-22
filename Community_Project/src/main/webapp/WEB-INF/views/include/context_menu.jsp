<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<script src="${rootPath}/resources/js/context_menu.js"></script>
<style>
	.context {
		position: absolute;
		border: 1px solid gray;
		text-align: center;
		background-color: white;
		font-size: 13px;
		cursor: pointer;
		box-shadow: 3px 3px 3px rgba(0, 0, 0, 0.4);
	}
	.context ul {
		list-style: none;
		margin: 0;
	}
	.context li {
		padding: 8px 15px;
	}
	.context li:hover {
		background-color: #eee;
		font-weight: bold;
	}
	
	.context_inactive {
		display: none;
	}
	.context_active {
		display: block;
	}
</style>
<div class="context context_inactive">
	<ul>
		<li class="search_nickname">작성 글 보기</li>
	</ul>
</div>