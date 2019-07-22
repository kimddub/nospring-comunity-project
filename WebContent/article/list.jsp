<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String pageTitle = "게시글 목록";
%>

<%@ include file="../part/head.jspf" %>
<link rel="stylesheet" href="../Resources/article.css">

<%
	long loginedMemberId = Controller.getLoginedMemberId(request);
	boolean isNotLogined = Controller.isNotLogined(request);
	List<Map<String,Object>> articles = (List<Map<String,Object>>)request.getAttribute("articles");
%>

	<h1 class="con"><%=pageTitle%></h1>
	
	<section class="article-list table-common con">
		<table>
			<colgroup>
				<col width="100px">
				<col width="200px">
				<col>
				<col width="70px">
				<col width="70px">
				<col width="70px">
				<col width="60px">
			</colgroup>
			<thead>
		    	<tr>
					<th>번호</th>
					<th>날짜</th>
					<th>제목</th>
					<th>댓글</th>
					<th>조회수</th>
					<th>추천</th>
					<th></th>
				</tr>
		  	</thead>
		  	<tbody>
			    <% for ( Map<String, Object> article : articles ) { %>
				<%
					boolean needToBtnDeleteVisible = (long) article.get("memberId") == 0L;
		
					if (loginedMemberId == (long) article.get("memberId")) {
						needToBtnDeleteVisible = true;
					}
				%>
				<tr>
					<td><%=article.get("id")%></td>
					<td><%=article.get("regDate")%></td>
					<td class="article-title"><a href="./detail.sbs?id=<%=article.get("id")%>"><%=article.get("title")%></a></td>
					<td><%=article.get("countReply")%></td>
					<td><%=article.get("hit") %></td>
					<td><%=article.get("countRecommend") %></td>
					<td class="article-delete">
						<% if ( needToBtnDeleteVisible ) { %>
							<span class="article-delete-btn" onclick="deleteArticle(<%=article.get("id")%>,<%=article.get("memberId")%>)"></div>
						<% } %>
					</td>
				</tr>
				<% } %>
			  </tbody>
		</table>
	</section>

</body>
</html>