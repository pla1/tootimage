package com.tootimage;

import javax.imageio.ImageIO;
import javax.swing.text.html.parser.ParserDelegator;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

public class Utils {
    public static void main(String[] args) throws Exception {
        String url = "https://lh3.googleusercontent.com/uPL7-otO66ZrFzPELpJLOhsbr8OER3PugMr36vVAWTIRrf6WfmYfCSaaYt3h2VtVmpEzbSaa5ql5ro0uM9Jvl0U2zMXObzJAs7BIZbHy-z3npWo6OnSFxulVVPBFbD0UAB9rhq7rW-4=w2400";
        BufferedImage bi = Utils.resizeImage(new URL(url), 100, 100);
        File file = File.createTempFile("test", ".png");
        ImageIO.write(bi, "png", file);
        System.out.format("File: %s Dimension: %dx%d\n", file.getAbsolutePath(), bi.getWidth(), bi.getHeight());
        System.exit(0);
    }

    public static String httpGet(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        InputStream response = connection.getInputStream();
        try (Scanner scanner = new Scanner(response)) {
            String responseBody = scanner.useDelimiter("\\A").next();
            return responseBody;
        }
    }

    public static BufferedImage resizeImage(URL url, int widthNew, int heightNew) {
        BufferedImage bufferedImageOriginal = getBufferdImage(url);
        Image imageTemporary = bufferedImageOriginal.getScaledInstance(widthNew, heightNew, Image.SCALE_SMOOTH);
        BufferedImage bufferedImageResized = new BufferedImage(widthNew, heightNew, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = bufferedImageResized.createGraphics();
        g2d.drawImage(imageTemporary, 0, 0, null);
        g2d.dispose();
        return bufferedImageResized;
    }

    public static BufferedImage getBufferdImage(String urlString) {
        try {
            URL url = new URL(urlString);
            return getBufferdImage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage getBufferdImage(URL url) {
        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.addRequestProperty("User-agent", "curl/7.58.0");
            return ImageIO.read(urlConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String run(String[] commandParts) {
        BufferedReader reader = null;
        StringBuilder output = new StringBuilder();
        int exitValue = 0;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(commandParts);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
                output.append("\n");
            }
            exitValue = process.waitFor();
            System.out.format("Exit value: %d.\n", exitValue);
        } catch (Exception e) {
            output.append("Exception: " + e.getLocalizedMessage());
        } finally {
            close(reader);
        }
        System.out.format("\nProcess exit value: %d\n", exitValue);
        return output.toString();
    }

    public static void close(Object... objects) {
        for (Object object : objects) {
            if (object != null) {
                try {
                    boolean closed = false;
                    if (object instanceof java.io.BufferedOutputStream) {
                        BufferedOutputStream bufferedOutputStream = (BufferedOutputStream) object;
                        bufferedOutputStream.close();
                        closed = true;
                    }
                    if (object instanceof java.io.StringWriter) {
                        StringWriter stringWriter = (StringWriter) object;
                        stringWriter.close();
                        closed = true;
                    }
                    if (object instanceof java.sql.Statement) {
                        Statement statement = (Statement) object;
                        statement.close();
                        closed = true;
                    }
                    if (object instanceof java.io.FileReader) {
                        FileReader fileReader = (FileReader) object;
                        fileReader.close();
                        closed = true;
                    }
                    if (object instanceof java.sql.ResultSet) {
                        ResultSet rs = (ResultSet) object;
                        rs.close();
                        closed = true;
                    }
                    if (object instanceof java.sql.PreparedStatement) {
                        PreparedStatement ps = (PreparedStatement) object;
                        ps.close();
                        closed = true;
                    }
                    if (object instanceof java.sql.Connection) {
                        Connection connection = (Connection) object;
                        connection.close();
                        closed = true;
                    }
                    if (object instanceof java.io.BufferedReader) {
                        BufferedReader br = (BufferedReader) object;
                        br.close();
                        closed = true;
                    }
                    if (object instanceof Socket) {
                        Socket socket = (Socket) object;
                        socket.close();
                        closed = true;
                    }
                    if (object instanceof PrintStream) {
                        PrintStream printStream = (PrintStream) object;
                        printStream.close();
                        closed = true;
                    }
                    if (object instanceof ServerSocket) {
                        ServerSocket serverSocket = (ServerSocket) object;
                        serverSocket.close();
                        closed = true;
                    }
                    if (object instanceof Scanner) {
                        Scanner scanner = (Scanner) object;
                        scanner.close();
                        closed = true;
                    }
                    if (object instanceof InputStream) {
                        InputStream inputStream = (InputStream) object;
                        inputStream.close();
                        closed = true;
                    }
                    if (object instanceof Socket) {
                        Socket socket = (Socket) object;
                        socket.close();
                        closed = true;
                    }
                    if (object instanceof PrintWriter) {
                        PrintWriter pw = (PrintWriter) object;
                        pw.close();
                        closed = true;
                    }
                    if (!closed) {
                        System.out.format("Object not closed. Object type not defined in this close method. %s\n", object.getClass().getName());
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public static void print(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            System.out.format("%d \"%s\"\n", i, strings[i]);
        }
    }

    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }

    public static boolean isBlank(String s) {
        if (s == null) {
            return true;
        }
        if (s.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String htmlToText(String html) {
        StringBuilder sb = new StringBuilder();
        HtmlParser htmlParser = new HtmlParser(sb);
        StringReader stringReader = new StringReader(html);
        try {
            new ParserDelegator().parse(stringReader, htmlParser, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String text = sb.toString().trim();
        text = text.replaceAll(" +", " ");
        text = text.replaceAll("&apos;", "'");
        text = text.replaceAll("# ", "#");
        return text;
    }

    public static BufferedImage roundCorners(BufferedImage bufferedImageSquare, int pixels) {
        System.out.format("Width: %d Height: %d\n", bufferedImageSquare.getWidth(), bufferedImageSquare.getHeight());
        BufferedImage bufferedImageRounded = new BufferedImage(bufferedImageSquare.getWidth(), bufferedImageSquare.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImageRounded.createGraphics();
        g2d.setComposite(AlphaComposite.Src);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fill(new RoundRectangle2D.Float(0, 0, bufferedImageSquare.getWidth(), bufferedImageSquare.getHeight(), pixels, pixels));
        g2d.setComposite(AlphaComposite.SrcAtop);
        g2d.drawImage(bufferedImageSquare, 0, 0, null);
        g2d.dispose();
        System.out.format("Width: %d Height: %d\n", bufferedImageRounded.getWidth(), bufferedImageRounded.getHeight());
        return bufferedImageRounded;
    }

    public static BufferedImage drawErrorMessage(String errorMessage) {
        BufferedImage bufferedImage = new BufferedImage(1920, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bufferedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.setColor(Color.RED);
        g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        g.setFont(g.getFont().deriveFont(30f));
        g.setColor(Color.BLACK);
        g.drawString(errorMessage, 1, 100);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        g.drawString(sdf.format(new Date()), 1, 150);
        return bufferedImage;
    }

}
