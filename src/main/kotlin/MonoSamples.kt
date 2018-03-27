import org.slf4j.LoggerFactory
import reactor.core.publisher.MonoProcessor
import java.time.Duration

class MonoSamples {
    val LOG = LoggerFactory.getLogger(MonoSamples::class.java!!)


    @Throws(Exception::class)
    fun main(args: Char) {
        // Deferred is the publisher, Promise the consumer
        val promise = MonoProcessor.create<String>()

        val result = promise.doOnSuccess({ p -> LOG.info("Promise completed {}", p) })
                .doOnTerminate{LOG.info("Got value:")}
                .doOnError { t -> LOG.error(t.message, t) }


        promise.onNext("Hello World!")
        promise.onError( IllegalArgumentException("Hello Shmello! :P"));

        val s = result.block(Duration.ofMillis(1000))
        LOG.info("s={}", s)
    }

}