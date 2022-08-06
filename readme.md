## 로그인 

### 로그인 양식 추가 

#### src/main/java/controllers.member.LoginController.java

```
package controllers.member;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/login")
public class LoginController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		RequestDispatcher rd = req.getRequestDispatcher("/member/login.jsp");
		rd.forward(req, resp);
	}
}
```

#### src/main/webapp/member/login.jsp

```jsp
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.common" />
<layout:main title="로그인" bodyClass="login">
	<h1 class='mtitle'><fmt:message key="MEMBER_LOGIN" /></h1>
	<form id="frmLogin" name="frmLogin" class="form_box" method="post" action="<c:url value="/member/login" />" target="ifrmProcess" autocomplete="off">
		<dl>
			<dt class='mobile_hidden'><fmt:message key="MEMBER_MEMID" /></dt>
			<dd class="mobile_fullwidth">
				<input type="text" name="memId" placeholder="<fmt:message key="MEMBER_MEMID" />" />
			</dd>
		</dl>
		<dl>
			<dt class='mobile_hidden'><fmt:message key="MEMBER_MEMPW" /></dt>
			<dd class="mobile_fullwidth">
				<input type="password" name="memPw" placeholder="<fmt:message key="MEMBER_MEMPW" />" />
			</dd>
		</dl>
		<div class="btn_grp mt20">
			<button type="submit" class="black"><fmt:message key="MEMBER_LOGIN" /></button>
		</div>
	</form>
</layout:main>
```

### 로그인 처리 

#### src/main/java/models.member.MemberException.java

```
package models.member;

import java.util.ResourceBundle;

public class MemberException extends RuntimeException {
	
	public static ResourceBundle bundle;
	public static String message;
	
	static {
		bundle = ResourceBundle.getBundle("bundle.common");
	}
	
	public MemberException() {}
	
	... 생략
}
```

#### src/main/java/models/member/validation/MemberNotFoundException.java

- 로그인 양식에 입력한 아이디로 검색했을 때 회원을 찾지 못하면 발생하는 예외
- 기본 메세지 :  MEMBER_NOT_FOUND

```java
package models.member.validation;

import models.member.MemberException;

/**
 * 회원을 찾지 못하였을 경우 발생 
 * 
 * @author YONGGYO
 *
 */
public class MemberNotFoundException extends MemberException {
	
	public MemberNotFoundException() {
		this(bundle.getString("MEMBER_NOT_FOUND"));
	}
	
	public MemberNotFoundException(String message) {
		super(message);
	}
}
```

#### src/main/java/models/member/validation/LoginFailedException.java

- 로그인 실패(비밀번호 불일치, 기타 사유 실패)시 발생하는 예외 

```java
package models.member.validation;

import models.member.MemberException;

/**
 * 로그인 실패(비밀번호 불일치, 기타 사유 실패)시 발생 
 * 
 * @author YONGGYO
 *
 */
public class LoginFailedException extends MemberException {
	
	public LoginFailedException() {
		this(bundle.getString("MEMBER_LOGIN_FAILED"));
	}
	
	public LoginFailedException(String message) {
		super(message);
	}
}

```

#### /WEB-INF/classes/bundle/common_ko.properties 

```
... 생략

MEMBER_NOT_FOUND=등록된 회원이 아닙니다.
MEMBER_LOGIN_FAILED=로그인에 실패하였습니다.
MEMBER_PASSWORD_INCORRECT=비밀번호가 일치하지 않습니다.
```

#### /WEB-INF/classes/bundle/common_en.properties 

```
... 생략

MEMBER_NOT_FOUND=Not Registered Member.
MEMBER_LOGIN_FAILED=Login is failed
MEMBER_PASSWORD_INCORRECT=Your password is not correct.
```

#### src/main/java/models/member/LoginService.java

- 로그인 처리 Service

