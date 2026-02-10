package com.github.bjhham

import com.github.bjhham.search.TorrentResult
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
            td {
                form(action = "/download/$category", method = FormMethod.post) {
                    input(type = InputType.hidden, name = "link") {
                        value = t.link
                    }
                    input(type = InputType.submit) {
                        value = "\uD83D\uDCBE"
                    }
                }
            }
        }
    }
}