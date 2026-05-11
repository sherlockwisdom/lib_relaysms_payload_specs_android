package com.afkanerd.smswithoutborders.payload_specs.payload.contents

import com.afkanerd.smswithoutborders.payload_specs.payload.Contents
import com.afkanerd.smswithoutborders.payload_specs.payload.extensions.isBitOn
import com.afkanerd.smswithoutborders.payload_specs.payload.extensions.turnBitOn

/**
 *
 */
class Email(private val data: ByteArray? = null) : Contents() {
    var iFrom: Boolean = false
    var iSubject: Boolean = false
    var lenFrom: Int = 0
    var lenTo: Int = 0
    var lenSubject: Int = 0
    var lenBody: Int = 0
    var from: String? = null
    var to: String? = null
    var subject: String? = null
    var body: String? = null

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun serialize(): ByteArray {
        var emailContent = byteArrayOf()

        var indicator: Byte = 0

        lenTo = to?.toByteArray()?.size ?: 0
        if(lenTo.toUInt() > 32u) throw Exception("TO length > 32 - $lenTo")

        lenBody = body?.toByteArray()?.size ?: 0
        if(lenBody.toUInt() > 560u) throw Exception("BODY length > 560 - $lenBody")

        if(!subject.isNullOrEmpty()) {
            iSubject = true
            lenSubject = subject!!.toByteArray().size
            if(lenSubject.toUInt() > 32u) throw Exception("SUBJECT length > 32 - $lenSubject")
            indicator = 1
        }

        if(!from.isNullOrEmpty()) {
            iFrom = true
            lenFrom = from!!.toByteArray().size
            if(lenFrom > 32) throw Exception("FROM length > 32 - $lenFrom")
            indicator = indicator.turnBitOn(3)
        }
        emailContent[0] = indicator

        var slidingIndex = 0

        if(iFrom) emailContent[++slidingIndex] = lenFrom.toByte()
        emailContent[++slidingIndex] = lenTo.toByte()
        if(iSubject) emailContent[++slidingIndex] = lenSubject.toByte()
        emailContent[++slidingIndex] = lenBody.toByte()

        if(iFrom) emailContent = emailContent.plus(from!!.toByteArray())
        emailContent = emailContent.plus(to!!.toByteArray())
        if(iSubject) emailContent = emailContent.plus(subject!!.toByteArray())
        emailContent = emailContent.plus(body!!.toByteArray())

        return emailContent
    }

    init {
        data?.let {
            var slidingIndex = 0

            try {
                val indicators = data[0]
                val isSubject = indicators.isBitOn(0)
                val isFrom = indicators.isBitOn(1)

                val lenFrom = if(isFrom) data[++slidingIndex] else 0
                if(lenFrom > 32) throw Exception("FROM length > 32 - $lenFrom")

                val lenTo = data[++slidingIndex]
                if(lenTo.toUInt() > 32u) throw Exception("TO length > 32 - $lenTo")

                val lenSubject = if(isSubject) data[++slidingIndex] else 0
                if(lenSubject.toUInt() > 32u) throw Exception("SUBJECT length > 32 - $lenSubject")

                val lenBody = data[++slidingIndex]
                if(lenBody.toUInt() > 560u) throw Exception("BODY length > 560 - $lenBody")

                val from = if (isFrom) String(
                    data,
                    ++slidingIndex,
                    lenFrom.toInt()
                ).also { slidingIndex += lenFrom.toInt() - 1 } else null

                val to = String(
                    data,
                    ++slidingIndex,
                    lenTo.toInt()
                ).also { slidingIndex += lenTo.toInt() - 1 }

                val subject = if (isSubject) String(
                    data,
                    ++slidingIndex,
                    lenSubject.toInt()
                ).also { slidingIndex += lenSubject.toInt() - 1 } else null

                val body = String(
                    data,
                    ++slidingIndex,
                    lenBody.toInt()
                )

                this.iFrom = isFrom
                this.iSubject = isSubject
                this.lenFrom = lenFrom.toInt()
                this.lenTo = lenTo.toInt()
                this.lenSubject = lenSubject.toInt()
                this.lenBody = lenBody.toInt()
                this.from = from
                this.to = to
                this.subject = subject
                this.body = body

            } catch (e: IndexOutOfBoundsException) {
                e.printStackTrace()
                throw e
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }
}
