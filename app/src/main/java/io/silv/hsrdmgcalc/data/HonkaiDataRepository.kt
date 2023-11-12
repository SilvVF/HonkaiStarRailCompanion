package io.silv.hsrdmgcalc.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import io.silv.HonkaiDatabase
import io.silv.honkai.Character
import io.silv.honkai.LightCone
import io.silv.honkai.Relic
import io.silv.honkai.SelectWithLightCone
import io.silv.hsrdmgcalc.AppDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class HonkaiDataRepository(
    private val database: HonkaiDatabase,
    private val dispatchers: AppDispatchers,
) {
    suspend fun removeLightCone(id: Long): LightCone? = withContext(dispatchers.io) {
        val deleted = database.lightConeQueries.selectById(id).executeAsOneOrNull()
        database.lightConeQueries.deleteById(id)

        deleted
    }

    suspend fun addLightCone(
        name: String,
        level: Int,
        superimpose: Int,
    ): Long = withContext(dispatchers.io) {
        database.lightConeQueries.insert(
            id = null,
            name = name,
            level = level.toLong(),
            superimpose = superimpose.toLong(),
            location = null
        )

        database.lightConeQueries.lastInsertRowId().executeAsOne()
    }

    fun observeAllCharacters(): Flow<List<Character>> {
        return database.characterQueries.selectAll().asFlow().mapToList(dispatchers.io)
    }

    fun observeCharacterByName(name: String): Flow<Character> {
        return database.characterQueries.selectByName(name).asFlow().mapToOne(dispatchers.io)
    }

    fun observeCharacterByNameWithLightCone(name: String): Flow<SelectWithLightCone> {
        return database.characterQueries.selectWithLightCone(name).asFlow().mapToOne(dispatchers.io)
    }

    fun observeAllRelics(): Flow<List<Relic>> {
        return database.relicQueries.selectAll().asFlow().mapToList(dispatchers.io)
    }

    fun observeAllLightCones(): Flow<List<LightCone>> {
        return database.lightConeQueries.selectAll().asFlow().mapToList(dispatchers.io)
    }
}