package com.github.jkweid;

public enum Eways
{
    EQ,//等于
    NE,//不等于
    GT,//大于
    GE,//大于等于
    LT,//小于
    LE,//小于等于
    UP,//修改唯一列
    TB,//字段与表名
    IN,//in
    NotIN,// not in
    EX,//exists
    NotEx,//not exists
    LF, //%左边
    RT, //右边%
    BH, //%两边%
    DSC,//升序
    ASC;//降序
//    YES,//要带上WHERE
//    NOT,//不要带上WHERE
//    Area;//范围

    private String[] Pram;
    private String Field;
    private Object Obj;

    protected String[] getPram() {
        return this.Pram;
    }
    protected String getField(){ return this.Field; }
    protected Object getObject(){ return this.Obj;}

    public Eways Set(String Pram1)
    {
        this.Field=Pram1;
        return this;
    }

    public Eways Set(Object Obj)
    {
        this.Obj=Obj;
        return this;
    }
    public Eways Set(String Pram1, String Pram2)
    {
        this.Pram=new String[]{Pram1,Pram2};
        return this;
    }
}
