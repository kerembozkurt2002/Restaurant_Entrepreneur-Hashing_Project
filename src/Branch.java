import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

//In this class, I hold the data fields needed by my brunch objects and create methods for some of the operations I want to perform.
public class Branch {
    private final String city;
    private final String district;
    private employeeOpenHashTable empList;  //Employees' Hashtable
    public Queue<String> leaveCourier=new LinkedList<String>();     // dismissal queue of Couriers
    public Queue<String> leaveCashier=new LinkedList<String>();     // dismissal queue of Cashiers
    public Queue<String> leaveCook=new LinkedList<String>();        // dismissal queue of Cooks
    public Queue<String> leaveManager=new LinkedList<String>();     // dismissal queue of Manager
    public Queue<String> promoteFromCashierToCook=new LinkedList<String>();     // promote queue of Cashiers to be Cook
    public Queue<String> promoteFromCookToManager=new LinkedList<String>();     // promote queue of Cooks to be Manager
    public int monthlyBonus;
    public int totalBonus;
    public String deletedManagerFromQueue;
    public String promotedNameFromCashierToCook;
    public Employee newCookFromCashierEmployee;

    public Branch(String city, String district) {
        this.city = city;
        this.district = district;
        this.empList=new employeeOpenHashTable();
        this.monthlyBonus=0;
        this.totalBonus=0;
    }

    //Here I am making the operation which makes a manager from Cook.
    public boolean makeThePromoteCookToManager(Queue<String> myQueue){
        Iterator<String> iterator=myQueue.iterator();
        while (iterator.hasNext()){
            String currentName=iterator.next();
            Employee temp = empList.hashEmployee(currentName);
            if(temp==null){
                iterator.remove();
            }
            else if (temp.promotionPoint >= 10) {
                temp.setProfession("MANAGER");
                temp.promotionPoint -= 10;
                empList.numOfCook -= 1;
                empList.numOfManager += 1;
                empList.managerName=currentName;
                deletedManagerFromQueue=currentName;
                iterator.remove();
                return true;
            }
            else {
                iterator.remove();
            }
        }
        return false;

    }

    //Here I am making the operation which makes a Cook from Cashier.
    public boolean makeThePromoteCashierToCook(Queue<String> myQueue){
        Iterator<String> iterator=myQueue.iterator();
        while (iterator.hasNext()){
            String currentName=iterator.next();
            Employee temp = empList.hashEmployee(currentName);
            if(temp==null){
                iterator.remove();
            }
            else if (temp.promotionPoint >= 3 && empList.numOfCashier > 1) {
                promotedNameFromCashierToCook=temp.getName();
                temp.setProfession("COOK");
                temp.promotionPoint -= 3;

                empList.numOfCashier -= 1;
                empList.numOfCook += 1;

                if(temp.promotionPoint>=10 && (leaveManager.peek()!=null)){
                    String oldManagerName=leaveManager.remove();
                    Employee oldManager=empList.hashEmployee(oldManagerName);
                    temp.promotionPoint -= 10;
                    empList.numOfCook -= 1;
                    empList.numOfManager += 1;
                    empList.managerName=currentName;
                    empList.remove(oldManager);
                    temp.setProfession("MANAGER");
                }
                else if(temp.promotionPoint>=10){
                    promoteFromCookToManager.add(temp.getName());
                }

                newCookFromCashierEmployee=temp;
                iterator.remove();
                return true;
            }
            else {
                if(temp.promotionPoint<3){
                    iterator.remove();
                }
            }
        }
        return false;
    }

