import java.io.IOException;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException {
        Parser parser = new Parser();
        parser.parse("random_structure_41.xml");

        for (Book book : parser.getBooks()) {
            System.out.println("----");
            if (book.getId() != null && !book.getId().isEmpty()) {
                System.out.println("Book ID: " + book.getId());
            }
            if (book.getTitle() != null && !book.getTitle().isEmpty()) {
                System.out.println("Title: " + book.getTitle());
            }
            if (book.getAuthor() != null && !book.getAuthor().isEmpty()) {
                System.out.println("Author: " + book.getAuthor());
            }
            if (book.getYear() != 0) {
                System.out.println("Year: " + book.getYear());
            }
            if (book.getGenre() != null && !book.getGenre().isEmpty()) {
                System.out.println("Genre: " + book.getGenre());
            }
            if (book.getPrice() != null) {
                System.out.println("Price: " + book.getPrice());
            }
            if (book.getIsbn() != null && !book.getIsbn().isEmpty()) {
                System.out.println("ISBN: " + book.getIsbn());
            }
            if (book.getFormat() != null && !book.getFormat().isEmpty()) {
                System.out.println("Format: " + book.getFormat());
            }
            if (book.getPublisher() != null) {
                System.out.println("Publisher: " + book.getPublisher());
            }
            if (book.getReviews() != null && !book.getReviews().isEmpty()) {
                System.out.println("Reviews:");
                for (Reviews review : book.getReviews()) {
                    System.out.println("  User: " + review.getUser());
                    System.out.println("  Rating: " + review.getRating());
                    System.out.println("  Comment: " + review.getComment());
                }
            }
            if (book.getTranslator() != null && !book.getTranslator().isEmpty()) {
                System.out.println("Translator: " + book.getTranslator());
            }

        }

        List<Book> books = parser.getBooks();
        XMLWriter.writeBooksToXML(books, "output.xml");





    }

}