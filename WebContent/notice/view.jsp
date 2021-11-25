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
		게시글 번호 : ${notice.nNo}<br>		<%-- session 저장이든 request 저장이든 똑같이 EL 사용 가능함 --%>
		작성자 : ${notice.writer}<br>
		조회수 : ${notice.hit}<br>
		IP : ${notice.ip}<br>
		작성일 : ${notice.nDate}<br>
		최종수정일 : ${notice.nLastModified}<br>
		제목 : ${notice.title}<br>
		내용 <br>
		<pre>${notice.content}</pre>
		<br>
		<!-- 
			목록 : 누구나
			수정, 삭제 : 작성자만		
		 -->
		<div>
			<input type="button" value="목록이동" onclick="location.href='list.notice'">
			<c:if test="${loginUser.id == notice.writer}">
				<input type="button" value="수정" onclick="location.href='updateForm.notice'">	 <%-- session에 올려둔 data를 갖고 수정할 것이기 때문에 아무 것도 전달해 주지 않는다. :: forward 되므로 전달되는 정보는 없다. --%>
				<input type="button" value="삭제" onclick="fnNoticeDelete()">		<%-- inline 이벤트로 fn 호출 --%>
			</c:if>
			
			<script>
				function fnNoticeDelete(){
					if(confirm('게시글을 삭제할까요?')) {
						location.href='delete.notice?nNo=${notice.nNo}';
						return true;
					}
				}
			</script>
			
		</div>
		<hr>
		<%-- 댓글 작성할 수 있는 form 구역 --%>
		<!-- 
			작성자 : 로그인 유저 아이디
			댓글달기 : 로그인 유저만 오픈
		 -->
		<form action="insert.reply" method="post" id="fo">	<%-- subfix --%>
			<label for="writer">작성자</label>
			<input name="writer" id="writer" value="${loginUser.id}" readonly><br>
			<textarea rows="5" cols="30" name="content" id="content"></textarea><br>
			<%-- !!DO NOT FORGET!! 꼭 실어서 보내자 hidden으로 :: nNo는 꼭 있어야 하는데 그것은 notice table의 No와같아야 한다 --%>
			<input type="hidden" name="nNo" value="${notice.nNo}">
			<c:if test="${loginUser != null}">
				<button>댓글달기</button>
			</c:if>
		</form>
		<hr>
		<%-- reply list가 보여질 구역 --%>
		<div>
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
		</div>
		
	</div>

</body>
</html>