package sbs.demo.controller;

import java.util.List;
import java.util.Map;

public class ArticleController extends Controller {

	public void _list() {

		String sql = "SELECT A.*, " + 
				"COUNT(DISTINCT R.id) AS countRecommend, " + 
				"COUNT(DISTINCT AR.id) AS countReply " + 
				"FROM article AS A " + 
				"LEFT JOIN recommend AS R " + 
				"ON A.id = R.rId " + 
				"AND R.rType = 1 " + 
				"LEFT JOIN articleReply AS AR " + 
				"ON A.id = AR.articleId " + 
				"GROUP BY A.id ORDER BY A.id DESC;";
		
		List<Map<String,Object>> articles = dbLink.getRows(sql);
		
		request.setAttribute("articles", articles);
		
		forwardJsp("/article/list.jsp");
	}
	
	public void _detail() {
		
		long loginedMemberId = Controller.getLoginedMemberId(request);
		int id = Integer.parseInt(request.getParameter("id"));
		
		String sql = "SELECT * FROM article WHERE id = '" + id + "';";
		
		Map<String, Object> article = dbLink.getRow(sql);
		
		sql = "UPDATE article SET hit = hit + 1 WHERE id = '" + id + "';";
		
		dbLink.executeQuery(sql);
		
		sql = "SELECT A.*, IF(R.recommenderId IS NULL,0,R.recommenderId) AS recommenderId " + 
				"FROM articleReply AS A " + 
				"LEFT JOIN ( " + 
				"    SELECT rId,rType, memberId AS recommenderId " + 
				"    FROM recommend  " + 
				")AS R " + 
				"ON A.id = R.rId AND R.recommenderId = '" + loginedMemberId + "' AND R.rType = '2' " + 
				"HAVING A.articleId = '" + id + "' ORDER BY A.id DESC;";
		
		List<Map<String,Object>> replies = dbLink.getRows(sql);
		
		sql = "SELECT COUNT(*) " + 
				"FROM recommend " + 
				"WHERE rType = 1 AND rId = '" + id + "';";
		
		int recommendsCount = dbLink.getRowIntValue(sql);
				
		sql = "SELECT COUNT(*) FROM recommend WHERE memberId = '" + loginedMemberId + "' AND " +
				"rType = '" + 1 + "' AND rId = '" + id + "';";

		int alreadyRecommededArticle = dbLink.getRowIntValue(sql);
		
		sql = "SELECT COUNT(*) FROM article WHERE memberId = '" + loginedMemberId + "' AND id = '" + id + "';";

		int myArticle = dbLink.getRowIntValue(sql);

		request.setAttribute("article", article);
		request.setAttribute("replies", replies);
		request.setAttribute("alreadyRecommededArticle", alreadyRecommededArticle);
		request.setAttribute("myArticle", myArticle);
		request.setAttribute("recommendsCount", recommendsCount);
		
		forwardJsp("/article/detail.jsp");
	}
	
	public void _add() {
		
		forwardJsp("/article/add.jsp");
		
	}
	
	public void _doAdd() {
		boolean isNotLogined = Controller.isNotLogined(request);
		long loginedMemberId = Controller.getLoginedMemberId(request);
		
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		String passwd = "0";
		
		if ( isNotLogined ) {
		
			passwd = request.getParameter("passwd");
		
		} 
		
		String sql = "INSERT INTO article " + 
				"SET regDate = NOW(), " + 
				"memberId ='" + loginedMemberId + "', " + 
				"title = '" + title + "', " + 
				"body = '" + body + "', " + 
				"passwd = '" + passwd + "', " +
				"hit = '0'; ";
		
		System.out.println(title + "," + body + "," + loginedMemberId + "," + passwd);
		dbLink.executeQuery(sql);
		
		int newArticleId = dbLink.getLastInsertId();
		
		rs.append("<script>alert('게시물이 성공적으로 작성되었습니다.');");
		rs.append("location.replace('./detail.sbs?id=" + newArticleId + "'); </script>");
		
	}
	
