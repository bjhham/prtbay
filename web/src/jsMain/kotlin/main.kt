package com.github.bjhham

@JsModule("htmx.org")
external object htmx

fun main() {
    // htmx reference is needed for import
    console.log("htmx loaded", htmx)
}