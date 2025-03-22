
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class XMLWriter {
    public static void writeBooksToXML(List<Book> books, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("<catalog>\n");
            for (Book book : books) {
                writer.write("  <book id=\"" + book.getId() + "\">\n");
                writer.write("    <title>" + book.getTitle() + "</title>\n");
                writer.write("    <author>" + book.getAuthor() + "</author>\n");
                writer.write("    <year>" + book.getYear() + "</year>\n");
                writer.write("    <genre>" + book.getGenre() + "</genre>\n");
                if (book.getPrice() != null) {
                    writer.write("    <price currency=\"" + book.getPrice().getClass() + "\">" + book.getPrice().getClass() + "</price>\n");
                }
                writer.write("    <format>" + book.getFormat() + "</format>\n");
                writer.write("    <isbn>" + book.getIsbn() + "</isbn>\n");
                if (book.getTranslator() != null && !book.getTranslator().isEmpty()) {
                    writer.write("    <translator>" + book.getTranslator() + "</translator>\n");
                }
                if (book.getPublisher() != null) {
                    writer.write("    <publisher>\n");
                    writer.write("      <name>" + book.getPublisher().getName() + "</name>\n");
                    if (book.getPublisher().getAddress() != null) {
                        writer.write("      <address>\n");
                        writer.write("        <city>" + book.getPublisher().getAddress().getCity() + "</city>\n");
                        writer.write("        <country>" + book.getPublisher().getAddress().getCountry() + "</country>\n");
                        writer.write("      </address>\n");
                    }
                    writer.write("    </publisher>\n");
                }
                if (book.getReviews() != null && !book.getReviews().isEmpty()) {
                    writer.write("    <reviews>\n");
                    for (Reviews review : book.getReviews()) {
                        writer.write("      <review>\n");
                        writer.write("        <user>" + review.getUser() + "</user>\n");
                        writer.write("        <rating>" + review.getRating() + "</rating>\n");
                        writer.write("        <comment>" + review.getComment() + "</comment>\n");
                        writer.write("      </review>\n");
                    }
                    writer.write("    </reviews>\n");
                }
                writer.write("  </book>\n");
            }
            writer.write("</catalog>\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
