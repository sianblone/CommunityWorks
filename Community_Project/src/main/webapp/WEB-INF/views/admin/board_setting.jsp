<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>

<style>
	.board_info {
		cursor: pointer;
	}
	.board_info:hover {
		background-color: rgba(0, 0, 0, 0.6);
	}
</style>
<script>
	$(function() {
		$(document).on("click", ".board_info", function() {
			$.ajax({
				url: "${rootPath}/admin/board_setting_details",
				type: "GET",
				data: { bi_id : $(this).data("id") }
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
<c:forEach items="${BOARD_INFO_LIST}" var="bi">
	<div class="board_info" data-id="${bi.bi_id}">${bi.bi_id} ${bi.bi_name}</div> 
</c:forEach>
<button>게시판 추가</button>
