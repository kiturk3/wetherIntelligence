package com.krutik.weatherintelligence.data.remote.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

class RetryInterceptor(
    private val maxRetries: Int = 3,
    private val initialBackoffMs: Long = 1000L
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response: Response? = null
        var retryCount = 0
        var backoff = initialBackoffMs

        while (retryCount < maxRetries) {
            try {
                response = chain.proceed(request)
                if (response.isSuccessful || !isTransientError(response.code)) {
                    return response
                }
                Timber.w("HTTP request failed with status code ${response.code}. Retry count: ${retryCount + 1}")
            } catch (e: IOException) {
                Timber.w("Network error during request: ${e.message}. Retry count: ${retryCount + 1}")
                if (retryCount >= maxRetries - 1) {
                    throw e
                }
            }

            retryCount++
            try {
                Thread.sleep(backoff)
            } catch (ignored: InterruptedException) {
            }
            backoff *= 2
        }

        return response ?: chain.proceed(request)
    }

    private fun isTransientError(code: Int): Boolean {
        return code == 502 || code == 503 || code == 504 || code == 408
    }
}
