
package com.myuplay.excelsmasher;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.filechooser.FileFilter;

public class FileSystem{

	private LinkedList<File> files;
	private FileFilter fileFilter;
	private int fileCount = 0;
	private Writter w;

	private FileSystem(){

		fileFilter = getFilter();
		files = new LinkedList<File>();
		w = new Writter();

	}

	public FileSystem(File directory){

		this();
		getFiles(directory);

	}

	public FileSystem(File[] files){

		this();
		addFiles(files);

	}

	private void addFiles(File[] files){

		for (File file : files){

			if (file.isDirectory())
				getFiles(file);
			else
				addFile(file);

		}

	}

	private void addFile(File file){
		if (this.fileFilter.accept(file)){
			files.add(file);
			fileCount++;
		}
	}

	private void getFiles(File directory){

		for (File file : directory.listFiles()){

			if (file.isFile()){
				addFile(file);
			} else if (file.isDirectory()) {
				getFiles(file);
			}

		}

	}

	public static final FileFilter getFilter(){

		return new FileFilter(){

			/**
			 * Directories, .hst, .sts, .sp
			 */
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) return true;
				return (f.getName().endsWith(".xls") || f.getName().endsWith(".xlsx"));
			}

			@Override
			public String getDescription() {
				return "Excel Files (.xls .xlsx)";
			}

		};

	}

	public void read(){

		int count = 1;
		for (File file : files){

			try {

				Console.log("Reading " + count + "/" + fileCount + "  (" + count/fileCount * 100 + "%)");
				count++;

				Reader r = new Reader(file);

				r.parse(w);
				r.close();
			} catch (Exception e) {
				Console.warn("There was something wrong with this format in file: " + file.getPath());
				Console.log(file.getName() + " - skipped...");
			}


		}

	}

	public int getFileCount(){

		return fileCount;

	}

	public void write(File f) throws IOException{
		w.write(f);
	}

}

