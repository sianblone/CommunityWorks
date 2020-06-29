$(function() {
	// 사용법
	// 1. 클릭 시 컨텍스트 메뉴를 띄울 위치에 use_context 클래스 주기
	// 2. use_context를 감싸고 있는 박스(테이블로 치면 tr)에 context_parent 클래스와 data-id 주기. 추가로 필요한 옵션 주기(이 프로젝트에선 검색을 위한 data-nickname 등)
	// 3. include file : context_menu.jsp
	let last_click_parent_data_id
	let nickname
	// .use_context 클릭 시 컨텍스트 메뉴 보이기
	$(document).off("click", ".use_context").on("click", ".use_context", function(event) {
		event.stopPropagation()
		
		// data 함수는 동적 태그를 가져오기 못하므로 attr 함수 사용
		let data_id = $(this).closest(".context_parent").attr("data-id")
		
		if(data_id == last_click_parent_data_id && $(".context").hasClass("context_active")) {
			// 새로 클릭한 data-id가 마지막으로 클릭한 닉네임 context_parent의 data-id와 같고 
			// 컨텍스트 메뉴가 보이는 상태(active)라면 안보이게
			$(".context").removeClass("context_active")
			$(".context").addClass("context_inactive")

		} else {
			// 1. 컨텍스트 메뉴 안보이는 상태(inactive)에서 클릭 시 컨텍스트 메뉴 마우스 위치에 표시
			let mouseX = event.pageX
			let mouseY = event.pageY
			
			$(".context").css({"left": mouseX, "top": mouseY})
			
			$(".context").removeClass("context_inactive")
			$(".context").addClass("context_active")
			
			// 2. last_click_parent_data_id는 지금 클릭한 context_parent의 data-id로 변경
			last_click_parent_data_id = data_id
			
			// 3. nickname 값 설정하기
			nickname = $(this).closest(".context_parent").attr("data-nickname")
		}
	})
	
	// DOM에서 .use_context를 제외한 모든 곳 클릭 시 컨텍스트 메뉴 안보이게(사라지게)
	$(document).on("click", function(e) {
		if( !$(e.target).is("use_context") ) {
			$(".context").removeClass("context_active")
			$(".context").addClass("context_inactive")
		}
	})
	
	// rootPath와 bi_id는 선언한 곳의 script에서 선언해주기
	$(document).off("click", ".search_nickname").on("click", ".search_nickname", function() {
		document.location.href = rootPath + "/board/list?board_info=" + bi_id + "&search_type=nickname" + "&search_txt=" + nickname
	})
	
})