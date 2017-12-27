package com.logpie.dba.support;


import com.logpie.dba.api.annotation.*;
import com.logpie.dba.api.basic.KVP;
import com.logpie.dba.api.basic.Model;
import com.logpie.dba.api.basic.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModelUtil {

	private static final String CLASSNAME = ModelUtil.class.getName();
	private static final Logger logger = Logger.getLogger(CLASSNAME);

	/**
	 * get a list of Key-Value Pair objects of model
	 * 
	 * @param model
	 *            is related java entity instance
	 * @param hasAutoGeneratedKey
	 *            determines whether the map contains auto-generated keys
	 * @return a list of KVP objects, where key is database key name and value
	 *         is java entity
	 */
	public static List<KVP> getModelKVP(final Model model,
										final boolean hasAutoGeneratedKey) {
		Assert.notNull(model, "Model must not be null");

		Table table = new Table(model.getClass());
		List<KVP> result = new ArrayList<>();

		Field[] fields = model.getClass().getDeclaredFields();
		for (Field field : fields) {

			// find every field of this model corresponding database column
			if (field.isAnnotationPresent(Column.class)) {

				Column column = field.getAnnotation(Column.class);
				if (!hasAutoGeneratedKey
						&& field.isAnnotationPresent(AutoGenerate.class)) {
					continue;
				}

				// get value of this field
				Object value = ReflectionUtil.runGetter(field, model);
				result.add(new KVP(table, column.name(), value));
			}
			// find every field of model corresponding foreign key
			else if (field.isAnnotationPresent(ForeignKeyColumn.class)) {

				// get this foreign table model
				Model entity = (Model) ReflectionUtil.runGetter(field, model);
				ForeignKeyColumn column = field.getAnnotation(ForeignKeyColumn.class);

				if (entity == null) {
					result.add(new KVP(table, column.name(), null));
				} else {
					// get fields of table
					Field[] referencedFields = column.referencedEntityClass()
							.getDeclaredFields();

					for (Field f : referencedFields) {
						// find the field representing ID of foreign table
						if (f.isAnnotationPresent(ID.class)) {
							result.add(new KVP(table, column.name(), ReflectionUtil.runGetter(
									f, entity)));
							break;
						}
					}
				}
			}

		}

		return result;
	}

	public static Long getIdValue(final Model model) {
		Assert.notNull(model, "Model must not be null");

		Field[] fields = model.getClass().getDeclaredFields();
		for (Field field : fields) {

			if (field.isAnnotationPresent(ID.class)
					&& field.isAnnotationPresent(Column.class)) {
				return (Long) ReflectionUtil.runGetter(field, model);
			}
		}

		logger.log(Level.WARNING, "Cannot find any ID from this table");
		return null;
	}


}
