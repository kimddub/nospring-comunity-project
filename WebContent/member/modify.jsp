<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	String pageTitle = "회원정보 수정";
	Map<String, Object> memberInfo = Controller.getLoginedMemberInfo(request);
	
%>

<%@ include file="../part/head.jspf" %>
<link rel="stylesheet" href="../Resources/member.css">

	<h1 class="con"><%=pageTitle%></h1>
	
	
	<form action="./doModify.sbs" method="POST">
		<div class="member-form table-common con">
			<table>
				<tbody>
					<tr>
						<td><input type="text" name="name" value="<%=memberInfo.get("name")%>"></td>
					</tr>	
					
					<tr>
						<td>
							<input type="radio" name="gender" value="M">남자 
							<input type="radio" name="gender" value="W">여자
						
							<script>
								var gender = <%="'" + memberInfo.get("gender") + "'"%>;
							</script>
						
							<script>
								$('form input[name="gender"][value="' + gender + '"]').prop('checked', true);
							</script>
						</td>
					</tr>
					
					<tr>
						<td><input type="password" name="loginPw" placeholder="기존 비밀번호"></td>
					</tr>
					
					<tr>
						<td><input type="password" name="newLoginPw" placeholder="새 비밀번호"></td>
					</tr>
					
					<tr>
						<td><input type="password" name="newLoginPwConfirm" placeholder="새 비밀번호 확인"></td>
					</tr>
					
					<tr>
						<td>
							<input type="submit" value="수정">
							<input type="button" value="취소" onclick="location.href='<%=request.getHeader("REFERER")%>';">
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
		
</body>
</html>