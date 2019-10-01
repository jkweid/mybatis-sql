package com.github.jkweid;

import java.util.List;

public class SqlCre
{
    private static SqlWay Create()
    {
        return new SqlWay();
    }

    /*添加数据的方法*/
    public static SqlWay Add(Object Obj)
    {
        return Create().Insert(Obj);
    }
    public static SqlWay Add(List<Object> Obj)
    {
        return Create().Insert(Obj);
    }
    public static SqlWay AddVerify(Object Obj)
    {
        return  Create().Insert_Ignore(Obj);
    }
    public static SqlWay Add(Object Obj, Eways Eways)
    {
        return Create().Insert_Update(Obj,Eways);
    }




    /*删除数据的方法*/
    public  static SqlWay Delete(Object Obj)
    {
        return Create().Delete(Obj);
    }
    public static SqlWay Truncate(String TableName)
    {
        return Create().Truncate(TableName);
    }



    /*查找的语句*/
    public static SqlWay SelR(Object Obj) {
        return Create().Select(Obj,"原表");
    }

    public static SqlWay SelR(String[] Str) {
        return  Create().Select(Str[0],Str[1]);
    }

    public static SqlWay Last_Insert_Id(){ return  Create().Select(Esing.LAST_INSERT_ID);}

    public static SqlWay Identity() { return  Create().Select(Esing.IDENTITY);}

}