package jp.ac.jec.a16cm0209.staffmanager;

/**
 * Created by nguyenhiep on 10/21/16 AD.
 */
public class Staff {
    public int id;
    public String name;
    public String phonenumber;
    public byte[] image;
    public String email;
    public String address;

    public Staff(int id, String name, String phonenumber, byte[] image, String email, String address) {
        this.id = id;
        this.name = name;
        this.phonenumber = phonenumber;
        this.image = image;
        this.email = email;
        this.address = address;
    }
}