```java
package models.member;

import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.bcrypt.BCrypt;

import models.member.validation.MemberValidator;
import models.member.validation.LoginFailedException;
import models.member.validation.MemberNotFoundException;

/**
 * 로그인 처리 
 * 
 * @author YONGGYO
 *
 */
public class LoginService implements MemberValidator {
	
	public void login(HttpServletRequest request) {
		ResourceBundle bundle = ResourceBundle.getBundle("bundle.common");
		
		/** 필수 입력 항목 체크 S */
		HashMap<String, String> checkFields = new HashMap<>();
		checkFields.put("memId", bundle.getString("MEMBER_REQUIRED_MEMID"));
		checkFields.put("memPw", bundle.getString("MEMBER_REQUIRED_MEMPW"));
		requiredCheck(request, checkFields);
		/** 필수 입력 항목 체크 E */
		
		/** 아이디 및 비밀번호 체크 S */
		String memId = request.getParameter("memId");
		String memPw = request.getParameter("memPw");
		
		MemberDao dao = MemberDao.getInstance();
		
		MemberDto member = dao.get(memId);
		if (member == null) {
			throw new MemberNotFoundException();
		}
		
		// 비밀번호 체크 - 해시 일치 여부 체크 
		if (!BCrypt.checkpw(memPw, member.getMemPw())) {
			throw new LoginFailedException(bundle.getString("MEMBER_PASSWORD_INCORRECT"));
		}
		
		/** 아이디 및 비밀번호 체크 E */
		
		/** 회원 정보 세션 처리 S */
		HttpSession session = request.getSession();
		member.setMemPw("");
		session.setAttribute("member", member);
		/** 회원 정보 세션 처리 E */
	}
}
```

- 아이디, 비밀번호는 필수 입력항목으로 체크

```java
HashMap<String, String> checkFields = new HashMap<>();
checkFields.put("memId", bundle.getString("MEMBER_REQUIRED_MEMID"));
checkFields.put("memPw", bundle.getString("MEMBER_REQUIRED_MEMPW"));
requiredCheck(request, checkFields);
```

- 등록된 회원인지 체크 

```java
MemberDto member = dao.get(memId);
if (member == null) {
	throw new MemberNotFoundException();
}
```

- 비밀번호 해시 검증(Bcrypt) 

```java
if (BCrypt.checkpw(memPw, member.getMemPw())) {
	throw new LoginFailedException(bundle.getString("MEMBER_PASSWORD_INCORRECT"));
}
```

> Bcrypt 방식은 유동 해시로 동일한 값에 대해 생성할 때마다 다른 해시값이 생성되므로 해시 검증 알고리즘이 구현된 별도 메서드로 일치여부를 체크한다.

- 회원 정보 유지를 위한 세션 처리 
- 비밀번호는 민감한 정보이므로 해시된 데이터라도 비워둔채 세션에 저장한다.

```java
HttpSession session = request.getSession();
member.setMemPw("");
session.setAttribute("member", member);
```

#### src/main/java/controllers/member/LoginController.java

```java

... 생략 

import models.member.LoginService;
import static commons.Utils.*;

@WebServlet("/member/login")
public class LoginController extends HttpServlet {

	... 생략 
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			LoginService service = new LoginService();
			service.login(req);
			
			// 로그인 성공시 메인페이지로 이동 
			String url = req.getContextPath();
			go(resp, url, "parent");
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}
}
```

* * * 
## 로그아웃

#### src/main/java/models/member/LogoutService.java

- 로그아웃 Service

```java
package models.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 로그아웃 처리
 * 
 * @author YONGGYO
 *
 */
public class LogoutService {
	
	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
	}
}
```

#### src/main/java/controllers/member/LogoutController.java

- 로그아웃 처리 Controller

```java
package controllers.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.member.LogoutService;

@WebServlet("/member/logout")
public class LogoutController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		LogoutService service = new LogoutService();
		service.logout(req);
		
		// 로그아웃 완료 후 메인페이지로 이동
		resp.sendRedirect(req.getContextPath());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}
```

- 로그아웃 요청이 오면 LogoutService 에서 처리 

```java 
LogoutService service = new LogoutService();
service.logout(req);
```

- 로그아웃은 POST 요청 형태로로 유입이 될 수 있으므로 doGet(req, resp); 를 추가하여 공통 소스를 하나의 메서드(doGet)에서 처리

```java
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	doGet(req,resp);
}
```

- 로그아웃이 완료되면 메인페이지로 이동
```java
resp.sendRedirect(req.getContextPath());
```

#### WEB-INF/classes/bundle/common_ko.properties

```
... 생략 

#회원
MEMBER_JOIN=회원가입
MEMBER_LOGIN=로그인
MEMBER_LOGOUT=로그아웃

... 생략

# 마이페이지
MYPAGE=마이페이지
```

#### WEB-INF/classes/bundle/common_en.properties

```

... 생략 

#회원
MEMBER_JOIN=JOIN
MEMBER_LOGIN=LOG IN
MEMBER_LOGOUT=LOG OUT

... 생략 

# 마이페이지
MYPAGE=MY PAGE
```

#### WEB-INF/tags/layouts/main.tag

