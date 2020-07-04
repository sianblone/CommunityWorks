<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<style>
	table {
		table-layout: fixed;
		width: 100%;
		border-collapse: collapse;
	}
	
	tr.board_info td:nth-child(2) {
		cursor: pointer;
	}
	
	tr.board_info td:nth-child(2):hover {
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
		padding: 0px;
	}
	
	.change_order:hover {
		background-color: var(--color-dodgerblue);
		color: white;
	}
	
	.btn_box {
		text-align: right;
		margin-top: 15px;
	}
</style>
<script>
	$(function() {
		
		let enable_board_info = true
		let enable_btn_add_board = true
		let enable_btn_change_order = true
		
		$(document).off("click", ".board_info td:nth-child(2)").on("click", ".board_info td:nth-child(2)", function() {
			event.stopPropagation()
			if(!enable_board_info) return false
			
			$.ajax({
				url: "${rootPath}/admin/board_setting_details",
				type: "GET",
				data: { bi_id : $(this).closest(".board_info").data("id") },
				beforeSend: function() {
					// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
					enable_board_info = false
					$("body").css("cursor", "wait")
				},
				success: function(result) {
					$("#admin_content").html(result)
				},
				error: function(error) {
					alert("서버 통신 오류")
				}
			}).always(function() {
				enable_board_info = true
				$("body").css("cursor", "default")
			})
		})
		
		$(document).off("click", "#btn_add_board").on("click", "#btn_add_board", function() {
			if(!enable_btn_add_board) return false
			
			$.ajax({
				url: "${rootPath}/admin/board_setting_create_board",
				type: "GET",
				beforeSend: function() {
					// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
					enable_btn_add_board = false
					$("body").css("cursor", "wait")
				},
				success: function(result) {
					$("#admin_content").html(result)
				},
				error: function(error) {
					alert("서버 통신 오류")
				}
			}).always(function() {
				enable_btn_add_board = false
				$("body").css("cursor", "default")
			})
		})
		
		$(document).off("click", ".change_order").on("click", ".change_order", function(event) {
			event.stopPropagation()
			if(!enable_btn_change_order) return false
			
			$.ajax({
				url: "${rootPath}/admin/board_change_order",
				type: "POST",
				data: {
					bi_id : $(this).closest(".board_info").attr("data-id"),
					order : $(this).attr("data-order")
				},
				beforeSend: function(ajx) {
					ajx.setRequestHeader("${_csrf.headerName}", "${_csrf.token}")
					// 서버 부하를 줄이기 위해 ajax 완료될 때까지 버튼 기능 끄기
					enable_btn_change_order = false
					$("body").css("cursor", "wait")
				},
				success: function(result) {
					$("#admin_content").html(result)
				},
				error: function() {
					alert("서버 통신 오류")
				}
			}).always(function() {
				enable_btn_change_order = false
				$("body").css("cursor", "default")
			})
			
			return false
		})
	})
</script>
<table>
	<colgroup>
		<col style="width: 15%">
		<col style="width: 50%">
		<col style="width: 15%">
		<col style="width: 10%">
		<col style="width: 10%">
	</colgroup>
	<thead>
		<tr>
			<th>게시판번호</th>
			<th>게시판이름</th>
			<th>활성여부</th>
			<th>순서</th>
			<th>순서변경</th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${empty BOARD_INFO_LIST}">
				<tr>
					<td colSpan="3">생성된 게시판이 없습니다</td>
				</tr>
			</c:when>
			<c:otherwise>
				<c:forEach items="${ADMIN_BOARD_INFO_LIST}" var="bi">
					<tr class="board_info" data-id="${bi.bi_id}">
						<td>${bi.bi_id}</td>
						<td>${bi.bi_name}</td>
						<td><c:choose><c:when test="${bi.bi_enabled}">활성</c:when><c:otherwise>비활성</c:otherwise></c:choose></td>
						<td>${bi.bi_order}</td>
						<td><div class="change_order_box"><div class="change_order" data-order="-1">▲</div><div class="change_order" data-order="1">▼</div></div></td>
					</tr>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
<div class="btn_box">
	<button id="btn_add_board">게시판 추가</button>
</div>