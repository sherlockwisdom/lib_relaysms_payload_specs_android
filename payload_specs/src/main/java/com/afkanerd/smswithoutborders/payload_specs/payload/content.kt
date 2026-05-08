package com.afkanerd.smswithoutborders.payload_specs.payload

abstract class Contents {
    abstract fun serialize(): ByteArray
}