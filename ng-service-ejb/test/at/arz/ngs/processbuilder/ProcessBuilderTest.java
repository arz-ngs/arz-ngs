package at.arz.ngs.processbuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProcessBuilderTest {

	public static void main(String[] args){
		List<String> command = new ArrayList<String>();
		command.add("firefox");
		ProcessBuilder processBuilder = new ProcessBuilder(command);
		Process process;
		try {
			process = processBuilder.start();
			int errorCode = process.waitFor();
			System.out.println(errorCode);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
