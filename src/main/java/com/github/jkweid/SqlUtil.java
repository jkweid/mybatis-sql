package com.github.jkweid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SqlUtil
{
    protected static final String Term=" WHERE ";
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
        String Table=SqlUtil.Change(Obj);
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


    //获取字段的属性的共用方法
    protected static String Columns(Object Obj,String Type)
    {
        Field[] Fields=SqlUtil.Fields(Obj);
        StringBuffer columns=new StringBuffer(Fields.length);
        for(Field Field:Fields)
        {
            Field.setAccessible(true);
            if("insert".equals(Type))
            {
                if("id".equals(Field.getName()))
                {
                    columns.append(Mark(Field.getName())+",");
                }
            }
            else
            {
                //过滤有该注解的字段
                if (Field.isAnnotationPresent(NotLoad.class))
                {
                    continue;
                }
                columns.append(Mark(Field.getName())+",");
            }
        }
        return  SqlUtil.Remove(columns,",",",");
    }

    /*================================================================================================================*/




    /*==============================================添加语句的辅助方法================================================*/
    protected static  String Values(Object Obj)
    {
        try
        {
            Field[] Fields=SqlUtil.Fields(Obj);
            StringBuffer buffer=new StringBuffer(Fields.length);
            for(Field Field :Fields)
            {
                Field.setAccessible(true);
                buffer.append(Lead(Field.get(Obj)+"")+",");
            }
            String  Values=SqlUtil.Remove(buffer,",",",");
            return  "("+Values+")";

        }catch (Exception e)
        {
            System.err.println("报错了:"+e.getMessage());
            return  "";
        }
    }
    protected static String Values(List<Object> Obj)
    {
        StringBuffer builder=new StringBuffer(Obj.size());
        int i=0;
        int len=Obj.size();
        for(;i<len;i++)
        {
            builder.append(Values(Obj.get(i))+",");
        }
        return SqlUtil.Remove(builder,",",",");
    }




    /*==================================================修改语句的辅助方法============================================*/
    public static String Set(Object Obj)
    {
        try
        {
            Field[] Fields=SqlUtil.Fields(Obj);
            StringBuffer buffer=new StringBuffer(Fields.length);
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
    protected static String Where(Eways Eways)
    {
        return  Mark(Eways.getPram()[0])+SqlUtil.Sing(Eways)+Lead(Eways.getPram()[1]);
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
        for(String[] Str:Area)
        {
            buffer.append(Area(Str[0],Str[1],Str[2])+" AND ");
        }
        return Remove(buffer,"A","D");
    }

   //逻辑运算与比较运算符的结合条件语句
    protected static String WhereOrAnd(Eways Eways,String Type)
    {
        try
        {
            Object Obj=Eways.getObject();
            Field[] Fields=SqlUtil.Fields(Obj);
            StringBuffer buffer=new StringBuffer(0);
            for(Field field : Fields)
            {
                field.setAccessible(true);
                //过滤有该注解的字段
                if (field.isAnnotationPresent(NotLoad.class))
                {
                    continue;
                }
                if(field.get(Obj)!=null && "".equals(field.get(Obj)))
                {
                    buffer.append(Mark(field.getName())+Sing(Eways)+Lead((String)field.get(Obj))+Type+" ");
                }
            }
            return OrAndVerify(buffer,Type);

        }catch (Exception e)
        {
            System.err.println("报错了:"+e.getMessage());
            return "";
        }
    }
    protected  static String WhereOrAnd(List<Eways> Eways,String Type)
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
    public static String WhereVary(Object Obj,String Like,String[][] Area,String Trem)
    {
        List<String> list=new ArrayList();
        StringBuffer buffer=new StringBuffer(5);
        //多字段的OR逻辑条件
        list.add(WhereOrAnd(Eways.EQ,"OR"));
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
            list.add(Where(Eways.EQ.Set(Trem,Field(Obj,Trem))));
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

        if (!where.toString().equals(""))
        {
            where.delete(where.lastIndexOf("A"), where.lastIndexOf("D") + 1);
            return where.toString();
        }
        else
        {
            return where.toString();
        }
    }

    protected static String In(Eways Eways,boolean bn)
    {
        String [] Arr=Eways.getPram()[1].split(",");
        StringBuffer buffer=new StringBuffer(0);
        buffer.append(" WHERE "+Mark(Eways.getPram()[0])+" "+SqlUtil.Sing(Eways)+"(");
        for(String Str:Arr)
        {
            //如果是查询语句
            if(bn)
            {
                buffer.append(Str);
            }
            else
            {
                buffer.append(Lead(Str)+",");
            }

        }
        if(!bn)
        {
            Remove(buffer,",",",");
        }
        buffer.append(")");
        return buffer.toString();
    }


    //模糊查询
    protected static String Like(Eways E,String Filed,String Value)
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
    //多个模糊条件
    protected static String Like(Eways E,String[][] Like)
    {
        StringBuffer buffer=new StringBuffer(0);
        for(String[] Str:Like)
        {
            buffer.append(Like(E,Str[0],Str[1])+" OR ");
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
            case DSC:return "DESC";
            case ASC:return "ASC";
            default:
                return "<=";
        }
    }

    /*================================================================================================================*/


    //SQL语句的输出
    public static String Sql(Object Obj)
    {
        SqlWay Select = (SqlWay) Obj;
        StringBuffer Sql = new StringBuffer(5);
        Sql.append(Select.getSelect());
        Sql.append(Select.getWhere());
        Sql.append(Select.getGroupBy());
        Sql.append(Select.getOrderBy());
        Sql.append(Select.getLimit());
        return Sql.toString();
    }
}
