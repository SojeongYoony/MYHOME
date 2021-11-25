package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import dto.Board;
import mybatis.config.DBService;

public class BoardDao {
	
	private SqlSessionFactory factory;
	
	/* singleton */
	private static BoardDao instance;
	private BoardDao() {
		factory = DBService.getInstance().getFactory();
	}
	
	public static BoardDao getInstance() {
		if (instance == null ) {
			instance = new BoardDao();
		}
		return instance;
	}
	
	/* insert image into board */
	public int insertBoard(Board board) {
		SqlSession ss = factory.openSession(false);
		int result = ss.insert("dao.board.insertBoard", board);
		if(result > 0) ss.commit();
		ss.close();
		return result;
	}
	
	/* select Board */
	public List<Board> selectBoardList() {
		SqlSession ss = factory.openSession();
		List<Board> list = ss.selectList("dao.board.selectBoardList");
		ss.close();
		return list;
	}
	
	/* view selectBoardView */
	public Board selectBoardView(Long bNo) {
		SqlSession ss = factory.openSession();
		Board board = ss.selectOne("dao.board.selectBoardView", bNo);
		ss.close();
		return board;
	}
	
	
}
