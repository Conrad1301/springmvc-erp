package com.multi.erp.board;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.multi.erp.member.MemberDTO;

@Controller
public class BoardController {
	FileUploadLogic fileuploadService;
	BoardService service;
	public BoardController() {}
	@Autowired
	public BoardController(BoardService service,FileUploadLogic fileuploadService) {
		super();
		this.service = service;
		this.fileuploadService = fileuploadService;
	}
	@RequestMapping(value = "/board/write.do",method = RequestMethod.GET)
	public String writePage() {
		return "board/writepage";
	}

	@RequestMapping("/board/download/{id}/{board_no}/{boardFileno}")
	public ResponseEntity<UrlResource> downloadFile(@PathVariable String id, @PathVariable String board_no,
															@PathVariable String boardFileno,HttpSession session)
																	throws MalformedURLException, FileNotFoundException, 
																														UnsupportedEncodingException{
		System.out.println(id+","+board_no+","+boardFileno);
		//글번호와 파일번호를 이용해서 해당 파일을 조회
		//=> 요청한 파일에 대한 조회이므로 BoardFileDTO를 리턴하도록 처리
		//요청한 파일을 db에서 조회한 후 sysout으로 출력
		//mapper
		BoardFileDTO selectFileInfo = service.getFile(new BoardFileDTO(board_no,"","",boardFileno));
		
		UrlResource resource = new UrlResource("file:"+WebUtils.getRealPath(session.getServletContext(), 
				"/WEB-INF/upload/"+selectFileInfo.getStoreFilename()));
		
		String encodedFilename = UriUtils.encode(selectFileInfo.getOriginalFilename(), "UTF-8");
		String mycontenttype = "attachment; filename=\"" + encodedFilename + "\"";
		System.out.println(selectFileInfo);
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, mycontenttype).body(resource);
	}
	@RequestMapping(value = "/board/write.do",method = RequestMethod.POST)
	public String write(BoardDTO board, HttpSession session) throws IllegalStateException, IOException {
		System.out.println("board=>"+board);
		//1.MultipartFile정보를 추출하기
		List<MultipartFile> files = board.getFiles();
		//2.업로드될 서버의 경로 - 실제 서버의 경로를 추출하기 위해서 context의 정볼르 담고 잇는 ServletContext객체를 추출
		//=> ServletContext는 우리가 생성한 프로젝트가 서버에 배포되는 실제 경로와 context에 대한 정보를 담고 있는 객체
		String path = 
				WebUtils.getRealPath(session.getServletContext(), "/WEB-INF/upload");
		System.out.println(path);
		
		//3. 파일업로드 서비스를 호출해서 실제 서버에 업로드되도록 작업하기
		List<BoardFileDTO> boardfiledtolist = fileuploadService.uploadFiles(files, path);
		int count = 1;
		//업로드된 파일의 boardfileno의 값을 세팅 - 1부터 1,2,3,4.... 첨부파일 마지막 번호 
		for(BoardFileDTO boardfiledto:boardfiledtolist) {
			boardfiledto.setBoardFileno(count+"");
			count++;
		}
		System.out.println(boardfiledtolist);
		//4. 게시글에 대한 일반적인 정보와 첨부되는 파일의 정보를 db에 저장하기
		service.insert(board,boardfiledtolist);
		return "redirect:/board/list.do?category=all";
	}
	@RequestMapping("/board/list.do")
	public ModelAndView list(String category) {
		System.out.println("category=>"+category);
		ModelAndView mav = new ModelAndView("board/list");
		List<BoardDTO> boardlist = service.findByCategory(category);
		System.out.println(boardlist);
		mav.addObject("category",category);
		mav.addObject("boardlist", boardlist);
		return mav;
	}
	
	@RequestMapping("/board/read.do")
	public String read(String board_no,String state, Model model) {
		//ModelAndView mav = new ModelAndView();
		BoardDTO board = service.getBoardInfo(board_no);
		List<BoardFileDTO> boardfiledtolist = service.getFileList(board_no);
		
		String view = "";
		if(state.equals("READ")) {
			view = "board/read";
		}else {
			view = "board/update";
		}
		//mav.setViewName(view);
		//System.out.println("model로 수정하기==============================");
		//System.out.println(board);
		model.addAttribute("board", board);
		model.addAttribute("boardfiledtolist", boardfiledtolist);
		return view;
	}
	//delete를 시도하면 로그인 유무를 체크해서 로그인을 하지 않은 사용자는 로그인을 할 수 있도록 로그인페이지로 리다이렉트
	@RequestMapping("/board/delete.do")
	public String delete(String board_no,HttpSession session) {
		MemberDTO user = (MemberDTO) session.getAttribute("user");
		//로그인한 사용자를 찾는다
		String view = "";
		if(user==null) {//로그인하지 않은 상태
			view = "redirect:/emp/login.do";
		}else {
			int result = service.delete(board_no);
			view = "redirect:/board/list.do?category=all";
		}
		return view;
	}
	//실제 업데이트기능을 처리
	@RequestMapping("/board/update.do")
	public String update(BoardDTO board) {
		System.out.println(board+"-----------업데이트---------------------");
		int result = service.update(board);
		return "redirect:/board/list.do?category=all";
	}
	@RequestMapping("/board/search.do")
	public ModelAndView search(String tag,String data) {
		ModelAndView mav = new ModelAndView("board/list");
		List<BoardDTO> boardlist = service.search(tag,data);
		System.out.println(boardlist);
		mav.addObject("boardlist", boardlist);
		return mav;
	}
	
	//ajax요청으로 실행될 메소드
	//jackson라이브러리가 List에 저장된 DTO들을 자동으로 json객체로 변환
	@RequestMapping(value = "/board/ajax/list.do", produces = "application/json;charset=utf-8")
	@ResponseBody
	public List<BoardDTO> ajaxlist(String category) {
		System.out.println("category=>"+category);
		
		List<BoardDTO> boardlist = service.findByCategory(category);
		System.out.println("ajax통신:"+boardlist);
		return boardlist;
	}
}


