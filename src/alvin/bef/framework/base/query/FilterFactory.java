package alvin.bef.framework.base.query;

import java.util.List;

import alvin.bef.framework.base.query.impl.BetweenFilter;
import alvin.bef.framework.base.query.impl.ConstantFilter;
import alvin.bef.framework.base.query.impl.ExistFilter;
import alvin.bef.framework.base.query.impl.InListFilter;
import alvin.bef.framework.base.query.impl.InStringFilter;
import alvin.bef.framework.base.query.impl.NullFilter;
import alvin.bef.framework.base.query.impl.SimpleFilter;
import alvin.bef.framework.base.query.impl.StringFilter;
import alvin.bef.framework.base.query.impl.VariableFilter;

/**
 * 
 * @author Alvin
 *
 */
public class FilterFactory {
    /**
     * Get simple filter by name of property and object value.
     * 
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, Object val) {
        return getSimpleFilter((String) null, name, val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by logic name of bean object, name of property and the
     * value. OR Get simple filter by name of property, value and operator.
     * Since two method has same declaration, we had to check type of parameters.
     * 
     * @param logicName
     *            logic name of bean object or name of property.
     * @param name
     *            name of property or value
     * @param val
     *            value or operator.
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, Object name,
            Object val) {
        if (val instanceof IFilter.FILTER_OPERATOR) {
            return getSimpleFilter((String) null, logicName, name,
                    (IFilter.FILTER_OPERATOR) val);
        } else if (name instanceof String) {
            return getSimpleFilter(logicName, (String) name, val,
                    IFilter.OPERATOR_EQ);
        } else {
            return getSimpleFilter((String) null, logicName, name,
                    IFilter.OPERATOR_EQ);
        }
    }

    /**
     * Get simple filter by name of property and boolean value.
     * 
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, boolean val) {
        return getSimpleFilter(name, val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by logic name of bean object, name of property and
     * boolean value.
     * 
     * @param logicName
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            boolean val) {
        return getSimpleFilter(logicName, name, val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by name of property and byte value.
     * 
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, byte val) {
        return getSimpleFilter(name, (byte) val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by logic name of bean object, name of property and byte
     * value.
     * 
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            byte val) {
        return getSimpleFilter(logicName, name, (byte) val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by name of property and char value.
     * 
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, char val) {
        return getSimpleFilter(name, val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by logic name of bean object, name of property and char
     * value.
     * 
     * @param logicName
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            char val) {
        return getSimpleFilter(logicName, name, val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by name of property and double value.
     * 
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, double val) {
        return getSimpleFilter(name, (double) val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by logic name of bean object, name of property and
     * double value.
     * 
     * @param logicName
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            double val) {
        return getSimpleFilter(logicName, name, (double) val,
                IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by name of property and float value.
     * 
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, float val) {
        return getSimpleFilter(name, (float) val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by logic name of bean object, name of property and
     * float value.
     * 
     * @param logicName
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            float val) {
        return getSimpleFilter(logicName, name, (float) val,
                IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by name of property and int value.
     * 
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, int val) {
        return getSimpleFilter(name, (int) val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by logic name of bean object, name of property and int
     * value.
     * 
     * @param logicName
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name, int val) {
        return getSimpleFilter(logicName, name, (int) val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by name of property and long value.
     * 
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, long val) {
        return getSimpleFilter(name, (long) val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by logic name of bean object, name of property and long
     * value.
     * 
     * @param logicName
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            long val) {
        return getSimpleFilter(logicName, name, (long) val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by name of property and short value.
     * 
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, short val) {
        return getSimpleFilter(name, (short) val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by logic name of bean object, name of property and
     * short value.
     * 
     * @param logicName
     * @param name
     * @param val
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            short val) {
        return getSimpleFilter(name, (short) val, IFilter.OPERATOR_EQ);
    }

    /**
     * Get simple filter by logic name of bean object, name of property, value
     * and operator.
     * 
     * @param logicName
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            Object val, IFilter.FILTER_OPERATOR op) {
        VariableFilter left = new VariableFilter(logicName, name);
        ConstantFilter right = new ConstantFilter(val);
        IFilter ret = new SimpleFilter(op, left, right);
        return ret;
    }

    /**
     * Get simple filter by name of property, boolean value and operator.
     * 
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, boolean val,
            IFilter.FILTER_OPERATOR op) {
        return getSimpleFilter((String) null, name, val, op);
    }

    /**
     * Get simple filter by logic name of bean object, name of property, boolean
     * value and operator.
     * 
     * @param logicName
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            boolean val, IFilter.FILTER_OPERATOR op) {
        VariableFilter left = new VariableFilter(logicName, name);
        ConstantFilter right = new ConstantFilter(new Boolean(val));
        IFilter ret = new SimpleFilter(op, left, right);
        return ret;
    }

    /**
     * Get simple filter by name of property, byte value and operator.
     * 
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, byte val,
            IFilter.FILTER_OPERATOR op) {
        return getSimpleFilter((String) null, name, val, op);
    }

    /**
     * Get simple filter by logic name of bean object, name of property, byte
     * value and operator.
     * 
     * @param logicName
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            byte val, IFilter.FILTER_OPERATOR op) {
        VariableFilter left = new VariableFilter(logicName, name);
        ConstantFilter right = new ConstantFilter(new Byte(val));
        IFilter ret = new SimpleFilter(op, left, right);
        return ret;
    }

    /**
     * Get simple filter by name of property, char value and operator.
     * 
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, char val,
            IFilter.FILTER_OPERATOR op) {
        return getSimpleFilter((String) null, name, val, op);
    }

    /**
     * Get simple filter by logic name of bean object, name of property, char
     * value and operator.
     * 
     * @param logicName
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            char val, IFilter.FILTER_OPERATOR op) {
        VariableFilter left = new VariableFilter(logicName, name);
        ConstantFilter right = new ConstantFilter(new Character(val));
        IFilter ret = new SimpleFilter(op, left, right);
        return ret;
    }

    /**
     * Get simple filter by name of property, double value and operator.
     * 
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, double val,
            IFilter.FILTER_OPERATOR op) {
        return getSimpleFilter((String) null, name, val, op);
    }

    /**
     * Get simple filter by logic name of bean object, name of property, double
     * value and operator.
     * 
     * @param logicName
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            double val, IFilter.FILTER_OPERATOR op) {
        VariableFilter left = new VariableFilter(logicName, name);
        ConstantFilter right = new ConstantFilter(new Double(val));
        IFilter ret = new SimpleFilter(op, left, right);
        return ret;
    }

    /**
     * Get simple filter by name of property, float value and operator.
     * 
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, float val,
            IFilter.FILTER_OPERATOR op) {
        return getSimpleFilter((String) null, name, val, op);
    }

    /**
     * Get simple filter by logic name of bean object, name of property, float
     * value and operator.
     * 
     * @param logicName
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            float val, IFilter.FILTER_OPERATOR op) {
        VariableFilter left = new VariableFilter(logicName, name);
        ConstantFilter right = new ConstantFilter(new Float(val));
        IFilter ret = new SimpleFilter(op, left, right);
        return ret;
    }

    /**
     * Get simple filter by name of property, int value and operator.
     * 
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, int val,
            IFilter.FILTER_OPERATOR op) {
        return getSimpleFilter((String) null, name, val, op);
    }

    /**
     * Get simple filter by logic name of bean object, name of property, int
     * value and operator.
     * 
     * @param logicName
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            int val, IFilter.FILTER_OPERATOR op) {
        VariableFilter left = new VariableFilter(logicName, name);
        ConstantFilter right = new ConstantFilter(new Integer(val));
        IFilter ret = new SimpleFilter(op, left, right);
        return ret;
    }

    /**
     * Get simple filter by name of property, long value and operator.
     * 
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, long val,
            IFilter.FILTER_OPERATOR op) {
        return getSimpleFilter((String) null, name, val, op);
    }

    /**
     * Get simple filter by logic name of bean object, name of property, long
     * value and operator.
     * 
     * @param logicName
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            long val, IFilter.FILTER_OPERATOR op) {
        VariableFilter left = new VariableFilter(logicName, name);
        ConstantFilter right = new ConstantFilter(new Long(val));
        IFilter ret = new SimpleFilter(op, left, right);
        return ret;
    }

    /**
     * Get simple filter by name of property, short value and operator.
     * 
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String name, short val,
            IFilter.FILTER_OPERATOR op) {
        return getSimpleFilter((String) null, name, val, op);
    }

    /**
     * Get simple filter by logic name of bean object, name of property, short
     * value and operator.
     * 
     * @param logicName
     * @param name
     * @param val
     * @param op
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String logicName, String name,
            short val, IFilter.FILTER_OPERATOR op) {
        VariableFilter left = new VariableFilter(logicName, name);
        ConstantFilter right = new ConstantFilter(new Short(val));
        IFilter ret = new SimpleFilter(op, left, right);
        return ret;
    }

    /**
     * Get simple filter by string of condition.
     * 
     * @param strCondition
     * @return IFilter object.
     */
    public static IFilter getSimpleFilter(String strCondition) {
        StringFilter ret = new StringFilter(strCondition);
        return ret;
    }

