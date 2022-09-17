package com.sodkwhy.exam.service;


import com.sodkwhy.exam.vo.TaskVO;

import java.util.List;

/**
 * @Description:  本源码分享自 www.cx1314.cn   欢迎访问获取更多资源
 * @Created: 程序源码论坛
 * @author: cx
 * @createTime: 2020-06-30 17:06
 **/
public interface InteractionService {


    void saveOrUpdate(String key, TaskVO taskVO, String username);


    List<TaskVO> getTaskList(String username);



    void deleteTask(String key, String username);


}
