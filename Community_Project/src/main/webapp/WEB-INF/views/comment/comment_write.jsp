<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form"  prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>

<script>
	$(function() {
		$(document).on("click", "#cmt_content_unauth", function() {
			if(confirm("로그인 하시겠습니까?")) {
				document.location.href = "${rootPath}/user/login"
			}
		})
		
		$(document).on("click", "#btn_cmt_save", function() {
			if($("#isAuth").length == 0) {
				if(confirm("로그인 하시겠습니까?")) {
					document.location.href = "${rootPath}/user/login"
					return false
				} else {
					return false
				}
			}
			
			$.ajax({
				url: "${rootPath}/comment/save",
				type: "POST",
				data: $("#comment_form").serialize(),
				success: function(result) {
					alert("성공")
				},
				error: function(error) {
					alert("서버 통신 오류")
				}
			})
		})
	})
</script>
<form id="comment_form" method="POST" autocomplete="${FORM_AUTOCOMPLETE}">
	<div class="row p-4 bg-light">
		<sec:authorize access="hasAnyRole('ADMIN','USER')">
			<input id="isAuth" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
		</sec:authorize>
		<input type="hidden" name="cmt_board_no" value="${param.board_no}">
		<input type="hidden" name="cmt_p_no" value="">
		<div class="col-2">
			<sec:authorize access="isAuthenticated()">
				<span id="cmt_writer" class="form-control"><sec:authentication property="principal.username"/></span>
			</sec:authorize>
		</div>
		<div class="col-8">
			<sec:authorize access="isAuthenticated()">
				<input id="cmt_content" class="form-control" placeholder="댓글">
			</sec:authorize>
			<sec:authorize access="!isAuthenticated()">
				<input id="cmt_content_unauth" class="form-control" name="cmt_content" placeholder="댓글을 달려면 로그인을 해야합니다.">
			</sec:authorize>
		</div>
		
		<div class="col-2 d-flex justify-content-center">
			<button id="btn_cmt_save" class="btn btn-success" type="button">저장</button>
		</div>
	</div>
</form>