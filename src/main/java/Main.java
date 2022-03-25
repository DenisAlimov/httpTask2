import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class Main {
    public static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key=cqChOQ51m1g06vk6DTrjqEJ44HZ6yaCGmVHg8qo1");

        CloseableHttpResponse response = httpClient.execute(request);

        Nasa nasa = mapper.readValue(response.getEntity().getContent(), new TypeReference<>() {
        });
        String url = nasa.getUrl();
        HttpGet request1 = new HttpGet(url);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

        response = httpClient.execute(request1);

        File file = new File("url.jpg");

         try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
         BufferedImage bufferedImage = ImageIO.read(response.getEntity().getContent());
         ImageIO.write(bufferedImage, "jpg", baos);
         baos.flush();
         ImageIO.write(bufferedImage, "jpg", file);
         } catch (IOException e) {
         System.out.println(e.getMessage());
         }
//  Способ загрузки сообщения по url без повторного запроса:
//        URL url1 = new URL(url);
//        try {
//            ImageIO.write(ImageIO.read(url1), "jpg",new File("url.jpg"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        System.out.println("Done");
    }
}