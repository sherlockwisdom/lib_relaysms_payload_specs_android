package com.afkanerd.smswithoutborders.payload_specs.payload.extensions

fun bytesToShortLittleEndian(b1: Byte, b2: Byte): Short {
    // b1 is the low byte, b2 is the high byte
    val low = b1.toInt() and 0xFF
    val high = b2.toInt() and 0xFF

    return ((high shl 8) or low).toShort()
}
