package com.github.jkweid;


import java.util.List;
import static com.github.jkweid.SqlUtil.Mark;

public class SqlWay {


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
    protected String Insert(Object Obj, String Pame) {
        return "INSERT " + Pame + " INTO " + Mark(SqlUtil.Change(Obj)) + "(" + SqlUtil.Columns(Obj, "insert") + ")";
    }

    //添加单条数据
    protected SqlWay Insert(Object Obj) {
        this.Add = Insert(Obj, "") + " VALUES" + SqlUtil.Values(Obj);
        return this;
    }

    //添加单条数据时验证唯一值
    protected SqlWay Insert_Ignore(Object Obj) {
        this.Add = Insert(Obj,Esing.IGNORE) + " VALUES" + SqlUtil.Values(Obj);
        return this;
    }

    //添加数据时如果有重复值,将进行修改
    protected SqlWay Insert_Update(Object Obj, Eways Eways) {
        this.Add = Insert(Obj, "") + " VALUES" + SqlUtil.Values(Obj)+Esing.KEY_UPDATE+ SqlUtil.Where(Eways);
        return this;
    }

    //添加多条数据
    protected SqlWay Insert(List<Object> Obj) {
        this.Add = Insert(Obj.get(0), "") + " VALUES" + SqlUtil.Values(Obj);
        return this;
    }
    /*================================================================================================================*/


    /*===============================================删除语句的方法===================================================*/
    protected SqlWay Delete(Object Obj) {
        this.Delete = "DELETE FROM " + Mark(SqlUtil.Change(Obj));
        return this;
    }

    protected SqlWay Truncate(String Table) {
        this.Delete = "TRUNCATE TABLE " + Mark(Table);
        return this;
    }
    /*================================================================================================================*/


    /*==============================================查询语句的方法====================================================*/
    private String SelUtil(String Filed) {
        return "SELECT " + Filed + " FROM ";
    }

    protected SqlWay Select(Object Obj, String Type) {
        this.Select = SelUtil(SqlUtil.Columns(Obj, "")) + SqlUtil.Table(Obj, Type);
        return this;
    }

    protected SqlWay Select(String Filed, String Table) {
        this.Select = SelUtil(SqlUtil.SelField(Filed)) + Mark(Table);
        return this;
    }

    protected SqlWay Select(String Str) {
        this.Select = "SELECT "+Str;
        return this;
    }
    /*================================================================================================================*/


    /*=================================================Where条件语句==================================================*/
    private String WhereVerify(String Str)
    {
        if("".equals(Str))
        {
            return "";
        }
        else
        {
            return SqlUtil.Term+Str;
        }
    }
    //自定义条件
    public SqlWay Where(String Term)
    {
        this.Where=WhereVerify(Term);
        return this;
    }

    //单条件
    public SqlWay Where(Eways Eways) {
        this.Where = SqlUtil.Term + SqlUtil.Where(Eways);
        return this;
    }

    //范围条件
    public SqlWay WhereArea(String... Str)
    {
        this.Where=SqlUtil.Term+SqlUtil.Area(Str[0],Str[1],Str[2]);
        return this;
    }

    //And条件
    public SqlWay WhereAnd(Eways Eways)
    {
        this.Where=WhereVerify(SqlUtil.WhereOrAnd(Eways,SqlUtil.And));
        return this;
    }
    //Or条件
    public SqlWay WhereOr(Eways Eways)
    {
        this.Where=WhereVerify(SqlUtil.WhereOrAnd(Eways,SqlUtil.Or));
        return this;
    }


    //字段`='值' And  字段`!='值'
    public SqlWay WhereAnd(List<Eways> Eways) {
        this.Where = SqlUtil.Term + SqlUtil.WhereOrAnd(Eways, SqlUtil.And);
        return this;
    }

    //字段`='值' OR  字段`!='值'
    public SqlWay WhereOr(List<Eways> Eways) {
        this.Where = SqlUtil.Term + SqlUtil.WhereOrAnd(Eways, SqlUtil.Or);
        return this;
    }

    //可变化的条件
    //WHERE `字段`='值' OR `字段`='值' OR `字段` LIKE '%值%' OR `ip`>='1' AND `ip`<='1' AND `status`='1';
    public SqlWay WhereVary(Object Obj, String Like, String[] Area, String Trem)
    {
        return this;
    }

    // `字段` IN('','') 或者 `字段` NOT IN('','')
    public SqlWay In(Eways Eways)
    {
        this.Where=SqlUtil.In(Eways,false);
        return this;
    }
    public SqlWay InSel(Eways Eways)
    {
        this.Where=SqlUtil.In(Eways,true);
        return this;
    }


    // WHERE EXISTS() 或者 WHERE NOT EXISTS()
    public SqlWay Exists(String Sel)
    {
        this.Where=SqlUtil.Term+SqlUtil.Sing(Eways.EX)+"("+Sel+")";
        return this;
    }
    public SqlWay NotExists(String Sel)
    {
        this.Where=SqlUtil.Term+SqlUtil.Sing(Eways.NotEx)+"("+Sel+")";
        return this;
    }


    // WHERE `字段` LIKE '%值' | '值%' | '%值%'
    public SqlWay Like(Eways E)
    {
        this.Where=SqlUtil.Term+SqlUtil.Like(E,E.getPram()[0],E.getPram()[1]);
        return this;
    }
    // WHERE `字段` LIKE '%值%' OR  `字段` LIKE '%值%'
    public SqlWay Like(Eways E, String[][] Like)
    {
        this.Where=SqlUtil.Term+SqlUtil.Like(E,Like);
        return this;
    }


    //条件的拼接
    public SqlWay And(Eways E)
    {
        this.Where=this.Where+" AND "+SqlUtil.OrAnd(E);
        return this;
    }
    public SqlWay Or(Eways E)
    {
        this.Where=this.Where+" OR "+SqlUtil.OrAnd(E);
        return this;
    }


    //分组
    public SqlWay GroupBy(String Field)
    {
        this.GroupBy=" GROUP BY "+Mark(Field);
        return this;
    }

    // 降序或升序
    public SqlWay OrderBy(Eways Eways)
    {
        this.OrderBy=" ORDER BY "+Mark(Eways.getField())+" "+SqlUtil.Sing(Eways);
        return this;
    }



    /*==================================================限制条件语句==================================================*/
    public SqlWay Limit(int Number)
    {
        this.Limit=" LIMIT "+Number;
        return this;
    }
    public SqlWay Limit(int Page, int Size)
    {
        this.Limit=" LIMIT "+SqlUtil.Start(Page,Size)+","+Size;
        return this;
    }




}
