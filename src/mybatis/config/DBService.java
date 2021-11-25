package mybatis.config;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

// SqlSessionFactory를 만들어서 Dao에게 반환하는 클래스

public class DBService {
	
	/* SqlSessionFactory  :: 궁극적 목적은 factory를 만드는 것 */
	private SqlSessionFactory factory;			// 얘를 만들어서 반납하는 역할
	public SqlSessionFactory getFactory() {
		return factory;
	}
	
	/* singleton */
	private static DBService instance;
	private DBService() {					// 생성될 때 Sql factory도 만들어진다.
		/* SqlSessionFactory 빌드 */
		try {
		String resource	= "mybatis/config/mybatis-config.xml";		// 위치 :: 기본 정보는 xml에 저장해 두고 그 정보를 읽어 factory를 만든다.
		InputStream inputStream = Resources.getResourceAsStream(resource);
		factory = new SqlSessionFactoryBuilder().build(inputStream);			// 핵심 코드들.
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public static DBService getInstance() {			//DBService instance를 만들면 그 때 factory도 만들어 지는 것임.	== 관계
		if (instance == null) {
			instance = new DBService();
		}
		return instance;
		
	}

}
