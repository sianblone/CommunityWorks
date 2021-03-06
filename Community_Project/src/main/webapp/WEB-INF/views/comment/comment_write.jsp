<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form"  prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<style>
	.cmt_form {
		border: 2px solid rgba(0, 0, 0, 0.1);
	}
	.cmt_write_box {
		display: flex;
		padding: 20px 0px;
	}
	.cmt_write_group {
		display: flex;
		align-items: center;
	}
	.cmt_write_group:nth-child(3n+1) {
		width: 17%;
		justify-content: center;
		font-weight: bold;
		overflow: hidden;
		white-space: nowrap;
	}
	.cmt_write_group:nth-child(3n+2) {
		width: 80%;
	}
	.cmt_content_unauth {
		background-color: white;
	}
	.cmt_btn_box {
		display: flex;
		margin-bottom: 10px;
	}
	.cmt_btn_box button {
		padding: 0.2rem 0.5rem;
	}
	.cmt_btn_box button:first-child {
		margin-left: auto;
	}
	.cmt_btn_box button:last-child {
		margin-right: 30px;
	}
</style>

<form class="cmt_form" method="POST" autocomplete="${FORM_AUTOCOMPLETE}">
	<sec:authorize access="hasAnyRole('ADMIN','USER')">
		<input class="cmt_csrf" name="${_csrf.parameterName}" value="${_csrf.token}" type="hidden">
	</sec:authorize>
	<input name="cmt_board_no" value="<c:out value='${BOARD_VO.board_no}' default='0'/>" type="hidden">
	
	<article class="cmt_write_box">
		<div class="cmt_write_group cmt_nickname">
			<sec:authorize access="isAuthenticated()">
				<span><sec:authentication property="principal.nickname"/></span>
			</sec:authorize>
		</div>
		
		<div class="cmt_write_group">
			<sec:authorize access="isAuthenticated()">
				<textarea class="form-control cmt_content" name="cmt_content" rows="2"></textarea>
			</sec:authorize>
			<sec:authorize access="!isAuthenticated()">
				<textarea class="form-control cmt_content_unauth" rows="2" placeholder="댓글을 달려면 로그인해야 합니다." readonly></textarea>
			</sec:authorize>
		</div>
	</article>
	
	<article class="cmt_btn_box">
		<button class="btn_cmt_save btn_blue" type="button">등록</button>
	</article>
</form>