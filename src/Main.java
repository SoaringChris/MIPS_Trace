import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class Main {
static Map registers = new HashMap();
static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
	System.out.println("Please provide your instruction"); //get initial instruction
	String opCode = in.next();
	String op1 = in.next();
	String op2 = in.next();
	String op3 = in.nextLine();

	System.out.println("Provide your registers line by line"); //Get initial register values
	for (int i = 0; i < 40; i++)
        registers.put(in.next(), in.nextLine());

	switch (opCode)
	{
		case "add":
			add(op1, op2, op3);
			break;
		case "sub":
			sub(op1, op2, op3);
			break;
		case "lw":
			lw(op1, op2, op3);
			break;
		case "sw":
			sw(op1, op2, op3);
			break;
		case "beq":
			beq(op1, op2, op3);
			break;
		default:
			System.out.println("There was in input error");
			break;
	}

    }

    public static void add(String op1, String op2, String op3)
	{

	}

	public static void sub(String op1, String op2, String op3)
	{

	}

	public static void lw(String op1, String op2, String op3)
	{

	}

	public static void sw(String op1, String op2, String op3)
	{

	}

	public static void beq(String op1, String op2, String op3)
	{

	}
}
