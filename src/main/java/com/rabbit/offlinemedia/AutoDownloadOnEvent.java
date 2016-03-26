package com.rabbit.offlinemedia;

import java.io.*;
import java.util.*;
import java.util.regex.*;

class AutoDownloadOnEvent {
		
	public static void main(String args[]) throws Exception {
		System.out.println("\t\t*************************************************");
		System.out.println("\t\tWelcome to Automatic Media Offliner 1.0");
		System.out.println("\t\tCredits: Shanmukha Katuri <shanmukha.k@gmail.com>");
		System.out.println("\t\t*************************************************");


		String urlToDl = "https://www.youtube.com/watch?v=KNSfwgGbrTA";
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				download(args[i]);
			}
		} else {
			System.out.print("\n\nEnter the URL to download: ");
			Scanner sc = new Scanner(System.in);
			String answer = sc.next();
			while (answer != null && answer.length() > 0 && !"q".equalsIgnoreCase(answer)) {
				download(answer);
				System.out.print("\n\nEnter the URL to download: (q to quit) ");
				answer = sc.next();
			}
 			System.out.println("Good bye, Have a great day !!");
		}
		
	}


	private static void download(String URL) throws Exception {
		java.lang.Process proc = Runtime.getRuntime().exec(String.format("youtube-dl %s", URL));
		output(proc);
	}

	private static final String DEST_PATTERN = "Destination";
	private static final String LIST_PATTERN = "Downloading video [0-9]+ of [0-9]+";
	private static Pattern listPattern = Pattern.compile(LIST_PATTERN);
	private static Pattern numberPattern = Pattern.compile("(\\d+(?:\\.\\d+)?)");
	private static Pattern percentPattern = Pattern.compile("(\\d+(?:\\.\\d+)?)%");
	private static Pattern destPattern = Pattern.compile(DEST_PATTERN);
	private static void output(java.lang.Process process) throws Exception {
		
		InputStream stdin = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(stdin);
		BufferedReader br = new BufferedReader(isr);

		String line = null;
		String anim= "|/-\\";
		int i = 0;
		String fileName = "";
		int currentItem = 0;
		int totalItems = 0;
		while ( (line = br.readLine()) != null) {
			// System.out.println(line);
			Matcher destMatcher = destPattern.matcher(line);
			if (destMatcher.find()) {
				String match = destMatcher.group();
				// System.out.println("ORIGINAL LINE: " + line);
				line = line.substring(destMatcher.start() + DEST_PATTERN.length() + 1).trim();
				fileName = line;
				continue;
			}

			Matcher listMatcher = listPattern.matcher(line);
			if (listMatcher.find()) {
				// System.out.println("ORIGINAL LINE: " + line);
				String match = listMatcher.group();
				Matcher numMatcher = numberPattern.matcher(match);
				if (numMatcher.find()) {
					currentItem = (int) Double.parseDouble(numMatcher.group());
					if (numMatcher.find()) totalItems = (int) Double.parseDouble(numMatcher.group());
					continue;
				}
			}

			Matcher m = percentPattern.matcher(line);
			if (m.find()) {
				String match = m.group();
				String prefix = (currentItem > 0 && totalItems > 0) ? "["+currentItem + " of " + totalItems + "]" : "";
				String data = "\r" + prefix + " [" + ((fileName.length() > 30) ? fileName.substring(0, 25) + "...mp4" : fileName) + "] " + match;
				System.out.write(data.getBytes());
			}
            /*do {
                    String data = "\r" + anim.charAt(x % anim.length())  + " " + x ;
                    System.out.write(data.getBytes());
                    Thread.sleep(100);
            } while ();*/





		     // System.out.println(line);
		 }

		int exitVal = process.waitFor();
	}
}