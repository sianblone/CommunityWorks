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
		width: 17%;
	}
	.cmt_item_group:nth-child(2) {
		width: 83%;
	}
	
	.cmt_item_btn_box {
		margin: 5px 10px 10px 10px;
		text-align: right;
	}
	.cmt_item_btn_box button {
		padding: 0.2rem 0.5rem;
	}
	.btn_cmt_delete {
		color: red;
	}
	
	.depth1 {
		margin-left: 50px;
	}
	.cmt_parent_writer {
		color: gray;
	}
	.cmt_datetime {
		font-size: 11px;
	}
	.deleted .cmt_item_box {
		color: gray;
	}
	
	.cmt_form_storage {
		display: none;
	}
	
	/* 대댓글 comment_form CSS */
	.cmt_form_box {
		margin: 10px 0px;
	}
	.btn_cmt_cancel {
		margin-right: 10px;
	}
</style>
<script>
	$(function() {
		let enable_btn_cmt_save = true
		let enable_btn_cmt_delete = true
		let enable_btn_cmt_edit = true
		
		let last_parent_cmt_no
		
		// 댓글 답글 버튼
		$(document).off("click", ".btn_cmt_reply").on("click", ".btn_cmt_reply", function() {
			let cmt_list_item = $(this).closest(".cmt_list_item")
			let parent_cmt_no = cmt_list_item.attr("data-id")// 댓글 번호
			let parent_cmt_board_no = cmt_list_item.attr("data-board-no")// 게시글 번호
			
			let cmt_form_box = $(".cmt_form_box")// 대댓글 form box
			
			// 1. 같은 부모댓글에서 답글 두 번 클릭 시 댓글 form 돌려보내기
			if(parent_cmt_no == last_parent_cmt_no) {
				cmt_form_box.appendTo(".cmt_form_storage")// 대댓글 원래 위치
				
				// 마지막 부모댓글 번호 없애기 
				last_parent_cmt_no = null
				return false
			}
			
			// 2. input[name='cmt_no']에 값이 0이 아니면(수정창에서 넘어온 댓글이면)
			let input_cmt_no = cmt_form_box.find("input[name='cmt_no']").val()
			if(input_cmt_no != 0) {
				// 2-1. 0으로 만들기
				cmt_form_box.find("input[name='cmt_no']").val(0)
				// 2-2. 댓글 내용 지우기
				cmt_form_box.find(".cmt_content").val("")
				// 2-3. 부모댓글 display: block
				$(".cmt_item_display").css("display", "block")
			}
			
			// 3. 대댓글 form value 세팅
			cmt_form_box.find("input[name='cmt_board_no']").val(parent_cmt_board_no)
			cmt_form_box.find("input[name='cmt_p_no']").val(parent_cmt_no)
			
			// 2-1. 부모댓글에서 답글 클릭 시 대댓글 form 붙이기
			let cmt_reply_box = cmt_list_item.find(".cmt_reply_box")// 대댓글 붙일 위치
			cmt_form_box.appendTo(cmt_reply_box)
			
			// 2-2. 마지막 부모댓글 번호 저장
			last_parent_cmt_no = parent_cmt_no
			
		})
		
		// 댓글 수정 버튼
		$(document).off("click", ".btn_cmt_edit").on("click", ".btn_cmt_edit", function() {
			let cmt_list_item = $(this).closest(".cmt_list_item")
			let parent_cmt_no = cmt_list_item.attr("data-id")// 댓글 번호
			let parent_cmt_board_no = cmt_list_item.attr("data-board-no")// 게시글 번호
			let parent_cmt_content = cmt_list_item.find(".cmt_content").text()
			
			let cmt_form_box = $(".cmt_form_box")// 수정 form box
			
			// 1. 수정 form value 세팅
			cmt_form_box.find("input[name='cmt_no']").val(parent_cmt_no)
			cmt_form_box.find("input[name='cmt_board_no']").val(parent_cmt_board_no)
			cmt_form_box.find("input[name='cmt_p_no']").val(0)
			cmt_form_box.find(".cmt_content").val(parent_cmt_content)
			
			// 2. 부모댓글 display: block
			$(".cmt_item_display").css("display", "block")
			
			// 3. 수정 버튼 클릭 시 부모댓글 display: none으로 변경
			cmt_list_item.find(".cmt_item_display").css("display", "none")
			
			// 4. 수정 form 붙이기
			let cmt_edit_box = cmt_list_item.find(".cmt_edit_box")// 수정 form 붙일 위치
			cmt_form_box.appendTo(cmt_edit_box)
			
			// 5. 마지막 부모댓글 번호 없애기
			last_parent_cmt_no = null
		})
		
		// 댓글 취소 버튼
		$(document).off("click", ".btn_cmt_cancel").on("click", ".btn_cmt_cancel", function() {
			let cmt_form_box = $(".cmt_form_box")
			cmt_form_box.appendTo(".cmt_form_storage")// 대댓글/수정 form 원래 위치
			
			// 1. 대댓글/수정 form value 0으로 만들기
			cmt_form_box.find("input[name='cmt_no']").val(0)
			cmt_form_box.find("input[name='cmt_board_no']").val(0)
			cmt_form_box.find("input[name='cmt_p_no']").val(0)
			
			// 2. 댓글 내용 지우기
			cmt_form_box.find(".cmt_content").val("")
			
			// 3. 부모댓글 display: block
			$(".cmt_item_display").css("display", "block")
			
			// 4. 마지막 부모댓글 번호 없애기
			last_parent_cmt_no = null
			
			return false
		})
		
		// 대댓글/수정 등록 버튼
		$(document).off("click", ".btn_cmt_save").on("click", ".btn_cmt_save", function() {
			if(!enable_btn_cmt_save) return false
			
			let cmt_form = $(this).closest(".cmt_form")
			
			if(cmt_form.find(".cmt_csrf").length == 0) {
				if(confirm("로그인하시겠습니까?")) {
					document.location.href = "${rootPath}/user/login"
					return false
				} else {
					return false
				}
			}
			
			if(cmt_form.find(".cmt_content").val() == "") {
				alert("내용을 입력하세요.")
				return false
			}
			
			
			$.ajax({
				url: "${rootPath}/comment/save",
				type: "POST",
				data: cmt_form.serialize(),
				beforeSend: function(ajx) {
					// 유효성 검사 통과 시
					// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
					enable_btn_cmt_save = false
				},
				success: function(result) {
					cmt_form.find(".cmt_content").val("")
					$(".cmt_list").html(result)
				},
				error: function(error) {
					alert("서버 통신 오류")
				}
			}).always(function() {
				enable_btn_cmt_save = true
			})
		})
		
		$(document).off("click", ".cmt_content_unauth").on("click", ".cmt_content_unauth", function() {
			if(confirm("로그인하시겠습니까?")) {
				document.location.href = "${rootPath}/user/login"
			}
		})
		
		$(document).off("click", ".btn_cmt_delete").on("click", ".btn_cmt_delete", function() {
			if(!enable_btn_cmt_delete) return false
			
			if(confirm("정말 삭제하시겠습니까?")) {
				let cmt_no = $(this).closest(".cmt_list_item").attr("data-id")
				let cmt_board_no = $(this).closest(".cmt_list_item").attr("data-board-no")
				
				$.ajax({
					url: "${rootPath}/comment/delete",
					type: "POST",
					beforeSend: function(ajx) {
						ajx.setRequestHeader("${_csrf.headerName}", "${_csrf.token}")
						// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
						enable_btn_cmt_delete = false
					},
					data: {
						cmt_no: cmt_no,
						cmt_board_no: cmt_board_no,
						pageNo: "${PAGE_DTO.pageNo}"
					},
					success: function(result) {
						$(".cmt_list").html(result)
					},
					error: function(error) {
						alert("서버 통신 오류")
					}
				}).always(function() {
					enable_btn_cmt_delete = true
				})
			}
		})
	})
