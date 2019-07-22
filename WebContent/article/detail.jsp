<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String pageTitle = "게시글 상세페이지";
%>

<%@ include file="../part/head.jspf" %>
<link rel="stylesheet" href="../Resources/article.css">

<%
	long loginedMemberId = Controller.getLoginedMemberId(request);
	boolean isNotLogined = Controller.isNotLogined(request);
	
	boolean alreadyRecommededArticle = (Integer)request.getAttribute("alreadyRecommededArticle") > 0 ? true : false;
	boolean myArticle = (Integer)request.getAttribute("myArticle") > 0 ? true : false;
	int recommendsCount = (Integer)request.getAttribute("recommendsCount");
	int id = Integer.parseInt(request.getParameter("id"));
	
	Map<String,Object> article = (Map<String,Object>)request.getAttribute("article");
	List<Map<String,Object>> replies = (List<Map<String,Object>>)request.getAttribute("replies");
%>


<script>
	function enableEditMode(btn) {
		$btn = $(btn);
		$tr = $btn.closest('tr');
		$tr.addClass('edit-mode');
		$tr.siblings('.edit-mode').removeClass('edit-mode');
	}	
	
	function disableEditMode(btn) {
		$btn = $(btn);
		$tr = $btn.closest('tr');
		$tr.removeClass('edit-mode');
	}	
</script>

	<h1 class="con"><%=pageTitle%></h1>
	
	<h2 class="con">글 본문</h2>
	
	<section class="article-detail article-table table-common con row">
		
		<div class="article-writer cell">
               <div class="writer-icon"></div>
               <span></span>
       	</div>
       	
		<table class="cell" border="1" >
        	<colgroup>
                <col width="100px">
            </colgroup>
            
			<tbody>
				<tr class="article-title">
					<th colspan="1">제목</th>
					<td colspan="3">[<%=id%>]<%=article.get("title")%></td>
				</tr>
				<tr class="article-info">
					<th>날짜</th>
					<td><%=article.get("regDate")%></td>
					<th>조회수</th>
					<td><%=article.get("hit")%></td>
				</tr>
				<tr class="article-body">
					<th>내용</th>
					<td colspan="4"><%=article.get("body").toString().replaceAll("\n","<br>")%></td>
				</tr>
			</tbody>
		</table>
		
		<div class="article-menu row">
			<%
		boolean needToBtnDeleteVisible = (long) article.get("memberId") == 0L;
	
		if (loginedMemberId == (long) article.get("memberId")) {
			needToBtnDeleteVisible = true;
		}
	%>
		<div class="cell-right">
			<% if ( needToBtnDeleteVisible ) { %>
				<a href="./modify.sbs?id=<%=id%>">글 수정</a>
				<a href="javascript:;" onclick="deleteArticle(<%=id%>,<%=article.get("memberId")%>)">글 삭제</a>
			<% } %>
			
			
			<% if ( !myArticle ) {
				if ( alreadyRecommededArticle ) { %>
					
					<span class="alreadyRecommended recommendButton">
						<a href="./doCancelRecommend.sbs?id=<%=id%>&rType=1">추천</a>
					</span>
					
				<% } else { %>
					<span class="notYetRecommended recommendButton" >
						<a href="./doRecommend.sbs?id=<%=id%>&rType=1">추천</a>
					</span>
				<% } 
				}%>
				
				( 추천수 : <%=recommendsCount%> )
			
			</div>
		</div>
	</section>
	
	
	<div class="con reply">
	
		<h2 class="con">댓글 작성</h2>
		
		<div class="reply-form">
			<form class="con" action="./doAddReply.sbs" method="POST">
				
				<input type="hidden" name="articleId" value="<%=id%>">
								
				<% if (isNotLogined) { %>
					<div>
						<input type="password" name="passwd" placeholder="비밀번호" maxLength="20">
					</div>
				<% } %>
				
				<div>
					<textarea name="body" placeholder="내용"></textarea>
					<input type="submit" value="댓글작성">
				</div>
				
			</form>
		</div>
		
		<h2 class="con">댓글 목록(<%=replies.size() %>)</h2>
		
		<div class="reply-list table-common">
			<% if ( replies.size() < 1 ) { %>
				<div class="con">댓글이 없습니다.</div>
			<% } else { %>
			
			<table border="1" class="con article-replies-list">
				<thead>
					<tr>
						<th>번호</th>
						<th>날짜</th>
						<th>내용</th>
						<th>비고</th>
					</tr>
				</thead>
				<tbody>
					<% for ( Map<String, Object> reply : replies ) { %>
					
					<%
						boolean needToBtnDeleteRepluVisible = (long) reply.get("memberId") == 0L;
					
						if (loginedMemberId == (long) reply.get("memberId")) {
							needToBtnDeleteRepluVisible = true;
						}
					%>
					
					<tr>
						<td><%=reply.get("id")%></td>
						<td><%=reply.get("regDate")%></td>
						<td>
							<div class="read-mode-visible">
								<%=reply.get("body").toString().replaceAll("\n","<br>")%>
							</div>
							
							<div class="edit-mode-visible">
								<form action="./doModifyReply.sbs" method="POST">
									<input type="hidden" name="id" value="<%=reply.get("id")%>"> 
									<textarea name="body">
										<%=reply.get("body").toString().replaceAll("\n","\n")%> 
									</textarea>
									<% if ( (long) reply.get("memberId") == 0L ) { %>
										<input type="password" name="passwd">
									<% } %>
									<input type="submit" value="댓글수정">
									<input type="button" value="취소" onclick="disableEditMode(this);">
								</form>
							</div>
							
						</td>
						<td style="padding:2px;">
							<% if (needToBtnDeleteRepluVisible) { %>
								<a href="javascript:;" onclick="deleteArticleReply(<%=reply.get("id")%>,<%=reply.get("memberId")%>)" >삭제</a>
								<a class="read-mode-visible" href="javascript:;" onclick="enableEditMode(this);" >수정</a>
							<% } %>
		
							<% if ( (long)reply.get("memberId") != loginedMemberId ) {
								if ( ((long)reply.get("recommenderId")) != 0L ) { %>
									
									<span class="alreadyRecommended recommendButton">
										<a href="./doCancelRecommend.sbs?id=<%=reply.get("id")%>&rType=2">추천</a>
									</span>
									
								<% } else { %>
									<span class="notYetRecommended recommendButton" >
										<a href="./doRecommend.sbs?id=<%=reply.get("id")%>&rType=2">추천</a>
									</span>
								<% } 
								}%>
						</td>
					</tr>
					<% } %>
				</tbody>
			</table>
			
			<% } %>
		</div>
	</div>
</body>
</html>