package io.silv.hsrdmgcalc.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import io.silv.HonkaiDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PrepopulateWorker(
    applicationContext: Context,
    workerParameters: WorkerParameters
): CoroutineWorker(applicationContext, workerParameters), KoinComponent {

    private val db by inject<HonkaiDatabase>()

    override suspend fun doWork(): Result {

        val result = runCatching {
            db.transaction {
                HonkaiConstants.characters.forEach { name ->
                    db.characterQueries.prepopulate(name)
                }
            }
        }

        return if (result.isSuccess) {
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