package com.logpie.dba.dao;

import com.logpie.dba.support.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Sort implements Iterable<Sort.Order> {

    private static final Direction DEFAULT_DIRECTION = Direction.ASC;
    private final List<Order> orders;

    public Sort(Direction direction, DbField... dbFields)
    {
        Assert.notNull(dbFields, "Keys must not be null");
        direction = direction == null ? DEFAULT_DIRECTION : direction;

        orders = new ArrayList<>(dbFields.length);
        for (DbField dbField : dbFields)
        {
            Assert.notNull(dbField, "DbField must not be null");
            this.orders.add(new Order(direction, dbField));
        }
    }

    private Sort(List<Order> orders)
    {
        Assert.notNull(orders, "Orders must not be null");
        this.orders = new ArrayList<>(orders);
    }

    /**
     * @param orders
     * @return a new Sort instance for the given order List
     */
    public static Sort by(List<Order> orders)
    {
        return new Sort(orders);
    }

    /**
     * @param sort
     * @return a new Sort consisting of the Sort.Order of the current Sort
     * combined with the given ones
     */
    public Sort and(Sort sort)
    {
        if (sort == null)
        {
            return this;
        }
        List<Order> these = new ArrayList<>(orders);
        for (Order order : sort)
        {
            these.add(order);
        }
        return Sort.by(these);
    }

    /**
     * @param key
     * @return the order registered for the given key
     */
    public Order getOrderFor(String key)
    {
        for (Order order : orders)
        {
            if (order.getDbField().getColumnLabel().equals(key))
            {
                return order;
            }
        }
        return null;
    }

    @Override
    public Iterator<Order> iterator()
    {
        return this.orders.iterator();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (!(obj instanceof Sort))
        {
            return false;
        }

        Sort that = (Sort) obj;
        return this.orders.equals(that.orders);
    }

    @Override
    public int hashCode()
    {
        int res = 17;
        res = 31 * res + orders.hashCode();
        return res;
    }

    public static class Order implements Parsable {
        private Direction direction;
        private DbField dbField;

        public Order(Direction direction, DbField dbField)
        {
            this.direction = direction == null ? Direction.ASC : direction;
            this.dbField = dbField;
        }

        public Direction getDirection()
        {
            return direction;
        }

        public DbField getDbField()
        {
            return dbField;
        }

        public boolean isAscending()
        {
            return this.direction.isAscending();
        }

        @Override
        public String pattern()
        {
            Assert.notNull(dbField, "DbField must not be null");
            String pattern = dbField.pattern();
            pattern += " " + direction.toString();
            return pattern;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
            {
                return true;
            }

            if (!(obj instanceof Order))
            {
                return false;
            }

            Order that = (Order) obj;
            return this.direction.equals(that.direction) && this.dbField.equals(that.dbField);
        }

        @Override
        public int hashCode()
        {
            int res = 17;
            res = 31 * res + direction.hashCode();
            res = 31 * res + dbField.hashCode();
            return res;
        }
    }

    public static enum Direction {
        ASC, DESC;

        public boolean isAscending()
        {
            return this.equals(ASC);
        }

        public static Direction fromString(String value)
        {
            return Direction.valueOf(value.toLowerCase(Locale.US));
        }
    }

}
