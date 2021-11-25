package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import dto.Reply;
import mybatis.config.DBService;

public class ReplyDao {
	
	private SqlSessionFactory factory;
	
	/* singleton */
	private static ReplyDao instance;
	private ReplyDao() {
		factory = DBService.getInstance().getFactory();
	}
	
	public static ReplyDao getInstance() {
		if (instance == null ) {
			instance = new ReplyDao();
		}
		return instance;
	}
	
	/* insert reply */
	public int insertReply(Reply reply) {		// Reply type, reply 
		SqlSession ss = factory.openSession(false);
		int result = ss.insert("dao.reply.insertReply", reply);
		if (result > 0) ss.commit();
		ss.close();
		return result;
	}
	
	/* select replies list */
	public List<Reply> selectReplyList(Long nNo) {	// 받아와서 넘겨주기.
		SqlSession ss = factory.openSession();
		List<Reply> list = ss.selectList("dao.reply.selectReplyList", nNo);	// 여기로 넘겨줌
		ss.close();
		return list;
	}
}
