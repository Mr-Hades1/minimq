import com.singularityfold.core.QueueManager;
import com.singularityfold.core.WorkerManager;
import com.singularityfold.util.KeyUtil;
import org.junit.Test;

/**
 * @author Mr_Hades
 * @date 2022-03-26 11:04
 */
public class UnitTest {

    @Test
    public void testQueue() {
        QueueManager.init(4, new String[]{"aaa.bbb.ccc", "aa.cc.df", "bnc"});
        QueueManager.getQueue(0).add(
                "hello My guys!"
        );
        try {
            System.out.println(QueueManager.getQueue(0).take());
        } catch (Exception e) {

        }
    }

    @Test
    public void testKeyComparator() {
        // 模拟邮政管理系统
        System.out.println(KeyUtil.routingKeyCompare(
                "中国.台湾省.高雄市", "中国.海南省.*|中国.台湾省.*"
        ));
        System.out.println(KeyUtil.routingKeyCompare(
                "中国.湖北省.黄冈市", "中国.#"
        ));
        System.out.println(KeyUtil.routingKeyCompare(
                "中国.黑龙江省.哈尔滨市", "中国.辽宁省.#|中国.吉林省.#"
        ));
        System.out.println(KeyUtil.routingKeyCompare(
                "中国.黑龙江省.哈尔滨市", "#"
        ));
    }

    @Test
    public void testQueueManager() {
        QueueManager.init(
                3,
                new String[]{"American.#", "China.*.*", "UK.*.*"},
                new String[]{"American", "China", "UK"}
        );
        QueueManager.put("Make America Great Again!", "American.great.again.!");
        QueueManager.put("China is getting stronger!", "China.daily.com");
        QueueManager.put("中国建党一百年", "China.xinhua.net");
        QueueManager.put("The voice from Europe", "UK.Reuters.com");

        for (int i = 0; i < 3; i++) {
            System.out.println(QueueManager.getQueue(i));
        }
    }

    @Test
    public void testWorkerManager() {
        int queueNum = 3;
        QueueManager.init(
                queueNum,
                new String[]{"American.#", "China.*.*", "UK.*.*"},
                new String[]{"American", "China", "UK"}
        );
        WorkerManager.init(queueNum);
        QueueManager.put("Make America Great Again!", "American.great.again.!");
        QueueManager.put("China is getting stronger!", "China.daily.com");
        QueueManager.put("中国建党一百年万岁！", "China.xinhua.net");
        QueueManager.put("The voice from Europe", "UK.Reuters.com");

        for (int i = 0; i < queueNum; i++) {
            System.out.println(QueueManager.getQueue(i));
        }
    }

    @Test
    public void testColor() {
        System.out.println("\033[1;91;40mINFO");
    }


}
