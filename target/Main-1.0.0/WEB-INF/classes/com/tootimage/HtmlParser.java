package com.tootimage;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class HtmlParser extends HTMLEditorKit.ParserCallback {
    private StringBuilder sb;

    HtmlParser(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public void handleComment(char[] chars, int i) {
        System.out.format("handleComment: %s\n", new String(chars));
    }

    @Override
    public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet mutableAttributeSet, int i) {
        System.out.format("handleSimpleTag: %s\n", tag);
    }

    @Override
    public void handleStartTag(HTML.Tag tag, MutableAttributeSet mutableAttributeSet, int i) {
        System.out.format("handleStartTag: %s\n", tag);
    }

    @Override
    public void handleEndOfLineString(String s) {
        System.out.format("handleEndOfLineString: %s\n", s);
        sb.append(s).append(" ");
    }

    @Override
    public void handleEndTag(HTML.Tag tag, int i) {
        System.out.format("handleEndTag: %s\n", tag);
    }

    @Override
    public void handleError(String s, int i) {
        System.out.format("handleError: %s position: %d\n", s, i);
    }

    @Override
    public void handleText(char[] chars, int i) {
        System.out.format("handleEndOfLineString: %s\n", new String(chars));
        sb.append(new String(chars)).append(" ");
    }
}
