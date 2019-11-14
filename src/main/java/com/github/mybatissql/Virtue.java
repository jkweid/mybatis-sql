package com.github.mybatissql;

public class Virtue
{
    protected static final  String Ignore=" IGNORE ";//添加数据的同时验证唯一值
    protected static final  String KEY_UPDATE=" ON DUPLICATE KEY UPDATE ";//添加数据的时候如果唯一值有重复将进行修改
    protected static final  String LAST_INSERT_ID=" LAST_INSERT_ID() AS id ";//获取自增id的值1
    protected static final  String IDENTITY=" @@IDENTITY AS id ";//获取自增id的值2

    protected static final String Term=" WHERE ";
    protected static final String In=" IN ";
    protected static final String NotIn=" NOT IN ";
    protected static final String GROUP_BY=" GROUP BY ";
    protected static final String ORDER_BY=" ORDER BY ";
    protected static final String DESC=" DESC ";
    protected static final String ASC=" ASC ";
}
