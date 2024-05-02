import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractBlocking
import it.skrape.fetcher.skrape
import it.skrape.selects.Doc
import it.skrape.selects.html5.a

object HstCharacterData {

    private const val page_va_eng = "Voice-Overs"
    private const val page_va_chi = "Voice-Overs/Chinese"
    private const val page_va_jap = "Voice-Overs/Japanese"
    private const val page_va_kor = "Voice-Overs/Korean"

    fun parseCharacter(characterListItem: CharacterListItem): CharacterDetail {
        val wikiUrl = "${Const.base_url}${characterListItem.wikiUrl}"
        println("Paring url $wikiUrl")
        val (
            splashArtUrl,
            charNameList,
            vaNameList
        ) = skrape(HttpFetcher) {
            request { this.url = wikiUrl }
            extractBlocking {
                htmlDocument {
                    Triple(
                        extractSplashArtUrl(),
                        extractCharNameList(),
                        extractVaNameList()
                    )
                }
            }
        }
        return CharacterDetail(
            splashArtUrl = splashArtUrl,
            nameEng = charNameList.eng,
            nameChi = charNameList.chi,
            nameJap = charNameList.jap,
            nameKor = charNameList.kor,
            vaEng = vaNameList.eng,
            vaChi = vaNameList.chi,
            vaJap = vaNameList.jap,
            vaKor = vaNameList.kor,
            voiceOverListEng = extractVoicePackList(wikiUrl + page_va_eng),
            voiceOverListChi = extractVoicePackList(wikiUrl + page_va_chi),
            voiceOverListJap = extractVoicePackList(wikiUrl + page_va_jap),
            voiceOverListKor = extractVoicePackList(wikiUrl + page_va_kor),
        )
    }

    private fun Doc.extractSplashArtUrl(): String? {
        return findFirst("figure img").a {
            findFirst { eachHref }.firstOrNull()
        }
    }

    private fun extractCharNameList(): NameList {
        TODO()
    }

    private fun extractVaNameList(): NameList {
        TODO()
    }

    private fun extractVoicePackList(pageUrl: String): List<Pair<String, String>> {
        TODO()
    }

    data class NameList(
        val eng: String,
        val chi: String,
        val jap: String,
        val kor: String,
    )

}