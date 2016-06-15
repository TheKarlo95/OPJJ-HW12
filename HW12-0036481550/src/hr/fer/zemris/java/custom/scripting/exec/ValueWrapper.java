package hr.fer.zemris.java.custom.scripting.exec;

import java.util.function.BiFunction;

/**
 * {@code ValueWrapper} is a class used for wrapping various objects.
 * <p>
 * This class provides methods for simple arithmetic operations and comparison
 * between various objects.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class ValueWrapper {

    /**
     * Value to wrapped in this {@code ValueWrapper} object.
     */
    private Object value;

    /**
     * Constructs an empty {@code ValueWrapper} with the specified value.
     * 
     * @param value
     *            value to be wrapped in this {@code ValueWrapper} object
     */
    public ValueWrapper(Object value) {
        this.value = value;
    }

    /**
     * Increment value stored in this object with {@code incValue} if
     * {@code incValue} is instance of {@code Integer}, {@code Double} or
     * {@link String} which can be parsed to {@code int} or {@code double}.
     * 
     * @param incValue
     *            object to increment with
     * @return this
     * @throws RuntimeException
     *             if the specified {@code incValeu} isn't instance of
     *             {@code Integer}, {@code Double} or {@link String} which can
     *             be parsed to {@code int} or {@code double}.
     */
    public ValueWrapper increment(Object incValue) {
        value = doOperation(incValue, (n1, n2) -> new Double(n1.doubleValue() + n2.doubleValue()));

        return this;
    }

    /**
     * Decrement value stored in this object with {@code decValue} if
     * {@code decValue} is instance of {@code Integer}, {@code Double} or
     * {@link String} which can be parsed to {@code int} or {@code double}.
     * 
     * @param decValue
     *            object to decrement with
     * @return this
     * @throws RuntimeException
     *             if the specified {@code incValeu} isn't instance of
     *             {@code Integer}, {@code Double} or {@link String} which can
     *             be parsed to {@code int} or {@code double}.
     */
    public ValueWrapper decrement(Object decValue) {
        value = doOperation(decValue, (n1, n2) -> new Double(n1.doubleValue() - n2.doubleValue()));

        return this;
    }

    /**
     * Multiply value stored in this object with {@code mulValue} if
     * {@code mulValue} is instance of {@code Integer}, {@code Double} or
     * {@link String} which can be parsed to {@code int} or {@code double}.
     * 
     * @param mulValue
     *            object to multiply with
     * @return this
     * @throws RuntimeException
     *             if the specified {@code incValeu} isn't instance of
     *             {@code Integer}, {@code Double} or {@link String} which can
     *             be parsed to {@code int} or {@code double}.
     */
    public ValueWrapper multiply(Object mulValue) {
        value = doOperation(mulValue, (n1, n2) -> new Double(n1.doubleValue() * n2.doubleValue()));

        return this;
    }

    /**
     * Divide value stored in this object with {@code divValue} if
     * {@code divValue} is instance of {@code Integer}, {@code Double} or
     * {@link String} which can be parsed to {@code int} or {@code double}.
     * 
     * @param divValue
     *            object to divide with
     * @return this
     * @throws RuntimeException
     *             if the specified {@code incValeu} isn't instance of
     *             {@code Integer}, {@code Double} or {@link String} which can
     *             be parsed to {@code int} or {@code double}.
     */
    public ValueWrapper divide(Object divValue) {
        value = doOperation(divValue, (n1, n2) -> new Double(n1.doubleValue() / n2.doubleValue()));

        return this;
    }

    /**
     * Compares object stored in this {@code ValueWrapper} object with the
     * specified object for order. Returns a negative integer, zero, or a
     * positive integer as this object is less than, equal to, or greater than
     * the specified object.
     * 
     * @param withValue
     *            the object to be compared
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object
     * @throws RuntimeException
     *             if the specified {@code incValeu} isn't instance of
     *             {@code Integer}, {@code Double} or {@link String} which can
     *             be parsed to {@code int} or {@code double}
     */
    public int numCompare(Object withValue) {
        Double double1 = ((Number) prepareObject(value)).doubleValue();
        Double double2 = ((Number) prepareObject(withValue)).doubleValue();

        return ((Comparable<Double>) double1).compareTo(double2);
    }

    /**
     * Returns the value wrapped in this {@code ValueWrapper} object.
     * 
     * @return the value wrapped in this {@code ValueWrapper} object
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value wrapped in this {@code ValueWrapper} object.
     * 
     * @param value
     *            new value wrapped in this {@code ValueWrapper} object
     */
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (value instanceof Integer) {
            return ((Integer) value).toString();
        } else if (value instanceof Double) {
            return ((Double) value).toString();
        } else if (value instanceof String) {
            return ((String) value).toString();
        } else {
            return value.toString();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ValueWrapper other = (ValueWrapper) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    /**
     * Do operation specified by {@code function} argument on value stored in
     * this object with {@code obj} if {@code obj} is instance of
     * {@code Integer}, {@code Double} or {@link String} which can be parsed to
     * {@code int} or {@code double}.
     * 
     * @param obj
     *            object to do operation specified by function with
     * @param function
     *            function to be performed on value stored in this
     *            {@code ValueWrapper} and specified object
     * @return the result of the operation specified by {@code function}
     * @throws RuntimeException
     *             if the specified {@code incValeu} isn't instance of
     *             {@code Integer}, {@code Double} or {@link String} which can
     *             be parsed to {@code int} or {@code double}
     */
    private Number doOperation(Object obj, BiFunction<Number, Number, Double> function) {
        Object obj1 = prepareObject(value);
        Object obj2 = prepareObject(obj);

        Double output = function.apply((Number) obj1, (Number) obj2);

        if (obj1 instanceof Integer && obj2 instanceof Integer) {
            return output.intValue();
        } else {
            return output;
        }
    }

    /**
     * Parses the string argument as a integer and if that throws
     * {@link NumberFormatException} then it parses the string argument as a
     * double.
     * 
     * @param str
     *            string to be parsed
     * @return either {@code Integer} or {@code Double} representation of
     *         specified string
     * @throws RuntimeException
     *             if string argument cannot be parsed either to integer or
     *             double
     */
    private static Number parseString(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e1) {
            try {
                return Double.parseDouble(str);
            } catch (NumberFormatException e2) {
                throw new RuntimeException(
                        "String argument couldn't be parsed to integer or double!");
            }
        }
    }

    /**
     * Prepares specified object for further processing.
     * <p>
     * For given object it specified object into:
     * <ul>
     * <li>integer value of 0 if null is passed as argument
     * <li>integer if string which can be parsed to integer is passed as
     * argument
     * <li>double if string which can be parsed to double is passed as argument
     * <li>integer and double object stay the same
     * </ul>
     * 
     * @param obj
     *            object to be prepared for further processing.
     * @return object prepared for further processing
     */
    private static Object prepareObject(Object obj) {
        if (obj == null) {
            return new Integer(0);
        } else if (obj instanceof String) {
            return parseString((String) obj);
        } else if (obj instanceof Integer) {
            return (Integer) obj;
        } else if (obj instanceof Double) {
            return (Double) obj;
        } else {
            throw new RuntimeException("Argument isn't of valid type!");
        }
    }
}
