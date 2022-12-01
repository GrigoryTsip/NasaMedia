package ru.netology;

public class NasaCardMedia {

    private String copyright;
    private String date;
    private String explanation;
    private String hdurl;
    private String media_type;
    private String service_version;
    private String title;
    private String url;

    public NasaCardMedia(String[] attr) {
        this.copyright = attr[0];
        this.date = attr[1];
        this.explanation = attr[2];
        this.hdurl = attr[3];
        this.media_type = attr[4];
        this.service_version = attr[5];
        this.title = attr[6];
        this.url = attr[7];
    }

    public String getUrl() { return this.url; }

    public String toString() {
        return "Copyright = " + this.copyright
             + ", \nДата = " + this.date
             + ", \nКомментарий:\n " + this.explanation
             + ", \nHDURL = " + this.hdurl
             + ", \nТип = " + this.media_type
             + ", \nВерсия = " + this.service_version
             + ", \nНазвание = " + this.title + "\n"
             + ", \nURL = " + this.url + "\n";
    }

    public String toString(String fileName) {

        int stringLenght = 20; //количество слов в строке

        // Формируем комментарий, разбивая его на строки
        String[] words = this.explanation.split(" ");
        StringBuilder string = new StringBuilder(words[0]);
        int i = 1;
        while (i < words.length) {
            if ( i % stringLenght == 0) string.append("\n");
            string.append(" " + words[i]);
            i++;
        }

        // Есть ли автор?
        String author = (this.copyright.equals("")) ? "Неизвестен" : this.copyright;

        return  "Название: " + this.title + "\nКомментарий:\n " + string +
                "\n\nАвтор: " + author + "\nФайл: " + fileName + "\n" + this.date;
    }
}
