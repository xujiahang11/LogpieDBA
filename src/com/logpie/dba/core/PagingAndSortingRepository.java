package com.logpie.dba.core;


import com.logpie.dba.dao.Condition;
import com.logpie.dba.dao.Page;
import com.logpie.dba.dao.Pageable;

import java.io.Serializable;

public interface PagingAndSortingRepository<T extends Model, S extends Serializable> {

    public Page<T> queryAll(Pageable pageable);

    public Page<T> queryBy(Pageable pageable, Condition condition);

}
