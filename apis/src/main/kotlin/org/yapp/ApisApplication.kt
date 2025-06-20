package org.yapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Main application class for the apis module.
 */
@SpringBootApplication
class ApisApplication

/**
 * Main function to start the application.
 */
fun main(args: Array<String>) {
    runApplication<ApisApplication>(*args)
}
