package com.singularityfold.util;

/**
 * 参考rabbitMQ的匹配规则，对用户的键与队列的键进行匹配
 *
 * @author Mr_Hades
 * @date 2022-03-26 14:28
 */
public class KeyUtil {

    /**<pre>
     * 匹配routingKey和bindingKey的工具类
     * 按照以下规则进行匹配：
     *  1. #：匹配一个或多个词
     *  2. *：匹配不多不少恰好1个词
     *  3. |：或的关系，用来匹配多个key
     * 举例：
     *      item.#：能够匹配item.insert.abc 或者 item.insert
     *      item.*：只能匹配item.insert
     * 注：  #符号只能出现在开头或者结尾，不能出现多次
     *      简单处理，此处不对bindingKey的格式进行检查，由用户自行判断
     *</pre>
     * @return 是否匹配成功
     */
    public static boolean routingKeyCompare(String routingKey, String bindingKey) {
        String[] keys = bindingKey.split("\\|");
        String[] part1 = routingKey.split("\\.");
        for (String key : keys) {
            String[] part2 = key.split("\\.");
            int len2 = part2.length;
            int len1 = part1.length;
            // 包含#的bindingKey
            if (key.contains("#")) {
                int i;
                if (part2[0].equals("#")) {
                    // 从后往前比
                    for (i = 1; i <= Math.min(len1, len2); i++) {
                        if (!part2[len2 - i].equals(part1[len1 - i]))
                            break;
                    }
                    if (part2[len2 - i].equals("#"))
                        return true; // 当前键匹配成功
                } else if (part2[len2 - 1].equals("#")) {
                    // 从前往后比
                    for (i = 0; i < Math.min(len1, len2); i++) {
                        if (!part2[i].equals(part1[i]))
                            break;
                    }
                    if (part2[i].equals("#"))
                        return true; // 当前键匹配成功
                }
            } else {
                boolean flag = true;
                if (len1 == len2) {
                    for (int i = 0; i < len1; i++) {
                        if (!(part2[i].equals("*") || part1[i].equals(part2[i]))) {
                            flag = false;
                            break; // 匹配失败
                        }
                    }
                    if (flag)
                        return true;
                }
            }

        }
        return false;
    }

}
