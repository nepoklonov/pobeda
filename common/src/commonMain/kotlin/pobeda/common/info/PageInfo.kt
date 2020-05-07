package pobeda.common.info

class PageInfo (val url : String, val name : String, val ruName : String) {
    companion object {
        val Main = PageInfo("/", "Main", "Главная")
        val Smi = PageInfo("/for-smi", "Smi", "СМИ")
        val Symbols = PageInfo("/symbols", "Symbols", "Символика")
        val Resources = PageInfo("/resources", "Resources", "Ресурсы")
        val Contacts = PageInfo("/contacts", "Contact", "Контакты")

        val Gallery = PageInfo("/gallery", "Gallery", "Галерея")

        val Admin = PageInfo("/admin", "Admin", "Админка")

        val Join = PageInfo("/join", "Join", "Участвовать")
        val About = PageInfo("/about", "About", "Об акции")
        val Official = PageInfo("/official", "Official", "Положение")
        val News = PageInfo("/news", "News", "Новости")
        val Team = PageInfo("/team", "Team", "Штаб")
        val Partners = PageInfo("/partners", "Partners", "Партнёры")
    }

}