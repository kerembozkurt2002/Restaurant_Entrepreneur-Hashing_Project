//In this class, I create the object class with the necessary data fields
// for my employee objects that I will keep in the employee hashtable.
public class Employee {
    private final String city;
    private final String district;
    private final String name;
    private String profession;
    public int promotionPoint;


    public Employee(String city, String district, String name, String profession) {
        this.city = city;
        this.district = district;
        this.name = name;
        this.profession = profession;
    }

    public String getDistrict() {
        return district;
    }

    public String getName() {
        return name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}


