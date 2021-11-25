package service.notice;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.HttpJspPage;

import common.ModelAndView;
import dao.NoticeDao;
import dao.ReplyDao;
import dto.Notice;
import dto.Reply;

public class NoticeViewService implements NoticeService {

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* parameter가 꼭 있어야 하는 아이  :: nNo */
		
		
		// Notice notice = (Notice) session.getAttribute("notice");		// session은 object type 이므로 casting이 필요하다
		// 상세 보기를 처음 한 경우	:: DB에서 게시글(notice)을 가져와야한다. == session에 notice가 저장되어 있지 않은 상태이다.
		//if (notice == null) {			// 조회수 때문에 들어온 부분이었는데
		// 조회수 증가.	(수정이 필요함)							증가한 조회수의 정보를 가지고 해당 공지사항(notice table's data == nNo )을 가져온다
		
		
		// 상세 보기를 수행할 게시글번호(nNo)를 받아 와야 함.
		// 전달되지 않는다면 0(없는 게시글번호)을 사용함.
		// Long nNo는 상세 보기와 댓글 리스트에서 모두 필요함.   == 상세보기는 최초 1회라 필요없을 수 있지만 댓글은 무조건 필요하기 때문에 
			Optional<String> opt = Optional.ofNullable(request.getParameter("nNo"));
			Long nNo = Long.parseLong(opt.orElse("0")); 		// 전달되면 전달받은걸 꺼내고, 전달되지 않으면 0
			// JSP에서는 session이 내장 객체여서 그냥 쓸 수 있었는데, 여기는 자바이고 request에서 Session가져와야함.
			// session에 저장된 notice 꺼내기
			HttpSession session = request.getSession();		// session 꺼내기
			
			// 게시글을 열면 session에 "open"값 저장하기로 함.
			if (session.getAttribute("open") == null) {		// session에 저장한 open 값이 없을 때
				session.setAttribute("open", true);			// open 값을 넣고
				NoticeDao.getInstance().updateNoticeHit(nNo);
			}
		
			// 게시글번호와 일치하는 공지사항을 가져 옴.
			Notice notice = NoticeDao.getInstance().selectNoticeView(nNo);	// nNo 결과받기
			
			
			// 댓글정보를 불러오기 위해 session을 쓰고 있는데, ajax를 이용하여 page이동이 없게 댓글 처리를 하면 session에 저장해 둘 필요가 없다. :: 댓글 작업은 ajax로 처리하는 것이 대중적이다.
		
		// 댓글 달고 상세 보기로 이동한 경우	  :: session에 notice가 저장되어 있는 상태이므로
		
		if (notice != null) {	// 일치하는 공지사항이 있는 경우
			// session에 저장해 둠. (수정, 삭제 작업으로 이동할 때 파라미터 넘길 필요가 없음. param전달 X)
			// 여기서 session에 저장해둔 notice 정보는 목록으로 돌아갈 때 필요가 없어지므로 List로 돌아갈때 초기화를 해야한다 그러므로 NoticeListService 맨 처음에서 session 초기화를 한다.
			session.setAttribute("notice", notice);	// null 이면 session에 저장	 if else :: 저장 필요 없음.
			// 댓글 리스트 가져옴.
			List<Reply> replyList = ReplyDao.getInstance().selectReplyList(nNo); // nNo 전달
			// view.jsp에서 보여줄 수 있도록 request에 저장해 둠. (JSP에서 확인할 수 있도록)
			request.setAttribute("replyList", replyList);
			// view.jsp에서 보여줄 수 있도록 request에 저장해 둠.
			request.setAttribute("notice", notice);			
			// notice/view.jsp로 forward 이동
			return new ModelAndView("notice/view.jsp", false);
		} else {				// 일치하는 공지 사항이 없는 경우 경고 메시지 작성함.
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('일치하는 공지사항이 없습니다. 다시 시도하세요.')");
			out.println("history.back()");
			out.println("</script>");
			out.close();
			return null;		// 일치하는 공지사항이 없을 때 : history.back으로 이동이 이뤄지므로 return 할 사항이 없음
		}
		
	}

}
