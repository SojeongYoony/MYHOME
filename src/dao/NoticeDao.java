package dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import dto.Notice;
import mybatis.config.DBService;

public class NoticeDao {

	// MyBatis는 SqlSession 객체를 이용해서 DB에 접근한다.
	// SqlSession 객체는 메소드별로 만들고 닫는 것이 좋다.
	// SqlSession 객체를 만들 수 있는 SqlSessionFactory를 준비해 둔다.
	
	private SqlSessionFactory factory;							// 동작만 만들어 둔다 :: 선언만 해 둔다		factory는 DBService객체로 부터 얻어낸다.
	
	
	/* ***** VERY VERY IMPORTANT :: 이론적인 것에 대해 숙지해 두자 ***** */
	/* frame work에는 singleton이 내장되어 있다. */
	/* singleton :: 자기 자신을 객체로 만들어 단 하나의 객체만 생성한다. */
	private static NoticeDao instance;							// 자기 자신을 선언해 두고
	private NoticeDao() {										// 아무도 부를 수 없게 private type으로 묶어버리고
		factory = DBService.getInstance().getFactory();			//factory는 DBService객체로 부터 얻어낸다.	getInstance를 하면 getFactory를 얻어낼 수 있다.
	}
	public static NoticeDao getInstance() {						//오직 이 메소드를 통해서만 NoticeDao를 부를 수 있다.
		if ( instance == null ) {
			instance = new NoticeDao();							// new :: 생성자 호출함수
		}
		return instance;
	}
	
	
	// 1. SELECT NOTICE LIST
	public List<Notice> selectNoticeList(Map<String, Integer> map) {
		SqlSession ss = factory.openSession();
		List<Notice> list = ss.selectList("dao.notice.selectNoticeList", map);
		ss.close(); 				// 꼭 닫아주자
		return list;
	}
	
	// get total count
	public int selectTotalCount() {
		SqlSession ss = factory.openSession();
		int totalCount = ss.selectOne("dao.notice.selectTotalCount");
		ss.close();
		return totalCount;		                                     
	}
 	
	// 2. notice 하나	 == board의 게시글 하나를 가져온다 (param 하나 담을 때)
	public Notice selectNoticeView(Long nNo) {	// Long Type의 noticeNumber == nNo
		SqlSession ss = factory.openSession();
		Notice notice = ss.selectOne("dao.notice.selectNoticeView", nNo);
		ss.close();
		return notice;
	}
	
	// 3. update Notice content's hit
	public int updateNoticeHit (Long nNo) {	// 게시글 번호 받아와서 해당 게시글의 hit만 늘려줘야 한다.
		SqlSession ss = factory.openSession(false);
		int result = ss.update("dao.notice.updateNoticeHit", nNo);
		if (result > 0) ss.commit();
		ss.close();
		return result;
	}
	
	// 4. INSERT NOTICE CONTENT
	public int insertNotice(Notice notice) {
		SqlSession ss = factory.openSession(false);
		int result = ss.insert("dao.notice.insertNotice", notice);
		if (result > 0) ss.commit();
		ss.close();
		return result;
	}
	
	// 5. UPDATE NOTICE CONTENT
	public int updateNotice(Notice notice) {
		SqlSession ss = factory.openSession(false);
		int result = ss.update("dao.notice.updateNotice", notice);
		if (result > 0) ss.commit();
		ss.close();
		return result;
	}
	
	// 6. DELETE NOTICE CONTENT
	public int deleteNotice(Long nNo) {
		SqlSession ss = factory.openSession(false);
		int result = ss.delete("dao.notice.deleteNotice", nNo);
		if (result > 0) ss.commit();
		ss.close();
		return result;

	}
	
	// 7. search query
	public List<Notice> findNotice(Map<String, String> map) {
		SqlSession ss = factory.openSession();
		List<Notice> list = ss.selectList("dao.notice.findNotice", map);
		ss.close(); 				
		return list;
	}
	
	// 8. select find count
	public int selectFindCount(Map<String, String> map) {
		SqlSession ss = factory.openSession();
		int findCount = ss.selectOne("dao.notice.selectFindCount", map);
		ss.close();
		return findCount;
	}
	
		
}
