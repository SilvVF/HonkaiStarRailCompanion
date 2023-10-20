package io.silv.hsrdmgcalc.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import io.silv.HonkaiDatabase
import io.silv.hsrdmgcalc.preferences.DataPreferences
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PrepopulateWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(applicationContext, workerParameters), KoinComponent {

    private val TAG = "PrepopulateWorker"
    private val db by inject<HonkaiDatabase>()
    private val dataPrefs by inject<DataPreferences>()

    override suspend fun doWork(): Result {

        if (dataPrefs.characterListVersion() == HonkaiConstants.CharacterListVersion) {
            Log.d(TAG, "Skipping prepopulate versions matched version: ${HonkaiConstants.CharacterListVersion}")
            return Result.success()
        }

        Log.d(TAG, "Updating Character table version to match version: ${HonkaiConstants.CharacterListVersion}")

        val result = runCatching {

            db.transaction {
                HonkaiConstants.characters.forEach { name ->
                    db.characterQueries.prepopulate(name)
                }
            }

            dataPrefs.updateCharacterListVersion(HonkaiConstants.CharacterListVersion)
        }

        return if (result.isSuccess) {
            Log.d(TAG, "Successfully updated Character table version to match version: ${HonkaiConstants.CharacterListVersion}")
            Result.success()
        } else {
            Log.e(TAG, "Failed to updated Character table version to match version: ${HonkaiConstants.CharacterListVersion} ${result.exceptionOrNull()?.message}")
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