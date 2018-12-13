package com.tootimage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.imageio.ImageIO;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        System.out.println(new Date());
        ArrayList<String> urls = new ArrayList<>();
urls.add("https://pla.social/notice/1795885");

        if (false) {
            urls.add("https://pla.social/notice/1794765");
            urls.add("https://pla.social/web/statuses/1734723");
            urls.add("https://pla.social/web/statuses/1763490");
            urls.add("https://pla.social/web/statuses/1777999");
            urls.add("https://pla.social/notice/1794437");
            urls.add("https://pla.social/notice/1794159");
            urls.add("https://mastodon.sdf.org/@andyc/101229826636296921");
            urls.add("https://pla.social/notice/1731952");
            urls.add("https://mastodon.social/@pla1/100373815343386126");
            urls.add("https://pla.social/notice/1792178");
            urls.add("https://pla.social/notice/1770125");
            urls.add("https://pleroma.soykaf.com/notice/21987533");
            urls.add("https://pleroma.soykaf.com/notice/21948548");
            urls.add("https://pleroma.soykaf.com/notice/22003228");
            urls.add("https://pleroma.soykaf.com/notice/22005168");
            urls.add("https://pleroma.pla1.net/notice/414688");
        }
        for (String url : urls) {
            main.tootImage(url);
        }
    }


    private String[] wrapText(String text) {
        ArrayList<String> lines = new ArrayList<>();
        String[] words = text.split("\\s+");
        for (String word : words) {
            System.out.format("%s\n", word);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            if (sb.length() > 40 || sb.length() + words[i].length() > 40) {
                lines.add(sb.toString());
                sb = new StringBuilder();
                sb.append(words[i]).append(" ");
            } else {
                sb.append(words[i]).append(" ");
            }
        }
        lines.add(sb.toString());
        return lines.toArray(new String[0]);
    }

    public BufferedImage tootImage(String url) {
        int widthMaximum = 500;
        String[] words = url.split("/+");
        for (String word : words) {
            System.out.format("%s\n", word);
        }
        if (words.length < 3) {
            System.out.format("Invalid URL splits into %d words.", words.length);
            return null;
        }
        if (!words[0].equals("https:")) {
            System.out.format("Invalid URL. Does not start with https:. URL: %s", url);
            return null;
        }
        String hostName = words[1];
        String noticeId = words[words.length -1];
        url = String.format("https://%s/api/v1/statuses/%s", hostName, noticeId);
        System.out.format("%s\n", url);
        String output = null;
        try {
            output = Utils.httpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
            BufferedImage bufferedImage = Utils.drawErrorMessage(e.getLocalizedMessage());
            try {
                File file = File.createTempFile("error", ".png");
                System.out.format("Error file: %s\n", file.getAbsolutePath());
                ImageIO.write(bufferedImage, "png", file);
                Utils.run(new String[]{"/usr/bin/xdg-open", file.getAbsolutePath()});
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
        System.out.format("%s\n%s\n", url, output);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Toot toot = gson.fromJson(output, Toot.class);
        String text = Utils.htmlToText(toot.getContent());
        System.out.format("Fav: %d Repeat: %d Replies: %s Text: %s\n", toot.getFavourites_count(), toot.getReblogs_count(), toot.getReplies_count(), text);
        try {
            System.out.format("Profile URL: %s\n", toot.getAccount().getAvatar());
            BufferedImage bufferedImageProfile = Utils.resizeImage(new URL(toot.getAccount().getAvatar()), 100, 100);
            BufferedImage bufferedImageMain = new BufferedImage(widthMaximum, 4000, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g2d = bufferedImageMain.createGraphics();
            g2d.setPaint(Color.WHITE);
            g2d.fillRect(0, 0, bufferedImageMain.getWidth(), bufferedImageMain.getHeight());
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g2d.drawImage(bufferedImageProfile, 0, 0, null);
            g2d.setFont(g2d.getFont().deriveFont(30f));
            g2d.setPaint(Color.BLACK);
            g2d.drawString(toot.getAccount().getDisplay_name(), 105, 30);
            g2d.setFont(g2d.getFont().deriveFont(20f));
            g2d.drawString(toot.getAccount().getAcct(), 105, 60);
            g2d.drawString(toot.getCreated_at().replace(" +0000", ""), 105, 90);
            String[] lines = wrapText(text);
            int h = 120;
            for (String line : lines) {
                System.out.format("%s\n", line);
                g2d.drawString(line, 1, h);
                h += 25;
            }
            String popularity = String.format("↺ %d      ☆ %d", toot.getReblogs_count(), toot.getFavourites_count());
            g2d.drawString(popularity, 1, h);
            h += 25;
            System.out.format("%d attachments.\n", toot.getMedia_attachments().size());
            for (Media_attachment attachment : toot.getMedia_attachments()) {
                BufferedImage bufferedImage = Utils.getBufferdImage(attachment.getUrl());
                if (bufferedImage.getWidth() > widthMaximum) {
                    int width = widthMaximum - 3;
                    int height = (width * bufferedImage.getHeight()) / bufferedImage.getWidth();
                    bufferedImage = Utils.resizeImage(new URL(attachment.getUrl()), width, height);
                }
                g2d.drawImage(bufferedImage, 1, h, null);
                h += bufferedImage.getHeight() + 10;
            }
            String outputFileName = String.format("/tmp/%s_%s.png", hostName, noticeId);
            File outputFileImage = new File(outputFileName);
            System.out.format("Height: %d\n", h);
            BufferedImage bufferedImageSquare = bufferedImageMain.getSubimage(0, 0, bufferedImageMain.getWidth(), h + 10);
            return Utils.roundCorners(bufferedImageSquare, 30);
       //     ImageIO.write(Utils.roundCorners(bufferedImageSquare, 30), "png", outputFileImage);
       //     Utils.run(new String[]{"/usr/bin/xdg-open", outputFileImage.getAbsolutePath()});
        } catch (IOException e) {
            e.printStackTrace();
        }
return null;
    }


}
