package Model;

public class Signature {
    private int id;
    private String signature;
    private User user_id;

    public Signature(int id, String signature, User user_id) {
        this.id = id;
        this.signature = signature;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Signature{" +
                "id=" + id +
                ", signature='" + signature + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
