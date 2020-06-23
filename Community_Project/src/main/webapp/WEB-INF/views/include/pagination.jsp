<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" type="text/css" href="${rootPath}/resources/css/pagination.css"/>
<script>
	$(function() {
		$(document).on("click", ".page_middot", function() {
			let jump_page = prompt("이동할 페이지 (1~" + ${PAGE_DTO.lastPageNo} + ")")
			
			if(jump_page != null) {
				document.location.href = "?currPage=" + jump_page + "${PAGE_DEFAULT_QUERY}"
			}
		})
	})
</script>
<article class="page_box">
	<ul class="page_body">
		<li class="page_item"><a class="page_link" href="?currPage=1${PAGE_DEFAULT_QUERY}">처음</a></li>
		<li class="page_item"><a class="page_link page_middot">&middot;&middot;&middot;</a></li>
		
		<c:forEach begin="${PAGE_DTO.startPageNo}" end="${PAGE_DTO.endPageNo}" var="pageNo">
			<li class="page_item <c:if test='${pageNo == PAGE_DTO.currentPageNo}'>active</c:if>"><a class="page_link" href="?currPage=${pageNo}${PAGE_DEFAULT_QUERY}">${pageNo}</a></li>
		</c:forEach>
		
		<li class="page_item"><a class="page_link page_middot">&middot;&middot;&middot;</a></li>
		<li class="page_item"><a class="page_link" href="currPage=${PAGE_DTO.lastPageNo}${PAGE_DEFAULT_QUERY}">끝</a></li>
	</ul>
</article>