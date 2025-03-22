public class Reviews {
    private String user;
    private String rating;
    private String comment;

    public Reviews(String user, String rating, String comment) {
        this.user = user;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUser() {
        return user;
    }

    public String getRating() {
        return rating;
    }

    public String getComment(){
        return comment;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "\n\t -- user: " + getUser() + " \n\t -- rating: " + getRating() + " \n\t -- comment: " + getComment();
    }

}
