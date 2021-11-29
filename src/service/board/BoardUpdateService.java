package service.board;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import common.ModelAndView;
import dao.BoardDao;
import dto.Board;

public class BoardUpdateService implements BoardService {

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// real Path	== real path 구하기
		HttpSession session = request.getSession();
		String path = (String)session.getAttribute("path");
		String realPath = request.getServletContext().getRealPath(path);	// request로 path를 받지 못 해, session에 path를 저장하고, get path from session
		
		// MultipartRequest 객체 생성
		MultipartRequest mr = new MultipartRequest(request, realPath, 10 * 1024 * 1024, "UTF-8", new DefaultFileRenamePolicy());
		
		/** 첨부파일 수정 **/
		// 기존에 첨부되어 있던 파일
		String saveName = mr.getParameter("saveName");
		File previous = new File(realPath, saveName);
		
		// 새로 첨부 하려는 파일
		File present = mr.getFile("fileName"); // get param X , getFile 하면 File바로 가져옴 --> String으로 request할 필요 없이, getFile하면 바로 File로 저장할 수 있다.
		
		// 새 첨부가 있으면 기존 첨부를 지운다
		if (present != null) {			// 새 첨부가 있냐 : null이 아님  ==> null이면 새로 첨부한게 없다는 뜻임 
			if (previous.exists()) {	// 기존 첨부가 있냐
				previous.delete();		// 기존 첨부를 지워라
			}
		}
		
		/** DB수정하기 **/
		// 수정 할 게시글 정보 parameters
		Long bNo = Long.parseLong(mr.getParameter("bNo"));
		String title = mr.getParameter("title");
		String content = mr.getParameter("content");
		
		// DB로 보낼 Board(수정내용을 저장한 Board)		-- 3개만 갖고 있거나 5개 모두 갖고 있거나 ==> DB에서 if로 분리
		Board board = new Board();
		board.setbNo(bNo);
		board.setTitle(title);		// 무조건덮어쓰기
		board.setContent(content);
		if (present != null) {	// 새 첨부가 있으면 올린이름, 저장이름 모두 변경 / 새 첨부가 없으면 기존 첨부명을 사용  
			board.setFileName(mr.getOriginalFileName("fileName"));	// 올린 이름
			board.setSaveName(mr.getFilesystemName("fileName")); 	// 저장된 이름
		}
		
		int result = BoardDao.getInstance().updateBoard(board);
		PrintWriter out = response.getWriter();
		if (result > 0) {
			out.println("<script>");
			out.println("alert('게시글 수정 성공')");
			out.println("location.href='view.board?bNo=" + bNo + "'");
			out.println("</script>");
			out.close();
		} else {
			out.println("<script>");
			out.println("alert('게시글 수정 실패')");
			out.println("history.back()");
			out.println("</script>");
			out.close();
		}
		return null;
	}

}
