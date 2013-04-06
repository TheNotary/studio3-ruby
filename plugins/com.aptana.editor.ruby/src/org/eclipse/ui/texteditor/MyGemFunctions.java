package org.eclipse.ui.texteditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.eclipse.core.internal.filebuffers.SynchronizableDocument;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class MyGemFunctions
{
	public static final int helloInt(){
		return 3;
	}

	public static String[] parseCurrentFileForRequires()
	{
		// TODO:  Get this to actually parse the damned ruby file currently open... check window.getOpenFiles() or something... I saw that somewhere
		// Get active file...
		String text = getActiveFilesText();
		String[] requiredFiles = { "benchmark/methods.rb" };
		
		return requiredFiles;
	}

	public static String[] locateSourceCodeOfRequiredFiles(String[] requiredFiles)
	{
		// TODO Get this file to actually search for files in the local project directory, and then sniff for it in the
		// gems folder...
		String[] requireFilePaths = new String[requiredFiles.length];
		String absolutePathToProjectsParent = getAbsolutePathOfProjectsParentFolder();
		
		for (int i = 0; i < requiredFiles.length; i++){
			String relativePath = requiredFiles[i];
			requireFilePaths[i] = absolutePathToProjectsParent + File.separator + relativePath;
		}
		
		return requireFilePaths;
	}

	public static IDocument[] pullUpSourceFilesAsIDocuments(String[] requiredFilesPaths)
	{
		// TODO:  Have this method read each file pointed to in it's argument array and load them into IDocumens
		IDocument[] sourceFiles = new IDocument[requiredFilesPaths.length];
		for (int i = 0; i < requiredFilesPaths.length; i++){
			String text;
			try{
				text = readFile(requiredFilesPaths[i]);
			}
			catch (IOException e){
				// TODO Auto-generated catch block
				text = "";
				e.printStackTrace();
			}
			sourceFiles[i] = new SynchronizableDocument();
			sourceFiles[i].set(text);
		}
		
		return sourceFiles;
	}
	
	// TODO:  Use w/e Eclipse has built in for this...  
	private static String readFile(String path) throws IOException {
	  FileInputStream stream = new FileInputStream(new File(path));
	  try {
	    FileChannel fc = stream.getChannel();
	    MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
	    /* Instead of using default, pass in a decoder. */
	    return Charset.defaultCharset().decode(bb).toString();
	  }
	  finally {
	    stream.close();
	  }
	}
	
	public static String getAbsolutePathOfProjectsParentFolder(){
		// To get root of active workbench page...
		String absoluteProjectPath = "";
		IWorkbenchPart workbenchPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		IFile file = (IFile) workbenchPart.getSite().getPage().getActiveEditor().getEditorInput().getAdapter(IFile.class);
		if (file != null){
			String absoluteFilePath = file.getRawLocation().toOSString();
			String relativePath = file.getFullPath().toOSString();
			//String projectName = file.getProject().getFullPath().toOSString();  // oops, don't need
			absoluteProjectPath = absoluteFilePath.substring(0, absoluteFilePath.length() - relativePath.length()); // + projectName;
		}
		
		
		return absoluteProjectPath;
	}
	
	private static String getActiveFilesText()
	{
		String text = null;
		
		IWorkbenchPart workbenchPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart();
		IFile file = (IFile) workbenchPart.getSite().getPage().getActiveEditor().getEditorInput().getAdapter(IFile.class);
		
		try	{
			text = file.getContents().toString();
		}
		catch (CoreException e)	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		text.getBytes().toString();
		// FIXME look here.  
		//file.
		
		return text;
	}
	
	
}
