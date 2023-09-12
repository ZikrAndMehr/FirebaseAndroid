/*
 * Copyright (C) 2023 Zokirjon Mamadjonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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