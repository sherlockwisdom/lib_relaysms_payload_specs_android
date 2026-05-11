package com.afkanerd.smswithoutborders.payload_specs.payload.categories

import android.util.Log.e
import com.afkanerd.smswithoutborders.payload_specs.payload.Category
import com.afkanerd.smswithoutborders.payload_specs.payload.Contents
import com.afkanerd.smswithoutborders.payload_specs.payload.extensions.bytesToShortLittleEndian
import com.afkanerd.smswithoutborders.payload_specs.payload.extensions.getBits
import com.afkanerd.smswithoutborders.payload_specs.payload.extensions.getBitsDownTo
import com.afkanerd.smswithoutborders.payload_specs.payload.extensions.isBitOn
import com.afkanerd.smswithoutborders.payload_specs.payload.extensions.turnBitOn

class Image(private val data: ByteArray? = null) : Category() {
    var iLi: Boolean = false
    var iPl: Boolean = false
    var iLEh: Boolean = false
    var sessId: Int? = null
    var iEncH: Boolean = false
    var segNum: Int? = null
    var lenImage: Short = 0
    var pl: Int? = null
    var lenEh: Int? = 0
    var encHeader: ByteArray? = null
    var image: ByteArray? = null
    var content: Contents? = null

    override fun serialize(): ByteArray {
        TODO()
    }

    init {
        data?.let {
            try {
                val indicators: Byte = data[0]
                this.sessId = indicators.getBits(0, 4)
                this.iLEh = indicators.isBitOn(5)
                this.iPl = indicators.isBitOn(6)
                this.iLi = indicators.isBitOn(7)

                var offset = 1
                this.segNum = data[offset].getBits(0, 6)
                this.iEncH = data[offset].isBitOn(7)

                if(iLi) this.lenImage = bytesToShortLittleEndian(
                    data[++offset],
                    data[++offset]
                ).getBitsDownTo(13)

                if (iPl) this.pl = data[offset].getBits(12, 15)

                if(iLEh) {
                    this.lenEh = data[++offset].toInt()
                    if (iEncH) {
                        this.encHeader = data.sliceArray(++offset until offset + (lenEh ?: 0))
                        offset += (lenEh ?: 0) - 1
                    }
                }


                if (offset + 1 < data.size) {
                    this.image = data.sliceArray(++offset until data.size)
                }

//                this.content = Contents()
            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
                throw e
            } catch(e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }
}