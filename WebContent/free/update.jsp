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
			$('#f').on('submit', function(event){
				if($('#content').val() == '${param.content}' ) {
					alert('변경할 내용이 없습니다.');
					event.preventDefault();
					return false;
				}
				return true;
				
			}); // end submit event
			
		
		}); // end onload
	</script>
</head>
<body>

	
	<div>
		<form action="update.free" method="post" id="f">
			작성자 : ${loginUser.id}<br>
			<input type="hidden" name="fNo" value="${param.fNo}">	<%-- submit의 parameter를 통해 온 content임 주의! --%>
			<textarea rows="5" cols="30" name="content" id="content" placeholder="내용">${param.content}</textarea>	<%-- submit의 parameter를 통해 온 content임 주의! --%>
			<br><br>
			<button>수정하기</button>
			<input type="reset" value="입력초기화">
			<input type="button" value="목록이동" onclick="location.href='list.free'">
		</form>
	</div>

</body>
</html>