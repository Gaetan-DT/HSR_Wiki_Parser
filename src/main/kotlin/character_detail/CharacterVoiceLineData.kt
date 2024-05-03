package character_detail

import kotlinx.serialization.Serializable

@Serializable
data class CharacterVoiceLineData(
    val voiceLineName: String,
    val voiceLineCaption: String,
    val voiceLineUrl: String,
)
