package com.jiamian.translation.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jiamian.translation.common.exception.BOException;
import com.jiamian.translation.common.exception.BizUnshowException;

import java.util.Collection;
import java.util.Map;

public abstract class AssertBOUtil {

	public static void isTrue(boolean expression, String message) {
		isTrue(expression, message, true);
	}

	/**
	 * 表达式成立， 就跑错
	 *
	 * @param expression
	 * @param message
	 * @param isShow
	 */
	public static void isTrue(boolean expression, String message,
	                          boolean isShow) {
		if (!expression) {
			if (isShow) {
				throw new BOException(message);
			} else {
				throw new BizUnshowException(message);
			}
		}
	}

	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new BOException(message);
		}
	}

	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	public static void notNull(Object object, String message) {
		notNull(object, message, true);
	}

	public static void show(String message) {
		throw new BOException(message);
	}

	public static void notNull(Object object, String message, boolean isShow) {
		if (object == null) {
			if (isShow) {
				throw new BOException(message);
			} else {
				throw new BizUnshowException(message);
			}
		}
	}

	public static void isBlank(String text, String message) {
		if (StrUtil.isNotBlank(text)) {
			throw new BOException(message);
		}
	}

	public static void notBlank(String text, String message) {
		if (StrUtil.isBlank(text)) {
			throw new BOException(message);
		}
	}

	public static void notBlank(Long id, String message) {
		if (id == null || id == 0) {
			throw new BOException(message);
		}
	}

	public static void notBlank(Integer id, String message) {
		if (id == null || id == 0) {
			throw new BOException(message);
		}
	}

	public static void notNull(Object object) {
		notNull(object,
				"[Assertion failed] - this argument is required; it must not be null");
	}

	public static void hasLength(String text, String message) {
		if (!StrUtil.isNotBlank(text)) {
			throw new BOException(message);
		}
	}

	public static void hasLength(String text) {
		hasLength(text,
				"[Assertion failed] - this String argument must have length; it must not be null or empty");
	}

	public static void hasText(String text, String message) {
		hasText(text, message, true);
	}

	public static void hasText(String text, String message, boolean isShow) {
		if (!StrUtil.isNotBlank(text)) {
			if (isShow) {
				throw new BOException(message);
			} else {
				throw new BizUnshowException(message);
			}
		}
	}

	public static void hasText(String text) {
		hasText(text,
				"[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	public static void doesNotContain(String textToSearch, String substring,
	                                  String message) {
		if (StrUtil.isNotBlank(textToSearch) && StrUtil.isNotBlank(substring)
				&& textToSearch.contains(substring)) {
			throw new BOException(message);
		}
	}

	/**
	 * Assert that the given text does not contain the given substring.
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.doesNotContain(name, &quot;rod&quot;);
	 * </pre>
	 *
	 * @param textToSearch the text to search
	 * @param substring    the substring to find within the text
	 */
	public static void doesNotContain(String textToSearch, String substring) {
		doesNotContain(textToSearch, substring,
				"[Assertion failed] - this String argument must not contain the substring ["
						+ substring + "]");
	}

	/**
	 * Assert that an array has elements; that is, it must not be {@code null}
	 * and must have at least one element.
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.notEmpty(array, &quot;The array must have elements&quot;);
	 * </pre>
	 *
	 * @param array   the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws BOException if the object array is {@code null} or has no elements
	 */
	public static void notEmpty(Object[] array, String message) {
		if (ObjectUtil.isEmpty(array)) {
			throw new BOException(message);
		}
	}

	/**
	 * Assert that an array has elements; that is, it must not be {@code null}
	 * and must have at least one element.
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.notEmpty(array);
	 * </pre>
	 *
	 * @param array the array to check
	 * @throws BOException if the object array is {@code null} or has no elements
	 */
	public static void notEmpty(Object[] array) {
		notEmpty(array,
				"[Assertion failed] - this array must not be empty: it must contain at least 1 element");
	}

	/**
	 * Assert that an array has no null elements. Note: Does not complain if the
	 * array is empty!
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.noNullElements(array, &quot;The array must have non-null elements&quot;);
	 * </pre>
	 *
	 * @param array   the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws BOException if the object array contains a {@code null} element
	 */
	public static void noNullElements(Object[] array, String message) {
		if (array != null) {
			for (Object element : array) {
				if (element == null) {
					throw new BOException(message);
				}
			}
		}
	}

	/**
	 * Assert that an array has no null elements. Note: Does not complain if the
	 * array is empty!
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.noNullElements(array);
	 * </pre>
	 *
	 * @param array the array to check
	 * @throws BOException if the object array contains a {@code null} element
	 */
	public static void noNullElements(Object[] array) {
		noNullElements(array,
				"[Assertion failed] - this array must not contain any null elements");
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * {@code null} and must have at least one element.
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.notEmpty(collection, &quot;Collection must have elements&quot;);
	 * </pre>
	 *
	 * @param collection the collection to check
	 * @param message    the exception message to use if the assertion fails
	 * @throws BOException if the collection is {@code null} or has no elements
	 */
	public static void notEmpty(Collection<?> collection, String message) {
		if (ObjectUtil.isEmpty(collection)) {
			throw new BOException(message);
		}
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * {@code null} and must have at least one element.
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.notEmpty(collection, &quot;Collection must have elements&quot;);
	 * </pre>
	 *
	 * @param collection the collection to check
	 * @throws BOException if the collection is {@code null} or has no elements
	 */
	public static void notEmpty(Collection<?> collection) {
		notEmpty(collection,
				"[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
	}

	/**
	 * Assert that a Map has entries; that is, it must not be {@code null} and
	 * must have at least one entry.
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.notEmpty(map, &quot;Map must have entries&quot;);
	 * </pre>
	 *
	 * @param map     the map to check
	 * @param message the exception message to use if the assertion fails
	 * @throws BOException if the map is {@code null} or has no entries
	 */
	public static void notEmpty(Map<?, ?> map, String message) {
		if (ObjectUtil.isEmpty(map)) {
			throw new BOException(message);
		}
	}

	/**
	 * Assert that a Map has entries; that is, it must not be {@code null} and
	 * must have at least one entry.
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.notEmpty(map);
	 * </pre>
	 *
	 * @param map the map to check
	 * @throws BOException if the map is {@code null} or has no entries
	 */
	public static void notEmpty(Map<?, ?> map) {
		notEmpty(map,
				"[Assertion failed] - this map must not be empty; it must contain at least one entry");
	}

	/**
	 * Assert that the provided object is an instance of the provided class.
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.instanceOf(Foo.class, foo);
	 * </pre>
	 *
	 * @param clazz the required class
	 * @param obj   the object to check
	 * @throws BOException if the object is not an instance of clazz
	 * @see Class#isInstance
	 */
	public static void isInstanceOf(Class<?> clazz, Object obj) {
		isInstanceOf(clazz, obj, "");
	}

	/**
	 * Assert that the provided object is an instance of the provided class.
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.instanceOf(Foo.class, foo);
	 * </pre>
	 *
	 * @param type    the type to check against
	 * @param obj     the object to check
	 * @param message a message which will be prepended to the message produced by
	 *                the function itself, and which may be used to provide context.
	 *                It should normally end in a ": " or ". " so that the function
	 *                generate message looks ok when prepended to it.
	 * @throws BOException if the object is not an instance of clazz
	 * @see Class#isInstance
	 */
	public static void isInstanceOf(Class<?> type, Object obj, String message) {
		notNull(type, "Type to check against must not be null");
		if (!type.isInstance(obj)) {
			throw new BOException(
					(StrUtil.isNotBlank(message) ? message + " " : "")
							+ "Object of class ["
							+ (obj != null ? obj.getClass().getName() : "null")
							+ "] must be an instance of " + type);
		}
	}

	/**
	 * Assert that {@code superType.isAssignableFrom(subType)} is {@code true}.
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.isAssignable(Number.class, myClass);
	 * </pre>
	 *
	 * @param superType the super type to check
	 * @param subType   the sub type to check
	 * @throws BOException if the classes are not assignable
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType) {
		isAssignable(superType, subType, "");
	}

	/**
	 * Assert that {@code superType.isAssignableFrom(subType)} is {@code true}.
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.isAssignable(Number.class, myClass);
	 * </pre>
	 *
	 * @param superType the super type to check against
	 * @param subType   the sub type to check
	 * @param message   a message which will be prepended to the message produced by
	 *                  the function itself, and which may be used to provide context.
	 *                  It should normally end in a ": " or ". " so that the function
	 *                  generate message looks ok when prepended to it.
	 * @throws BOException if the classes are not assignable
	 */
	public static void isAssignable(Class<?> superType, Class<?> subType,
	                                String message) {
		notNull(superType, "Type to check against must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			throw new BOException(
					message + subType + " is not assignable to " + superType);
		}
	}

	/**
	 * Assert a boolean expression, throwing {@code BOException} if the test
	 * result is {@code false}. Call isTrue if you wish to throw BOException on
	 * an assertion failure.
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.state(id == null,
	 * 		&quot;The id property must not already be initialized&quot;);
	 * </pre>
	 *
	 * @param expression a boolean expression
	 * @param message    the exception message to use if the assertion fails
	 * @throws BOException if expression is {@code false}
	 */
	public static void state(boolean expression, String message) {
		if (!expression) {
			throw new BOException(message);
		}
	}

	/**
	 * Assert a boolean expression, throwing {@link BOException} if the test
	 * result is {@code false}.
	 * <p>
	 * Call {@link #isTrue(boolean)} if you wish to throw {@link BOException} on
	 * an assertion failure.
	 * <p>
	 *
	 * <pre class="code">
	 * Assert.state(id == null);
	 * </pre>
	 *
	 * @param expression a boolean expression
	 * @throws BOException if the supplied expression is {@code false}
	 */
	public static void state(boolean expression) {
		state(expression,
				"[Assertion failed] - this state invariant must be true");
	}
}