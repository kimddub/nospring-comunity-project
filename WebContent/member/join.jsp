<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String pageTitle = "회원가입";
%>

<%@ include file="../part/head.jspf" %>
<link rel="stylesheet" href="../Resources/member.css">

	<h1 class="con"><%=pageTitle%></h1>

	<form action="./doJoin.sbs" method="POST">
		<!-- doLogin < login < doJoin < join 에서 부터 이 전 페이지를 기억하기 시작 -->
		<input type="hidden" name="redirectUrl"
		value="<%=request.getParameter("redirectUrl") != null ? request.getParameter("redirectUrl") : ""%>">
		
		<div class="member-form table-common con">
			<table>
				<tbody>
				
					<tr>
						<td><input type="text" name="name" placeholder="이름"></td>
					</tr>
					
					<tr>
						<td><input type="text" name="nickname" placeholder="별명"></td>
					</tr>
					
					<tr>
						<td><input type="text" name="loginId" placeholder="아이디"></td>
					</tr>
					
					<tr>
						<td>
							<input type="password" name="loginPw" placeholder="비밀번호" maxLength="20">
							<div>비밀번호는 최대 20자리 입니다.</div>
						</td>
					</tr>
					
					<tr>
						<td><input type="password" name="loginPwConfirm" placeholder="비밀번호 확인" maxLength="20"></td>
					</tr>
					
					<tr>
						<td>
							<input checked type="radio" name="gender" value="M"> 남자 
							<input type="radio" name="gender" value="W"> 여자
						</td>
					</tr>
					
					<tr>
						<td>
							<input type="submit" value="가입신청">
							<input type="button" value="취소" onclick="location.href='<%=request.getHeader("referer")%>';">
						</td>
					</tr>
				
				</tbody>
			</table>
	</form>

</body>
</html>