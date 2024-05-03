package character_detail

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
    val voiceOverListEng: List<CharacterVoiceLineData>,
    val voiceOverListChi: List<CharacterVoiceLineData>,
    val voiceOverListJap: List<CharacterVoiceLineData>,
    val voiceOverListKor: List<CharacterVoiceLineData>,
)