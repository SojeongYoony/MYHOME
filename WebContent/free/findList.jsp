<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>FREE - FINDLIST 검색 결과만 보여줌</title>
	<script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.css" integrity="sha512-4wfcoXlib1Aq0mUtsLLM74SZtmB73VHTafZAvxIp/Wk9u1PpIsrfmTvK0+yKetghCL8SHlZbMyEcV8Z21v42UQ==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<style>
	a {
		text-decoration: none;
		color: black;
	}

	table {
		border-collapse: collapse;
		width: 800px;
	}
	table tr {
		padding: 5px;
	}
	
	.reply_link {
		display: inline-block;
		width: 100px;
		font-size: 8px;
		color: crimson;
	}
	.reply_link:hover {
		cursor: pointer;
	}
	.reply_form {
		display: none;
	}
	thead{
		text-align: center;	
	}
	
		
	
</style>
<script>
	$(document).ready(function(){
		$('.reply_link').on('click', function(){
			
			$(this).parent().parent().next().toggleClass('reply_form');
			
			
		}) //  click event
		
	}) // load
	
</script>

</head>
<body>

	<header>
		<h1><a href="index.jsp">WEB PAGE TITLE</a></h1>
	</header>
	
	<hr>
	<!-- 검색란  -->
	<form action="find.free">
		<!-- option의 value를 DB 칼럼명으로 직접 사용함.== 그래서 대문자로 표시했고, column의 이름과 같아야 한다. *if 처리안하고 해당 column에 바로 들어갈 수 있도록 작업 -->
		<select name="column">
			<option value="WRITER">작성자</option>
			<option value="CONTENT">내용</option>
			<option value="ALL">작성자+내용</option>
		</select>
		<input type="text" name="query">
		<button>검색</button>
		<input type="button" value="전체보기" onclick="location.href='list.free'">
	</form>

	<!-- 작성 링크 -->
	<div>
		<c:if test="${loginUser != null}">		<%-- login User가 있을 때(=session에 loginUser)만 새글작성 보여준다. --%>
			<a href="insertForm.free">새글작성</a>
		</c:if>
	</div>

	<!-- 목록  -->
	전체 게시글 : ${totalRecord}개<br>
	<table border="1">
		<thead>
			<tr>
				<td>번호</td>
				<td>작성자</td>
				<td>내용</td>
				<td>조회수</td>
				<td>최종수정일</td>
				<td></td>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty list}">
			<tr>
				<td colspan="6">게시글이 없습니다.</td>
			</tr>
			</c:if>
			<c:if test="${not empty list}">
				<c:forEach items="${list}" var="free">
					<c:if test="${free.state == 0 }">
						<tr>
							<td>${free.fNo}</td>
							<td>${free.writer}</td>
							<td>
								<!-- depth만큼 들여쓰기 -->
								<c:forEach begin="1" end="${free.depth}">
									&nbsp;&nbsp;
								</c:forEach>
								<!-- 댓글(depth 1 이상) [re] -->
								<c:if test="${free.depth >= 1}">
									↪
								</c:if>
								<!-- 20자 이내는 그대로 표시 -->
								<c:if test="${free.content.length() < 20}">
									<a href="view.free?fNo=${free.fNo}">${free.content}</a>&nbsp;&nbsp;&nbsp;
								</c:if>
								<!-- 20자 이상은 20자만 표시 -->
								<c:if test="${free.content.length() >= 20}">
									<a href="view.free?fNo=${free.fNo}">${free.content.substring(0, 20)}</a>&nbsp;&nbsp;&nbsp;
								</c:if>
								<a class="reply_link">댓글달기</a>
							</td>
							<td>${free.hit}</td>
							<td>${free.lastModified}</td>
							<td>
								<c:if test="${loginUser.id == free.writer}">
									<a onclick="fnDelete(this)" href="delete.free?fNo=${free.fNo}"><i class="far fa-times-circle"></i></a>
									<script type="text/javascript">
										function fnDelete(a){
											$(a).on('click', function(event){
												if(confirm('삭제 할까요?') == false) {
													event.preventDefault();
													return false;
												} 
												return true;
											});
										}
									</script>
								</c:if>
							</td>
						</tr>
						<tr class="reply_form">
							<td colspan="6">
								<form action="insertReply.free" method="post">
									<!-- TIP : HIDDEN 작업하기 전에 TEXT로 값이 들어오는지 확인한 후, 확인이 되면 HIDDEN으로 바꿔준다  -->
									<!-- 원글의 DEPTH, GROUPNO, GROUPORD를 넘겨줌  -->
									<input type="hidden" name="depth" value="${free.depth}">
									<input type="hidden" name="groupNo" value="${free.groupNo}">
									<input type="hidden" name="groupOrd" value="${free.groupOrd}">
									<input type="text" name="writer" value="${loginUser.id}" readonly>
									<input type="text" name="content" placeholder="내용">
									<button id="sub_btn">댓글달기</button>
								</form>
							</td>
						</tr>
					</c:if>
					<c:if test="${free.state == -1}">
						<tr>
							<td colspan="6">삭제된 게시글 입니다.</td>
							<%-- 삭제된 게시글일지라도 댓글확인이 필요하다 그리고 페이징 처리할 때, count되어야 한다 그래서 포함시킴 --%>
						</tr>
					</c:if>
				</c:forEach>
			</c:if>
		</tbody>
		<tfoot>
			<tr>
				<td colspan="6">
					<!-- 1페이지로 이동 : 1페이지는 링크가 필요 없음 == 링크를 걸지 않음 -->
					<c:if test="${p.page == 1}">		
							◀◀&nbsp;&nbsp;		
					</c:if>
					<c:if test="${p.page != 1}">
						<a href="find.free?column=${column}&query=${query}&page=1">◀◀</a>&nbsp;&nbsp;	<!-- 검색 후 mapping값 실어줘야함  column=${column}&query=${query}&page= // 검색용 list jsp를 추가하든 if를 추가하든 선택하여 개선할 수 있다. -->
					</c:if>
				
					<!-- 이전 블록으로 이동 : 1블록은 이전 블록이 없음 == 링크를 걸지 않음 -->
					<c:if test="${p.page <= p.pagePerBlock}">
							◀&nbsp;&nbsp;	
					</c:if>
					<c:if test="${p.page > p.pagePerBlock}">
						<a href="find.free?column=${column}&query=${query}&page=${p.beginPage - 1}">◀</a>&nbsp;&nbsp;	
					</c:if>
					
					<!-- 페이지 번호 : 현재 페이지는 이동이 필요 없음 == 링크를 걸지 않음 -->
					<c:forEach var="i" begin="${p.beginPage}" end="${p.endPage}">  <%-- begin - end :: var = i => currentPage  --%>
						<c:if test="${p.page == i}">
							${i}&nbsp;&nbsp;
						</c:if>
						<c:if test="${p.page != i}">
							<a href="find.free?column=${column}&query=${query}&page=${i}">${i}</a>&nbsp;&nbsp;
						</c:if>
					</c:forEach>
					
					<!-- 다음 블록으로 이동 : 마지막 블락은 이동이 필요 없음 == 링크를 걸지 않음 -->
						<c:if test="${p.endPage == p.totalPage}">
							▶&nbsp;&nbsp;	
						</c:if>						
						<c:if test="${p.endPage != p.totalPage }">
							<a href="find.free?column=${column}&query=${query}&page=${p.endPage + 1}">▶</a>&nbsp;&nbsp;	
						</c:if>						
						
					
					<!-- 마지막 페이지로 이동 : 마지막 페이지는 링크가 필요 없음 -->
						<c:if test="${p.page == p.totalPage}">
							▶▶&nbsp;&nbsp;	
						</c:if>
						<c:if test="${p.page != p.totalPage}">
							<a href="find.free?column=${column}&query=${query}&page=${p.totalPage}">▶▶</a>&nbsp;&nbsp;	
						</c:if>
				</td>
			</tr>
		</tfoot>
		
	</table>
</body>
</html>