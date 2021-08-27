package com.radofigura.lottery.check.sk.fragments

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.radofigura.lottery.check.sk.R
import com.radofigura.lottery.check.sk.util.CacheManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (CacheManager.getString(getString(R.string.cache_user_generated_id), null, applicationContext) == null) {
            CacheManager.addString(getString(R.string.cache_user_generated_id), UUID.randomUUID().toString(), applicationContext)
        }

        if (CacheManager.getBoolean(getString(R.string.cache_agree_generated_id), false, applicationContext) == false) {
            createDialog()
        }

        activity_main_about_app_button.setOnClickListener {
            createDialog()
        }
    }

    fun createDialog() {
        var builder = AlertDialog.Builder(this)
            .setTitle(getString(R.string.string_dialog_title))
            .setMessage(getString(R.string.string_dialog_message))
            .setNegativeButton("OK") { dialogInterface, i ->
                CacheManager.addBoolean(getString(R.string.cache_agree_generated_id), true, applicationContext)

                dialogInterface.dismiss()
            }
            .create()
            .show()
    }

    fun initializeRemoteConfig() {
        // TODO: app version to force recreate
    }
}