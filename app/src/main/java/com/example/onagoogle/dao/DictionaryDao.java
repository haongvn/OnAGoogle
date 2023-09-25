package com.example.onagoogle.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.onagoogle.entity.DictionaryWord;

import java.util.List;

@Dao
public interface DictionaryDao {
    @Query("SELECT * FROM Word WHERE word LIKE :searchQuery")
    List<DictionaryWord> searchWords(String searchQuery);

    // Các phương thức truy vấn khác
    // ..

}