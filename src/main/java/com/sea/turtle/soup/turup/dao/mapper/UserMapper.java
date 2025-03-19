package com.sea.turtle.soup.turup.dao.mapper;

import com.sea.turtle.soup.turup.dao.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    /**
     * 根据 openid 查询用户
     */
    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User findByOpenid(@Param("openid") String openid);

    /**
     * 插入新用户
     */
    @Insert("INSERT INTO user (openid, nickname, avatar, create_time) " +
            "VALUES (#{openid}, #{nickname}, #{avatar}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertUser(User user);

    /**
     * 更新用户信息
     */
    @Update("UPDATE user SET nickname = #{nickname}, avatar = #{avatar} " +
            "WHERE id = #{id}")
    void updateUser(User user);
}
