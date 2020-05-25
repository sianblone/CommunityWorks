<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<form method="POST" class="repl">
	<div class="row p-4 bg-light">
		<input type="hidden" name="cmt_p_no" id="cmt_p_no" value="${C.cmt_p_no}"> 
		<input type="hidden" name="cmt_board_no" value="${C.cmt_board_no}">
		<div class="col-2">
			<input name="cmt_writer" id="cmt_writer" class="form-control"
				placeholder="작성자">
		</div>
		<div class="col-8">
			<input name="cmt_content" id="cmt_content" class="form-control"
				placeholder="댓글을 입력하세요">
		</div>
		<div class="col-2  d-flex justify-content-center">
			<button type="button" class="btn btn-success btn-cmt-save">답변저장</button>
		</div>
	</div>
</form>