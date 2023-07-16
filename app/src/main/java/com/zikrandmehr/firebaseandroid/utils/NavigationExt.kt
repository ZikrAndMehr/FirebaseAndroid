package com.zikrandmehr.firebaseandroid.utils

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.navOptions
import androidx.navigation.ui.R

fun NavController.navigateWithDefaultAnimation(
    directions: NavDirections,
    popUpToDestinationId: Int? = null
) {
    navigate(
        directions = directions,
        navOptions = navOptions {
            anim {
                enter = R.anim.nav_default_enter_anim
                exit = R.anim.nav_default_exit_anim
                popEnter = R.anim.nav_default_pop_enter_anim
                popExit = R.anim.nav_default_pop_exit_anim
            }
            popUpToDestinationId?.let {
                popUpTo(it) {
                    inclusive = true
                }
            }
        }
    )
}

fun NavController.navigateWithDefaultAnimation(
    resId: Int,
    args: Bundle? = null,
    popUpToDestinationId: Int? = null
) {
    navigate(
        resId = resId,
        args = args,
        navOptions = navOptions {
            anim {
                enter = R.anim.nav_default_enter_anim
                exit = R.anim.nav_default_exit_anim
                popEnter = R.anim.nav_default_pop_enter_anim
                popExit = R.anim.nav_default_pop_exit_anim
            }
            popUpToDestinationId?.let {
                popUpTo(it) {
                    inclusive = true
                }
            }
        }
    )
}