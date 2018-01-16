package com.logpie.dba.api.basic;


import com.logpie.dba.api.annotation.ForeignKeyColumn;
import com.logpie.dba.support.TableUtil;

import java.lang.reflect.Field;

public class ForeignKey {

	private Table table;
	private ForeignKeyColumn foreignEntity;

	public ForeignKey(Class<?> tableClass, ForeignKeyColumn foreignEntity) {
		this(tableClass, null, foreignEntity);
	}

	public ForeignKey(Class<?> tableClass, String alias,
			ForeignKeyColumn foreignEntity) {
		this.table = new Table(tableClass, alias);
		this.foreignEntity = foreignEntity;
	}

	public String getTableName() {
		return table.getName();
	}

	public String getTableAlias() {
		return table.getAlias();
	}

	public String getKey() {
		return foreignEntity.name();
	}

	public Class<?> getRefTableClass() {
		return foreignEntity.referencedEntityClass();
	}

	public String getRefTableName() {
		return TableUtil.getTableName(getRefTableClass());
	}

	public String getRefTableAlias() {
		return foreignEntity.referencedEntityAlias();
	}

	public boolean hasChildForeignKey() {
		if (hasForeignEntityAnnotation(getRefTableClass())) {
			return true;
		}
		return false;
	}

	private static boolean hasForeignEntityAnnotation(final Class<?> c) {
		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {
			if (field.isAnnotationPresent(ForeignKeyColumn.class)) {
				return true;
			}
		}
		return false;
	}
}
