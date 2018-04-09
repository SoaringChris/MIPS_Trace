import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Supplier;

public class Main {
static Map<String, String> registers = new HashMap<>();
static Scanner in = new Scanner(System.in);

    public static void main(String[] args) throws FileNotFoundException {
		String opCode, op1, op2, op3;

    	System.out.println("Enter the filepath of your input or 'm' for Manual Mode");
    	String mode = in.next();
		in.nextLine();
    	if(mode.equals("m")) {
			System.out.println("Please provide your instruction"); //get initial instruction
			opCode = in.next();
			op1 = in.next();
			op2 = in.next();
			op3 = in.next();
			in.nextLine();
		}
		else
		{
			in = new Scanner(new FileReader(new File(mode.trim())));
			opCode = in.next();
			op1 = in.next();
			op2 = in.next();
			op3 = in.next();
			in.nextLine();
		}

		if(mode.equals("m")) {
			System.out.println("Provide your registers line by line"); //Get initial register values
			for (int i = 0; i < 40; i++)
			{
				registers.put(in.next(), in.next());
				in.nextLine();
			}
		}
		else
		{
			for (int i = 0; i < 40; i++)
			{
				registers.put(in.next(), in.next());
				in.nextLine();
			}
		}

		for (String name: registers.keySet())
		{

			String key = name.toString();
			String value = registers.get(name).toString();
			System.out.println(key + " " + value);
		}

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
		String funct = "32";
		String shamt = "0";
		String rd = op1;
		String rt = op3;
		String rs = op2;
		String op = "0";
		int pc = 0;
		System.out.println("\nSemantics");
		System.out.println("$" + op1 + " <- $" + op2 + " + $" + op3 +"\n");
		int pc4 = pcInc(pc);
		System.out.print("\n");
		instr(op, rs, rt, rd, funct, pc4, false);
		instr(op, rs, rt, rd, funct, pc4, true);
		sigVect("add");
		System.out.println("\n");
		String [] rdData = dataReg(rs,rt);
		System.out.print("\n");
		idexbuff(funct, rd, rs, rdData[0], rdData[1], pc);
		System.out.print("\n");
		int shift4 = shiftL2(Integer.parseInt(funct), pc);
		System.out.println("\n");
		String mux1 = mux11(rdData[1], funct, op);
		System.out.println("\n");
		String[] aluOut = aluCont(funct, op, "addition", rdData[0], mux1);
		System.out.print("\n");
		String mux2 = mux12(rt, rd, op, shift4, aluOut[1], aluOut[0]);
		System.out.print("\n");
		branchGate(op);
		System.out.print("\n");
		String rdData2 = datMem(op, aluOut[1], rdData[1]);
		System.out.print("\n");
		memwbBuff(mux2, aluOut[1], rdData2);
		System.out.print("\n");
		String mux3 = mux14(op, rdData2, aluOut[1]);
		System.out.print("\n");
		regRevis(rs, rt, mux2, mux3, op);

	}

	public static void sub(String op1, String op2, String op3)
	{
		String funct = "34";
		String shamt = "0";
		String rd = op1;
		String rt = op3;
		String rs = op2;
		String op = "0";
		int pc = 0;
		System.out.println("\nSemantics");
		System.out.println("$" + op1 + " <- $" + op2 + " - $" + op3 + "\n");
		int pc4 = pcInc(pc);
		System.out.print("\n");
		instr(op,rs, rt, rd, funct, pc4, false);
		instr(op, rs, rt, rd, funct, pc4, true);
		sigVect("sub");
		System.out.println("\n");
		String [] rdData = dataReg(rs,rt);
		System.out.print("\n");
		idexbuff(funct, rd, rs, rdData[0], rdData[1], pc);
		System.out.print("\n");
		int shift4 = shiftL2(Integer.parseInt(funct), pc);
		System.out.println("\n");
		String mux1 = mux11(rdData[1], funct, op);
		System.out.println("\n");
		String[] aluOut = aluCont(funct, op, "subtraction", rdData[0], mux1);
		System.out.print("\n");
		String mux2 = mux12(rt, rd, op, shift4, aluOut[1], aluOut[0]);
		System.out.print("\n");
		branchGate(op);
		System.out.print("\n");
		String rdData2 = datMem(op, aluOut[1], rdData[1]);
		System.out.print("\n");
		memwbBuff(mux2, aluOut[1], rdData2);
		System.out.print("\n");
		String mux3 = mux14(op, rdData2, aluOut[1]);
		System.out.print("\n");
		regRevis(rs, rt, mux2, mux3, op);
		System.out.print("\n");
	}

