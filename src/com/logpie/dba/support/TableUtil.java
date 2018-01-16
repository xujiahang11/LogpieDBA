package com.logpie.dba.support;

import com.logpie.dba.api.annotation.*;
import com.logpie.dba.api.basic.ForeignKey;

import java.lang.reflect.Field;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableUtil {

	private static final String CLASSNAME = TableUtil.class.getName();
	private static final Logger LOG = Logger.getLogger(CLASSNAME);

	// TODO: cache all columns with alias of tables
	private static final Map<Class<?>, List<String>> columnCache = new HashMap<>();


	public static String getTableName(final Class<?> c) {
		Entity table = getEntityAnnotation(c);
		Assert.notNull(table, "Cannot find any table. Please make sure to add @Entity annotation to your model class");
		return table.name();
	}

	public static String getId(final Class<?> c) {
		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(ID.class)
					&& field.isAnnotationPresent(Column.class)) {
				return field.getAnnotation(Column.class).name();
			}
		}
		LOG.log(Level.WARNING, "Cannot find any ID from this table");
		return null;
	}

	/**
	 * get columns of table c
	 * 
	 * @param c
	 * @param tableAlias
	 * @return table name/table alias + "." + column names
	 */
	public static List<String> getColumnsWithAlias(final Class<?> c,
			final String tableAlias) {
		List<String> res = new ArrayList<>();

		String table = getTableName(c);
		Assert.hasLength(table, "Table name must not be empty");

		table = (tableAlias == null || tableAlias.equals("")) ? table
				: tableAlias;

		List<Column> columns = getColumnAnnotations(c, true);
		Assert.notEmpty(columns, "Cannot find columns in Table " + table);

		for (Column column : columns) {
			res.add(table + "." + column.name());
		}

		List<ForeignKeyColumn> foreignKeys = getForeignEntityAnnotations(c);
		if (foreignKeys != null) {
			for (ForeignKeyColumn foreignKey : foreignKeys) {
				res.add(table + "." + foreignKey.name());
			}
		}

		return res;
	}

	/**
	 * get all columns from table c and all other referenced tables
	 * 
	 * @param c
	 * @return column names with table alias or name
	 */
	public static List<String> getAllColumnsWithAlias(final Class<?> c) {
		List<String> res = new ArrayList<>();
		res.addAll(getColumnsWithAlias(c, null));

		List<ForeignKey> foreignKeys = getAllForeignKeys(c, null);
		for (ForeignKey key : foreignKeys) {
			String refAlias = getOrAutoGenerateRefAlias(key);
			res.addAll(getColumnsWithAlias(key.getRefTableClass(), refAlias));
		}

		return res;
	}

	/**
	 * 
	 * @param c
	 * @param alias
	 *            set alias of table c
	 * @return
	 */
	public static List<ForeignKey> getAllForeignKeys(final Class<?> c,
			final String alias) {
		List<ForeignKey> res = new ArrayList<>();

		String table = getTableName(c);
		Assert.hasLength(table, "Table name must not be empty");

		List<ForeignKey> foreignKeys = new ArrayList<>();

		// add foreign keys of original table c
		List<ForeignKeyColumn> foreignEntities = getForeignEntityAnnotations(c);
		for (ForeignKeyColumn entity : foreignEntities) {
			foreignKeys.add(new ForeignKey(c, alias, entity));
		}

		// check if each foreign table has more foreign keys
		while (!foreignKeys.isEmpty()) {
			List<ForeignKey> moreKeys = new ArrayList<>();

			for (ForeignKey key : foreignKeys) {
				Class<?> refTable = key.getRefTableClass();

				if (key.hasChildForeignKey()) {
					String referencedAlias = getOrAutoGenerateRefAlias(key);
					List<ForeignKeyColumn> moreEntities = getForeignEntityAnnotations(refTable);

					for (ForeignKeyColumn entity : moreEntities) {
						moreKeys.add(new ForeignKey(refTable, referencedAlias,
								entity));
					}
				}

				res.add(key);
			}

			foreignKeys = moreKeys;
		}

		return res;
	}

	/**
	 * get an alias of a foreign key's table or auto generate one if it does not
	 * have any alias.
	 * 
	 * Auto-generated Alias Format
	 * 
	 * if the original table has an alias, use original_table_alias +
	 * foreign_table_name, otherwise use original_table_name +
	 * foreign_table_name;
	 * 
	 * e.g "OrderShop" means an alias for a Shop table which is referred by an
	 * Order table
	 * 
	 * @param key
	 * @return
	 */
	public static String getOrAutoGenerateRefAlias(final ForeignKey key) {
		Assert.notNull(key, "Foreign key must not be null");

		String originalAlias = key.getRefTableAlias();
		if (originalAlias == null || originalAlias.equals("")) {
			String referencedTableName = getTableName(key.getRefTableClass());
			String alias = key.getTableAlias();

			return alias == null || alias.equals("") ? key.getTableName()
					+ referencedTableName : alias + referencedTableName;
		} else {
			return originalAlias;
		}
	}

	public static String toSqlString(Object obj) {
		Assert.notNull(obj, "Value must not be null");

		if (obj instanceof Boolean || obj instanceof Number
				|| obj instanceof Timestamp) {
			return String.valueOf(obj);
		}
		return "'" + SqlUtil.clearIllegalCharacters(obj.toString()) + "'";
	}


	private static Entity getEntityAnnotation(final Class<?> c) {
		Assert.notNull(c, "Class must not be null");

		if (c.isAnnotationPresent(Entity.class)) {
			return c.getAnnotation(Entity.class);
		}
		return null;
	}

	private static List<Column> getColumnAnnotations(final Class<?> c,
			final boolean hasAutogeneratedKey) {
		Assert.notNull(c, "Class must not be null");

		List<Column> res = new ArrayList<>();

		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {

			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);

				if (!hasAutogeneratedKey
						&& field.isAnnotationPresent(AutoGenerate.class)) {
					continue;
				}

				res.add(column);
			}
		}
		return res;
	}

	private static List<ForeignKeyColumn> getForeignEntityAnnotations(
			final Class<?> c) {
		Assert.notNull(c, "Class must not be null");

		List<ForeignKeyColumn> res = new ArrayList<>();

		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {

			if (field.isAnnotationPresent(ForeignKeyColumn.class)) {
				ForeignKeyColumn column = field
						.getAnnotation(ForeignKeyColumn.class);
				res.add(column);
			}
		}
		return res;
	}
}
