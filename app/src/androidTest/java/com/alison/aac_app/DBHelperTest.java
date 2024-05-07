package com.alison.aac_app;
import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertTrue;

import com.alison.aac_app.database.DBHelper;

public class DBHelperTest {

    private DBHelper dbHelper;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        File tempDir = context.getCacheDir();
        dbHelper = new DBHelper(context, tempDir.getAbsolutePath());
    }

    @After
    public void tearDown() {
        dbHelper.deleteDb();
    }

    @Test
    public void testGetWordsSentences() {
        try {
            List<String> words = dbHelper.getWordsSentences();
            assertTrue(words.size() > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetWordsMinLength() {
        try {
            int minLength = 3;
            List<String> words = dbHelper.getWordsMinLength(minLength);
            for (String word : words) {
                assertTrue(word.length() >= minLength);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetImages() {
        try {
            List<String> wordList = Arrays.asList("morning", "afternoon", "night");
            List<String> images = dbHelper.getImages(wordList);
            assertTrue(images.size() > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSentences() {
        try {
            List<String> sentences = dbHelper.getSentences();
            assertTrue(sentences.size() > 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
