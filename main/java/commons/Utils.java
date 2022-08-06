package commons;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

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
	 * @param {String} target  : _self, parent, _blank(새창), _top   
	 */
	public static void go(HttpServletResponse response, String url, String target) {
		try {
			if (target == null) {
				target="_self";
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
		go(response, url, "_self");
	}
}
