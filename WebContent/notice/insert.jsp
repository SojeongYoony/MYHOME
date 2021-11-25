<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>insert form</title>
	<script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
	
	<script>
		$(document).ready(function(){
			
			$('#fo').on('submit',function(event){
				if($('#writer').val() == '') {
					alert('작성자 입력 필수');
					event.preventDefault();
					$('#writer').focus();
					return false;
				} else if ($('#title').val() == '') {	// DB의 UNIQUE 옵션을 생각하여 공백필수를 프론트에서 먼저 해준다.
					alert('제목 입력 필수');
					event.preventDefault();
					$('#title').focus();
					return false;
				} else if ($('#content').val() == '') {
					alert('내용 입력 필수');
					event.preventDefault();
					$('#content').focus();
					return false;
				}
				return true;
			}) // end submit
		}) // end onload
	
	</script>
	
	
</head>
<body>

	<div>
		<form action="insert.notice" method="post" id="fo">
			<label for="writer">작성자</label>
			<input type="text" id="writer" name="writer" value="${loginUser.id}" readonly>
			<br>
			<label for="title">제목</label>
			<input type="text" id="title" name="title">
			<br>
			<textarea rows="5" cols="30" name="content" id="content" placeholder="내용"></textarea>
			<br><br>
			<button>작성완료</button>
			<input type="reset" value="다시작성">
			<input type="button" value="목록이동" onclick="location.href='list.notice'">
		</form>
	</div>

</body>
</html>