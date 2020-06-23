<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<style>
	.cmt_item {
		display: flex;
	}
	.cmt_reply, .cmt_delete {
		cursor: pointer;
	}
	.cmt_reply:hover, .cmt_delete:hover {
		text-decoration: underline;
	}
	.cmt_delete {
		color: red;
	}
	.deleted {
		color: gray;
	}
</style>
<script>
	$(function() {
		$(document).off("click", ".cmt_reply").on("click", ".cmt_reply", function() {
			
		})
		
		$(document).off("click", ".cmt_delete").on("click", ".cmt_delete", function() {
			if(confirm("정말 삭제하시겠습니까?")) {
				let cmt_no = $(this).closest(".cmt_item").attr("data-id")
				$.ajax({
					url: "${rootPath}/comment/delete",
					type: "POST",
					beforeSend: function(ajx) {
						ajx.setRequestHeader("${_csrf.headerName}", "${_csrf.token}")
					},
					data: {
						cmt_no: cmt_no,
						currPage: "${PAGE_DTO.currentPageNo}"
					},
					success: function(result) {
						$(".cmt_list").html(result)
					},
					error: function() {
						alert("서버 통신 오류")
					}
				})
			}
		})
	})
</script>
<section class="cmt_list">
	<c:forEach items="${CMT_LIST}" var="C">
		<article class="cmt_item <c:if test="${C.cmt_delete== 1}">deleted</c:if> row p-2 bg-light" data-id="${C.cmt_no}">
			<div class="cmt_item_group">
				<span class="cmt_nickname">${C.cmt_nickname}</span>
				<span class="cmt_datetime">${C.cmt_custom_full_datetime}</span>
				<span class="cmt_reply">[답글]</span>
			</div>
			
			<div class="cmt_item_group">
				<span class="cmt_content"><c:if test="${C.cmt_depth > 0}">└<span class="cmt_p_no">[${C.cmt_p_no}]</span> </c:if><c:if test="${C.cmt_delete == 1}">[삭제됨] </c:if>${C.cmt_content}</span>
			</div>
			
			<div class="cmt_item_group">
				<span class="cmt_delete">&times;</span>
			</div>
		</article>
	</c:forEach>
	<%@ include file="/WEB-INF/views/comment/cmt_pagination.jsp"%>
</section>