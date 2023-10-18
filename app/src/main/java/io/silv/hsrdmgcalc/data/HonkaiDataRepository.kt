package io.silv.hsrdmgcalc.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.silv.HonkaiDatabase
import io.silv.honkai.Character
import io.silv.honkai.LightCone
import io.silv.honkai.Relic
import io.silv.hsrdmgcalc.AppDispatchers
import kotlinx.coroutines.flow.Flow

class HonkaiDataRepository(
    private val database: HonkaiDatabase,
    private val dispatchers: AppDispatchers,
) {

    fun observeAllCharacters(): Flow<List<Character>> {
        return database.characterQueries.selectAll().asFlow().mapToList(dispatchers.io)
    }

    fun observeAllRelics(): Flow<List<Relic>> {
        return database.relicQueries.selectAll().asFlow().mapToList(dispatchers.io)
    }

    fun observeAllLightCones(): Flow<List<LightCone>> {
        return database.lightConeQueries.selectAll().asFlow().mapToList(dispatchers.io)
    }
}