import kotlinx.serialization.Serializable

@Serializable
data class CharacterListItem(
    val wikiUrl: String? = null,
    val iconUrl: String? = null,
    val name: String? = null,
    val rarityImageUrl: String? = null,
    val pathName: String? = null,
    val combatType: String? = null,
    val version: String? = null
)
