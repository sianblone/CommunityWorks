<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/include/include_head.jspf" %>
	<link rel="stylesheet" href="${rootPath}/resources/css/join.css"/>
	<script src="${rootPath}/resources/js/join_valid.js"></script>
	<script>
		let rootPath = "${rootPath}"
	</script>
	<script src="${rootPath}/resources/js/join.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/include_nav.jspf" %>
	<header>
		<h2><a class="header_item">회원가입</a></h2>
	</header>
	<form:form id="join-form" action="${rootPath}/join/join" method="POST" autocomplete="${FORM_AUTOCOMPLETE}" onSubmit="return false">
		<div class="form_item">
			<label for="username">ID</label>
		</div>
		
		<div class="form_item">
			<input id="username" name="username"/>
			<span id="m_username" class="message">&#8203;</span>
		</div>
		
		<div class="form_item">
			<label for="nickname">닉네임</label><br/>
		</div>
		
		<div class="form_item">
			<input id="nickname" name="nickname" />
			<span id="m_nickname" class="message">* 입력하지 않으면 아이디와 똑같이 설정되며 추후 수정할 수 있습니다.</span>
		</div>
		
		<div class="form_item">
			<label for="password">비밀번호</label><br/>
		</div>
		
		<div class="form_item">
			<input id="password" name="password" type="password" />
		</div>
		
		<div class="form_item">
			<label for="re_password">비밀번호 확인</label><br/>
		</div>
		
		<div class="form_item">
			<input id="re_password" name="re_password" type="password" >
		</div>
		
		<div class="form_item">
			<label for="email">이메일</label><br/>
		</div>
		
		<div class="form_item">
			<input id="email" name="email" type="email"/>
			<span id="m_email" class="message">* 해당 이메일로 인증 링크가 발송됩니다.</span>
		</div>
		
		<div class="form_item">
			<label for="phone">핸드폰</label><br/>
		</div>
		
		<div class="form_item">
			<input type="tel" id="phone" name="phone" />
		</div>
		
		<div class="form_item">
			<label for="year">생년월일</label><br/>
		</div>
		
		<div class="form_item birth">
			<input id="year" name="year" placeholder="년" maxlength="4"/>
			<select id="month" name="month">
				<option value="">월</option>
				<c:forEach begin="1" end="12" var="count">
					<option value="${count}">${count}월</option>
				</c:forEach>
			</select>
			<input id="day" name="day" placeholder="일" maxlength="2"/>
		</div>
		
		<div class="form_item btn_box">
			<button id="btn_join" class="btn_confirm" type="button">회원가입</button>
		</div>
	</form:form>
</body>
</html>