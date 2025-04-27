import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    DatabaseFactory.connect()
    val crawler = Crawler()
    application {
        Window(onCloseRequest = {
            ChromeManager.closeAllSessions()
            exitApplication()
        }) {
            var websiteName by remember { mutableStateOf<String?>(null) }
            var isDialogOpen by remember { mutableStateOf(false) }
            var demonProcessStatus by remember { mutableStateOf(DemonProcessStatus.UNREGISTER) }

            websiteName = transaction {
                Websites.selectAll()
                    .orderBy(Websites.id, SortOrder.DESC)
                    .limit(1)
                    .map(Website::of)
                    .firstOrNull()
                    ?.name
            }

            demonProcessStatus =
                if (websiteName == null) DemonProcessStatus.UNREGISTER
                else DemonProcessStatus.START

            MaterialTheme {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (websiteName == null) Text("등록된 웹사이트가 없습니다.")
                    else Text("등록된 웹사이트: $websiteName")

                    Spacer(Modifier.height(8.dp))

                    Button(onClick = { isDialogOpen = true }) {
                        Text("새로 등록")
                    }
                }

                if (isDialogOpen) {
                    websiteRegisterDialog(
                        onDismiss = { isDialogOpen = false },
                        onSubmit = {
                            websiteName =
                                transaction {
                                    Websites.selectAll()
                                        .orderBy(Websites.id, SortOrder.DESC)
                                        .limit(1)
                                        .map(Website::of)
                                        .first()
                                        .name
                                }

                            isDialogOpen = false
                            demonProcessStatus = DemonProcessStatus.REGISTER

                            GlobalScope.launch {
                                crawler.allStop()
                                val chromeDriver = ChromeManager.newChrome()
                                crawler.start(chromeDriver)
                            }
                        }
                    )
                }
            }

            LaunchedEffect(demonProcessStatus) {
                val chromeDriver = when (demonProcessStatus) {
                    DemonProcessStatus.UNREGISTER -> return@LaunchedEffect
                    DemonProcessStatus.REGISTER -> ChromeManager.newChrome()
                    DemonProcessStatus.START -> ChromeManager.newChrome(headless = true)
                }

                crawler.start(chromeDriver)
            }
        }
    }
}
