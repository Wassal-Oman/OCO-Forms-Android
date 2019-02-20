package om.org.oco.androidocoforms.models;

public class Beneficiary {

    // attributes
    private int id;
    private int fileNumber;
    private String name;
    private String date_of_birth;
    private String nationality;
    private String phone;
    private String mobile;
    private String passport;
    private int civilId;
    private String address;
    private String createdAt;
    private String updatedAt;
    private int categoryId;
    private int wilayatId;

    // constructors
    public Beneficiary() {

    }

    public Beneficiary(int id, int fileNumber, String name, String date_of_birth, String nationality, String phone, String mobile, String passport, int civilId, String address, String createdAt, String updatedAt, int categoryId, int wilayatId) {
        this.id = id;
        this.fileNumber = fileNumber;
        this.name = name;
        this.date_of_birth = date_of_birth;
        this.nationality = nationality;
        this.phone = phone;
        this.mobile = mobile;
        this.passport = passport;
        this.civilId = civilId;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categoryId = categoryId;
        this.wilayatId = wilayatId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public int getCivilId() {
        return civilId;
    }

    public void setCivilId(int civilId) {
        this.civilId = civilId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getWilayatId() {
        return wilayatId;
    }

    public void setWilayatId(int wilayatId) {
        this.wilayatId = wilayatId;
    }
}
