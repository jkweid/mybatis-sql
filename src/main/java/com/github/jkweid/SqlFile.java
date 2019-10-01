package com.github.jkweid;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface SqlFile<T>
{

    class SqlWays
    {
        /*添加语句的生成*/
        public String Insert(Map<Object,Object> Para)
        {
            SqlWay Insert=(SqlWay)Para.get("Obj");
            return Insert.getAdd();
        }

        /*获取删除的语句*/
        public String Delete(Map<Object,Object> Para)
        {
            SqlWay Delete=(SqlWay)Para.get("Obj");
            return Delete.getDelete()+Delete.getWhere();
        }

        /*查找的语句*/
        public String  Select(Map<String,Object> para)
        {
            return SqlUtil.Sql(para.get("Obj"));
        }
    }


    //运行添加的语句
    @InsertProvider(type=SqlWays.class,method ="Insert")
    int RunAdd(@Param("Obj") Object Obj);


    //运行删除的语句
    @DeleteProvider(type=SqlWays.class,method ="Delete")
    int RunDel(@Param("Obj") Object Obj);


    //运行修改的语句
    @UpdateProvider(type=SqlWays.class,method ="Select")
    int RunUpd(@Param("Obj") Object Obj);


    //运行查找多条的语句
    @SelectProvider(type=SqlWays.class,method ="Select")
    List<T> RunSelM(@Param("Obj") Object Obj);


    //运行查找一条数据
    @SelectProvider(type=SqlWays.class,method ="Select")
    T RunSelO(@Param("Obj") Object Obj);


    //运行只查询表中某个字段的值
    @SelectProvider(type=SqlWays.class,method ="Select")
    String RunSelV(@Param("Obj") Object Obj);


    //运行统计数据
    @SelectProvider(type=SqlWays.class,method ="Select")
    int RunCount(@Param("Obj") Object Obj);

}