	public static void lw(String op1, String op2, String op3)
	{
		String imd = op2;
		String rt = op1;
		String rs = op3;
		String op = "35";
		int pc = 0;
		System.out.println("\nSemantics");
		System.out.println("$" + op1 + " <- Memory[$" + op2 + " + " + op3 + "]\n");
		int pc4 = pcInc(pc);
		System.out.print("\n");
		instr(op, rs, rt, imd, pc4, false);
		instr(op, rs, rt, imd, pc4, true);
		sigVect("lw");
		System.out.println("\n");
		String [] rdData = dataReg(rs,rt);
		System.out.print("\n");
		idexbuff(imd, imd, rs, rdData[0], rdData[1], pc);
		System.out.print("\n");
		int shift4 = shiftL2(Integer.parseInt(imd), pc);
		System.out.println("\n");
		String mux1 = mux11(rdData[1], imd, op);
		System.out.println("\n");
		String[] aluOut = aluCont(imd, op, "addition", rdData[0], mux1);
		System.out.print("\n");
		String mux2 = mux12(rt, imd, op, shift4, aluOut[1], aluOut[0]);
		System.out.print("\n");
		branchGate(op);
		System.out.print("\n");
		String rdData2 = datMem(op, aluOut[1], rdData[1]);
		System.out.print("\n");
		memwbBuff(mux2, aluOut[1], rdData2);
		System.out.print("\n");
		String mux3 = mux14(op, rdData2, aluOut[1]);
		System.out.print("\n");
		regRevis(rs, rt, mux2, mux3, op);
		System.out.print("\n");
	}

	public static void sw(String op1, String op2, String op3)
	{
		String imd = op2;
		String rt = op1;
		String rs = op3;
		String op = "43";
		int pc = 0;
		System.out.println("\nSemantics");
		System.out.println("Memory[$" + op1 + " + " + op2 + "] -> $" + op3 +"\n");
		int pc4 = pcInc(pc);
		System.out.print("\n");
		instr(op, rs, rt, imd, pc4, false);
		instr(op, rs, rt, imd, pc4, true);
		sigVect("sw");
		System.out.println("\n");
		String [] rdData = dataReg(rs,rt);
		System.out.print("\n");
		idexbuff(imd, imd, rs, rdData[0], rdData[1], pc);
		System.out.print("\n");
		int shift4 = shiftL2(Integer.parseInt(imd), pc);
		System.out.println("\n");
		String mux1 = mux11(rdData[1], imd, op);
		System.out.println("\n");
		String[] aluOut = aluCont(imd, op, "addition", rdData[0], mux1);
		System.out.print("\n");
		String mux2 = mux12(rt, imd, op, shift4, aluOut[1], aluOut[0]);
		System.out.print("\n");
		branchGate(op);
		System.out.print("\n");
		String rdData2 = datMem(op, aluOut[1], rdData[1]);
		System.out.print("\n");
		memwbBuff(mux2, aluOut[1], rdData2);
		System.out.print("\n");
		String mux3 = mux14(op, rdData2, aluOut[1]);
		System.out.print("\n");
		regRevis(rs, rt, mux2, mux3, op);
		System.out.print("\n");
	}

	public static void beq(String op1, String op2, String op3)
	{
		String imd = op3;
		String rt = op2;
		String rs = op1;
		String op = "4";
		int pc = 0;
		System.out.println("\nSemantics");
		System.out.println("If($" + op1 +"==$" + op2 + ") go to PC + 4 + " + op3 + "*4");
		int pc4 = pcInc(pc);
		System.out.print("\n");
		instr(op, rs, rt, imd, pc4, false);
		instr(op, rs, rt, imd, pc4, true);
		sigVect("beq");
		System.out.println("\n");
		String [] rdData = dataReg(rs,rt);
		System.out.print("\n");
		idexbuff(imd, imd, rs, rdData[0], rdData[1], pc);
		System.out.print("\n");
		int shift4 = shiftL2(Integer.parseInt(imd), pc);
		System.out.println("\n");
		String mux1 = mux11(rdData[1], imd, op);
		System.out.println("\n");
		String[] aluOut = aluCont(imd, op, "addition", rdData[0], mux1);
		System.out.print("\n");
		String mux2 = mux12(rt, imd, op, shift4, aluOut[1], aluOut[0]);
		System.out.print("\n");
		branchGate(op);
		System.out.print("\n");
		String rdData2 = datMem(op, aluOut[1], rdData[1]);
		System.out.print("\n");
		memwbBuff(mux2, aluOut[1], rdData2);
		System.out.print("\n");
		String mux3 = mux14(op, rdData2, aluOut[1]);
		System.out.print("\n");
		regRevis(rs, rt, mux2, mux3, op);
		System.out.print("\n");
	}

