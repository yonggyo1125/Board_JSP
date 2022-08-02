package filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import filters.wrappers.CommonRequestWrapper;
import filters.wrappers.CommonResponseWrapper;

/**
 * 공통 Filter 
 * 
 * @author YONGGYO
 *
 */
public class CommonFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		/** 공통 필터 Wrapper 처리 S */
		CommonRequestWrapper requestWrapper = new CommonRequestWrapper((HttpServletRequest)request);
		CommonResponseWrapper responseWrapper = new CommonResponseWrapper((HttpServletResponse)response);
		chain.doFilter(requestWrapper, responseWrapper);
		/** 공통 필터 Wrapper 처리 E */
	}
	
}
