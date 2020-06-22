<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<link rel="stylesheet" href="${rootPath}/resources/css/toggle.css"/>
<style>
	form {
		width: 70%;
		margin: 10px auto;
	}
	
	.my_form_item, .switch {
		display: flex;
		align-items: center;
		padding: 5px 0;
	}
	.switch {
		margin-left: 5px;
	}
	
	.my_label {
		display: inline-block;
		text-align: right;
		width: 20%;
		margin-right: 20px;
	}
	
	.my_data {
		padding: 0.5rem 1rem;
		width: 60%;
	}
	
	.birth {
		display: flex;
		width: 60%;
		justify-content: space-between;
	}
	#year, #month, #day {
		width: 30%;
		padding: 0.5rem 0.2rem;
		min-width: 60px;
	}
	
	#btn_add_auth {
		background-color: var(--color-success);
	}
	
	.btn_box {
		display: flex;
	}
	
	.btn_box button {
		display: block;
		width: 100px;
		padding: 10px;
		margin-top: 20px;
	}
	#btn_edit {
		margin-left: auto;
	}
</style>
<script>
	var check = $("input[type='checkbox']");
	check.click(function(){
		$("p").toggle();
	})
</script>
<form:form id="user_details_form" action="${rootPath}/user/mypage" autocomplete="${FORM_AUTOCOMPLETE}">
	<div class="my_form_item">
		<span class="my_label">ID</span>
		<span class="my_data">${USER_VO.username}</span>
	</div>
	
	<div class="my_form_item">
		<span class="my_label">활성여부</span>
		<label class="switch">
			<input type="checkbox" name="enabled" <c:if test="${USER_VO.enabled}">checked</c:if>/>
			<span class="slider round"></span>
		</label>
	</div>
	
	<div class="my_form_item">
		<span class="my_label">닉네임</span>
		<input id="nickname" class="my_data" name="nickname" value="${USER_VO.nickname}" />
	</div>
	
	<div class="my_form_item">
		<span class="my_label">이메일</span>
		<input type="email" id="email" class="my_data" name="email" value="${USER_VO.email}" />
	</div>
	
	<div class="my_form_item">
		<span class="my_label">핸드폰</span>
		<input type="tel" id="phone" class="my_data" name="phone" value="${USER_VO.phone}" />
	</div>
	
	<div class="my_form_item">
		<span class="my_label">생년월일</span>
		<div class="birth">
			<input type="number" id="year" name="year" value="${USER_VO.year}" />
			<select id="month" name="month">
				<option value="">월</option>
				<c:forEach begin="1" end="12" var="count">
					<option value="${count}" <c:if test="${USER_VO.month == count}">selected="selected"</c:if>>${count}월</option>
				</c:forEach>
			</select>
			<input type="number" id="day" name="day" value="${USER_VO.day}" />
		</div>
	</div>
	
	<c:choose>
		<c:when test="${empty USER_VO.authorities}">
			<div class="my_form_item">
				<span class="my_label">권한</span>
				<select class="my_data" name="auth">
					<option value="">없음</option>
					<option value="ROLE_USER">유저</option>
					<option value="ROLE_ADMIN">관리자</option>
				</select>
			</div>
		</c:when>
		<c:otherwise>
			<c:forEach items="${USER_VO.authorities}" var="auth" varStatus="s">
				<div class="my_form_item">
					<span class="my_label">권한${s.count}</span>
					<select class="my_data" name="auth">
						<option value="">없음</option>
						<option value="ROLE_USER" <c:if test="${auth.authority == 'ROLE_USER'}">selected="selected"</c:if>>유저</option>
						<option value="ROLE_ADMIN" <c:if test="${auth.authority == 'ROLE_ADMIN'}">selected="selected"</c:if>>관리자</option>
					</select>
				</div>
			</c:forEach>
		</c:otherwise>
	</c:choose>
	
	<div id="auth_box">
	</div>
	
	<div class="btn_box">
		<button id="btn_add_auth" type="button">권한 추가</button>
		<button id="btn_edit" type="button" data-id="${USER_VO.username}">수정</button>
	</div>
	
</form:form>