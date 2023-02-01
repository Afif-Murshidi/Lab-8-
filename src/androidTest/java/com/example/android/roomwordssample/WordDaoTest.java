package com.example.android.roomwordssample;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;


@RunWith(AndroidJUnit4.class)
public class WordDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private WordDao mWordDao;
    private WordRoomDatabase mDb;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();

        mDb = Room.inMemoryDatabaseBuilder(context, WordRoomDatabase.class)
                .allowMainThreadQueries()
                .build();
        mWordDao = mDb.wordDao();
    }

    @After
    public void closeDb() {
        mDb.close();
    }

    @Test
    public void insertAndGetWord() throws Exception {
        Word word = new Word("word");
        mWordDao.insert(word);
        List<Word> allWords = LiveDataTestUtil.getValue(mWordDao.getAlphabetizedWords());
        assertEquals(allWords.get(0).getWord(), word.getWord());
    }

    @Test
    public void getAllWords() throws Exception {
        Word word = new Word("aaa");
        mWordDao.insert(word);
        Word word2 = new Word("bbb");
        mWordDao.insert(word2);
        List<Word> allWords = LiveDataTestUtil.getValue(mWordDao.getAlphabetizedWords());
        assertEquals(allWords.get(0).getWord(), word.getWord());
        assertEquals(allWords.get(1).getWord(), word2.getWord());
    }

    @Test
    public void deleteAll() throws Exception {
        Word word = new Word("word");
        mWordDao.insert(word);
        Word word2 = new Word("word2");
        mWordDao.insert(word2);
        mWordDao.deleteAll();
        List<Word> allWords = LiveDataTestUtil.getValue(mWordDao.getAlphabetizedWords());
        assertTrue(allWords.isEmpty());
    }
}
