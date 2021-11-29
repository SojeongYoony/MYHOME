<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>수정하는Form</title>
	<script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
	
	<style>
		label {
			display: block;
			margin-top: 10px;
		}
	</style>
	<script>
		$(document).ready(function(){
			fnFileCheck();
			fnUpdateBoard();
		})// load
		
		<%-- ----------------------------  function fnFileCheck()  ------------------------- --%>			

		function fnFileCheck(){
	         $('#fileName').on('change', function(){	// 첨부를 할 때만 동작
	     
	        	let fullname = $(this).val();
	            let extension = fullname.substring(fullname.lastIndexOf('.') + 1).toUpperCase();
	            let extList = ['JPG', 'JPEG', 'PNG', 'GIF', 'PDF'];
	            if ($.inArray(extension, extList) == -1) {  // 배열에 찾는 요소가 없으면 -1을 반환
	               alert('확장자가 jpg, jpeg, png, gif인 파일만 업로드 할 수 있습니다.');
	               $(this).val('');  // 첨부된 파일명을 빈 문자열로 바꿈 => 첨부가 없어짐.
	               return false;
	            }
	            
	            /* 첨부 파일의 용량 제한하기 */
	            let maxSize = 10 * 1024 * 1024;  // 10메가 * 1024킬로바이트 * 1024바이트
	            let fileSize = $(this)[0].files[0].size;
	            if (maxSize < fileSize) {
	               alert('10MB 이하의 파일만 업로드 할 수 있습니다.');
	               $(this).val('');
	               return false;
	            }
	         });
	      } // fnFileCheck
			
		<%-- ----------------------------  function fnUpdateBoard()  ------------------------- --%>					
		function fnUpdateBoard(){
			$('#update_btn').on('click', function(){
				if( $('#title').val() == '${param.title}' && 
				    $('#content').val() == '${param.content}' && 
				    $('#fileName').val() == '') {
					alert('변경된 내용이 없습니다.');
					return;
				}
				$('#f').attr('action', 'update.board');	// 성공 했을 경우 처리 : attribute action="insert.board" 부여, submit
				$('#f').submit();
				
			}) // update_btn click event
			
			
		}; //fnUpdateBoard
		
		
	</script>

</head>
<body>

	<div>
	
		<!-- 파일 첨부 폼 -->
		<!-- 
			1. method="post"
			2. enctype="multipart/form-data"
		 -->
		 
		 <!-- 첨부 했을 때, 안 했을때 image (작게)보여줄거고 insert_btn => update_btn으로 수정할 예정 -->
		 
		 <form id="f" method="post" enctype="multipart/form-data">	<!-- file 첨부가 가능한 form ==> multipart request가 필요함 -->
		 	<label for="writer">작성자</label>		
			${param.writer}<!-- submit -> forward -> parameter : get from parameter -->
					 	
		 	<label for="title">제목</label>	<!-- NOT NULL : SCRIPT -->
		 	<input type="text" id="title" name="title" value="${param.title}">
		 	
		 	<label for="content">내용</label>
		 	<textarea id="content" name="content">${param.content}</textarea>

		 	<div>
		 		<img width="300px" src="${param.path}/${param.saveName}">
		 	</div>
		 	
		 	<label for="fileName">새로 첨부하기</label>	<!-- 확장자 : SCRIPT 작업 필요함 -->
		 	<input type="file" id="fileName" name="fileName">	<!-- 새로 첨부할 파일의 이름 -->
		 	
		 	<input type="hidden" name="bNo" value="${param.bNo}">
   	<!--    <input type="hidden" name="path" value="${param.path}">	 여기가 server로 가면서 request로 전달이 되지 않음(request에 저장이 안 된다는 소리): 그래서 session 저장소로 해야할 것 같음 : jsp에서 하거나 java:viewService에서 하는방법 -->
		 	<% session.setAttribute("path", request.getParameter("path")); %>	<!-- request로 전달 받은 parameter 값을 session에 저장 함 -->
		 	<input type="hidden" name="saveName" value="${param.saveName}"> <!-- 기존에 첨부할 파일의 이름 -->
		 		
		 	<br><br>
		 	
		 	<div class="btn_area">
		 		<input type="button" value="수정하기" id="update_btn">
		 		<input type="reset" value="입력초기화">
		 		<input type="button" value="목록보기" onclick="location.href='list.board'">
		 	</div>
		 	
		 </form>
	</div>

</body>
</html>