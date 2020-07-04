<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<style>
	/* toggle CSS */
	/* The switch - the box around the slider */
	.switch {
	  position: relative;
	  display: inline-block;
	  width: 60px;
	  height: 26px;
	  margin: auto;
	  vertical-align:middle;
	}
	
	/* Hide default HTML checkbox */
	.switch input {display:none;}
	
	/* The slider */
	.slider {
	  position: absolute;
	  cursor: pointer;
	  top: 0;
	  left: 0;
	  right: 0;
	  bottom: 0;
	  background-color: #ccc;
	  -webkit-transition: .4s;
	  transition: .4s;
	}
	
	.slider:before {
	  position: absolute;
	  content: "";
	  height: 20px;
	  width: 20px;
	  left: 3px;
	  bottom: 3px;
	  background-color: white;
	  -webkit-transition: .4s;
	  transition: .4s;
	}
	/* toggle CSS End */
	
	input:checked + .slider {
	  background-color: #2196F3;
	}
	
	input:focus + .slider {
	  box-shadow: 0 0 1px #2196F3;
	}
	
	input:checked + .slider:before {
	  -webkit-transform: translateX(34px);
	  -ms-transform: translateX(34px);
	  transform: translateX(34px);
	}
	
	/* Rounded sliders */
	.slider.round {
	  border-radius: 26px;
	}
	
	.slider.round:before {
	  border-radius: 50%;
	}
	
	p {
		margin:0px;
		display:inline-block;
		font-size:15px;
		font-weight:bold;
	}
</style>
<style>
	form {
		width: 100%;
		margin: 0px auto;
	}
	
	.my_form_item, .switch {
		display: flex;
		align-items: center;
		padding: 5px 0px;
	}
	.switch {
		margin-left: 5px;
	}
	
	.my_label {
		display: inline-block;
		text-align: right;
		width: 20%;
		margin-right: 20px;
	}
	.my_data {
		padding: 0.5rem 1rem;
		width: 60%;
	}
	
	.btn_box {
		display: flex;
	}
	
	.btn_box button {
		display: block;
		width: 120px;
		padding: 10px;
		margin-top: 20px;
	}
	
	.new_category span, .new_category select {
		color: var(--color-dodgerblue);
	}
	
	.cancel_category, .delete_category {
		margin-left: 10px;
	}
	
	#btn_edit_board {
		margin-left: auto;
	}
</style>
<script>
	$(function() {
		
		let enable_btn_edit_board = true
		let enable_btn_delete_board = true
		
		$(document).off("click","#btn_add_category").on("click","#btn_add_category",function(){
			let cate_input = "<div class='my_form_item category_box new_category'>"
							+ "	<span class='my_label'>새 카테고리</span>"
							+ "	<input class='my_data' name='cate_id_list' type='hidden' value='0'/>"
							+ "	<input class='my_data' name='cate_text_list'/>"
							+ " <button class='cancel_category btn_blue' type='button'>취소</button>"
							+ "</div>"
			$("#cate_box").append(cate_input)
		})
		
		$(document).off("click", ".cancel_category").on("click",".cancel_category",function() {
			$(this).closest(".new_category").remove()
		})
		
		$(document).off("click", ".delete_category").on("click",".delete_category",function() {
			let cate_box = $(this).closest(".category_box")
			let cate_id = cate_box.attr("data-id")
			
			if(confirm("카테고리를 정말 삭제하시겠습니까?\n관련 게시글의 카테고리는 기본값으로 변경됩니다.")) {
				$.ajax({
					url: "${rootPath}/admin/delete_category",
					type: "POST",
					data: {cate_id : cate_id},
					beforeSend: function(ajx) {
						ajx.setRequestHeader("${_csrf.headerName}", "${_csrf.token}")
					},
					success: function(result) {
						if(result > 0) {
							alert("카테고리를 삭제했습니다.")
							cate_box.closest(".category_box").remove()
						} else {
							alert("카테고리 삭제에 실패했습니다.")
						}
					},
					error: function(error) {
						alert("서버 통신 오류")
					}
				})
			}
		})
		
		$(document).off("click", "#btn_edit_board").on("click", "#btn_edit_board", function() {
			
			if(!enable_btn_edit_board) return false
			
			let formData = $("#board_setting_details_form").serialize()
			formData += "&bi_id=${BOARD_INFO.bi_id}"
			
			$.ajax({
				url : "${rootPath}/admin/board_setting_details",
				type : "POST",
				data : formData,
				beforeSend : function() {
					// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
					enable_btn_edit_board = false
					$("body").css("cursor", "wait")
				},
				success : function(result) {
					if(result == -100) {
						alert("게시판 이름은 100글자 이내여야 합니다.")
					} else if(result == -101) {
						alert("카테고리는 20글자 이내여야 합니다.")
					} else if(result == -200) {
						alert("등록되지 않은 게시판 ID입니다.")
					} else {
						alert("변경사항이 저장되었습니다.")
						document.location.replace(document.location.href)
					}
				},
				error : function() {
					alert("서버 통신 오류")
				}
			}).always(function() {
				enable_btn_edit_board = true
				$("body").css("cursor", "default")
			})
			
		})
		
		$(document).off("click", "#btn_delete_board").on("click", "#btn_delete_board", function() {
			
			if(!enable_btn_delete_board) return false
			
			if(confirm("해당 게시판의 모든 게시물이 삭제됩니다.\n정말로 게시판을 삭제하시겠습니까?")) {
				
				let bi_id = $(this).attr("data-id")
				$.ajax({
					url: "${rootPath}/admin/delete_board",
					type: "POST",
					data: { bi_id : bi_id },
					beforeSend: function(ajx) {
						ajx.setRequestHeader("${_csrf.headerName}", "${_csrf.token}")
						// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
						enable_btn_delete_board = false
						$("body").css("cursor", "wait")
					},
					success: function(result) {
						alert("게시판이 삭제되었습니다.")
						document.location.replace(document.location.href)
					},
					error: function(error) {
						alert("서버 통신 오류")
					}
				}).always(function() {
				enable_btn_delete_board = true
				$("body").css("cursor", "default")
			})
			}
		})
		
	})
