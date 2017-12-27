package com.logpie.dba.api.repository;


import com.logpie.dba.api.basic.*;
import com.logpie.dba.support.Assert;
import com.logpie.dba.support.ReflectionUtil;
import com.logpie.dba.support.SqlUtil;
import com.logpie.dba.support.TableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class JDBCTemplateRepository<T extends Model> implements
		SimpleRepository<T, Long>, PagingAndSortingRepository<T, Long> {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private Class<T> c;
	private RowMapper<T> rowMapper;

	/**
	 * initiate repository
	 * 
	 * @param c
	 */
	public JDBCTemplateRepository(Class<T> c) {
		this.c = c;
		rowMapper = (RowMapper<T>) ReflectionUtil.buildInstanceByDefaultConstructor(c);
	}

	@Override
	public Long insert(T model) {
		String sql = SqlUtil.insertSQL(model);
		if (sql == null) {
			return null;
		}
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				return connection.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
			}
		};
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(psc, holder);
		return holder.getKey() == null ? null : new Long(holder.getKey()
				.longValue());
	}

	@Override
	public void update(T model) {
		String sql = SqlUtil.updateSQL(model);
		if (sql == null) {
			return;
		}
		jdbcTemplate.execute(sql);
	}

	@Override
	public T queryOne(Long primaryKey) {
		String sql = SqlUtil.queryAllSQL(c);
		if (sql == null) {
			return null;
		}
		Parameter param = new WhereParam(c, TableUtil.getId(c), primaryKey);
		sql += SqlUtil.whereSQL(c, param);
		return jdbcTemplate.queryForObject(sql, rowMapper);
	}

	@Override
	public Iterable<T> queryAll() {
		String sql = SqlUtil.queryAllSQL(c);
		if (sql == null) {
			return null;
		}
		return jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public Page<T> queryAll(Pageable pageable) {
		Assert.notNull(pageable, "Paging information must not be null");

		String sql = SqlUtil.queryBySQL(c, pageable);
		List<T> contents = jdbcTemplate.query(sql, rowMapper);

		return new SimplePage<>(pageable, contents, count());
	}

	@Override
	public Iterable<T> queryBy(Parameter... params) {
		String sql = SqlUtil.queryBySQL(c, params);
		if (sql == null) {
			return null;
		}
		return jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public Page<T> queryBy(Pageable pageable, Parameter... params) {
		Assert.notNull(pageable, "Paging information must not be null");
		Assert.notNull(params, "Parameter must not be null");

		String sql = SqlUtil.queryBySQL(c, pageable, params);
		System.out.println("SQL --- " + sql);
		List<T> contents = jdbcTemplate.query(sql, rowMapper);

		return new SimplePage<>(pageable, contents, count());
	}

	@Override
	public boolean exists(Long primaryKey) {
		return queryOne(primaryKey) == null ? true : false;
	}

	@Override
	public int count() {
		String sql = SqlUtil.countSQL(c, null);
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	@Override
	public void delete(T model) {
		// TODO Auto-generated method stub

	}

	public Iterable<T> query(String sql) {
		if (sql == null || sql.isEmpty()) {
			return null;
		}
		return jdbcTemplate.query(sql, rowMapper);
	}
}
