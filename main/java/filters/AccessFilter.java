package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import filters.wrappers.AccessRequestWrapper;

/**
 * 접속 통제 Filter
 * 
 * @author YONGGYO
 */
public class AccessFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		chain.doFilter(new AccessRequestWrapper((HttpServletRequest)request), response);
	}
}