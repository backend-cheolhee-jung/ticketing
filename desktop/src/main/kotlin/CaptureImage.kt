import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
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
        transaction {
            CaptureImages.insert {
                it[x] = this@CaptureImage.x
                it[y] = this@CaptureImage.y
                it[width] = this@CaptureImage.width
                it[height] = this@CaptureImage.height
                it[filePath] = this@CaptureImage.filePath
            }
        }
    }

    fun deleteImage() {
        File(filePath).delete()
        transaction {
            CaptureImages.deleteWhere {
                filePath eq this@CaptureImage.filePath
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is CaptureImage) return false
        if (this === other) return true

        val img1 = this.image
        val img2 = other.image

        if (img1.width != img2.width || img1.height != img2.height) return false

        for (x in 0 until img1.width) {
            for (y in 0 until img1.height) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false
                }
            }
        }

        return true
    }

    override fun hashCode(): Int {
        var result = 17
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                result = 31 * result + image.getRGB(x, y)
            }
        }
        return result
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