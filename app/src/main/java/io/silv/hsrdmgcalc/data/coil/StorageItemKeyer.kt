package io.silv.hsrdmgcalc.data.coil

import coil.key.Keyer
import coil.request.Options
import io.github.jan.supabase.storage.StorageItem
import io.silv.data.character.Character

object StorageItemKeyer : Keyer<StorageItem> {

    override fun key(
        data: StorageItem,
        options: Options,
    ): String {
        return "${data.bucketId}${data.path}"
    }
}

fun Character.toStorageItem(): StorageItem {
    return StorageItem(
        path = "character_$name.webp",
        bucketId = "images",
        authenticated = false
    )
}