package plat.lab1.lab7_25213.data

import plat.lab1.lab7_25213.data.local.CharacterEntity
import plat.lab1.lab7_25213.data.local.LocationEntity
import plat.lab1.lab7_25213.data.model.Character
import plat.lab1.lab7_25213.data.model.Location
import plat.lab1.lab7_25213.data.remote.dto.CharacterDTO
import plat.lab1.lab7_25213.data.remote.dto.LocationDTO

fun CharacterDTO.toEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image
)

fun CharacterEntity.toModel() = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = isFavorite
)

fun Character.toEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image,
    isFavorite = isFavorite
)

fun LocationDTO.toEntity() = LocationEntity(
    id = id,
    name = name,
    type = type,
    dimension = dimension
)

fun LocationEntity.toModel() = Location(
    id = id,
    name = name,
    type = type,
    dimension = dimension,
    isFavorite = isFavorite
)
