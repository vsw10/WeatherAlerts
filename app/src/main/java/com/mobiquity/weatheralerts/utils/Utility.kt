package com.mobiquity.weatheralerts.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle

// Function to launch the activity
fun launchActivity(context: Context,
                    clazz: Class<*>,
                    bundle: Bundle? = null,
                      flag: Int? = null ){
    val intent = Intent(context,clazz)
    bundle?.let {
        intent.putExtras(bundle)
    }
    flag?.let {
        intent.setFlags(flag)
    }
    context.startActivity(intent)

}