package com.example.vkapistart

import android.app.Application
import androidx.appcompat.content.res.AppCompatResources
import com.pnikosis.materialishprogress.BuildConfig
import com.vk.api.sdk.VK
import com.vk.auth.internal.AuthLibBridge.silentTokenExchanger
import com.vk.auth.main.VkClientUiInfo
import com.vk.superapp.SuperappKit
import com.vk.superapp.SuperappKitConfig
import com.vk.superapp.core.SuperappConfig

internal object Utils {
    fun initSuperAppKit(context: Application) {

        val appName = context.getString(R.string.app_name)

        // Укажите этот параметр и appId в файле ресурсов!
        val clientSecret = context.getString(R.string.vk_client_secret)

        // Укажите иконку, которая будет отображаться в компонентах пользовательского интерфейса
        val icon = AppCompatResources.getDrawable(context, R.mipmap.ic_launcher)!!

        val appInfo = SuperappConfig.AppInfo(
            appName,
            VK.getAppId(context).toString(),
            BuildConfig.VERSION_NAME
        )

        val config = SuperappKitConfig.Builder(context)
            // настройка VK ID
            .setAuthModelData(clientSecret)
            .setAuthUiManagerData(VkClientUiInfo(icon, appName))
            .setLegalInfoLinks(
                serviceUserAgreement = "https://id.vk.com/terms",
                servicePrivacyPolicy = "https://id.vk.com/privacy"
            )
            .setApplicationInfo(appInfo)
            // Обмен Silent token на Access
            .setSilentTokenExchanger(silentTokenExchanger)
            .build()

        // Инициализация SuperAppKit
        SuperappKit.init(config)
    }
}