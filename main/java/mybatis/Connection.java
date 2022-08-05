package mybatis;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Connection {
	/** 데이터베이스 접속 객체 */
	private static SqlSessionFactory sqlSessionFactory;
	
	static {
		// 접속정보를 명시하고 있는 XML의 경로 읽기
		try {
			// mybatis-config.xml 파일의 경로 */
			Reader reader = Resources.getResourceAsReader("mybatis/config/mybatis-config.xml");
			
			if (sqlSessionFactory == null) {
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 데이터베이스 접속 세션 반환 */
	public static SqlSession getSqlSession() {
		return sqlSessionFactory.openSession();
	}
}
