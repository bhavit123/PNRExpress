package com.bhavit.pnrexpress.dao;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bhavit.pnrexpress.model.LastStatus;
import com.bhavit.pnrexpress.model.PnrDetail;
import com.bhavit.pnrexpress.model.Station;

public class SqlHelper extends SQLiteOpenHelper {

	private Context myContext;

	public SqlHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.myContext = context;
	}

	private static String DB_PATH = "/data/data/com.bhavit.pnrexpress/databases/";
	private static final String DATABASE_NAME = "pnrexpressdb";
	private static final int DATABASE_VERSION = 13;

	// TABLE CHECKED_PNRS
	private static final String KEY_PNR_NUM = "PNR_NUMBER";
	private static final String KEY_TRAIN_NAME = "TRAIN_NAME";
	private static final String KEY_TRAIN_NUM = "TRAIN_NUMBER";
	private static final String KEY_FROM_ST_NAME = "FROM_STATION_NAME";
	private static final String KEY_FROM_ST_CODE = "FROM_STATION_CODE";
	private static final String KEY_TO_ST_NAME = "TO_STATION_NAME";
	private static final String KEY_TO_ST_CODE = "TO_STATION_CODE";
	private static final String KEY_BOARD_ST_NAME = "BOARDING_STATION_NAME";
	private static final String KEY_BOARD_ST_CODE = "BOARDING_STATION_CODE";
	private static final String KEY_RESUPTO_ST_NAME = "RESERVATION_UPTO_STATION_NAME";
	private static final String KEY_RESUPTO_ST_CODE = "RESERVATION_UPTO_STATION_CODE";
	private static final String KEY_DATE_OF_JOURNEY = "DATE_OF_JOURNEY";
	private static final String KEY_RES_CLASS = "RESERVATION_CLASS";

	// TABLE CHECKED_PNRS
	private static final String KEY_ST_NAME = "STATION_NAME";
	private static final String KEY_ST_CODE = "STATION_CODE";
	private static final String KEY_ST_NUM = "STATION_NUM";
	private static final String KEY_ARRIVAL_TIME = "ARRIVAL_TIME";
	private static final String KEY_DEPARTURE_TIME = "DEPARTURE_TIME";
	private static final String KEY_STOP_TIME = "STOP_TIME";
	private static final String KEY_DAY = "DAY";
	private static final String KEY_DISTANCE = "DISTANCE";
	private static final String KEY_LATITUDE = "LATITUDE";
	private static final String KEY_LONGITUDE = "LONGITUDE";
	
	
	
	// insert in TRAIN_ROUTE
		public void insertInTrainRoute(Station obj) {
			SQLiteDatabase db = this.getWritableDatabase();

			// 2. create ContentValues to add key "column"/value
			ContentValues values = new ContentValues();
			values.put(KEY_PNR_NUM, obj.getPnrNumber());
			values.put(KEY_ST_NAME, obj.getStationName());
			values.put(KEY_ST_CODE, obj.getStationCode());
			values.put(KEY_ST_NUM, obj.getStationNo());
			values.put(KEY_ARRIVAL_TIME, obj.getArrivalTime());
			values.put(KEY_DEPARTURE_TIME, obj.getDepartureTime());
			values.put(KEY_STOP_TIME, obj.getStopTime());
			values.put(KEY_DAY, obj.getDay());
			values.put(KEY_DISTANCE, obj.getDistance());
			values.put(KEY_LATITUDE, obj.getLatitude());
			values.put(KEY_LONGITUDE, obj.getLongitude());

			// 3. insert
			db.insert("TRAIN_ROUTE", // table
					null, // nullColumnHack
					values); // key/value -> keys = column names/ values = column
			// values

			// 4. close
			db.close();

		}


	// insert in CHECKED_PNRS
	public void insertInCheckedPnrs(PnrDetail obj) {
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_PNR_NUM, obj.getPnrNumber());
		values.put(KEY_TRAIN_NAME, obj.getTrainName());
		values.put(KEY_TRAIN_NUM, obj.getTrainNumber());
		values.put(KEY_FROM_ST_NAME, obj.getFromStationName());
		values.put(KEY_FROM_ST_CODE, obj.getFromStationCode());
		values.put(KEY_TO_ST_NAME, obj.getToStationName());
		values.put(KEY_TO_ST_CODE, obj.getToStationCode());
		values.put(KEY_BOARD_ST_NAME, obj.getBoardingStationName());
		values.put(KEY_BOARD_ST_CODE, obj.getBoardingStationCode());
		values.put(KEY_RESUPTO_ST_NAME, obj.getReservationUptoStationName());
		values.put(KEY_RESUPTO_ST_CODE, obj.getReservationUptoStationCode());
		values.put(KEY_DATE_OF_JOURNEY, obj.getDateOfJourney());
		values.put(KEY_RES_CLASS, obj.getReservationClass());
		// 3. insert
		db.insert("CHECKED_PNRS", // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
		// values

		// 4. close
		db.close();

	}
	
	// get Details from TRAIN_ROUTE
		public ArrayList<Station> getTrainRoute(String pnr) {
			ArrayList<Station> list = new ArrayList<Station>();

			Station obj = null;
			String query = "select * from TRAIN_ROUTE where PNR_NUMBER ="+pnr;
			SQLiteDatabase db = this.getWritableDatabase();

			Cursor cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				do {
					obj = new Station();
					obj.setPnrNumber(cursor.getString(0));
					obj.setStationName(cursor.getString(1));
					obj.setStationCode(cursor.getString(2));
					obj.setStationNo(cursor.getString(3));
					obj.setArrivalTime(cursor.getString(4));
					obj.setDepartureTime(cursor.getString(5));
					obj.setStopTime(cursor.getString(6));
					obj.setDay(cursor.getString(7));
					obj.setDistance(cursor.getString(8));
					obj.setLatitude(cursor.getString(9));
					obj.setLongitude(cursor.getString(10));

					list.add(obj);
				} while (cursor.moveToNext());

			}
			cursor.close();
			return list;

		}

	// CHECK IF PNR EXIST
	public boolean doesPnrExist(String pnr) {

		String query = "select * from CHECKED_PNRS where PNR_NUMBER = '" + pnr
				+ "'";
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(query, null);

		if (cursor.getCount() == 0) {

			return false;

		} else
			return true;

	}

	// get Details from pNRDetails
	public List<PnrDetail> getPnrDetail() {
		List<PnrDetail> list = new ArrayList<PnrDetail>();

		PnrDetail obj = null;
		String query = "select * from CHECKED_PNRS";
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				obj = new PnrDetail();
				obj.setPnrNumber(cursor.getString(0));
				obj.setTrainName(cursor.getString(1));
				obj.setTrainNumber(cursor.getString(2));
				obj.setFromStationName(cursor.getString(3));
				obj.setFromStationCode(cursor.getString(4));
				obj.setToStationName(cursor.getString(5));
				obj.setToStationCode(cursor.getString(6));
				obj.setBoardingStationName(cursor.getString(7));
				obj.setBoardingStationCode(cursor.getString(8));
				obj.setReservationUptoStationName(cursor.getString(9));
				obj.setReservationUptoStationCode(cursor.getString(10));
				obj.setDateOfJourney(cursor.getString(11));
				obj.setReservationClass(cursor.getString(11));
				list.add(obj);
			} while (cursor.moveToNext());

		}
		cursor.close();
		return list;

	}
	
	public List<PnrDetail> getUpcomingPnrDetails() {
		List<PnrDetail> list = new ArrayList<PnrDetail>();

		PnrDetail obj = null;
		String query = "select * from CHECKED_PNRS";
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date date;
				long time = 0;
				try {
					date = dateFormat.parse(cursor.getString(11));
					time = date.getTime();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(new Timestamp(time).getTime() >= System.currentTimeMillis()) {
				obj = new PnrDetail();
				obj.setPnrNumber(cursor.getString(0));
				obj.setTrainName(cursor.getString(1));
				obj.setTrainNumber(cursor.getString(2));
				obj.setFromStationName(cursor.getString(3));
				obj.setFromStationCode(cursor.getString(4));
				obj.setToStationName(cursor.getString(5));
				obj.setToStationCode(cursor.getString(6));
				obj.setBoardingStationName(cursor.getString(7));
				obj.setBoardingStationCode(cursor.getString(8));
				obj.setReservationUptoStationName(cursor.getString(9));
				obj.setReservationUptoStationCode(cursor.getString(10));
				obj.setDateOfJourney(cursor.getString(11));
				obj.setReservationClass(cursor.getString(11));
				list.add(obj);
				}
			} while (cursor.moveToNext());

		}
		cursor.close();
		return list;

	}

	public boolean doesPnrExistInLastCheckedStatus(String pnr) {

		String query = "select * from LAST_CHECKED_STATUS where PNR_NUMBER = '"
				+ pnr + "'";
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(query, null);

		if (cursor.getCount() == 0) {
			cursor.close();
			return false;

		} else {
			cursor.close();
			return true;
		}

	}

	// insert in LAST_CHECKED_STATUS
	public void insertInLastCheckedStatus(LastStatus obj) {
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_PNR_NUM, obj.getPnrNumber());
		values.put("LAST_STATUS", obj.getLastStatus());

		// 3. insert
		db.insert("LAST_CHECKED_STATUS", // table
				null, // nullColumnHack
				values); // key/value -> keys = column names/ values = column
		// values

		// 4. close
		db.close();

	}

	// update in LAST_CHECKED_STATUS
	public void updateInLastCheckedStatus(LastStatus obj) {
		SQLiteDatabase db = this.getWritableDatabase();

		// 2. create ContentValues to add key "column"/value
		ContentValues values = new ContentValues();
		values.put(KEY_PNR_NUM, obj.getPnrNumber());
		values.put("LAST_STATUS", obj.getLastStatus());

		// Update
		db.update("LAST_CHECKED_STATUS", values,
				"PNR_NUMBER = '" + obj.getPnrNumber() + "'", null);

		// 4. close
		db.close();

	}

	// get last status of a pnr

	public String getLastStatus(String pnr) {

		String query = "select LAST_STATUS from LAST_CHECKED_STATUS where PNR_NUMBER = '"
				+ pnr + "'";
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		String result = cursor.getString(0);
		cursor.close();
		return result;

	}

	
	public void deleteRow(String tableName, String whereClause) {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(tableName, whereClause, null);

	}

	// delete table contents
	public void deleteTable(String DATABASE_TABLE) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(DATABASE_TABLE, null, null);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_TABLE_CHECKED_PNRS = "CREATE TABLE CHECKED_PNRS( "
				+ "PNR_NUMBER TEXT, " + "TRAIN_NAME TEXT, "
				+ "TRAIN_NUMBER INTEGER, " + "FROM_STATION_NAME TEXT, "
				+ "FROM_STATION_CODE TEXT, " + "TO_STATION_NAME TEXT, "
				+ "TO_STATION_CODE TEXT, " + "BOARDING_STATION_NAME TEXT, "
				+ "BOARDING_STATION_CODE TEXT, "
				+ "RESERVATION_UPTO_STATION_NAME TEXT, "
				+ "RESERVATION_UPTO_STATION_CODE TEXT, "
				+ "DATE_OF_JOURNEY TEXT, " + "RESERVATION_CLASS TEXT)";

		String CREATE_TABLE_LAST_CHECKED_STATUS = "CREATE TABLE LAST_CHECKED_STATUS( "
				+ "PNR_NUMBER TEXT, " + "LAST_STATUS TEXT)";

		String CREATE_TABLE_TRAIN_ROUTE = "CREATE TABLE TRAIN_ROUTE( "
				+ "PNR_NUMBER TEXT, " + "STATION_NAME TEXT, "
				+ "STATION_CODE TEXT, " + "STATION_NUM INTEGER, "
				+ "ARRIVAL_TIME TEXT, " + "DEPARTURE_TIME TEXT, "
				+ "STOP_TIME TEXT, " + "DAY INTEGER, " + "DISTANCE TEXT, "
				+ "LATITUDE TEXT, " + "LONGITUDE TEXT)";

		db.execSQL(CREATE_TABLE_CHECKED_PNRS);
		db.execSQL(CREATE_TABLE_LAST_CHECKED_STATUS);
		db.execSQL(CREATE_TABLE_TRAIN_ROUTE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 db.execSQL("DROP TABLE IF EXISTS CHECKED_PNRS");
		 db.execSQL("DROP TABLE IF EXISTS LAST_CHECKED_STATUS");
		 db.execSQL("DROP TABLE IF EXISTS TRAIN_ROUTE");

		this.onCreate(db);

	}

}
