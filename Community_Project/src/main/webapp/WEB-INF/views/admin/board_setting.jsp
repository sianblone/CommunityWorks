<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<style>
	table {
		table-layout: fixed;
		width: 100%;
		border-collapse: collapse;
	}
	
	tr.board_info {
		cursor: pointer;
	}
	
	tr.board_info:hover {
		background-color: #eee;
	}
	
	th, td {
		border-bottom: 1px solid var(--color-dodgerblue);
		overflow: hidden;
		white-space: nowrap;
		padding: 8px 12px;
		text-align: center;
	}
	td {
		border-right: 1px solid var(--color-dodgerblue);
	}
	td:last-child {
		border-right: none;
	}
	.btn_box {
		text-align: right;
		margin-top: 15px;
	}
</style>
<script>
	$(function() {
		$(document).off("click", ".board_info").on("click", ".board_info", function() {
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
		
		$(document).off("click", "#btn_add_board").on("click", "#btn_add_board", function() {
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
	})
</script>
<table>
	<colgroup>
		<col style="width: 25%">
		<col style="width: 75%">
	</colgroup>
	<thead>
		<tr>
			<th>게시판번호</th>
			<th>게시판이름</th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${empty BOARD_INFO_LIST}">
				<tr>
					<td colSpan="2">생성된 게시판이 없습니다</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach items="${BOARD_INFO_LIST}" var="vo">
					<tr class="board_info" data-id="${vo.bi_id}">
						<td>${vo.bi_id}</td>
						<td>${vo.bi_name}</td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
<div class="btn_box">
	<button id="btn_add_board">게시판 추가</button>
</div>