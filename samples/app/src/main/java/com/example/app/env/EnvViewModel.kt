/*
 * Copyright (c) 2023 ForgeRock. All rights reserved.
 *
 *  This software may be modified and distributed under the terms
 *  of the MIT license. See the LICENSE file for details.
 */

package com.example.app.env

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.forgerock.android.auth.FRAuth
import org.forgerock.android.auth.FROptions
import org.forgerock.android.auth.FROptionsBuilder
import org.forgerock.android.auth.Logger
import org.forgerock.android.auth.PIInitParams
import org.forgerock.android.auth.PIProtect

class EnvViewModel : ViewModel() {
    val servers = mutableListOf<FROptions>()

    val localhost =
        FROptionsBuilder.build {
            server {
                url = "http://192.168.131.247:8080/am"
                realm = "root"
                cookieName = "iPlanetDirectoryPro"
                timeout = 50
            }
            oauth {
                oauthClientId = "AndroidTest"
                oauthRedirectUri = "org.forgerock.demo:/oauth2redirect"
                oauthCacheSeconds = 0
                oauthScope = "openid profile email address phone"
                oauthThresholdSeconds = 0
            }
        }

    val dbind =
        FROptionsBuilder.build {
            server {
                url = "https://openam-sdks.forgeblocks.com/am"
                realm = "alpha"
                cookieName = "5421aeddf91aa20"
                timeout = 50
            }
            oauth {
                oauthClientId = "AndroidTest"
                oauthRedirectUri = "org.forgerock.demo:/oauth2redirect"
                oauthCacheSeconds = 0
                oauthScope = "openid profile email address phone"
                oauthThresholdSeconds = 0
            }
            service {
                authServiceName = "jeydevicebind"
            }
        }

    val sdk =
        FROptionsBuilder.build {
            server {
                url = "https://openam-dbind.forgeblocks.com/am"
                realm = "alpha"
                cookieName = "43d72fc37bdde8c"
                timeout = 50
            }
            oauth {
                oauthClientId = "AndroidTest"
                oauthRedirectUri = "org.forgerock.demo:/oauth2redirect"
                oauthCacheSeconds = 0
                oauthScope = "openid profile email address phone"
                oauthThresholdSeconds = 0
            }
            service {
                authServiceName = "WebAuthn"
            }
        }

    val local =
        FROptionsBuilder.build {
            server {
                url = "https://andy.petrov.ca/openam"
                realm = "root"
                cookieName = "iPlanetDirectoryPro"
                timeout = 50
            }
            oauth {
                oauthClientId = "AndroidTest"
                oauthRedirectUri = "org.forgerock.demo:/oauth2redirect"
                oauthCacheSeconds = 0
                oauthScope = "openid profile email address phone"
                oauthThresholdSeconds = 0
            }
            service {
                authServiceName = "WebAuthn"
            }
        }

    val ops =
        FROptionsBuilder.build {
            server {
                url = "https://default.forgeops.petrov.ca/am"
                realm = "root"
                cookieName = "iPlanetDirectoryPro"
                timeout = 50
            }
            oauth {
                oauthClientId = "AndroidTest"
                oauthRedirectUri = "org.forgerock.demo:/oauth2redirect"
                oauthCacheSeconds = 60
                oauthScope = "openid profile email address phone"
                oauthThresholdSeconds = 0
            }
            service {
                authServiceName = "WebAuthn"
            }
        }

    val forgeblock =
        FROptionsBuilder.build {
            server {
                url = "https://openam-sdks.forgeblocks.com/am"
                realm = "alpha"
                cookieName = "iPlanetDirectoryPro"
                timeout = 50
            }
            oauth {
                oauthClientId = "AndroidTest"
                oauthRedirectUri = "org.forgerock.demo:/oauth2redirect"
                oauthCacheSeconds = 0
                oauthScope = "openid profile email address phone"
                oauthThresholdSeconds = 0
            }
            service {
                authServiceName = "WebAuthn"
            }
        }

    var current by mutableStateOf(dbind)
        private set

    init {
        servers.add(localhost)
        servers.add(dbind)
        servers.add(sdk)
        servers.add(local)
        servers.add(ops)
        servers.add(forgeblock)
    }

    fun select(
        context: Context,
        options: FROptions,
    ) {
        FRAuth.start(context, options)
        current = options

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val params =
                    PIInitParams(
                        envId = "02fb4743-189a-4bc7-9d6c-a919edfe6447",
                        isBehavioralDataCollection = false,
                        isConsoleLogEnabled = true,
                    )
                PIProtect.initSDK(context, params)
            } catch (e: Exception) {
                Logger.error("Int Error", e.message)
            }
        }
    }

    fun select(
        context: Context,
        host: String,
    ) {
        servers.find {
            it.server.url == host
        }?.let {
            select(context, it)
        } ?: run {
            select(context, dbind)
        }
    }

    fun getAll(): List<FROptions> {
        return servers
    }
}
