package com.github.bjhham.prtbay

import com.github.bjhham.prtbay.search.TorrentResult
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

fun HTML.searchPage(
    categories: List<Category>,
    searchValue: String = "",
    categoryValue: String = "All",
    results: List<TorrentResult>? = null,
) {
    head {
        title("Parrot Bay \uD83E\uDD9C\uD83C\uDFF4")
        script(src = "/web.js") {}
        link(rel = "stylesheet", href = "/style.css")
    }
    body {
        h1 { +"Parrot Bay \uD83E\uDD9C\uD83C\uDFF4" }
        p { +"Your personal torrent server." }

        form(action="/") {
            div("categories") {
                for (c in categories) {
                    categorySelector(c.name, categoryValue)
                }
            }
            div("form-option") {
                input(type = InputType.text, name = "search") {
                    placeholder = "Search titles..."
                    value = searchValue
                }
            }
        }

        if (results != null) {
            table {
                thead {
                    tr {
                        th { +"Name" }
                        th { +"SE" }
                        th { +"LE" }
                        th { colSpan = "2" }
                    }
                }
                tbody {
                    searchResultRows(categoryValue, results)
                }
            }
        } else {
            p { +"Enter a search term above to see results." }
        }
    }
}

fun DIV.categorySelector(name: String, currentValue: String?) {
    div("category") {
        input(type = InputType.radio, name = "category") {
            id = "category-${name.lowercase()}"
            value = name
            checked = currentValue == name
        }
        label {
            htmlFor = "category-${name.lowercase()}"
            +name
        }
    }
}

fun TBODY.searchResultRows(category: String, results: List<TorrentResult>) {
    for (t in results) {
        tr {
            td { +t.title }
            td { +t.seeders.toString() }
            td { +t.leechers.toString() }
            td {
                a {
                    href = t.link
                    +"\uD83E\uDDF2"
                }
            }
            // Save button
            td {
                @OptIn(ExperimentalKtorApi::class)
                form(method = FormMethod.post) {
                    attributes.hx {
                        post = "/torrents/$category"
                        swap = "none"
                        disabledElt = "find button[type=submit]"

                    // TODO Fix KTOR-9329
                    //   on["before-request"] = SaveButton.beforeRequest
                    //   on["after-request"] = SaveButton.afterRequest
                    //   on["response-error"] = SaveButton.responseError
                    }
                    attributes["hx-on::before-request"] = SaveButton.beforeRequest
                    attributes["hx-on::after-request"] = SaveButton.afterRequest
                    attributes["hx-on::response-error"] = SaveButton.responseError

                    input(type = InputType.hidden, name = "link") {
                        value = t.link
                    }

                    button(type = ButtonType.submit) {
                        +"Save \uD83D\uDCBE"
                    }
                }
            }
        }
    }
}

object SaveButton {
    // language=JavaScript
    val beforeRequest = js("""
        const b=this.querySelector('button[type=submit]');
        b.dataset.originalHtml=b.dataset.originalHtml||b.innerHTML; 
        b.innerHTML='Saving...'; 
        b.disabled=true;
    """)

    // language=JavaScript
    val afterRequest = js("""
        const b=this.querySelector('button[type=submit]');
        if (event.detail.successful){ b.innerHTML='Saved ✅'; b.disabled=true; }
    """.trimIndent())

    // language=JavaScript
    val responseError = js("""
        var b=this.querySelector('button[type=submit]');
        b.innerHTML=b.dataset.originalHtml||b.innerHTML;
        b.disabled=false;
    """.trimIndent())

    private fun js(text: String): String =
        text.trimIndent().replace('\n', ' ')
}
