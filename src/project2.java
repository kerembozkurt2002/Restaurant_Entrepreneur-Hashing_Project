import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.LinkedList;
import java.io.*;


/**
 * In this project, I run a lahmacun restaurant chain and accurately process the data about the employees and branches of this company.
 * In this class, I read the input files and direct them to the necessary operations via my brunchOpenHashtable.
 * @author Kerem Bozkurt, Student ID: 2020400177
 * @since Date: 26.11.2023
 */

public class project2 {
    public static void main(String[] args) throws FileNotFoundException {
        //In this section, I read the input files in text format through
        // the terminal and print them into a text file with the name I received from the terminal.
        PrintStream out = new PrintStream(new FileOutputStream(args[2]));
        System.setOut(out);
        File fileInitial =new File(args[0]);
        File fileInput=new File(args[1]);
        Scanner scanInitial=new Scanner(fileInitial);
        Scanner scanInput=new Scanner(fileInput);

        //Here, I initialize the hashtable that I prepared specifically where I keep all the information.
        branchOpenHashTable branchOpenHashTable =new branchOpenHashTable();

        //Here I read the input in the initial state and add the branches and employees to my own hashtable.
        while (scanInitial.hasNextLine()){
            String [] temp=scanInitial.nextLine().split(", ");
            String city=temp[0];
            String district=temp[1];
            String name=temp[2];
            String profession=temp[3];

            Branch tempBranch =new Branch(city,district);
            Employee tempEmployee=new Employee(city,district,name,profession);
            branchOpenHashTable.addEmployee(tempBranch,tempEmployee);
        }

        //In this part, I read the second input file and perform the operations in the file
        // such as performance update, add leave, print manager of the branch, print all bonuses of the branch, print this month of bonus of the branch.
        while (scanInput.hasNextLine()){
            String holder = scanInput.nextLine();
            String [] temp= holder.split(": ");

            //When this if happens, it is a new month, so I reset the monthly bonuses of all branches.
            if(temp.length==1 && temp[0].length()>0){
                for (LinkedList<Branch> myList : branchOpenHashTable.allList){
                    for (Branch tempBranch : myList) {
                        tempBranch.monthlyBonus = 0;
                    }
                }
            }

            //In this section, I perform the performance update of the given employee.
            if(temp[0].equals("PERFORMANCE_UPDATE")){
                String [] temp2=temp[1].split(", ");
                branchOpenHashTable.performanceUpdate(temp2[0],temp2[1],temp2[2],Integer.parseInt(temp2[3]));
            }

            //In this section, I print the monthly bonus of the given branch for that month.
            else if(temp[0].equals("PRINT_MONTHLY_BONUSES")){
                String [] temp2=temp[1].split(", ");
                int hasBrunch= branchOpenHashTable.hash(temp2[0],temp2[1]);
                LinkedList<Branch> listToPrint= branchOpenHashTable.allList[hasBrunch];
                Branch printToBonusBranch = branchOpenHashTable.getBrunch(listToPrint,temp2[0],temp2[1]);
                System.out.println("Total bonuses for the "+ printToBonusBranch.getDistrict() +" branch this month are: "+ printToBonusBranch.monthlyBonus);

            }

            //I am fulfilling the employee's leave request given in this section.
            else if(temp[0].equals("LEAVE")){
                String [] temp2=temp[1].split(", ");
                branchOpenHashTable.leaveEmployee(temp2[0],temp2[1],temp2[2]);
            }

            //In this section, I print the manager belonging to the given branch.
            else if(temp[0].equals("PRINT_MANAGER")){
                String [] temp2=temp[1].split(", ");
                int hasBrunch= branchOpenHashTable.hash(temp2[0],temp2[1]);
                LinkedList<Branch> listToPrint= branchOpenHashTable.allList[hasBrunch];
                Branch printToManagerBranch = branchOpenHashTable.getBrunch(listToPrint,temp2[0],temp2[1]);
                System.out.print("Manager of the "+temp2[1]+" branch is ");
                System.out.println(printToManagerBranch.getEmpList().managerName+".");

            }

            //In this section, I perform the process of adding a new employee. If there is already an employee with that name, I do not add it.
            else if(temp[0].equals("ADD")){
                String [] temp2=temp[1].split(", ");
                Branch tempBranch =new Branch(temp2[0],temp2[1]);
                Employee tempEmployee=new Employee(temp2[0],temp2[1],temp2[2],temp2[3]);
                boolean addedFlag= branchOpenHashTable.addEmployee(tempBranch,tempEmployee);
                if (addedFlag){
                    int hasBrunch= branchOpenHashTable.hash(temp2[0],temp2[1]);
                    LinkedList<Branch> listToAdded= branchOpenHashTable.allList[hasBrunch];
                    Branch addedBranch = branchOpenHashTable.getBrunch(listToAdded,temp2[0],temp2[1]);
                    addedBranch.checkQueues();
                }
                else {
                    System.out.println("Existing employee cannot be added again.");
                }
            }

            //In this section, I print the all bonus of the given branch for that month.
            else if(temp[0].equals("PRINT_OVERALL_BONUSES")){
                String [] temp2=temp[1].split(", ");
                int hasBrunch= branchOpenHashTable.hash(temp2[0],temp2[1]);
                LinkedList<Branch> listToPrint= branchOpenHashTable.allList[hasBrunch];
                Branch printToBonusBranch = branchOpenHashTable.getBrunch(listToPrint,temp2[0],temp2[1]);
                System.out.println("Total bonuses for the "+ printToBonusBranch.getDistrict() +" branch are: "+ printToBonusBranch.totalBonus);

            }
        }

    }
}