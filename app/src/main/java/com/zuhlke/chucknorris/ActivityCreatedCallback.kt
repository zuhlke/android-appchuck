package com.zuhlke.chucknorris

import com.zuhlke.chucknorris.model.AppModel

interface ActivityCreatedCallback {
    fun onActivityCreated(appModel: AppModel)
}