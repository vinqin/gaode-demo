package edu.stu.bean;

public class Result {

    private String city = "汕头"; // 商铺所在城市

    private String category = ""; // 商铺分类

    private String storeName = "";

    private String longitude = ""; // 商铺经纬度

    private String grade = ""; // 商户星级

    private String detailedAddress = "";

    private String url = "";

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        int commaIndex = longitude.indexOf(",");
        this.longitude = longitude.substring(0, commaIndex + 1) + "  " + longitude.substring(commaIndex + 1);
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = "广东省汕头市" + detailedAddress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
