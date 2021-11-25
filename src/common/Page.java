package common;

public class Page {
	// Page 객체는 하나로 모아서 호출하여 사용하는 방식으로 모두 이용 가능 함
	
	private int totalRecord;			// 전체 개시글 갯수(DB에서 가져옴)
	private int recordPerPage = 10;		// 한 페이지에 표시하는 게시글 갯수(여기서 정함)
	private int totalPage;				// 전체 페이지 갯수 == (totalRecord, recordPerPage로 계산)
	
	/********************************************* 
	  - 전체 11개 게시글
	  - 한 페이지당 3개 게시글
	  page = 1, beginRecord = 1,  endRecord = 3
	  page = 2, beginRecord = 4,  endRecord = 6
	  page = 3, beginRecord = 7,  endRecord = 9
	  page = 4, beginRecord = 10, endRecord = 11
	   
	 *********************************************/
	private int page;					// 현재 페이지번호(파라미터로 받아옴 )  :: parameter로 전달 받을 것
	private int beginRecord;			// 각 페이지에 표시하는 시작 게시글 번호    :: 게시글의 시작  		==  (page, recordPerPage로 계산)
	private int endRecord;				// 각 페이지에 표시하는 종료 게시글 번호    :: 게시글의 끝(마지막)	==	(beginRecord, recordPerPage, totalRecord로 계산)
	
	/********************************************************************* 
	  - 전체 12개 페이지
	  - 한 블록당 5개 페이지		( << 1, 2, 3, 4, 5 >> )
	   1 block < 1  2  3  4  5 >  page=1~5,   beginPage=1,  endPage=5
	   2 block < 6  7  8  9  10 > page=6~10,  beginPage=6,  endPage=10
	   3 block < 11 12 >          page=11~15, beginPage=11, endPage=12
	 *********************************************************************/
	private int pagePerBlock = 5;	// 한 블록에 표시하는 페이지 갯(여기서 정함) -- block당 등록되는 page 수
	private int beginPage;			// 각 블록에 표시하는 시작 페이지 번호 == (page, pagePerBlock로 계산)
	private int endPage;			// 각 블록에 표시하는 종료 페이지 번호 == (beginPage, pagePerBlock, totalPage) beginpage + pageperblock -1
	
	
	public void setPageEntity(int totalRecord, int page) {
		
		this.totalRecord = totalRecord;
		this.page = page;
		
		// totalPage
		totalPage = totalRecord / recordPerPage;
		if (totalRecord % recordPerPage != 0) {
			totalPage ++;
		}
		
		// begin-record, end-record
		beginRecord = (page - 1) * recordPerPage + 1;
		endRecord = beginRecord + recordPerPage - 1;
		endRecord = (totalRecord < endRecord) ? totalRecord : endRecord;
		
		// beginPage, endPage : page Per block
		beginPage = ((page - 1) / pagePerBlock) * pagePerBlock + 1;
		endPage = beginPage + pagePerBlock - 1;
		endPage = (totalPage < endPage) ? totalPage : endPage;
		
	}
	