	public void _modify() {
		
		int id = Integer.parseInt(request.getParameter("id"));
		
		String sql = "SELECT * FROM article WHERE id = '" + id + "';";
		
		Map<String, Object> article = dbLink.getRow(sql);
		
		request.setAttribute("article", article);
		
		forwardJsp("/article/modify.jsp");
	}
	
	public void _doModify() {

		boolean isNotLogined = Controller.isNotLogined(request);
		
		int id = Integer.parseInt(request.getParameter("id"));
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		
		String passwd = "0";
		
		if ( isNotLogined ) {
		
			passwd = request.getParameter("passwd");
		
		} 
		
		String sql = "SELECT COUNT(*) FROM article WHERE id = '" + id + "' AND passwd = '" + passwd +"';";
		
		boolean notPermission = dbLink.getRowIntValue(sql) == 0;
		
		if ( notPermission ) {
		
			rs.append("<script>alert('비밀번호가 일치하지 않습니다.'); history.back();</script>");
			return;
		
		}
		
		sql = "UPDATE article SET "
				+ "title = '" + title + "', "
				+ "body = '" + body + "' "
				+ "WHERE id = '" + id + "';";
		
		dbLink.executeQuery(sql);
		
		rs.append("<script>alert('게시물이 성공적으로 수정되었습니다.');");
		rs.append("location.replace('./detail.sbs?id="+ id + "');</script>");
		
	}
	
	public void _doDelete() {
		
		int id = Integer.parseInt(request.getParameter("id"));
		
		String passwd = request.getParameter("passwd");
		
		String sql = "SELECT COUNT(*) FROM article WHERE id = '" + id + "' AND passwd = '" + passwd +"';";
		
		boolean notPermission = dbLink.getRowIntValue(sql) == 0;
		
		if ( notPermission ) {
		
			rs.append("<script>alert('비밀번호가 일치하지 않습니다.'); history.back();</script>");
			return;
		
		}
		
		sql = "DELETE FROM article WHERE id = '" + id + "';";
		
		dbLink.executeQuery(sql);
		
		rs.append("<script>alert('게시물이 성공적으로 삭제되었습니다.');");
		rs.append("location.replace('./list.sbs');</script>");
		
	}
	
	public void _doAddReply() {
		
		boolean isNotLogined = Controller.isNotLogined(request);
		long loginedMemberId = Controller.getLoginedMemberId(request);
		
		int articleId = Integer.parseInt(request.getParameter("articleId"));
		String body = request.getParameter("body");
		String passwd = "0";
		
		String sql = "";
		
		if ( isNotLogined ) {
			passwd = request.getParameter("passwd");
		}
		
		sql = "INSERT INTO articleReply " + 
				"SET regDate = NOW(), " + 
				"memberId = '" + loginedMemberId + "', " + 
				"articleId = '" + articleId + "', " + 
				"body = '" + body + "', " + 
				"passwd = '" + passwd + "';";
		
		dbLink.executeQuery(sql);
		
		rs.append("<script>alert('댓글이 성공적으로 작성되었습니다.');");
		rs.append("location.replace('./detail.sbs?id=" + articleId + "');</script>");
		
	}
	
	public void _doModifyReply() {
		
		boolean isNotLogined = Controller.isNotLogined(request);
		
		int id = Integer.parseInt(request.getParameter("id"));
		String body = request.getParameter("body");
		String passwd = "0";
		String sql = "";
		
		if ( isNotLogined ) {
		
			passwd = request.getParameter("passwd");
			
			sql = "SELECT COUNT(*) FROM articleReply WHERE id = '" + id + "' AND passwd = '" + passwd +"';";
			
			boolean notPermission = dbLink.getRowIntValue(sql) == 0;
			
			if ( notPermission ) {
			
				rs.append("<script>alert('비밀번호가 일치하지 않습니다.'); history.back();</script>");
				return;
			
			} 
		}
		
		sql = "UPDATE articleReply " + 
				"SET body = '" + body + "', " + 
				"passwd = '" + passwd + "' " +
				"WHERE id = '" + id + "';";
		
		dbLink.executeQuery(sql);
		
		rs.append("<script>alert('댓글이 성공적으로 작성되었습니다.');");
		rs.append("location.replace('" + request.getHeader("referer") + "');</script>");
		
	}
	
