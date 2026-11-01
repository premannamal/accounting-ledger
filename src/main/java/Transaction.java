public class Transaction {
    public String date;
    public String time;
    public String description;
    public String vendor;
    public float amount;

    //Constructor method
    public Transaction(String date,String time,String description,String vendor,float amount)
    {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    //Add gettor functions
    public String getdate() { return this.date; }
    public String gettime() { return this.time; }
    public String getdescription() { return this.description; }
    public String getvendor() { return this.vendor; }
    public float getanount() { return this.amount; }

}
