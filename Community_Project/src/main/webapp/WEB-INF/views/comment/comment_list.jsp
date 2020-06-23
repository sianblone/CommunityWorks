<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<style>
	.cmt_reply, .cmt_delete {
		cursor: pointer;
	}
	.cmt_reply:hover, .cmt_delete:hover {
		text-decoration: underline;
	}
	.cmt_delete {
		color: red;
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
		<article class="row p-2 bg-light cmt_item" data-id="${C.cmt_no}">
			<div class="cmt_nickname col-2">${C.cmt_nickname}</div>
			<div class="cmt_content col-7">${C.cmt_content}</div>
			<div class="cmt_reply col-1 ml-auto text-center">[답글]</div>
			<div class="cmt_delete col-1 text-center">&times;</div>
		</article>
	</c:forEach>
	<%@ include file="/WEB-INF/views/comment/cmt_pagination.jsp"%>
</section>