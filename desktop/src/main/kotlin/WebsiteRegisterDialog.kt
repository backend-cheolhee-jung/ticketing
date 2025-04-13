import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

@Composable
fun websiteRegisterDialog(
    onDismiss: () -> Unit,
    onSubmit: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var loginUrl by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var idInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var loginButtonElement by remember { mutableStateOf("") }

    DialogWindow(onCloseRequest = onDismiss, title = "웹사이트 등록") {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("이름") })
            OutlinedTextField(value = url, onValueChange = { url = it }, label = { Text("URL") })
            OutlinedTextField(value = loginUrl, onValueChange = { loginUrl = it }, label = { Text("로그인 URL") })
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("이메일") })
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("비밀번호") })
            OutlinedTextField(value = idInput, onValueChange = { idInput = it }, label = { Text("ID 입력 필드") })
            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text("비밀번호 입력 필드") }
            )
            OutlinedTextField(
                value = loginButtonElement,
                onValueChange = { loginButtonElement = it },
                label = { Text("로그인 버튼 요소") }
            )

            Spacer(Modifier.height(16.dp))

            Row {
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

                Spacer(Modifier.width(8.dp))

                Button(onClick = onDismiss) {
                    Text("취소")
                }
            }
        }
    }
}
