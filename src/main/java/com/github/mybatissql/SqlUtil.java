package com.github.mybatissql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SqlUtil
{
    protected static final String And="AND";
    protected static final String Or="OR";

    //将实体类名转换成对应的数据库表名(表名必须是骆驼峰的形式)
    protected static String Change(Object Obj)
    {
        String Str=Obj.getClass().getSimpleName();
        return Str.replace(Str.charAt(0), Character.toLowerCase(Str.charAt(0)));
    }

    protected static String Mark(String Filed)
    {
        return "`"+Filed+"`";
    }
    protected static String Lead(String value)
    {
        return "'"+value+"'"+" ";
    }

    //将实体类名转换为数据库的表名
    protected static String Table(Object Obj,String Type)
    {
        String Table= SqlUtil.Change(Obj);
        if("原表".equals(Type))
        {
            return Mark(Table);
        }
        else
        {
            if(Table.indexOf("V")>0)
            {
                return Mark(Table);
            }
            else
            {
                return Mark( Table+"V");
            }
        }
    }



    //去除多余字符串的方法
    protected static String Remove(StringBuffer Str,String Pam1,String Pam2)
    {
        if(Str.length()!=0)
        {
            Str.delete(Str.lastIndexOf(Pam1),Str.lastIndexOf(Pam2) + 1);
            return Str.toString();
        }
        return "";
    }

    //根据逗号进行切割
    protected static String[] Cutting(String Str)
    {
        String [] Arr=Str.split(",");
        return Arr;
    }


    //反射获取多个字段
    protected static Field[] Fields(Object Obj)
    {
        Field[] Fields = Obj.getClass().getDeclaredFields();
        return Fields;
    }
    /*根据属性名获取属性值*/
    public static String Field(Object Obj,String Name)
    {
        try
        {
            Field field =Obj.getClass().getDeclaredField(Name);
            field.setAccessible(true);
            return  field.get(Obj)+"";

        }catch (Exception e)
        {
            System.err.println("报错了:"+e.getMessage());
            return  "";
        }
    }
    /*================================================================================================================*/




    /*==============================================添加语句的辅助方法================================================*/
    protected static String Columns(Object Obj)
    {
        Field[] Fields=Obj.getClass().getDeclaredFields();
        StringBuffer columns=new StringBuffer(Fields.length);
        columns.append("(");
        for(Field Field:Fields)
        {
            Field.setAccessible(true);
            if("id".equals(Field.getName()))
            {
                columns.append(Mark(Field.getName())+",");
            }
        }
        columns.append(")");
        return  SqlUtil.Remove(columns,",",",");
    }
    protected static  String Values(Object Obj)
    {
        try
        {
            Field[] Fields= SqlUtil.Fields(Obj);
            StringBuffer buffer=new StringBuffer(Fields.length);
            for(Field Field :Fields)
            {
                Field.setAccessible(true);
                buffer.append(Lead(Field.get(Obj)+"")+",");
            }
            String  Values= SqlUtil.Remove(buffer,",",",");
            return  "("+Values+")";

        }catch (Exception e)
        {
            System.err.println("报错了:"+e.getMessage());
            return  "";
        }
    }
    protected static String Values(List<Object> List)
    {
        StringBuffer builder=new StringBuffer(List.size());
        for(Object Obj:List)
        {
            builder.append(Values(Obj)+",");
        }
        return SqlUtil.Remove(builder,",",",");
    }




    /*==================================================修改语句的辅助方法============================================*/
    public static String Set(Object Obj)
    {
        try
        {
            Field[] Fields=Obj.getClass().getDeclaredFields();
            StringBuffer buffer=new StringBuffer(Fields.length);
            buffer.append(" SET ");
            for(Field Field:Fields)
            {
                Field.setAccessible(true);
                //过滤掉为id的字段
                if("id".equals(Field.getName())) {
                    continue;
                }
                if(Field.get(Obj) != null && !"".equals(Field.get(Obj)))
                {
                    buffer.append(Mark(Field.getName())+"="+Lead(Field.get(Obj)+""));
                }
            }
            return  buffer.toString();

        }catch (Exception e)
        {
            System.err.println("报错了:"+e.getMessage());
            return  "";
        }
    }
    /*================================================================================================================*/





    /*==============================================查询指定的表字段的方法============================================*/
    protected static String SelField(Object Obj)
    {
        Field[] Fields=Obj.getClass().getDeclaredFields();
        StringBuffer columns=new StringBuffer(Fields.length);
        for(Field Field:Fields)
        {
            Field.setAccessible(true);
            //过滤有该注解的字段
            if (Field.isAnnotationPresent(NLoad.class))
            {
                continue;
            }
            columns.append(Mark(Field.getName())+",");
        }
        return  SqlUtil.Remove(columns,",",",");
    }
    protected static String SelField(String Str)
    {
        String[] Fields=Str.split(",");
        StringBuffer Sfr=new StringBuffer(Fields.length);
        for(String Field:Fields)
        {
            Sfr.append(Mark(Field)+",");
        }
        return Remove(Sfr,",",",");
    }
    /*================================================================================================================*/




    /*================================================条件辅助方法====================================================*/
    protected static String Term(Eways Eways)
    {
        if(Eways.getPram()[0]!=null && !"".equals(Eways.getPram()[0])
         && Eways.getPram()[1]!=null && "".equals(Eways.getPram()[1]))
        {
            return  Mark(Eways.getPram()[0])+Eways.Sing(Eways)+Lead(Eways.getPram()[1]);
        }
        else
        {
            return "";
        }
    }
    //双范围条件组装
    public static String Area(String Filed,String Area1,String Area2)
    {
        if(Area1!=null && !"".equals(Area1) && Area2==null || "".equals(Area2))
        {
            if(Area1!=null && !"".equals(Area1))
            {
                return Mark(Filed)+">="+Lead(Area1);
            }
        }
        if(Area1==null || "".equals(Area1) && Area2!=null && !"".equals(Area2))
        {
            if(Area2!=null && !"".equals(Area2))
            {
                return Mark(Filed)+"<="+Lead(Area2);
            }
        }
        if (Area1 != null && !"".equals(Area1) && Area2 != null && !"".equals(Area2))
        {
            return Mark(Filed)+">="+Lead(Area1)+"AND "+Mark(Filed)+"<="+Lead(Area1);
        }
        return "";
    }
    //多个范围条件的查找
    protected static String Area(String[][] Area)
    {
        StringBuffer buffer=new StringBuffer(0);
        String Result=null;
        for(String[] Str:Area)
        {
            Result=Area(Str[0],Str[1],Str[2]);
            if("".equals(Result))
            {
                continue;
            }
            else
            {
                buffer.append(Result+" AND ");
            }
        }
        return Remove(buffer,"A","D");
    }

   //逻辑运算与比较运算符的结合条件语句
    protected static String WhereOrAnd(Eways Eways, String Type)
    {
        try
        {
            Object Obj=Eways.getObject();
            Field[] Fields= SqlUtil.Fields(Obj);
            StringBuffer buffer=new StringBuffer(0);
            for(Field field : Fields)
            {
                field.setAccessible(true);
                //过滤有该注解的字段
                if (field.isAnnotationPresent(NLoad.class))
                {
                    continue;
                }
                if(field.get(Obj)!=null && "".equals(field.get(Obj)))
                {
                    buffer.append(Mark(field.getName())+Eways.Sing(Eways)+Lead((String)field.get(Obj))+Type+" ");
                }
            }
            return OrAndVerify(buffer,Type);

        }catch (Exception e)
        {
            System.err.println("报错了:"+e.getMessage());
            return "";
        }
    }
    protected  static String WhereOrAnd(List<Eways> Eways, String Type)
    {
        int i=0;
        int len=Eways.size();
        StringBuffer buffer=new StringBuffer(len);
        for(;i<len;i++)
        {
            buffer.append(Where(Eways.get(i))+Type+" ");
        }
        return OrAndVerify(buffer,Type);
    }
    protected static String OrAndVerify(StringBuffer buffer,String Type)
    {
        if("OR".equals(Type))
        {
            return Remove(buffer,"O","R");
        }
        else
        {
            return Remove(buffer,"A","D");
        }
    }

    //可变化的条件
    //WHERE `字段`='值' OR `字段`='值' OR `字段` LIKE '%值%' OR `ip`>='1' AND `ip`<='1' AND `status`='1';
    private static String WhereVary(Object Obj,String Like,String[][] Area,String Trem,boolean bn)
    {
        List<String> list=new ArrayList();
        //多字段的OR逻辑条件
        if(bn)
        {
            list.add(WhereOrAnd(Eways.EQ,"OR"));
        }
        //Like条件
        if(Like!=null && !"".equals(Like))
        {
            list.add(Like(Eways.BH,Like,Field(Obj,Like)));
        }
        //范围条件
        if(Area!=null)
        {
            list.add(Area(Area));
        }
        //最后的条件
        if(Trem!=null && "".equals(Trem))
        {
            list.add(Term(Eways.EQ.Set(Trem,Field(Obj,Trem))));
        }

        StringBuffer where = new StringBuffer(list.size());
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i)==null || "".equals(list.get(i)))
            {
                continue;
            }
            else
            {
                where.append(list.get(i)+" AND ");
            }
        }

        if (where.length()!=0)
        {
            System.out.println(where.length()+">>>>>>>>>>>>>>>>");
            where.delete(where.lastIndexOf("A"), where.lastIndexOf("D") + 1);
            return where.toString();
        }
        else
        {
            return "";
        }
    }
    //可变化的条件
    public static String VarietyOr(Object Obj, String Like, String[][] Area, String Trem)
    {
        return SqlUtil.WhereVary(Obj,Like,Area,Trem,true);
    }
    public static String VarietyNotOr(Object Obj,String Like, String[][] Area, String Trem)
    {
        return SqlUtil.WhereVary(Obj,Like,Area,Trem,false);
    }






    protected static String In(String Field,String Value,boolean bn,String Type)
    {
        StringBuffer buffer=new StringBuffer(0);
        buffer.append(Virtue.Term+Mark(Field)+Type+"(");
        if(bn)
        {
            String[] Arry= SqlUtil.Cutting(Value);
            for (String Str:Arry)
            {
                buffer.append(Lead(Str) + ",");
            }
            Remove(buffer, ",", ",");
        }
        else
        {
            buffer.append(Value);
        }
        buffer.append(")");
        return buffer.toString();
    }


    //模糊查询
    protected static String Like(Eways E, String Filed, String Value)
    {
        if(Filed!=null && Value!=null || !"".equals(Filed) && !"".equals(Value))
        {
            StringBuffer Like = new StringBuffer(" LIKE ");
            //左边/右边/两边(both)
            switch(E)
            {
                case LF:
                    return Mark(Filed)+Like.append(Lead("%"+Value));
                case RT:
                    return Mark(Filed)+Like.append(Lead(Value+"%"));
                default:
                    return Mark(Filed)+Like.append(Lead("%"+Value+"%"));
            }
        }
        else
        {
            return "";
        }
    }
    //多个模糊条件
    protected static String Like(Eways E, String[][] Like)
    {
        StringBuffer buffer=new StringBuffer(0);
        String Result=null;
        for(String[] Str:Like)
        {
            Result=Like(E,Str[0],Str[1]);
            if("".equals(Result))
            {
                continue;
            }
            else
            {
                buffer.append(Result+" OR ");
            }
        }
        return Remove(buffer,"O","R");
    }


    //拼接条件
    protected static String OrAnd(Eways E)
    {
        switch (E)
        {
            case LF:
            case RT:
            case BH:
                return Like(E,E.getPram()[0],E.getPram()[1]);
            default:
                return Where(E);
        }
    }
    /*================================================================================================================*/

    protected static String  GroupBy_OrderBy(String Virture,String Field,String Type)
    {
        String[] Arry= SqlUtil.Cutting(Field);
        StringBuffer buffer=new StringBuffer(0);
        buffer.append(Virture);
        for(String Fields:Arry)
        {
            buffer.append(Mark(Fields)+Type+",");
        }
        Remove(buffer,",",",");
        return buffer.toString();
    }

    /*================================================================================================================*/
    public static int Start(int Page, int Size)
    {
        int Start=0;
        if (Page <= 0) {
            Start = 0;
        } else {
            Start = (Page - 1) * Size;
        }
        return Start;
    }
    /*================================================================================================================*/


    //SQL语句的输出
    public static String Sql(Object Obj)
    {
        SqlFun Select = (SqlFun) Obj;
        StringBuffer Sql = new StringBuffer(5);
        Sql.append(Select.getSelect());
        Sql.append(Select.getWhere());
        Sql.append(Select.getGroupBy());
        Sql.append(Select.getOrderBy());
        Sql.append(Select.getLimit());
        return Sql.toString();
    }

    //可变条件的解析
    public static String Where(Object Obj)
    {
        SqlFun sqlFun = (SqlFun) Obj;
        return sqlFun.getWhere();
    }

}
