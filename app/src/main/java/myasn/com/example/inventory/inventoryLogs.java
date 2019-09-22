package myasn.com.example.inventory;
/*
 *****************************************************
 * @author Willyana - 12067867
 *****************************************************
 */

public class inventoryLogs {

    private String name;
    private String time;
    private String longitude;
    private String latitude;
    private String referenceNum;
    private String description;

    // Required empty public constructor
    inventoryLogs() {
    }

    //Constructor
    inventoryLogs(String name, String time, String longitude, String latitude, String referenceNum, String description) {
        this.name = name;
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
        this.referenceNum = referenceNum;
        this.description = description;
    }
    //----------------Method for get and set object---------------------
    public String getName() {
        return name;
    } //get name method

    public void setName(String name) {
        this.name = name;
    } //set name method

    public String getTime() {
        return time;
    }//get time method

    public void setTime(String time) {
        this.time = time;
    } //set time method

    public String getLongitude() { return longitude; }//get longitude method

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }//set longitude method

    public String getLatitude() { return latitude; }//get latitude method

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }//set latitude method

    public String getReferenceNum() {
        return referenceNum;
    }//get reference method

    public void setReferenceNum(String referenceNum) {
        this.referenceNum = referenceNum;
    } //set reference method

    public String getDescription() {
        return description;
    }//get description method

    public void setDescription(String description) {
        this.description = description;
    }//set description method

    @Override
    public String toString() {//to string method
        return name + " " + time + " \nLongitude: " + longitude + " Latitude: " + latitude + " " + referenceNum + " " + description;
    }// end method String toString
}//end InventoryLogs class
