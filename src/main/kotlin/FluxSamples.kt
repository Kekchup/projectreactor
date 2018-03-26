import org.slf4j.LoggerFactory;
import reactor.core.publisher.EmitterProcessor



class FluxSamples {
    val LOG = LoggerFactory.getLogger(FluxSamples::class.java)

    @Throws(InterruptedException::class)
    fun main(args: Array<String>) {

        simpleFlux()

        transformValues()

        filterValues()

    }

    @Throws(InterruptedException::class)
    private fun simpleFlux() {
        // A Flux is a data publisher
        val stream = EmitterProcessor.create<String>().publish().autoConnect()


        // Log values passing through the Flux and capture the first coming signal
        val promise =  stream.doOnNext({ s -> LOG.info("Consumed String {}", s) })
                .next()
        promise.subscribe()

        // Publish a value
        stream.doOnComplete({LOG.info("Hello World!")})

        promise.block()
    }

    @Throws(InterruptedException::class)
    private fun transformValues() {
        // A Flux is a data publisher
        val stream = EmitterProcessor.create<String>().publish().autoConnect()

        // Transform values passing through the Flux, observe and capture the result once.
        val promise = stream.map ({ it.toUpperCase()})
                .doOnNext({ s -> LOG.info("UC String {}", s) })
                .next()

        promise.subscribe()

        // Publish a value
        stream.doOnComplete({LOG.info("Hello World!")})

        promise.block()
    }

    @Throws(InterruptedException::class)
    private fun filterValues() {
        // A Flux is a data publisher
        val stream = EmitterProcessor.create<String>()

        // Filter values passing through the Flux, observe and capture the result once.
        val promise = stream.filter({ s -> s.startsWith("Hello") })
                .doOnNext({ s -> LOG.info("Filtered String {}", s) })
                .collectList()
        promise.subscribe()

        // Publish a value
        stream.onNext("Hello World!")
        stream.onNext("Goodbye World!")
        stream.onComplete()

        promise.block()
    }
}