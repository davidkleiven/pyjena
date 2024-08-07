package com.github.davidkleiven.pyjena

import py4j.GatewayServer

fun main() {
    val container = TdbContainer()
    val server = GatewayServer(container)
    server.start()
}