	public String getPageEntity(String path) {
		
		StringBuilder sb = new StringBuilder();
		// path에 ?가 포함되어 있으면 path에 파라미터가 포함되어 있다는 의미임.
		// path = find.notice?column=WRITER&query=admin
		
		// 위와 같은 경우 page 파라미터는 "&page"로 path에 추가해야 함.
		// path = find.notice?column=WRITER&query=admin&page=
		// 검색 : 만약 path에 ?가 있으면 parameter가 있다는 뜻이고, 뒤의 parameter에 page를 붙일 수 있도록 ?를 &로 바꿔주는 작업이 필요하다.
		// == path에 ?가 있다는 의미는 무조건 parameter가 있다는 의미이다.
		
		// 1페이지로 이동 : 1페이지는 링크가 필요 없음 == 링크를 걸지 않음 
		if (page == 1) {
			sb.append("◀◀&nbsp;");
		} else {
			if (path.contains("?")) {		// path에 ?를 포함하고 있나 
				sb.append("<a href=\"" + path +"&page=1\">◀◀</a>&nbsp;");		
			} else {
				sb.append("<a href=\"" + path +"?page=1\">◀◀</a>&nbsp;");
			}
		}
		
		String concat = path.contains("?") ? "&" : "?"; // 애초에 &와 ?를 변수에 넣어서 변수로 작업하여도 가능 -- 처음에 생각했던 방법
		// 이전 블록으로 이동 : 1블록은 이전 블록이 없음 == 링크를 걸지 않음
		if ( page <= pagePerBlock ) {
			sb.append("◀&nbsp;");
		} else {
			sb.append("<a href=\"" + path + concat + "page=" + ( beginPage - 1) + "\">◀</a>&nbsp;");
		}
		
		// 페이지 번호 : 현재 페이지는 이동이 필요 없음 == 링크를 걸지 않음 
		for (int i = beginPage; i <= endPage; i ++) {
			if ( page == i ) {
				sb.append(i + "&nbsp;");
			} else {
				if (path.contains("?")) {
					sb.append("<a href=\"" + path + "&page=" + i + "\">"+ i + "</a>&nbsp;");
				} else {
					sb.append("<a href=\"" + path + "?page=" + i + "\">"+ i + "</a>&nbsp;");
				}
			}
		}
		
		// 다음 블록으로 이동 : 마지막 블락은 이동이 필요 없음 == 링크를 걸지 않음 
		if( endPage == totalPage ) {
			sb.append("▶&nbsp;");
		} else {
			if (path.contains("?")) {
				sb.append("<a href=\"" + path + "&page="+ (endPage + 1) +"\">▶</a>&nbsp;");
			} else {
				sb.append("<a href=\"" + path + "?page="+ (endPage + 1) +"\">▶</a>&nbsp;");
			}
		}
		
		// 마지막 페이지로 이동 : 마지막 페이지는 링크가 필요 없음 
		if (page == totalPage) {
			sb.append("▶▶&nbsp;");
		} else {
			if (path.contains("?")) {
				sb.append("<a href=\"" + path + "&page="+ totalPage + "\">▶▶</a>");
			} else {
				sb.append("<a href=\"" + path + "?page="+ totalPage + "\">▶▶</a>");
			}
		}
		
		return sb.toString(); // string 대신 builder 쓰는건, 성능상의 이유
	}
		
	public int getPagePerBlock() {
		return pagePerBlock;
	}
	public void setPagePerBlock(int pagePerBlock) {
		this.pagePerBlock = pagePerBlock;
	}
	public int getBeginPage() {
		return beginPage;
	}
	public void setBeginPage() {		// 계산식으로 수정
		beginPage = ((page - 1) / pagePerBlock) * pagePerBlock + 1;
	}
	public int getEndPage() {						
		return endPage;
	}
	public void setEndPage() {			// 계산식으로 수정
		endPage = beginPage + pagePerBlock - 1;
		if (totalPage < endPage) {		// 마지막일 경우 작은 값을 적용시켜줘야 하므로,
			endPage = totalPage;		// 여기서 값을 바꿔준다.
		}
	}
	public int getTotalRecord() {
		return totalRecord;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getBeginRecord() {
		return beginRecord;
	}
	public void setBeginRecord() {  // 계산식으로 수정
		beginRecord = (page - 1) * recordPerPage + 1;
	}
	public int getEndRecord() {
		return endRecord;
	}
	public void setEndRecord() {  // 계산식으로 수정
		endRecord = beginRecord + recordPerPage -1;
	  // 주의 : 마지막 page에서는 endRecord와 비교하여 totalRecord를 사용한다.
		if (totalRecord < endRecord) {	// totalRecord의 값과 endRecord의 값을 비교하여
			endRecord = totalRecord;	// endRecord의 값을 totalRecord의 값으로 바꿔준다.
		}
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	public int getRecordPerPage() {
		return recordPerPage;
	}
	public void setRecordPerPage(int recordPerPage) {
		this.recordPerPage = recordPerPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage() {	/* 수정 */
		totalPage = totalRecord / recordPerPage;	// 이 식은 몫만 구함. 나머지가 있는 경우 날려버리기 때문에,
		if (totalRecord % recordPerPage != 0) {		// 나머지 게시글의 수가 있는 경우
			totalPage ++;							// page를 하나 더 만듦
		}
	}
	
	
	
	
}
