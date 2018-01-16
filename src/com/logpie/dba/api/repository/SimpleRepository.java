package com.logpie.dba.api.repository;


import com.logpie.dba.api.basic.Model;
import com.logpie.dba.api.basic.Parameter;

import java.io.Serializable;

public interface SimpleRepository<T extends Model, S extends Serializable> {

	public T insert(T model);

	public void update(T model);

	public T queryOne(S primaryKey);

	public Iterable<T> queryAll();

	public Iterable<T> queryBy(Parameter... conditions);

	public boolean exists(S primaryKey);

	public int count();

	public void delete(T model);
}
