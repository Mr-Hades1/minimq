package com.singularityfold.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 加载出一个好看的Banner
 *
 * @author Mr_Hades
 * @date 2022-03-26 21:25
 */
public class BannerUtil {
    private static Logger log = LoggerFactory.getLogger(BannerUtil.class);

    public static void loadBanner(String filename) {
        try {
            InputStream inputStream = BannerUtil.class.getClassLoader().getResourceAsStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            System.out.println();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println();
            log.debug("banner loaded successfully!");
        } catch (IOException e) {
            // 忽略输出，不加载banner
            log.debug("banner loading failed");
        }
    }
}