</script>

<article class="cmt_list_label">
	댓글<span class="cmt_count"> ${CMT_TOTAL}</span>
</article>
<c:forEach items="${CMT_LIST}" var="C">
	<article class="cmt_list_item context_parent<c:if test="${C.cmt_delete == 1}"> deleted</c:if>"
	data-id="${C.cmt_no}" data-board-no="${C.cmt_board_no}" data-nickname="${C.cmt_nickname}">
		<section class="cmt_item_display">
			<article class="cmt_item_box<c:if test="${C.cmt_depth > 0}"> depth1</c:if>">
				<div class="cmt_item_group">
					<div class="cmt_writer_box">
						<span class="use_context context_nickname">${C.cmt_nickname}</span>
						<span class="cmt_datetime">${C.cmt_custom_full_datetime}</span>
					</div>
				</div>
				
				<div class="cmt_item_group">
					<span><c:if test="${C.cmt_delete == 1}">[삭제됨] </c:if><c:if test="${C.cmt_depth > 1}"><span
					class="cmt_parent_writer">[${C.cmt_parent_nickname}] </span></c:if><span class="cmt_content">${C.cmt_content}</span></span>
				</div>
			</article>
			
			<article class="cmt_item_btn_box">
				<c:if test="${C.viewerAdmin || C.viewerWriter}">
					<button	class="btn_cmt_delete btn_red">&times;</button>
					<button class="btn_cmt_edit btn_blue">수정</button>
				</c:if>
				<button class="btn_cmt_reply btn_blue">답글</button>
			</article>
			
			<article class="cmt_reply_box">
				
			</article>
		</section>
		<section class="cmt_edit_box">
		
		</section>
	</article>
</c:forEach>
<article class="cmt_form_storage">
	<section class="cmt_form_box">
		<%@ include file="/WEB-INF/views/comment/comment_form.jsp"%>
	</section>
</article>

<%@ include file="/WEB-INF/views/comment/cmt_pagination.jsp"%>

<section class="cmt_write">
	<%@ include file="/WEB-INF/views/comment/comment_write.jsp"%>
</section>