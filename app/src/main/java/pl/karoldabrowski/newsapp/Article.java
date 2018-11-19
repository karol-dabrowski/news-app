package pl.karoldabrowski.newsapp;

public class Article {

    private String title;
    private String section;
    private String date;
    private String author;
    private String url;

    public Article(String title, String section, String url) {
        this.title = title;
        this.section = section;
        this.url = url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public boolean hasAuthor() {
        return author != null;
    }

    public boolean hasDate() {
        return date != null;
    }
}
