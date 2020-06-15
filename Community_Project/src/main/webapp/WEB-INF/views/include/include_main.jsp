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
		<c:forEach items="${topMenuList}" var="boardinfoVO">
			<div class="col-lg-6 cardbox">
				<div class="card shadow">
					<div class="card-body">
						<h4 class="card-title boardname_box"><span>${boardinfoVO.bi_name}</span>
							<a href="${rootPath}/board/list?board_info=${boardinfoVO.bi_id}" class="more"><img src="${rootPath}/resources/images/add.png"></a>
						</h4>
						<table class="table table-hover" id='board_list'>
							<thead>
								<tr>
									<th class="text-center w-25">글번호</th>
									<th>제목</th>
									<th class="text-center w-25 d-none d-xl-table-cell">작성날짜</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach begin="1" end="5" varStatus="status">
									<tr>
										<td class="text-center">${status.count }</td>
										<th><a href='${rootPath}board/read'>제목입니다</a></th>
										<td class="text-center d-none d-xl-table-cell">2018-12-12</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						<div class="text-right">
						
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>
</div>