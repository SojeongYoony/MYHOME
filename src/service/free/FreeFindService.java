package service.free;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ModelAndView;
import common.Page;
import dao.FreeDao;
import dto.Free;

public class FreeFindService implements FreeService {

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 파라미터
		String column = request.getParameter("column");
		String query = request.getParameter("query");
		
		// DB로 보낼 HashMap
		Map<String, String> map = new HashMap<String, String>();
		map.put("column", column);
		map.put("query", "%" + query + "%");
		
		// 페이징 기능 필요 함. -- 검색결과를 가지고 페이징을 해 줘야 하므로, 결과를 가지고 온 뒤에 구현
		// 페이징 1. page 객체 만들기
		Page p = new Page();
		 
		// 페이징 2. 검색된 게시글의 전체 갯수 구하기 
		int totalRecord = FreeDao.getInstance().selectFindCount(map);		// select 결과를 넣어주고
		p.setTotalRecord(totalRecord);										// 실어준다
		
		// 페이징 3. 검색된 게시글을 이용한 페이지 갯수 구하기
		p.setTotalPage();
		
		// 페이징 4. 현재 페이지 번호 확인하기			파라미터의 page 파라미터를 구하고, 만약 그것이 null이면, 1로 세팅한 뒤, page객체에 넣어준다.
		Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt( opt.orElse("1") );
		p.setPage(page);
		
		// 페이징 5. beginRecord, endRecord 계산하기 : 계산은 page 객체에 이미 되어 있음.
		p.setBeginRecord();
		p.setEndRecord();
		
		// 페이징 6. 검색 결과 중, beginRecord ~ endRecord 사이 목록 가져오기
		// 기존의 검색어 관련 map이 있음. 거기에 beginRecord와 endRecord 추가해줘야 함.
		map.put("beginRecord", p.getBeginRecord() + "");		// map의 String type을 맞춰주기 위해 빈 문자열을 붙여 String type으로 바꿔줌
		map.put("endRecord", p.getEndRecord() + "");
		List<Free> list = FreeDao.getInstance().findFree(map);	// 필드와 검색값 들어있음. + begin R, end R 값 들어있음 = 총 4개 들고있음
		
		// 페이징 7. beginPage, endPage 계산하기
		p.setBeginPage();
		p.setEndPage();
		
		// list.jsp로 보낼 데이터
		request.setAttribute("column", column);
		request.setAttribute("query", query);  // paging 변경 시 사라지는 mapping값 해결을 위해 추가로 실어서 보낼 작업을 함
		request.setAttribute("p", p);
		request.setAttribute("totalRecord", totalRecord);
		request.setAttribute("list", list);
		// list service 와 검색을 위한 parameter가 있냐, 없냐의 차이이고, 다른 내용은 같기 때문에 합치는 것이 좋지만, 고려할 사항이 있다.
		// service를 분리해서 구현하였기 때문에 jsp(page)도 분리해서 구현한다.													
		return new ModelAndView("free/findList.jsp", false);
	}

}
