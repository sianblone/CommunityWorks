<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:forEach items="${BOARD_LIST}" var="B">
	<div>${B.board_name}</div> 
</c:forEach>
