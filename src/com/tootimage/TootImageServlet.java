package com.tootimage;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TootImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     //   response.setContentType("application/force-download");
        String url = request.getParameter("url");
        String host = request.getParameter("host");
        String noticeId = request.getParameter("noticeId");
        System.out.format("url: %s host: %s noticeId: %s\n", url, host, noticeId);
        Main main = new Main();
        ImageIO.write(main.tootImage(url), "png", response.getOutputStream());
    }
}
