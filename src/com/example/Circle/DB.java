package com.example.Circle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;
import android.widget.Toast;


public class DB {
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_TIME = "time";
	public static final String KEY_CONTEXT ="context";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_MYLOVE = "mylove";
	public static final String KEY_KIND = "kind";
	//宣告欄位的名稱
	
	private static final String DATABASE_TABLE_RECEIVE = "receive_collection_table";
	private static final String DATABASE_TABLE2_PUSH = "push_table";
	private static final String DATABASE_ViewCollection = "view_collection";
	//宣告資料表的名稱
	
	private Context mContext = null; //宣告場景(通常都是拿來放某activity，表示現在場景是在某activity)
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	//宣告使用資料庫的輔助函式(SQLiteOpenHelper、SQLiteDatabase)
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		private static final String DATABASE_NAME = "deyrkbase.db";
		private static final int DATABASE_VERSION = 1;
		//宣告資料庫的名稱跟版本
		
		private static final String DATABASE_CREATE = 
				"CREATE TABLE "+DATABASE_TABLE_RECEIVE+"("
				+KEY_ROWID+" INTEGER PRIMARY KEY,"
				+KEY_TITLE+" TEXT NOT NULL,"
				+KEY_TIME+" TIMESTAMP,"
				+KEY_CONTEXT+" TEXT,"
				+KEY_IMAGE+" BLOB,"
				+KEY_MYLOVE+" INTEGER,"
				+KEY_KIND+" TEXT"
				+");";
		private static final String DATABASE_CREATE_PUSH = 
				"CREATE TABLE "+DATABASE_TABLE2_PUSH+"("
				+KEY_ROWID+" INTEGER PRIMARY KEY,"
				+KEY_TITLE+" TEXT NOT NULL,"
				+KEY_TIME+" TIMESTAMP,"
				+KEY_CONTEXT+" TEXT,"
				+KEY_IMAGE+" BLOB,"
				+KEY_MYLOVE+" INTEGER,"
				+KEY_KIND+" TEXT"
				+");";
		
		private static final String DATABASE_VIEW =
				"CREATE VIEW "+DATABASE_ViewCollection+" AS SELECT"
						+" A."+KEY_ROWID+" AS _id,"
						+" A."+KEY_TITLE+" AS "+KEY_TITLE+","
						+" A."+KEY_TIME+" AS "+KEY_TIME+","
						+" A."+KEY_CONTEXT+" AS "+KEY_CONTEXT+","
						+" A."+KEY_IMAGE+" AS "+KEY_IMAGE+","
						+" A."+KEY_MYLOVE+" AS "+KEY_MYLOVE+","
						+" A."+KEY_KIND+" AS "+KEY_KIND
						+" FROM "+DATABASE_TABLE_RECEIVE+" A"
						+" WHERE A."+KEY_MYLOVE+"=1;";
		  
