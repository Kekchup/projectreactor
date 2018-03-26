import org.slf4j.LoggerFactory
import reactor.core.publisher.MonoProcessor

class MonoSamples {
    val LOG = LoggerFactory.getLogger(MonoSamples::class.java!!)


    @Throws(Exception::class)
    fun main(args: Array<String>) {
        // Deferred is the publisher, Promise the consumer
        val promise = MonoProcessor.create<String>()

        val result = promise.doOnSuccess({ p -> LOG.info("Promise completed {}", p) })
                .doOnTerminate({s -> LOG.info("Got value: {}", s)})
        //.doOnError { t -> LOG.error(t.message, t) }
        //.subscribe()

        promise.onNext("Hello World!")
        //promise.onError(new IllegalArgumentException("Hello Shmello! :P"));

        val s = result.blockMillis(1000)
        LOG.info("s={}", s)
    }

}