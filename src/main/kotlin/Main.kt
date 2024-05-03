import character_detail.CharacterDetail
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun main(args: Array<String>) {
    /*HsrCharacterListUrl.parserHonkaiStarRailPlayableCharacters()
        .let { saveHsrCharacterList(it) }*/
    loadHsrCharacterList()
        ?.let { listOf(it.first()) }
        ?.map { HstCharacterData.parseCharacter(it) }
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
fun saveHasCharacterDetailList(
    hsrCharacterDetailList: List<CharacterDetail>,
    fileName: String = "HsrCharacterDetailList.json",
) {
    val file = File(fileName)
    if (file.exists())
        file.delete()
    FileOutputStream(file).use { Json.encodeToStream(hsrCharacterDetailList, it) }
}