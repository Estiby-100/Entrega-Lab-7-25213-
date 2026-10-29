package plat.lab1.lab7_25213.data.local

import plat.lab1.lab7_25213.data.Character
import plat.lab1.lab7_25213.data.Location

fun CharacterEntity.toModel() = Character(id, name, status, species, gender, image, isFavorite)

fun Character.toEntity() = CharacterEntity(id, name, status, species, gender, image, isFavorite)

fun LocationEntity.toModel() = Location(id, name, type, dimension)

fun Location.toEntity() = LocationEntity(id, name, type, dimension)
