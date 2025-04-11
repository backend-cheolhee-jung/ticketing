import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import java.awt.Rectangle
import java.awt.Robot
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object CaptureImages : Table("capture_images") {
    val id = long("id").autoIncrement()
    val x = integer("x")
    val y = integer("y")
    val width = integer("width")
    val height = integer("height")
    val filePath = varchar("file_path", 255)

    override val primaryKey = PrimaryKey(id)
}

data class CaptureImage(
    val x: Int,
    val y: Int,
    val width: Int,
    val height: Int,
    val filePath: String,
) {
    private lateinit var image: BufferedImage

    fun capture() {
        val robot = Robot()
        val screenRect = Rectangle(x, y, width, height)
        image = robot.createScreenCapture(screenRect)
    }

    fun saveImage() {
        ImageIO.write(image, "png", File(filePath))
        CaptureImages.insert {
            it[x] = this@CaptureImage.x
            it[y] = this@CaptureImage.y
            it[width] = this@CaptureImage.width
            it[height] = this@CaptureImage.height
            it[filePath] = this@CaptureImage.filePath
        }
    }

    fun deleteImage() {
        File(filePath).delete()
        CaptureImages.deleteWhere {
            filePath eq this@CaptureImage.filePath
        }
    }

    operator fun compareTo(other: CaptureImage): Int {
        val isSame = image == other.image
        return if (isSame) 0 else 1
    }

    companion object {
        @JvmStatic
        fun of(
            x: Int,
            y: Int,
            width: Int,
            height: Int,
        ): CaptureImage {
            val filePath = "capture_${System.currentTimeMillis()}.png"
            return CaptureImage(
                x = x,
                y = y,
                width = width,
                height = height,
                filePath = filePath,
            )
        }

        @JvmStatic
        fun of(
            resultRow: ResultRow,
        ) =
            CaptureImage(
                x = resultRow[CaptureImages.x],
                y = resultRow[CaptureImages.y],
                width = resultRow[CaptureImages.width],
                height = resultRow[CaptureImages.height],
                filePath = resultRow[CaptureImages.filePath],
            )
    }
}