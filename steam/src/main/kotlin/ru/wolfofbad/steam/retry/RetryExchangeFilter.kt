package ru.wolfofbad.steam.retry

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.ExchangeFunction
import reactor.core.publisher.Mono
import ru.wolfofbad.steam.configuration.BackoffConfiguration
import kotlin.time.toJavaDuration

@Service
class RetryExchangeFilter(
    private val backoffPolicy: BackoffPolicy,

    config: BackoffConfiguration
) : ExchangeFilterFunction {
    private val httpStatuses: List<HttpStatus> = config.codes
    private val maxAttempts: Int = config.maxAttempts

    override fun filter(request: ClientRequest, next: ExchangeFunction): Mono<ClientResponse> {
        return retry(request, next, 1)
    }

    private fun retry(request: ClientRequest, next: ExchangeFunction, attempt: Int): Mono<ClientResponse> {
        return next
            .exchange(request)
            .flatMap { clientResponse: ClientResponse ->
                val code = clientResponse.statusCode() as HttpStatus
                if (httpStatuses.contains(code) && attempt <= maxAttempts) {
                    return@flatMap Mono.delay(backoffPolicy.getWaitTime(attempt).toJavaDuration())
                        .then<ClientResponse>(
                            Mono.defer<ClientResponse> {
                                retry(
                                    request,
                                    next,
                                    attempt + 1
                                )
                            }
                        )
                } else if (attempt > maxAttempts) {
                    return@flatMap clientResponse.bodyToMono<String>(String::class.java)
                        .flatMap<ClientResponse?> { errorBody: String ->
                            Mono.error<ClientResponse?>(
                                Exception(
                                    "Failed to execute client method $maxAttempts times. Error: $errorBody"
                                )
                            )
                        }
                }
                Mono.just(clientResponse)
            }
    }
}
