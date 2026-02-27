package me.wickyplays.discord.leaflet.utils;

import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;

public class LanguageUtil {

    private static final LanguageDetector DETECTOR =
            new OptimaizeLangDetector().loadModels();

    public static boolean isEnglish(String text) {
        LanguageResult result = DETECTOR.detect(text);
        return "en".equals(result.getLanguage());
    }

    public static String getLanguage(String text) {
        LanguageResult result = DETECTOR.detect(text);
        return result.getLanguage();
    }
}