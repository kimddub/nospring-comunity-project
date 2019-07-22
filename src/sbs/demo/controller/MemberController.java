package sbs.demo.controller;

import java.util.Map;

public class MemberController extends Controller {

	public void _join() {
		forwardJsp("/member/join.jsp");
	}
	
	public void _doJoin() {
		String name = request.getParameter("name");
		String nickname = request.getParameter("nickname");
		String loginId = request.getParameter("loginId");
		String loginPw = request.getParameter("loginPw");
		String loginPwConfirm = request.getParameter("loginPwConfirm");
		String gender = request.getParameter("gender");
		
		if ( !loginPw.equals(loginPwConfirm) ) {
			rs.append("<script>alert('비밀번호가 일치하지 않습니다.'); history.back(); </script>");
			return;
		}
		
		String sql = "SELECT COUNT(*) " + 
				"FROM member " + 
				"WHERE loginId = '" + loginId + "';";
		
		boolean notPermission = dbLink.getRowIntValue(sql) > 0 ;
		
		if ( notPermission ) {
			rs.append("<script>alert('해당 ID는 사용할 수 없습니다.'); history.back(); </script>");
			return;
		}
		
		sql = "INSERT INTO member " + 
				"SET regDate = NOW(), " + 
				"`name` = '" + name + "', " + 
				"nickName = '" + nickname + "', " + 
				"loginId = '" + loginId + " ', " + 
				"loginPasswd = '" + loginPw + "', " + 
				"gender = '" + gender + "';";
		
		dbLink.executeQuery(sql);
		
		rs.append("<script>alert('회원가입이 완료되었습니다. 로그인 후 이용해주세요.')</script>;");
		
		// 회원가입이 끝나면 로그인 페이지로 간다.
		// 로그인이 성공하면 이전 페이지로 돌아가야 한다.
		// 그 페이지가 로그인이나 회원가입 페이지는 아니어야 한다. (1차 대처 head에서 ""로 지정해준다.)
		// 회원가입 전에 있던 페이지를 기억해둔다.
		// join > doJoin > login > doLogin 까지 기억해가야 한다.
		
		String redirectUrl = request.getParameter("redirectUrl");
		
		// 2차 대처 이 전 주소값 기록이 없다면 돌아갈 페이지를 "마이페이지"로 지정한다. 
		if (redirectUrl == null || redirectUrl.length() == 0) {
			redirectUrl = "../member/myPage.sbs";
		}

		// 2차 대처 이 전 주소값 기록이 로그인이나 회원가입 페이지라면 마이페이지로 수정한다. 2차 방지
		if (redirectUrl.indexOf("login.sbs") != -1 || redirectUrl.indexOf("join.sbs") != -1) {
			redirectUrl = "../member/myPage.sbs";
		}
		
		rs.append("<script>location.replace('./login.sbs?redirectUrl=" + redirectUrl + "');</script>");
	}
	
	public void _login( ) {
		forwardJsp("/member/login.jsp");
	}
	
	public void _doLogin() {
		
		String loginId = request.getParameter("loginId");
		String loginPw = request.getParameter("loginPw");
		
		String sql = "SELECT COUNT(*) " + 
				"FROM member " + 
				"WHERE loginId = '" + loginId + "';";
		
		boolean checkLoginId = dbLink.getRowIntValue(sql) > 0 ;
		
		sql = "SELECT COUNT(*) " + 
				"FROM member " + 
				"WHERE loginId = '" + loginId + "' AND loginPasswd = '" + loginPw + "';";
		
		boolean checkLloginPw = dbLink.getRowIntValue(sql) > 0 ;
				
		if ( !checkLoginId ) {
			rs.append("<script>alert('존재하지 않는 ID입니다.'); history.back(); </script>");
			return;
		}
		
		if ( !checkLloginPw ) {
			rs.append("<script>alert('비밀번호가 틀렸습니다.'); history.back(); </script>");
			return;
		}
		
		Map<String, Object> memberInfo = dbLink.getRow("SELECT * FROM member WHERE loginId = '" + loginId + "'; ");
		
		request.getSession().setAttribute("loginedMemberId", memberInfo.get("id"));
		
		String redirectUrl = request.getParameter("redirectUrl");
		
		if (redirectUrl == null || redirectUrl.length() == 0) {
			redirectUrl = "../member/myPage.sbs";
		}

		if (redirectUrl.indexOf("login.sbs") != -1 || redirectUrl.indexOf("join.sbs") != -1) {
			redirectUrl = "../member/myPage.sbs";
		}
		
		rs.append("<script> alert('" + memberInfo.get("nickName") + "님 환영합니다.'); location.replace('" + redirectUrl + "'); </script>");
		 
	}
	
	public void _doLogout() {
		
		request.getSession().removeAttribute("loginedMemberId");

		rs.append("<script> alert('로그아웃 되었습니다.'); location.replace('" + request.getHeader("REFERER") + "'); </script>");
		
	}
	
	public void _myPage() {
		
		boolean isNotLogined = Controller.isNotLogined(request);

		if (isNotLogined) {
			rs.append("<script> alert('로그인 후 이용해주세요'); location.replace('../member/login.sbs?redirectUrl=../member/myPage.sbs'); </script>");

			return;
		}
		
		forwardJsp("/member/myPage.jsp");
		
	}
	
	public void _modify() {
		
		boolean isNotLogined = Controller.isNotLogined(request);

		if (isNotLogined) {
			String msg = "로그인 후 접근해주세요.";
			rs.append("<script> alert('" + msg
					+ "'); location.replace('../member/login.sbs?redirectUrl=../member/modify.sbs'); </script>");

			return;
		}
		
		forwardJsp("/member/modify.jsp");
		
	}
	
	public void _doModify() {
		
		long id = (long)request.getSession().getAttribute("loginedMemberId");
		
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String loginPw = request.getParameter("loginPw");
		String newLoginPw = request.getParameter("newLoginPw");
		String newLoginPwConfirm = request.getParameter("newLoginPwConfirm");
		
		String sql = "SELECT COUNT(*) " + 
				"FROM member " + 
				"WHERE id = '" + id + "' AND loginPasswd = '" + loginPw + "';";
		
		boolean checkLloginPw = dbLink.getRowIntValue(sql) > 0 ;
		
		if ( !checkLloginPw ) {
			rs.append("<script>alert('비밀번호가 틀렸습니다.'); history.back(); </script>");
			return;
		}
		
		if ( !newLoginPw.equals(newLoginPwConfirm) ) {
			rs.append("<script>alert('비밀번호가 일치하지 않습니다.'); history.back(); </script>");
			return;
		}
		
		sql = "UPDATE member SET `name` = '" + name + "', "
				+ "gender = '" + gender + "', "
				+ "loginPasswd = '" + newLoginPw + "' "
				+ "WHERE id = '" + id + "'; ";
				
		dbLink.executeQuery(sql);
		
		rs.append("<script>alert('회원정보가 수정되었습니다.'); location.href='./myPage.sbs'; </script>");
		
	}
	
}
