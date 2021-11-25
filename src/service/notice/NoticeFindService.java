package service.notice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ModelAndView;
import common.Page;
import dao.NoticeDao;
import dto.Notice;

public class NoticeFindService implements NoticeService {

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 파라미터처리 받아올 data - query column
		String column = request.getParameter("column");
		String query = request.getParameter("query");

		// DB로 보낼 HashMap
		Map<String, String> map = new HashMap<String, String>();	// both of parameters are String type
		map.put("column", column);
		map.put("query", "%" + query + "%"); // 만능문자 % 붙여서 전송 	
		// 검색어는 앞 뒤로 %가 붙어있어야 함 - 만능 문자 활용 따라서, 미리실어서 보냄. ==> 연결 연산자를 따로 작업할 필요가 없어서 다른 DB 앱을 사용하더라도, 편하다.
		// 비어있는 검색어는  검색 못 하도록 script로 작업하여 java로 넘어오지 않도록 하는 것이 좋음.	
		
		// 페이징 1. 검색된 공지사항 갯수 구하기
		int totalRecord = NoticeDao.getInstance().selectFindCount(map);
		
		// 페이징 2. 현재 페이지 번호 확인하기
		// page가 안 넘어오면 page = 1로 처리함 == optional
		Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt.orElse("1"));
		
		// 페이징 3. 페이징에 필요한 모든 계산 처리하기  		새로운 메소드 만듦 setPageEntity
		Page p = new Page();
		p.setPageEntity(totalRecord, page);
		
		// 페이징 4. String으로 < 1 2 3 4 5 > 만들기 			-- 만들어서 JSP에 던지고, JSP에서는 보여주기만 하는 방법 = response에 실어서 보낸다는 뜻 인가?
		String pageEntity = p.getPageEntity("find.notice?column=" + column + "&query=" + query); // 검색으로 계속 이동하기 위해 find.notice로 mapping값 수정
		// 검색 결과를 그대로 실어주기 위해 column + query를 mapping에 실어줌
		
		// DB로 보낼 beginRecord, endRecord 작업
		map.put("beginRecord", p.getBeginRecord() + "");
		map.put("endRecord", p.getEndRecord() + "");
		
		// 검색 결과 가져오기
		List<Notice> list = NoticeDao.getInstance().findNotice(map);
		
		// list.jsp로 보낼 데이터
		// list.jsp의 ${list}에서 사용하고 있으므로 이름을 맞춰줘야 한다.
		request.setAttribute("totalRecord", totalRecord);
		request.setAttribute("pageEntity", pageEntity);
		request.setAttribute("list", list);
		request.setAttribute("startNum", totalRecord - (page - 1) * p.getRecordPerPage());	// 공식을 실어줌 

		return new ModelAndView("notice/list.jsp", false);
	}

}