	public static int pcInc(int pc)
	{
		System.out.println("IF Stage\n");

		System.out.println("PC = " + pc);
		int temp = pc+4;
		System.out.println("PC + 4 = " +temp);
		return temp;
	}

	public static void instr(String six, String rs, String rt, String rd, String funct, int pc, boolean buf) //To generate instruction for R types
	{
		System.out.println("Instruction");
		System.out.print("op  rs  rt  rd  shamt  funct");
		if(buf)//For when generating the full IF/ID Buffer
			System.out.println("  PC+4");
		else
			System.out.print("\n");
		System.out.print(six);
		for(int i = 0; i < 4 - six.length(); i++)
			System.out.print(" ");
		System.out.print(rs);
		for(int i = 0; i < 4 - rs.length(); i++)
			System.out.print(" ");
		System.out.print(rt);
		for(int i = 0; i < 4 - rt.length(); i++)
			System.out.print(" ");
		System.out.print(rd);
		for(int i = 0; i < 4 - rd.length(); i++)
			System.out.print(" ");
		System.out.print("0      "+funct);
		if(buf)//For when generating the full IF/ID Buffer
			System.out.println("     " + pc +"\n\nID Stage");
		else
			System.out.print("\n");
	}

	public static void instr(String six, String rs, String rt, String imd, int pc, boolean buf) //To generate instruction for I types
	{
		System.out.println("Instruction");
		System.out.print("op  rs  rt  immediate");
		if(buf)//For when generating the full IF/ID Buffer
			System.out.println(" PC+4");
		else
			System.out.print("\n");
		System.out.print(six + " ");
		System.out.print(rs);
		if(rs.length() == 1)
			System.out.print(" ");
		System.out.print(" ");
		System.out.print(rt);
		if(rt.length() == 1)
			System.out.print(" ");
		System.out.print("  +" + imd);
		if(buf)//For when generating the full IF/ID Buffer
			System.out.println("     " + pc +"\n\nID Stage");
		else
			System.out.print("\n");
	}

	public static void sigVect(String op) //Generates Control Signal Vectors
	{
		System.out.println("Control Signal Vector");
		System.out.println("RegDst  ALUOp1  ALUOp0  ALUSrc  Branch  MemRead  MemWrite  RegWrite  MemtoReg");
		switch(op)
		{
			case "add":
			case "sub":
				System.out.println("1       1       0       0       0       0        0         1         0");
				break;
			case "lw":
				System.out.println("0       0       0       1       0       1        0         1         1");
				break;
			case "sw":
				System.out.println("0       0       0       1       0       0        1         0         0");
				break;
			case "beq":
				System.out.println("0       0       1       0       1       0        0         0         0");

		}
	}

	public static String[] dataReg(String opr2, String opr3) //Generates the data register information
	{
		System.out.println("Data Registers");
		System.out.println("Read Register 1  Read Register 2  Write Register  Write Data  Read Data 1  Read Data 2  RegWrite");
		System.out.print(opr2);
		if(opr2.length() == 1)
			System.out.print(" ");
		System.out.print("               " + opr3);
		if(opr3.length() == 1)
			System.out.print(" ");
		System.out.print("               0               0           " + registers.get("R" + opr2));
		for(int i = 0; i < 12 - registers.get("R" + opr2).length(); i++)
			System.out.print(" ");
		System.out.print(registers.get("R" + opr3));
		for(int i = 0; i < 13 - registers.get("R" + opr3).length(); i++)
			System.out.print(" ");
		System.out.println("0");
		System.out.println("Note: Write Register and Write Data have not been assigned values yet!");
		String[] out = new String[2];
		out[0] = registers.get("R" + opr2);
		out[1] = registers.get("R" + opr3);
		return out;
	}

	public static void idexbuff(String six, String opr1, String opr3, String read1, String read2, int pc) //Generates the ID/EX Buffer information
	{
		System.out.println("ID/EX Buffer");
		System.out.println("Inst[15-11]  Inst[20-16]  Inst[15-0]  ReadData2  ReadData1  PC+4");
		System.out.print(opr1);
		for(int i = 0; i < 13 - opr1.length(); i++)
			System.out.print(" ");
		System.out.print(opr3);
		for(int i = 0; i < 13 - opr3.length(); i++)
			System.out.print(" ");
		System.out.print(six);
		for(int i = 0; i < 12 - six.length(); i++)
			System.out.print(" ");
		System.out.print(read2);
		for(int i = 0; i < 11 - read2.length(); i++)
			System.out.print(" ");
		System.out.print(read1);
		for(int i = 0; i < 11 - read1.length(); i++)
			System.out.print(" ");
		int temp = pc+4;
		System.out.println(temp + "\n\nEX stage");
	}

