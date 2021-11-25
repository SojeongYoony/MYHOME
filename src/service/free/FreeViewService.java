package service.free;

import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.ModelAndView;
import dao.FreeDao;
import dto.Free;

public class FreeViewService implements FreeService {

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 파라미터
		Optional<String> opt = Optional.ofNullable(request.getParameter("fNo"));
		Long fNo = Long.parseLong(opt.orElse("0"));
		
		
		// 조회수 증가
		// 게시글을 열면 session에 open 속성을 저장해 둔다.
		// 게시글을 닫지 않으면, session의 open 속성 값이 계속 유지된다.
		// session의 open 속성 값이 있으면, 게시글을 닫지 않았다고 볼 수 있다.	(== 안 닫았으니까, 게시글의 Hit를 더 올리면 안된다)
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute("open") == null) {		// session에 저장된 게시글이 있을 때,
			session.setAttribute("open", true);
			FreeDao.getInstance().updateHit(fNo);
		}
		
		// 불러오기
		Free free = FreeDao.getInstance().selectFreeByfNo(fNo);
		
		if (free != null) {
			request.setAttribute("free", free);
			return new ModelAndView("free/view.jsp", false);
		} else {
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('일치하는 게시글이 없습니다. 다시 시도하세요.')");
			out.println("history.back()");
			out.println("</script>");
			out.close();
			return null;
		}
		
	}

}
