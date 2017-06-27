# lite orm 数据库

* [liteOrm数据库导入](#liteOrm数据库导入)
* [liteOrm实例化](#liteOrm实例化)
* [数据库操作](#数据库操作)
* [注解](#注解)
    * [基本注解](#基本注解)
    * [关系映射](#关系映射)
    * [约束相关](#约束相关)
* [CRUD操作](#crud操作)
* [级联操作](#级联操作)

## liteOrm数据库导入

项目中的版本是 lite-orm-1.9.1.jar，网络上的下载地址
[https://github.com/litesuits/android-lite-orm](https://github.com/litesuits/android-lite-orm "lite orm demo 下载地址")


## liteOrm实例化

一个数据库对应一个LiteOrm的实例，如果一个App只有一个数据库，那么LiteOrm应该是全局单例的
可以在application中写：
```
static LiteOrm liteOrm;
if (liteOrm == null) { 
        DataBaseConfig config = new DataBaseConfig(this, "liteorm.db");
        //"liteorm.db"是数据库名称，名称里包含路径符号"/"则将数据库建立到该路径下，可以使用sd卡路径。 不包含则在系统默认路径下创建DB文件。
        //例如 public static final String DB_NAME = SD_CARD + "/lite/orm/liteorm.db";     DataBaseConfig config = new DataBaseConfig(this, DB_NAME);
        config.dbVersion = 1; // set database version
        config.onUpdateListener = null; // set database update listener
        //独立操作，适用于没有级联关系的单表操作，
        liteOrm = LiteOrm.newSingleInstance(config);
        //级联操作,适用于多表级联操作
        // liteOrm=LiteOrm.newCascadeInstance(config); 
}
liteOrm.setDebugged(true); // open the log
```

## 数据库操作

通过一些项目整理了一个包含增删改查的方法的类。

```

public class DBOperation {
    public static final String DB_NAME = "lite_orm";
    public static LiteOrm liteOrm;

    /**
     * 创建数据库
     * @param mContext
     * @param liteName 数据库名称
     */
    public static void createLiteOrm(Context mContext, String liteName){
    	if(liteOrm == null){
    		liteOrm = LiteOrm.newSingleInstance(mContext, liteName);
            liteOrm.setDebugged(true);
    	}
        
    }

    public static LiteOrm getLiteOrm(){
        if(liteOrm == null){
            createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        }
        return liteOrm;
    }

    /**
     * 插入数据库
     * @param t 实体类
     * @param <T>
     */
    public static <T> void insert(T t){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        liteOrm.save(t);
    }

    /**
     * 插入多条数据
     * @param list 实体类的集合
     * @param <T>
     */
    public static <T> void insertAll(List<T> list){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        liteOrm.save(list);
    }

    
    /**
     * 查询所有数据
     * @param cla 查询的类型
     * @param <T>
     * @return
     */
    public static <T> List<T> queryAll(Class<T> cla){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        return liteOrm.query(cla);
    }

    /**
     * 根据条件查询数据
     * @param cla
     * @param field 数据库字段,单个字段
     * @param value 条件的值，单个值
     * @param <T>
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> queryByWhere(Class<T> cla,String field,String value){
        String[] values = new String[]{value};
        createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        return liteOrm.<T>query(new QueryBuilder(cla).where(field + "=?", values));
    }

    /**
     * 多条件查询数据
     * @param cla
     * @param field 多个字段要写上 = ？
     * @param value 多个字段对应的值
     * @param <T>
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> List<T> queryByWhere(Class<T> cla,String field,String[] value){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        return liteOrm.<T>query(new QueryBuilder(cla).where(field, value));
    }
    
    /**
     * 2个条件查询
     * @param cla
     * @param field1
     * @param value1
     * @param field2
     * @param value2
     * @return
     */
    public static <T> List<T> queryByWhereTwo(Class<T> cla,String field1,String[] value1,String field2,String[] value2){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
    	return liteOrm.<T>query(new QueryBuilder<T>(cla).where(field1, value1).whereAppendAnd().whereAppend(field2, value2));
    }

    /**
     * 分页查询,多个条件查询
     * @param cla 实体类的类型
     * @param field 查询的条件，需要写 = ？
     * @param value 多个查询的值
     * @param start 开始位置
     * @param length 长度最后的条数
     * @param <T>
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> List<T> queryByWhereLength(Class<T> cla,String field,String [] value,int start,int length){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        return liteOrm.<T>query(new QueryBuilder(cla).where(field, value).limit(start, length));
    }

    /**
     * 分页查询，单个条件查询
     * @param cla 实体类的类型
     * @param field 单个数据的字段名称
     * @param value 数据对应的值
     * @param start 开始的位置
     * @param length 查询的最后条数
     * @param <T>
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> List<T> queryByWhereLength(Class<T> cla, String field, String value, int start, int length) {
        String[] values = new String[]{value};
        createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        return liteOrm.<T>query(new QueryBuilder(cla).where(field + " = ?", values).limit(start, length));
    }

    /**
     * 按照条件查找并且排序
     * @param cla 实体类的类型
     * @param where 单个数据的字段名称
     * @param values 数据对应的值
     * @param order 排序字段
     * @param <T>
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> List<T> queryByWhereOrder(Class<T> cla, String where, String[] values, String order){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
    	return liteOrm.<T>query(new QueryBuilder(cla).where(where, values).orderBy(order + " desc"));
    }
    public static <T> List<T> queryByWhereLike(Class<T> cla){
        return null;
    }


    /**
     * 查询首页菜单列表数据
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> ArrayList<T> queryMenuList(Class<T> cla, boolean ischecked){
        createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        if(ischecked){
            return liteOrm.<T>query(new QueryBuilder(cla).where("isshow=?", new String[]{"1"}).whereAppendAnd().whereAppend("ischecked=?",new String[]{"1"}));
        }else{
            return liteOrm.<T>query(new QueryBuilder(cla).where("isshow=?", new String[]{"1"}));
        }
    }
    /**
     * 按照条件删除数据
     * @param cla 实体类的类型
     * @param field 删除的条件， 多个条件，带有 = ？
     * @param value 多个值
     * @param <T>
     */
    @SuppressWarnings("deprecation")
	public static <T> void deleteWhere(Class<T> cla,String field,String [] value){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        liteOrm.delete(cla, WhereBuilder.create(cla).where(field, value));
    }

    /**
     * 单个条件的删除
     * @param cla 实体类的类型
     * @param field 字段的名称，单个
     * @param value 字段对应的值
     * @param <T>
     */
    @SuppressWarnings("deprecation")
	public static <T> void deleteWhere(Class<T> cla,String field,String value){
        String[] values = new String[]{value};
        createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        liteOrm.delete(cla, WhereBuilder.create(cla).where(field + "=?", values));
    }

    /**
     * 删除一个实体类
     * @param t 实体类
     * @param <T>
     */
    public static <T> void delete(T t){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        liteOrm.delete(t);
    }

    /**
     * 删除所有数据
     * @param cla 实体类的类型
     * @param <T>
     */
    public static <T> void deleteAll(Class<T> cla){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        liteOrm.deleteAll(cla);
    }
    /**
     * 连库文件一起删掉
     */
    public static <T> void deleteDatabase(){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        liteOrm.deleteDatabase();
    }
    /**
     * 重建一个新库
     */
    public static <T> void reCreateDatabase(){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        liteOrm.openOrCreateDatabase();
    }

    /**
     * 删除某个区间的数据
     * @param cla 实体类的类型
     * @param arg1 删除第arg1到第arg2条数据
     * @param arg2
     * @param arg3 可为null，默认按ID升序排列 
     */
    public static <T> void deleteSection(Class<T> cla,long arg1, long arg2, String arg3){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        liteOrm.delete(cla, arg1, arg2, arg3);
    }
    
    /**删除一个实体list
     * @param list
     */
    public static <T> void deleteList(List<T> list){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        liteOrm.delete(list);
    }
        

    /**
     * 仅在已存在的时候更新
     * @param t 实体类
     * @param <T>
     */
    public static <T> void update(T t){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        liteOrm.update(t, ConflictAlgorithm.Replace);
    }

    /**
     * 更新所有
     * @param list 实体类的集合
     * @param <T>
     */
    public static <T> void updateALL(List<T> list){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        liteOrm.update(list);
    }
    
    
    /**
     * 查询某个表的数据
     * @param <T>
     * @param cla
     * @return
     */
    public static <T> long queryCount(Class<T> cla){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
    	return liteOrm.queryCount(cla);
    }
}

```

## 注解

#### 基本注解

* @Table("test_model") 表名
* @PrimaryKey(AssignType.AUTO_INCREMENT) 主键自增长
* @PrimaryKey(AssignType.BY_MYSELF) 自己设置主键
* @Ignore 忽略该字段，不存入数据库
* @Column("login") 指定列名
* @Collate("NOCASE") 大小写无关


#### 关系映射

* @Mapping(Relation.ManyToMany) 多对多
* @Mapping(Relation.OneToMany) 一对多
* @Mapping(Relation.OneToOne) 一对一
* @Mapping(Relation.ManyToOne) 多对一
* @MapCollection(ConcurrentLinkedQueue.class) 指定约束对象的集合类型


#### 约束相关

* @NotNull 非空约束
* @Default("true") 默认约束
* @Check("index > 0 ") check约束
* @Unique 唯一约束
* @UniqueCombine() 联合唯一约束


## CRUD操作

查询操作

```
//聚合函数count查询，好像只有这一个
long nums = liteOrm.queryCount(Address.class); //查询有多少行

//模糊查询
QueryBuilder<Address> qb = new QueryBuilder<Address>(Address.class).where("address LIKE ?", new String[]{"%山%"});

//与或非等
qb = new QueryBuilder<Address>(Address.class)        
.whereEquals("city", "南京")
.whereAppendAnd() 
.whereEquals("address", "香港路");

//自己拼SQL语句
QueryBuilder<Address> qb = new QueryBuilder<Address>(Address.class)        
.columns(new String[]{Address.COL_ADDRESS})    //查询列
.appendOrderAscBy(Address.COL_ADDRESS)        //升序
.appendOrderDescBy(Address.COL_ID)       //当第一列相同时使用该列降序排序
.distinct(true)        //去重
.where(Address.COL_ADDRESS + "=?", new String[]{"香港路"}); //where条件

liteOrm.query(qb);
```

sql语句

```
SELECT 列
FROM 表
WHERE 条件
GROUP BY 分组条件
HAVING 分组后条件
ORDER BY 排序
LIMIT (x,y)
```


## 级联操作

```
@Table("school")
public class School{
  @Mapping(Relation.OneToMany)
  public ArrayList<Classes> classesList; //一个学校有多个教室
}

@Table("class")public class Classes  {        
  @Mapping(Relation.OneToOne)   
  public Teacher teacher; //一个教室有一个老师，假设
}

@Table("teacher")public class Teacher {   
  @Mapping(Relation.ManyToMany)    
  @MapCollection(ConcurrentLinkedQueue.class)   
  private Queue<Student> studentLinkedQueue; //一个老师多个学生，一个学生多个老师，多对多关系
}

@Table("student")public class Student  {    
  @Mapping(Relation.ManyToMany)    
  private Teacher[] teachersArray;//一个老师多个学生，一个学生多个老师，多对多关系
}
```

注：级联操作没有使用过，通过网络查询到的。
