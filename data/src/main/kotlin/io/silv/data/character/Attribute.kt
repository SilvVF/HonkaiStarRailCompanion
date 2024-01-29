package io.silv.data.character

sealed interface Attribute

data object ATK: Attribute
data object DEF: Attribute
data object HP: Attribute
data object SPD: Attribute