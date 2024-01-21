import java.util.LinkedList;
import java.util.Iterator;

//In this class, I create the hashtable class where
// I store my employees using the separate chaining and open addressing method.
public class employeeOpenHashTable {
    public LinkedList<Employee>[]allList;
    public int currentSize=0;
    public int numOfCourier=0;
    public int numOfCashier=0;
    public int numOfManager=0;
    public int numOfCook=0;
    public String managerName;


    public employeeOpenHashTable(){
        this.allList=new LinkedList[101];
        for(int i = 0; i < allList.length; i++) {
            allList[i] = new LinkedList<>();
        }
    }

    //Here, I first find the location of the employee and print it if there is no such employee.
    // If there is, I make the desired amount of performance updates,
    // calculate the promotion point and bonus, and then check whether I can promote or dismiss it.
    public void performanceUpdate(Branch branch, String name, int value){
        Employee target=hashEmployee(name);
        if(target==null){
            System.out.println("There is no such employee.");
        }
        else {
            if(value>0){
                int promotionPoint=value/200;
                int bonus=value%200;
                branch.monthlyBonus+=bonus;
                branch.totalBonus+=bonus;
                target.promotionPoint+=promotionPoint;
                branch.checkDismissalAndPromote(target);
            }
            else {
                int promotionPoint=value/200;
                target.promotionPoint+=promotionPoint;
                branch.checkDismissalAndPromote(target);

            }
        }
    }

    //Here, I first find the location of the employee and print it if there is no such employee. If there is, I deleted the employee from hashtable.
    public void remove(Employee employee){
        LinkedList<Employee> listtoRemove = allList[hash(employee.getName())];
        Employee target=getEmployee(listtoRemove,employee.getName());

        if(target==null){
            System.out.println("There is no such an employee");
        }
        else {
            changeNumOfEmployees(target,false);
            listtoRemove.remove(target);
            currentSize--;
        }
    }

    //In this part, I find the location of the employee in the hashtable by entering his name into the hashCode.
    public Employee hashEmployee(String name){
        LinkedList<Employee> empList =allList[hash(name)];
        Employee employee=getEmployee(empList,name);
        return employee;
    }

    public int hash(String name){
        int val=name.hashCode();
        val=val%allList.length;
        if(val<0){
            val+=allList.length;
        }
        return val;
    }
    public Employee getEmployee(LinkedList<Employee> myList,String nanme){
//        ListIterator<Employee> iterator=myList.listIterator();
        Iterator<Employee> iterator=myList.iterator();
        Employee temp;
        while (iterator.hasNext()) {
            temp=iterator.next();
            if (temp.getName().equals(nanme)) {
                return temp;
            }
        }
        return null;
    }

    //In this section, I find the location where I need to put the employee in the hashtable, then place it, check the size of my hashtable, and rehash it if necessary.
    public boolean insert(Employee employee){
        LinkedList<Employee> listtoInsert = allList[hash(employee.getName())];
        if(getEmployee(listtoInsert,employee.getName())==null) {
            listtoInsert.add(employee);
            currentSize++;
            changeNumOfEmployees(employee,true);
            if(currentSize > allList.length) {
                rehash();
            }
            return true;
        }
        else {
            return false;
        }
    }

    //Here I rehash the hash table according to number of employees.
    public void rehash(){
        LinkedList<Employee>[] oldList=allList;
        allList = new LinkedList[nextPrime(allList.length * 2)];
        for(int i = 0; i < allList.length; i++) {
            allList[i] = new LinkedList<>();
        }
        currentSize = 0;
        numOfCourier=0;
        numOfCook=0;
        numOfCashier=0;
        numOfManager=0;
        for(LinkedList<Employee> list : oldList) {
            for(Employee item: list) {
                insert(item);
            }
        }

    }

    //In this section, I change the total number of employees with the profession of the given employee belonging to that brunch, depending on the add or leave operation.
    public void changeNumOfEmployees(Employee employee,boolean operation){
        String profession=employee.getProfession();
        if(operation){
            if(profession.equals("COURIER")){
                numOfCourier+=1;
            }
            else if(profession.equals("CASHIER")){
                numOfCashier+=1;
            }
            else if(profession.equals("COOK")){
                numOfCook+=1;
            }
            else if(profession.equals("MANAGER")){
                numOfManager+=1;
                managerName=employee.getName();
            }
        }
        else {
            if(profession.equals("COURIER")){
                numOfCourier-=1;
            }
            else if(profession.equals("CASHIER")){
                numOfCashier-=1;
            }
            else if(profession.equals("COOK")){
                numOfCook-=1;
            }
            else if(profession.equals("MANAGER")){
                numOfManager-=1;
            }
        }

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
