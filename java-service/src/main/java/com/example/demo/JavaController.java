package com.example.demo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zjz on 2017/12/16.
 */
@RestController
public class JavaController {
    @Autowired
    private RestTemplate restTemplate;
    @RequestMapping("/java-user")
    public String JavaUser() {
        return "{'username': '王', 'password': 'java'}"  ;
    }

    @RequestMapping(value = "/python-user", method = RequestMethod.POST)
    public String PythonUser(@RequestBody String json) {
    	HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
        headers.setContentType(type);
        String requestJson = json;
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);
        String res = restTemplate.postForObject("http://py-sidecar/responds", entity, String.class);
    	System.out.println(unicodeToString(res));
    	return unicodeToString(res);
    	//return restTemplate.postForEntity("http://py-sidecar/responds", jsonObj, String.class);getForEntity("http://py-sidecar/responds", String.class).getBody();
    }
    
    /**
     * unicode转中文
     * @param str
     * @return
     * @author yutao
     * @date 2017年1月24日上午10:33:25
     */
    private static String unicodeToString(String str) {

        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

        Matcher matcher = pattern.matcher(str);

        char ch;

        while (matcher.find()) {

            ch = (char) Integer.parseInt(matcher.group(2), 16);

            str = str.replace(matcher.group(1), ch+"" );

        }

        return str;

    }
}
