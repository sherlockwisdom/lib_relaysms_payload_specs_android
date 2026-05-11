package com.afkanerd.smswithoutborders.payload_specs.payload

abstract class Category {
    abstract fun serialize(): ByteArray
}