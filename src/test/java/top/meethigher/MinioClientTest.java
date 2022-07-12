package top.meethigher;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import org.junit.Test;
import top.meethigher.utils.MinioClientBuilder;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

public class MinioClientTest {



    @Test
    public void download() throws Exception {
        MinioClient minioClient = MinioClientBuilder.build();
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket("xiangwan")
                .object("巴扎黑.png")
                .build();
        InputStream is = minioClient.getObject(args);
        FileOutputStream fos = new FileOutputStream("xiangwan.jpg");
        int len;
        byte[] b=new byte[1024];
        while((len=is.read(b))!=-1) {
            fos.write(b,0,len);
        }
        fos.close();
        is.close();
    }

    @Test
    public void query() throws Exception{
        MinioClient minioClient = MinioClientBuilder.build();
        ListObjectsArgs args= ListObjectsArgs.builder()
                .bucket("xiangwan")
                //指定文件夹
                //.prefix("test/")
                .build();
        Iterable<Result<Item>> results = minioClient.listObjects(args);
        for (Result<Item> next : results) {
            Item item = next.get();
            boolean dir = item.isDir();
            if (dir) {
                System.out.printf("文件名:%s,文件修改时间:0,文件大小:0,文件类型:%s\n", item.objectName(),
                        item.isDir());
            } else {
                System.out.printf("文件名:%s,文件修改时间:%s-%s-%s %s:%s:%s,文件大小:%s,文件类型:%s\n", URLDecoder.decode(item.objectName(), "utf-8"),
                        item.lastModified().getYear(), item.lastModified().getMonthValue(), item.lastModified().getDayOfMonth(),
                        item.lastModified().getHour(), item.lastModified().getMinute(), item.lastModified().getSecond(),
                        item.size(),
                        item.isDir());
            }
        }
    }

    @Test
    public void delete() throws Exception {
        MinioClient minioClient = MinioClientBuilder.build();
        //删除单个
        RemoveObjectArgs args= RemoveObjectArgs.builder()
                .bucket("xiangwan")
                .object("log")
                .build();
        minioClient.removeObject(args);

        //删除多个
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket("xiangwan")
                .prefix("test/")
                .build());
        List<DeleteObject> deleteObjects=new LinkedList<>();
        for (Result<Item> result : results) {
            Item item = result.get();
            deleteObjects.add(new DeleteObject(URLDecoder.decode(item.objectName(),"utf-8")));
        }
        RemoveObjectsArgs argss= RemoveObjectsArgs.builder()
                .bucket("xiangwan")
                .objects(deleteObjects)
                .build();
        //该方法是懒加载，类似于Feature，需要拿到返回值执行
        Iterable<Result<DeleteError>> removeObjects = minioClient.removeObjects(argss);
        for (Result<DeleteError> removeObject : removeObjects) {
            DeleteError error = removeObject.get();
            System.out.printf("minio删除错误->bucketName=%s,objectName=%s,message=%s\n", error.bucketName(), error.objectName(), error.message());
        }
    }

    @Test
    public void upload() throws Exception{
        MinioClient minioClient = MinioClientBuilder.build();
        UploadObjectArgs args = UploadObjectArgs.builder()
                .bucket("xiangwan")
                .object("wuhuang.jpg")
                //创建并上传到test文件夹
                //.object("test/wuhuang.jpg")
                .filename("C:\\Users\\14251\\Desktop\\吾皇.png")
                .build();
        minioClient.uploadObject(args);
    }


}
