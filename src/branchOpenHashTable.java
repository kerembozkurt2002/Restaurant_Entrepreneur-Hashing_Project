import java.util.Iterator;
import java.util.LinkedList;

//In this class, I create the hashtable class where
// I store my branches using the separate chaining and open addressing method.
public class branchOpenHashTable {
    public LinkedList<Branch>[]allList;
    public int currentSize=0;


    public branchOpenHashTable(){
        this.allList=new LinkedList[101];
        for(int i = 0; i < allList.length; i++) {
            allList[i] = new LinkedList<>();
        }
    }

    //Here I am removing the employee, first I find out which branch
    // she is in with my own hash method and direct her to the function in the brunch class to do the operation
    public void leaveEmployee(String city, String district, String name){
        LinkedList<Branch> listtoRemove = allList[hash(city,district)];
        Branch targetBranch =getBrunch(listtoRemove,city,district);
        if(targetBranch ==null){
            System.out.println("There is no such an Brunch");
        }
        else {
            targetBranch.leaveEmployee(name);
        }
    }

    //Here I am doing the employee performance update.
    // First, I find out which branch it is in with my own hash method and direct it to the function in the brunch class to do the operation.
    public void performanceUpdate(String city, String district, String name, int value){
        LinkedList<Branch> targetList=allList[hash(city,district)];
        Branch target=getBrunch(targetList,city,district);
        if(target==null){
            System.out.println("There is no such an Branch");
        }
        else {
            target.performanceUpdate(target,name, value);
        }
    }

    //In this part, I find the place to put the brunch in the hashtable and add the brunch.
    public void insert(Branch branch){
        LinkedList<Branch> listtoInsert = allList[hash(branch.getCity(), branch.getDistrict())];
        if(!listtoInsert.contains(branch)) {
            listtoInsert.add(branch);
            currentSize++;
            if(currentSize > allList.length) {
                rehash();
            }
        }
    }

    //Here, I do the employee insert operation.
    // First, I find out which branch it is in with my own hash method and
    // direct it to the function in the brunch class to perform the operation.
    // I check the total number of employees and rehash it accordingly.
    //If I don't have a brunch to put my employee in, I create my brunch and place it in a hashtable.
    public boolean addEmployee(Branch branch, Employee employee){
        LinkedList<Branch> listtoInsert = allList[hash(branch.getCity(), branch.getDistrict())];
        Branch temp=getBrunch(listtoInsert, branch.getCity(), branch.getDistrict());
        if(temp==null){
            boolean flag = branch.insert(employee);
            listtoInsert.add(branch);
            currentSize++;
            if(currentSize > allList.length) {
                rehash();
            }
            if(flag){
                return true;
            }
            else {
                return false;
            }
        }
        else {
            boolean flag = temp.insert(employee);

            if(flag){
                return true;
            }
            else {
                return false;
            }
        }
    }

    //Here, I find the brunch I want with the city and district information that given to me.
    public Branch getBrunch(LinkedList<Branch> myList, String city, String district){
        Iterator<Branch> iterator=myList.iterator();
        Branch tempBranch;
        while (iterator.hasNext()) {
            tempBranch = iterator.next();
            if (tempBranch.getCity().equals(city) && tempBranch.getDistrict().equals(district)) {
                return tempBranch;
            }
        }
        return null;
    }

    //Here I rehash the hash table according to my brunch size.
    public void rehash(){
        LinkedList<Branch>[] oldList=allList;
        allList = new LinkedList[nextPrime(allList.length * 2)];
        for(int i = 0; i < allList.length; i++) {
            allList[i] = new LinkedList<>();
        }
        currentSize = 0;
        for(LinkedList<Branch> list : oldList) {
            for(Branch item: list) {
                insert(item);
            }
        }

    }

    //Here, I find the location of the brunch in the hashtable with the help of hashCode according to the brunch city and district information.
    public int hash(String city, String district ){
        int val=city.hashCode()+district.hashCode();
        val=val % allList.length;
        if(val<0){
            val+=allList.length;
        }
        return val;
    }

    //Here I find the prime number required to find the table size that I will use in the rehash.
    private static int nextPrime(int currentPrime) {
        if(currentPrime % 2 == 0) {
            currentPrime++;
        }
        while(!isPrime(currentPrime)) {
            currentPrime += 2;
        }
        return currentPrime;
    }

    private static boolean isPrime(int n) {
        if(n == 2 || n == 3) {
            return true;
        }
        if (n == 1 || n % 2 == 0) {
            return false;
        }
        for(int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
