package service.notice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ModelAndView;

public interface NoticeService {
								// JSP로 request, response가 이뤄져야 하므로 request response가 필요하다.
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
																						// String을 사용함 --> 예외발생.	
}
