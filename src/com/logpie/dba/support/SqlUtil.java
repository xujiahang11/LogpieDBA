package com.logpie.dba.support;

import com.logpie.dba.api.basic.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqlUtil {

	private static final String CLASSNAME = ModelUtil.class.getName();
	private static final Logger LOG = Logger.getLogger(CLASSNAME);

	public static String insertSQL(final Model model) {
		String tableName = TableUtil.getTableName(model.getClass());
		List<KVP> keyValuePairs = ModelUtil.getModelKVP(model, false);

		Assert.notEmpty(keyValuePairs, "Cannot get model map for INSERT");

		StringBuilder keys = new StringBuilder();
		StringBuilder values = new StringBuilder();

		for (KVP keyValuePair : keyValuePairs) {

			if (keyValuePair.getValue() == null) {
				continue;
			}

			if (keys.length() > 0) {
				keys.append(", ");
				values.append(", ");
			}

			keys.append(keyValuePair.getKey());
			values.append(keyValuePair.valueToString());
		}

		return "insert into " + tableName + "(" + keys.toString() + ") values (" + values.toString() + ")";
	}

	public static String updateSQL(final Model model) {
		String tableName = TableUtil.getTableName(model.getClass());

		List<KVP> keyValuePairs = ModelUtil.getModelKVP(model, false);
		Assert.notEmpty(keyValuePairs, "Cannot get model map for UPDATE");

		StringBuilder sql = new StringBuilder();
		sql.append("update " + tableName + " set ");

		int i = 0;
		for (KVP keyValuePair : keyValuePairs) {

			if (keyValuePair.getValue() == null) {
				continue;
			}

			if (i++ > 0) {
				sql.append(", ");
			}

			sql.append(keyValuePair.getKey() + " = "
					+ keyValuePair.valueToString());
		}

		Long id = ModelUtil.getIdValue(model);
		Parameter cond = new WhereParam(model.getClass(), TableUtil.getId(model
				.getClass()), id);
		sql.append(whereSQL(model.getClass(), cond));

		return sql.toString();
	}

	/**
	 * a query sql without any conditional sql, using left join connections
	 * 
	 * @param c model class
	 * @return sql
	 */
	public static String queryAllSQL(final Class<?> c) {
		return "select * from " + TableUtil.getTableName(c) + joinSQL(c);
	}

	public static String queryBySQL(final Class<?> c, final Parameter... params) {
		String sql = queryAllSQL(c);
		Assert.hasLength(sql, "SQL query must not be empty");

		return params.length == 0 ? sql : sql + whereSQL(c, params);
	}

	public static String queryBySQL(final Class<?> c, final Pageable pageable,
			final Parameter... params) {
		Assert.notNull(pageable, "Pageable must not be null");

		if (!pageable.isPaged()) {
			return queryAllSQL(c);
		}

		StringBuilder sql = new StringBuilder(queryAllSQL(c));

		/*
		 * 分页SQL语句
		 * 
		 * select * from table ... join (select #id# from table where ... order
		 * by #id# limit #offset#, #size#) alias on #id# = alias.#id#
		 */
		String table = TableUtil.getTableName(c);
		String id = TableUtil.getId(c);
		String alias = "Inner" + TableUtil.getTableName(c);

		sql.append(" join (select " + id + " from " + table);

		/*
		 * 这里的where查询语句是关于分页中的where条件，目前只能查询关于主表中columns的相关条件，
		 * 不支持查询主表中其他子表的columns条件
		 */
		if (params.length > 0) {
			sql.append(whereSQL(c, params));
		}
		sql.append(" order by " + id + " limit " + pageable.getOffset() + ", "
				+ pageable.getSize() + ") " + alias);
		sql.append(" on " + table + "." + id + " = " + alias + "." + id);

		return sql.toString();
	}

	public static String countSQL(final Class<?> c, final String key,
			final Parameter... params) {
		String column = key == null || key.isEmpty() ? TableUtil.getId(c) : key;
		String sql = "select count(" + column + ") from "
				+ TableUtil.getTableName(c);

		return params.length == 0 ? sql : sql + whereSQL(c, params);
	}

	public static String orderBySQL(final Sort sort) {
		Assert.notNull(sort, "Sort must not be null");

		StringBuilder sql = new StringBuilder();
		sql.append(" order by ");

		int i = 0;
		for (Sort.Order order : sort) {
			if (i++ > 0) {
				sql.append(", ");
			}
			Assert.hasLength(order.getKey(), "Order key must not be empty");
			// TODO check alias, add Table to Order
			sql.append(order.toString());
		}
		return sql.toString();
	}

	public static String whereSQL(final Class<?> c, final Parameter... params) {
		Assert.notEmpty(params, "Parameter must not be null or empty");

		StringBuilder sql = new StringBuilder();
		sql.append(" where ");

		for (int i = 0; i < params.length; i++) {

			if (i > 0) {
				sql.append(" and ");
			}

			Parameter param = params[i];
			String column = findColumn(c, param);
			
			sql.append(column + " " + param.getOperator() + " "
					+ param.valueToString());
		}

		return sql.toString();
	}

	static String clearIllegalCharacters(final String s) {
		return s.replaceAll(".*([';]+|(--)+).*", "");
	}

	private static String joinSQL(final Class<?> c) {
		StringBuilder sql = new StringBuilder();

		Set<String> aliasSet = new HashSet<>();
		List<ForeignKey> foreignKeys = TableUtil.getAllForeignKeys(c, null);

		for (ForeignKey key : foreignKeys) {
			sql.append(" left join ");

			String alias = key.getTableAlias();
			if (alias == null || alias.equals("")) {
				alias = key.getTableName();
			}

			String refAlias = TableUtil.getOrAutoGenerateRefAlias(key);

			// check if alias of referenced table is unique
			if (aliasSet.contains(refAlias.toLowerCase())) {
				LOG.log(Level.SEVERE, "foreign table alias duplicates");
				return null;
			}
			aliasSet.add(refAlias);

			sql.append(key.getRefTableName() + " " + refAlias + " on " + alias
					+ "." + key.getKey() + " = " + refAlias + "."
					+ TableUtil.getId(key.getRefTableClass()));
		}

		return sql.toString();
	}

	private static String findColumn(final Class<?> c, final Parameter param) {
		Assert.notNull(param, "Parameter must not be null or empty");
		String tableName = param.getTable().getName();
		String tableAlias = param.getTable().getAlias();

		List<String> columns = TableUtil.getAllColumnsWithAlias(c);
		for (String column : columns) {
			String columnUncased = column.toLowerCase();

			if (!column.contains(".")) {
				if (columnUncased.equals(param.getKey().toLowerCase())) {
					return column;
				}
				continue;
			}

			String alias = columnUncased.split("\\.")[0];
			String key = columnUncased.split("\\.")[1];

			if (key.equals(param.getKey().toLowerCase())) {
				if (((tableAlias == null || tableAlias.isEmpty()) && alias
						.endsWith(tableName.toLowerCase()))
						|| (tableAlias != null && alias.equals(tableAlias
								.toLowerCase()))) {
					return column;
				}
			}
		}
		return null;
	}
}
