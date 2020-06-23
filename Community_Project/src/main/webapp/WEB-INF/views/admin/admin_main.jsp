<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/include_head.jspf" %>
<style>
	#admin_main {
		width: 100%;
		display: flex; 
	}
	
	#admin_main nav {
		flex-basis: 20%;
		text-align: center;
		padding: 20px 0;
	}
	
	#admin_main nav ul {
		list-style: none;
	}
	
	#admin_main nav li {
		display: block;
		padding: 0.5rem 1rem;
		cursor: pointer;
	}
	
	#admin_main nav li:hover {
		background-color: #eee;
	}
	
	#admin_main #vertical_line {
		width: 2px;
		margin: 0px 20px;
		border-right: 2px solid var(--color-dodgerblue);
	}
	
	#admin_main #admin_content {
		flex-basis: 90%;
	}
</style>
<script>
	$(function() {
		
		let enable_btn_main = true
		
		// 유저 목록
		$(document).on("click", "#user_list", function() {
			if(!enable_btn_main) return false
			// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
			enable_btn_main = false
			$("body").css("cursor", "wait")
			
			$.get("${rootPath}/admin/user_list", function(result) {
				$("#admin_content").html(result)
			}).always(function() {
				// ajax 완료 후 버튼 기능 키기
				enable_btn_main = true
				$("body").css("cursor", "default")
			})
		})
		
		// 게시판 설정
		$(document).on("click", "#board_setting", function() {
			if(!enable_btn_main) return false
			// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
			enable_btn_main = false
			$("body").css("cursor", "wait")
			
			$.get("${rootPath}/admin/board_setting", function(result) {
				$("#admin_content").html(result)
			}).always(function() {
				// ajax 완료 후 버튼 기능 키기
				enable_btn_main = true
				$("body").css("cursor", "default")
			})
		})
		
	})
</script>
</head>
<body>
	<%@ include file="/WEB-INF/views/include/include_nav.jspf" %>
	<header>
		<h2>관리자 페이지</h2>
	</header>
	
	<main>
		<section id="admin_main">
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
	</main>
</body>
</html>