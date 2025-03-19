package com.sea.turtle.soup.turup.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sea.turtle.soup.turup.dao.entity.Record;
import com.sea.turtle.soup.turup.dao.mapper.RecordMapper;
import com.sea.turtle.soup.turup.dto.resp.RecordResponse;
import com.sea.turtle.soup.turup.dto.resp.UserResponse;
import com.sea.turtle.soup.turup.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {
    @Autowired
    private RecordMapper recordMapper;
    @Override
    public PageInfo<RecordResponse> getUserGameRecordsByPage(int userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<RecordResponse> records = recordMapper.getUserGameRecords(userId);
        return new PageInfo<>(records);
    }

    @Override
    public Integer getUserIdByOpenid(String openid) {
        int id = recordMapper.getUserIdByOpenid(openid);
        return id;
    }

    @Override
    public void insert(Record record) {
        recordMapper.insertIntoByUserId(record);
    }


}
