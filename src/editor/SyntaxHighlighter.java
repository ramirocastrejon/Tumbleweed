package editor;

import java.io.*;

import java.time.Duration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

public class SyntaxHighlighter {

    private static final String[] KEYWORDS = new String[] {
            "abstract", "assert", "boolean", "break", "byte",
            "case", "catch", "char", "class", "const",
            "continue", "default", "do", "double", "else",
            "enum", "extends", "final", "finally", "float",
            "for", "goto", "if", "implements", "import",
            "instanceof", "int", "interface", "long", "native",
            "new", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super",
            "switch", "synchronized", "this", "throw", "throws",
            "transient", "try", "void", "volatile", "while"
    };

    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String ADD_PATTERN = "\\+";
    private static final String SUB_PATTERN = "\\-";
    private static final String MUL_PATTERN = "\\*";
    private static final String DIV_PATTERN = "\\/";
    private static final String OR_PATTERN = "\\|\\|";
    private static final String AND_PATTERN = "\\&\\&";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
            + "|(?<STRING>" + STRING_PATTERN + ")"
            + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
            + "|(?<ADD>" + ADD_PATTERN + ")"
            + "|(?<SUB>" + SUB_PATTERN + ")"
            + "|(?<MUL>" + MUL_PATTERN + ")"
            + "|(?<DIV>" + DIV_PATTERN + ")"
            + "|(?<OR>" + OR_PATTERN + ")"
            + "|(?<AND>" + AND_PATTERN + ")"
    );

    public static StyleSpans<Collection<String>> highlight(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int keywordIndex = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                matcher.group("KEYWORD") != null ? "keyword" :
                matcher.group("STRING") != null ? "string" :
                matcher.group("COMMENT") != null ? "comment" :
                matcher.group("ADD") != null ? "op" :
                matcher.group("SUB") != null ? "op" :
                matcher.group("MUL") != null ? "op" :
                matcher.group("DIV") != null ? "op" :
                matcher.group("OR") != null ? "op" :
                matcher.group("AND") != null ? "op" : null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - keywordIndex);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            keywordIndex = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - keywordIndex);
        return spansBuilder.create();
    }
}
