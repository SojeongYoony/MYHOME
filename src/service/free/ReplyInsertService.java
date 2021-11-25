package service.free;

import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ModelAndView;
import dao.FreeDao;
import dto.Free;

public class ReplyInsertService implements FreeService {

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 원글의 depth, groupNo, groupOrd임.
		int depth = Integer.parseInt( request.getParameter("depth") );
		Long groupNo = Long.parseLong( request.getParameter("groupNo") );
		Long groupOrd = Long.parseLong( request.getParameter("groupOrd") );	// 넘겨주는 법 두 가지 : Map / Free(dto, bean)
		
		String writer = request.getParameter("writer");
		String content = request.getParameter("content");
		Optional<String> opt = Optional.ofNullable(request.getHeader("X-Forwarded-For"));
		String ip = opt.orElse(request.getRemoteAddr());
	
		// 삽입할 댓글 reply
		Free reply = new Free();
		reply.setDepth(depth + 1);			// 원글의 depth + 1
		reply.setGroupNo(groupNo);			// 원글의 groupNo
		reply.setGroupOrd(groupOrd + 1);	// 원글의 groupOrd + 1
		reply.setWriter(writer);
		reply.setContent(content);
		reply.setIp(ip);
		
		// 원글 만들기
		Free free = new Free();
		free.setGroupNo(groupNo);
		free.setGroupOrd(groupOrd);
		// 같은 groupNo + 이미 달린 댓글 중에서 원글의 groupOrd보다 큰 값을 가지는 댓글의 groupOrd + 1
		FreeDao.getInstance().updatePreviousReplyGroupOrd(free);

		int result = FreeDao.getInstance().insertReply(reply);
		
		PrintWriter out = response.getWriter();		// spring에서도 response 부분은 같음 
		if (result > 0 ) {
			out.println("<script>");
			out.println("alert('댓글 삽입 성공.')");			
			out.println("location.href='list.free'");		// 전달 안하면 1page로 setting 해 뒀기 때문에 전달 안할 뿐, 전달이 필요할 시, parameter 실어서 해당 page로 이동할 수 있도록 해야 한다.
			out.println("</script>");	
			out.close();									// append method도 println도 있지만, append는 newline을 하지 않으므로 세미클론;을 이용하여 구분을 줘야 한다. ex)out.append("<script>");
		} else {
			out.println("<script>");
			out.println("alert('댓글 삽입 실패.')");
			out.println("history.back()");		// 전달 안하면 1page로 setting 해 뒀기 때문에 전달 안할 뿐, 전달이 필요할 시, parameter 실어서 해당 page로 이동할 수 있도록 해야 한다.
			out.println("</script>");
			out.close();
		}
		return null;
	}

}
