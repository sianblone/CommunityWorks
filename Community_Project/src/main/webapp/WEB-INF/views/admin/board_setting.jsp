<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
	.board_info {
		cursor: pointer;
	}
	.board_info:hover {
		background-color: #DDD;
	}
</style>

<c:choose>
	<c:when test="${empty BOARD_INFO_LIST}">
		<p>생성된 게시판이 없습니다</p>
	</c:when>
	<c:otherwise>
		<c:forEach items="${BOARD_INFO_LIST}" var="vo">
			<div class="board_info" data-id="${vo.bi_id}">${vo.bi_id}번 ${vo.bi_name}</div> 
		</c:forEach>
	</c:otherwise>
</c:choose>
<button id="btn_create_board">게시판 추가</button>
