package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ModelAndView;
import service.board.BoardDeleteService;
import service.board.BoardInsertService;
import service.board.BoardListService;
import service.board.BoardService;
import service.board.BoardUpdateService;
import service.board.BoardViewService;
import service.free.FreeDeleteService;
import service.free.FreeFindService;
import service.free.FreeInsertService;
import service.free.FreeListService;
import service.free.FreeService;
import service.free.FreeUpdateService;
import service.free.FreeViewService;
import service.free.ReplyInsertService;

@WebServlet("*.board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public BoardController() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 요청/응답 기본 처리 
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		// JSP의 요청 확인 
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length() + 1);
		
		BoardService service = null;
		ModelAndView mav = null;
		
		switch(command) {
		case "list.board" :
			service = new BoardListService();
			break;
		case "insertForm.board" :
			mav = new ModelAndView("board/insert.jsp", false);
			break;
		case "insert.board" :
			service = new BoardInsertService();
			break;
		case "view.board" :
			service = new BoardViewService();
			break;
		case "delete.board" :
			service = new BoardDeleteService();
			break;
		case "updateForm.board" :
			mav = new ModelAndView("board/update.jsp", false);
			break;
		case "update.board" :
			service = new BoardUpdateService();
			break;
		
		}
		
		// service가 사용되지 않은 경우 (단순 이동) service 실행이 불가능
		if (service != null) {
			try {											// exception 처리 : try-catch 필요
				mav = service.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (mav != null) {
			if(mav.isRedirect()) {
				response.sendRedirect(mav.getView());
			} else {
				request.getRequestDispatcher(mav.getView()).forward(request, response);
			}
		}
	
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
