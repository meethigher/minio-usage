package top.meethigher.utils;

import io.minio.MinioClient;
import top.meethigher.config.MinioConfig;

/**
 * 获取MinioClientBuilder
 *
 * @author chenchuancheng
 * @since 2022/7/8 14:42
 */
public class MinioClientBuilder {

    private static MinioClient minioClient = null;

    private MinioClientBuilder() {
        minioClient = MinioClient.builder()
                .endpoint(MinioConfig.endpoint)
                .credentials(MinioConfig.accessKey, MinioConfig.secretKey)
                .build();
    }

    private static final class MinioClientBuilderHolder {
        static final MinioClientBuilder minioClientBuilder = new MinioClientBuilder();
    }

    public static MinioClient build() {
        MinioClientBuilder minioClientBuilder = MinioClientBuilderHolder.minioClientBuilder;
        return minioClientBuilder.getMinioClient();
    }

    private MinioClient getMinioClient() {
        return minioClient;
    }
}
