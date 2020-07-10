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
	
	.btn_box {
		display: flex;
	}
	
	.btn_box button {
		width: 120px;
		margin-top: 20px;
		padding: 10px;
	}
	
	#btn_edit {
		margin-left: auto;
	}
</style>
<script>
	$(function() {
		
		let enable_btn_edit = true
		
		$(document).off("click", "#btn_edit").on("click", "#btn_edit", function() {
			
			if(!enable_btn_edit) return false
			
			let formData = $("#main_page_setting_form").serialize()
			
			$.ajax({
				url : "${rootPath}/admin/main_page_setting",
				type : "POST",
				data : formData,
				beforeSend : function() {
					// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
					enable_btn_edit = false
				},
				success : function(result) {
					alert("변경사항이 저장되었습니다.")
					document.location.replace(document.location.href)
				},
				error : function() {
					alert("서버 통신 오류")
				}
			}).always(function() {
				enable_btn_edit = true
			})
			
		})
		
	})
</script>
<form:form id="main_page_setting_form" autocomplete="${FORM_AUTOCOMPLETE}">
	<input type="hidden"/>
	<div class="my_form_item">
		<span class="my_label">메인페이지 게시판<br/>최신 글 수</span>
		<input id="page_data_cnt" class="my_data" name="page_data_cnt" value="${PAGE_DTO.page_data_cnt}" maxlength="8" type="number"/>
	</div>
	
	<div class="btn_box">
		<button id="btn_edit" class="btn_confirm mg-10px" type="button">수정</button>
	</div>
	
</form:form>