    //In this section, I check to see if I can perform all the dismissal and promotion operations that could not be done.
    //I browse all queues through the Iterator and do all the dismissal and promotion operations I can.
    // In all dismissal and promotion transactions that I cannot complete,
    // if the employee's promotion point is not at the desired level, I remove it from the queue,
    // if it has not changed, I do not touch it.
    public void checkQueues(){

        Iterator<String> iteratorLeaveCourier=leaveCourier.iterator();
        while (iteratorLeaveCourier.hasNext()){
            String name=iteratorLeaveCourier.next();
            Employee target = empList.hashEmployee(name);
            if(target==null){
                iteratorLeaveCourier.remove();
            }
            //
            else if (leaveCourier.contains(target.getName()) && (target.promotionPoint>(-5))){
                iteratorLeaveCourier.remove();
            }
            //
            else if (target.promotionPoint <= -5) {
                if (empList.numOfCourier > 1) {
                    System.out.println(target.getName() + " is dismissed from branch: " + target.getDistrict()+".");
                    empList.remove(target);
                    iteratorLeaveCourier.remove();
                }
            }
            else {
                iteratorLeaveCourier.remove();
            }
        }

        //---------------------------------------------------------------------------------------------------------

        Iterator<String> iteratorLeaveCashier=leaveCashier.iterator();
        while (iteratorLeaveCashier.hasNext()){
            String name=iteratorLeaveCashier.next();
            Employee target = empList.hashEmployee(name);

            if(target==null){
                iteratorLeaveCashier.remove();
            }
            //
            else if(leaveCashier.contains(target.getName()) && (target.promotionPoint>(-5))){
                iteratorLeaveCashier.remove();
            }
            //
            else if (target.promotionPoint <= -5) {
                if (empList.numOfCashier > 1) {
                    System.out.println(target.getName() + " is dismissed from branch: " + target.getDistrict()+".");
                    empList.remove(target);
                    iteratorLeaveCashier.remove();
                }
            }
            else {
                iteratorLeaveCashier.remove();
            }
        }

        //---------------------------------------------------------------------------------------------------------

        Iterator<String> iteratorPromoteFromCashierToCook=promoteFromCashierToCook.iterator();
        while (iteratorPromoteFromCashierToCook.hasNext()){
            String name=iteratorPromoteFromCashierToCook.next();
            Employee target = empList.hashEmployee(name);
            if (target==null){
                iteratorPromoteFromCashierToCook.remove();
            }
            //
            else if(promoteFromCashierToCook.contains(target.getName()) && (target.promotionPoint<3)){
                iteratorPromoteFromCashierToCook.remove();
            }
            else if(leaveCashier.contains(target.getName()) && (target.promotionPoint>(-5))){
                leaveCashier.remove(target.getName());
            }
            //
            if (target==null){
                continue;
            }
            else if (target.promotionPoint >= 3) {
                if (empList.numOfCashier > 1) {
                    System.out.println(target.getName() + " is promoted from Cashier to Cook.");
                    target.setProfession("COOK");
                    target.promotionPoint -= 3;


                    empList.numOfCashier -= 1;
                    empList.numOfCook += 1;
                    iteratorPromoteFromCashierToCook.remove();
                    //
                    if(target.promotionPoint>=10 && (leaveManager.peek()!=null)){
                        String oldManagerName=leaveManager.peek();
                        Employee oldManager=empList.hashEmployee(oldManagerName);
                        if(oldManager.promotionPoint<=(-5)){
                            leaveManager.remove();
                            target.promotionPoint -= 10;
                            empList.numOfCook -= 1;
                            empList.numOfManager += 1;
                            empList.managerName=name;
                            empList.remove(oldManager);
                            target.setProfession("MANAGER");
                        }
                    }
                    else if(target.promotionPoint>=10){
                        promoteFromCookToManager.add(target.getName());
                    }
                    //
                }
            }
            else {
                iteratorPromoteFromCashierToCook.remove();
            }
        }

        //---------------------------------------------------------------------------------------------------------

        Iterator<String> iteratorLeaveCook=leaveCook.iterator();
        while (iteratorLeaveCook.hasNext()){
            String name=iteratorLeaveCook.next();
            Employee target = empList.hashEmployee(name);
            if(target==null){
                iteratorLeaveCook.remove();
            }
            else if (target.promotionPoint <= -5) {
                if (empList.numOfCook > 1) {
                    System.out.println(target.getName() + " is dismissed from branch: " + target.getDistrict()+".");
                    empList.remove(target);
                    iteratorLeaveCook.remove();

                }
                else if (empList.numOfCook == 1 && (promoteFromCashierToCook.peek() != null)) {
                    boolean flag = makeThePromoteCashierToCook(promoteFromCashierToCook);
                    if (flag) {
                        System.out.println(promotedNameFromCashierToCook+" is promoted from Cashier to Cook.");
                        checkDismissalAndPromote(newCookFromCashierEmployee);
                        System.out.println(target.getName() + " is dismissed from branch: " + target.getDistrict()+".");
                        empList.remove(target);
                        iteratorLeaveCook.remove();
                    }
                }
            }
            else {
                iteratorLeaveCook.remove();
            }
        }

        //---------------------------------------------------------------------------------------------------------

        Iterator<String> iterator = promoteFromCookToManager.iterator();
        while (iterator.hasNext()){
            String name1=iterator.next();
            Employee target = empList.hashEmployee(name1);
            if (target==null){
                iterator.remove();
            }
            else if(target.promotionPoint>=10) {
                if(empList.numOfCook>1 && (leaveManager.peek()!=null)){
                    Iterator<String> iteratorLeaveManager = leaveManager.iterator();
                    while (iteratorLeaveManager.hasNext()) {
                        String name=iteratorLeaveManager.next();
                        Employee temp=empList.hashEmployee(name);
                        if(temp==null){
                            iteratorLeaveManager.remove();
                        }
                        else if(temp.promotionPoint<=-5){
                            empList.managerName=target.getName();
                            target.setProfession("MANAGER");
                            target.promotionPoint-=10;
                            empList.numOfManager+=1;
                            empList.numOfCook-=1;
                            System.out.println(temp.getName() + " is dismissed from branch: " + temp.getDistrict()+".");
                            System.out.println(target.getName()+" is promoted from Cook to Manager.");
                            empList.remove(temp);
                            iteratorLeaveManager.remove();
                            iterator.remove();
                        }
                        else {
                            iteratorLeaveManager.remove();
                        }
                    }
                }
                else if(empList.numOfCook==1 && (leaveManager.peek()!=null) ) {
                    boolean flag = makeThePromoteCashierToCook(promoteFromCashierToCook);
                    if (flag) {
                        System.out.println(promotedNameFromCashierToCook+" is promoted from Cashier to Cook.");
                        checkDismissalAndPromote(newCookFromCashierEmployee);
                        Iterator<String> iteratorLeaveManager = leaveManager.iterator();
                        while (iteratorLeaveManager.hasNext()){
                            String name=iteratorLeaveManager.next();
                            Employee temp = empList.hashEmployee(name);
                            if (temp.promotionPoint <= -5) {
                                empList.managerName = target.getName();
                                target.setProfession("MANAGER");
                                target.promotionPoint -= 10;
                                empList.numOfManager += 1;
                                empList.numOfCook -= 1;
                                System.out.println(temp.getName() + " is dismissed from branch: " + temp.getDistrict()+".");
                                System.out.println(target.getName() + " is promoted from Cook to Manager.");
                                empList.remove(temp);
                                iteratorLeaveManager.remove();
                                iterator.remove();
                            }
                            else {
                                iteratorLeaveManager.remove();
                            }
                        }
                    }
                }
            }
            else {
                iterator.remove();
            }
        }



        //---------------------------------------------------------------------------------------------------------

        Iterator<String> iteratorLeaveManager=leaveManager.iterator();
        while (iteratorLeaveManager.hasNext()){
            String name1=iteratorLeaveManager.next();
            Employee target = empList.hashEmployee(name1);
            if(target==null){
                iteratorLeaveManager.remove();
            }
            else if (target.promotionPoint <= -5) {
                if (empList.numOfCook > 1 && (promoteFromCookToManager.peek() != null)) {
                    boolean flag = makeThePromoteCookToManager(promoteFromCookToManager);
                    if (flag) {
                        System.out.println(target.getName() + " is dismissed from branch: " + target.getDistrict()+".");
                        System.out.println(empList.managerName + " is promoted from Cook to Manager.");
                        empList.remove(target);
                        iteratorLeaveManager.remove();
                    }
                }
            }
            else {
                iteratorLeaveManager.remove();
            }
        }

        //---------------------------------------------------------------------------------------------------------
    }