		//宣告創建資料表的字串
		
		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//當資料庫被建造時所做的事
			db.execSQL(DATABASE_CREATE); //創建資料表
			db.execSQL(DATABASE_CREATE_PUSH);
			db.execSQL(DATABASE_VIEW);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//當資料庫版本更新時所做的事
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE_RECEIVE); //刪除DATABASE_TABLE_RECEIVE資料表
			onCreate(db); //呼叫onCreate重建資料庫
		}
		
	}
	
	//DB建構子(設定場景)
	public DB(Context context){
		this.mContext = context;
	}
	
	//打開資料庫
	public DB open() throws SQLException {
		dbHelper = new DatabaseHelper(mContext, null, null, 0);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	//關閉資料庫
	public void close(){
		dbHelper.close();
	}
	
	//自定義函式(讓別的程式來呼叫使用)
	public Cursor getAll(){
		
		//取得DATABASE_TABLE_RECEIVE的所有欄位資料並以KEY_TIME遞減排序
		return db.query(DATABASE_TABLE2_PUSH,
				new String[]{KEY_ROWID, KEY_TITLE, KEY_TIME,KEY_CONTEXT, KEY_IMAGE,KEY_MYLOVE,KEY_KIND},
				null,
				null,
				null,
				null,
				null);
	}
	public Cursor getAllReceive(){
		
		//取得DATABASE_TABLE_RECEIVE的所有欄位資料並以KEY_TIME遞減排序
		return db.query(DATABASE_TABLE_RECEIVE,
				new String[]{KEY_ROWID, KEY_TITLE, KEY_TIME,KEY_CONTEXT, KEY_IMAGE,KEY_MYLOVE,KEY_KIND},
				null,
				null,
				null,
				null,
				KEY_TIME + " DESC");
	}
	public Cursor getVsContext(String s){
		
		//取得DATABASE_TABLE_RECEIVE的所有欄位資料並以KEY_TIME遞減排序
		return db.query(DATABASE_TABLE_RECEIVE,
				new String[]{KEY_ROWID, KEY_TITLE, KEY_TIME,KEY_CONTEXT, KEY_IMAGE,KEY_MYLOVE,KEY_KIND},
				KEY_CONTEXT + "=" + "'"+s+"'",
				null,
				null,
				null,
				null);
	}
	public Cursor getMylove(){
		return db.query(DATABASE_ViewCollection,
				new String[]{KEY_ROWID, KEY_TITLE, KEY_TIME,KEY_CONTEXT, KEY_IMAGE,KEY_MYLOVE,KEY_KIND},
				null,
				null,
				null,
				null,
				KEY_TIME + " DESC");
		
	}
	//取得DATABASE_TABLE_RECEIVE的指定ID一筆資料
	public Cursor getlistad_push(Long receive_id){
		
		return db.query(DATABASE_TABLE2_PUSH,
				new String[]{KEY_ROWID, KEY_TITLE, KEY_TIME,KEY_CONTEXT, KEY_IMAGE,KEY_MYLOVE,KEY_KIND},
				KEY_ROWID + "=" + receive_id,
				null,
				null,
				null,
				null
				);
	}
	
	public Cursor getlistad_receive(Long receive_id){
		
		return db.query(DATABASE_TABLE_RECEIVE,
				new String[]{KEY_ROWID, KEY_TITLE, KEY_TIME,KEY_CONTEXT, KEY_IMAGE,KEY_MYLOVE,KEY_KIND},
				KEY_ROWID + "=" + receive_id,
				null,
				null,
				null,
				null
				
				);
		
				
	}
	
	// +"and"+ KEY_MYLOVE + "=" + 1
	public Cursor getlistlovead(Long receive_id){
		
		return db.query(DATABASE_ViewCollection,
				new String[]{KEY_ROWID, KEY_TITLE, KEY_TIME,KEY_CONTEXT, KEY_IMAGE,KEY_MYLOVE,KEY_KIND},
				KEY_ROWID + "=" + receive_id,
				null,
				null,
				null,
				null
				
				);
		
				
	}
	
	//自定義函式(讓別的程式來呼叫使用)
	public long create(String titlerecord, String contextrecord, Bitmap imagerecord, String kindrecord){
		
		//新增一筆資料
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.ENGLISH);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Date now = new Date();
		//宣告&設定：df(日期格式化)、bos(二元陣列轉換函式)、now(日期)
		if(imagerecord!=null){
			imagerecord.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			//將傳進來的imagerecord轉成Bitmap格式
			int options = 100;  
			while ( bos.toByteArray().length/1024 > 2000) {
				bos.reset();
				imagerecord.compress(Bitmap.CompressFormat.JPEG, options, bos);
				options -= 10;
				Toast.makeText(this.mContext, ""+options, Toast.LENGTH_SHORT).show();
			}
		}
        Toast.makeText(this.mContext, "bos.toByteArray().length: "+bos.toByteArray().length, Toast.LENGTH_SHORT).show();
		ContentValues args = new ContentValues(); //宣告一個容器(args)
		args.put(KEY_TITLE, titlerecord);
		args.put(KEY_TIME, df.format(now.getTime()));
		if(imagerecord==null){
			args.put(KEY_IMAGE, bos.toByteArray());
		}else{
			args.put(KEY_IMAGE, bos.toByteArray());
		}
		args.put(KEY_CONTEXT, contextrecord);
		args.put(KEY_MYLOVE, 0);
		args.put(KEY_KIND, kindrecord);
		//用put的方式將各個資料放進各個欄位
		
		return db.insert(DATABASE_TABLE_RECEIVE, null, args);
		//將以上設定好的一筆資料新增到DATABASE_TABLE_RECEIVE裡
	}
	
	//自定義函式(讓別的程式來呼叫使用)
		public long createPush(String titlerecord, String contextrecord, Bitmap imagerecord, String kindrecord){
			
			//新增一筆資料
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss",Locale.ENGLISH);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Date now = new Date();
			//宣告&設定：df(日期格式化)、bos(二元陣列轉換函式)、now(日期)
			if(imagerecord!=null){
				imagerecord.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				//將傳進來的imagerecord轉成Bitmap格式
				int options = 100;  
				while ( bos.toByteArray().length/1024 > 2000) {
					bos.reset();
					imagerecord.compress(Bitmap.CompressFormat.JPEG, options, bos);
					options -= 10;
					Toast.makeText(this.mContext, ""+options, Toast.LENGTH_SHORT).show();
				}
			}
	        Toast.makeText(this.mContext, "bos.toByteArray().length: "+bos.toByteArray().length, Toast.LENGTH_SHORT).show();
			ContentValues args = new ContentValues(); //宣告一個容器(args)
			args.put(KEY_TITLE, titlerecord);
			args.put(KEY_TIME, df.format(now.getTime()));
			if(imagerecord==null){
				args.put(KEY_IMAGE, bos.toByteArray());
			}else{
				args.put(KEY_IMAGE, bos.toByteArray());
			}
			args.put(KEY_CONTEXT, contextrecord);
			args.put(KEY_MYLOVE, 0);
			args.put(KEY_KIND, kindrecord);
			//用put的方式將各個資料放進各個欄位
			
			return db.insert(DATABASE_TABLE2_PUSH, null, args);
			//將以上設定好的一筆資料新增到DATABASE_TABLE裡
		}
	
	//自定義函式(讓別的程式來呼叫使用)
	//資料庫刪除資料
	public boolean delete(long rowId){
		
		//刪除一筆資料
		
		return db.delete(DATABASE_TABLE_RECEIVE,KEY_ROWID + "=" + rowId, null) > 0;
		//刪除DATABASE_TABLE資料表的一筆資料
	}
	//資料庫修改資料
	public boolean update(long rowId,String titlerecord, String contextrecord, Bitmap imagerecord, String kindrecord){
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		if(imagerecord!=null){
		imagerecord.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		//byte[] b = bos.toByteArray();
		int options = 100;  
        	while ( (bos.toByteArray().length / 1024)>2000) {
        		bos.reset();
        		imagerecord.compress(Bitmap.CompressFormat.JPEG, options, bos);
        		options -= 10;
        	}
		}
        //ByteArrayInputStream isBm = new ByteArrayInputStream(bos.toByteArray());
        //Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        //byte[] b = bos.toByteArray();
	
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE,titlerecord);
		args.put(KEY_CONTEXT, contextrecord);
		if(imagerecord==null){
			args.put(KEY_IMAGE, bos.toByteArray());
		}else{
			args.put(KEY_IMAGE, bos.toByteArray());
		}
		
		args.put(KEY_KIND, kindrecord);
		return db.update(DATABASE_TABLE2_PUSH, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	public boolean loveupdate(long rowId,int myloverecord){
		ContentValues args = new ContentValues();
		args.put(KEY_MYLOVE,myloverecord);
		return db.update(DATABASE_TABLE_RECEIVE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	public boolean loveupdate_push(Long rowId,int myloverecord){
		ContentValues args = new ContentValues();
		args.put(KEY_MYLOVE,myloverecord);
		return db.update(DATABASE_TABLE2_PUSH, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	
}
