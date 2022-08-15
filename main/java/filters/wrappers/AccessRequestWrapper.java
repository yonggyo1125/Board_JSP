package filters.wrappers;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.member.MemberDto;


/**
 * 접속 제한 처리 
 * 
 * @author YONGGYO
 *
 */
public class AccessRequestWrapper extends HttpServletRequestWrapper {
	public AccessRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	
	public AccessRequestWrapper(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		super(request);
		
		String URI = request.getRequestURI();
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		ResourceBundle common = ResourceBundle.getBundle("bundle.common");
		
		HttpSession session = request.getSession();
		MemberDto member = (session.getAttribute("member") == null)?null:(MemberDto)session.getAttribute("member");
		
		/** 회원 전용 URL 체크 S */
		String memberUrls = bundle.getString("member_urls");
		if (memberUrls != null && member == null) {
			for (String url : memberUrls.split(",")) {
				if (URI.indexOf(url) != -1) {
					request.setAttribute("errorMessage", common.getString("MEMBER_ONLY"));
					request.setAttribute("statusCode", 401);
					response.sendError(401);
					break;
				}
			}
		}
		/** 회원 전용 URL 체크 E */
		
		/** 비회원 전용 URL 체크 S */
		String guestUrls = bundle.getString("guest_urls");
		if (guestUrls != null && member != null) {
			for (String url : guestUrls.split(",")) {
				if (URI.indexOf(url) != -1) {
					request.setAttribute("errorMessage", common.getString("GUEST_ONLY"));
					request.setAttribute("statusCode", 401);
					response.sendError(401);
					break;
				}
			}
		}
		/** 비회원 전용 URL 체크 E */
		
		/** 관리자 전용 URL 체크 S */
		if (URI.indexOf("/admin") != -1 && (member == null || ! member.getMemType().equals("admin"))) {
			request.setAttribute("errorMessage", common.getString("ADMIN_ONLY"));
			request.setAttribute("statusCode", 401);
			response.sendError(401);
		}
		/** 관리자 전용 URL 체크 E */
	}	
}
