package com.tootimage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class Utils {
    public static void main(String[] args) {
        String url = "https://pla.social/api/statuses/show/1701797.json";
        String output = Utils.httpGet(url);
        System.out.format("%s\n%s\n", url, output);
    }
    public static String httpGet(String url) {
        try {
            URLConnection connection = new URL(url).openConnection();
            InputStream response = connection.getInputStream();
            try (Scanner scanner = new Scanner(response)) {
                String responseBody = scanner.useDelimiter("\\A").next();
                return responseBody;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
        System.out.format("Image URL: %s File: %s\n", imageUrl, destinationFile);
        URL url = new URL(imageUrl);
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }
    public static String getExtension(String url) {
        if (url == null) {
            return url;
        }
        int pos = url.lastIndexOf(".");
        if (pos > -1) {
            return url.substring(pos);
        } else {
            return null;
        }
    }
}