```jsp

... 생략

<jsp:attribute name="header">
	<section class='top_menu'>
		<div class="layout_width">
			<c:if test="${ empty member }">
				<a href="<c:url value="/member/join" />"><fmt:message key="MEMBER_JOIN" /></a>
				<a href="<c:url value="/member/login" />"><fmt:message key="MEMBER_LOGIN" /></a>
			</c:if>
			<c:if test="${ ! empty member }">
				<a href="<c:url value="/mypage" />"><fmt:message key="MYPAGE" /></a>
				<a href="<c:url value="/member/logout" />"><fmt:message key="MEMBER_LOGOUT" /></a>
			</c:if>
		</div>
	</section>
	
	... 생략
	
</jsp:attribute>

... 생략

```


#### 로그인/로그아웃 구현 화면

![image2](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/8%EC%9D%BC%EC%B0%A8(3h)%20-%20%EC%BF%A0%ED%82%A4%EC%99%80%20%EC%84%B8%EC%85%98/images/project/image2.png)

![image1](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/8%EC%9D%BC%EC%B0%A8(3h)%20-%20%EC%BF%A0%ED%82%A4%EC%99%80%20%EC%84%B8%EC%85%98/images/project/image1.png)


* * * 
## 아이디 저장 기능 구현

#### WEB-INF/classes/bundle/common_ko.properties

```

... 생략

MEMBER_PASSWORD_INCORRECT=비밀번호가 일치하지 않습니다.
MEMBER_SAVE_MEMID=아이디 저장

... 생략 

```

#### WEB-INF/classes/bundle/common_en.properties

```
... 생략

MEMBER_PASSWORD_INCORRECT=Your password is not correct.
MEMBER_SAVE_MEMID=Save ID

... 생략

```

#### src/main/webapp/member/login.jsp

```jsp
<dl>
	<dt class='mobile_hidden'><fmt:message key="MEMBER_MEMPW" /></dt>
	<dd class="mobile_fullwidth">
		<input type="text" name="memId" value="${empty cookie.savedMemId ? '' : cookie.savedMemId.value }" placeholder="<fmt:message key="MEMBER_MEMID" />" />
	</dd>
</dl>
<div class="ar mt10">
	<input type="checkbox" name="saveMemId" id="saveMemId"${empty cookie.savedMemId ? '' : ' checked' }>
	<label for="saveMemId"><fmt:message key="MEMBER_SAVE_MEMID" /></label>
</div>

... 생략
		
```


#### src/main/java/controllers/member/LoginController.java

```java

... 생략 

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;

... 생략

@WebServlet("/member/login")
public class LoginController extends HttpServlet {
	
	... 생략
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			LoginService service = new LoginService();
			service.login(req);
			
			/** 아이디 저장 처리 S */
			String memId = req.getParameter("memId");
			String saveMemId = req.getParameter("saveMemId");
			if (saveMemId == null) { 
				// 아이디 저장이 아니면 쿠키 삭제
				for (Cookie cookie : req.getCookies()) {
					if (cookie.getName().equals("savedMemId")) {
						cookie.setMaxAge(0);
					}
				}
			} else { 
				// 아이디 저장이라면 쿠기 추가 
				resp.addCookie(new Cookie("savedMemId", memId));
			}
			/** 아이디 저장 처리 E */
			
			// 로그인 성공시 메인페이지로 이동 
			String url = req.getContextPath();
			go(resp, url, "parent");
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			alertError(resp, e);
		}
	}	
}
```

- 아이디 저장 체크를 해제한 경우 쿠키를 삭제합니다.
- 쿠키 삭제는 쿠키 만료시간을 0으로 지정하면 브라우저에서 자동으로 삭제합니다. <code>cookie.setMaxAge(0);</code>
- 아이디 저장을 체크한 경우는 쿠키를 저장합니다. 

```java
if (saveMemId == null) { 
	// 아이디 저장이 아니면 쿠키 삭제
	for (Cookie cookie : req.getCookies()) {
		if (cookie.getName().equals("savedMemId")) {
			cookie.setMaxAge(0);
		}
	}
} else { 
	// 아이디 저장이라면 쿠기 추가 
	resp.addCookie(new Cookie("savedMemId", memId));
}
```


### 구현 화면

![image3](https://raw.githubusercontent.com/yonggyo1125/curriculum300H/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/8%EC%9D%BC%EC%B0%A8(3h)%20-%20%EC%BF%A0%ED%82%A4%EC%99%80%20%EC%84%B8%EC%85%98/images/project/image3.png)