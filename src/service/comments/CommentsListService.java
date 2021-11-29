package service.comments;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import common.Page;
import dao.CommentsDao;
import dto.Comments;

public class CommentsListService implements CommentsService {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 파라미터 page
		Optional<String> opt = Optional.ofNullable(request.getParameter("page"));	// page도 넘어 옴 주의
		int page = Integer.parseInt(opt.orElse("1"));
		
		Long bNo = Long.parseLong(request.getParameter("bNo"));
		
		// 전체 댓글 갯수
		int totalCount = CommentsDao.getInstance().selectTotalCount(bNo);
		
		// Page 객체 생성
		Page p = new Page();
		p.setPageEntity(totalCount, page);
		
		// beginRecord, endRecord를 DB로 보낼 Map	-- map에다가 3가지를 가지고 실어서 간다
		Map<String, Long> map = new HashMap<String, Long>();
		map.put("beginRecord", (long)p.getBeginRecord());
		map.put("endRecord", (long)p.getEndRecord());
		map.put("bNo", bNo);
		
		// 목록
		List<Comments> list = CommentsDao.getInstance().selectCommentsList(map);
		
		// 반환할 JSON
		JSONObject result = new JSONObject();
		result.put("commentsCount", list.size());		// list.size() == 댓글의 갯수 // jsp에서 받을거 두개 
		
		JSONArray comments = new JSONArray();
		for (Comments comment : list) {
			JSONObject obj = new JSONObject();
			obj.put("cNo", comment.getcNo());
			obj.put("writer", comment.getWriter());
			obj.put("content", comment.getContent());
			obj.put("bNo", comment.getbNo());
			obj.put("state", comment.getState());
			obj.put("created", comment.getCreated());
			comments.put(obj);
		}
		result.put("comments", comments);		// result에 목록과 list.size(댓글의갯수) 둘 다 실어줌
		JSONObject pageEntity = new JSONObject(p);
		result.put("pageEntity", pageEntity);
		// result에 담겨 있는 데이터의 형태
		// System.out.println(result);
/*		{
			"commentsCount" : 2,
			"comments" : [
			    {
			       "cNo" : 1,
			       "writer" : "admin",
			       "content": "일빠",
			       "bNo" : 1,
			       "state" : 0,
			       "created" : "2021-11-29"
			    }, 	
			    {
			    	"cNo" : 2,
			    	"writer" : "banana",
			    	"content": "이빠",
			    	"bNo" : 1,
			    	"state" : 0,
			    	"created" : "2021-11-29"
			    } 	
			],
			"pageEntity" : {
				"totalRecord" : 2,
				"recordPerPage": 10,
				...
			} 
		}
*/		
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(result);	// object 하나에다가 세가지를 다 실어서 보낸다 pageEntity, commentsCount, comments => view.jsp에 보냄
		out.close();
	}

}
