import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JWindow
import kotlin.math.abs

fun showDragToCaptureOverlay(
    captureAreaSelector: (x: Int, y: Int, width: Int, height: Int) -> Unit,
) {
    val overlay = object : JWindow() {
        var start: Point? = null
        var end: Point? = null

        init {
            isAlwaysOnTop = true
            background = Color(0, 0, 0, 32)
            size = Toolkit.getDefaultToolkit().screenSize
            location = Point(0, 0)

            val glassPane = object : Canvas() {
                override fun paint(g: Graphics) {
                    super.paint(g)
                    if (start != null && end != null) {
                        val g2 = g as Graphics2D
                        g2.color = Color.CYAN
                        val x = minOf(start!!.x, end!!.x)
                        val y = minOf(start!!.y, end!!.y)
                        val w = abs(end!!.x - start!!.x)
                        val h = abs(end!!.y - start!!.y)
                        g2.drawRect(x, y, w, h)
                    }
                }
            }

            add(glassPane)

            glassPane.addMouseListener(object : MouseAdapter() {
                override fun mousePressed(e: MouseEvent) {
                    start = e.point
                }

                override fun mouseReleased(e: MouseEvent) {
                    end = e.point
                    repaint()

                    val x = minOf(start!!.x, end!!.x)
                    val y = minOf(start!!.y, end!!.y)
                    val w = abs(end!!.x - start!!.x)
                    val h = abs(end!!.y - start!!.y)

                    dispose()

                    captureAreaSelector(x, y, w, h)
                }
            })

            glassPane.addMouseMotionListener(object : MouseAdapter() {
                override fun mouseDragged(e: MouseEvent) {
                    end = e.point
                    repaint()
                }
            })

            isVisible = true
        }
    }
}