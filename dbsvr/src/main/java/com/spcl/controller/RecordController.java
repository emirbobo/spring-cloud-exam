package com.spcl.controller;

import com.spcl.dao.RecordDao;
import com.spcl.entity.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
    public Record getById(@PathVariable int id)
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

    @RequestMapping("/records")
    @ResponseBody
    public List<Record> list()
    {
        List<Record> records = recordDao.getAllRecords();
        return records;
    }
}
