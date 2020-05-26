package com.example.themovieapp.about

import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mehdi.sakout.aboutpage.AboutPage


class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return AboutPage(context)
            .isRTL(false)
            .setImage(com.example.themovieapp.R.mipmap.ic_launcher)
            .setDescription(getString(com.example.themovieapp.R.string.about_app))
            .addGroup("Connect with us!")
            .addEmail("tomkeane07@gmail.com", "email")
            .addGitHub("https://github.com/tomkeane07/TheMovieApp/tree/development", "Github")
            //GIthub doesn't work on android browser.. nav to github app in store?
            .create()
    }
}