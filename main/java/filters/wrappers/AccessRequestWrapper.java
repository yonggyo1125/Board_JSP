package filters.wrappers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

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
}
