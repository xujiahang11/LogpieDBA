package com.logpie.dba.core;

import com.logpie.dba.dao.*;
import com.logpie.dba.support.Assert;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;

public class SQLEngine {

    public static String toSqlString(Object obj)
    {
        Assert.notNull(obj, "Value object is null");

        if (obj instanceof Boolean || obj instanceof Number || obj instanceof Timestamp)
        {
            return String.valueOf(obj);
        }
        return "'" + SQLEngine.clearIllegalCharacters(obj.toString()) + "'";
    }

    public static String insertSQL(final Model model)
    {
        Assert.notNull(model, "Model object is null");

        List<KVP> kvps = ModelUtils.getKVPsFrom(model, false);
        Assert.notEmpty(kvps, "Cannot get the model KVPs for INSERT");

        StringBuilder keys = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (KVP kvp : kvps)
        {
            String key = kvp.getKey();
            Object value = kvp.getValue();

            if (value == null)
            {
                continue;  // Do nothing if value is null
            }

            if (keys.length() > 0)
            {
                keys.append(", ");
                values.append(", ");
            }

            keys.append(key);
            values.append(toSqlString(value));
        }

        String tableName = TableUtils.getTableName(model.getClass());
        StringBuilder sql = new StringBuilder().append("INSERT INTO ")
                                               .append(tableName)
                                               .append(" (")
                                               .append(keys.toString())
                                               .append(") VALUES (")
                                               .append(values.toString())
                                               .append(")");
        return sql.toString();
    }

    public static String updateSQL(final Model model)
    {
        Assert.notNull(model, "Model object is null");

        List<KVP> kvps = ModelUtils.getKVPsFrom(model, true);
        Assert.notEmpty(kvps, "Cannot get the model KVPs for UPDATE");

        StringBuilder sql = new StringBuilder();
        String tableName = TableUtils.getTableName(model.getClass());
        sql.append("UPDATE " + tableName + " SET ");

        int i = 0;
        for (KVP kvp : kvps)
        {
            String key = kvp.getKey();
            Object value = kvp.getValue();

            if (value == null)
            {
                continue;  // Do nothing if value is null
            }

            if (i++ > 0)
            {
                sql.append(", ");
            }

            sql.append(key + " = " + toSqlString(value));
        }

        Number id = ModelUtils.getIdValueFrom(model);
        Condition cond = getIdCondition(model.getClass(), id);
        sql.append(whereSQL(model.getClass(), cond));

        return sql.toString();
    }

    // TODO:
    public static String deleteSQL(final Model model)
    {
        return "";
    }

    public static String querySQL(final Query query, final Pageable pageable)
    {
        if(pageable == null || pageable.equals(Unpaged.INSTANCE))
        {
            return querySQL(query);
        }

        return "";
    }

    public static String querySQL(final Query query, final Sort sort)
    {
        return "";
    }

    public static String querySQL(final Query query, final Group group)
    {
        return "";
    }

    public static String querySQL(final Query query)
    {
        Assert.notNull(query, "Query must not be null");

        StringBuilder sql = new StringBuilder();

        Class<?> c = query.getC();
        Selection selection = query.getSelection();
        sql.append(selectionSQL(c, selection));

        Join join = query.getJoin();
        sql.append(joinSQL(c, join));

        Condition condition = query.getCondition();
        sql.append(whereSQL(c, condition));

        return sql.toString();
    }

    private static String deferredJoinSQL(final Query query, final Pageable pageable)
    {
        Class<?> c = query.getC();
        Table table = CacheUtil.get(c);
        String orderBy = orderBySQL(c, pageable.getSort());

        /*
         * 分页SQL语句 a deferred join
		 *
		 * select * from #table# inner join (select #id# from #table# order
		 * by #id# limit #offset#, #size#) #alias# use index(#id#)
		 */
        StringBuilder builder = new StringBuilder().append("SELECT ")
                                                   .append(table.getIdLabel())
                                                   .append(" FROM ")
                                                   .append(table.getName())
                                                   .append(orderBy)
                                                   .append(" LIMIT ")
                                                   .append(pageable.getOffset())
                                                   .append(" ")
                                                   .append(pageable.getSize());
        String sql = Join.INNER + " (" + builder.toString() + ")" + "";
        return sql;
    }

    private static String selectionSQL(final Class<?> c, final Selection selection)
    {
        Assert.notNull(selection, "Selection must not be null");
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");

        if(selection.isDistinct())
        {
            sql.append("DISTINCT ");
        }
        int i = 0;
        for(DbField dbField : selection)
        {
            if(i++ > 0)
            {
                sql.append(", ");
            }
            if(dbField.equals(All.INSTANCE))
            {
                sql.append("*");
            }else
            {
                Table table = CacheUtil.get(c);
                String[] arg = getTableAndFieldArg(table, dbField);
                Assert.notNull(arg, "Cannot find any matched dbField in table " + table.getNameOrAliasIfExisted());

                String s = parseToSQL(dbField, arg);
                if(dbField instanceof FunctionDbField)
                {
                    String columnAlias = ((FunctionDbField) dbField).getColumnAlias();
                    if(columnAlias != null)
                    {
                        s += " AS " + columnAlias;
                    }
                }
                sql.append(s);
            }
        }

        return sql.toString();
    }

