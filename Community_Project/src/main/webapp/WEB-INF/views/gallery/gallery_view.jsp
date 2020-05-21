<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/include_head.jspf" %>
<script>
$(function(){
	$(document).on("click","cmt-item",function(){
		let id = $(this).data("id")
		let writer = $(this).find("div.writer").find("b").text()
		let subject = $(this).find("div.subject").text()
		
		$("#c_id").val(id)
		$("#c_writer").val(writer)
		$("#c_subject").val(subject)
	})
	
	$(document).on("click","div.cmt-item-del",function(event){
		event.stopPropagation()
		if(!confirm("삭제할까요?")) {
			return false
		}
		let c_id = $(this).parent("div").data("id")
		$.ajax({
			url : "${rootPath}/comment/delete/",
			data : {
				c_id : c_id,
				g_id : "${GALLERY.g_id}"
			},
			type : "POST",
			success : function(result) {
				$("div.cmt-list").html(result)
			},
			error : function() {
				alert("서버통신오류")
			}
		})
	})
	
	$(document).on("click",".cmt-item-repl",function(event){
		let g_id = "${GALLERY.g_id}"
		let c_id = $(this).parent("div").data("id")
		let data = {c_g_id:g_id,c_p_id:c_id}
		
		ebent.stopPropagation()
		
		$.get("${rootPath}/comment/repl",data,function(result){
			$(".modal-body").html(result)
			$(".modal-main").css("display","block")
		})
	})
	
	$(document).on("click","button",function(){
		let txt = $(this).text()
		if(txt == '수정') {
			document.location.href="${rootPath}/update?g_id=${GALLERY.g_id}"
		} else if(txt == '삭제') {
			if(confirm("삭제할까요?")) {
				document.location.replace("${rootPath}/delete/${GALLERY.g_id}")
			}
		} else if(txt == '답변저장') {
			var formData = $("form.repl").serialize()
			$.ajax({
				url : "${rootPath}/comment/insert",
				data : formData,
				type : "POST",
				success : function(result) {
					$(".modal-main").css("display","none")
					$("div.cmt-list").html(result)
				},
				error : function() {
					alert("서버통신오류")
				}
			})
		} else if(txt == '저장') {
			var aData = {
					c_writer : $("#c_writer").val(),
					c_subject : $("#c_subject").val()
			}
			var formData = $("form.main").serialize()
			$.ajax({
				url : "${rootPath}/comment/insert",
				data : formData,
				success : function(result) {
					$("div.cmt-list").html(result)
				},
				error : function() {
					alert("서버통신오류")
				}
			})
			return true
		} else if(txt == "답글") {
			document.location.href = "${rootPath}/repl?g_id=${GALLERY.g_id}"
			return false
		} else {
			document.location.href="${rootPath}/list"
		}
	})
})
</script>
</head>
<style>
.cmt-item-del {
	cursor: pointer;
}
</style>
<body>
<%@ include file="/WEB-INF/views/include/include_nav.jspf" %>
	<section class="container-fluid">
		<h2 class="p-3">${GALLERY.g_subject}</h2>
		<div>
			<small class="m-3">${GALLERY.g_writer}</small>
			<small class="m-3">${GALLERY.g_writer}</small>
		</div>
		<hr/>
		<div class="p-3">
			${GALLERY.g_content}
		</div>
	</section>
	<div class="form-group d-flex justify-content-end">
		<button class="btn btn-primary mr-3">수정</button>
		<button class="btn btn-danger mr-3">삭제</button>
		<button class="btn btn-info mr-3">답글</button>
		<button class="btn btn-success">목록으로</button>
	</div>
	<hr/>
	<section class="container-fluid p-4">
		<div class="p-2">
			<b>댓글을 남겨주세요</b>
		</div>
		<form method="POST" class="main">
			<div class="row p-4 bg-light">
				<input type="hidden" name="c_id" id="c_id" value="0">
				<input type="hidden" name="c_g_id" value="${GALLERY.g_id}">
				<div class="col-2">
					<input name="c_writer" id="c_writer"
							class="form-control" placeholder="작성자">
				</div>
				<div class="col-8">
					<input name="c_subject" id="c_subject"
							class="form-control" placeholder="댓글을 입력하세요">
				</div>
				<div class="col-2 d-flex justify-content-center">
					<button type="button" class="btn btn-success btn-cmt-save">저장</button>
				</div>
			</div>
		</form>
		<div class="p-2">
			<b>댓글 리스트</b>
		</div>
		<div class="p-4 cmt-list">
			<%@ include file="/WEB-INF/views/gallery/comment_list.jsp" %>
		</div>

	</section>
</body>
</html>