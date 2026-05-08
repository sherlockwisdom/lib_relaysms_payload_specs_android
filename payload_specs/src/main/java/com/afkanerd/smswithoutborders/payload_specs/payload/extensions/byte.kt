package com.afkanerd.smswithoutborders.payload_specs.payload.extensions

fun Byte.isBitOn(index: Int): Boolean {
    if (index !in 0..7) throw Exception("Exceeded max octet index")

    return (this.toInt() and (1 shl index)) != 0
}

fun Byte.turnBitOn(index: Int): Byte {
    if (index !in 0..7) throw Exception("Exceeded max octet index")

    return (this.toInt() or (1 shl index)).toByte()
}
