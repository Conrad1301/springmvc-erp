package com.multi.erp.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//BoardDAO의 메소드를 호출
//=> 컨틀로러에게 받은 데이터를 가공해서 DAO로 넘기거나 DAO에서 받은 데이터를 가공해서 컨트롤러로 넘기는 작업
//=> 트랜잭션처리
@Service
public class BoardServiceImpl implements BoardService{
	BoardDAO dao;
	
	public BoardServiceImpl() {
		
	}
	@Autowired
	public BoardServiceImpl(BoardDAO dao) {
		super();
		this.dao = dao;
	}
	//게시글 등록
	//=> 게시글 기본정보 저장, 첨부된 파일에 대한 정보 저장
	//dao클래스에 정의된 두 개의 메소드를 호출
	@Transactional
	@Override
	public int insert(BoardDTO board, List<BoardFileDTO> boardfiledtolist) {
		dao.insert(board);
		dao.insertFile(boardfiledtolist);
		return 0;
		
	}
	@Override
	public List<BoardDTO> boardList() {
		return dao.boardList();
	}
	@Override
	public BoardDTO getBoardInfo(String board_no) {
		return dao.read(board_no);
	}
	@Override
	public int update(BoardDTO board) {
		return dao.update(board);
	}
	@Override
	public int delete(String board_no) {
		return dao.delete(board_no);
	}
	@Override
	public List<BoardDTO> search(String data) {
		return dao.search(data);
	}
	@Override
	public List<BoardDTO> search(String tag, String data) {
		return dao.search(tag, data);
	}
	@Override
	public List<BoardDTO> findByCategory(String category) {
		
		//그냥 메소드 수행시간 측정하기 
		 List<BoardDTO> list = null;
		 if(category!=null) {
			 if(category.equals("all")) {
				 list = dao.boardList();
			 }else {
				 list = dao.findByCategory(category);
			 }
		 }
		return list;
	}
	@Override
	public List<BoardFileDTO> getFileList(String boardno) {
		
		return dao.getFileList(boardno);
	}
	@Override
	public BoardFileDTO getFile(BoardFileDTO inputdata) {
		// TODO Auto-generated method stub
		return dao.getFile(inputdata);
	}
	
}
