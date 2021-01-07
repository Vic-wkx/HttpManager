package com.example.httpmanager.commen.httpConfig

import com.base.library.rxRetrofit.download.config.DownloadConfig

class MyDownloadConfig : DownloadConfig() {
    /**进度更新频率，下载多少Byte后更新一次进度。默认每下载4KB更新一次，使用[DownloadConfig.PROGRESS_BY_PERCENT]表示每下载百分之一更新一次*/
    override var progressStep = 32 * 1024
}