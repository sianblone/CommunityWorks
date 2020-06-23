<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form"  prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<style>
	#cmt_content_unauth {
		background-color: white;
	}
	.cmt_nickname {
		display: flex;
		align-items: center;
		font-weight: bold;
		overflow: hidden;
		white-space: nowrap;
	}
</style>
<script>
	$(function() {
		let enable_btn_cmt_save = true
		
		$(document).on("click", "#cmt_content_unauth", function() {
			if(confirm("로그인 하시겠습니까?")) {
				document.location.href = "${rootPath}/user/login"
			}
		})
		
		$(document).on("click", "#btn_cmt_save", function() {
			if(!enable_btn_cmt_save) return false
			
			if($("#isAuth").length == 0) {
				if(confirm("로그인 하시겠습니까?")) {
					document.location.href = "${rootPath}/user/login"
					return false
				} else {
					return false
				}
			}
			
			if($("#cmt_content").val() == "") {
				alert("내용을 입력하세요.")
				return false
			}
			
			// 유효성 검사 통과 시
			// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
			enable_btn_cmt_save = false
			$("body").css("cursor", "progress")
			
			$.ajax({
				url: "${rootPath}/comment/save",
				type: "POST",
				data: $("#comment_form").serialize(),
				success: function(result) {
					$("#cmt_content").val("")
					$(".cmt_list").html(result)
				},
				error: function(error) {
					alert("서버 통신 오류")
				}
			}).always(function() {
				enable_btn_cmt_save = true
				$("body").css("cursor", "default")
			})
		})
	})
</script>
<form id="comment_form" method="POST" autocomplete="${FORM_AUTOCOMPLETE}">
	<div class="row p-4 bg-light">
		<sec:authorize access="hasAnyRole('ADMIN','USER')">
			<input id="isAuth" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
		</sec:authorize>
		<input type="hidden" name="cmt_board_no" value="<c:out value='${param.board_no}' default='0'/>">
		<input type="hidden" name="cmt_p_no" value="0">
		<div class="col-2 cmt_nickname">
			<sec:authorize access="isAuthenticated()">
				<span><sec:authentication property="principal.nickname"/></span>
			</sec:authorize>
		</div>
		<div class="col-8">
			<sec:authorize access="isAuthenticated()">
				<input id="cmt_content" class="form-control" name="cmt_content" placeholder="댓글">
			</sec:authorize>
			<sec:authorize access="!isAuthenticated()">
				<input id="cmt_content_unauth" class="form-control" placeholder="댓글을 달려면 로그인을 해야합니다." readonly>
			</sec:authorize>
		</div>
		
		<div class="col-2 d-flex justify-content-center">
			<button id="btn_cmt_save" class="btn btn-success" type="button">저장</button>
		</div>
	</div>
</form>