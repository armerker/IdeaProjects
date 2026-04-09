package edu.phystech.hw2.analyzer;


import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

enum Label {
    SPAM, NEGATIVE, TOO_LONG, OK
}

interface TextAnalyzer {
    Label processText(String text);
}

abstract class KeywordAnalyzer implements TextAnalyzer {
    protected abstract List<String> getKeywords();
    protected abstract Label getLabel();
    
    @Override
    public Label processText(String text) {
        for (String keyword : getKeywords()) {
            if (text.contains(keyword)) {
                return getLabel();
            }
        }
        return Label.OK;
    }
}

class SpamAnalyzer extends KeywordAnalyzer {
    private final List<String> keywords;
    
    public SpamAnalyzer(List<String> keywords) {
        this.keywords = keywords;
    }
    
    @Override
    protected List<String> getKeywords() {
        return keywords;
    }
    
    @Override
    protected Label getLabel() {
        return Label.SPAM;
    }
}

class NegativeTextAnalyzer extends KeywordAnalyzer {
    private static final List<String> SAD_SMILEYS = List.of(":(", "=(", ":|");
    
    @Override
    protected List<String> getKeywords() {
        return SAD_SMILEYS;
    }
    
    @Override
    protected Label getLabel() {
        return Label.NEGATIVE;
    }
}

class TooLongTextAnalyzer implements TextAnalyzer {
    private final int maxLength;
    
    public TooLongTextAnalyzer(int maxLength) {
        this.maxLength = maxLength;
    }
    
    @Override
    public Label processText(String text) {
        return text.length() > maxLength ? Label.TOO_LONG : Label.OK;
    }
}

public class TextAnalyzerTest {

    @Test
    public void tooLongTextTest() {
        TooLongTextAnalyzer tooLongTextAnalyzer = new TooLongTextAnalyzer(3);
        Assertions.assertEquals(Label.TOO_LONG, tooLongTextAnalyzer.processText("123123"));
        Assertions.assertEquals(Label.OK, tooLongTextAnalyzer.processText("12"));
    }

    @Test
    public void spamTextTest() {
        SpamAnalyzer spamAnalyzer = new SpamAnalyzer(List.of("kek", "lol"));
        Assertions.assertEquals(Label.SPAM, spamAnalyzer.processText("kek 123"));
        Assertions.assertEquals(Label.OK, spamAnalyzer.processText("123"));
        Assertions.assertEquals(Label.SPAM, spamAnalyzer.processText("123 lol"));
    }

    @Test
    public void negativeTextTest() {
        NegativeTextAnalyzer negativeTextAnalyzer = new NegativeTextAnalyzer();
        Assertions.assertEquals(Label.NEGATIVE, negativeTextAnalyzer.processText("hello :("));
        Assertions.assertEquals(Label.NEGATIVE, negativeTextAnalyzer.processText(":) =("));
        Assertions.assertEquals(Label.NEGATIVE, negativeTextAnalyzer.processText("))) :|"));
        Assertions.assertEquals(Label.OK, negativeTextAnalyzer.processText("))) :||"));
    }
}
