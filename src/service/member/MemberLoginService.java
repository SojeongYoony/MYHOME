package service.member;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.ModelAndView;
import dao.MemberDao;
import dto.Member;

public class MemberLoginService implements MemberService {

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		
		Member member = new Member();
		member.setId(id);
		member.setPw(pw);
		
		/* select member method 호출, 조회 */
		// ID/PW가 일치하는 회원 조회
		Member loginUser = MemberDao.getInstance().selectMember(member);
		if (loginUser != null) {	// != null  == 일치하는 사람이 있음 == login success
			// 회원 정보를 session에 loginUser로 저장함.
			HttpSession session = request.getSession();	// for use session
			session.setAttribute("loginUser", loginUser);
			
			// 로그인 기록 남기기.
			MemberDao.getInstance().loginLog(id);
			
			// index.jsp로 redirect이동  == insert 작업이 있었으므로, db수정작업이 있고 redirect해야함.
			return new ModelAndView("index.jsp", true);
		} else {					// 일치하는 사람이 없으면 아무것도 안함
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('일치하는 회원 없음')");
			out.println("history.back()");
			out.println("</script>");
			out.close();
			return null;
		}
		
	}

}
