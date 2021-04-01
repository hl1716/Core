package com.hviewtech.wowpay.merchant.core.net

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

class FileRequestBody(
    private val requestBody: RequestBody,
    private val loadingListener: LoadingListener
) : RequestBody() {
    private var contentLength: Long = 0L

    override fun contentLength(): Long {
        try {
            if (contentLength == 0L) {
                contentLength = requestBody.contentLength()
            }
            return contentLength
        } catch (e: IOException) {

        }
        return -1
    }

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    override fun writeTo(sink: BufferedSink) {
        val byteSink = ByteSink(sink)
        val bufferedSink = Okio.buffer(byteSink)
        //val bufferedSink = byteSink.buffer()
        requestBody.writeTo(bufferedSink)
        bufferedSink.flush()
    }

    private inner class ByteSink(delegate: Sink): ForwardingSink(delegate) {
        private var byteLength = 0L

        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            byteLength += byteCount
            loadingListener.onProgress(byteLength, contentLength)
        }
    }

    interface LoadingListener {
        fun onProgress(currentLength: Long, contentLength: Long)
    }
}
