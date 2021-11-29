package service.board;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.ModelAndView;
import dao.BoardDao;

public class BoardDeleteService implements BoardService {

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 파라미터
		String param = request.getParameter("bNo");
		if (param == null || param.isEmpty()) {
			param = "0";
		}
		
		Long bNo = Long.parseLong(param);
		String path = request.getParameter("path");	// 삭제할 파일 이름 hidden input tag : param으로 받아옴
		String saveName = request.getParameter("saveName");
		// 첨부 삭제	: 파일 이름, 파일 경로 알아내서 찾아가서 지울거임.
		String realPath = request.getServletContext().getRealPath(path);	// 서버상의 경로와 실제 경로는 다르니, 바꿔주고
		File file = new File(realPath, saveName);	// 삭제 할 file은 File 써야 가능 함		; 경로와 파일을 분리해서 넣는 방법은 ,(comma)로 구분 (경로, 파일이름)
		if (file.exists()) {	// 만약 있으면
			file.delete();		// 해당 file을 삭제한다.
		}
		
		// DB 삭제
		int result = BoardDao.getInstance().deleteBoard(bNo);
		PrintWriter out = response.getWriter();
		if (result > 0) {
			out.println("<script>");
			out.println("alert('게시글 삭제 성공')");
			out.println("location.href='list.board'");
			out.println("</script>");
			out.close();
		} else {
			out.println("<script>");
			out.println("alert('게시글 삭제 실패')");
			out.println("history.back()");
			out.println("</script>");
			out.close();
		}
		
		return null;
	}

}
