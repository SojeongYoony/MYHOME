package service.member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.ModelAndView;

public class MemberLogoutService implements MemberService {

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// session의 모든 정보 초기화
		HttpSession session = request.getSession();
		session.invalidate();		// 싹 다 지운다.
		
		// index.jsp로 forward/redirect 상관없음
		return new ModelAndView("index.jsp", false);
	}

}
