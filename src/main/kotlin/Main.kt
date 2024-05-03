import character_detail.CharacterDetail
import kotlinx.coroutines.delay
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
suspend fun main(args: Array<String>) {
    /*HsrCharacterListUrl.parserHonkaiStarRailPlayableCharacters()
        .let { saveHsrCharacterList(it) }*/
    loadHsrCharacterList()
        //?.let { listOf(it.first()) }
        ?.map {
            val detail = loadSingleChartDetail(it.name ?: "")
                ?:  HstCharacterData.parseCharacter(it)
                    .also {
                        saveSingleChartDetail(it)
                        delay(10.seconds)
                    }
            detail
        }
        ?.let { saveHasCharacterDetailList(it) }
}

@OptIn(ExperimentalSerializationApi::class)
fun saveHsrCharacterList(
    data: List<CharacterListItem>,
    fileName: String = "HsrCharacterList.json",
) {
    val file = File(fileName)
    if (file.exists())
        file.delete()
    FileOutputStream(file).use { Json.encodeToStream(data, it) }
}

@OptIn(ExperimentalSerializationApi::class)
private fun loadHsrCharacterList(
    fileName: String = "HsrCharacterList.json",
): List<CharacterListItem>? {
    return File(fileName)
        .takeIf { it.exists() }
        ?.let { FileInputStream(it) }
        ?.use { Json.decodeFromStream(it) }
}

@OptIn(ExperimentalSerializationApi::class)
fun saveSingleChartDetail(
    characterDetail: CharacterDetail
) {
    val folder = "char_details_data"
    val fileName = "detail_${characterDetail.nameEng ?: "Unknown"}.json"
    val file = File(folder, fileName)
    if (file.exists())
        file.delete()
    println("Save ${characterDetail.nameEng} to $fileName")
    FileOutputStream(file).use { Json.encodeToStream(characterDetail, it) }
}

@OptIn(ExperimentalSerializationApi::class)
fun loadSingleChartDetail(
    nameEng: String
): CharacterDetail? {
    val folder = "char_details_data"
    val fileName = "detail_${nameEng}.json"
    val file = File(folder, fileName)
    println("Loading $fileName (exist:${file.exists()})")
    if (!file.exists())
        return null
    return FileInputStream(file).use { Json.decodeFromStream(it) }
}

@OptIn(ExperimentalSerializationApi::class)
fun saveHasCharacterDetailList(
    hsrCharacterDetailList: List<CharacterDetail>,
    fileName: String = "HsrCharacterDetailList.json",
) {
    val file = File(fileName)
    if (file.exists())
        file.delete()
    FileOutputStream(file).use { Json.encodeToStream(hsrCharacterDetailList, it) }
}