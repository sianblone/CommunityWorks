<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>

<form:form>
	<input name="bi_id" value="${BOARD_INFO.bi_id}">
	<input name="bi_name" value="${BOARD_INFO.bi_name}">
	<c:forEach items="bi_category" var="c">
		<input name="${c.cate_text}">
	</c:forEach>
</form:form>