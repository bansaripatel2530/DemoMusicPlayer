package com.sa.baseproject.utils.baseinrerface

/**
 * Purpose  - Handle base interface for BaseActivity.
 * @author  - amit.prajapati
 * Created  - 30/10/17
 * Modified - 26/12/17
 */
interface LoadingBridge {

    fun showProgress()

    fun hideProgress()

    fun showActionBarProgress()

    fun hideActionBarProgress()
}