	public static int shiftL2(int six, int pc) //Generates Shift Left 2
	{
		System.out.println("Shift Left 2");
		System.out.println("offset  offset*4");
		System.out.print(six);
		for(int i = 0; i < 9 - Integer.toString(six).length(); i++)
			System.out.print(" ");
		int shift = six*4;
		System.out.println(shift +"\n");
		return exAdder(shift, pc);
	}

	public static int exAdder(int lShift, int pc) //Generates the Adder from the EX State
	{
		System.out.println("EX Adder");
		System.out.println("PC+4  offset*4  Output");
		int temp = pc+4;
		System.out.print(temp + "     " + lShift);
		for(int i = 0; i < 11 - Integer.toString(lShift).length(); i++)
			System.out.print(" ");
		System.out.println(lShift+4);
		return lShift + 4;
	}

	public static String mux11(String rdData2, String six, String op) //Generates data for the MUX marked 11
	{
		System.out.println("MUX 11");
		System.out.println("ReadData2  Inst[15-0]  ALUSrc  Output");
		System.out.print(rdData2);
		for(int i = 0; i < 11 - rdData2.length(); i++)
			System.out.print(" ");
		System.out.print(six);
		for(int i = 0; i < 12 - six.length(); i++)
			System.out.print(" ");
		if(op.equals("4"))
		{
			System.out.println("1       " + six);
			return six;
		}
		else
		{
			System.out.println("0       " + rdData2);
			return rdData2;
		}

	}

	public static String[] aluCont(String six, String op, String operation, String rdData1, String mux) //Generates data for ALU Control
	{
		System.out.println("ALU Control");
		System.out.println("Inst[5-0]  ALUOp1  ALUOp2  Operation");
		System.out.print(six);
		for(int i = 0; i < 11 - six.length(); i++)
			System.out.print(" ");
		switch (op)
		{
			case "0":
				System.out.print("1       0       ");
				break;
			case "35":
			case "43":
				System.out.print("0       0       ");
				break;
			case "4":
				System.out.print("0       1       ");
				break;
		}
		System.out.println(operation + "\n");
		return alu(rdData1, mux, operation, op);

	}

	public static String[] alu(String rdData1, String mux, String cont, String op) //Generates data for the ALU
	{
		String[] out = new String[2];
		System.out.println("ALU");
		System.out.println("ReadData1  MUX11 Output  ALUControl  Zero  ALUresult");
		System.out.print(rdData1);
		for(int i = 0; i < 11 - rdData1.length(); i++)
			System.out.print(" ");
		System.out.print(mux);
		for(int i = 0; i < 15 - mux.length(); i++)
			System.out.print(" ");
		System.out.print(cont);
		for(int i = 0; i < 12 - cont.length(); i++)
			System.out.print(" ");
		if(op.equals("4"))
		{
			System.out.print("1     ");
			out[0] = "1";
		}
		else
		{
			System.out.print("0     ");
			out[0] = "0";
		}
		if(cont.equals("addition"))
			out[1] = Integer.toString(Integer.parseInt(rdData1) + Integer.parseInt(mux));
		else
			out[1] = Integer.toString(Integer.parseInt(rdData1) - Integer.parseInt(mux));
		System.out.println(out[1]);
		return out;

	}

	public static String mux12(String rt, String five, String op, int shift4, String alu, String zero) //Generates data for the MUX marked 12
	{
		System.out.println("MUX 12");
		System.out.println("Inst[20-16]  Inst[15-11]  RegDst  Output");
		System.out.print(rt);
		for(int i = 0; i < 13 - rt.length(); i++)
			System.out.print(" ");
		System.out.print(five);
		for(int i = 0; i < 13 - five.length(); i++)
			System.out.print(" ");
		if(op.equals("0"))
		{
			System.out.println("1       " + five + "\n");
			exmemBuff(five, rt, alu, shift4, five);
			return five;
		}
		else
		{
			System.out.println("0       " + rt + "\n");
			exmemBuff(rt, rt, alu, shift4, zero);
			return zero;
		}
	}

