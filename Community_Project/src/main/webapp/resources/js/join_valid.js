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

let username = document.querySelector("#username")
let password = document.querySelector("#password")
let re_password = document.querySelector("#re_password")
let email = document.querySelector("#email")
let year = document.querySelector("#year")
let month = document.querySelector("#month")
let day = document.querySelector("#day")

function regJoin() {
	if(username.value == "") {
		alert("아이디를 입력하세요.")
		username.focus()
		return false
	} else if ( !regId(username.value) ) {
		alert("아이디는 4~12자의 영문 대소문자와 숫자로만 입력하세요.")
		username.focus()
		return false
	} else if (password.value == "") {
		alert("비밀번호를 입력하세요.")
		password.focus()
		return false
	} else if (re_password.value == "") {
		alert("비밀번호 확인을 입력하세요.")
		re_password.focus()
		return false
	} else if (password.value != re_password.value) {
		alert("비밀번호가 다릅니다.\n다시 확인하세요.")
		re_password.focus()
		return false
	} else if(email.value == "") {
		alert("이메일을 입력하세요.")
		email.focus()
		return false
	} else if( !isEmail(email.value) ) {
		alert("올바른 형식의 이메일이 아닙니다.")
		email.focus()
		return false
	} else if ( !regYear(year.value) ) {
		alert("생년을 정확히 입력하세요.")
		year.focus()
		return false
	} else if ( !regMonthOrDay(month.value) ) {
		alert("생월을 정확히 입력하세요.")
		month.focus()
		return false
	} else if ( !regMonthOrDay(day.value) ) {
		alert("생일을 정확히 입력하세요.")
		day.focus()
		return false
	}
}