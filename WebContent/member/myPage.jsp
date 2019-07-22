<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String pageTitle = "마이페이지";
	Map<String, Object> memberInfo = Controller.getLoginedMemberInfo(request);
	
	String gender = memberInfo.get("gender").equals("M") ? "남자" : "여자";
%>

<%@ include file="../part/head.jspf" %>
<link rel="stylesheet" href="../Resources/member.css">

	<h1 class="con"><%=pageTitle%></h1>

	<div class="member-form table-common con">
		<table border="1">
			<tr>
				<th>이름</th>
				<td><%=memberInfo.get("name") %></td>
			</tr>
			
			<tr>
				<th>별명</th>
				<td><%=memberInfo.get("nickName") %></td>
			</tr>
			
			<tr>
				<th>성별</th>
				<td><%=gender%></td>
			</tr>
		</table>
	</div>
	
</body>
</html>