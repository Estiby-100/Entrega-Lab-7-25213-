package plat.lab1.lab7_25213.screens.characterform

sealed interface CharacterFormEvent {
    data class OnNameChange(val name: String) : CharacterFormEvent
    data class OnStatusChange(val status: String) : CharacterFormEvent
    data class OnSpeciesChange(val species: String) : CharacterFormEvent
    data class OnGenderChange(val gender: String) : CharacterFormEvent
    data class OnImageChange(val image: String) : CharacterFormEvent
    data object OnSaveClick : CharacterFormEvent
}
