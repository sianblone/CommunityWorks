<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="sec"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/include_head.jspf"%>
<style>
	#email_auth {
		position: absolute;
		top: 58px;
		width: 1024px;
		padding: 20px;
		left: 50%;
		transform: translateX(-50%);
		text-align: center;
		background-color: black;
		color: white;
		font-weight: bold;
		font-size: large;
	}
</style>
<script>
	$(function() {
		setTimeout(function() {
			$("#email_auth").css("display", "none")
		}, 3000)
	})
</script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/include_nav.jspf"%>
	<c:if test="${EMAIL_AUTH}">
		<div id="email_auth">이메일 인증이 완료되었습니다!</div>
	</c:if>
	<h2>커뮤니티 메인 페이지</h2>

	<%@ include file="/WEB-INF/views/include/include_main.jsp" %>
</body>
<footer>
	<%@ include file="/WEB-INF/views/include/include_footer.jsp" %>
</footer>
</html>