    /**
     * Get between filter by name of property and two values.
     * 
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String name, Object val1, Object val2) {
        return getBetweenFilter((String) null, name, val1, val2);
    }

    /**
     * Get between filter by logic name of bean object, name of property and two
     * values.
     * 
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String logicName, String name,
            Object val1, Object val2) {
        VariableFilter left = new VariableFilter(logicName, name);
        ConstantFilter right1 = new ConstantFilter(val1);
        ConstantFilter right2 = new ConstantFilter(val2);
        BetweenFilter filter = new BetweenFilter(left, right1, right2);
        return filter;
    }

    /**
     * Get between filter by name of property and two byte values.
     * 
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String name, byte val1, byte val2) {
        return getBetweenFilter(name, new Byte(val1), new Byte(val2));
    }

    /**
     * Get between filter by logic name of bean object, name of property and two
     * byte values.
     * 
     * @param logicName
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String logicName, String name,
            byte val1, byte val2) {
        return getBetweenFilter(logicName, name, new Byte(val1), new Byte(val2));
    }

    /**
     * Get between filter by name of property and two char values.
     * 
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String name, char val1, char val2) {
        return getBetweenFilter(name, new Character(val1), new Character(val2));
    }

    /**
     * Get between filter by logic name of bean object, name of property and two
     * char values.
     * 
     * @param logicName
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String logicName, String name,
            char val1, char val2) {
        return getBetweenFilter(logicName, name, new Character(val1),
                new Character(val2));
    }

    /**
     * Get between filter by name of property and two double values.
     * 
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String name, double val1, double val2) {
        return getBetweenFilter(name, new Double(val1), new Double(val2));
    }

    /**
     * Get between filter by logic name of bean object, name of property and two
     * double values.
     * 
     * @param logicName
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String logicName, String name,
            double val1, double val2) {
        return getBetweenFilter(logicName, name, new Double(val1), new Double(
                val2));
    }

    /**
     * Get between filter by name of property and two float values.
     * 
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String name, float val1, float val2) {
        return getBetweenFilter(name, new Float(val1), new Float(val2));
    }

    /**
     * Get between filter by logic name of bean object, name of property and two
     * float values.
     * 
     * @param logicName
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String logicName, String name,
            float val1, float val2) {
        return getBetweenFilter(logicName, name, new Float(val1), new Float(
                val2));
    }

    /**
     * Get between filter by name of property and two int values.
     * 
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String name, int val1, int val2) {
        return getBetweenFilter(name, new Integer(val1), new Integer(val2));
    }

    /**
     * Get between filter by logic name of bean object, name of property and two
     * int values.
     * 
     * @param logicName
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String logicName, String name,
            int val1, int val2) {
        return getBetweenFilter(logicName, name, new Integer(val1),
                new Integer(val2));
    }

    /**
     * Get between filter by name of property and two long values.
     * 
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String name, long val1, long val2) {
        return getBetweenFilter(name, new Long(val1), new Long(val2));
    }

    /**
     * Get between filter by logic name of bean object, name of property and two
     * long values.
     * 
     * @param logicName
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String logicName, String name,
            long val1, long val2) {
        return getBetweenFilter(logicName, name, new Long(val1), new Long(val2));
    }

    /**
     * Get between filter by name of property and two short values.
     * 
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String name, short val1, short val2) {
        return getBetweenFilter(name, new Short(val1), new Short(val2));
    }

    /**
     * Get between filter by logic name of bean object, name of property and two
     * short values.
     * 
     * @param logicName
     * @param name
     * @param val1
     * @param val2
     * @return IFilter object.
     */
    public static IFilter getBetweenFilter(String logicName, String name,
            short val1, short val2) {
        return getBetweenFilter(logicName, name, new Short(val1), new Short(
                val2));
    }

