package com.mycompany.inventory

import io.javalin.Javalin
import io.javalin.Javalin.log
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.ApiBuilder.put

import io.javalin.plugin.metrics.MicrometerPlugin
import io.micrometer.core.instrument.Clock
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jetty.TimedHandler
import io.micrometer.core.instrument.util.HierarchicalNameMapper
import io.micrometer.graphite.GraphiteConfig
import io.micrometer.graphite.GraphiteMeterRegistry
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory
import org.eclipse.jetty.http2.HTTP2Cipher
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory
import org.eclipse.jetty.server.*
import org.eclipse.jetty.util.ssl.SslContextFactory
import java.time.Duration


class JavalinApp {
    val graphiteConfig: GraphiteConfig = object : GraphiteConfig {
        override fun host(): String {
            return "localhost"
        }

        override fun get(p0: String): String? {
            return null
        }

        override fun step(): Duration {
            return Duration.ofSeconds(5)
        }
    }

    val graphiteRegistry = GraphiteMeterRegistry(graphiteConfig, Clock.SYSTEM, HierarchicalNameMapper.DEFAULT)

    val app = Javalin.create { config ->
        config.server { createHttp2Server() }
        config.registerPlugin(MicrometerPlugin(graphiteRegistry))
        //config.enableDevLogging()
        config.requestLogger { ctx, ms -> log.error("${ctx.status()} ${ctx.url()} time:$ms")}
    } .routes {
        post("/inventory", InventoryController::create)
        get("/inventory", InventoryController::getAll)
        get("/inventory/:locationId", InventoryController::get)
        put("/inventory/:locationId", InventoryController::put)
    }

    fun start(port: Int) {
        JvmMemoryMetrics().bindTo(graphiteRegistry)
        TimedHandler(graphiteRegistry, emptyList())
        //this.app.start(port)
        this.app.start()
    }

    fun stop() {
        app.stop()
    }
}


private fun createHttp2Server(): Server {

    val alpn = ALPNServerConnectionFactory().apply {
        defaultProtocol = "h2"
    }

    val sslContextFactory = SslContextFactory().apply {
        keyStorePath = this::class.java.getResource("/keystore.p12").toExternalForm() // replace with your real keystore
        setKeyStorePassword("cookie") // replace with your real password
        cipherComparator = HTTP2Cipher.COMPARATOR
        provider = "Conscrypt"
    }

    val ssl = SslConnectionFactory(sslContextFactory, alpn.protocol)

    val httpsConfig = HttpConfiguration().apply {
        sendServerVersion = false
        secureScheme = "https"
        securePort = 8443
        addCustomizer(SecureRequestCustomizer())
    }

    val http2 = HTTP2ServerConnectionFactory(httpsConfig)

    val fallback = HttpConnectionFactory(httpsConfig)

    return Server().apply {
        //HTTP/1.1 Connector
        addConnector(ServerConnector(server).apply {
            port = 8000
        })
        // HTTP/2 Connector
        addConnector(ServerConnector(server, ssl, alpn, http2, fallback).apply {
            port = 8001
        })
    }
}
