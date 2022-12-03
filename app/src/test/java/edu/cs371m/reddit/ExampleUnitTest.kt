package edu.cs371m.reddit

import android.content.Context
import android.content.res.AssetManager

import androidx.test.core.app.ApplicationProvider.getApplicationContext

import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.signature.ObjectKey
import edu.cs371m.reddit.glide.RedditDiskCacheFactory

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList
import java.util.Random

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class DiskCacheTest {

}
