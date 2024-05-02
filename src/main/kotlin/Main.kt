import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun main(args: Array<String>) {
    //saveHsrCharacterList()
    loadHsrCharacterList()
        ?.firstOrNull()
        ?.let { HstCharacterData.parseCharacter(it) }
}

@OptIn(ExperimentalSerializationApi::class)
fun saveHsrCharacterList(
    fileName: String = "HsrCharacterList.json",
) {
    val listPlayableCharacter = HsrCharacterListUrl.parserHonkaiStarRailPlayableCharacters()
    val file = File(fileName)
    if (file.exists())
        file.delete()
    FileOutputStream(file).use { Json.encodeToStream(listPlayableCharacter, it) }
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