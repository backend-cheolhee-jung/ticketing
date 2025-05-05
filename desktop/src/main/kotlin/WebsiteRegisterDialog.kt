import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.awt.Toolkit
import javax.swing.JOptionPane

@Composable
fun websiteRegisterDialog(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val windowWidth = (screenSize.width / 2).dp
    val windowHeight = (screenSize.height / 2).dp

    val windowState = DialogState(
        width = windowWidth,
        height = windowHeight,
        position = WindowPosition.Aligned(Alignment.Center),
    )

    val focusRequesters = remember { List(9) { FocusRequester() } }

    val (nameFR, urlFR, idValueFR, passwordFR, loginUrlFR, idInputFR, passwordInputFR, loginButtonFR, emailFR) =
        focusRequesters

    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var loginUrl by remember { mutableStateOf("") }
    var idValue by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var idInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var loginButtonElement by remember { mutableStateOf("") }

    fun Modifier.tabToNext(
        currentIndex: Int,
    ): Modifier = this.onPreviewKeyEvent {
        if (it.key == Key.Tab && it.type == KeyEventType.KeyUp) {

            focusRequesters.getOrNull(currentIndex + 1)?.requestFocus()
            true
        } else {
            false
        }
    }

    DialogWindow(
        onCloseRequest = onDismiss,
        title = "웹사이트 등록",
        state = windowState,
        resizable = true,
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("사이트명") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                        .focusRequester(nameFR)
                        .tabToNext(0),
                )
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("URL") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                        .focusRequester(urlFR)
                        .tabToNext(1),
                )
            }

            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                OutlinedTextField(
                    value = idValue,
                    onValueChange = { idValue = it },
                    label = { Text("아이디") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                        .focusRequester(idValueFR)
                        .tabToNext(2),
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("비밀번호") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                        .focusRequester(passwordFR)
                        .tabToNext(3),
                )
            }

            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                OutlinedTextField(
                    value = loginUrl,
                    onValueChange = { loginUrl = it },
                    label = { Text("로그인 URL") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                        .focusRequester(loginUrlFR)
                        .tabToNext(4),
                )
                OutlinedTextField(
                    value = idInput,
                    onValueChange = { idInput = it },
                    label = { Text("ID Input Element") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                        .focusRequester(idInputFR)
                        .tabToNext(5),
                )
            }

            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("비밀번호 Input Element") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                        .focusRequester(passwordInputFR)
                        .tabToNext(6),
                )
                OutlinedTextField(
                    value = loginButtonElement,
                    onValueChange = { loginButtonElement = it },
                    label = { Text("로그인 Button Element") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                        .focusRequester(loginButtonFR)
                        .tabToNext(7),
                )
            }

            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("연락 받을 이메일") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                        .focusRequester(emailFR)
                        .tabToNext(8),
                )
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    runCatching {
                        transaction {
                            Websites.insert {
                                it[Websites.name] = name.trim()
                                it[Websites.url] = url.trim()
                                it[Websites.loginUrl] = loginUrl.trim()
                                it[Websites.email] = email.trim()
                                it[Websites.idValue] = idValue.trim()
                                it[Websites.password] = password.trim()
                                it[Websites.idInput] = idInput.trim()
                                it[Websites.passwordInput] = passwordInput.trim()
                                it[Websites.loginButtonElement] = loginButtonElement.trim()
                            }
                        }
                    }.onFailure { error ->
                        if (error is ExposedSQLException) error.showDialogMessage()
                        else error.printStackTrace()
                    }
                    onSubmit()
                }) {
                    Text("등록")
                }

                Spacer(Modifier.width(16.dp))

                Button(onClick = onDismiss) {
                    Text("취소")
                }
            }
        }
    }
}

private operator fun <E> List<E>.component6(): E {
    return this[5]
}

private operator fun <E> List<E>.component7(): E {
    return this[6]
}

private operator fun <E> List<E>.component8(): E {
    return this[7]
}

private operator fun <E> List<E>.component9(): E {
    return this[8]
}

private fun ExposedSQLException.showDialogMessage() {
    val message = this.message
    checkNotNull(message)

    val dialogMessage: String

    if (message.contains("UNIQUE constraint failed")) {
        dialogMessage = when {
            message.contains("name") -> "사이트명은 이미 존재합니다."
            message.contains("url") -> "URL은 이미 존재합니다."
            message.contains("login_url") -> "로그인 URL은 이미 존재합니다."
            message.contains("email") -> "이메일은 이미 존재합니다."
            else -> "알 수 없는 오류입니다."
        }
    } else if (message.contains("SQLITE_CONSTRAINT_CHECK")) {
        dialogMessage = when {
            message.contains("check_website_urls_0") -> "사이트명은 비어있을 수 없습니다."
            message.contains("check_website_urls_1") -> "URL은 비어있을 수 없습니다."
            message.contains("check_website_urls_2") -> "Login URL은 비어있을 수 없습니다."
            message.contains("check_website_urls_3") -> "이메일은 비어있을 수 없습니다."
            message.contains("check_website_urls_4") -> "비밀번호는 비어있을 수 없습니다."
            message.contains("check_website_urls_5") -> "ID 입력 필드는 비어있을 수 없습니다."
            message.contains("check_website_urls_6") -> "비밀번호 입력 필드는 비어있을 수 없습니다."
            message.contains("check_website_urls_7") -> "로그인 버튼 요소는 비어있을 수 없습니다."
            else -> "알 수 없는 오류입니다."
        }
    } else {
        dialogMessage = "알 수 없는 오류입니다."
    }

    JOptionPane.showMessageDialog(
        null,
        dialogMessage,
        "Error",
        JOptionPane.ERROR_MESSAGE
    )
}