    private static String joinSQL(final Class<?> c, final Join join)
    {
        Table table = CacheUtil.get(c);
        StringBuilder sql = new StringBuilder().append(" FROM ")
                                               .append(table.getNameOrAliasIfExisted())
                                               .append(" ")
                                               .append(join.toString());
        joinSQL(table, sql);
        return sql.toString();
    }

    private static void joinSQL(final Table table, final StringBuilder builder)
    {
        Map<String, Table> refTables = table.getReferencedTableMapping();
        refTables.forEach((refColumnLabel, refTable) -> {
            builder.append(refTable.getName())
                   .append(" ")
                   .append(refTable.getAlias())
                   .append(" on ")
                   .append(table.getNameOrAliasIfExisted())
                   .append(".")
                   .append(refColumnLabel)
                   .append(" = ")
                   .append(refTable.getAlias())
                   .append(".")
                   .append(refTable.getIdLabel());
            joinSQL(refTable, builder);
        });
    }

    private static String whereSQL(final Class<?> c, final Condition cond)
    {
        if(cond == null)
        {
            return "";
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" WHERE ");
        Table table = CacheUtil.get(c);
        sql.append(clauseSQL(table, cond));

        return sql.toString();
    }

    private static String clauseSQL(final Table table, final Condition root)
    {
        Assert.notNull(root, "Cannot getTableAndFieldArg any conditions");

        // If root is a condition
        if (root instanceof SimpleCondition)
        {
            DbField dbField = ((SimpleCondition) root).getClause().getDbField();
            String[] arg = getTableAndFieldArg(table, dbField);
            Assert.notNull(arg, "Cannot find any matched dbField in table " + table.getNameOrAliasIfExisted());

            return parseToSQL(dbField, arg);
        }

        // Otherwise root is a condition connector, we will look for conditions recursively
        Condition leftCondition = root.getLeft();
        Condition rightCondition = root.getRight();

        String leftClause = clauseSQL(table, leftCondition);
        String rightClause = clauseSQL(table, rightCondition);
        String connectorClause = ((LogicCondition) root).getConnector().toString();

        StringBuilder sql = new StringBuilder().append("(")
                                               .append(leftClause)
                                               .append(" ")
                                               .append(connectorClause)
                                               .append(" ")
                                               .append(rightClause)
                                               .append(")");
        return sql.toString();
    }

    private static String orderBySQL(final Class<?> c, final Sort sort)
    {
        if(sort == null)
        {
            return "";
        }

        Table table = CacheUtil.get(c);
        StringBuilder sql = new StringBuilder();
        sql.append(" ORDER BY ");

        int i = 0;
        for (Sort.Order order : sort)
        {
            if (i++ > 0)
            {
                sql.append(", ");
            }

            DbField dbField = order.getDbField();
            String[] arg = getTableAndFieldArg(table, dbField);
            Assert.notNull(arg, "Cannot find any matched dbField in table " + table.getNameOrAliasIfExisted());
            sql.append(parseToSQL(dbField, arg));
        }

        return sql.toString();
    }

    private static String groupBySQL(final Class<?> c, final Group group, final Condition having)
    {
        if(group == null)
        {
            return "";
        }

        Table table = CacheUtil.get(c);
        StringBuilder sql = new StringBuilder();
        sql.append(" GROUP BY ");

        int i = 0;
        for(DbField dbField : group)
        {
            if(i++ > 0)
            {
                sql.append(", ");
            }

            String[] arg = getTableAndFieldArg(table, dbField);
            Assert.notNull(arg, "Cannot find any matched dbField in table " + table.getNameOrAliasIfExisted());
            sql.append(parseToSQL(dbField, arg));
        }

        if(having != null)
        {
            String havingClause = clauseSQL(table, having);
            sql.append(havingClause);
        }

        return sql.toString();
    }

    /**
     * Find a target table which has a matched key compared with the given dbField. If
     * there are multiple eligible tables, then return the first one.
     *
     * @param table a Table
     * @param dbField the given dbField
     * @return an array where the first value is target table's name or alias and the
     *         second is the dbField's column label
     */
    private static String[] getTableAndFieldArg(final Table table, final DbField dbField)
    {
        if(table.hasColumnEquals(dbField))
        {
            String[] arg = new String[2];
            arg[0] = table.getNameOrAliasIfExisted();
            arg[1] = dbField.getColumnLabel();
            return arg;
        }
        Map<String, Table> refTables = table.getReferencedTableMapping();
        for(Table refTable : refTables.values())
        {
            String[] arg = getTableAndFieldArg(refTable, dbField);
            if(arg != null) return arg;
        }
        return null;
    }

    private static Condition getIdCondition(Class<?> c, Number id) {
        String idLabel = CacheUtil.get(c).getIdLabel();
        Clause clause = new Clause.Equal(new SimpleDbField(c, idLabel), id);

        return new SimpleCondition(clause);
    }

    private static String parseToSQL(Parsable parsable, String[] arg)
    {
        String pattern = parsable.pattern();
        MessageFormat messageFormat = new MessageFormat(pattern);
        return messageFormat.format(arg);
    }

    private static String clearIllegalCharacters(final String s)
    {
        return s.replaceAll(".*([';]+|(--)+).*", "");
    }
}
