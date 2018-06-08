package com.spcl.dao;

import com.spcl.entity.Record;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Administrator on 2018/6/7.
 */
@Mapper
public interface  RecordDao {

    @Select("SELECT id, name FROM record WHERE id = #{id}")
    Record findById(@Param(value = "id") int id);

    @Select("SELECT id, name FROM record WHERE name = #{name}")
    Record findByName(@Param(value = "username") String username);

    @Insert("Insert into record (name) values (#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "record.id")
    void createRecord(Record user);

    @Select("select * from Record")
    List<Record> getAllRecords();
}
