package com.health.vita.core.utils.error_management


/***
 * This class ensures that an event is managed once.
 */
class EventOnceManager <out T> (private val content: T) {

    private var hasBeenHandled = false

    /***
     * Return the content of a event only when the event haven't been resolved
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /***
     * Return the content
     */
    fun peekContent(): T? = content
}