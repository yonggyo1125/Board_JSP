package mybatis;

import java.util.ResourceBundle;
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
			ResourceBundle config = ResourceBundle.getBundle("application");
			
			String environment = config.getString("environment");
			if (environment == null || environment.isBlank()) {
				environment = "development";
			}
			
			// mybatis-config.xml 파일의 경로 */
			String configPath = null;
			if (environment.equals("production")) { 
				configPath = "mybatis/config/mybatis-config.xml";
			} else { 
				configPath = "mybatis/config/mybatis-dev-config.xml";
			}

			Reader reader = Resources.getResourceAsReader(configPath);
			
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
