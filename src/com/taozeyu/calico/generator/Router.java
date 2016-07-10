package com.taozeyu.calico.generator;

import java.io.File;

import com.taozeyu.calico.resource.ResourceManager;
import com.taozeyu.calico.util.PathUtil;

public class Router {

	public static final String RootPath = "/";
	private static final String[] IndexFilePosibleExtensionNames = new String[]{
			"html", "htm",
	};

	private final ResourceManager resource;
	private final File routeDir;
	private final String rootMapToPath;
	
	public Router(ResourceManager resource, File routeDir, String rootMapToPath) {
		this.resource = resource;
		this.routeDir = routeDir;
		this.rootMapToPath = rootMapToPath;
	}

	public File getFile(String relativePath) {
		return new File(routeDir.getPath(), relativePath);
	}

	public FileGenerator getFileGenerator(String absolutePath) {

		String targetPath = PathUtil.normalizePathAndCleanExtensionName(absolutePath);
		boolean isRootPage = false;

		if(targetPath.equals(RootPath)) {
			targetPath = rootMapToPath;
			isRootPage = true;
		}
		PageService pageService = getPageServiceWithNormalizeTargetPath(targetPath);
		if (pageService == null) {
			throw new RouteException("No template file map to '"+ absolutePath +"'.");
		}
		return new FileGenerator(pageService, new File(targetPath), isRootPage);
	}

	public PageService getPageService(String absolutePath) {

		String targetPath = PathUtil.normalizePathAndCleanExtensionName(absolutePath);

		if(targetPath.equals(RootPath)) {
			targetPath = rootMapToPath;
		}
		return getPageServiceWithNormalizeTargetPath(targetPath);
	}

	private PageService getPageServiceWithNormalizeTargetPath(String absolutePath) {
		File targetPathTemplateFile = new File(routeDir, absolutePath);
		if(targetPathTemplateFile.exists()) {
			String params = "";
			return new PageService(resource, targetPathTemplateFile, routeDir, params);
			
		} else {
			String extensionName = PathUtil.getExtensionName(absolutePath);
			String pathCells[] = clearHeadTailSlash(absolutePath).split("/");
			return createPageService(pathCells, extensionName);
		}
	}

	private String clearHeadTailSlash(String path) {
		return path.replaceAll("^/", "").replaceAll("/$", "");
	}

	private PageService createPageService(String[] pathCells, String extensionName) {
		
		int endOfExistDirIndex = findEndOfExistDirIndex(pathCells, extensionName);
		String path = getTemplateDirPath(pathCells, endOfExistDirIndex);
		File templatePath = getTemplatePath(path, extensionName);
		if (templatePath != null) {
			String params = selectParamsFromPath(pathCells, endOfExistDirIndex + 1);
			return new PageService(resource, templatePath, routeDir, params);
		}
		return null;
	}

	private String getTemplateDirPath(String[] pathCells, int endOfExistDirIndex) {
		String path = "";
		for(int i=0; i < endOfExistDirIndex + 1; ++i) {
			path += pathCells[i];
			if(i < endOfExistDirIndex) {
				path += "/";
			}
		}
		return path;
	}

	private int findEndOfExistDirIndex(String[] pathCells, String extensionName) {
		int endOfExistDirIndex = -1;
		File path = new File("");
		for(int i=0; i<pathCells.length; ++i) {
			String pathCell = pathCells[i] + "." + extensionName;
			path = new File(path, pathCell);
			if(!isFileExist(path)) {
				break;
			}
			endOfExistDirIndex = i;
		}
		return endOfExistDirIndex;
	}

	private boolean isFileExist(File path) {
		return getFile(path.getPath()).exists();
	}

	private String selectParamsFromPath(String pathCells[], int startIndex) {
		String params = "";
		for(int i=startIndex; i<pathCells.length; ++i) {
			params += pathCells[i];
			if(i < pathCells.length - 1) {
				params += "/";
			}
		}
		return params;
	}

	private boolean isTemplateFile(File file) {
		return file.exists() || file.isFile();
	}

	private File getTemplatePath(String dirPath, String extensionName) {
		File file = new File(routeDir.getPath(), dirPath + "." + extensionName);
		if (isTemplateFile(file)) {
			return file;
		}
		extensionName = normalizeIndexFileExtensionName(extensionName);
		file = new File(routeDir.getPath(), dirPath + "/index." + extensionName);
		if (isTemplateFile(file)) {
			return file;
		}
		return null;
	}

	private String normalizeIndexFileExtensionName(String extensionName) {
		extensionName = extensionName.trim();
		for (String posibleName : IndexFilePosibleExtensionNames) {
			if (posibleName.equals(extensionName)) {
				return extensionName;
			}
		}
		return "html";
	}
}
