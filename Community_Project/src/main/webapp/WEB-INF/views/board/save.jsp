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
		
		var toolbar = [
			['style',['bold','italic','underline'] ],
			['fontsize',['fontsize']],
			['font Style',['fontname']],
			['color',['color']],
			['para',['ul','ol','paragraph']],
			['height',['height']],
			['table',['table']],
			['insert',['link','hr','picture']],
			['view',['fullscreen','codeview']]
			
		]
		
		$("#board_content").summernote({
			lang:'ko-KR',
			placeholder:'본문을 입력하세요',
			width:'100%',
			toolbar:toolbar,
			height:'200px',
			disableDragAndDrop : false,
			callbacks : {
				onImageUpload : function(files,editor,isEdite) {
					for(let i = files.length - 1; i >=0 ; i--) {
						upFile(files[i],this)
					}
				}
			}
		}) // end summer
		
		function upFile(file,editor) {
			
			var formData = new FormData()
		
			formData.append('upFile',file)
			$.ajax({
				url : "${rootPath}/image_up",
				type : "POST",
				data : formData,
				contentType : false,
				processData : false,
				enctype : "multipart/form-data",
				success:function(result) {
					alert(result)
					result = "${rootPath}/files/" + result
					$(editor)
						.summernote('editor.insertImage',result)
				},
				error:function() {
					alert("서버통신오류")
				}
			})
		}
	})
	</script>
</head>

<body>
<%@ include file="/WEB-INF/views/include/include_nav.jspf" %>
	<section class="container-fluid">
		<fieldset>
			<form method="POST">
			<div class="form-group">
				<input name="board_writer" class="form-control" 
						placeholder="작성자" value="${B.board_writer}">
			</div>
			<div class="form-group">
				<input name="board_subject"  class="form-control" 
					placeholder="제목" value="${B.board_subject}">
			</div>
			<div class="form-group">
				<textarea name="board_content" id="board_content" rows="5" cols="30">${B.board_content}</textarea>
			</div>
			<div class="form-group d-flex justify-content-end">
				<button class="btn btn-primary mr-2">저장</button>
				<button type="button" 
					class="btn btn-success">목록으로</button>
			</div>
			</form>
		</fieldset>
	</section>
</body>
</html>