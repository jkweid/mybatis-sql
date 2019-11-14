package com.github.mybatissql;

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
            SqlFun Insert=(SqlFun)Para.get("Obj");
            return Insert.getAdd();
        }

        /*删除语句的解析*/
        public String Delete(Map<Object,Object> Para)
        {
            SqlFun Delete=(SqlFun)Para.get("Obj");
            return Delete.getDelete()+Delete.getWhere();
        }

        /*修改语句的解析*/
        public String Update (Map<Object,Object> Para)
        {
            SqlFun Update=(SqlFun)Para.get("Obj");
            return Update.getUpdate()+Update.getWhere();
        }

        /*查找语句的解析*/
        public String  Select(Map<String,Object> para)
        {
            return SqlUtil.Sql(para.get("Obj"));
        }
    }


    //运行添加的语句
    @InsertProvider(type=SqlWays.class,method ="Insert")
    int RunAdd(@Param("Obj") Object Obj);

    @Select("${Sql}")
    int Add(@Param("Sql") String Sql);


    //运行删除的语句
    @DeleteProvider(type=SqlWays.class,method ="Delete")
    int RunDel(@Param("Obj") Object Obj);

    @Select("${Sql}")
    int Delete(@Param("Sql") String Sql);


    //运行修改的语句
    @UpdateProvider(type=SqlWays.class,method ="Update")
    int RunUpd(@Param("Obj") Object Obj);

    @Select("UPDATE #{Table} SET #{Filed}=#{Value} WHERE #{Trem}=#{TremValue} LIMIT 1")
    int Update(@Param("Table") String T, @Param("Filed") String F, @Param("Value") String V, @Param("Trem") String W, @Param("TremValue") String WV);


    //运行查找多条的语句
    @SelectProvider(type=SqlWays.class,method ="Select")
    List<T> RunSelM(@Param("Obj") Object Obj);

    @Select("${Sql}")
    List<T> SelectL(@Param("Sql") String Sql);


    //运行查找一条数据
    @SelectProvider(type=SqlWays.class,method ="Select")
    T RunSelO(@Param("Obj") Object Obj);

    @Select("${Sql}")
    T SelectO(@Param("Sql") String Sql);

    //运行只查询表中某个字段的值
    @SelectProvider(type=SqlWays.class,method ="Select")
    String RunSelV(@Param("Obj") Object Obj);

    @Select("${Sql}")
    String SelectV(@Param("Sql") String Sql);


    //运行统计数据
    @SelectProvider(type=SqlWays.class,method ="Select")
    int RunCount(@Param("Obj") Object Obj);

}
