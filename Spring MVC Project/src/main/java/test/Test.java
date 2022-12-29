package test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

public class Test {

	public static void main(String[] args) {
		String apiURL = "https://scss119stu.apigw.ntruss.com/custom/v1/19891/92f5644cc16e65af79c7af2bc43f594834af74e980caf5c41fd8bdfaabe8cdd9/infer";
		String secretKey = "Tk11ZExBQldiR3ZjR3Fmc214UmZJcEJ2ek9JRXdveG8=";
		//http통신을 위해 준비
		try {
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			//http통신을 담당하는 객체에 속성을 설정
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			//요청객체에 필욯나 값을 셋팅
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
			con.setRequestProperty("X-OCR-SECRET", secretKey);
		
			//API를 요청하기 위해 필요한 모든 값들을 json객체로 만들어서 전송
			JSONObject json = new JSONObject();
			json.put("version", "V2");
			json.put("requestId", UUID.randomUUID().toString());
			json.put("timestamp", System.currentTimeMillis());
			JSONObject image = new JSONObject();
			image.put("format", "jpg");
			image.put("url", "https://kr.object.ncloudstorage.com/ocr-ci-test/sample/1.jpg"); // image should be public, otherwise, should use data
			// FileInputStream inputStream = new FileInputStream("YOUR_IMAGE_FILE");
			// byte[] buffer = new byte[inputStream.available()];
			// inputStream.read(buffer);
			// inputStream.close();
			// image.put("data", buffer);
			image.put("name", "demo");
			JSONArray images = new JSONArray();
			images.put(image);
			json.put("images", images);
			String postParams = json.toString();
			
			//전송
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();
			
			//응답되는 데이터를 StrignBuffer에 추가하기 
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if (responseCode == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
			//응답을 출력하기
			System.out.println(response);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
