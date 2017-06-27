package com.dawn.liteormdawn;

import android.content.Context;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ConflictAlgorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dawn on 2016/5/4.
 * note:
 * 数据库的基本操作
 */
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
     * 重建一个新库
     */
    public static <T> void reCreateDatabase(){
        createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
        liteOrm.openOrCreateDatabase();
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
     * 查询某个表的数量
     * @param <T>
     * @param cla
     * @return
     */
    public static <T> long queryCount(Class<T> cla){
    	createLiteOrm(BaseApplication.getAppContext(), DB_NAME);
    	return liteOrm.queryCount(cla);
    }
}
