import com.singularityfold.client.Consumer;
import com.singularityfold.client.Producer;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;

/**
 * @author Mr_Hades
 * @date 2022-03-27 10:52
 */
public class ClientTest {

//    String url = "ws://www.xhades.top:2333/";
    String url = "ws://localhost:8888/";

    @Test
    public void TestProducer() throws IOException {
        Producer producer1 = new Producer(URI.create(url), "producer_1");
        Producer producer2 = new Producer(URI.create(url), "producer_2");
        Producer producer3 = new Producer(URI.create(url), "producer_3");
        producer1.send("Make America Great Again!", "American.great.again.!");
        producer2.send("China is getting stronger!", "China.daily.com");
        producer2.send("中国建党一百年万岁", "China.xinhua.net");
        producer3.send("The voice from Europe", "UK.Reuters.com");
        System.in.read();

    }

    @Test
    public void TestConsumer() throws Exception {
        Consumer consumer1 = new Consumer(URI.create(url), "American");
        Consumer consumer2 = new Consumer(URI.create(url), "China");
        Consumer consumer3 = new Consumer(URI.create(url), "UK");
        consumer1.register("American", true);
        consumer1.onMessage((String message) -> System.out.println("American: " + message));
        consumer2.register("China", true);
        consumer2.onMessage((String message) -> System.out.println("China: " + message));
        consumer3.register("UK", true);
        consumer3.onMessage((String message) -> System.out.println("UK: " + message));
        System.in.read();
    }


}
