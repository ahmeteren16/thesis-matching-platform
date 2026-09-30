package com.lorem_ipsum.thesis.service;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class MarkdownService {

    public String convertToHtml(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return "";
        }

        String html = markdown;
        
        html = html.replaceAll("(?m)^### (.+)$", "<h3>$1</h3>");
        html = html.replaceAll("(?m)^## (.+)$", "<h2>$1</h2>");
        html = html.replaceAll("(?m)^# (.+)$", "<h1>$1</h1>");

        html = html.replaceAll("\\*\\*(.+?)\\*\\*", "<strong>$1</strong>");

        html = html.replaceAll("\\*(.+?)\\*", "<em>$1</em>");

        html = html.replaceAll("\\[(.+?)\\]\\((.+?)\\)", "<a href=\"$2\">$1</a>");

        html = html.replaceAll("(?m)^(?!<[hul]|$)(.+)$", "<p>$1</p>");

        html = removeScriptTags(html);

        return html;
    }

    public String removeScriptTags(String html) {

        Pattern scriptPattern = Pattern.compile("<script[^>]*>.*?</script>", 
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        html = scriptPattern.matcher(html).replaceAll("");
        
        Pattern singleScriptPattern = Pattern.compile("<script[^>]*>", 
                Pattern.CASE_INSENSITIVE);
        html = singleScriptPattern.matcher(html).replaceAll("");
        
        return html;
    }
}