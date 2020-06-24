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

$(function() {
	
	$(document).on("click", "#btn_join", function() {
		if(!enable_btn_join) return false
		
		// 유효성 검사
		if(regJoin() == false) return false
		
		// 유효성 검사 통과 시
		// 이메일 스팸 및 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
		enable_btn_join = false
		$("body").css("cursor", "wait")
		
		$.ajax({
			url : rootPath + "/join/join",
			method : "POST",
			data : $("#join-form").serialize(),
			success : function(result) {
				if(result > 0) {
					alert("가입을 환영합니다!\n이메일로 발송된 인증 링크를 클릭하세요.")
					document.location.href = rootPath + "/"
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
				} else if(result == -300) {
					alert("인증메일 발송에 실패했습니다. 다시 시도해주세요.")
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
	
	// 현재 입력박스에서 포커스가 벗어났을때 발생하는 이벤트
	// 아이디 중복 확인
	$(document).on("blur", "#username", function() {
		let username = $(this).val()
		
		if( !regId(username) ) {
			$("#m_username").text("* 아이디는 4~12자의 영문 대소문자와 숫자로만 입력하세요")
			$("#m_username").css("color", "var(--color-danger)")
			$(".message").css("display", "block")
			return false
		}
		
		$.ajax({
			url : rootPath + "/join/idcheck",
			type : "GET",
			data : {username : username},
			success : function(result) {
				if(result) {
					$("#m_username").text("* 이미 사용중인 ID입니다")
					$("#m_username").css("color", "var(--color-danger)")
					$(".message").css("display", "block")
				} else {
					$("#m_username").text("* 사용 가능한 ID입니다")
					$("#m_username").css("color", "var(--color-success)")
					$(".message").css("display", "block")
				}
			},
			error : function() {
				$("#m_username").text("* 서버 통신 오류")
				$("#m_username").css("color", "var(--color-danger)")
				$(".message").css("display", "block")
			}
		})
	})
})