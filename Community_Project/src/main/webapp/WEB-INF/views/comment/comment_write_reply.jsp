<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form"  prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<section class="cmt_write_reply">
	<form class="comment_reply_form" method="POST" autocomplete="${FORM_AUTOCOMPLETE}">
		<sec:authorize access="hasAnyRole('ADMIN','USER')">
			<input class="cmt_reply_csrf" name="${_csrf.parameterName}" value="${_csrf.token}" type="hidden">
		</sec:authorize>
		<input name="cmt_board_no" value="<c:out value='${C.cmt_board_no}' default='0'/>" type="hidden">
		<input class="cmt_reply_p_no" name="cmt_p_no" value="<c:out value='${C.cmt_no}' default='0'/>" type="hidden">
		<article class="cmt_write_box">
			<div class="cmt_write_group cmt_nickname">
				<sec:authorize access="isAuthenticated()">
					<span><sec:authentication property="principal.nickname"/></span>
				</sec:authorize>
			</div>
			
			<div class="cmt_write_group">
				<sec:authorize access="isAuthenticated()">
					<textarea class="form-control cmt_reply_content" name="cmt_content" rows="2"></textarea>
				</sec:authorize>
				<sec:authorize access="!isAuthenticated()">
					<textarea class="form-control cmt_content_unauth" rows="2" placeholder="댓글을 달려면 로그인을 해야합니다." readonly></textarea>
				</sec:authorize>
			</div>
			
			<div class="cmt_btn_box">
				<button class="btn_cmt_reply_save btn_blue" type="button">등록</button>
			</div>
		</article>
	</form>
</section>