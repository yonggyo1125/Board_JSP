package filters.wrappers;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 공통 Request Filter Wrapper
 * 
 * @author YONGGYO
 */
public class CommonResponseWrapper extends HttpServletResponseWrapper {

	public CommonResponseWrapper(HttpServletResponse response) {
		super(response);
	}
}