</script>
<form:form id="board_setting_details_form" autocomplete="${FORM_AUTOCOMPLETE}">
	
	<div class="my_form_item">
		<span class="my_label">게시판 ID</span>
		<span class="my_data">${BOARD_INFO.bi_id}</span>
	</div>
	
	<div class="my_form_item">
		<span class="my_label">활성여부</span>
		<label class="switch">
			<input type="checkbox" name="bi_enabled" <c:if test="${BOARD_INFO.bi_enabled}">checked</c:if>/>
			<span class="slider round"></span>
		</label>
	</div>
	
	<div class="my_form_item">
		<span class="my_label">게시판 이름</span>
		<input id="bi_name" class="my_data" name="bi_name" value="${BOARD_INFO.bi_name}" maxlength="100"/>
	</div>
	
	<div class="my_form_item">
		<span class="my_label">게시판 페이지 글 수</span>
		<input id="data_cnt_board" class="my_data" name="data_cnt_board" value="${BOARD_INFO.data_cnt_board}" maxlength="8" type="number"/>
	</div>
	
	<div class="my_form_item">
		<span class="my_label">게시판 페이지 범위</span>
		<input id="page_range_board" class="my_data" name="page_range_board" value="${BOARD_INFO.page_range_board}" maxlength="8" type="number"/>
	</div>
	
	<div class="my_form_item">
		<span class="my_label">댓글 페이지 댓글 수</span>
		<input id="data_cnt_comment" class="my_data" name="data_cnt_comment" value="${BOARD_INFO.data_cnt_comment}" maxlength="8" type="number"/>
	</div>
	
	<div class="my_form_item">
		<span class="my_label">댓글 페이지 범위</span>
		<input id="page_range_comment" class="my_data" name="page_range_comment" value="${BOARD_INFO.page_range_comment}" maxlength="8" type="number"/>
	</div>
	
	<!-- 카테고리 영역 -->
	<c:if test="${not empty BOARD_INFO.bi_category}">
		<c:forEach items="${BOARD_INFO.bi_category}" var="cate" varStatus="s">
			<div class="my_form_item category_box" data-id="${cate.cate_id}">
				<span class="my_label">카테고리${s.count}</span>
				<input class="my_data" name="cate_id_list" type="hidden" value="${cate.cate_id}"/>
				<input class="my_data" name="cate_text_list" value="${cate.cate_text}" maxlength="20"/>
				<button class="delete_category btn_red" type="button">삭제</button>
			</div>
		</c:forEach>
	</c:if>
	
	<div id="cate_box">
	</div>
	
	<div id="add_category_box" class="my_form_item">
		<span class="my_label"></span>
		<button id="btn_add_category" class="my_data btn_blue" type="button">카테고리 추가</button>
	</div>
	
	<div class="btn_box">
		<button id="btn_delete_board" class="btn_red" type="button" data-id="${BOARD_INFO.bi_id}">삭제</button>
		<button id="btn_edit_board" type="button">수정</button>
	</div>
	
</form:form>