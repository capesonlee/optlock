package demo.tangtang.com.web;


import demo.tangtang.com.runnables.PackageTaker;
import demo.tangtang.com.service.RedPackgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Created by john on 2018/7/10.
 */
@RestController
@RequestMapping("/redpkg")
public class RedPackageController {
    @Autowired
    RedPackgeService redPackgeService;

    @RequestMapping(value = "/take",method = RequestMethod.GET)
    public String take(){

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        int clientNum = 100;// 模拟客户数目
        for (int i = 0; i < clientNum; i++) {
            cachedThreadPool.execute(new PackageTaker(i,new RedPackgeService()));
        }
        cachedThreadPool.shutdown();
//        try{
//            cachedThreadPool.awaitTermination(10, TimeUnit.SECONDS);
//        }catch (Exception exce){
//
//        }



        return "success";

     }
    @RequestMapping(value = "/give",method = RequestMethod.GET)
    public String give(){
        return redPackgeService.give();
    }
}
