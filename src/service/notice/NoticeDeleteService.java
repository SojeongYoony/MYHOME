package service.notice;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ModelAndView;
import dao.NoticeDao;
import dao.ReplyDao;
import dto.Notice;
import dto.Reply;

public class NoticeDeleteService implements NoticeService {

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 삭제할 게시글의 번호 nNo
		Long nNo = Long.parseLong(request.getParameter("nNo"));
		
		// 삭제할 게시글에 댓글 유무 확인하기
		List<Reply> replyList = ReplyDao.getInstance().selectReplyList(nNo);

		PrintWriter out = response.getWriter();
		// 댓글이 없으면 삭제 진행
		if(replyList.size() == 0) {	// replyList == null  ==> reply가 없다. == 삭제 진행		*** 주의 *** replyList가 null이 아니고 size로 해야 함 	**개선version : query로 작업하는 게 좋음
			int result = NoticeDao.getInstance().deleteNotice(nNo);	// dto 호출, db로 보내는 code
			// 댓글이 있으면 못 지운다 작업이 필요함 (삭제에서는)
			if (result > 0) {
				out.println("<script>");
				out.println("alert('공지사항 삭제 성공')");
				out.println("location.href='list.notice'");
				out.println("</script>");
				out.close();
			} else {
				out.println("<script>");
				out.println("alert('공지사항 삭제 실패')");
				out.println("history.back()");
				out.println("</script>");
				out.close();
			}
		} else {	// 댓글이 있는 경우 돌려 보내기	replyList != null  ==> reply가 있다 == 삭제 진행 하지 않음.
			out.println("<script>");
			out.println("alert('댓글이 달린 게시글은 삭제할 수 없습니다.')");
			out.println("history.back()");
			out.println("</script>");
			out.close();
		}
		
		return null;
	}

}
