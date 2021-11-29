<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>상세보기</title>
	<script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
	
	<style>
		#comments_list > ul {
			width: 700px;
			margin-top: 10px;
			padding: 0;
			list-style-type: none;
			display: flex;
		}
		#comments_list > ul > li:nth-of-type(1) {width: 100px;}
		#comments_list > ul > li:nth-of-type(2) {width: 500px;}
		#comments_list > ul > li:nth-of-type(3) {width: 100px;}
		#comments_list > ul > li:nth-of-type(4) {width: 100px;}
		
		#pageEntity {
			display: flex;
			justify-content: center;	
			width: 100%;
		}
			
			
		#pageEntity > div {
			width: 20px;
			height: 20px;
			text-align: center;
		}
		.disable_link {
			color: silver;
		}
		.enable_link {
			cursor: pointer;
		}
		.enable_link:hover, .now_page {
			border: 1px solid silver;
			color: orange;
		}
		
	</style>
	
	<script>
		$(document).ready(function(){
			fnDeleteBoard();	// 게시글 삭제
			fnUpdateForm();		// 게시글 수정하러가기
			fnInsertComments();	// 댓글 삽입
			fnCommentsList();   // 댓글 리스트 보여주기
			fnDeleteComments(); // 댓글 삭제
			fnChangePage();		// 전역 변수 page 설정하기			-- page에 따라 목록이 달라진다.
		}); // load event
		
	<%-- --------------------------------- fnDeleteBoard --------------------------------- --%>
		function fnDeleteBoard(){
			$('#delete_btn').on('click', function(){
				if (confirm('게시글에 달린 모든 댓글도 함께 삭제됩니다. 삭제할까요?')) {
					$('#f').attr('action', 'delete.board');
					$('#f').submit();
				}	
			}) // delete_btn click event				== click has no basic event, except submit.
		}; // fnDeleteComments		-- 내부가 복잡해서 외부에 만들어 호출하는 방식으로 함
		
	<%-- --------------------------------- fnUpdateForm --------------------------------- --%>
		function fnUpdateForm(){
			$('#update_btn').on('click', function(){
				$('#f').attr('action', 'updateForm.board');
				$('#f').submit();
			}) // update_btn click event
		}; // fnUpdateForm
		
		
	<%-- --------------------------------- fnInsertComments --------------------------------- --%>
		function fnInsertComments(){
			$('#insert_btn').on('click', function(){
				if ( $('#content').val() == '' ) {
					alert('댓글 내용 필수');
					$('#content').focus();
					return;
				}
				$.ajax({
					url: 'insert.comments',			
					type: 'post',
					data: $('#comments_form').serialize(),	// 모든걸 보내는 serialize
					dataType: 'json',
					success: function(obj){					// obj : {"result" : 0} 또는 {"result" : 1}
						fnCommentsList();
						$('#content').val('');				// 쓴 댓글의 댓글란 지우기
					},// success
					error: function(xhr){
						alert(xhr.responseText);
					}
				}); // ajax
			}); // insert_btn click event
			
		}; // fnInsertComments
	
	<%-- --------------------------------- fnCommentsList --------------------------------- --%>
		// 전역 변수 : 현재 페이지 번호	-- 최초 page 초기화 1로 세팅
		var page = 1;
		
		function fnCommentsList(){
			$.ajax({
				url: 'list.comments',
				type: 'get',
				data: 'bNo=${board.bNo}&page=' + page,
				dataType: 'json',
				success: function(result){	// result = {"commentsCount" : 2, "comments" : [{}, {}], "pageEntity" : {"totalRecord" : 2, ...}  }// paging을 위한 paging 정보도 받아와서, 함께 뿌려줘야 한다.
					// 댓글 목록 출력하기
						fnPrintCommentsList(result);	// 댓글이 있으면 목록 보여주기
						fnPageEntity(result.pageEntity);// 페이징 출력하기
				}, //success
				error: function(xhr){
					alert(xhr.responseText);
				}
			}) // ajax
			
		}//fncommentslist
		
		

	<%-- --------------------------------- fnPrintCommentsList --------------------------------- --%>
		function fnPrintCommentsList(result){
			$('#comments_list').empty();	// 목록 비우기
			if (result.commentsCount == 0){	// 댓글이 없는 상황
				$('<ul>')
				.append( $('<li>').text() )
				.append( $('<li>').text('첫 댓글의 주인공이 되어 보세요.') )
				.append( $('<li>').text() )
				.append( $('<li>').text() )
				.appendTo( '#comments_list' );
			} else {	// 댓글이 있는 상황
				$.each(result.comments, function(i, comment){
					if (comment.state == 0)	{	// 정상 댓글이면
						if ( '${loginUser.id}' == comment.writer ) {	// 관리자만 삭제 가능하게끔 하려면..여기에 조건을 하나 더 줌
							$('<ul>')
							.append( $('<li>').text(comment.writer) )
							.append( $('<li>').text(comment.content) )
							.append( $('<li>').text(comment.created) )
							.append( $('<li>').html('<a class="delete_comments_link" data-cno="' + comment.cNo + '">삭제</a>') ) // param : cNo
							.appendTo('#comments_list');
						} else {
							$('<ul>')
							.append( $('<li>').text(comment.writer) )
							.append( $('<li>').text(comment.content) )
							.append( $('<li>').text(comment.created) )
							.append( $('<li>').html('') )
							.appendTo('#comments_list')
						} // if
						
					} else if (comment.state == -1) {	// 삭제 된 댓글이면 
						$('<ul>')
						.append( $('<li>').text('') )
						.append( $('<li>').text('삭제된 댓글입니다.') )
						.append( $('<li>').text('') )
						.append( $('<li>').html('') )
						.appendTo('#comments_list');
					}
					
				}); //for
			} // end if
			
			
		
			
		};	// fnPrintCommentsList == print only
		
		<%-- --------------------------------- function fnPageEntity --------------------------------- --%>
		function fnPageEntity(p) {
			$('#pageEntity').empty();	// 초기화 하는 함수 empty()	-- pageEntity영역을 초기화한다
			// 1페이지로 이동 : 1페이지에는 링크가 필요없음.
			if (page == 1) {	// 전역변수의 page 값
				// class="disable_link" : CSS 용도 -- click안되는 CSS용도
				$('<div class="disable_link">◀◀</div>').appendTo('#pageEntity');
			} else {
				// class="enable_link"  : CSS 용도
				// class="first_page"	: 전역 변수 page를 1로 수정
				// class="change_page"  : click 이벤트로 연결	-- 절충안
				$('<div class="enable_link first_page change_page" data-page="1">◀◀</div>').appendTo('#pageEntity');
			} 
				// 이전 블록으로 이동 : 1블록은 링크가 필요 없음.
			if (page <= p.pagePerBlock) {
				// class="disable_link" : CSS 용도 -- click안되는 CSS용도
				$('<div class="disable_link">◀</div>').appendTo('#pageEntity');
			} else {
				// class="enable_link"  : CSS 용도
				// class="prev_block"	: 전역변수 page를 (beginPage -1)로 수정
				$('<div class="enable_link prev_block change_page" data-page="'+ (p.beginPage - 1) +'">◀</div>').appendTo('#pageEntity');
			} 
			// 페이지 번호 : 현재 페이지는 링크가 없음
			for (let i = p.beginPage; i <= p.endPage; i++) {
				if (i == p.page) {	// 링크가 없는 현재 페이지	// 현재 페이지 번호 == i
					// class="disable_link"  : CSS 용도
					// class="now_page"		 : CSS 용도
					$('<div class="disable_link now_page">'+ i +'</div>').appendTo('#pageEntity');
				} else {
					// class="enable_link"  : CSS 용도
					// class="other_page"	: 전역변수 page를 i로 수정
					$('<div class="enable_link other_page change_page" data-page="'+ i +'">'+ i +'</div>').appendTo('#pageEntity');
				}
			}
			// 다음 블록으로 이동 : 마지막 블록은 링크가 필요없음
			if (p.endPage == p.totalPage) {
				// class="disable_link"  : CSS 용도
				$('<div class="disable_link">▶</div>').appendTo('#pageEntity');
			} else {
				// class="enable_link"  : CSS 용도
				// class="next_block"	: 전역변수 page를 (endPage + 1)로 수정
				$('<div class="enable_link next_block change_page" data-page="'+ (p.endPage + 1) +'">▶</div>').appendTo('#pageEntity');
			}
			// 마지막 페이지로 이동 : 마지막 페이지는 링크가 필요 없음 
			if (p.page == p.totalPage) {
				// class="disable_link"  : CSS 용도
				$('<div class="disable_link">▶▶</div>').appendTo('#pageEntity');
			} else {
				// class="enable_link"  : CSS 용도
				// class="last_page"	: 전역변수 page를 totalPage로 수정
				$('<div class="enable_link last_page change_page" data-page="'+ p.totalPage +'">▶▶</div>').appendTo('#pageEntity');
			}
			
		} // end fnPageEntity
		
		
	<%-- ------------------------------------ fnChangePage ------------------------------------ --%>
	function fnChangePage() {
		$('body').on('click', '.change_page', function(){	// 사실은 동적 클래스가 모두 같은 일을 함 	 -- 다른 방법 : data속성으로 이동이 이뤄지는 tag들은 별도의 class로 따로 지정하여 작업하는 방법이 있음 
			page = $(this).data('page');	// 전역 변수 page 수정 this == click된 first_page
			fnCommentsList();				// 바뀐 page의 목록 다시 가져오기 -- 바꾼 뒤, 다시 목록을 가져와야 하므로 fnCommentsList함수 재호출
		}); // body on click event 
	} //fnChangePage
		
	
		
	<%-- --------------------------------- fnDeleteComments --------------------------------- --%>
		
		function fnDeleteComments(){
			$('body').on('click', '.delete_comments_link', function(event){		// page load가 시작 되었을 때, 확인되지 않으므로 body tag를 잡음
				if ( confirm('댓글을 삭제할까요?')) {
					$.ajax({
						 url: 'delete.comments',
						type: 'get',
						data: 'cNo=' + $(this).data('cno'),	// this -> delete_comments_btn	:: !!!!!data 속성은 대소문자가 무시 됨 주의 !!!!!
						success: function(){
							fnCommentsList();
						},
						error: function(xhr){
							alert(xhr.responseText);
						}
					}); //ajax
				}
			}); // click fn
		}//delete function
		
	</script>
	

