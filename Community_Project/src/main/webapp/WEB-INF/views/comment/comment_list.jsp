<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>

<c:forEach items="${CMT_LIST}" var="C">
	<div class="row p-2 bg-light cmt-item" data-id="${C.cmt_no}">
		<div class="col-2 writer"><b>${C.cmt_writer}</b></div>
		<div class="col-7 subject">${C.cmt_content}</div>
		<div class="col-1 cmt-item-repl"><b>답변</b></div>
		<div class="col-1 cmt-item-del"><b>&times;</b></div>
	</div>
</c:forEach>