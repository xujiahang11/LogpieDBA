package com.logpie.dba.core;


import com.logpie.dba.dao.Condition;

import java.io.Serializable;

public interface SimpleRepository<T extends Model, S extends Serializable> {

    public T insert(T model);

    public void update(T model);

    public T queryOne(S primaryKey);

    public Iterable<T> queryAll();

    public Iterable<T> queryBy(Condition condition);

    public boolean exists(S primaryKey);

    public int count();

    public void delete(T model);
}
