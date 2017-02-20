package com.ruin.db;

import java.util.ArrayList;
import com.ruin.util.SwapMessage;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * 数据库操作类
 * @author LHH
 *
 */
public class SqlOpenHelper extends SQLiteOpenHelper {

	public static final int version = 1;//数据库版本
	private static  SqlOpenHelper helper = null;//数据库打开对象
	private static SQLiteDatabase sqlDB=null;//获取该数据库对象
	public static final String createTable = "create table tb_info (" +
			"ID integer primary key autoincrement, " +
			"Temp text, " +
			"Humi text, " +
			"Light text, " +
			"Rain text, " +
			"Date text) ";//创建表名sql语句
	public SqlOpenHelper(Context context, String name, CursorFactory factory,int version){
		super(context, name, factory, version);
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {//更新数据库版本
		// TODO 自动生成的方法存根
		
	}
	




	@Override
	public void onCreate(SQLiteDatabase arg0) {

		arg0.execSQL(createTable);//创建数据表

	}

	
	

	

	public synchronized static SqlOpenHelper getInstance(Context context,String name) {//静态获取数据库对象 
		if (helper == null) { 
			helper = new SqlOpenHelper(context, name, null, version);  
			sqlDB = helper.getWritableDatabase();

		} 
		return helper; 
	};

	

	public void insert2db(SwapMessage msg) { //插入一条记录到数据库
		ContentValues contentValues=new ContentValues();
		contentValues.put("temp", msg.getTemp());
		contentValues.put("Humi", msg.getHumi());
		contentValues.put("Light", msg.getLight());
		contentValues.put("Rain", msg.getRain());
		contentValues.put("Date", msg.getDate());
		sqlDB.insert("tb_info", null, contentValues);
				

	}
	public int deleteInfo(int ID) {	//根据ID删除数据库	
		return sqlDB.delete("tb_info", "ID=?", new String[]{String.valueOf(ID)});
	}
	
	public int deleteInfoByDate(String date) {	//根据日期删除数据库	
		return sqlDB.delete("tb_info", "Date like ? ", new String[]{"%"+date+"%"});
	}
	
	public ArrayList<SwapMessage> searchInfo(String Date,int PageID,int PageSize) {//根据日期分页查询数据库，Date：日期，PageID:第几页 PageSize：分页大小	
		 String sql= "select * from tb_info  where Date like '%" +Date+"%' "+   
			        "order by ID DESC Limit "+String.valueOf(PageSize)+ " Offset " +String.valueOf(PageID*PageSize);  
			        Cursor rec = sqlDB.rawQuery(sql, null);  
			        ArrayList<SwapMessage> list=new ArrayList<>();
	     while (rec.moveToNext()) {
	    	 SwapMessage msg=new SwapMessage();	    	
	    	 msg.setID(rec.getInt(rec.getColumnIndex("ID")));
	    	 msg.setTemp(rec.getString(rec.getColumnIndex("Temp")));
	    	 msg.setHumi(rec.getString(rec.getColumnIndex("Humi")));
	    	 msg.setLight(rec.getString(rec.getColumnIndex("Light")));
	    	 msg.setRain(rec.getString(rec.getColumnIndex("Rain")));
	    	 msg.setDate(rec.getString(rec.getColumnIndex("Date")));    	 
	    	 list.add(msg);			
		}
	     return list;

	}






	
	


}
