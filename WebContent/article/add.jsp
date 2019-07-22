<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String pageTitle = "게시글 추가";

	long loginedMemberId = Controller.getLoginedMemberId(request);
	boolean isNotLogined = Controller.isNotLogined(request);
%>

<%@ include file="../part/head.jspf" %>
<link rel="stylesheet" href="../Resources/article.css">

	<h1 class="con"><%=pageTitle%></h1>

	
	<form action="./doAdd.sbs" method="POST">
		<div class="edit-form-table article-table table-common con">
			<table>
				<tbody>
					<tr>
						<td>
							<input type="text" name="title" placeholder="제목">
						</td>
					</tr>
					
					<tr class="article-body">
						<td>
							<textarea name="body" placeholder="내용"></textarea>
						</td>
					</tr>
					
					
					<% if ( isNotLogined ) { %>
					<tr>
						<td>
							<input type="password" name="passwd" placeholder="비밀번호" maxLength="20">
							<div>비밀번호는 최대 20자리 입니다.</div>
						</td>
					<tr>
					<% } %>
					
					<tr class="btn-box">
						<td>
							<input type="submit" value="작성">
							<input type="button" value="취소" onclick="location.href='<%=request.getHeader("referer")%>';">
						</td>
					</tr>
					
				</tbody>
			</table>	
		</div>
	</form>
	
</body>
</html>