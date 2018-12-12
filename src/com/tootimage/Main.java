package com.tootimage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        System.out.println(new Date());
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://pla.social/notice/1731952");
      //      urls.add("https://pleroma.soykaf.com/notice/21987533");
      //      urls.add("https://pleroma.soykaf.com/notice/21948548");
      //      urls.add("https://pleroma.soykaf.com/notice/22003228");
       //     urls.add("https://pleroma.soykaf.com/notice/22005168");
        for (String url : urls) {
            main.tootImage(url);
        }
    }


    private String[] wrapText(String text) {
        ArrayList<String> lines = new ArrayList<>();
        String[] words = text.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            sb.append(words[i]).append(" ");
            if (sb.length() > 40) {
                lines.add(sb.toString());
                sb = new StringBuilder();
            }
        }
        lines.add(sb.toString());
        return lines.toArray(new String[0]);
    }

    private void tootImage(String url) {
        url = getJsonUrl(url);
        System.out.format("%s\n", url);
        String output = Utils.httpGet(url);
        System.out.format("%s\n%s\n", url, output);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Toot toot = gson.fromJson(output, Toot.class);
        System.out.format("Fav: %d Repeat: %d Text: %s\n", toot.getFave_num(), toot.getRepeat_num(), toot.getText());
        ArrayList<Attachment> attachments = toot.getAttachments();
        try {
            for (Attachment attachment : attachments) {
                File imageFileTemp = File.createTempFile("imageAttachment", Utils.getExtension(attachment.getUrl()));
                Utils.saveImage(attachment.getUrl(), imageFileTemp.getAbsolutePath());
                System.out.format("%s %s\n", imageFileTemp.getAbsoluteFile(), url);
            }
            System.out.format("Profile URL: %s\n", toot.getUser().getProfile_image_url());
            BufferedImage bufferedImageProfile = ImageIO.read(new URL(toot.getUser().getProfile_image_url()));
            Image tmp = bufferedImageProfile.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);

            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();
            File outputFileImageProfile = File.createTempFile("profileImage", ".jpeg");
            ImageIO.write(dimg, "jpg", outputFileImageProfile);

            BufferedImage header = new BufferedImage(500, 1000, BufferedImage.TYPE_3BYTE_BGR);
            g2d = header.createGraphics();
        //    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
        //            RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g2d.drawImage(dimg, 0, 0, null);
            g2d.setFont(g2d.getFont().deriveFont(30f));
            g2d.drawString(toot.getUser().getName(), 105, 30);
            g2d.setFont(g2d.getFont().deriveFont(20f));
            g2d.drawString(toot.getCreated_at().replace(" +0000", ""), 105, 90);


            String[] lines = wrapText(toot.getText());
            int h = 120;
            for (String line:lines) {
                System.out.format("%s\n", line);
                g2d.drawString(line, 1, h);
                h += 25;
            }


            for (Attachment attachment : attachments) {
                BufferedImage bi = ImageIO.read(new URL(attachment.getUrl()));
                g2d.drawImage(bi, 1,h,null);
                h += bi.getHeight();
            }

            File outputFileHeader = File.createTempFile("header", ".png");
            ImageIO.write(header, "png", outputFileHeader);
            //       File imageFileProfile = File.createTempFile("imageProfile", Utils.getExtension(toot.getUser().getProfile_image_url()));
            //       Utils.saveImage(toot.getUser().getProfile_image_url(), imageFileProfile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getJsonUrl(String tootUrl) {
        String[] words = tootUrl.split("/+");
        if (words.length < 3) {
            System.out.format("Invalid URL splits into %d words.", words.length);
            return null;
        }
        for (String word : words) {
            System.out.format("%s\n", word);
        }
        if (!words[0].equals("https:")) {
            return null;
        }
        return String.format("https://%s/api/statuses/show/%s.json", words[1], words[3]);
    }
}
