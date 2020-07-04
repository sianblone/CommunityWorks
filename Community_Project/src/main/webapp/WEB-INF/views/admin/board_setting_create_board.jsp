<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<style>
	#create_board_form {
		width: 50%;
		margin-top: 50px;
		margin-left: 15%;
		text-align: center;
	}
	#bi_name {
		width: 60%;
	}
</style>
<script>
	$(function() {
		
		$(document).off("click", "#btn_create_board").on("click", "#btn_create_board", function() {
			if($("#bi_name").val() == "") {
				alert("게시판 이름을 입력하세요.")
				return false
			}
			
			$.ajax({
				url: "${rootPath}/admin/board_setting_create_board",
				type: "POST",
				data: $("#create_board_form").serialize(),
				success: function(result) {
					alert("게시판 생성 완료")
					document.location.replace(document.location.href)
				},
				error: function(error) {
					alert("서버 통신 오류")
				}
			})
		})
		
	})
</script>
<form:form id="create_board_form" autocomplete="${FORM_AUTOCOMPLETE}">
	<input type="hidden" />
	<input id="bi_name" name="bi_name" placeholder="게시판 이름" maxlength="100"/>
	<button id="btn_create_board" type="button">게시판 생성</button>
</form:form>