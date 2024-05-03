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

        val rarityImageUrl: String? = null
        val pathName: String? = null
        val combatType: String? = null
        val version: String? = null

        return CharacterListItem(
            wikiUrl = tableColumn.getOrNull(0)?.a { findAll { eachHref }.firstOrNull() },
            iconUrl = tableColumn.getOrNull(0)?.a { img { findFirst { eachSrc }.firstOrNull() } },
            name = tableColumn.getOrNull(1)?.a { findFirst { text } },
            rarityImageUrl = rarityImageUrl,
            pathName = pathName,
            combatType = combatType,
            version = version,
        )
    }
}