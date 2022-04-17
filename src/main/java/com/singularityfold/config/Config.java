package com.singularityfold.config;

import com.singularityfold.core.QueueManager;
import com.singularityfold.core.WorkerManager;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author Mr_Hades
 * @date 2022-03-26 13:59
 */
@Slf4j(topic = "Config")
public class Config {

    static {
        try {
            InputStream inputStream =
                    Config.class.getClassLoader().getResourceAsStream("config.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            int queueNum = Integer.parseInt((String) properties.get("queueNum"));
            String rawKeys = (String) properties.get("bindingKeys");
            String[] bindingKeys = rawKeys.split(",\\s+");
            String[] queueNames = null;
            try {
                String rawQueueNames = (String) properties.get("queueNames");
                queueNames = rawQueueNames.split(",\\s+");
            } catch (Exception e) {
                // 若查找不到QueueNames参数则忽略
                log.info("Not found queueNames definition, use default naming strategy.");
            }
            init(queueNum, bindingKeys, queueNames);

        } catch (Exception e) {
            // 若加载异常，则抛出运行时异常
            throw new RuntimeException("Can't not find Config.properties or the format is wrong.");
        }
    }


    /**
     * 对MQ Server进行参数配置
     *
     * @param queueNum    队列的数量
     * @param bindingKeys 每个队列对应的key
     */
    private static void init(int queueNum, String[] bindingKeys) {
        init(queueNum, bindingKeys, null);
    }

    /**
     * 对MQ Server进行参数配置
     *
     * @param queueNum    队列的数量
     * @param bindingKeys 每个队列对应的key
     * @param queueNames  每个队列对应的名字
     */
    private static void init(int queueNum, String[] bindingKeys, String[] queueNames) {
        QueueManager.init(queueNum, bindingKeys, queueNames);
        WorkerManager.init(queueNum);
        if (QueueManager.isInited() && WorkerManager.isInited()) {
            log.info("All is ready.");
        }
    }

}