	public void _doDeleteReply() {
		
		int id = Integer.parseInt(request.getParameter("id"));
		String passwd = request.getParameter("passwd");
		
		String sql = "SELECT COUNT(*) FROM articleReply WHERE id = '" + id + "' AND passwd = '" + passwd +"';";
		
		boolean notPermission = dbLink.getRowIntValue(sql) == 0;
		
		if ( notPermission ) {
		
			rs.append("<script>alert('비밀번호가 일치하지 않습니다.'); history.back();</script>");
			return;
		
		} 
		
		sql = "DELETE FROM articleReply " + 
				"WHERE id = '" + id + "';";
		
		dbLink.executeQuery(sql);
		
		rs.append("<script>alert('댓글이 성공적으로 삭제되었습니다.');");
		rs.append("location.replace('" + request.getHeader("referer") + "');</script>");
		
	}
	
	public void _doRecommend() {
		
		int id = Integer.parseInt(request.getParameter("id"));
		int rType = request.getParameter("rType").equals("1") ? 1 : 2;
		String rTable = rType == 1 ? "article" : "articleReply"; 
		long loginedMemberId = Controller.getLoginedMemberId(request);
		boolean isNotLogined = Controller.isNotLogined(request);
		
		if (isNotLogined) {
			rs.append(
					"<script> alert('로그인 후 이용해주세요.'); location.replace('../member/login.sbs?redirectUrl=../member/myPage.sbs'); </script>");
			return;
		}
		
		String sql = "SELECT COUNT(*) " + 
		"FROM recommend  " + 
		"WHERE memberId = '" + loginedMemberId +"' " + 
		"AND rType = '" + rType + "' " + 
		"AND rId = '" + id + "';";

		boolean alreadyRecommeded = dbLink.getRowIntValue(sql)> 0;
		
		if ( alreadyRecommeded ) {
			rs.append("<script>alert('이미 추천되었습니다.'); history.back(); </script>");
			return ;
		}
		
		sql = "SELECT COUNT(*) FROM '" + rTable + "' WHERE memberId = '" + loginedMemberId + "' AND id = '" + id + "';";

		boolean myArticle = dbLink.getRowIntValue(sql)> 0;
		
		if ( myArticle ) {
			rs.append("<script>alert('나의 게시물을 추천할 수 없습니다.'); history.back(); </script>");
			return ;
		}
		
		sql = "INSERT INTO recommend " + 
				"SET regDate = NOW(), " + 
				"memberId = '" + loginedMemberId + "', " + 
				"rType = '" + rType + "', " + 
				"rId = '" + id + "';";
		
		dbLink.executeQuery(sql);
		
		rs.append("<script>alert('추천되었습니다.');");
		rs.append("location.replace('" + request.getHeader("referer") + "');</script>");
	}
	
	public void _doCancelRecommend() {
		
		int id = Integer.parseInt(request.getParameter("id"));
		int rType = request.getParameter("rType").equals("1") ? 1 : 2;
		long loginedMemberId = Controller.getLoginedMemberId(request);
		boolean isNotLogined = Controller.isNotLogined(request);
				
		String sql = "DELETE FROM recommend " + 
				"WHERE memberId = '" + loginedMemberId + "' AND " + 
				"rType = '" + rType + "' AND " + 
				"rId = '" + id + "';";
		
		dbLink.executeQuery(sql);
		
		rs.append("<script>alert('추천이 취소되었습니다.');");
		rs.append("location.replace('" + request.getHeader("referer") + "');</script>");
		
	}
	
}
