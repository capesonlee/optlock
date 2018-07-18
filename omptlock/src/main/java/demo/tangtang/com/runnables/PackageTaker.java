package demo.tangtang.com.runnables;


import demo.tangtang.com.service.RedPackgeService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by john on 2018/7/17.
 */
public class PackageTaker implements Runnable {
    RedPackgeService redPackgeService;
    private String takerID;
    public PackageTaker(int i,RedPackgeService redPackgeService){
        takerID =" taker " + i;
        this.redPackgeService = redPackgeService;
    }
    @Override
    public void run(){
        System.out.println("runnable " + takerID);
        try{
            Thread.sleep((int)(Math.random()*5000));
        }catch (Exception exc){
            System.out.println(exc.getMessage());
        }


        String text = redPackgeService.take(takerID);
        System.out.println("taker is " + takerID + text);
    }
}
