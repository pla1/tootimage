package com.tootimage;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TootImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getParameter("url");
        String host = request.getParameter("host");
        String noticeId = request.getParameter("noticeId");
        String servletPath = request.getServletPath();
        System.out.format("PARAMETERS - url: %s host: %s noticeId: %s servlet path: %s\n", url, host, noticeId, servletPath);
        if (Utils.isBlank(url)) {
            if (Utils.isNotBlank(host) && Utils.isNotBlank(noticeId)) {
                url = String.format("https://%s/api/v1/statuses/%s", host, noticeId);
            } else {
                String[] words = servletPath.split("/");
                Utils.print(words);
                if (words.length > 2) {
                    host = words[1];
                    noticeId = words[2];
                    url = String.format("https://%s/api/v1/statuses/%s", host, noticeId);
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters. url or host and noticeId. Example: https://tootimage.com/?url=https://pla.social/notice/1770125 See the software repository for more info: https://github.com/pla1/tootimage");
                    return;
                }
            }
        } else {
            String[] words = url.split("/+");
            host = words[1];
            noticeId = words[words.length - 1];
        }
        String fileName = String.format("/opt/tootimages/%s_%s.png", host, noticeId);
        File file = new File(fileName);
        System.out.format("Output file: %s exists: %s\n", file.getAbsolutePath(), file.exists());
        if (file.exists()) {
            writeFile(request, response, file);
            return;
        }
        Main main = new Main();
        BufferedImage bufferedImage = main.tootImage(url);
        ImageIO.write(bufferedImage, "png", file);
        ImageIO.write(bufferedImage, "png", response.getOutputStream());
    }

    private void writeFile(HttpServletRequest request, HttpServletResponse response, File file) throws ServletException, IOException {
        ServletContext servletContext = request.getServletContext();
        String mime = servletContext.getMimeType(file.getAbsolutePath());
        if (mime == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, String.format("MIME type is null for file %s", file.getAbsolutePath()));
            return;
        }
        response.setContentType(mime);
        response.setContentLength((int) file.length());
        FileInputStream in = new FileInputStream(file);
        OutputStream out = response.getOutputStream();
        byte[] buf = new byte[1024];
        int count;
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        out.close();
        in.close();
    }
}
