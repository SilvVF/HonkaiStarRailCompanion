package io.silv.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import io.silv.data.Database
import io.silv.data.constants.HonkaiConstants
import io.silv.data.preferences.DataPreferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PrepopulateWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(applicationContext, workerParameters), KoinComponent {

    private val TAG = "PrepopulateWorker"
    private val db by inject<Database>()
    private val dataPrefs by inject<DataPreferences>()

//    private suspend fun prepopulateLightCones() = runCatching {
//        if(dataPrefs.lightConeListVersion() == HonkaiConstants.LightConeListVersion) {
//            Log.d(TAG, "Skipping prepopulate lightcone versions matched version: ${HonkaiConstants.CharacterListVersion}")
//            return@runCatching
//        }
//
//        db.transaction {
//            HonkaiConstants.lightCones.forEach { name ->
//
//                if (db.lightConeQueries.selectByName(name).executeAsList().isEmpty()) {
//                    db.lightConeQueries.prepopulate(null, name)
//                }
//            }
//        }
//
//        dataPrefs.updateLightConeListVersion(HonkaiConstants.LightConeListVersion)
//    }
//        .onFailure { Log.d(TAG, "Failed to prepopulate light cones") }

    private suspend fun prepopulateCharacters() = runCatching {
        if(dataPrefs.characterListVersion() == HonkaiConstants.CharacterListVersion) {
            Log.d(TAG, "Skipping prepopulate character versions matched version: ${HonkaiConstants.CharacterListVersion}")
            return@runCatching
        }

        db.transaction {
            HonkaiConstants.characters.forEach { name ->
                db.characterQueries.prepopulate(name)
            }
        }

        dataPrefs.updateCharacterListVersion(HonkaiConstants.CharacterListVersion)
    }
        .onFailure { Log.d(TAG, "Failed to prepopulate characters") }

    override suspend fun doWork(): Result {
        return if (
            listOf(
                prepopulateCharacters(),
                //prepopulateLightCones()
            ).all { result ->
                result.isSuccess
            }
        ) {
            Result.success()
        } else {
            Result.retry()
        }
    }

    companion object {
        const val PrepopulateWorkName = "PrepopulateWorkName"

        fun prepopulate(context: Context) {
            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    PrepopulateWorkName,
                    ExistingWorkPolicy.REPLACE,
                    OneTimeWorkRequestBuilder<PrepopulateWorker>()
                        .build()
                )
        }
    }
}