<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>시작화면</title>
	<script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
</head>
<body>

	<header>
		<h1>WEB PAGE TITLE</h1>
		<nav>
			<ul>
				<li><a href="list.notice">공지사항</a></li>		<%-- context path(MYHOME)는 생략 --%>
				<li><a href="list.free">자유게시판</a></li>		<%-- context path(MYHOME)는 생략 --%>
				<li><a href="list.board">이미지게시판</a></li>
			</ul>
		</nav>
		<c:if test="${loginUser != null}">
			<div>
				<h3>${loginUser.name}님 반갑습니다.
					<input type="button" value="로그아웃" onclick="location.href='logout.member'">
					<input type="button" value="회원탈퇴" onclick="location.href='leave.member'">
				</h3>
			</div>
		</c:if>
	</header>
	<section>
		<c:if test="${loginUser == null}">
		<div>
			<form action="login.member" method="post" id="fo">
				<input type="text" name="id" id="id" placeholder="아이디"><br>
				<input type="password" name="pw" id="pw" placeholder="비밀번호"><br>
				<button>로그인</button>
				<a href="joinForm.member">회원가입</a>		<!-- 회원가입을 하기 위해서는 중복체크가 필요하고, 중복체크는 ajax로 처리하여 page이동이 없게 한다. query작업은 두 가지다. select -> insert -->
			</form>
		</div>
		</c:if>
		
	</section>
	
</body>
</html>