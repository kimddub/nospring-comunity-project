package sbs.demo.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sbs.demo.util.DBUtil;
import sbs.demo.util.DBUtil.DBLink;

public class Controller {
	protected DBLink dbLink;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected StringBuilder rs;

	Controller() {
		rs = new StringBuilder();
	}

	public String getRsString() {
		return rs.toString();
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	// 요청에 대한 응답을 JSP로 위임한다.
	void forwardJsp(String jspPath) {
		ServletContext context = request.getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher(jspPath); // jspPath : 데이터 넘길 페이지 주소
		try {
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 액션 처리 전에 수행되어야 하는 메서드
	public void beforeAction() {
		dbLink = DBUtil.getNewDBLink();

		HttpSession session = request.getSession();

		boolean isNotLogined = session.getAttribute("loginedMemberId") == null;
		boolean isLogined = !isNotLogined;

		long loginedMemberId = 0;

		if (session.getAttribute("loginedMemberId") != null) {
			loginedMemberId = (long) session.getAttribute("loginedMemberId");
		}

		Map<String, Object> loginedMemberInfo = new HashMap<>();

		if (isLogined) {
			loginedMemberInfo = dbLink.getRow("SELECT * FROM member WHERE id = '" + loginedMemberId + "'");
		}

		request.setAttribute("isNotLogined", isNotLogined);
		request.setAttribute("isLogined", isLogined);
		request.setAttribute("loginedMemberId", loginedMemberId);
		request.setAttribute("loginedMemberInfo", loginedMemberInfo);
	}

	// 액션 처리 후에 수행되어야 하는 메서드
	public void afterAction() {
		dbLink.close();
	}

	// 로그인 되었는지 알려주는 메서드
	public static boolean isLogined(HttpServletRequest request) {
		return (boolean) request.getAttribute("isLogined");
	}

	// 로그인 안 되었는지 알려주는 메서드
	public static boolean isNotLogined(HttpServletRequest request) {
		return (boolean) request.getAttribute("isNotLogined");
	}

	// 로그인 된 회원의 번호를 리턴하는 메서드
	// 로그인이 안되어 있다면 null을 리턴하는 대신 0을 리턴한다.
	// 이 함수를 쓰면 null 체크를 안해도 되어서 편하다.
	public static long getLoginedMemberId(HttpServletRequest request) {
		return (long) request.getAttribute("loginedMemberId");
	}

	// 로그인 된 회원의 정보를 리턴하는 메서드
	// 로그인이 안되어 있다면 null을 리턴하는 대신 비어있는 HashMap을 리턴한다.
	// 이 함수를 쓰면 null 체크를 안해도 되어서 편하다.
	public static Map<String, Object> getLoginedMemberInfo(HttpServletRequest request) {
		return (Map<String, Object>) request.getAttribute("loginedMemberInfo");
	}

}