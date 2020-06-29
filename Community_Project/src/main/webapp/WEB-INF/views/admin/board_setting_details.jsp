<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<style>
	form {
		width: 100%;
		margin: 0px auto;
	}
	
	.my_form_item {
		display: flex;
		align-items: center;
		padding: 5px 0px;
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
	
	.new_category span, .new_category select {
		color: var(--color-dodgerblue);
	}
	
	.cancel_category, .delete_category {
		margin-left: 10px;
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
	#btn_edit_board {
		margin-left: auto;
	}
</style>
<script>
	$(function() {
		
		let enable_btn_edit_board = true
		
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
			// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
			enable_btn_edit_board = false
			$("body").css("cursor", "wait")
			
			let formData = $("#board_setting_details_form").serialize()
			formData += "&bi_id=" + $("#btn_edit_board").attr("data-id")
			
			$.ajax({
				url : "${rootPath}/admin/board_setting_details",
				type : "POST",
				data : formData,
				success : function(result) {
					if(result == -100) {
						alert("게시판 이름은 20글자 이내여야 합니다.")
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
		
	})
</script>
<form:form id="board_setting_details_form" autocomplete="${FORM_AUTOCOMPLETE}">
	
	<div class="my_form_item">
		<span class="my_label">게시판 ID</span>
		<span class="my_data">${BOARD_INFO.bi_id}</span>
	</div>
	
	<div class="my_form_item">
		<span class="my_label">게시판 이름</span>
		<input id="bi_name" class="my_data" name="bi_name" value="${BOARD_INFO.bi_name}" maxlength="20"/>
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
		<button id="btn_edit_board" type="button" data-id="${BOARD_INFO.bi_id}">수정</button>
	</div>
	
</form:form>