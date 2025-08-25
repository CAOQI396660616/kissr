package com.dubu.common.event

/**
 * Author:v
 * Time:2022/5/11
 */
object EventManager {
    private val eventMap = ArrayList<Event>()

    @JvmStatic
    fun <T : Any> registerDisposable(key: String, callback: EventCallback<T>) {
        synchronized(eventMap) {
            for (i in 0 until eventMap.size) {
                val event = eventMap[i]
                if (event.key == key) {
                    return
                }
            }
            @Suppress("UNCHECKED_CAST")
            eventMap.add(Event(key, callback as EventCallback<Any>, disposable = true))
        }
    }

    @JvmStatic
    fun <T : Any> register(key: String, callback: EventCallback<T>) {
        synchronized(eventMap) {
            for (i in 0 until eventMap.size) {
                val event = eventMap[i]
                if (event.key == key) {
                    return
                }
            }
            @Suppress("UNCHECKED_CAST")
            eventMap.add(Event(key, callback as EventCallback<Any>))
        }
    }

    /**
     * will remove same key register before
     */
    @JvmStatic
    fun <T : Any> registerNew(key: String, callback: EventCallback<T>) {
        synchronized(eventMap) {
            for (i in 0 until eventMap.size) {
                val event = eventMap[i]
                if (event.key == key) {
                    eventMap.removeAt(i)
                    break
                }
            }
            @Suppress("UNCHECKED_CAST")
            eventMap.add(Event(key, callback as EventCallback<Any>))
        }
    }

    /**
     * multiple observers for one key
     */
    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T : Any> registerObservers(key: String, callback: EventCallback<T>) {
        synchronized(eventMap) {
            for (i in 0 until eventMap.size) {
                val event = eventMap[i]
                if (event.key == key) {
                    if (event.eventList == null) {
                        event.eventList = ArrayList()
                    }
                    event.eventList!!.add(callback as EventCallback<Any>)
                    return
                }
            }
            eventMap.add(Event(key).apply {
                eventList = ArrayList()
                eventList!!.add(callback as EventCallback<Any>)
            })
        }
    }

    @JvmStatic
    fun postObservables(key: String, data: Any) {
        synchronized(eventMap) {
            for (e in eventMap) {
                if (e.key == key) {
                    e.eventList?.let {
                        for (o in it) {
                            o.invoke(data)
                        }
                    }
                    return
                }
            }
        }
    }

    @JvmStatic
    fun removeObserver(key: String, callback: EventCallback<*>) {
        synchronized(eventMap) {
            for (e in eventMap) {
                if (e.key == key) {
                    e.eventList?.let {
                        for (cb in it) {
                            if (callback == cb) {
                                it.remove(cb)
                                return
                            }
                        }
                    }
                    return
                }
            }
        }
    }

    @JvmStatic
    fun post(key: String, data: Any) {
        synchronized(eventMap) {
            for (e in eventMap) {
                if (e.key == key) {
                    e.onEvent?.let {
                        it.invoke(data)
                        if (e.disposable) {
                            eventMap.remove(e)
                        }
                    }
                    return
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun postSticky(key: String, data: Any) {
        synchronized(eventMap) {
            for (e in eventMap) {
                if (e.key == key) {
                    if (e.onEvent != null) {
                        e.onEvent!!(data)
                        if (e.disposable) {
                            eventMap.remove(e)
                        }
                    } else {
                        if (e.data == null) {
                            e.data = ArrayList<Any>().apply {
                                add(data)
                            }
                        } else {
                            (e.data as ArrayList<Any>).add(data)
                        }
                    }
                    return
                }
            }
            eventMap.add(Event(key, null, ArrayList<Any>().apply {
                add(data)
            }))
        }
    }


    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun <T : Any> registerSticky(key: String, callback: EventCallback<T>) {
        synchronized(eventMap) {
            for (i in 0 until eventMap.size) {
                val event = eventMap[i]
                if (event.key == key) {
                    (event.data as? ArrayList<T>)?.let {
                        for (d in it) {
                            callback(d)
                        }
                        event.data = null
                    }
                    event.onEvent = callback as EventCallback<Any>
                    return
                }
            }
            eventMap.add(Event(key, callback as EventCallback<Any>))
        }
    }

    @JvmStatic
    fun unregister(key: String) {
        synchronized(eventMap) {
            for (e in eventMap) {
                if (e.key == key) {
                    eventMap.remove(e)
                    return
                }
            }
        }
    }

    fun clear() {
        synchronized(eventMap) {
            eventMap.clear()
        }
    }
}


