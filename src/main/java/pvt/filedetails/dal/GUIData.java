/**
 * 
 */
package pvt.filedetails.dal;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pvt.filedetails.directoryprocessor.SharedResources;
import pvt.filedetails.pojo.FileProperties;
import pvt.filedetails.pojo.FolderProperties;

/**
 * Class for storing operation data so that the processing will not be required
 * when operation is performed
 * 
 * @author Sahil Jain
 *
 */
public class GUIData {
	private List<String[]> folderContentData;
	private List<String[]> allFoldersData;
	private List<String[]> allFileData;
	private SharedResources sharedResources;

	public GUIData(SharedResources sharedResources) {
		this.sharedResources = sharedResources;
		this.folderContentData = new LinkedList<>();
		this.allFoldersData = new LinkedList<>();
		this.allFileData = new LinkedList<>();
	}

	/**
	 * This method clears all maps
	 */
	public void clearData() {
		folderContentData.clear();
		allFoldersData.clear();
		allFileData.clear();
	}

	/**
	 * This method returns content of the given folder path in list format
	 * 
	 * @param folderPath
	 * @return list of FileProperties/FolderProperties array object
	 */
	public List<String[]> getFolderContent(String folderPath) {
		if (this.folderContentData.isEmpty()) {
			Map<String, List<String>> folderContent = this.sharedResources.getFolderContent();
			Map<String, FileProperties> fileDetails = this.sharedResources.getFileDetails();
			Map<String, FolderProperties> folderDetails = this.sharedResources.getFolderDetails();
			if (folderContent.containsKey(folderPath)) {
				List<String> list = folderContent.get(folderPath);
				Iterator<String> iterator = list.iterator();
				while (iterator.hasNext()) {
					String filePath = iterator.next();
					if (fileDetails.containsKey(filePath)) {
						this.folderContentData.add(fileDetails.get(filePath).getFilePropertiesVariableArray());
					} else if (folderDetails.containsKey(filePath)) {
						this.folderContentData.add(folderDetails.get(filePath).getFolderPropertiesVariableArray());
					}
				}
			}
		}
		return this.folderContentData;
	}

	/**
	 * This method returns all folder properties
	 * 
	 * @return list of FolderProperties arrays
	 */
	public List<String[]> getAllFoldersDetails() {
		if (this.allFoldersData.isEmpty()) {
			Iterator<Entry<String, FolderProperties>> iterator = this.sharedResources.getFolderDetails().entrySet()
					.iterator();
			while (iterator.hasNext()) {
				FolderProperties folderProperties = iterator.next().getValue();
				this.allFoldersData.add(folderProperties.getFolderPropertiesVariableArray());
			}
		}
		return this.allFoldersData;
	}

	/**
	 * This method returns all file properties
	 * 
	 * @return list of FileProperties arrays
	 */
	public List<String[]> getAllFileDetails() {
		if (this.allFileData.isEmpty()) {
			Iterator<Entry<String, FileProperties>> iterator = this.sharedResources.getFileDetails().entrySet()
					.iterator();
			while (iterator.hasNext()) {
				FileProperties fileProperties = iterator.next().getValue();
				this.allFileData.add(fileProperties.getFilePropertiesVariableArray());
			}
		}
		return this.allFileData;
	}
}
