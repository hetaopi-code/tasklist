package com.sodkwhy.exam.controller;


import com.sodkwhy.exam.vo.TaskVO;
import com.sodkwhy.exam.service.InteractionService;
import com.sodkwhy.exam.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class InteractionController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private InteractionService interactionService;

    @PostMapping(value = "/saveOrUpdate")
    @ApiOperation(value = "saveOrUpdate")
    public R saveOrUpdate(@RequestBody TaskVO taskVO){
        interactionService.saveOrUpdate(taskVO.getKey(), taskVO,"Tony");
        return R.ok();
    }

    @GetMapping(value = "/list")
    @ApiOperation(value = "")
    public R list(){
        List<TaskVO> result = interactionService.getTaskList("Tony");
        return R.ok().data("result",result);
    }

    @PostMapping(value = "/delete")
    @ApiOperation(value = "")
    public R delete(@RequestBody TaskVO taskVO){
        interactionService.deleteTask(taskVO.getKey(),"Tony");
        return R.ok();
    }


    @PostMapping(value = "/interaction")
    @ApiOperation(value = "")
    public R interaction(@RequestParam("params") String params){
        System.out.println(params);
        String[] split = params.split("\\s+");
        if (!"task".equals(split[0])) {
            return R.error().message("格式错误");
        }
        if ("list".equals(split[1])) {
            return dealList();
        }
        if ("add".equals(split[1])) {
            return dealAdd(params);
        }
        if ("delete".equals(split[1])) {
            return dealDelete(params);
        }
        return R.error().message("格式错误");
    }
    private R dealList() {
        String url = "http://127.0.0.1:9884/list";
        ResponseEntity<String> response = restTemplate.getForEntity(url,  String.class);
        return R.ok().data("result",response.getBody());
    }

    private R dealAdd(@RequestParam("params") String params) {
        Pattern p1= Pattern.compile("\"(.*?)\"");
        Matcher m = p1.matcher(params);
        ArrayList<String> list = new ArrayList<String>();
        while (m.find()) {
            list.add(m.group().trim().replace("\"", "") + " ");
        }
        HashMap<String,Object> map = new HashMap<>();

        map.put("key",list.get(0));
        map.put("value",list.get(1));
        String url = "http://127.0.0.1:9884/saveOrUpdate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        // 2、使用postForEntity请求接口
        HttpEntity<Map> httpEntity = new HttpEntity<>(map,headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, httpEntity, String.class);
        return R.ok();
    }

    private R dealDelete(@RequestParam("params") String params) {
        Pattern p1= Pattern.compile("\"(.*?)\"");
        Matcher m = p1.matcher(params);
        ArrayList<String> list = new ArrayList<String>();
        while (m.find()) {
            list.add(m.group().trim().replace("\"", "") + " ");
        }
        HashMap<String,Object> map = new HashMap<>();
        map.put("key",list.get(0));

        String url = "http://127.0.0.1:9884/delete";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        // 2、使用postForEntity请求接口
        HttpEntity<Map> httpEntity = new HttpEntity<>(map,headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, httpEntity, String.class);
        return R.ok();
    }


}