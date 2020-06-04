<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>

<script>
	$(function() {
		$(document).on("click", "#create_board", function() {
			$.ajax({
				url: "${rootPath}/admin/create_board",
				type: "POST",
				data: $("#create_board_form").serialize(),
				success: function(result) {
					$("#admin_content").html(result)
				},
				error: function(error) {
					alert("서버 통신 오류")
				}
			})
		})
	})
</script>
<form:form id="create_board_form">
	<input type="hidden" />
	<input name="bi_name" placeholder="게시판 이름" />
	<button id="create_board" type="button">게시판 생성</button>
</form:form>