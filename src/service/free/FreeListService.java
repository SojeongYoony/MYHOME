package service.free;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.ModelAndView;
import common.Page;
import dao.FreeDao;
import dto.Free;

public class FreeListService implements FreeService {

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 조회수 증가에서 사용된 session의 open 속성 제거하기
		HttpSession session = request.getSession();
		if (session.getAttribute("open") != null) {		// 없는데 제거할 수 는 없잖아? 있을 때만 제거.
			session.removeAttribute("open");
		}
		
		// 페이징1. Page 객체 만들기
		Page p = new Page();
		
		// 페이징2. 전체 게시글의 갯수 구하기
		int totalRecord = FreeDao.getInstance().selectTotalCount();
		p.setTotalRecord(totalRecord);  // 전달해서 몇갠지
		
		// 페이징3. 전체 페이지의 갯수 구하기
		p.setTotalPage();  				// page객체의 setTotalPage method를 호출하면 자동으로 구해진다.
		/*
		System.out.println(page.getTotalRecord());			:: 전체 게시글의 수 
		System.out.println(page.getRecordPerPage());		:: 페이지당 게시글의 수
		System.out.println(page.getTotalPage());			:: 만들어 질 페이지의 수
		*/
		
		// 페이징4. 현재 페이지 번호 확인하기
		// 1) page가 안 넘어오면 page = 1로 처리함
		Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt.orElse("1"));
		p.setPage(page);	// 계산할 수 있게 page 객체(p)에 넘겨주기
		
		// 페이징5. beginRecord, endRecord 계산하기	:: page 객체에 이미 계산되어 있고, 호출만 하면 된다.
		p.setBeginRecord();
		p.setEndRecord();
		
		// 페이징6. beginRecord ~ endRecord 사이 목록 가져오기
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("beginRecord", p.getBeginRecord());
		map.put("endRecord", p.getEndRecord());			// page 객체 p에서 가져오기.
				
		List<Free> list = FreeDao.getInstance().selectFreeList(map);
		
		// 페이징7. beginPage ~ endPage 계산하기
		p.setBeginPage();
		p.setEndPage();
		
		// 페이징8. Page 객체를 list.jsp에서 사용할 수 있도록 저장해준다.			--> 반복문으로 page만들어주고 link걸어서 이동할 수 있도록 함 => parameter를 link에 실어서 처리하면 되는 것 일까?
		request.setAttribute("p", p);
		request.setAttribute("totalRecord", totalRecord);
		request.setAttribute("list", list);
		
		return new ModelAndView("free/list.jsp", false);
	}

}
