package com.spcl.controller;

import com.spcl.dao.RecordDao;
import com.spcl.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2018/6/7.
 */
@RestController
public class RecordController
{
    @Autowired
    private RecordDao recordDao;

    @RequestMapping(value = "/record/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Record getById(@RequestParam int id)
    {
        return recordDao.findById(id);
    }

    @RequestMapping(value = "/record", method = RequestMethod.POST)
    @ResponseBody
    public Record create(@RequestParam String name)
    {
        Record record = new Record();
        record.setName(name);
        recordDao.createRecord(record);
        return record;
    }
}
