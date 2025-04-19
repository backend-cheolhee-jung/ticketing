import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogState
import androidx.compose.ui.window.DialogWindow
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.awt.Toolkit

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
        position = WindowPosition.Aligned(Alignment.Center)
    )

    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var loginUrl by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var idInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var loginButtonElement by remember { mutableStateOf("") }

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
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                )
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text("URL") },
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                )
            }

            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                OutlinedTextField(
                    value = loginUrl,
                    onValueChange = { loginUrl = it },
                    label = { Text("로그인 URL") },
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("이메일") },
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                )
            }

            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("비밀번호") },
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                )
                OutlinedTextField(
                    value = idInput,
                    onValueChange = { idInput = it },
                    label = { Text("ID 입력 필드") },
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                )
            }

            Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                OutlinedTextField(
                    value = passwordInput,
                    onValueChange = { passwordInput = it },
                    label = { Text("비밀번호 입력 필드") },
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                )
                OutlinedTextField(
                    value = loginButtonElement,
                    onValueChange = { loginButtonElement = it },
                    label = { Text("로그인 버튼 요소") },
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = {
                    transaction {
                        Websites.insert {
                            it[Websites.name] = name
                            it[Websites.url] = url
                            it[Websites.loginUrl] = loginUrl
                            it[Websites.email] = email
                            it[Websites.password] = password
                            it[Websites.idInput] = idInput
                            it[Websites.passwordInput] = passwordInput
                            it[Websites.loginButtonElement] = loginButtonElement
                        }
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
