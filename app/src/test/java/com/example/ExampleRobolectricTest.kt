package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Programmer Calculator", appName)
    val developerName = context.getString(R.string.developer_name)
    assertEquals("Noor Tech Apps", developerName)
    val developerEmail = context.getString(R.string.developer_email)
    assertEquals("noortechapp@gmail.com", developerEmail)
  }
}
