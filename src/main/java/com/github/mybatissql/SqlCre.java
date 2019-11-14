package com.github.mybatissql;

import java.util.List;

public class SqlCre
{
    private static SqlFun Create()
    {
        return new SqlFun();
    }


    //添加一条数据
    public static SqlFun Add(Object Obj)
    {
        return Create().Insert(Obj);
    }
    //添加多条数据
    public static SqlFun Add(List<Object> Obj)
    {
        return Create().Insert(Obj);
    }
    //添加数据的同时验证唯一值,重复则不添加
    public static SqlFun Add_Unique(Object Obj)
    {
        return  Create().Insert_Ignore(Obj);
    }
    //添加数据的同时验证唯一值,重复则修改
    public static SqlFun Add_Upd(Object Obj, String U, String V)
    {
        return Create().Insert_Update(Obj,U,V);
    }




    /*删除数据的方法*/
    public  static SqlFun Del(Object Obj)
    {
        return Create().Delete(Obj);
    }
    public static SqlFun Truncate(String TableName)
    {
        return Create().Truncate(TableName);
    }



    /*修改数据的方法*/
    public static SqlFun Upd(Object Obj)
    {
       return Create().Update(Obj);
    }
    //单条件的修改数据
    public static SqlFun Upd(String Tb, String Fd, String Fv, String We, String Wv)
    {
        return Create().Update(Tb,Fd,Fv,We,Wv);
    }
    public static SqlFun Upd_Sel(String Table, String Field, String Trem, String Value, String Sign, int number)
    {
        return Create().Upd_Sel(Table,Field,Trem,Value,Sign,number);
    }




    /*查找的语句*/
    public static SqlFun SelR(Object Obj)
    {
        return Create().Select(Obj,"原表");
    }
    public static SqlFun SelV(Object Obj)
    {
        return Create().Select(Obj,"视图");
    }
    //说明:自定义的查询方法  F-字段 T-表名
    public static SqlFun ShareSel(String Fd, String Tb)
    {
        return  Create().Select(Fd,Tb);
    }

    public static SqlFun LastInsertId()
    {
        return  Create().Select(Virtue.LAST_INSERT_ID);
    }
    public static SqlFun Identity()
    {
        return  Create().Select(Virtue.IDENTITY);
    }

    //聚和函数的使用
    public static SqlFun Count(String Tb)
    {
        return  Create().Count(Tb);
    }
    public static SqlFun Count(String Col, String Tb)
    {
        return  Create().Count(Col,Tb);
    }
    public static SqlFun Count1(String Tb)
    {
        return  Create().Count1(Tb);
    }
    public static SqlFun Count1(String Col, String Tb)
    {
        return  Create().Count1(Col,Tb);
    }
    public static SqlFun CountDistinct(String Col, String Tb)
    {
        return  Create().CountDistinct(Col,Tb);
    }

}