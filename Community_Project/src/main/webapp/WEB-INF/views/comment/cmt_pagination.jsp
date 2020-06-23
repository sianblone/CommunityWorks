<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" type="text/css" href="${rootPath}/resources/css/pagination.css"/>
<script>
	$(function() {
		$(document).off("click", ".page_middot").on("click", ".page_middot", function() {
			let jump_page = prompt("이동할 페이지 (1~" + ${PAGE_DTO.lastPageNo} + ")")
			
			if(jump_page != null) {
				let requestURL = "${rootPath}/comment/list"
				let query = "?currPage=" + jump_page + "${PAGE_DEFAULT_QUERY}"
				requestURL += query
				
				$.ajax({
					url: requestURL,
					type: "GET",
					success: function(result) {
						$(".cmt_list").html(result)
					},
					error: function(error) {
						console.log("댓글 불러오기 실패")
					}
				})
			}
		})
		
		$(document).off("click", ".page_link").on("click", ".page_link", function() {
			let requestURL = "${rootPath}/comment/list"
			let query = $(this).data("href")
			requestURL += query
			
			$.ajax({
				url: requestURL,
				type: "GET",
				success: function(result) {
					$(".cmt_list").html(result)
				},
				error: function(error) {
					console.log("댓글 불러오기 실패")
				}
			})
			
		})
	})
</script>
<article class="page_box">
	<ul class="page_body">
		<li class="page_item"><span class="page_link" data-href="?currPage=1${PAGE_DEFAULT_QUERY}">처음</span></li>
		<li class="page_item"><span class="page_link page_middot">&middot;&middot;&middot;</span></li>
		
		<c:forEach begin="${PAGE_DTO.startPageNo}" end="${PAGE_DTO.endPageNo}" var="pageNo">
			<li class="page_item <c:if test='${pageNo == PAGE_DTO.currentPageNo}'>active</c:if>"><span class="page_link" data-href="?currPage=${pageNo}${PAGE_DEFAULT_QUERY}">${pageNo}</span></li>
		</c:forEach>
		
		<li class="page_item"><span class="page_link page_middot">&middot;&middot;&middot;</span></li>
		<li class="page_item"><span class="page_link" data-href="currPage=${PAGE_DTO.lastPageNo}${PAGE_DEFAULT_QUERY}">끝</span></li>
	</ul>
</article>