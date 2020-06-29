<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form"  prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<style>
	.cmt_write_box {
		display: flex;
		background-color: #f2f2f2;
		padding: 20px 0px;
	}
	.cmt_write_group {
		display: flex;
		align-items: center;
	}
	.cmt_write_group:nth-child(3n+1) {
		width: 17%;
		justify-content: center;
		font-weight: bold;
		overflow: hidden;
		white-space: nowrap;
	}
	.cmt_write_group:nth-child(3n+2) {
		width: 67%;
	}
	.cmt_content_unauth {
		background-color: white;
	}
	.cmt_btn_box {
		display: flex;
		align-items: center;
		margin: 0px auto;
	}
	.btn_cmt_save {
		padding: 20px 30px;
	}
</style>
<script>
	$(function() {
		let enable_btn_cmt_save = true
		
		$(document).on("click", ".cmt_content_unauth", function() {
			if(confirm("로그인 하시겠습니까?")) {
				document.location.href = "${rootPath}/user/login"
			}
		})
		
		$(document).on("click", ".btn_cmt_save", function() {
			if(!enable_btn_cmt_save) return false
			
			if($(".cmt_csrf").length == 0) {
				if(confirm("로그인 하시겠습니까?")) {
					document.location.href = "${rootPath}/user/login"
					return false
				} else {
					return false
				}
			}
			
			if($(".cmt_content").val() == "") {
				alert("내용을 입력하세요.")
				return false
			}
			
			// 유효성 검사 통과 시
			// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
			enable_btn_cmt_save = false
			$("body").css("cursor", "progress")
			
			$.ajax({
				url: "${rootPath}/comment/save",
				type: "POST",
				data: $(".comment_form").serialize(),
				success: function(result) {
					$(".cmt_content").val("")
					$(".cmt_list").html(result)
				},
				error: function(error) {
					alert("서버 통신 오류")
				}
			}).always(function() {
				enable_btn_cmt_save = true
				$("body").css("cursor", "default")
			})
		})
	})
</script>
<section class="cmt_write">
	<form class="comment_form" method="POST" autocomplete="${FORM_AUTOCOMPLETE}">
		<sec:authorize access="hasAnyRole('ADMIN','USER')">
			<input class="cmt_csrf" name="${_csrf.parameterName}" value="${_csrf.token}" type="hidden">
		</sec:authorize>
		<input name="cmt_board_no" value="<c:out value='${BOARD_VO.board_no}' default='0'/>" type="hidden">
		<article class="cmt_write_box">
			<div class="cmt_write_group cmt_nickname">
				<sec:authorize access="isAuthenticated()">
					<span><sec:authentication property="principal.nickname"/></span>
				</sec:authorize>
			</div>
			
			<div class="cmt_write_group">
				<sec:authorize access="isAuthenticated()">
					<textarea class="form-control cmt_content" name="cmt_content" rows="2"></textarea>
				</sec:authorize>
				<sec:authorize access="!isAuthenticated()">
					<textarea class="form-control cmt_content_unauth" rows="2" placeholder="댓글을 달려면 로그인을 해야합니다." readonly></textarea>
				</sec:authorize>
			</div>
			
			<div class="cmt_btn_box">
				<button class="btn_cmt_save btn_blue" type="button">등록</button>
			</div>
		</article>
	</form>
</section>