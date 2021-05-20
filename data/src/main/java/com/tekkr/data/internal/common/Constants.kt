package com.tekkr.data.internal.common

import com.tekkr.data.BuildConfig

const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"

const val MILLS_IN_A_DAY: Long = 1000 * 60 * 60 * 24

const val BUILD_TYPE_RELEASE: String = "release"

const val BUILD_TYPE_DEBUG: String = "debug"

//val baseUrl = if(BuildConfig.BUILD_TYPE == BUILD_TYPE_RELEASE) "http://cms.cashtek.in"
//else "http://test.cms.cashtek.in"

val baseUrl = if (BuildConfig.BUILD_TYPE == BUILD_TYPE_RELEASE) "http://165.22.214.54:9000"
else "http://165.22.214.54:9000"