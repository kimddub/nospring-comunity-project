package sbs.demo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sbs.demo.controller.Controller;
import sbs.demo.util.ClassUtil;
import sbs.demo.util.DBUtil;
import sbs.demo.util.StringUtil;

@WebServlet("*.sbs")
public class FrontServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		DBUtil.initIfNeed("com.mysql.cj.jdbc.Driver",
				"jdbc:mysql://localhost:3306/a22?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false", "root", "");

		// 요청에 대한 문자열을 UTF-8로 읽겠습니다.
		request.setCharacterEncoding("UTF-8");

		// 응답에 대한 문자열을 UTF-8로 작성하겠습니다.
		response.setCharacterEncoding("UTF-8");
		// 응답하는 파일은 최종적으로 utf-8 html문서가 될 것입니다.
		response.setContentType("text/html;charset=UTF-8");

		// 요청된 URI 를 `/`를 기준으로 자른다.
		String[] uriBits = request.getRequestURI().split("/");

		// controllerName 에는 컨트롤러명이 입력된다.
		// EX : article, member, ...
		String controllerName = uriBits[2];

		// funcName 에는 기능명이 입력된다.
		// EX : doAdd, add, login, doLogin
		String funcName = uriBits[3].replace(".sbs", "");

		String methodName = "_" + funcName;

		String fullClassName = "sbs.demo.controller." + StringUtil.cap(controllerName) + "Controller";

		Controller controller = (Controller) ClassUtil.getClassObj(fullClassName);

		controller.setRequest(request);
		controller.setResponse(response);

		controller.beforeAction();

		try {
			String rs;

			boolean methodCalled = ClassUtil.callMethodWithNoReturn(controller, methodName);

			if (methodCalled) {
				rs = controller.getRsString();

				if (rs.length() > 0) {
					response.getWriter().append(rs);
				}
			} else {
				response.getWriter().append("<h1>ERROR : 해당 액션이 존재하지 않습니다.</h1>");
			}
		} finally {
			controller.afterAction();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}