</head>
<body>
	<div>
		<input type="button" value="목록으로이동" onclick="location.href='list.board'">
		<c:if test="${loginUser.id == board.writer}">	<!-- 작성자만 볼 수 있다. -->
			<form id="f" method="post">
				<input type="hidden" name="bNo" value="${board.bNo}"> <!-- hidden으로 가는 parameter -->
				<input type="hidden" name="path" value="storage/${year}/${month}/${day}">
				<input type="hidden" name="saveName" value="${board.saveName}">
				<input type="hidden" name="title" value="${board.title}">
				<input type="hidden" name="content" value="${board.content}">	<!-- session을 이용하는 방법도 있음 그러나 session 이용 시, 수정이 이뤄질 때, session도 해야함 -->
				<input type="hidden" name="writer" value="${board.writer}">
				
				<input type="button" value="수정하러가기" id="update_btn">
				<input type="button" value="삭제하러가기" id="delete_btn">
			</form>
		</c:if>
	</div>
	
	<div>
		<table>
			<tbody>
				<tr>
					<td>작성자</td>
					<td>${board.writer}</td>
					<td>작성일자</td>
					<td>${board.created}</td>
					<td>수정일자</td>
					<td>${board.lastModified}</td>
				</tr>
				<tr>
					<td>제목</td>
					<td colspan="5">${board.title}</td>
				</tr>
				<tr>
					<td>내용</td>
					<td colspan="5">${board.content}</td>
				</tr>
				<tr>
					<td colspan="6">
						<img width="640px" src="storage/${year}/${month}/${day}/${board.saveName}" alt="${board.fileName}">
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<hr>
	
	<%-- --------------------------------------  댓글 입력란   ---------------------------------------------- --%>
	<div>
		<form id="comments_form">
			<table>
				<tbody>
					<tr>
						<td rowspan="2">
							<textarea rows="3" cols="80" name="content" id="content"></textarea>
							<input type="hidden" name="writer" value="${loginUser.id}">
							<input type="hidden" name="bNo" value="${board.bNo}">
						</td>
						<td>
							${loginUser.id}(${loginUser.name})
						</td>
					</tr>
					<tr>
						<td>
							<c:if test="${loginUser != null}">
								<input type="button" value="댓글달기" id="insert_btn">
							</c:if> <!-- login한 사람만 보임 -->
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	<%-- --------------------------------------  댓글 목록   ---------------------------------------------- --%>
	<div id="comments_list">
		<ul>
			<li>작성자</li>
			<li>내용</li>
			<li>작성일자</li>
			<li>삭제</li> <!-- 작성자에게만 삭제 버튼을 활성화 시켜줘서, 삭제할 수 있게끔 할 것 -->
		</ul>
	</div>
	<div id="pageEntity"></div>
</body>
</html>