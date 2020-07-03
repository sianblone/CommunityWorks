<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form"  prefix="form" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/include_head.jspf" %>
<style>
	header {
		cursor: pointer;
	}
	#board_category {
		width: auto;
	}
	.save_btn_box {
		text-align: right;
	}
	.save_btn_box button {
		margin-right: 10px;
	}
</style>
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
		
		function upFile(file, editor) {
			var formData = new FormData()

			formData.append('upFile', file)
			
			$.ajax({

				url: "${rootPath}/board/image_up",
				type: "POST",
				data: formData,
				contentType: false,
				processData: false,
				enctype: "multipart/form-data",
				beforeSend: function(ajx) {
					ajx.setRequestHeader("${_csrf.headerName}", "${_csrf.token}")
				},
				success: function(result) {
					result = "${rootPath}/files/" + result
					$(editor).summernote('editor.insertImage', result)
				},
				error: function() {
					alert("서버 통신 오류")
				}
			})
		}
		
		$("#board_content").summernote({
			lang: 'ko-KR',
			placeholder: '본문을 입력하세요',
			width: '100%',
			toolbar: toolbar,
			height: '200px',
			disableDragAndDrop: false,
			callbacks: {
				onImageUpload: function(files, editor, isEdit) {
					for(let i = files.length - 1; i >=0 ; i--) {
						upFile(files[i], this)
					}
				}
			}
		})
		
		$("#board_subject").focus()
		
		$("#save_form").submit(function() {
			let subject = $("#board_subject")
			let content= $("#board_content")
			
			if(subject.val() == "") {
				alert("제목을 입력하세요.")
				subject.focus()
				return false
			} else if (content.val() == "") {
				alert("내용을 입력하세요.")
				$(".note-editable").focus()
				return false
			}
			
		})
		
		$("#btn_cancel").click(function() {
			if(confirm("작성을 취소하고 목록으로 돌아가시겠습니까?")) {
				document.location.href="${rootPath}/board/list?board_info=${BOARD_INFO.bi_id}"
			}
		})
		
		$("header").click(function() {
			document.location.href = "${rootPath}/board/list?board_info=${BOARD_INFO.bi_id}"
		})
		
	})
	</script>
</head>

<body>
	<%@ include file="/WEB-INF/views/include/include_nav.jspf" %>
	<header>
		<h2>${BOARD_INFO.bi_name}</h2>
	</header>
	<main>
		<form:form id="save_form" method="POST" enctype="multipart/form-data" action="${rootPath}/board/save" autocomplete="${FORM_AUTOCOMPLETE}">
			<fieldset>
				<input type="hidden" name="board_info" value="<c:out value='${BOARD_INFO.bi_id}' default='0'/>"/>
				<input type="hidden" name="board_no" value="<c:out value='${param.board_no}' default='0'/>"/>
				<input type="hidden" name="board_p_no" value="<c:out value='${param.board_p_no}' default='0'/>"/>
				<input type="hidden" name="pageNo" value="${param.pageNo}"/>
				<div class="form-group">
					<select id="board_category" class="form-control" name="board_category">
						<option value="0">카테고리</option>
						<c:forEach items="${CATEGORY_LIST}" var="c">
							<option value="${c.cate_id}">${c.cate_text}</option>
						</c:forEach>
					</select>
				</div>
				
				<div class="form-group">
					<input id="board_subject" name="board_subject" class="form-control" placeholder="제목" value="${BOARD_VO.board_subject}">
				</div>
				
				<div class="form-group">
					<textarea id="board_content" name="board_content" id="board_content" rows="5" cols="30">${BOARD_VO.board_content}</textarea>
				</div>
				
				<div class="form-group save_btn_box">
					<button id="btn_cancel" class="btn_red" type="button">취소</button>
					<button id="btn_save" class="btn_blue">등록</button>
				</div>
			</fieldset>
		</form:form>
	</main>
	<%@ include file="/WEB-INF/views/include/include_footer.jsp" %>
</body>
</html>