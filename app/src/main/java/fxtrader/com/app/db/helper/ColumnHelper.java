package fxtrader.com.app.db.helper;

import android.content.ContentValues;
import android.database.Cursor;

public abstract class ColumnHelper<T>{
	/**
	 * 判断数据是否存在
	 * 
	 * @param
	 * @return
	 */
	protected boolean exist(Cursor c) {
		return c.getCount() > 0;
	}
	protected int getInt(Cursor c, String columnName) {
		return c.getInt(c.getColumnIndex(columnName));
	}

	protected String getString(Cursor c, String columnName) {
		return c.getString(c.getColumnIndex(columnName));
	}

	protected long getLong(Cursor c, String columnName) {
		return c.getLong(c.getColumnIndex(columnName));
	}
	
	protected static String getSelectSql(String tableName, String[] args) {
		StringBuilder builder = new StringBuilder("SELECT * FROM ");
		builder.append(tableName).append(" WHERE ");
		int length = args.length;
		for (int i = 0; i < length; i++) {
			builder.append(args[i]).append(" = ?");
			if (i != length - 1) {
				builder.append(" AND ");
			}
		}
		return builder.toString();
	}
	
	protected static String getWhereStr(String key) {
		return key + " = ?";
	}

	protected abstract ContentValues getValues(T bean);

	protected abstract T getBean(Cursor c);
}
