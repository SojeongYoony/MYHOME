/*
	댓글형 게시판
	1. 공지사항 : notice
	2. 댓     글  : reply
*/

DROP TABLE REPLY;
DROP TABLE NOTICE;

CREATE TABLE NOTICE
(
/* 게시글 번호 */	NNO				NUMBER,				
/* 작성자 이름 */	WRITER  		VARCHAR2(50),
/* 게시글 제목 */	TITLE			VARCHAR2(2000),	/* NOTNULL 필요 */
/* 게시글 내용 */	CONTENT 		VARCHAR2(4000),
/* 글의 조회수 */	HIT 			NUMBER,
/* 작성자 IP */	IP				VARCHAR2(32),
/* 최초 작성일 */	NDATE			DATE,
/* 최종 수정일 */	NLASTMODIFIED 	DATE
);

CREATE TABLE REPLY
(
/* 댓글 번호    */	RNO				NUMBER,
/* 댓글작성자  */	WRITER			VARCHAR2(50),
/* 댓글 내용    */	CONTENT			VARCHAR2(2000),
/* 작성자 IP */	IP				VARCHAR2(32),
/* 게시글 번호 */	NNO				NUMBER,
/* 최초 작성일 */	RDATE			DATE
);

ALTER TABLE NOTICE ADD CONSTRAINT NOTICE_PK PRIMARY KEY(NNO);
ALTER TABLE REPLY ADD CONSTRAINT REPLY_PK PRIMARY KEY(RNO);

ALTER TABLE REPLY ADD CONSTRAINT REPLY_NOTICE_FK FOREIGN KEY(NNO) REFERENCES NOTICE(NNO) ON DELETE CASCADE;

DROP SEQUENCE NOTICE_SEQ;
DROP SEQUENCE REPLY_SEQ;
CREATE SEQUENCE NOTICE_SEQ NOCACHE;
CREATE SEQUENCE REPLY_SEQ NOCACHE;



INSERT INTO NOTICE 
VALUES (NOTICE_SEQ.NEXTVAL, '관리자', '이용 시 주의사항', '바른말 사용하기', 0, '0:0:0:0:0:0:1', SYSDATE, SYSDATE);
COMMIT

/* 
	회원
	1. 회원 : member
	2. 기록 : member_log
*/
DROP TABLE MEMBER_LOG;
DROP TABLE MEMBER;

CREATE TABLE MEMBER
(
	/* 회원번호 */ MNO			NUMBER,
	/* 아이디    */ ID			VARCHAR2(32) NOT NULL UNIQUE,	/* PK가 아닌 외래키는 UNIQUE가 꼭 필요 함 */
	/* 비밀번호 */ PW			VARCHAR2(32) NOT NULL,
	/*  이름     */ NAME			VARCHAR2(50),
	/*  메일     */ EMAIL		VARCHAR2(200),
	/*  가입일  */ MDATE		DATE
);
CREATE TABLE MEMBER_LOG
(
	/* 기록번호 */ LNO			NUMBER,
	/*  아이디  */ ID			VARCHAR2(32),
	/*로그인일시*/ LOGIN		DATE
);

ALTER TABLE MEMBER ADD CONSTRAINT MEMBER_PK PRIMARY KEY(MNO);
ALTER TABLE MEMBER_LOG ADD CONSTRAINT MEMBER_LOG_PK PRIMARY KEY(LNO);

ALTER TABLE MEMBER_LOG ADD CONSTRAINT MEMBER_LOG_MEMBER_FK FOREIGN KEY(ID) REFERENCES MEMBER(ID) ON DELETE CASCADE;

DROP SEQUENCE MEMBER_SEQ;
DROP SEQUENCE MEMBER_LOG_SEQ;

CREATE SEQUENCE MEMBER_SEQ NOCACHE;
CREATE SEQUENCE MEMBER_LOG_SEQ NOCACHE;

INSERT INTO MEMBER VALUES (MEMBER_SEQ.NEXTVAL, 'admin', '1111', '관리자', 'admin@myhome.com', SYSDATE);
INSERT INTO MEMBER VALUES (MEMBER_SEQ.NEXTVAL, 'scott', '1111', '스콧', 'scott@myhome.com', SYSDATE);

COMMIT


/*
	계층형 게시판
	자유게시판 
*/

DROP TABLE FREE;
CREATE TABLE FREE
(
	/* 글번호       */	FNO				NUMBER,
	/* 작성자       */	WRITER			VARCHAR2(32),	
	/* 글내용       */	CONTENT			VARCHAR2(4000),
	/*  IP    */	IP				VARCHAR2(32),
	/* 조회수       */	HIT				NUMBER,
	/* 최초작성일 */	CREATED			DATE,
	/* 최종수정일 */	LASTMODIFIED 	DATE,
	/* 삭제여부    */	STATE			NUMBER, /* 정상 게시글 : 0, 삭제 게시글 : -1 */
	/* 게시글/댓글 */	DEPTH			NUMBER,	/* 게시글 : 0, 댓글 : 1이상 (원글의 DEPTH + 1)// 게시글 : 0 // 댓글 : 1 // 대댓글 : 2  //  따라서 값 : 원글 + 1 //원글의 depth값을 알아야 한다 */
	/* 동일그룹    */	GROUPNO			NUMBER,	/* 게시글 : 자신의 글번호(FNO), 댓글 : 원글의 글번호(FNO)  댓글은 게시글의 글번호를 참조함 :: selfJoin :: have to be same type as fno */
	/* 그룹내순서 */	GROUPORD		NUMBER	/* 동일그룹내 표시순서를 의미 */
);