    /**
     * Get in filter by name of property and list of values.
     * 
     * @param name
     * @param values
     * @return IFilter object.
     */
    public static IFilter getInFilter(String name, List values) {
        return getInFilter((String) null, name, values);
    }

    /**
     * Get in filter by logic name of bean object, name of property and list of
     * values.
     * 
     * @param name
     * @param values
     * @return IFilter object.
     */
    public static IFilter getInFilter(String logicName, String name, List values) {
        return new InListFilter(new VariableFilter(logicName, name), values,
                false);
    }

    /**
     * Get not in filter by name of property and list of values.
     * 
     * @param name
     * @param values
     * @return IFilter object.
     */
    public static IFilter getNotInFilter(String name, List values) {
        return getNotInFilter((String) null, name, values);
    }

    /**
     * Get not in filter by logic name of bean object, name of property and list
     * of values.
     * 
     * @param logicName
     * @param name
     * @param values
     * @return IFilter object.
     */
    public static IFilter getNotInFilter(String logicName, String name,
            List values) {
        return new InListFilter(new VariableFilter(logicName, name), values,
                true);
    }

    /**
     * Get in filter by name of property and sub select string.
     * 
     * @param name
     * @param selectQuery
     * @return IFilter object.
     */
    public static IFilter getInFilter(String name, String selectQuery) {
        return getInFilter((String) null, name, selectQuery);
    }

