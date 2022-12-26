<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#testbtn").on("click", function() {
			var querydata = {
				"boardno" : $("#boardno").val()
			}
			$.ajax({
				url : "/erp/ajax/exam01",
				tyte : "get",
				data : querydata,
				dataType : "text",
				success : success_run,
				error : error_run

			})//end ajax
		})//end click
		$("#testjsonbtn").on("click", function() {
			var querydata = {
				"boardno" : $("#boardno").val()
			}
			$.ajax({
				url : "/erp/ajax/exam02/getjsondata",
				tyte : "get",
				data : querydata,
				dataType : "json",
				success : function(data) {
					/* alert(data.id); */
					$("#no").text(data.board_no )
					$("#title").text(data.title )
					$("#writer").text(data.id )
					$("#content").text(data.content )
				},
				error : error_run
			})//end ready
		})
	})
	//ajax요청이 성공하면 .ajax메소드 내부에서 success속성에 설정한 함수를 호출하면서 
	//ajax의 처리 결과를 함수의 매개변수로 전달 - 자동
	function success_run(txt) {
		//alert(txt);
		$("#result").html("<h2>jquery:" + txt + "</h2>")
	}
	function error_run(obj, msg, statusMsg) {
		alert("오류발생=>" + obj + "," + msg + "," + statusMsg);
	}
</script>
<title>Insert title here</title>
</head>
<body>
	<form>
		글번호:<input type="text" name="boardno" id="boardno" /> <input
			type="button" value="ajax테스트" id="testbtn" /> <input type="button"
			value="ajaxjson테스트" id="testjsonbtn" />
	</form>
	<div id="result">
		<h4>
			글번호:<span id="no"></span>
		</h4>
		<h4>
			제목:<span id="title"></span>
		</h4>
		<h4>
			작성자:<span id="writer"></span>
		</h4>
		<h4>
			내용:<span id="content"></span>
		</h4>

	</div>
</body>
</html>