DROP SEQUENCE FREE_SEQ;
CREATE SEQUENCE FREE_SEQ NOCACHE;
ALTER TABLE FREE ADD CONSTRAINT FREE_PK PRIMARY KEY(FNO);


/* 최종 select list query 기본 BASE LIST가 될 것이다 IF NEED SOMETHING ELSE, ADD IT HERE */
/*SELECT B.RN, B.FNO, B.WRITER, B.CONTENT
  FROM (SELECT ROWNUM AS RN, A.FNO, A.WRITER, A.CONTENT
          FROM (SELECT FNO, WRITER, CONTENT
          FROM FREE
         ORDER BY FNO DESC) A) B
 WHERE B.RN BETWEEN 1 AND 3;  
*/      
-- WHERE B.RN BETWEEN 1 AND 3 : BEGIN / END 결정 자리
-- ROWNUM == 행 번호 BEGIN, END 기준이 ROWNUM!! FNO가 기준이 아님 주의!!!
-- ROWNUM == RN :: RN이 기준이 된다 BEGIN, END  ** RN은 가상 칼럼이다.
-- 1. 최신 게시글을 DESC 정렬, 그것을 A라고 부름 
-- 2. 거기다가 행번호를 붙임 그것이 B (B: 정렬, 행번호 붙음),
-- 3. 최종 결과인 B에서 BEGIN, END를 구함



-- DEPTH		원글 DEPTH + 1
-- GROUPNO		원글 GROUPNO
-- GROUPORD		원글 GROUPORD + 1
-- 				같은 GROUPNO + 이미 달린 댓글 중 원글GROUPORD 보다 큰 것들 + 1

-- 게시판 검색은 일부를 포함하면 검색 되게끔 구현해야 함
-- SELECT *
--   FROM NOTICE
--  WHERE WRITER like '%ad%';			'%ad%' == name parameter = "query"

/*
SELECT B.nno, B.writer, B.title, B.content, B.hit, B.ip, B.ndate, B.nlastmodified
  FROM (SELECT ROWNUM AS RN, A.nno, A.writer, A.title, A.content, A.hit, A.ip, A.ndate, A.nlastmodified
  		  FROM (SELECT nno, writer, title, content, hit, ip, ndate, nlastmodified
  		          FROM notice 
  		         ORDER BY nno DESC) A ) B
 WHERE B.RN BETWEEN 1 AND 3
*/
 
-- totalRecord - (page-1) * recordPerPage
 
/*
	이미지 게시판
	1. 이미지 게시판 : BOARD
	2. 댓글 : COMMENT
*/
 
DROP TABLE COMMENTS;
DROP TABLE BOARD;
CREATE TABLE BOARD
(
	/* 게시글 번호 */	BNO				NUMBER,
	/* 작성자 */		WRITER			VARCHAR2(32),
	/* 제목    */		TITLE			VARCHAR2(2000)	NOT NULL,
	/* 내용    */		CONTENT			VARCHAR2(4000),
	/* 올린파일명 */	FILENAME		VARCHAR2(300),
	/* 저장파일명 */	SAVENAME		VARCHAR2(300),
	/* 작성일자 */		CREATED			DATE,
	/* 최종수정일 */	LASTMODIFIED	DATE
);

CREATE TABLE COMMENTS
(
	/* 댓글 번호 */		CNO				NUMBER,
	/* 작성자 */		WRITER			VARCHAR2(32),
	/* 내용    */		CONTENT			VARCHAR2(4000),
	/* 원글 번호 */		BNO				NUMBER,
	/* 삭제여부 */		STATE			NUMBER,
	/* 작성일자 */		CREATED			DATE
);
 
DROP SEQUENCE BOARD_SEQ;
DROP SEQUENCE COMMENTS_SEQ;
CREATE SEQUENCE BOARD_SEQ NOCACHE;
CREATE SEQUENCE COMMENTS_SEQ;

ALTER TABLE BOARD ADD CONSTRAINT BOARD_PK PRIMARY KEY(BNO);
ALTER TABLE COMMENTS ADD CONSTRAINT COMMENTS_PK PRIMARY KEY(CNO);
ALTER TABLE COMMENTS ADD CONSTRAINT COMMENTS_BOARD_FK FOREIGN KEY(BNO) REFERENCES BOARD(BNO) ON DELETE CASCADE;
ALTER TABLE BOARD ADD CONSTRAINT BOARD_MEMBER_FK FOREIGN KEY(WRITER) REFERENCES MEMBER(ID) ON DELETE CASCADE;
ALTER TABLE COMMENTS ADD CONSTRAINT COMMENTS_MEMBER_FK FOREIGN KEY(WRITER) REFERENCES MEMBER(ID) ON DELETE CASCADE;
















 