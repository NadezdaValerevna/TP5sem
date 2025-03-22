import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Parser {
    private List<Book> books = new ArrayList<>();

    public void parse(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            Book currentBook = null;
            Publisher currentPublisher = null;
            Address currentAddress = null;
            Price currentPrice = null;
            List<Reviews> currentReviews = null;
            Reviews currentReview = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                switch (getElementType(line)) {
                    case "book":
                        String id = line.replaceAll(".*id=\"|\">", "").trim();
                        currentBook = new Book(id, "", "", 0, "", null, "", null, "", null, null);
                        break;
                    case "title":
                        String title = line.replaceAll("<title>|</title>", "").trim();
                        currentBook.setTitle(title);
                        break;
                    case "author":
                        String author = line.replaceAll("<author>|</author>", "").trim();
                        currentBook.setAuthor(author);
                        break;
                    case "year":
                        int year = Integer.parseInt(line.replaceAll("<year>|</year>", "").trim());
                        currentBook.setYear(year);
                        break;
                    case "genre":
                        String genre = line.replaceAll("<genre>|</genre>", "").trim();
                        currentBook.setGenre(genre);
                        break;
                    case "price":
                        String currency = line.replaceAll(".*currency=\"([^\"]+)\".*", "$1").trim();
                        double amount = Double.parseDouble(line.replaceAll(".*<price\\s+currency=\"[^\"]+\">([\\d.]+)</price>.*", "$1").trim());
                        currentPrice = new Price(currency, amount);
                        break;
                    case "format":
                        String format = line.replaceAll("<format>|</format>", "").trim();
                        currentBook.setFormat(format);
                        break;
                    case "isbn":
                        String isbn = line.replaceAll("<isbn>|</isbn>", "").trim();
                        currentBook.setIsbn(isbn);
                        break;
                    case "translator":
                        String translator = line.replaceAll("<translator>|</translator>", "").trim();
                        currentBook.setTranslator(translator);
                        break;
                    case "publisher":
                        currentPublisher = new Publisher("", null);
                        break;
                    case "publisher_name":
                        if (currentPublisher != null) {
                            String name = line.replaceAll("<name>|</name>", "").trim();
                            currentPublisher.setName(name);
                        }
                        break;
                    case "city":
                        String city = line.replaceAll("<city>|</city>", "").trim();
                        currentAddress = new Address(city, "");
                        break;
                    case "country":
                        String country = line.replaceAll("<country>|</country>", "").trim();
                        currentAddress.setCountry(country);
                        break;
                    case "end_address":
                        if (currentPublisher != null) {
                            currentPublisher.setAddress(currentAddress);
                        }
                        break;
                    case "end_publisher":
                        currentBook.setPublisher(currentPublisher);
                        break;
                    case "reviews":
                        currentReviews = new ArrayList<>();
                        break;
                    case "review":
                        currentReview = new Reviews("", "", "");
                        break;
                    case "user":
                        if (currentReview != null) {
                            String user = line.replaceAll("<user>|</user>", "").trim();
                            currentReview.setUser(user);
                        }
                        break;
                    case "rating":
                        if (currentReview != null) {
                            int rating = Integer.parseInt(line.replaceAll("<rating>|</rating>", "").trim());
                            currentReview.setRating(String.valueOf(rating));
                        }
                        break;
                    case "comment":
                        if (currentReview != null) {
                            String comment = line.replaceAll("<comment>|</comment>", "").trim();
                            currentReview.setComment(comment);
                        }
                        break;
                    case "end_review":
                        if (currentReviews != null && currentReview != null) {
                            currentReviews.add(currentReview);
                        }
                        break;
                    case "end_reviews":
                        if (currentBook != null) {
                            currentBook.setReviews(currentReviews);
                        }
                        break;
                    case "end_book":
                        if (currentBook != null) {
                            currentBook.setPrice(currentPrice);
                            books.add(currentBook);
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getElementType(String line) {
        if (line.startsWith("<book")) return "book";
        if (line.startsWith("<title>")) return "title";
        if (line.startsWith("<author>")) return "author";
        if (line.startsWith("<year>")) return "year";
        if (line.startsWith("<genre>")) return "genre";
        if (line.startsWith("<price")) return "price";
        if (line.startsWith("<format>")) return "format";
        if (line.startsWith("<isbn>")) return "isbn";
        if (line.startsWith("<translator>")) return "translator";
        if (line.startsWith("<publisher>")) return "publisher";
        if (line.startsWith("<name>")) return "publisher_name";
        if (line.startsWith("<city>")) return "city";
        if (line.startsWith("<country>")) return "country";
        if (line.startsWith("</address>")) return "end_address";
        if (line.startsWith("</publisher>")) return "end_publisher";
        if (line.startsWith("<reviews>")) return "reviews";
        if (line.startsWith("<review>")) return "review";
        if (line.startsWith("<user>")) return "user";
        if (line.startsWith("<rating>")) return "rating";
        if (line.startsWith("<comment>")) return "comment";
        if (line.startsWith("</review>")) return "end_review";
        if (line.startsWith("</reviews>")) return "end_reviews";
        if (line.startsWith("</book>")) return "end_book";
        return "unknown";
    }

    public List<Book> getBooks() {
        return books;
    }
}
