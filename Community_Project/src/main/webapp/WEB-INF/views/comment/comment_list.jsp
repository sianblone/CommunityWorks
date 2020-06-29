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
		overflow-wrap: break-word;
		padding: 5px 10px;
	}
	.cmt_writer_box, .cmt_item_group:last-child {
		display: flex;
		flex-direction: column;
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
	.depth-1 {
		margin-left: 20px;
	}
	.cmt_parent_writer {
		color: gray;
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
	
	/* 대댓글 */
	.cmt_write_reply {
		display: none;
	}
	.btn_cmt_reply_save {
		border: 1px solid var(--color-dodgerblue);
		background-color: white;
		color: black;
		padding: 20px 30px;
	}
	.btn_cmt_reply_save:hover {
		background-color: var(--color-dodgerblue);
		color: white;
	}
</style>
<script>
	$(function() {
		let enable_btn_cmt_reply_save = true
		
		$(document).off("click", ".btn_cmt_reply").on("click", ".btn_cmt_reply", function() {
			let cmt_write_reply = $(this).closest(".cmt_list_item").find(".cmt_reply").find(".cmt_write_reply")
			let display = cmt_write_reply.css("display")
			
			if(display == "none") {
				cmt_write_reply.css("display", "block")
			} else {
				cmt_write_reply.css("display", "none")
			}
		})
		
		$(document).off("click", ".btn_cmt_reply_save").on("click", ".btn_cmt_reply_save", function() {
			if(!enable_btn_cmt_reply_save) return false
			
			let comment_reply_form = $(this).closest(".comment_reply_form")
			
			if(comment_reply_form.find(".cmt_reply_csrf").length == 0) {
				if(confirm("로그인 하시겠습니까?")) {
					document.location.href = "${rootPath}/user/login"
					return false
				} else {
					return false
				}
			}
			
			if(comment_reply_form.find(".cmt_reply_content").val() == "") {
				alert("내용을 입력하세요.")
				return false
			}
			
			// 유효성 검사 통과 시
			// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
			enable_btn_cmt_reply_save = false
			$("body").css("cursor", "progress")
			
			$.ajax({
				url: "${rootPath}/comment/save",
				type: "POST",
				data: comment_reply_form.serialize(),
				success: function(result) {
					$(".cmt_list").html(result)
				},
				error: function(error) {
					alert("서버 통신 오류")
				}
			}).always(function() {
				enable_btn_cmt_reply_save = true
				$("body").css("cursor", "default")
			})
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
				<div class="cmt_writer_box<c:if test="${C.cmt_depth > 0}"> depth-1</c:if>">
					<span class="cmt_nickname">${C.cmt_nickname}</span>
					<span class="cmt_datetime">${C.cmt_custom_full_datetime}</span>
				</div>
			</div>
			
			<div class="cmt_item_group">
				<span class="cmt_content"><c:if test="${C.cmt_delete == 1}">[삭제됨] </c:if><c:if test="${C.cmt_depth > 0}"><span
				class="cmt_parent_writer">[${C.cmt_parent_writer}] </span></c:if>${C.cmt_content}</span>
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
			<%@ include file="/WEB-INF/views/comment/comment_write_reply.jsp"%>
		</section>
	</article>
</c:forEach>
<%@ include file="/WEB-INF/views/comment/cmt_pagination.jsp"%>