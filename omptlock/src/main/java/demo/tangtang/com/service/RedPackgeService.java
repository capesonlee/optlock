package demo.tangtang.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by john on 2018/7/17.
 */
@Service
public class RedPackgeService {
    @Autowired
    private RedisTemplate<String,Serializable> redisTemplate;

    private static final String RED_PKG_KEY  = "redpkg";
    public String take(String taker) {


        redisTemplate.setEnableTransactionSupport(true);
        while (true) {

            System.out.println(taker + " before get counter");
            int counter = (int) redisTemplate.opsForValue().get("counter");
            if (counter < 1) {
                System.out.println("红包抢完啦");
                redisTemplate.setEnableTransactionSupport(false);
                return "红包抢完啦";
            }
            counter--;
            System.out.println(taker + " before watch counter");
            redisTemplate.watch("counter");
            System.out.println(taker + " after watch counter");
            redisTemplate.multi();

            System.out.println(taker + " before set counter");
            redisTemplate.opsForValue().set("counter", counter);
            List<Object> list = redisTemplate.exec();
            if (list == null || list.isEmpty()) {
                System.out.println("请稍等");
                continue;
            }
            int value = (int) redisTemplate.opsForList().leftPop(RED_PKG_KEY);

            redisTemplate.setEnableTransactionSupport(false);
            return " amount:" + value +"; left :" +counter;
        }
    }

    public String give(){
        Integer amount  = 10000;//100块，用分表示
        Integer total = 100; //红包个数

        Double [] fractions = new Double[total];
        Double sumFraction = 0.0;
        Integer[] values = new Integer[total];
        for ( int i = 0; i < fractions.length; ++i ){
            Double fraction = Math.random();
            sumFraction += fraction;
            fractions[i] = fraction;
        }

        Integer diff = amount;
        for( int i = 0; i< values.length; ++i ){
            values[i] = (int)(fractions[i]/sumFraction*amount);
            diff -=values[i];
        }
        values[values.length-1] += diff;

        redisTemplate.opsForList().leftPushAll("redpkg",values);
        redisTemplate.opsForValue().set("counter",total);


        //测试
        diff = 0;
        for( int i = 0; i< values.length; ++i ){
            System.out.println("第：" + (i+1) + "个红包:" + values[i]);
            diff += values[i];
        }

        return "发送一个红包:" + diff;
    }
}
