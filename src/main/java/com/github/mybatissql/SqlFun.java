package com.github.mybatissql;


import java.util.List;

import static com.github.mybatissql.SqlUtil.Lead;
import static com.github.mybatissql.SqlUtil.Mark;

public class SqlFun {


    private String Add = "";//添加的语句
    private String Delete = "";//删除的语句
    private String Update = "";//修改语句
    private String Select = "";//查询语句
    private String Where = "";//Where字句
    private String GroupBy = "";//分组
    private String OrderBy = "";//降序
    private String Limit = "";//限制


    protected String getAdd() {
        return this.Add;
    }

    protected String getDelete() {
        return this.Delete;
    }

    protected String getUpdate(){ return this.Update; }

    protected String getSelect() {
        return this.Select;
    }

    protected String getWhere() {
        return this.Where;
    }

    protected String getGroupBy() {
        return this.GroupBy;
    }

    protected String getOrderBy() {
        return this.OrderBy;
    }

    protected String getLimit() {
        return this.Limit;
    }


    /*===============================================添加语句的方法===================================================*/
    protected String Insert(Object Obj, String Pame,boolean bn)
    {
        StringBuffer Insert=new StringBuffer(5);
        Insert.append("INSERT "+Pame+" INTO "+Mark(SqlUtil.Change(Obj))+ SqlUtil.Columns(Obj));
        if(bn)
        {
            Insert.append(" VALUES "+ SqlUtil.Values(Obj));
        }
        return Insert.toString();
    }

    //添加单条数据
    protected SqlFun Insert(Object Obj) {
        this.Add = Insert(Obj, "",true);
        return this;
    }

    //添加单条数据时验证唯一值
    protected SqlFun Insert_Ignore(Object Obj) {
        this.Add = Insert(Obj, Virtue.Ignore,true);
        return this;
    }

    //添加数据时如果有重复值,将进行修改
    protected SqlFun Insert_Update(Object Obj, String U, String V)
    {

        this.Add = Insert(Obj, "",true)+ Virtue.KEY_UPDATE+ SqlUtil.Where(Eways.UP.Set(U,V));
        return this;
    }

    //添加多条数据
    protected SqlFun Insert(List<Object> Obj) {
        this.Add = Insert(Obj.get(0), "",false)+ SqlUtil.Values(Obj);
        return this;
    }
    /*================================================================================================================*/





    /*===============================================删除语句的方法===================================================*/
    protected SqlFun Delete(Object Obj) {
        this.Delete = "DELETE FROM " + Mark(SqlUtil.Change(Obj));
        return this;
    }

    protected SqlFun Truncate(String Table) {
        this.Delete = "TRUNCATE TABLE " + Mark(Table);
        return this;
    }
    /*================================================================================================================*/



    /*================================================修改语句的方法==================================================*/
    protected SqlFun Update(Object Obj)
    {
        this.Update="UPDATE "+ SqlUtil.Table(Obj,"原表")+ SqlUtil.Set(Obj);
        return this;
    }
    protected SqlFun Update(String Tb, String Fd, String Ve, String We, String Wv)
    {
        this.Update="UPDATE "+Mark(Tb)+" SET "+Mark(Fd)+"="+Lead(Ve);
        this.Update=this.Update+" WHERE "+Mark(We)+"="+Lead(Wv)+" LIMIT 1";
        return this;
    }
    /**
     * 累加或累减的 Fd-修改与查找的字段名 We-条件 Wv-条件的值 Sign-符号(+/-) number-数值
     * UPDATE `codityType` SET `number`=(SELECT `number` FROM (SELECT `number` FROM `codityType`) AS X WHERE `id`='1')+1 WHERE `id`='1'
     */
    public SqlFun Upd_Sel(String Tb, String Fd, String We, String Wv, String Sign, int Nber)
    {
        String Sel=SelUtil(Fd);

        String Where= Virtue.Term+Mark(We)+"="+Lead(Wv);

        StringBuffer Sql=new StringBuffer("UPDATE "+Mark(Tb)+" SET "+Lead(Fd)+"=");

        Sql.append("("+Sel+"("+Sel+Mark(Tb)+") AS X "+Where+")"+Sign+Nber+" "+Where);

        this.Update=Sql.toString();

        return this;
    }
    /*================================================================================================================*/




    /*==============================================查询语句的方法====================================================*/
    private String SelUtil(String Pame) {
        return "SELECT " +Pame+ " FROM ";
    }

    protected SqlFun Select(Object Obj, String Type) {
        this.Select = SelUtil(SqlUtil.SelField(Obj))+ SqlUtil.Table(Obj, Type);
        return this;
    }

    protected SqlFun Select(String Filed, String Table) {
        this.Select = SelUtil(SqlUtil.SelField(Filed))+Mark(Table);
        return this;
    }

    protected SqlFun Select(String Str) {
        this.Select = "SELECT "+Str;
        return this;
    }
    /*================================================================================================================*/


