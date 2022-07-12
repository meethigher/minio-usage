package top.meethigher.config;

import java.io.IOException;
import java.util.Properties;

/**
 * minio配置
 *
 * @author chenchuancheng
 * @since 2022/7/8 14:41
 */
public class MinioConfig {
    //连接url
    public static String endpoint;
    //公钥
    public static String accessKey;
    //私钥
    public static String secretKey;

    static {
        Properties prop = new Properties();
        try {
            prop.load(MinioConfig.class.getClassLoader().getResourceAsStream("application.properties"));
            endpoint=prop.getProperty("endpoint");
            accessKey=prop.getProperty("accessKey");
            secretKey=prop.getProperty("secretKey");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
