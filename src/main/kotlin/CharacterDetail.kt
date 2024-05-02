import kotlinx.serialization.Serializable

@Serializable
data class CharacterDetail(
    val splashArtUrl: String?,
    val nameEng: String?,
    val nameChi: String?,
    val nameJap: String?,
    val nameKor: String?,
    val vaEng: String?,
    val vaChi: String?,
    val vaJap: String?,
    val vaKor: String?,
    val voiceOverListEng: List<Pair<String, String>>,
    val voiceOverListChi: List<Pair<String, String>>,
    val voiceOverListJap: List<Pair<String, String>>,
    val voiceOverListKor: List<Pair<String, String>>,
)