package com.logpie.dba.api.repository;


import com.logpie.dba.api.basic.Model;
import com.logpie.dba.api.basic.Page;
import com.logpie.dba.api.basic.Pageable;
import com.logpie.dba.api.basic.Parameter;

import java.io.Serializable;

public interface PagingAndSortingRepository<T extends Model, S extends Serializable> {

	public Page<T> queryAll(Pageable pageable);

	public Page<T> queryBy(Pageable pageable, Parameter... params);

}
