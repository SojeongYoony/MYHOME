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
	
	<script type="text/javascript">
		$(document).ready(function(){
			$('#fo').on('submit',function(event){
				if($('#writer').val() == '') {
					alert('작성자 입력 필수');
					event.preventDefault();
					$('#writer').focus();
					return false;
				} else if ($('#content').val() == '') {
					alert('내용 입력 필수');
					event.preventDefault();
					$('#content').focus();
					return false;
				}
				return true;
			}) // end submit
		})
	</script>
	
	
</head>
<body>
	<%-- 받아올 dto 2가지 --%>
	<div>
		게시글 번호 : ${free.fNo}<br>		<%-- session 저장이든 request 저장이든 똑같이 EL 사용 가능함 --%>
		작성자 : ${free.writer}<br>
		조회수 : ${free.hit}<br>
		IP : ${free.ip}<br>
		작성일 : ${free.created}<br>
		최종수정일 : ${free.lastModified}<br>
		내용 <br>
		<pre>${free.content}</pre>
		<br>
		<!-- 
			목록 : 누구나
			수정, 삭제 : 작성자만		
		 -->
		<div>
			<input type="button" value="목록이동" onclick="location.href='${referer}'">
			<c:if test="${loginUser.id == free.writer}">
				<form method="post" id="f">
					<!-- 수정할 게시글 정보를 넘기기 --><%-- 어떤 내용, 어떤 게시글인지 꼭 parameter로 담아서 보낼 것 !!! 그리고 받는 곳에서는 parameter를 받는다!!! --%>
					<input type="hidden" name="fNo" value="${free.fNo}">
					<input type="hidden" name="content" value="${free.content}">				
					<input type="button" value="수정하러가기" onclick="fnFreeUpdate()">	 <%-- session에 올려둔 data를 갖고 수정할 것이기 때문에 아무 것도 전달해 주지 않는다. :: forward 되므로 전달되는 정보는 없다. --%>
				</form>
				<input type="button" value="삭제" onclick="fnFreeDelete()">		<%-- inline 이벤트로 fn 호출 --%>
			</c:if>
		</div>
			<script>
				function fnFreeUpdate(){
					$('#f').attr('action', 'updateForm.free')	// fNo, content 전달 된다. **주의** data 실어서 가기!!! ==> forward!!!!!
					$('#f').submit();
					// session의 정보가 유효한지 확인이 필요함. 일단 언급만 하고 넘어감.
				}
			
			
				function fnFreeDelete(){
					if(confirm('게시글을 삭제할까요?')) {
						location.href='delete.free?fNo=${free.fNo}';
						
					}
					
					
					
				}
			</script>

		<hr>
		<div>
		<%-- reply list가 보여질 구역 
			<c:if test="${empty replyList}">
				첫 댓글의 주인공이 되어 보자
			</c:if>
			<c:if test="${not empty replyList}">
				<c:forEach items="${replyList}" var="reply">
					${reply.writer}&nbsp;&nbsp;		<!-- reply.writer == loginUser.id :: 내가 쓴 글 == 삭제가능 버튼 만든다. -->
					${reply.ip}&nbsp;&nbsp;
					${reply.rDate}<br>
					<pre>${reply.content}</pre>
				</c:forEach>
			</c:if>
		--%>
		</div>
		
	</div>

</body>
</html>