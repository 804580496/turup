package com.sea.turtle.soup.turup.dao.mapper;


import com.sea.turtle.soup.turup.dao.entity.Puzzle;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface PuzzleMapper {

    /**
     * 插入一条题目数据到 puzzle 表，并将生成的 id 保存到传入的 puzzle 对象中
     *
     * @param puzzle 题目实体类
     */
    @Insert("INSERT INTO puzzle (title, content, answer, type, difficulty, create_time) " +
            "VALUES (#{title}, #{content}, #{answer}, #{type}, #{difficulty}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertPuzzle(Puzzle puzzle);
}
