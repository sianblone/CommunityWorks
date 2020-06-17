<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<form:form id="create_board_form">
	<input type="hidden" />
	<input id="bi_name" name="bi_name" placeholder="게시판 이름" />
	<button id="btn_create_board" type="button">게시판 생성</button>
</form:form>