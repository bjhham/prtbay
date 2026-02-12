package com.github.bjhham.prtbay

import com.github.bjhham.prtbay.search.TorrentResult
import io.ktor.htmx.html.hx
import io.ktor.utils.io.ExperimentalKtorApi
import kotlinx.html.*

fun HTML.searchPage(
    search: String = "",
    category: String = "TV",
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
                div("category") {
                    input(type = InputType.radio, name = "category") {
                        id = "category-tv"
                        value = "TV"
                        checked = category == "TV"
                    }
                    label {
                        htmlFor = "category-tv"
                        +"TV \uD83D\uDCFA"
                    }
                }
                div("category") {
                    input(type = InputType.radio, name = "category") {
                        id = "category-movies"
                        value = "Movies"
                        checked = category == "Movies"
                    }
                    label {
                        htmlFor = "category-movies"
                        +"Movies \uD83C\uDFA5"
                    }
                }
            }
            div("form-option") {
                input(type = InputType.text, name = "search") {
                    placeholder = "Search titles..."
                    value = search
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
                    searchResultRows(category, results)
                }
            }
        } else {
            p { +"Enter a search term above to see results." }
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

// TODO Ktor bug KTOR-9329
//                        on["before-request"] = "var b=this.querySelector('button[type=submit]'); b.dataset.originalHtml=b.dataset.originalHtml||b.innerHTML; b.innerHTML='Saving...'; b.disabled=true;"
//                        on["after-request"] = "var b=this.querySelector('button[type=submit]'); if(event.detail.successful){b.innerHTML='Saved ✅'; b.disabled=true;}"
//                        on["response-error"] = "var b=this.querySelector('button[type=submit]'); b.innerHTML=b.dataset.originalHtml||b.innerHTML; b.disabled=false;"
                    }
                    attributes["hx-on::before-request"] =
                        "var b=this.querySelector('button[type=submit]'); b.dataset.originalHtml=b.dataset.originalHtml||b.innerHTML; b.innerHTML='Saving...'; b.disabled=true;"
                    attributes["hx-on::after-request"] =
                        "var b=this.querySelector('button[type=submit]'); if(event.detail.successful){b.innerHTML='Saved ✅'; b.disabled=true;}"
                    attributes["hx-on::response-error"] =
                        "var b=this.querySelector('button[type=submit]'); b.innerHTML=b.dataset.originalHtml||b.innerHTML; b.disabled=false;"

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
