package alvin.bef.framework.base.query;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * @author Alvin
 *
 */
public interface IFilter extends Serializable {
    public static final FILTER_OPERATOR OPERATOR_NONE = FILTER_OPERATOR.OPERATOR_NONE;

    public static final FILTER_OPERATOR OPERATOR_LIKE = FILTER_OPERATOR.OPERATOR_LIKE;

    public static final FILTER_OPERATOR OPERATOR_EQ = FILTER_OPERATOR.OPERATOR_EQ;

    public static final FILTER_OPERATOR OPERATOR_NOT_EQ = FILTER_OPERATOR.OPERATOR_NOT_EQ;

    public static final FILTER_OPERATOR OPERATOR_GREATER_THAN = FILTER_OPERATOR.OPERATOR_GREATER_THAN;

    public static final FILTER_OPERATOR OPERATOR_LESS_THEN = FILTER_OPERATOR.OPERATOR_LESS_THEN;

    public static final FILTER_OPERATOR OPERATOR_GREATER_EQ = FILTER_OPERATOR.OPERATOR_GREATER_EQ;

    public static final FILTER_OPERATOR OPERATOR_LESS_EQ = FILTER_OPERATOR.OPERATOR_LESS_EQ;

    public static final FILTER_OPERATOR OPERATOR_IN = FILTER_OPERATOR.OPERATOR_IN;

    public static final FILTER_OPERATOR OPERATOR_NOT_IN = FILTER_OPERATOR.OPERATOR_NOT_IN;

    public static final FILTER_OPERATOR OPERATOR_AND = FILTER_OPERATOR.OPERATOR_AND;

    public static final FILTER_OPERATOR OPERATOR_OR = FILTER_OPERATOR.OPERATOR_OR;

    public static final FILTER_OPERATOR OPERATOR_BETWEEN = FILTER_OPERATOR.OPERATOR_BETWEEN;

    public static final FILTER_OPERATOR OPERATOR_EXISTS = FILTER_OPERATOR.OPERATOR_EXISTS;

    public static final FILTER_OPERATOR OPERATOR_NOT_EXISTS = FILTER_OPERATOR.OPERATOR_NOT_EXISTS;

    public static final FILTER_OPERATOR OPERATOR_NULL = FILTER_OPERATOR.OPERATOR_NULL;

    public static final FILTER_OPERATOR OPERATOR_NOT_NULL = FILTER_OPERATOR.OPERATOR_NOT_NULL;

    public static final int TYPE_CONSTANT = 1;

    public static final int TYPE_FILTER = 2;

    public static final String DEFAULT_ALIAS = "_default_";

    public int getFilterType();

    public String getString();

    public List getValues();

    public List getFields();

    public IFilter appendAnd(IFilter filter);

    public IFilter appendOr(IFilter filter);

    static class FILTER_OPERATOR {
        private static final int NONE = 0;

        private static final FILTER_OPERATOR OPERATOR_NONE = new FILTER_OPERATOR(
                FILTER_OPERATOR.NONE);

        private static final int LIKE = 1;

        private static final FILTER_OPERATOR OPERATOR_LIKE = new FILTER_OPERATOR(
                FILTER_OPERATOR.LIKE);

        private static final int EQ = 2;

        private static final FILTER_OPERATOR OPERATOR_EQ = new FILTER_OPERATOR(
                FILTER_OPERATOR.EQ);

        private static final int NOT_EQ = 3;

        private static final FILTER_OPERATOR OPERATOR_NOT_EQ = new FILTER_OPERATOR(
                FILTER_OPERATOR.NOT_EQ);

        private static final int GREATER_THAN = 4;

        private static final FILTER_OPERATOR OPERATOR_GREATER_THAN = new FILTER_OPERATOR(
                FILTER_OPERATOR.GREATER_THAN);

        private static final int LESS_THEN = 5;

        private static final FILTER_OPERATOR OPERATOR_LESS_THEN = new FILTER_OPERATOR(
                FILTER_OPERATOR.LESS_THEN);

        private static final int GREATER_EQ = 6;

        private static final FILTER_OPERATOR OPERATOR_GREATER_EQ = new FILTER_OPERATOR(
                FILTER_OPERATOR.GREATER_EQ);

        private static final int LESS_EQ = 7;

        private static final FILTER_OPERATOR OPERATOR_LESS_EQ = new FILTER_OPERATOR(
                FILTER_OPERATOR.LESS_EQ);

        private static final int IN = 8;

        private static final FILTER_OPERATOR OPERATOR_IN = new FILTER_OPERATOR(
                FILTER_OPERATOR.IN);

        private static final int NOT_IN = 9;

        private static final FILTER_OPERATOR OPERATOR_NOT_IN = new FILTER_OPERATOR(
                FILTER_OPERATOR.NOT_IN);

        private static final int AND = 10;

        private static final FILTER_OPERATOR OPERATOR_AND = new FILTER_OPERATOR(
                FILTER_OPERATOR.AND);

        private static final int OR = 11;

        private static final FILTER_OPERATOR OPERATOR_OR = new FILTER_OPERATOR(
                FILTER_OPERATOR.OR);

        private static final int BETWEEN = 12;

        private static final FILTER_OPERATOR OPERATOR_BETWEEN = new FILTER_OPERATOR(
                FILTER_OPERATOR.BETWEEN);

        private static final int EXISTS = 13;

        private static final FILTER_OPERATOR OPERATOR_EXISTS = new FILTER_OPERATOR(
                FILTER_OPERATOR.EXISTS);

        private static final int NOT_EXISTS = 14;

        private static final FILTER_OPERATOR OPERATOR_NOT_EXISTS = new FILTER_OPERATOR(
                FILTER_OPERATOR.NOT_EXISTS);

        private static final int NULL = 15;

        private static final FILTER_OPERATOR OPERATOR_NULL = new FILTER_OPERATOR(
                FILTER_OPERATOR.NULL);

        private static final int NOT_NULL = 16;

        private static final FILTER_OPERATOR OPERATOR_NOT_NULL = new FILTER_OPERATOR(
                FILTER_OPERATOR.NOT_NULL);

        private FILTER_OPERATOR(int operator) {
            this.operator = operator;
        }

        private int operator;

        public int getOperator() {
            return operator;
        }
    }
}
