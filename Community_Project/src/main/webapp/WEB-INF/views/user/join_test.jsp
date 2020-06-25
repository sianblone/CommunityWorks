<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/include/include_head.jspf" %>
	<link rel="stylesheet" href="${rootPath}/resources/css/join.css" />
	<style>
		.authority {
			text-align: center;
		}
	</style>
	<script>
		let rootPath = "${rootPath}"
		
		$(function() {
			let enable_btn_join = true
			
			function isEmail(email) {
				let regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{1,6})+$/
				return regex.test(email)
			}

			function regId(username) {
				let regex = /^[a-zA-Z0-9]{4,12}$/
				return regex.test(username)
			}

			function regYear(year) {
				let regex = /^[0-9]{4}$/
				return regex.test(year)
			}

			function regMonthOrDay(monthOrDay) {
				let regex = /^[0-9]{1,2}$/
				return regex.test(monthOrDay)
			}

			let username = $("#username")
			let password = $("#password")
			let re_password = $("#re_password")
			let email = $("#email")
			let year = $("#year")
			let month = $("#month")
			let day = $("#day")

			function regJoin() {
				if(username.val() == "") {
					alert("아이디를 입력하세요.")
					username.focus()
					return false
				} else if ( !regId(username.val()) ) {
					alert("아이디는 4~12자의 영문 대소문자와 숫자로만 입력하세요.")
					username.focus()
					return false
				} else if (password.val() == "") {
					alert("비밀번호를 입력하세요.")
					password.focus()
					return false
				} else if (re_password.val() == "") {
					alert("비밀번호 확인을 입력하세요.")
					re_password.focus()
					return false
				} else if (password.val() != re_password.val()) {
					alert("비밀번호가 다릅니다.\n다시 확인하세요.")
					re_password.focus()
					return false
				} else if(email.val() == "") {
					alert("이메일을 입력하세요.")
					email.focus()
					return false
				} else if( !isEmail(email.val()) ) {
					alert("올바른 형식의 이메일이 아닙니다.")
					email.focus()
					return false
				} else if ( !regYear(year.val()) ) {
					alert("생년을 정확히 입력하세요.")
					year.focus()
					return false
				} else if ( !regMonthOrDay(month.val()) ) {
					alert("생월을 정확히 입력하세요.")
					month.focus()
					return false
				} else if ( !regMonthOrDay(day.val()) ) {
					alert("생일을 정확히 입력하세요.")
					day.focus()
					return false
				}
			}
			
			// ----------------------------------------------------------
			
			// join_text에서 사용하는 이벤트
			$(document).on("click", "#btn_test_join", function() {
				if(!enable_btn_join) return false
				// 유효성 검사
				if(regJoin() == false) return false
				
				// 유효성 검사 통과 시
				// 이메일 스팸 및 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
				enable_btn_join = false
				$("body").css("cursor", "wait")
				
				$.ajax({
					url : "${rootPath}/join/test-join",
					method : "POST",
					data : $("#join-form").serialize(),
					success : function(result) {
						if(result > 0) {
							alert("회원가입을 환영합니다!")
							document.location.href = "${rootPath}/"
						} else if(result == -100) {
							alert("아이디를 정확히 입력하세요.")
						} else if(result == -101) {
							alert("비밀번호를 정확히 입력하세요.")
						} else if(result == -102) {
							alert("이메일을 정확히 입력하세요.")
						} else if(result == -103) {
							alert("생년월일을 정확히 입력하세요.")
						} else if(result == -200) {
							alert("이미 사용중인 아이디입니다.")
						}
					},
					error : function() {
						alert("서버 통신 오류")
					}
				}).always(function() {
					enable_btn_join = true
					$("body").css("cursor", "default")
				})
			})
		})
	</script>
	<script src="${rootPath}/resources/js/join.js"></script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/include_nav.jspf" %>
	<header>
		<h2>회원가입</h2>
	</header>
	<form:form id="join-form" action="${rootPath}/join/join" method="POST" autocomplete="${FORM_AUTOCOMPLETE}" onSubmit="return false">
		<div class="form_item">
			<label for="username">ID</label>
		</div>
		
		<div class="form_item">
			<input id="username" name="username"/>
			<span id="m_username" class="message"></span>
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
		
		<div class="form_item">
			<label for="authority">권한</label><br/>
		</div>
		
		<div class="form_item authority">
			<select name="authority">
				<option value="">테스트 가입 시 아이디 권한 설정</option>
				<option value="ROLE_USER">유저</option>
				<option value="ROLE_ADMIN">관리자</option>
			</select>
		</div>
		
		<div class="form_item btn_box">
			<button id="btn_join" type="button">회원가입</button>
		</div>
		
		<div class="form_item btn_box">
			<button id="btn_test_join" type="button">회원가입(개발테스트)</button>
		</div>
	</form:form>
</body>
</html>