<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>join</title>
	<script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
	
		<script>
		$(document).ready(function(){
			
			$('#id').on('keyup', function(){
				$.ajax({
					url: 'idCheck.member',			// server 경로
					type: 'post',
					data: 'id=' + $('#id').val(),	// 보내는 parameter
					dataType: 'json',
					success: function(resData) {
						if(resData.result == true) {
							$('#id_check').text('사용 가능한 아이디');
						} else {
							$('#id_check').text('이미 사용중인 아이디');
						}
					},
					error: function(xhr){
						alert(xhr.responseText);
					}
				});
			});
			
			$('#fo').on('submit',function(event){
				
				if( $('#pw').val() != $('#pwcheck').val() ){
					alert('비밀번호가 일치하지 않습니다.');
					event.preventDefault();
					$('#pw').focus();
					return false;
				} 
				return true;
				
/* 				if($('#id').val() == '') {
					alert('아이디 입력 필수');
					event.preventDefault();
					$('#id').focus();
					return false;
				} else if ($('#pw').val() == '') {
					alert('비밀번호 입력 필수');
					event.preventDefault();
					$('#pw').focus();
					return false;
				} else if ($('#name').val() == '') {
					alert('이름 입력 필수');
					event.preventDefault();
					$('#name').focus();
					return false;
				} else if ($('#email').val() == '') {
					alert('이메일 입력 필수');
					event.preventDefault();
					$('#email').focus();
					return false;
				}
				return true; */
				
			}) // end submit
		}) // end onload
	</script>
	
	
</head>
<body>
	<div>
		<h1>회원가입</h1>
		<form id="fo" action="join.member" method="post">
			<input type="text" name="id" id="id" placeholder="아이디">
			<span id="id_check"></span><br>
			<input type="password" name="pw" id="pw" placeholder="비밀번호"><br>
			<input type="password" id="pwcheck" placeholder="비밀번호 확인"><br>
			<input type="text" name="name" id="name" placeholder="이름"><br>
			<input type="text" name="email" id="email" placeholder="이메일"><br>
			<button>가입하기</button>
			<input type="reset" value="입력 취소">
			<input type="button" value="가입 취소" onclick="location.href='index.jsp'">
		</form>
	</div>
</body>
</html>