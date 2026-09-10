package jp.co.yumemi.android.codecheck

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.codecheck.core.designsystem.theme.CodeCheckTheme
import jp.co.yumemi.android.codecheck.navigation.AppNavHost

/**
 * Single-Activity application shell hosting Compose Navigation and Hilt injection.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CodeCheckTheme {
                AppNavHost(
                    onOpenBrowser = { url ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(intent)
                    }
                )
            }
        }
    }
}
