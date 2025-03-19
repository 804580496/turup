package com.sea.turtle.soup.turup.service;

import com.github.pagehelper.PageInfo;
import com.sea.turtle.soup.turup.dao.entity.Record;
import com.sea.turtle.soup.turup.dto.resp.RecordResponse;

import java.util.List;

public interface RecordService {

    PageInfo<RecordResponse> getUserGameRecordsByPage(int userId, int pageNum, int pageSize);

    Integer getUserIdByOpenid(String openid);

    void insert(Record record);
}
