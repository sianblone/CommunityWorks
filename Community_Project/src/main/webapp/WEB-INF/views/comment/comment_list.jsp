<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>

<style>
	.cmt_list_label {
		margin-bottom: 10px;
		padding: 5px 10px;
		border-bottom: 2px solid rgba(0, 0, 0, 0.1);
		font-weight: bold;
	}
	.cmt_list_label .cmt_count {
		color: var(--color-primary);
	}
	.cmt_list_item {
		border-bottom: 1px solid rgba(0, 0, 0, 0.1);
	}
	.cmt_list_item:first-child {
		border-top: 1px solid rgba(0, 0, 0, 0.1);
	}
	.cmt_item_box {
		display: flex;
		justify-content: space-between;
	}
	.cmt_item_group {
		display: flex;
		flex-direction: column;
		overflow-wrap: break-word;
		padding: 5px 10px;
	}
	.cmt_item_group:first-child {
		width: 15%;
	}
	.cmt_item_group:nth-child(2) {
		width: 70%;
	}
	.cmt_item_group:last-child {
		width: 8%;
		text-align: center;
	}
	.cmt_datetime {
		font-size: 11px;
	}
	.btn_cmt_reply, .btn_cmt_delete {
		cursor: pointer;
	}
	.btn_cmt_reply:hover, .btn_cmt_delete:hover {
		text-decoration: underline;
	}
	.btn_cmt_delete {
		color: red;
	}
	.deleted {
		color: gray;
	}
</style>
<script>
	$(function() {
		$(document).off("click", ".btn_cmt_reply").on("click", ".btn_cmt_reply", function() {
			let cmt_no = $(this).closest(".cmt_item_box").attr("data-id")
			
			
		})
		
		$(document).off("click", ".btn_cmt_delete").on("click", ".btn_cmt_delete", function() {
			if(confirm("정말 삭제하시겠습니까?")) {
				let cmt_no = $(this).closest(".cmt_item_box").attr("data-id")
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
					error: function(error) {
						alert("서버 통신 오류")
					}
				})
			}
		})
	})
</script>

<article class="cmt_list_label">
	댓글<span class="cmt_count"> ${CMT_TOTAL}</span>
</article>
<c:forEach items="${CMT_LIST}" var="C">
	<article class="cmt_list_item">
		<section class="cmt_item_box <c:if test="${C.cmt_delete == 1}">deleted</c:if>" data-id="${C.cmt_no}">
			<div class="cmt_item_group">
				<span class="cmt_nickname">${C.cmt_nickname}</span>
				<span class="cmt_datetime">${C.cmt_custom_full_datetime}</span>
			</div>
			
			<div class="cmt_item_group">
				<span class="cmt_content"><c:if test="${C.cmt_depth > 0}">└<span class="cmt_p_no">[${C.cmt_p_no}]</span> </c:if><c:if test="${C.cmt_delete == 1}">[삭제됨] </c:if>${C.cmt_content}</span>
			</div>
			
			<div class="cmt_item_group">
				<c:choose>
					<c:when test="${C.viewerAdmin || C.viewerWriter}"><span class="btn_cmt_delete">&times;</span></c:when>
					<c:otherwise><span>&#8203;</span></c:otherwise>
				</c:choose>
				<span class="btn_cmt_reply">[답글]</span>
			</div>
		</section>
		
		<section class="cmt_reply">
		</section>
	</article>
</c:forEach>
<%@ include file="/WEB-INF/views/comment/cmt_pagination.jsp"%>