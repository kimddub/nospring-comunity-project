<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String pageTitle = "게시글 수정";
%>

<%@ include file="../part/head.jspf" %>
<link rel="stylesheet" href="../Resources/article.css">

<%

	long loginedMemberId = Controller.getLoginedMemberId(request);
	boolean isNotLogined = Controller.isNotLogined(request);
	int id = Integer.parseInt(request.getParameter("id"));
	Map<String,Object> article = (Map<String,Object>)request.getAttribute("article");
%>

	<h1 class="con"><%=pageTitle%></h1>
	
	<form action="./doModify.sbs" method="POST">
		<input type="hidden" name="id" value="<%=id%>">
		
		<div class="edit-form-table article-table table-common con">
			<table>
				<colgroup>
	                <col width="100px">
	            </colgroup>
            
				<tbody>
					<tr>
						<td>
							<input type="text" name="title" placeholder="제목" value="<%=article.get("title")%>">
						</td>
					</tr>
					
					<tr class="article-body">
						<td>
							<textarea name="body" placeholder="내용"><%=article.get("body").toString().replaceAll("\n","\n") %></textarea>
						</td>
					</tr>
					
					<% if ( isNotLogined ) { %>
					<tr>
						<td>
							<input type="password" name="passwd" placeholder="비밀번호" maxLength="20">
							<div>비밀번호는 최대 20자리 입니다.</div>
						</td>
					</tr>
					<% } %>
					
					<tr class="btn-box">
						<td>
							<input type="submit" value="수정">
							<input type="button" value="취소" onclick="location.href='<%=request.getHeader("referer")%>';">
						</td>
					</tr>
	</form>

</body>
</html>