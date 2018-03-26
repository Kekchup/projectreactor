
import org.slf4j.LoggerFactory
import reactor.bus.Event
import reactor.bus.EventBus
import reactor.bus.selector.Selectors.*

class SelectorSamples {
    val LOG = LoggerFactory.getLogger(SelectorSamples::class.java)

    @Throws(InterruptedException::class)
    fun main(args: Array<String>) {

        val r = EventBus.create()

        // Simple topic selection
        r.on(`$`("/some/topic"), { ev -> LOG.info("Got event '{}'", ev.getData()) })

        // Topic selection based on regex
        r.on(R("/some/(.+)"), { ev ->
            // RegexSelector puts capture groups into headers using convention 'group'
            // + [capture group #]
            val topic = ev.getHeaders().get("group1")

            LOG.info("Got event '{}' for {}", ev.getData(), topic)
        })

        // Topic selection based on URI template
        r.on(U("/some/{topic}"), { ev ->
            // UriTemplateSelector puts path segment matches into headers using the
            // path variable name (like Spring MVC)
            val topic = ev.getHeaders().get("topic")

            LOG.info("Got event '{}' for {}", ev.getData(), topic)
        })

        // Type selection based on inheritance
        r.on(T(Exception::class.java),
                { ev: Event<Exception> -> LOG.error(ev.getData().getMessage()) })

        // A single publish goes to three Consumers
        r.notify("/some/topic", Event.wrap("Hello World!"))
        // Publish error using Exception class as the key
        r.notify(IllegalArgumentException::class.java,
                Event.wrap(IllegalArgumentException("That argument was invalid")))

    }}