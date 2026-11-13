package plat.lab1.lab7_25213.screens.characterform

data class CharacterFormState(
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val gender: String = "",
    val image: String = "",
    val isEditing: Boolean = false,
    val isSaved: Boolean = false
)
