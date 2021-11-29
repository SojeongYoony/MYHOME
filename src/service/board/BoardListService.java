package service.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ModelAndView;
import common.Page;
import dao.BoardDao;
import dto.Board;

public class BoardListService implements BoardService {

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 파라미터 페이지 parameter page
		Optional<String> opt = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt.orElse("1"));	// 페이지 게시글은 맨 첫 페이지에 게시 되고, 보통 최신글을 먼저 보므로 null일때 1페이지를 볼 수 있도록 1페이지를 세팅함.
		
		// 전체 레코드 갯수 total record 
		int totalRecord = BoardDao.getInstance().selectTotalCount();
		
		// 페이징 처리 Page 객체 생성 및 데이터 계산
		Page p = new Page();
		p.setPageEntity(totalRecord, page);		// 이미 계산식은 만들어 뒀고 필요한 것을 넣어준다.
		
		// beginRecord, endRecord를 DB로 보낼 Map
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("beginRecord", p.getBeginRecord());
		map.put("endRecord", p.getEndRecord());
				
		// beginRecord ~ endRecord 사이 목록 가져오기
		List<Board> list = BoardDao.getInstance().selectBoardList(map);		// Page의 p객체를 map대신 전달할 수도 있음 그렇게 되면 Map~코드 세줄 필요 없음 그렇게 되면 find()라는 함수를 써야 한다. 
		
		// board/list.jsp로 보낼 데이터	+ paging 처리를 위한 pageEntity, startNum 추가
		request.setAttribute("list", list);
		request.setAttribute("pageEntity", p.getPageEntity("list.board"));
		request.setAttribute("startNum", totalRecord - (page - 1) * p.getRecordPerPage());		// 번호 계산하는 방법
		
		
		return new ModelAndView("board/list.jsp", false);
	}

}
