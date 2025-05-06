import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener

fun registerGlobalEvent(
    vararg keyCodes: Int,
    action: () -> Unit,
) {
    val requiredKeys = keyCodes.toSet()
    val pressedKeys = mutableSetOf<Int>()

    GlobalScreen.registerNativeHook()

    GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
        override fun nativeKeyPressed(e: NativeKeyEvent) {
            pressedKeys += e.keyCode

            if (requiredKeys.all { it in pressedKeys }) {
                action()
            }
        }

        override fun nativeKeyReleased(e: NativeKeyEvent) {
            pressedKeys -= e.keyCode
        }

        override fun nativeKeyTyped(e: NativeKeyEvent) {}
    })
}

