import character_detail.CharacterDetail
import character_detail.CharacterVoiceLineData
import it.skrape.core.htmlDocument
import it.skrape.fetcher.HttpFetcher
import it.skrape.fetcher.extractBlocking
import it.skrape.fetcher.skrape
import it.skrape.selects.Doc
import it.skrape.selects.DocElement
import it.skrape.selects.html5.a
import it.skrape.selects.html5.b
import it.skrape.selects.html5.div
import it.skrape.selects.html5.h3

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
            voiceOverListEng = extractVoicePackList("$wikiUrl/$page_va_eng"),
            voiceOverListChi = extractVoicePackList("$wikiUrl/$page_va_chi", filterSpanLanguage = "zh"),
            voiceOverListJap = extractVoicePackList("$wikiUrl/$page_va_jap"),
            voiceOverListKor = extractVoicePackList("$wikiUrl/$page_va_kor"),
        )
    }

    private fun Doc.extractSplashArtUrl(): String? {
        return findFirst("figure").a {
            findFirst { eachHref }.firstOrNull()
        }
    }

    private fun Doc.extractCharNameList(): NameList {
        val englishLanguage = "English"
        val chinesseLanguage = "Chinese (Simplified)"
        val japaneseLanguage = "Japanese"
        val koreanLanguage = "Korean"

        val listLanguage = findAll("table")
            .getOrNull(1)
            ?.findAll("tbody tr:not(:has(th))")
            ?.map {
                val td = it.findAll("td")
                val languageName = td.getOrNull(0)?.b { findFirst { text } }
                val languageValue = td.getOrNull(1)?.findFirst { text }
                //println("foo => languageName=$languageName languageValue=$languageValue")
                languageName to languageValue
            }
        return NameList(
            eng = listLanguage?.firstOrNull { it.first == englishLanguage }?.second,
            chi = listLanguage?.firstOrNull { it.first == chinesseLanguage }?.second,
            jap = listLanguage?.firstOrNull { it.first == japaneseLanguage }?.second,
            kor = listLanguage?.firstOrNull { it.first == koreanLanguage }?.second,
        )
    }

    private fun Doc.extractVaNameList(): NameList {
        val englishLanguage = "English"
        val chineseLanguage = "Chinese"
        val japaneseLanguage = "Japanese"
        val koreanLanguage = "Korean"
        val listLanguage = findAll("section.pi-item.pi-panel")
            .first()
            .findAll("div.wds-tab__content")[1]
            .findAll(".pi-item.pi-data.pi-item-spacing.pi-border-color")
            .map { docElement ->
                println()
                val languageName = docElement.h3 {
                    findFirst {
                        text
                    }
                }
                val languageValue = docElement.div(".pi-data-value.pi-font") {
                    a { findFirst { text } }
                }
                languageName to languageValue
            }
        return NameList(
            eng = listLanguage.firstOrNull { it.first == englishLanguage }?.second,
            chi = listLanguage.firstOrNull { it.first == chineseLanguage }?.second,
            jap = listLanguage.firstOrNull { it.first == japaneseLanguage }?.second,
            kor = listLanguage.firstOrNull { it.first == koreanLanguage }?.second,
        )
    }

    private fun extractVoicePackList(
        pageUrl: String,
        filterSpanLanguage: String? = null
    ): List<CharacterVoiceLineData> {
        println("Paring voicepack url $pageUrl")
        val result = skrape(HttpFetcher) {
            request { this.url = pageUrl }
            extractBlocking {
                htmlDocument {
                    //this.findAll("table tbody tr")[2].extractVoicePackUrlFromTableTr()
                    this.findAll("table.wikitable tbody tr").mapNotNull {
                        it.extractVoicePackUrlFromTableTr(filterSpanLanguage)
                    }
                }
            }
        }
        return result
    }

    private fun DocElement.extractVoicePackUrlFromTableTr(
        filterSpanLanguage: String?
    ): CharacterVoiceLineData? {
        val result = try {
            val voiceLang = findLast("td span").attribute("lang")
            if (filterSpanLanguage == null || filterSpanLanguage == voiceLang) {
                CharacterVoiceLineData(
                    voiceLineName = findFirst("th").text,
                    voiceLineCaption = findLast("td span").text,
                    voiceLineUrl = findFirst("td span a").eachHref.first(),
                )
            } else {
                null
            }
        } catch (e: Exception) {
            //println("Exception: $e")
            null
        }
        return result
    }

    data class NameList(
        val eng: String?,
        val chi: String?,
        val jap: String?,
        val kor: String?,
    )

}