package com.sea.turtle.soup.turup.dao.mapper;

import com.github.pagehelper.PageInfo;
import com.sea.turtle.soup.turup.dao.entity.Record;
import com.sea.turtle.soup.turup.dto.resp.RecordResponse;
import com.sea.turtle.soup.turup.dto.resp.UserResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RecordMapper {
    List<RecordResponse> getUserGameRecords(@Param("userId") int userId);

    @Select("SELECT id FROM `user` WHERE openid = #{openid}")
    Integer getUserIdByOpenid(String openid);


    /**
     * 插入一条游戏记录到 record 表
     *
     * @param record 游戏记录实体类
     */
    @Insert("INSERT INTO record (user_id, puzzle_id, score, question_count, start_time, end_time) " +
            "VALUES (#{userId}, #{puzzleId}, #{score}, #{questionCount}, #{startTime}, #{endTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertIntoByUserId(Record record);
}