    /*==============================================聚和函数的使用====================================================*/
    protected SqlFun Count(String Tb)
    {
        this.Select = SelUtil("COUNT(*)")+Mark(Tb);
        return this;
    }
    protected SqlFun Count(String Col, String Tb)
    {
        this.Select = SelUtil(Mark(Col)+","+"COUNT(*)")+Mark(Tb);
        return this;
    }
    protected SqlFun Count1(String Tb)
    {
        this.Select = SelUtil("COUNT(1)")+Mark(Tb);
        return this;
    }
    protected SqlFun Count1(String Col, String Tb)
    {
        this.Select = SelUtil(Mark(Col)+","+"COUNT(1)")+Mark(Tb);
        return this;
    }
    public SqlFun CountDistinct(String Col, String Tb)
    {
        this.Select = SelUtil("COUNT(DISTINCT "+ SqlUtil.Mark(Col)+")")+Mark(Tb);
        return this;
    }
    /*================================================================================================================*/



    /*=================================================Where条件语句==================================================*/
    private String Verify(String Str)
    {
        return "".equals(Str) ? "" : Virtue.Term+Str;
    }

    //单条件
    public SqlFun Where(Eways E)
    {
        this.Where=Verify(SqlUtil.Term(E));
        return this;
    }
    //自定义条件
    public SqlFun Where(String Term)
    {
        this.Where=Verify(Term);
        return this;
    }


    //范围条件
    public SqlFun Area(String[][] Area)
    {
        this.Where=Verify(SqlUtil.Area(Area));
        return this;
    }


    //WHERE `字段`='值' AND `字段`='值'
    public SqlFun AndS(Eways Eways)
    {
        this.Where=Verify(SqlUtil.WhereOrAnd(Eways,"AND"));
        return this;
    }
    //WHERE `字段`='值' OR `字段`='值'
    public SqlFun OrS(Eways Eways)
    {
        this.Where=Verify(SqlUtil.WhereOrAnd(Eways,"OR"));
        return this;
    }



    // `字段` IN('','') 或者 `字段` NOT IN('','')
    public SqlFun In(String... Str)
    {
        this.Where= SqlUtil.In(Str[0],Str[1],true, Virtue.In);
        return this;
    }
    public SqlFun InSel(String... Str)
    {
        this.Where= SqlUtil.In(Str[0],Str[1],false, Virtue.In);
        return this;
    }
    public SqlFun NotIn(String F, String V)
    {
        this.Where= SqlUtil.In(F,V,true, Virtue.NotIn);
        return this;
    }
    public SqlFun NotInSel(String F, String Sel)
    {
        this.Where= SqlUtil.In(F,Sel,false, Virtue.NotIn);
        return this;
    }


    // WHERE EXISTS() 或者 WHERE NOT EXISTS()
    public SqlFun Exists(String Sel)
    {
        this.Where=Verify(Eways.Sing(Eways.EX)+"("+Sel+")");
        return this;
    }
    public SqlFun NotExists(String Sel)
    {
        this.Where=Verify(Eways.Sing(Eways.NotEx)+"("+Sel+")");
        return this;
    }


    // WHERE `字段` LIKE '%值' | '值%' | '%值%
    protected  String Like(Eways E, String Pame)
    {
        String[] Arry= SqlUtil.Cutting(Pame);
        return SqlUtil.Like(E,Arry[0],Arry[1]);
    }
    public SqlFun LikeLF(String Pame)
    {
        this.Where=Verify(Like(Eways.LF,Pame));
        return this;
    }
    public SqlFun LikeRT(String Pame)
    {
        this.Where=Verify(Like(Eways.RT,Pame));
        return this;
    }
    public SqlFun LikeBH(String Pame)
    {
        this.Where=Verify(Like(Eways.BH,Pame));
        return this;
    }
    // WHERE `字段` LIKE '%值%' OR  `字段` LIKE '%值%'
    public SqlFun LikeS(Eways E, String[][] Like)
    {
        this.Where=Verify(SqlUtil.Like(E,Like));
        return this;
    }



    //条件的拼接
    public SqlFun And(Eways E)
    {
        this.Where=this.Where+" AND "+ SqlUtil.OrAnd(E);
        return this;
    }
    public SqlFun Or(Eways E)
    {
        this.Where=this.Where+" OR "+ SqlUtil.OrAnd(E);
        return this;
    }


    //分组
    public SqlFun Group(String Field)
    {
        this.GroupBy= SqlUtil.GroupBy_OrderBy(Virtue.GROUP_BY,Field,"");
        return this;
    }


    // 降序或升序
    public SqlFun Desc(String Field)
    {
        this.OrderBy= SqlUtil.GroupBy_OrderBy(Virtue.ORDER_BY,Field, Virtue.DESC);
        return this;
    }
    public SqlFun Asc(String Field)
    {
        this.OrderBy= SqlUtil.GroupBy_OrderBy(Virtue.ORDER_BY,Field, Virtue.ASC);
        return this;
    }




    /*==================================================限制条件语句==================================================*/
    public SqlFun Limit(int Number)
    {
        this.Limit=" LIMIT "+Number;
        return this;
    }
    public SqlFun Limit(int Page, int Size)
    {
        this.Limit=" LIMIT "+ SqlUtil.Start(Page,Size)+","+Size;
        return this;
    }




}
