package com.github.jkweid;

public class Esing
{

    protected static final  String IGNORE=" IGNORE ";//添加数据的同时验证唯一值
    protected static final  String KEY_UPDATE=" ON DUPLICATE KEY UPDATE ";//添加数据的时候如果唯一值有重复将进行修改
    protected static final  String LAST_INSERT_ID=" LAST_INSERT_ID() AS id ";//获取自增id的值1
    protected static final  String IDENTITY=" @@IDENTITY AS id ";//获取自增id的值2
}
