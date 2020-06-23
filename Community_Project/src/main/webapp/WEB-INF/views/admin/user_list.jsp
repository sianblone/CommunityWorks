<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="rootPath" value="${pageContext.request.contextPath}"/>
<style>
	table {
		table-layout: fixed;
		width: 100%;
		border-collapse: collapse;
	}
	
	tr.tr_user {
		cursor: pointer;
	}
	
	tr.tr_user:hover {
		background-color: #eee;
	}
	
	th, td {
		border-bottom: 1px solid var(--color-dodgerblue);
		overflow: hidden;
		white-space: nowrap;
		padding: 8px 12px;
		text-align: center;
	}
</style>
<script>
	$(function() {
		$(document).off("click", "tr.tr_user").on("click", "tr.tr_user", function() {
			let username = $(this).attr("data-id")
			$.get("${rootPath}/admin/user_details/" + username, function(result) {
				$("#admin_content").html(result)
			})
		})
	})
</script>
<table>
	<colgroup>
		<col style="width: 20%">
		<col style="width: 20%">
		<col style="width: 16%">
		<col style="width: 18%">
		<col style="width: 18%">
		<col style="width: 18%">
	</colgroup>
	<thead>
		<tr>
			<th>ID</th>
			<th>닉네임</th>
			<th>활성여부</th>
			<th>이메일</th>
			<th>핸드폰</th>
			<th>생년월일</th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${empty USER_LIST}">
				<tr>
					<td colSpan="6">등록된 유저가 없습니다</td>
				</tr>
			</c:when>
			<c:when test="${!empty USER_LIST}">
				<c:forEach items="${USER_LIST}" var="user" varStatus="s">
					<tr data-id="${user.username}" class="tr_user">
						<td>${user.username}</td>
						<td>${user.nickname}</td>
						<td><c:choose><c:when test="${user.enabled}">활성</c:when><c:otherwise>비활성</c:otherwise></c:choose></td>
						<td>${user.email}</td>
						<td>${user.phone}</td>
						<td>${user.year}년 ${user.month}월 ${user.day}일</td>
					</tr>
				</c:forEach>
			</c:when>
		</c:choose>
	</tbody>
</table>