    //In this section, if I need to promote or dismiss according to the profession of the employee given to me,
    // I do it. If I cannot, I put it in the queue I need to put it in.
    public void checkDismissalAndPromote(Employee target) {
        String profession = target.getProfession();
        if (profession.equals("COURIER")) {
            if (leaveCourier.contains(target.getName()) && (target.promotionPoint>(-5))){
                leaveCourier.remove(target.getName());
            }
            else if(target.promotionPoint<=-5){
                if (empList.numOfCourier > 1) {
                    System.out.println(target.getName()+" is dismissed from branch: "+target.getDistrict()+".");
                    empList.remove(target);
                } else {
                    if(!leaveCourier.contains(target.getName())){
                        leaveCourier.add(target.getName());
                    }
                }
            }
        }
        else if (profession.equals("CASHIER")) {
            if(promoteFromCashierToCook.contains(target.getName()) && (target.promotionPoint<3)){
                promoteFromCashierToCook.remove(target.getName());
            }
            if(leaveCashier.contains(target.getName()) && (target.promotionPoint>(-5))){
                leaveCashier.remove(target.getName());
            }

            if (target.promotionPoint <= -5) {
                if (empList.numOfCashier > 1) {
                    System.out.println(target.getName()+" is dismissed from branch: "+target.getDistrict()+".");
                    empList.remove(target);
                } else {
                    if(!leaveCashier.contains(target.getName())){
                        leaveCashier.add(target.getName());
                    }
                }
            }
            else if (target.promotionPoint >= 3) {
                if (empList.numOfCashier > 1) {
                    System.out.println(target.getName()+" is promoted from Cashier to Cook.");
                    target.setProfession("COOK");
                    target.promotionPoint-=3;

                    empList.numOfCashier-=1;
                    empList.numOfCook+=1;

                    if(target.promotionPoint>=10 && (leaveManager.peek()!=null)){
                        String oldManagerName=leaveManager.remove();
                        Employee oldManager=empList.hashEmployee(oldManagerName);
                        target.promotionPoint -= 10;
                        empList.numOfCook -= 1;
                        empList.numOfManager += 1;
                        empList.managerName=target.getName();
                        empList.remove(oldManager);
                        target.setProfession("MANAGER");
                        System.out.println(target.getName()+" is promoted from Cook to Manager.");
                    }
                    else if(target.promotionPoint>=10){
                        promoteFromCookToManager.add(target.getName());
                    }

                }
                else {
                    if(!promoteFromCashierToCook.contains(target.getName())){
                        promoteFromCashierToCook.add(target.getName());
                    }
                }
            }
        }

        else if(profession.equals("COOK")){
            if(promoteFromCookToManager.contains(target.getName()) && (target.promotionPoint<10)){
                promoteFromCookToManager.remove(target.getName());
            }
            else if(leaveCook.contains(target.getName()) && (target.promotionPoint>(-5))){
                leaveCook.remove(target.getName());
            }

            else if (target.promotionPoint <= -5) {
                if(empList.numOfCook>1){
                    System.out.println(target.getName()+" is dismissed from branch: "+target.getDistrict()+".");
                    empList.remove(target);
                }
                else if(empList.numOfCook==1 && (promoteFromCashierToCook.peek()!=null) ) {
                    boolean flag = makeThePromoteCashierToCook(promoteFromCashierToCook);
                    if (flag) {
                        System.out.println(promotedNameFromCashierToCook+" is promoted from Cashier to Cook.");
                        checkDismissalAndPromote(newCookFromCashierEmployee);
                        System.out.println(target.getName()+" is dismissed from branch: "+target.getDistrict()+".");
                        empList.remove(target);
                    }
                    else {
                        if(!leaveCook.contains(target.getName())){
                            leaveCook.add(target.getName());
                        }
                    }
                }
                else {
                    if(!leaveCook.contains(target.getName())){
                        leaveCook.add(target.getName());
                    }
                }
            }
            else if(target.promotionPoint>=10){
                if(empList.numOfCook>1 && (leaveManager.peek()!=null)){
                    Iterator<String> iteratorLeaveManager=leaveManager.iterator();
                    while (iteratorLeaveManager.hasNext()){
                        String name=iteratorLeaveManager.next();
                        Employee temp = empList.hashEmployee(name);
                        if(temp==null){
                            iteratorLeaveManager.remove();
                        }
                        else if(temp.promotionPoint<=-5){
                            iteratorLeaveManager.remove();
                            empList.managerName=target.getName();
                            System.out.println(temp.getName()+" is dismissed from branch: "+temp.getDistrict()+".");
                            System.out.println(target.getName()+" is promoted from Cook to Manager.");
                            target.setProfession("MANAGER");
                            target.promotionPoint-=10;
                            empList.numOfManager+=1;
                            empList.numOfCook-=1;
                            empList.remove(temp);

                        }
                        else {
                            iteratorLeaveManager.remove();
                        }
                    }
                }
                else if(empList.numOfCook==1 && (leaveManager.peek()!=null) ){
                    boolean flag=makeThePromoteCashierToCook(promoteFromCashierToCook);
                    if(flag){
                        System.out.println(promotedNameFromCashierToCook+" is promoted from Cashier to Cook.");
                        checkDismissalAndPromote(newCookFromCashierEmployee);
                        Iterator<String> iteratorLeaveManager=leaveManager.iterator();
                        while (iteratorLeaveManager.hasNext()){
                            String name=iteratorLeaveManager.next();
                            Employee temp = empList.hashEmployee(name);
                            if(temp==null){
                                iteratorLeaveManager.remove();
                            }
                            else if(temp.promotionPoint<=-5){
                                iteratorLeaveManager.remove();
                                empList.managerName=target.getName();
                                System.out.println(temp.getName()+" is dismissed from branch: "+temp.getDistrict()+".");
                                System.out.println(target.getName()+" is promoted from Cook to Manager.");
                                target.setProfession("MANAGER");
                                target.promotionPoint-=10;
                                empList.numOfManager+=1;
                                empList.numOfCook-=1;
                                empList.remove(temp);
                            }
                            else {
                                iteratorLeaveManager.remove();
                            }
                        }
                    }
                    else {
                        if(!promoteFromCookToManager.contains(target.getName())){
                            promoteFromCookToManager.add(target.getName());
                        }
                    }
                }
                else {
                    if(!promoteFromCookToManager.contains(target.getName())){
                        promoteFromCookToManager.add(target.getName());
                    }
                }
            }
        }

        else if(profession.equals("MANAGER")){
            if(leaveManager.contains(target.getName()) && (target.promotionPoint>(-5))){
                leaveManager.remove(target.getName());
            }
            if (target.promotionPoint <= -5) {
                if(empList.numOfCook>1 && (promoteFromCookToManager.peek()!=null)){
                    boolean flag=makeThePromoteCookToManager(promoteFromCookToManager);
                    if(flag){
                        System.out.println(target.getName()+" is dismissed from branch: "+target.getDistrict()+".");
                        System.out.println(empList.managerName+" is promoted from Cook to Manager.");
                        empList.remove(target);

                    }
                    else {
                        if(!leaveManager.contains(target.getName())){
                            leaveManager.add(target.getName());
                        }
                    }
                }
                else {
                    if(!leaveManager.contains(target.getName())){
                        leaveManager.add(target.getName());
                    }
                }
            }
        }
    }

