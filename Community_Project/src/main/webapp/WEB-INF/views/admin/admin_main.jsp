<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/include/include_head.jspf" %>
	<style>
		#body {
			width: 800px;
			display: flex; 
			margin: 0 auto;
		}
		
		#body nav {
			width: 20%;
			text-align: center;
			padding: 20px 0;
		}
		
		#body nav ul {
			list-style: none;
		}
		
		#body nav li {
			display: block;
			padding: 0.5rem 1rem;
			cursor: pointer;
		}
		
		#body nav li:hover {
			background-color: #eee;
		}
		
		#vertical_line {
			width: 2px;
			margin: 0px 20px;
			border-right: 2px solid var(--color-dodgerblue);
		}
		
		#admin_content {
			width: 60%;
		}
		
		
	</style>
	<script>
		$(function() {
			
			let enable_btn_edit = true
			let enable_btn_edit_board = true
			
			function isEmail(email) {
				let regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{1,6})+$/
				return regex.test(email)
			}
			// --------------------------------------------------------
			
			// 유저 목록
			$(document).on("click", "#user_list", function() {
				$.get("${rootPath}/admin/user_list", function(result) {
					$("#admin_content").html(result)
				})
			})
			
			// 게시판 설정
			$(document).on("click", "#board_setting", function() {
				$.get("${rootPath}/admin/board_setting", function(result) {
					$("#admin_content").html(result)
				})
			})
			
			// user_list.jsp
			$(document).on("click", "tr.tr_user", function() {
				let username = $(this).attr("data-id")
				$.get("${rootPath}/admin/user_details/" + username, function(result) {
					$("#admin_content").html(result)
				})
			})
			// ------------------------
			
			// user_details.jsp
			$(document).on("click","#btn_add_auth",function(){
				let auth_input = "<div class='my_form_item'>"
								+ "	<span class='my_label'>새 권한</span>"
								+ "	<select class='my_data' name='auth'/>"
								+ "		<option value=''>없음</option>"
								+ "		<option value='ROLE_USER'>유저</option>"
								+ "		<option value='ROLE_ADMIN'>관리자</option>"
								+ "	</select>"
								+ "</div>"
				//auth_input.append($("<p/>", {"text":"제거","class":"auth_delete"}))
				$("div#auth_box").append(auth_input)
			})
			
			$(document).on("click", "#btn_edit", function() {
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
				
				// 유효성 검사 통과 시
				// 이메일 스팸 및 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
				enable_btn_edit = false
				$("body").css("cursor", "wait")
				
				let formData = $("#user_details_form").serialize()
				formData += "&username=" + $("#btn_edit").data("id")
				
				$.ajax({
					url : "${rootPath}/admin/user_details",
					type : "POST",
					data : formData,
					success : function(result) {
						if(result == -102) {
							alert("이메일을 정확히 입력하세요.")
						} else if(result == -103) {
							alert("생년월일을 정확히 입력하세요.")
						} else {
							$("#admin_content").html(result)
							alert("변경사항이 저장되었습니다.")
						}
					},
					error : function() {
						alert("서버 통신 오류")
					}
				}).always(function() {
					enable_btn_edit = true
					$("body").css("cursor", "default")
				})
			})
			// ------------------------------
			
			// board_setting.jsp
			$(document).on("click", ".board_info", function() {
				$.ajax({
					url: "${rootPath}/admin/board_setting_details",
					type: "GET",
					data: { board_info : $(this).data("id") },
					success: function(result) {
						$("#admin_content").html(result)
					},
					error: function(error) {
						alert("서버 통신 오류")
					}
				})
			})
			
			$(document).on("click", "#btn_add_board", function() {
				$.ajax({
					url: "${rootPath}/admin/board_setting_create_board",
					type: "GET",
					success: function(result) {
						$("#admin_content").html(result)
					},
					error: function(error) {
						alert("서버 통신 오류")
					}
				})
			})
			// ------------------------------
			
			// board_setting_create_board.jsp
			$(document).on("click", "#btn_create_board", function() {
				if($("#bi_name").val() == "") {
					alert("게시판 이름을 입력하세요.")
					return false
				}
				
				$.ajax({
					url: "${rootPath}/admin/board_setting_create_board",
					type: "POST",
					data: $("#create_board_form").serialize(),
					success: function(result) {
						$("#admin_content").html(result)
					},
					error: function(error) {
						alert("서버 통신 오류")
					}
				})
			})
			// ------------------------------
			
			// board_setting_details.jsp
			$(document).on("click","#btn_add_category",function(){
				let cate_input = "<div class='my_form_item category_box'>"
								+ "	<span class='my_label'>새 카테고리</span>"
								+ "	<input class='my_data' name='cate_id_list' type='hidden' value='0'/>"
								+ "	<input class='my_data' name='cate_delete_list' type='hidden' value='false'/>"
								+ "	<input class='my_data' name='cate_text_list'/>"
								//+ "<button class='delete'>삭제</button>"
								+ "</div>"
				$("div#cate_box").append(cate_input)
			})
			
			$(document).on("click", "#btn_edit_board", function() {
				
				if(!enable_btn_edit_board) return false
				// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
				enable_btn_edit_board = false
				$("body").css("cursor", "wait")
				
				/*
				let arrCategory = $(".category_box").map(function() {
					let o = {}
					$(this).find("input").each(function() {
						o[$(this).attr("name")] = this.value
					})
					return o
				}).get()
				console.log(arrCategory)
				*/
				
				let formData = $("#board_setting_details_form").serialize()
				formData += "&bi_id=" + $("#btn_edit_board").data("id")
				
				$.ajax({
					url : "${rootPath}/admin/board_setting_details",
					type : "POST",
					data : formData,
					success : function(result) {
						if(result == -100) {
							alert("게시판 이름은 20글자 이내여야 합니다.")
						} else if(result == -101) {
							alert("카테고리는 20글자 이내여야 합니다.")
						} else if(result == -200) {
							alert("등록되지 않은 게시판 ID입니다.")
						} else {
							alert("변경사항이 저장되었습니다.")
							document.location.replace(document.location.href)
						}
					},
					error : function() {
						alert("서버 통신 오류")
					}
				}).always(function() {
					enable_btn_edit_board = true
					$("body").css("cursor", "default")
				})
				
			})
			// ------------------------------
			
		})
	</script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/include_nav.jspf" %>
	<h2>관리자 페이지</h2>
	<section id="body">
		<nav>
			<ul>
				<li id="user_list">유저 목록</li>
				<li id="board_setting">게시판 설정</li>
			</ul>
		</nav>
		<article id="vertical_line">
		</article>
		<article id="admin_content">
			
		</article>
	</section>
</body>
</html>