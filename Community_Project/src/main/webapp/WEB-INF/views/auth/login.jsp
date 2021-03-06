<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/include/include_head.jspf" %>
	<style>
		.login_form {
			display: flex;
			flex-direction: column;
			width: 420px;
			height: 445px;
			margin: 0 auto;
		}
		#message {
			display: block;
			margin: 0px 0px 10px 0px;
			text-align: center;
			font-weight: bold;
			color: var(--color-danger)
		}
		#message:before {
			content: "\200b";
		}
		.form_item {
			margin-bottom: 10px;
		}
		.form_item label {
			color: var(--label-text-color);
			font-weight: 700;
		}
		.form_item input {
			box-sizing: border-box;
			display: block;
			width: 100%;
			background-color: var(--input-bg-color);
			/* border: var(--border-width-input) solid var(--border-color-input); */
			border: none;
			line-height: 1.5;
		}
		.btn_box {
			display: flex;
			justify-content: space-around;
		}
		.btn_box > a {
			flex: 1 1 0;
			display: flex;
			margin: 20px;
		}
		.btn_box > a button {
			flex-grow: 1;
		}
		
	</style>
	<script>
		$(function() {
			$("#username").focus()
		})
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/include_nav.jspf" %>
	<!-- Spring Security는 보통 Spring Form(form:form) 태그와 연계하여 사용한다 -->
	<header>
		<h2><a class="header_item">로그인</a></h2>
	</header>
	
	<form:form class="login_form" action="${rootPath}/login" method="POST" autocomplete="${FORM_AUTOCOMPLETE}">
		<div>
			<span id="message">
				<c:if test="${not empty param['error']}">
					<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
					<c:remove var = "SPRING_SECURITY_LAST_EXCEPTION" scope = "session" />
				</c:if>
			</span>
		</div>
		
		<div class="form_item">
			<label for="username">ID</label>
		</div>
		
		<div class="form_item">
			<input id="username" name="username" />
		</div>
		
		<div class="form_item">
			<label for="password">비밀번호</label><br/>
		</div>
		
		<div class="form_item">
			<input id="password" name="password" type="password" >
		</div>
		
		<div class="form_item btn_box">
			<a class="rm_deco_hover"><button id="btn_login" class="btn_confirm">로그인</button></a>
			<a class="rm_deco_hover" href="${rootPath}/user/find-id"><button id="btn_find" class="btn_confirm" type="button">ID/비밀번호 찾기</button></a>
			<a class="rm_deco_hover" href="${rootPath}/join"><button id="btn_join" class="btn_confirm" type="button">회원가입</button></a>
		</div>
	</form:form>
</body>
</html>