package com.afkanerd.smswithoutborders.payload_specs.payload.categories

import com.afkanerd.smswithoutborders.payload_specs.payload.Contents
import com.afkanerd.smswithoutborders.payload_specs.payload.extensions.isBitOn
import com.afkanerd.smswithoutborders.payload_specs.payload.extensions.turnBitOn

/**
 *
 */
class Email(private val data: ByteArray? = null) : Contents() {
    var iFrom: Boolean = false
    var iCc: Boolean = false
    var iBcc: Boolean = false
    var iSubject: Boolean = false
    var lenFrom: Int = 0
    var lenTo: Int = 0
    var lenCc: Int = 0
    var lenBcc: Int = 0
    var lenSubject: Int = 0
    var lenBody: Int = 0
    var from: String? = null
    var to: String? = null
    var cc: String? = null
    var bcc: String? = null
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

        if(!bcc.isNullOrEmpty()) {
            iBcc = true
            lenBcc = bcc!!.toByteArray().size
            if(lenBcc.toUInt() > 32u) throw Exception("BCC length > 32 - $lenBcc")
            indicator = indicator.turnBitOn(1)
        }

        if(!cc.isNullOrEmpty()) {
            iCc = true
            lenCc = cc!!.toByteArray().size
            if(lenCc.toUInt() > 32u) throw Exception("CC length > 32 - $lenCc")
            indicator = indicator.turnBitOn(2)
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
        if(iCc) emailContent[++slidingIndex] = lenCc.toByte()
        if(iBcc) emailContent[++slidingIndex] = lenBcc.toByte()
        if(iSubject) emailContent[++slidingIndex] = lenSubject.toByte()
        emailContent[++slidingIndex] = lenBody.toByte()

        if(iFrom) emailContent = emailContent.plus(from!!.toByteArray())
        emailContent = emailContent.plus(to!!.toByteArray())
        if(iCc) emailContent = emailContent.plus(cc!!.toByteArray())
        if(iBcc) emailContent = emailContent.plus(bcc!!.toByteArray())
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
                val isBcc = indicators.isBitOn(1)
                val isCc = indicators.isBitOn(2)
                val isFrom = indicators.isBitOn(3)

                val lenFrom = if(isFrom) data[++slidingIndex] else 0
                if(lenFrom > 32) throw Exception("FROM length > 32 - $lenFrom")

                val lenTo = data[++slidingIndex]
                if(lenTo.toUInt() > 32u) throw Exception("TO length > 32 - $lenTo")

                val lenCc = if(isCc) data[++slidingIndex] else 0
                if(lenCc.toUInt() > 32u) throw Exception("CC length > 32 - $lenCc")

                val lenBcc = if(isBcc) data[++slidingIndex] else 0
                if(lenBcc.toUInt() > 32u) throw Exception("BCC length > 32 - $lenBcc")

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

                val cc = if (isCc) String(
                    data,
                    ++slidingIndex,
                    lenCc.toInt()
                ).also { slidingIndex += lenCc.toInt() - 1 } else null

                val bcc = if (isBcc) String(
                    data,
                    ++slidingIndex,
                    lenBcc.toInt()
                ).also { slidingIndex += lenBcc.toInt() - 1 } else null

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
                this.iCc = isCc
                this.iBcc = isBcc
                this.iSubject = isSubject
                this.lenFrom = lenFrom.toInt()
                this.lenTo = lenTo.toInt()
                this.lenCc = lenCc.toInt()
                this.lenBcc = lenBcc.toInt()
                this.lenSubject = lenSubject.toInt()
                this.lenBody = lenBody.toInt()
                this.from = from
                this.to = to
                this.cc = cc
                this.bcc = bcc
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
