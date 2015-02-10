package org.obiba.magma

import java.util.{List, Locale}

trait AttributeAware {

    /**
     * Returns true if this instance has at least one {@code Attribute} (with any name).
     *
     * @return true when this instance has at least one { @code Attribute}
     */
    def hasAttributes: Boolean

    /**
     * Returns true if this instance has at least one {@code Attribute} with the specified name.
     *
     * @return true when this instance has at least one { @code Attribute} with the specified name; false otherwise.
     */
    def hasAttribute(name: String): Boolean

    /**
     * Returns true if this instance has at least one {@code Attribute} in the specified {@code namespace} and,
     * optionally, with the specified {@code name}.
     *
     * @param namespace the namespace
     * @param name an optional name
     */
    def hasAttribute(namespace: String, name: String): Boolean

    /**
     * Returns the first attribute associated with the specified name. Note that multiple instances of {@code Attribute}
     * can have the same name. This method will always return the first one.
     *
     * @param name
     * @return
     */

    def getAttribute(name: String): Option[Attribute]

    /**
     * Returns the first attribute associated with the specified name in the specified namespace.
     *
     * @param namespace
     * @param name
     * @return
     */
    def getAttribute(namespace: String, name: String): Option[Attribute]

    def hasAttribute(name: String, locale: Locale): Boolean

    def hasAttribute(namespace: String, name: String, locale: Locale): Boolean

    def getAttribute(name: String, locale: Locale): Option[Attribute]

    def getAttribute(namespace: String, name: String, locale: Locale): Option[Attribute]

    /**
     * Equivalent to calling
     * <p/>
     * <pre>
     * getAttribute(name).getValue()
     *
     * <pre>
     */
    def getAttributeValue(name: String): Option[Value]

    def getAttributeValue(namespace: String, name: String): Option[Value]

    /**
     * Returns the list of attributes associated with the specified name.
     *
     * @param name the key of the attributes to return
     * @return
     */
    def getAttributes(name: String): List[Attribute]

    def getAttributes(namespace: String, name: String): List[Attribute]

    def getNamespaceAttributes(namespace: String): List[Attribute]

    def getAttributes: List[Attribute]
}
