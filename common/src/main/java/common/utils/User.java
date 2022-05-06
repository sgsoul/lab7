package common.utils;

//import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;

public class User implements Serializable {
    //todo почему такие цифры
    //@Serial
    //private static final long serialVersionUID = 8347617547303456361L;

    private final String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        try {
            this.password = SHA384Generator.hash(password);
        } catch (RuntimeException e) {
            this.password = null;
        }
    }

    public User(String username) {
        this.username = username;
    }

    public String getLogin() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return getLogin();
    }

    public static class SHA384Generator {
        public static String hash(String str) throws RuntimeException {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-384");
                byte[] messageDigest = md.digest(str.getBytes());
                BigInteger no = new BigInteger(1, messageDigest);
                StringBuilder hashtext = new StringBuilder(no.toString(16));
                while (hashtext.length() < 32) {
                    hashtext.insert(0, "0");
                }
                return hashtext.toString();
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }

}