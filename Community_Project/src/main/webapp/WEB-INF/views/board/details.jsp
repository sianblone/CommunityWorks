<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initian-scale=1">
	<title>JSP 페이지</title>
</head>
<body>
	<p>${BOARD_VO.board_writer}</p>
	<p>${BOARD_VO.board_date} ${BOARD_VO.board_time}</p>
	<p>${BOARD_VO.board_count}</p>
	<p>${BOARD_VO.board_recommend}</p>
	<p>${BOARD_VO.board_category}</p>
	<p>${BOARD_VO.board_subject}</p>
	<p>${BOARD_VO.board_content}</p>
</body>
</html>