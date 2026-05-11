package com.afkanerd.smswithoutborders.payload_specs.payload.extensions

fun Byte.isBitOn(index: Int): Boolean {
    require(index in 0..7) { "Index must be between 0 and 7" }

    return (this.toInt() and (1 shl index)) != 0
}

fun Byte.turnBitOn(index: Int): Byte {
    require(index in 0..7) { "Index must be between 0 and 7" }

    return (this.toInt() or (1 shl index)).toByte()
}

fun Byte.getBits(start: Int, end: Int): Int {
    require(start in 0..7 && end in 0..7 && start <= end) {
        "Indices must be between 0 and 7, and start must be less than or equal to end"
    }

    val mask = ((1 shl (end - start + 1)) - 1) shl start
    return (this.toInt() and mask) ushr start
}
