package com.afkanerd.smswithoutborders.payload_specs.payload.extensions

fun Short.getBitsDownTo(n: Int): Short {
    require(n in 0..15) { "Index must be between 0 and 15" }
    val mask = (1 shl (n + 1)) - 1
    return ((this.toInt() and 0xFFFF) and mask).toShort()
}
