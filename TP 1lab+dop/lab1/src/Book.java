import java.util.List;

public class Book {
    private String id;
    private String title;
    private String author;
    private int year;
    private String genre;
    private Price price;
    private String format;
    private Publisher publisher;
    private String translator;
    private String isbn;
    private List<Reviews> reviews; // Изменено на список отзывов

    public Book(String id, String title, String author, int year, String genre, Price price,
                String format, Publisher publisher, String translator, String isbn, List<Reviews> reviews) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.genre = genre;
        this.price = price;
        this.format = format;
        this.publisher = publisher;
        this.translator = translator;
        this.isbn = isbn;
        this.reviews = reviews;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List<Reviews> getReviews() {
        return reviews;
    }

    public void setReviews(List<Reviews> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------\n");
        sb.append("Book:\n\tid: ").append(id);

        String[] attributes = {"title", "author", "year", "genre", "price", "format", "publisher", "translator", "isbn"};

        for (String attribute : attributes) {
            switch (attribute) {
                case "title":
                    if (title != null && !title.isEmpty()) {
                        sb.append("\n\ttitle: ").append(title);
                    }
                    break;
                case "author":
                    if (author != null && !author.isEmpty()) {
                        sb.append("\n\tauthor: ").append(author);
                    }
                    break;
                case "year":
                    if (year != 0) {
                        sb.append("\n\tyear: ").append(year);
                    }
                    break;
                case "genre":
                    if (genre != null && !genre.isEmpty()) {
                        sb.append("\n\tgenre: ").append(genre);
                    }
                    break;
                case "price":
                    if (price != null) {
                        sb.append("\n\tprice: ").append(price);
                    }
                    break;
                case "format":
                    if (format != null && !format.isEmpty()) {
                        sb.append("\n\tformat: ").append(format);
                    }
                    break;
                case "publisher":
                    if (publisher != null) {
                        sb.append("\n\tpublisher: ").append(publisher);
                    }
                    break;
                case "translator":
                    if (translator != null && !translator.isEmpty()) {
                        sb.append("\n\ttranslator: ").append(translator);
                    }
                    break;
                case "isbn":
                    if (isbn != null && !isbn.isEmpty()) {
                        sb.append("\n\tisbn: ").append(isbn);
                    }
                    break;
                default:
                    break;
            }
        }

        if (reviews != null && !reviews.isEmpty()) {
            sb.append("\n\treviews:");
            for (Reviews review : reviews) {
                sb.append("\n\t\t- User: ").append(review.getUser());
                sb.append("\n\t\t  Rating: ").append(review.getRating());
                sb.append("\n\t\t  Comment: ").append(review.getComment());
            }
        }

        return sb.toString();
    }
}