    //Here, if the employee who wants to leave the job can leave the job,
    // I reserve it, and if not, I give a bonus so that the employee does not leave the job,
    // provided that the promotion point is greater than -5.
    public void leaveEmployee(String name){
        Employee targetEmployee=empList.hashEmployee(name);
        if(targetEmployee==null){
            System.out.println("There is no such employee.");
        }
        else {
            String profession=targetEmployee.getProfession();
            if(profession.equals("COURIER")){
                if(empList.numOfCourier>1){
                    System.out.println(targetEmployee.getName()+" is leaving from branch: "+targetEmployee.getDistrict()+".");
                    empList.remove(targetEmployee);
                }
                else {
                    if(targetEmployee.promotionPoint>-5) {
                        monthlyBonus += 200;
                        totalBonus += 200;
                    }
                }
            }
            else if(profession.equals("CASHIER")){
                if(empList.numOfCashier>1){
                    System.out.println(targetEmployee.getName()+" is leaving from branch: "+targetEmployee.getDistrict()+".");
                    empList.remove(targetEmployee);
                }
                else {
                    if(targetEmployee.promotionPoint>-5) {
                        monthlyBonus += 200;
                        totalBonus += 200;

                    }
                }
            }
            else if(profession.equals("COOK")){
                if(empList.numOfCook>1){
                    System.out.println(targetEmployee.getName()+" is leaving from branch: "+targetEmployee.getDistrict()+".");
                    empList.remove(targetEmployee);
                }
                else if(empList.numOfCook==1 && (promoteFromCashierToCook.peek()!=null) ){
                    boolean flag=makeThePromoteCashierToCook(promoteFromCashierToCook);
                    if(flag){
                        System.out.println(promotedNameFromCashierToCook+" is promoted from Cashier to Cook.");
                        checkDismissalAndPromote(newCookFromCashierEmployee);
                        System.out.println(targetEmployee.getName()+" is leaving from branch: "+targetEmployee.getDistrict()+".");
                        empList.remove(targetEmployee);
                    }
                    else {
                        if(targetEmployee.promotionPoint>-5) {
                            monthlyBonus += 200;
                            totalBonus += 200;
                        }
                    }
                }
                else {
                    if(targetEmployee.promotionPoint>-5) {
                        monthlyBonus += 200;
                        totalBonus += 200;
                    }
                }
            }
            else if(profession.equals("MANAGER")){
                if(empList.numOfCook>1 && (promoteFromCookToManager.peek()!=null)){
                    boolean flag=makeThePromoteCookToManager(promoteFromCookToManager);
                    if(flag){
                        System.out.println(targetEmployee.getName()+" is leaving from branch: "+targetEmployee.getDistrict()+".");
                        System.out.println(empList.managerName+" is promoted from Cook to Manager.");
                        empList.remove(targetEmployee);
                    }
                    else {
                        if(targetEmployee.promotionPoint>-5) {
                            monthlyBonus += 200;
                            totalBonus += 200;
                        }
                    }
                }
                else {
                    if(targetEmployee.promotionPoint>-5) {
                        monthlyBonus += 200;
                        totalBonus += 200;
                    }
                }
            }

        }
    }

    // Here, I direct it to the employee hashtable to perform the performance update of the employee given here.
    public void performanceUpdate(Branch branch, String name, int value){
        branch.empList.performanceUpdate(branch,name,value);
    }

    //Here I am directing the employee hashtable to insert the employee.
    public boolean insert(Employee employee){
        boolean flag=empList.insert(employee);
        if (flag){
            return true;
        }
        else {
            return false;
        }
    }

    public void remove(Employee employee){
        empList.remove(employee);
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }
    public employeeOpenHashTable getEmpList() {
        return empList;
    }
}

























