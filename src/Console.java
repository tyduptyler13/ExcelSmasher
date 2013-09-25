import java.util.LinkedList;
import java.util.List;


/**
 * Primary way to print to console. This takes care of labeling and formating.
 * 
 * Also supports a way to link into a printing system and output things everywhere.
 * 
 * @author Tyler
 *
 */
public abstract class Console{

	private static List<Console> printers;

	public static void warn(String ... string){

		for(String s : string){
			printAll("[SuperParser][warn] " + s);
		}

	}

	public static void log(String ... string){

		for(String s : string){
			printAll("[SuperParser][log] " + s);
		}

	}

	public static void error(String ... string){

		for(String s : string){
			printAll("[SuperParser][ERROR] " + s);
		}

	}

	private static void printAll(String s){

		System.out.println(s);

		if (printers != null){
			for (Console c : printers){
				c.print(s);
			}
		}

	}

	public abstract void print(String s);

	public static void addPrinter(Console c){
		if (printers == null){
			printers = new LinkedList<Console>();
		}

		printers.add(c);
	}

	public static void removePrinter(Console c){

		if (printers == null){
			return;
		}

		printers.remove(c);

	}

}
