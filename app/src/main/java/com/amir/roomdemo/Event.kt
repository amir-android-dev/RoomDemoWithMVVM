package com.amir.roomdemo

//it is a boilerplate code, we can reuse it for all our android projects
//This class will be used as a wrapper for data that is exposed via a LiveData that represents an event.
// You don’t have to remember this code, you can just copy this class for all your Android projects.
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}