package com.sa.baseproject.base


import com.sa.baseproject.appview.music.view.MusicListFragment
import com.sa.baseproject.appview.home.view.HomeFragment
import com.sa.baseproject.appview.news.view.NewsDetailsFragment
import com.sa.baseproject.appview.news.view.NewsListFragment


enum class AppFragmentState(var fragment: Class<out AppFragment>) {

    F_HOME(HomeFragment::class.java),
    F_NEWS_LIST(NewsListFragment::class.java),
    F_NEWS_DETAIL(NewsDetailsFragment::class.java),
    F_MUSIC(MusicListFragment::class.java);

    companion object {

        // To get AppFragmentState  enum from class name
        fun getValue(value: Class<*>): AppFragmentState {
            return AppFragmentState.values().firstOrNull { it.fragment == value }
                    ?: AppFragmentState.F_HOME// not found
        }
    }

}
