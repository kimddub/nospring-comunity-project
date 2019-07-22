<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String pageTitle = "로그인";
%>

<%@ include file="../part/head.jspf" %>
<link rel="stylesheet" href="../Resources/member.css">

	<h1 class="con"><%=pageTitle%></h1>
	
	<form action="./doLogin.sbs">
		<input type="hidden" name="redirectUrl" value="<%=request.getParameter("redirectUrl") %>">
		
		<div class="member-form table-common con">
			<table>
				<tbody>
					<tr>
						<td><input type="text" name="loginId" placeholder="아이디"></td>
					</tr>
					<tr>
						<td><input type="password" name="loginPw" placeholder="비밀번호"></td>
					</tr>
					<tr>
						<td>
							<input type="submit" value="로그인">
							<input type="button" onclick="location.href='../article/list.sbs';" value="취소">
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
	</form>

</body>
</html>