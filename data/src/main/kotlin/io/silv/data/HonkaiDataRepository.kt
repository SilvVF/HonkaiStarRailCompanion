package io.silv.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import io.silv.honkai.Character
import io.silv.honkai.LightCone
import io.silv.honkai.Relic
import io.silv.honkai.SelectWithLightCone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class HonkaiDataRepository(
    private val database: Database,
) {

    suspend fun updateCharacter(
        name: String,
        updated: (prev: Character?) -> Character?
    ) = withContext(Dispatchers.IO) {
        val prev = database.characterQueries.selectByName(name).executeAsOneOrNull()

        updated(prev)?.let { updated ->
            database.characterQueries.upsert(updated)
        }
    }

    suspend fun removeLightCone(id: Long): LightCone? = withContext(Dispatchers.IO) {
        val deleted = database.lightConeQueries.selectById(id).executeAsOneOrNull()
        database.lightConeQueries.deleteById(id)

        deleted
    }

    suspend fun addLightCone(
        name: String,
        level: Int,
        maxLevel: Int,
        superimpose: Int,
    ): Long = withContext(Dispatchers.IO) {
        database.lightConeQueries.insert(
            id = null,
            name = name,
            level = level.toLong(),
            superimpose = superimpose.toLong(),
            maxLevel = maxLevel.toLong(),
            location = null
        )

        database.lightConeQueries.lastInsertRowId().executeAsOne()
    }

    fun observeAllCharacters(): Flow<List<io.silv.honkai.Character>> {
        return database.characterQueries.selectAll().asFlow().mapToList(Dispatchers.IO)
    }

    fun observeRelicsForCharacter(name: String): Flow<List<Relic>> {
        return database.relicQueries.selectByEquipedCharacter(name).asFlow().mapToList(Dispatchers.IO)
    }

    fun observeLightConeForCharacter(name: String): Flow<LightCone?> {
        return database.lightConeQueries.selectByEquippedCharacter(name).asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    fun observeCharacterByName(name: String): Flow<Character> {
        return database.characterQueries.selectByName(name).asFlow().mapToOne(Dispatchers.IO)
    }

    fun observeCharacterByNameWithLightCone(name: String): Flow<SelectWithLightCone> {
        return database.characterQueries.selectWithLightCone(name).asFlow().mapToOne(Dispatchers.IO)
    }

    fun observeAllRelics(): Flow<List<Relic>> {
        return database.relicQueries.selectAll().asFlow().mapToList(Dispatchers.IO)
    }

    fun observeAllLightCones(): Flow<List<LightCone>> {
        return database.lightConeQueries.selectAll().asFlow().mapToList(Dispatchers.IO)
    }
}