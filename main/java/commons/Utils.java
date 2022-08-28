package commons;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.member.MemberDto;

/**
 * 유용한 공통 기능 모음
 * 
 * @author YONGGYO
 *
 */
public class Utils {
	/**
	 * 에러 alert 처리 
	 * 
	 * @param {HttpServletResponse} response
	 * @param {RuntimeException} e
	 * @param {String} addScripts 추가 실행 스크립트
	 */
	public static void alertError(HttpServletResponse response,  RuntimeException e, String addScripts) {
		try {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('" + e.getMessage() + "');");
			if (addScripts != null) {
				out.println(addScripts);
			}
			out.println("</script>");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
	public static void alertError(HttpServletResponse response, RuntimeException e) {
		alertError(response, e, null);
	}
	
	/**
	 * 메세지 출력 
	 * 
	 * @param {HttpServletResponse} response
	 * @param {String} message 출력 메세지
	 * @param {String} addScripts 추가 실행 스크립트
	 */
	public static void alert(HttpServletResponse response, String message, String addScripts) {
		try {
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('" + message + "');");
			if (addScripts != null) {
				out.println(addScripts);
			}
			out.println("</script>");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void alert(HttpServletResponse response, String message) {
		alert(response, message);
	}
	
	/**
	 * 페이지 이동
	 * 
	 * @param {HttpServletResponse} response
	 * @param {String} url  이동 URL 
	 * @param {String} target  : self, parent, top   
	 */
	public static void go(HttpServletResponse response, String url, String target) {
		try {
			if (target == null) {
				target="self";
			}
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println(target + ".location.replace('" + url + "');");
			out.println("</script>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void go(HttpServletResponse response, String url) {
		go(response, url, "self");
	}
	
	/**
	 * history.back 처리 
	 *  
	 * @param {HttpServletResponse} response
	 * @param {String} target  : self, parent, top   
	 */
	public static void back(HttpServletResponse response, String target) {
		try {
			if (target == null) {
				target="self";
			}
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println(target + ".history.back();");
			out.println("</script>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void back(HttpServletResponse response) {
		back(response, "self");
	}
	
	/**
	 * 페이지 새로고침 
	 * 
	 * @param {HttpServletResponse} response
	 * @param {String} target : self, parent, top
	 */
	public static void reload(HttpServletResponse response, String target) {
		try {
			if (target == null || target.isBlank()) target = "self";
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.printf("<script>%s.location.reload();</script>", target);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void reload(HttpServletResponse response) {
		reload(response, "self");
	}
	
	/**
	 * 로그인 여부 체크 
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isLogin(HttpServletRequest request) {
		
		return request.getSession().getAttribute("member") != null;
	}
	
	/**
	 * 로그인 회원정보 조회
	 * 
	 * @param request
	 * @return
	 */
	public static MemberDto getMember(HttpServletRequest request) {
		if (!isLogin(request)) {
			return null;
		}
		
		MemberDto member = (MemberDto)request.getSession().getAttribute("member");
		
		return member;
	}
	
	/**
	 * 관리자 여부 체크 
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isAdmin(HttpServletRequest request) {
		MemberDto member = getMember(request);
		if (member == null) {
			return false;
		}
		
		return member.getMemType().equals("admin");
	}
}
