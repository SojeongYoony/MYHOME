package service.notice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.ModelAndView;
import common.Page;
import dao.NoticeDao;
import dto.Notice;

public class NoticeListService implements NoticeService {

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();		// session 사용을 위해 httpsession 에서 가져오기.
		
		// 상세 보기할 때 session에 올려 둔 notice를 제거 -- 고려할 점 : login 정보는 남겨둬야 한다
		Notice notice = (Notice) session.getAttribute("notice");
		if (notice != null) {
			session.removeAttribute("notice");  		// notice만 제거하자. 다른 정보들은 필요하니까 냄겨두고
		}
		
		// 상세 보기할 때 session에 올려둔 open을 제거
		if (session.getAttribute("open") != null) {
			session.removeAttribute("open");
		}
		
		
		// -- 페이징1, 페이징2가 왜 service에서 구현되어야 하는지 이해할 것
		// 페이징 1. 전체 공지사항 갯수 구하기
		int totalRecord = NoticeDao.getInstance().selectTotalCount();
		
		// 페이징 2. 현재 페이지 번호 확인하기
		// page가 안 넘어오면 page = 1로 처리함 == optional
		Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt.orElse("1"));
		
		// 페이징 3. 페이징에 필요한 모든 계산 처리하기  		새로운 메소드 만듦 setPageEntity
		Page p = new Page();
		p.setPageEntity(totalRecord, page);
		
		// 페이징 4. String으로 < 1 2 3 4 5 > 만들기 			-- 만들어서 JSP에 던지고, JSP에서는 보여주기만 하는 방법 = response에 실어서 보낸다는 뜻 인가?
		String pageEntity = p.getPageEntity("list.notice");
		
		// beginRecord ~ endRecord 목록 가져오기
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("beginRecord", p.getBeginRecord());		// beginRecord 구하기
		map.put("endRecord", p.getEndRecord());			// 검색기능 구현시, page을 위한 parameter가 된다.
		List<Notice> list = NoticeDao.getInstance().selectNoticeList(map);		// instance로 불러오기 method 가져오기
		
		// list.jsp로 보낼 데이터
		request.setAttribute("totalRecord", totalRecord);
		request.setAttribute("pageEntity", pageEntity);
		request.setAttribute("list", list);
		request.setAttribute("startNum", totalRecord - (page - 1) * p.getRecordPerPage());	// 각 페이지의 게시글 시작 번호
		return new ModelAndView("notice/list.jsp", false);		// request 가지고 가려면 forward :: false
	}

}
