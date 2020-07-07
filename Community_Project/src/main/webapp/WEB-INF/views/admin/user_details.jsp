<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<style>
	/* toggle CSS */
	/* The switch - the box around the slider */
	.switch {
	  position: relative;
	  display: inline-block;
	  width: 60px;
	  height: 26px;
	  margin: auto;
	  vertical-align:middle;
	}
	
	/* Hide default HTML checkbox */
	.switch input {display:none;}
	
	/* The slider */
	.slider {
	  position: absolute;
	  cursor: pointer;
	  top: 0;
	  left: 0;
	  right: 0;
	  bottom: 0;
	  background-color: #ccc;
	  -webkit-transition: .4s;
	  transition: .4s;
	}
	
	.slider:before {
	  position: absolute;
	  content: "";
	  height: 20px;
	  width: 20px;
	  left: 3px;
	  bottom: 3px;
	  background-color: white;
	  -webkit-transition: .4s;
	  transition: .4s;
	}
	/* toggle CSS End */
	
	input:checked + .slider {
	  background-color: #2196F3;
	}
	
	input:focus + .slider {
	  box-shadow: 0 0 1px #2196F3;
	}
	
	input:checked + .slider:before {
	  -webkit-transform: translateX(34px);
	  -ms-transform: translateX(34px);
	  transform: translateX(34px);
	}
	
	/* Rounded sliders */
	.slider.round {
	  border-radius: 26px;
	}
	
	.slider.round:before {
	  border-radius: 50%;
	}
</style>
<style>
	form {
		width: 100%;
		margin: 0px auto;
	}
	
	.my_form_item, .switch {
		display: flex;
		align-items: center;
		padding: 5px 0px;
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
	
	.btn_box {
		display: flex;
	}
	
	.btn_box button {
		width: 120px;
		margin-top: 20px;
		padding: 10px;
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
	
	.new_auth span, .new_auth select {
		color: var(--color-dodgerblue);
	}
	
	.cancel_auth {
		margin-left: 10px;
	}
	
	#btn_edit {
		margin-left: auto;
	}
</style>
<script>
	$(function() {
		
		let enable_btn_edit = true
		
		function isEmail(email) {
			let regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{1,6})+$/
			return regex.test(email)
		}
		
		$(document).off("click", "#btn_add_auth").on("click","#btn_add_auth",function() {
			let auth_input = "<div class='my_form_item new_auth'>"
							+ "	<span class='my_label'>새 권한</span>"
							+ "	<select class='my_data' name='auth'/>"
							+ "		<option value=''>없음</option>"
							+ "		<option value='ROLE_USER'>유저</option>"
							+ "		<option value='ROLE_ADMIN'>관리자</option>"
							+ "	</select>"
							+ " <button class='cancel_auth btn_blue' type='button'>취소</button>"
							+ "</div>"
			$("#auth_box").append(auth_input)
		})
		
		$(document).off("click", ".cancel_auth").on("click",".cancel_auth",function() {
			$(this).closest(".new_auth").remove()
		})
		
		$(document).off("click", "#btn_edit").on("click", "#btn_edit", function() {
			if(!enable_btn_edit) return false
			let email = $("#email")
			
			// 유효성 검사
			if(email.val() == "") {
				alert("이메일을 입력하세요.")
				email.focus()
				return false
			} else if( !isEmail(email.val()) ) {
				alert("올바른 형식의 이메일이 아닙니다.")
				email.focus()
				return false
			}
			
			let formData = $("#user_details_form").serialize()
			formData += "&username=" + $("#btn_edit").data("id")
			
			$.ajax({
				url : "${rootPath}/admin/user_details",
				type : "POST",
				data : formData,
				beforeSend: function(ajx) {
					// 유효성 검사 통과 시
					// 이메일 스팸 및 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
					enable_btn_edit = false
				},
				success : function(result) {
					if(result == -102) {
						alert("이메일을 정확히 입력하세요.")
					} else if(result == -103) {
						alert("생년월일을 정확히 입력하세요.")
					} else if(result == -200) {
						alert("등록되지 않은 아이디입니다.")
					} else if(result > 0) {
						alert("변경사항이 저장되었습니다.")
					}
				},
				error : function() {
					alert("서버 통신 오류")
				}
			}).always(function() {
				enable_btn_edit = true
			})
		})
		
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
	
	<!-- 권한 영역 -->
	<c:choose>
		<c:when test="${!empty USER_VO.authorities}">
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
		</c:when>
		<c:otherwise>
			<div class="my_form_item new_auth">
				<span class="my_label">권한</span>
				<select class="my_data" name="auth">
					<option value="">없음</option>
					<option value="ROLE_USER">유저</option>
					<option value="ROLE_ADMIN">관리자</option>
				</select>
			</div>
		</c:otherwise>
	</c:choose>
	
	<div id="auth_box">
	</div>
	
	<div id="add_auth_box" class="my_form_item">
		<span class="my_label"></span>
		<button id="btn_add_auth" class="my_data btn_blue" type="button">권한 추가</button>
	</div>
	
	<div class="btn_box">
		<button id="btn_edit" class="btn_confirm mg-10px" type="button" data-id="${USER_VO.username}">수정</button>
	</div>
	
</form:form>