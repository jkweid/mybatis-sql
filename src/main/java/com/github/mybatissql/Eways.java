package com.github.mybatissql;

public enum Eways
{
    EQ,//等于
    NE,//不等于
    GT,//大于
    GE,//大于等于
    LT,//小于
    LE,//小于等于
    UP,//修改唯一列
    IN,//in
    NotIN,// not in
    EX,//exists
    NotEx,//not exists
    LF, //%左边
    RT, //右边%
    BH, //%两边%
    DSC,//升序
    ASC;//降序


    //匹配符号
    protected static String Sing(Eways Eways)
    {
        switch (Eways)
        {
            case EQ:return "=";
            case NE:return "<>";
            case GT:return ">";
            case GE:return ">=";
            case LT:return "<";
            case UP:return "=";
            case IN:return "IN";
            case NotIN: return "NOT IN";
            case EX:return "EXISTS";
            case NotEx:return "NOT EXISTS";
            case DSC:return " DESC ";
            case ASC:return " ASC ";
            default:
                return "<=";
        }
    }

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
    public Eways Set(Object Obj, String Pram)
    {
        this.Obj=Obj;
        return this;
    }
}
