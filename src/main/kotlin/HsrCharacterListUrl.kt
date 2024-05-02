import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractBlocking
import it.skrape.fetcher.skrape
import it.skrape.selects.DocElement
import it.skrape.selects.eachHref
import it.skrape.selects.html5.a
import it.skrape.selects.html5.img

object HsrCharacterListUrl {
    fun parserHonkaiStarRailPlayableCharacters(): List<CharacterListItem> {
        return skrape(HttpFetcher) {
            request { url = "${Const.base_url}${Const.character_list_url}" }
            extractBlocking {
                htmlDocument {
                    findFirst("table")
                        .findAll("tbody tr:not(:has(th))")
                        .map {it.extractCharacterTableLine() }
                }
            }
        }.also { print(it.joinToString("\n")) }
    }

    private fun DocElement.extractCharacterTableLine(): CharacterListItem {
        //println("this => ${this.html}")
        val tableColumn = findAll(cssSelector = "td")

        var wikiUrl: String? = tableColumn.getOrNull(0)?.a { findAll { eachHref }.firstOrNull() }
        var iconUrl: String? = tableColumn.getOrNull(0)?.a { img { findFirst { eachSrc }.firstOrNull() } }
        var name: String? = tableColumn.getOrNull(1)?.a { findFirst { text } }
        var rarityImageUrl: String? = null
        var pathName: String? = null
        var combatType: String? = null
        var version: String? = null

        return CharacterListItem(
            wikiUrl = wikiUrl,
            iconUrl = iconUrl,
            name = name,
            rarityImageUrl = rarityImageUrl,
            pathName = pathName,
            combatType = combatType,
            version = version,
        )
    }
}