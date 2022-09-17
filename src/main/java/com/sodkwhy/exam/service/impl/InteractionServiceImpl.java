package com.sodkwhy.exam.service.impl;


import com.alibaba.fastjson.JSON;
import com.sodkwhy.exam.service.InteractionService;
import com.sodkwhy.exam.vo.TaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;





@Slf4j
@Service
public class InteractionServiceImpl implements InteractionService {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public void saveOrUpdate(String key, TaskVO taskVO, String username)  {

        BoundHashOperations<String, Object, Object> ops = getops(username);

        String taskVOJson = JSON.toJSONString(taskVO);
        ops.put(key, taskVOJson);
        return ;
    }


    @Override
    public List<TaskVO> getTaskList(String username)  {


        String key = "exam:" + username;

        List<TaskVO> taskList = getTaskItems(key);

        return taskList;
    }


    private BoundHashOperations<String, Object, Object> getops(String username) {


        String key = "";
        key = "exam:" + username;

        //绑定指定的key操作Redis
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);

        return operations;
    }



    private List<TaskVO> getTaskItems(String key) {

        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(key);
        List<Object> values = operations.values();
        if (values != null && values.size() > 0) {
            List<TaskVO> itemVoList = values.stream().map((obj) -> {
                String str = (String) obj;
                TaskVO taskVO = JSON.parseObject(str, TaskVO.class);
                return taskVO;
            }).collect(Collectors.toList());
            return itemVoList;
        }
        return null;

    }



    @Override
    public void deleteTask(String key, String username) {

        BoundHashOperations<String, Object, Object> ops = getops(username);
        ops.delete(key);
    }

}
