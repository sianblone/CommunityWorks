<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="rootPath" value="${pageContext.request.contextPath}" />
<style>
	.main_boards {
		margin: 0 auto;
		min-width: 768px;
		max-width: 1024px; 
	}
	table {
		font-family: gulim, 굴림;
		font-size: 13px;
	}
	.boardname_box {
		display: flex;
	}
	.more {
		margin-left: auto;
	}
	.cardbox{
		margin-bottom: 30px;
	}
</style>
<!-- 게시판 미리보기 부분 -->
<div class="main_boards">
	<div class="row">
		<c:forEach items="${BOARD_INFO_LIST}" var="boardInfoVO">
			<div class="col-lg-6 cardbox">
				<div class="card shadow">
					<div class="card-body">
						<h4 class="card-title boardname_box"><span>${boardInfoVO.bi_name}</span>
							<a href="${rootPath}/board/list?board_info=${boardInfoVO.bi_id}" class="more"><img src="${rootPath}/resources/images/add.png"></a>
						</h4>
						<table class="table table-hover" id='board_list'>
							<thead>
								<tr>
									<th>제목</th>
									<th class="text-center w-25 d-none d-xl-table-cell">등록일</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${boardInfoVO.bi_board_list}" var="boardVO" varStatus="status">
									<tr>
										<td class="c_subject"><a href="${rootPath}/board/details?board_info=${boardInfoVO.bi_id}&board_no=${boardVO.board_no}"><c:if
										test="${boardVO.board_depth > 0}">└ </c:if><c:if test="${boardVO.board_delete == 1}">[삭제됨] </c:if>${boardVO.board_category} ${boardVO.board_subject}</a></td>
										<td class="text-center d-none d-xl-table-cell">${boardVO.board_datetime}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
</div>