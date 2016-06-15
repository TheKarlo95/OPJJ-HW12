package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code ObjectMultistack} is a class that maps keys to stacks in which it
 * stores the values.
 * <p>
 * That way you can store multiple values with single key and not overwrite
 * them. Values in the same slot are all stored in the same stack.
 * 
 * @author Karlo Vrbić
 * @version 1.0
 */
public class ObjectMultistack {

	/**
	 * The initial used when none specified in constructor.
	 */
	private static final int DEFAULT_INITIAL_CAPACITY = 16;

	/**
	 * The load factor used when none specified in constructor.
	 */
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * Map of stack entries which contain values stored in this
	 * {@code ObjectMultistack} object
	 */
	private Map<String, MultistackEntry<ValueWrapper>> map;

	/**
	 * Constructs an empty {@code ObjectMultistack} with the specified initial
	 * capacity and load factor.
	 *
	 * @param initialCapacity
	 *            the initial capacity
	 * @param loadFactor
	 *            the load factor
	 * @throws IllegalArgumentException
	 *             if the initial capacity is negative or the load factor is
	 *             non-positive
	 */
	public ObjectMultistack(int initialCapacity, float loadFactor) {
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
		}
		if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
			throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
		}

		this.map = new HashMap<>(initialCapacity, loadFactor);
	}

	/**
	 * Constructs an empty {@code ObjectMultistack} with the specified initial
	 * capacity and the default load factor {@value #DEFAULT_LOAD_FACTOR}.
	 *
	 * @param initialCapacity
	 *            the initial capacity.
	 * @throws IllegalArgumentException
	 *             if the initial capacity is negative.
	 */
	public ObjectMultistack(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * Constructs an empty {@code ObjectMultistack} with the default initial
	 * capacity {@value #DEFAULT_INITIAL_CAPACITY} and the default load factor
	 * {@value #DEFAULT_LOAD_FACTOR}.
	 */
	public ObjectMultistack() {
		this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * Pushes an item onto the top of a stack at slot specified by the
	 * {@code name} argument.
	 * 
	 * @param name
	 *            the key with which the specified stack is to be associated
	 * @param valueWrapper
	 *            value to be associated with the stack specified by key
	 * @throws IllegalArgumentException
	 *             if one of the arguments({@code name} or {@code valueWrapper})
	 *             is null
	 */
	public void push(String name, ValueWrapper valueWrapper) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null reference!");
		}

		if (valueWrapper == null) {
			throw new IllegalArgumentException("Value wrapper cannot be null reference!");
		}

		if (isEmpty(name.trim())) {
			map.put(name.trim(), new MultistackEntry<ValueWrapper>(valueWrapper, null));
		} else {
			MultistackEntry<ValueWrapper> entry = map.get(name.trim());

			while (entry.next != null) {
				entry = entry.next;
			}

			entry.next = new MultistackEntry<ValueWrapper>(valueWrapper, null);
		}
	}

	/**
	 * Removes the object at the top of the stack specified by {@code name}
	 * argument and returns that object as the value of this function.
	 * 
	 * @param name
	 *            the key with which the specified stack is to be associated
	 * @return the object at the top of the stack specified by {@code name}
	 *         argument
	 * @throws IllegalArgumentException
	 *             if {@code name} argument is null
	 */
	public ValueWrapper pop(String name) {
		return popOrPeek(name.trim(), true);
	}

	/**
	 * Returns the object at the top of the stack specified by {@code name}
	 * argument. Contrary to {@link #pop(String)} this method doesn't remove the
	 * object form the top.
	 * 
	 * @param name
	 *            the key with which the specified stack is to be associated
	 * @return the object at the top of the stack specified by {@code name}
	 *         argument
	 * @throws IllegalArgumentException
	 *             if {@code name} argument is null
	 */
	public ValueWrapper peek(String name) {
		return popOrPeek(name.trim(), false);
	}

	/**
	 * If {@code delete} argument is set to {code true} it removes the object at
	 * the top of the stack specified by {@code name} argument and if set to
	 * {@code false} it doesn't remove it. For any value of {@code delete} it
	 * returns that object as the value of this function.
	 * 
	 * @param name
	 *            the key with which the specified stack is to be associated
	 * @param delete
	 *            specifies if the method should delete the object at the top of
	 *            stack if set to {@code true}; if set to {@code false} method
	 *            will not remove object at the top
	 * @return the object at the top of the stack specified by {@code name}
	 *         argument
	 * @throws IllegalArgumentException
	 *             if {@code name} argument is null
	 */
	private ValueWrapper popOrPeek(String name, boolean delete) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null reference!");
		}

		MultistackEntry<ValueWrapper> previous = null;
		MultistackEntry<ValueWrapper> entry = map.get(name);

		while (entry.next != null) {
			previous = entry;
			entry = entry.next;
		}

		ValueWrapper value = entry.value;

		// for pop delete and for peek
		if (delete) {
			if (previous == null) {
				map.remove(name);
			} else {
				previous.next = null;
			}
		}

		return value;
	}

	/**
	 * Tests if this stack specified by {@code name} argument is empty.
	 *
	 * @param name
	 *            the key with which the specified stack is to be associated
	 * @return {@code true} if and only if this stack contains no items;
	 *         {@code false} otherwise.
	 */
	public boolean isEmpty(String name) {
		return map.get(name) == null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		
		for(MultistackEntry<ValueWrapper> e : map.values()) {
			sb.append("Slot " + i++ + ": ");
			while (e.next != null) {
				sb.append(e);
				sb.append(", ");
				e = e.next;
			}
			
			sb.append(e);
			sb.append("%n");
		}
		
		return sb.toString();
	}

	/**
	 * Class used as node for stack.
	 * 
	 * @author Karlo Vrbić
	 * @version 1.0
	 * @param <T>
	 *            type of value stored in this {@code MultistackEntry}
	 */
	private static class MultistackEntry<T> {
		/**
		 * Reference to the next entry in stack
		 */
		private MultistackEntry<T> next;

		/**
		 * Value stored in this entry
		 */
		private T value;

		/**
		 * Constructs a node with specified data and references to previous and
		 * next node.
		 * 
		 * @param value
		 *            value stored in this entry
		 * @param next
		 *            reference to the next entry in stack
		 */
		private MultistackEntry(T value, MultistackEntry<T> next) {
			this.next = next;
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value.toString();
		}
	}
}
