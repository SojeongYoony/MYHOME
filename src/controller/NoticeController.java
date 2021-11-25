package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ModelAndView;
import service.notice.NoticeDeleteService;
import service.notice.NoticeFindService;
import service.notice.NoticeInsertService;
import service.notice.NoticeListService;
import service.notice.NoticeService;
import service.notice.NoticeUpdateService;
import service.notice.NoticeViewService;

@WebServlet("*.notice")

public class NoticeController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public NoticeController() {
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
		
		// 모든 Model은 인터페이스 NoticeService를 구현하므로
		// NoticeService 타입의 객체이다.
		NoticeService service = null;

		// ModelAndView 객체 선언
		// 1. 단순 이동 요청을 처리할 ModelAndView객체 선언 :: 이친구가 이동 담당
		// 2. 모든 Model(Service)은 ModelAndView를 반환한다.
		ModelAndView mav = null;
		
		// 해당 요청을 처리할 Model(Service) 선택
		switch(command) {
		case "list.notice" :
			service = new NoticeListService();
			break;
		// 댓글 보기	
		case "view.notice" :		// view notice 시 게시글 no받아오기
			service = new NoticeViewService();
			break;
		// 작성 form으로 보내는 명령.	
		case "insertForm.notice" :
			mav = new ModelAndView("notice/insert.jsp", false);		// 단순 이동 :: forward
			break;
		// insert board
		case "insert.notice" :
			service = new NoticeInsertService();		
			break;
		// update form
		case "updateForm.notice" :
			mav = new ModelAndView("notice/update.jsp", false);
			break;
		// update content data
		case "update.notice" :
			service = new NoticeUpdateService();
			break;
		// delete content 
		case "delete.notice" :
			service = new NoticeDeleteService();
			break;
		// search WRITER, CONTENT, TITLE FROM EACH COLUMN OR ALL OF THEM
		case "find.notice" :
			service = new NoticeFindService();
			break;
			
		}
		
		// service가 사용되지 않은 경우 (단순 이동) service 실행이 불가능
		if (service != null) {
			try {					// 모든 Exception을 여기로 던지기 때문에, try - catch를 이용하여 Exception을 잡는다.
				mav = service.execute(request, response);					// 실제로 자바 실행이 되는 곳
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		// mav가 null인 경우
		// 1. Model(Service)에서 응답으로 이동하는 경우		-- 삭제 성공 시, 이동 / 수정 성공 시, 이동 / 삽입 성공 시, 이동
		// 2. Model(Service)이 ajax 응답을 처리하는 경우 	-- ajax == page 이동없이 처리 함.
		if (mav == null) {
			return;
		}
		
		// mav가 null이 아닌 경우 : MVC 패턴으로 페이지 이동이 있음.
		if (mav.isRedirect()) {						// mav가 is Redirect라면
			response.sendRedirect(mav.getView());	// mav의 getView로 redirect이동하고
		} else {									// 아니면
			request.getRequestDispatcher(mav.getView()).forward(request, response);	// getView로 forward
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
