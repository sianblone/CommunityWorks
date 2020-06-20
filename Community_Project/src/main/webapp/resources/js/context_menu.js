$(function() {
	
	let last_click_tr_data_id
	
	// .use_context 클릭 시 컨텍스트 메뉴 보이기
	$(document).on("click", ".use_context", function(event) {
		event.stopPropagation()
		
		// data 함수는 동적 태그를 가져오기 못하므로 attr 함수 사용
		let data_id = $(this).closest("tr").attr("data-id")
		
		if(data_id == last_click_tr_data_id && $(".context").hasClass("context_active")) {
			// 새로 클릭한 data-id가 마지막으로 클릭한 닉네임 tr의 data-id와 같고 
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
			
			// 컨텍스트 메뉴 박스에 data-id 추가하기(data 함수는 읽기전용이기 때문에 attr 함수로 추가)
			$(".context").attr("data-id", data_id)
			
			// 2. last_click_tr_data_id는 지금 클릭한 tr의 data-id로 변경
			last_click_tr_data_id = data_id
		}
	})
	
	// DOM에서 .use_context를 제외한 모든 곳 클릭 시 컨텍스트 메뉴 안보이게(사라지게)
	$(document).click(function(e) {
		if( !$(e.target).is("use_context") ) {
			$(".context").removeClass("context_active")
			$(".context").addClass("context_inactive")
		}
	})
	
})