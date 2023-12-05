package com.smithjilks.cryptographydemo.firebase

import com.google.gson.annotations.SerializedName


data class RemoteConfig(
    @SerializedName("certificate_leaf_sha256")
    val certificateLeafSha256: String = "",

    @SerializedName("certificate_intermediate_sha256")
    val certificateIntermediateSha256: String = ""
)