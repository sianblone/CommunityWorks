<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<style>
	form {
		width: 70%;
		margin: 10px auto;
	}
	
	.my_form_item {
		display: flex;
		align-items: center;
		padding: 5px 0;
	}
	
	.my_label {
		display: inline-block;
		text-align: right;
		width: 30%;
		margin-right: 20px;
	}
	
	.my_data {
		padding: 0.5rem 1rem;
		width: 60%;
	}
	
	button.delete {
		position: relative;
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
	#btn_add_category {
		background-color: var(--color-success);
	}
	#btn_edit_board {
		margin-left: auto;
	}
</style>
<script>
	$(function() {
		
		let enable_btn_edit_board = true
		
		$(document).off("click","#btn_add_category").on("click","#btn_add_category",function(){
			let cate_input = "<div class='my_form_item category_box'>"
							+ "	<span class='my_label'>새 카테고리</span>"
							+ "	<input class='my_data' name='cate_id_list' type='hidden' value='0'/>"
							+ "	<input class='my_data' name='cate_delete_list' type='hidden' value='false'/>"
							+ "	<input class='my_data' name='cate_text_list'/>"
							//+ "<button class='delete'>삭제</button>"
							+ "</div>"
			$("div#cate_box").append(cate_input)
		})
		
		$(document).off("click", "#btn_edit_board").on("click", "#btn_edit_board", function() {
			
			if(!enable_btn_edit_board) return false
			// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
			enable_btn_edit_board = false
			$("body").css("cursor", "wait")
			
			/*
			let arrCategory = $(".category_box").map(function() {
				let o = {}
				$(this).find("input").each(function() {
					o[$(this).attr("name")] = this.value
				})
				return o
			}).get()
			console.log(arrCategory)
			*/
			
			let formData = $("#board_setting_details_form").serialize()
			formData += "&bi_id=" + $("#btn_edit_board").data("id")
			
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
	<c:choose>
		<c:when test="${not empty BOARD_INFO.bi_category}">
			<c:forEach items="${BOARD_INFO.bi_category}" var="vo" varStatus="s">
				<div class="ddd">
					<div class="my_form_item category_box">
						<span class="my_label">카테고리${s.count}</span>
						<input class="my_data" name="cate_id_list" type="hidden" value="${vo.cate_id}"/>
						<input class="my_data" name="cate_delete_list" type="hidden" value="false"/>
						<input class="my_data" name="cate_text_list" value="${vo.cate_text}" maxlength="20"/>
					</div>
				</div>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<div class="my_form_item category_box">
				<span class="my_label">새 카테고리</span>
				<input class="my_data" name="cate_id_list" type="hidden" value="0"/>
				<input class="my_data" name="cate_delete_list" type="hidden" value="false"/>
				<input class="my_data" name="cate_text_list" maxlength="20"/>
			</div>
		</c:otherwise>
	</c:choose>
	
	<div id="cate_box">
	</div>
	
	<div class="btn_box">
		<button id="btn_add_category" type="button">카테고리 추가</button>
		<button id="btn_edit_board" type="button" data-id="${BOARD_INFO.bi_id}">수정</button>
	</div>
	
</form:form>