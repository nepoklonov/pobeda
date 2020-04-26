package pobeda.server

import java.awt.Color
import java.awt.Font
import java.io.File
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.imageio.ImageIO
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


fun sendCertificate(email: String, fio: String, hash: String = "1", number: String, supervisorFIO: String?) {

    val bufferedImage = ImageIO.read(File("images/design/cert.jpg"))
    val graphics = bufferedImage.graphics
    graphics.color = Color.black

    val fioList = mutableListOf<String>()

    var s = ""
    fio.split(' ').forEach { word ->
        graphics.font = Font("Arial Black", Font.BOLD, 60)
        val fm = graphics.fontMetrics
        if (fm.stringWidth(s + word) > 1300) {
            fioList += s.trim()
            s = ""
        }
        s += "$word "
    }
    fioList += s


    val supervisorFioList = mutableListOf<String>()
    if (supervisorFIO != null) {
        graphics.font = Font("Arial Black", Font.BOLD, 40)
        val fm = graphics.fontMetrics
        s = ""
        ("(Куратор: $supervisorFIO)").split(' ').forEach { word ->
            if (fm.stringWidth(s + word) > 1300) {
                supervisorFioList += s.trim()
                s = ""
            }
            s += "$word "
        }
        supervisorFioList += s
    }


    fioList.forEachIndexed { index, str ->
        graphics.font = Font("Arial Black", Font.BOLD, 60)
        val fm = graphics.fontMetrics
        val otherHeight = supervisorFioList.size * 40
        val fioWidth = fm.stringWidth(str)
        graphics.drawString(str, 827 - fioWidth / 2,
            900 - 30 * (fioList.size - 1) + 60 * index - otherHeight / 2)
    }

    supervisorFioList.forEachIndexed { index, str ->
        graphics.font = Font("Arial Black", Font.PLAIN, 40)
        val fm = graphics.fontMetrics
        val otherHeight = fioList.size * 60
        val sFioWidth = fm.stringWidth(str)
        graphics.drawString(str, 827 - sFioWidth / 2,
            900 - 20 * (supervisorFioList.size - 1) + 40 * index + otherHeight / 2)
    }

    graphics.font = Font("Arial Black", Font.BOLD, 31)
    graphics.drawString(number, 1313, 490)
    val fileC = File("images/certs/cert-$hash.jpg")
    ImageIO.write(bufferedImage, "jpg", fileC)
    println("Image Created")

    val mailProps = Properties()
    mailProps["mail.smtp.host"] = "smtp.yandex.ru"
    mailProps["mail.smtp.auth"] = "true"
    mailProps["mail.smtp.port"] = "465"
    mailProps["mail.smtp.socketFactory.port"] = "465"
    mailProps["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"

    val mailSession = Session.getInstance(mailProps, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication("risyem-pobedy-2", "pobeda1945-2")
        }
    })

    val message = MimeMessage(mailSession)
    message.setFrom(InternetAddress("risyem-pobedy-2@yandex.ru"))
    message.setRecipients(Message.RecipientType.TO, email.trim().toLowerCase())
    message.setSubject("Сертификат участника", "UTF-8")
    val mp = MimeMultipart()

    val mbpText = MimeBodyPart()
    mbpText.setContent("Поздравляем Вас с успешным участием в Акции!\n" +
            "Созданная Вами работа загружена в мультимедийную галерею Интернет-портала #РисуемПобеду\n" +
            "Благодарим Вас за участие в Акции и поздравляем с наступающим Днем великой Победы!\n" +
            "С уважением, Оргкомитет Акции", "text/html; charset=utf-8")
    mp.addBodyPart(mbpText)

    val mbpImage = MimeBodyPart()
    val fds = FileDataSource(fileC)
    mbpImage.dataHandler = DataHandler(fds)
    mbpImage.fileName = fds.name
    mp.addBodyPart(mbpImage)

    message.setContent(mp)
    message.sentDate = Date()
    message.saveChanges()
    Transport.send(message)
    println("sent put $email")
}