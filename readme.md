## 공통 레이아웃 구성 

#### /WEB-INF/tags/layouts/common.tag

```java
<%@ tag description="공통 레이아웃" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="header" fragment="true" %>
<%@ attribute name="footer" fragment="true" %>
<%@ attribute name="main_menu"  fragment="true" %>
<%@ attribute name="title" type="java.lang.String" rtexprvalue="false" %>
<fmt:setBundle basename="bundle.common" />
<!DOCTYPE html> 
<html>
	<head>
		<meta charset="UTF-8"  />
		<meta name="viewport" content="user-scalable=yes, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, width=device-width">
		<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/xeicon@2.3.3/xeicon.min.css" />
		<link rel="stylesheet" type="text/css" href="<c:url value='/static/css/style.css' />" />
		<script src="<c:url  value='/static/js/common.js' />"></script>
		<c:if test="${empty title}">
			<title><fmt:message key="SITE_TITLE" /></title>
		</c:if>
		<c:if test="${!empty title}">
			<title>${title}</title>
		</c:if>
	</head>
	<body>
		<header>
			<jsp:invoke fragment="header" />
		</header>
		<jsp:invoke fragment="main_menu" />
		<main>
			<jsp:doBody />
		</main>
		<footer>
			<jsp:invoke fragment="footer" />
		</footer>
	</body>
</html>
```

- 다국어 지원을 위한 properties 파일 생성 및 적용 

```java 
<fmt:setBundle basename="bundle.common" />
```

```java
<fmt:message key="SITE_TITLE" />
```

#### /WEB-INF/classes/common_ko.properties : 한국어

```
# 공통
SITE_TITLE=사이트 제목

#회원
MEMBER_JOIN=회원가입
MEMBER_LOGIN=로그인
```

#### /WEB-INF/classes/common_en.properties : 영어

```
# 공통
SITE_TITLE=SITE TITLE

#회원
MEMBER_JOIN=JOIN
MEMBER_LOGIN=LOG IN
```

* * * 
## 메인페이지 전용 레이아웃 구성

- 공통 레이아웃에 헤더와 푸터 영역을 고정한 메인 레이아웃 구성

#### /WEB-INF/tags/layouts/main.tag

```java
<%@ tag description="메인 레이아웃" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<fmt:setBundle basename="bundle.common" />
<layout:common>
	<jsp:attribute name="header">
		<section class='top_menu'>
			<div class="layout_width">
				<a href="<c:url value="/join" />"><fmt:message key="MEMBER_JOIN" /></a>
				<a href="<c:url value="/login" />"><fmt:message key="MEMBER_LOGIN" /></a>
			</div>
		</section>
		<section class="logo">
			<a href="<c:url value="/" />">
				<fmt:message key="SITE_TITLE" />
			</a>
		</section>
	</jsp:attribute>
	<jsp:attribute name="main_menu">
	<nav>
		<div class='layout_width'>
			<a href='#'>메뉴1</a>
			<a href='#'>메뉴2</a>
			<a href='#'>메뉴3</a>
			<a href='#'>메뉴4</a>
		</div>
	</nav>
	</jsp:attribute>
	<jsp:attribute name="footer">
		&copy;CopyRight ...
	</jsp:attribute>
	<jsp:body>
		<div class='layout_width'>
			<jsp:doBody />
		</div>
	</jsp:body>
</layout:common>
```

### 레이아웃 적용

#### src/main/java/controllers/IndexController.java

```java
package controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 메인 페이지 - /index.jsp
 * 
 * @author YONGGYO
 *
 */
public class IndexController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		RequestDispatcher rd = request.getRequestDispatcher("main/index.jsp");
		rd.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}	
}
```

#### /src/main/webapp/main/index.jsp

```jsp
<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="layout" tagdir="/WEB-INF/tags/layouts" %>
<layout:main>
	<h1>본문 영역</h1>
</layout:main>
```


### PC

![image1](https://github.com/yonggyo1125/curriculum300H/blob/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/5%EC%9D%BC%EC%B0%A8(3h)%20-%20%EC%9B%B9MVC%20%2B%20%EC%BB%A4%EC%8A%A4%ED%85%80%20%EC%95%A1%EC%85%98%20-%20%EB%A0%88%EC%9D%B4%EC%95%84%EC%9B%83%20%EA%B5%AC%EC%84%B1/images/project/image1.png)

### MOBILE

![image2](https://github.com/yonggyo1125/curriculum300H/blob/main/5.JSP2%20%26%20JSP%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8(60%EC%8B%9C%EA%B0%84)/5%EC%9D%BC%EC%B0%A8(3h)%20-%20%EC%9B%B9MVC%20%2B%20%EC%BB%A4%EC%8A%A4%ED%85%80%20%EC%95%A1%EC%85%98%20-%20%EB%A0%88%EC%9D%B4%EC%95%84%EC%9B%83%20%EA%B5%AC%EC%84%B1/images/project/image2.png)
