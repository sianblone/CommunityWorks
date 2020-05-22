<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form"  prefix="form" %>
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
				onImageUpload : function(files, editor, isEdite) {
					for(let i = files.length - 1; i >=0 ; i--) {
						upFile(files[i], this)
					}
				}
			}
		}) // end summer
		
		$("button.btn-success").click(function(){
			document.location.href="${rootPath}/board/list?board_name=gallery"
		})
		
		function upFile(file, editor) {
			
			var formData = new FormData()

			formData.append('upFile',file)
			//formData.append('${_csrf.parameterName}', '${_csrf.token}')
			
			//alert(JSON.stringify(formData))
			
			formData.append('upFile',file)
			$.ajax({
				url : "${rootPath}/board/image_up",
				type : "POST",
				data : formData,
				contentType : false,
				processData : false,

				enctype : "multipart/form-data",
				success:function(result) {
					alert(result)
					result = "${rootPath}/resources/files/" + result
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
			<form:form method="POST">
				
				<div class="form-group">
					<select id="board_category" class="form-control" name="board_category">
						<option value="">카테고리</option>
						<option value="1">잡담</option>
						<option value="2">질문</option>
					</select>
				</div>
				
				<div class="form-group">
					<input name="board_subject"  class="form-control" 
						placeholder="제목" value="${BOARD_VO.board_subject}">
				</div>
				
				<div class="form-group">
					<textarea name="board_content" id="board_content" rows="5" cols="30">${BOARD_VO.board_content}</textarea>
				</div>
				
				<div class="form-group d-flex justify-content-end">
					<button class="btn btn-primary mr-2">저장</button>
					<button type="button" 
						class="btn btn-success">목록으로</button>
				</div>
			</form:form>
		</fieldset>
	</section>
</body>
</html>