    /**
     * Get in filter by logic name of bean object, name of property and sub
     * select string.
     * 
     * @param logicName
     * @param name
     * @param selectQuery
     * @return IFilter object.
     */
    public static IFilter getInFilter(String logicName, String name,
            String selectQuery) {
        return new InStringFilter(new VariableFilter(logicName, name),
                new StringFilter(selectQuery), false);
    }

    /**
     * Get not in filter by name of property and sub select string.
     * 
     * @param name
     * @param selectQuery
     * @return IFilter object.
     */
    public static IFilter getNotInFilter(String name, String selectQuery) {
        return getNotInFilter((String) null, name, selectQuery);
    }

    /**
     * Get not in filter by logic name of bean object, name of property and sub
     * select string.
     * 
     * @param logicName
     * @param name
     * @param selectQuery
     * @return IFilter object.
     */
    public static IFilter getNotInFilter(String logicName, String name,
            String selectQuery) {
        return new InStringFilter(new VariableFilter(logicName, name),
                new StringFilter(selectQuery), true);
    }

    /**
     * Get exist filter by select query string.
     * 
     * @param selectQuery
     * @return IFilter object.
     */
    public static IFilter getExistFilter(String selectQuery) {
        return new ExistFilter(new StringFilter(selectQuery), false);
    }

    /**
     * Get not exist filter by select query string.
     * 
     * @param selectQuery
     * @return IFilter object.
     */
    public static IFilter getNotExistFilter(String selectQuery) {
        return new ExistFilter(new StringFilter(selectQuery), true);
    }

    /**
     * Get is null filter by name of property.
     * 
     * @param name
     * @return IFilter object.
     */
    public static IFilter getIsNullFilter(String name) {
        return getIsNullFilter((String) null, name);
    }

    /**
     * Get is null filter by logic name of bean object and name of property.
     * 
     * @param logicName
     * @param name
     * @return IFilter object.
     */
    public static IFilter getIsNullFilter(String logicName, String name) {
        return new NullFilter(logicName, name, false);
    }

    /**
     * Get not null filter by name of property.
     * 
     * @param selectQuery
     * @return IFilter object.
     */
    public static IFilter getIsNotNullFilter(String name) {
        return getIsNotNullFilter((String) null, name);
    }

    /**
     * Get not null filter by logic name of bean object and name of property.
     * 
     * @param logicName
     * @param selectQuery
     * @return IFilter object.
     */
    public static IFilter getIsNotNullFilter(String logicName, String name) {
        return new NullFilter(logicName, name, true);
    }
}
