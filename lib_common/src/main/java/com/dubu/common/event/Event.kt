package com.dubu.common.event


class Event(
    val key: String,
    var onEvent: EventCallback<Any>? = null,
    var data: Any? = null,
    val disposable: Boolean = false,
    var eventList: ArrayList<EventCallback<Any>>? = null
)

typealias EventCallback<T> = (T) -> Unit