	public static void exmemBuff(String mux, String rt, String alu, int shift4, String zero) //Generates the data for the EX/MEM Buffer
	{
		System.out.println("EX/MEM Buffer");
		System.out.println("MUX12 Output  ReadData2  ALUResult  Zero  PC+4+offset*4");
		System.out.print(mux);
		for(int i = 0; i < 14 - mux.length(); i++)
			System.out.print(" ");
		System.out.print(registers.get("R" + rt));
		for(int i = 0; i < 11 - registers.get("R" + rt).length(); i++)
			System.out.print(" ");
		System.out.print(alu);
		for(int i =0; i < 11 - alu.length(); i++)
			System.out.print(" ");
		int temp = 4+shift4;
		if(zero.equals("0"))
			System.out.println("0     " + temp +"\n\nMEM Stage");
		else
			System.out.println("1     " + temp + "\n\nMEM Stage");
	}

	public static void branchGate(String op) //Generates data for the Branch AND Gate
	{
		System.out.println("Branch AND Gate");
		System.out.println("Branch  Zero  Output");
		if(op.equals("4"))
			System.out.println("1       1     1");
		else
			System.out.println("0       0     0");
	}

	public static String datMem(String op, String alu, String rdData) //Generates the data for the Data Memory module
	{
		System.out.println("Data Memory");
		if(op.equals("0") || op.equals("4"))
			System.out.println("*This instruction does not use this component*");
		System.out.println("Read Address  Write Address  Write Data  MemWrite  MemRead  Read Data");
		for(int i = 0; i < 2; i++)
		{
			System.out.print(alu);
			for(int j = 0; j < 14 - alu.length(); j++)
				System.out.print(" ");
		}
		System.out.print(" " + rdData);
		for(int i = 0; i < 12 - rdData.length(); i++)
			System.out.print(" ");
		switch(op)
		{
			case "0":
			case "4":
				System.out.println("0         0        t");
				return("t");
			case "35":
				System.out.println("1         0        " + registers.get("R" + alu));
				return(registers.get("R" + alu));
			case "43":
				System.out.println("0         1        " + registers.get("R" + alu));
				return (registers.get("R" + alu));
			default:
				return("t");
		}

	}

	public static void memwbBuff(String mux, String alu, String rData) //Generates the data for MEM/WB Buffer
	{
		System.out.println("** Field  ALU Result  Read Data");
		System.out.print(mux);
		for(int i = 0; i < 10 - mux.length(); i ++)
			System.out.print(" ");
		System.out.print(alu);
		for(int i = 0; i < 12 - alu.length(); i++)
			System.out.print(" ");
		System.out.println(rData +"\n\nWB Stage");
	}

	public static String mux14(String op, String rData, String alu) //Generates the data for the MUX labled 14
	{
		System.out.println("MUX 14");
		System.out.println("Read Data  ALU Result MemToReg  Output");
		System.out.print(rData);
		for(int i = 0; i < 11 - rData.length(); i++)
			System.out.print(" ");
		System.out.print(alu);
		for(int i = 0; i < 11 - alu.length(); i++)
			System.out.print(" ");
		if(op.equals("35") )
		{
			System.out.println("1         " + rData);
			return rData;
		}
		else
		{
			System.out.println("0         " + alu);
			return alu;
		}
	}

	public static void regRevis(String rs, String rt, String mux2, String mux3, String op) //Generates the data for the revisit to the data registers
	{
		System.out.println("Data Registers");
		System.out.println("Read Register 1  Read Register 2  Write Register  Write Data  Read Data 1  Read Data 2  RegWrite");
		System.out.print(rs);
		for(int i = 0; i < 17 - rs.length(); i ++)
			System.out.print(" ");
		System.out.print(rt);
		for(int i = 0; i < 17 - rt.length(); i ++)
			System.out.print(" ");
		System.out.print(mux2);
		for(int i = 0; i < 16 - mux2.length(); i ++)
			System.out.print(" ");
		System.out.print(mux3);
		for(int i = 0; i < 12 - mux2.length(); i ++)
			System.out.print(" ");
		System.out.print(registers.get("R" + rs));
		for(int i = 0; i < 13 - registers.get("R" + rs).length(); i++)
			System.out.print(" ");
		System.out.print(registers.get("R" + rt));
		for(int i = 0; i < 13 - registers.get("R" + rt).length(); i++)
			System.out.print(" ");
		if(op.equals("0") || op.equals("35"))
		{
			System.out.println("1\n");
			registers.replace("R" + mux2, mux3);
			System.out.println(mux3 + " was written to R" + mux2);
		}
		else
			System.out.println();
		System.out.println("\nEND");
	}



}
