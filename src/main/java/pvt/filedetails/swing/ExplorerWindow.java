/**
 * 
 */
package pvt.filedetails.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import pvt.filedetails.dal.GUIData;
import pvt.filedetails.directoryprocessor.Processor;
import pvt.filedetails.utility.Enums.MenuItems;
import pvt.filedetails.utility.Enums.ProcessingStatus;
import java.awt.event.ActionEvent;

/**
 * @author Sahil Jain
 *
 */
public class ExplorerWindow {

	private JFrame frmDirectoryExplorer;
	private JTable jTable;
	JScrollPane scrollPane;
	private GUIData guiData;
	private static final String COLUMN[] = { "Name", "Path", "Type", "Size", "File Count" };

	/**
	 * Launch the application.
	 */
	public static void launchExplorerWindow(Processor processor) {
//		EventQueue.invokeLater(new Runnable() {
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					ExplorerWindow window = new ExplorerWindow(processor);
					window.frmDirectoryExplorer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(runnable).start();
	}

	/**
	 * Create the application.
	 */
	public ExplorerWindow(Processor processor) {
		guiData = new GUIData(processor.getSharedResources());
		initialize(processor);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(Processor processor) {
		this.frmDirectoryExplorer = new JFrame();
		this.frmDirectoryExplorer.setTitle("Directory Explorer");
		this.frmDirectoryExplorer.setBounds(100, 100, 1215, 754);
		this.frmDirectoryExplorer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frmDirectoryExplorer.getContentPane().setLayout(null);

		JPanel panelInformation = new JPanel();
		panelInformation.setBackground(SystemColor.control);
		panelInformation.setBounds(10, 10, 1191, 94);
		this.frmDirectoryExplorer.getContentPane().add(panelInformation);
		panelInformation.setLayout(null);

		JLabel lblDir = new JLabel("Directory: ");
		lblDir.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		lblDir.setBounds(10, 10, 87, 24);
		panelInformation.add(lblDir);

		JLabel lblStatus = new JLabel("Status: ");
		lblStatus.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		lblStatus.setBounds(10, 57, 87, 24);
		panelInformation.add(lblStatus);

		JLabel lblDirectory = new JLabel(processor.getParentDirectory().getAbsolutePath());
		lblDirectory.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		lblDirectory.setBounds(107, 10, 1074, 24);
		panelInformation.add(lblDirectory);

		JLabel lblProcessingStatus = new JLabel(
				processor.getSharedResources().getProcessingStatus().getProcessingStatus());
		lblProcessingStatus.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		lblProcessingStatus.setBounds(107, 57, 227, 24);
		panelInformation.add(lblProcessingStatus);

		JLabel lblOpenedDirectory = new JLabel("Opened Directory:");
		lblOpenedDirectory.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
		lblOpenedDirectory.setBounds(344, 57, 155, 24);
		panelInformation.add(lblOpenedDirectory);

		JLabel lblOpenedDirectoryValue = new JLabel("");
		lblOpenedDirectoryValue.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 17));
		lblOpenedDirectoryValue.setBounds(509, 57, 672, 24);
		panelInformation.add(lblOpenedDirectoryValue);

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while (!processor.getSharedResources().getProcessingStatus().equals(ProcessingStatus.COMPLETED)) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				lblProcessingStatus.setText(ProcessingStatus.COMPLETED.getProcessingStatus());
				lblProcessingStatus.updateUI();
			}
		};
		new Thread(runnable).start();

		List<String[]> folderContent = this.guiData.getFolderContent(processor.getParentDirectory().getAbsolutePath());
		this.jTable = this.getJTable();
		this.updateTableData(folderContent);
		this.scrollPane = new JScrollPane(jTable);
		this.scrollPane.setBounds(10, 156, 1181, 551);
		this.frmDirectoryExplorer.getContentPane().add(scrollPane);

		JPanel panelOperations = new JPanel();
		panelOperations.setBounds(10, 114, 1181, 32);
		this.frmDirectoryExplorer.getContentPane().add(panelOperations);
		panelOperations.setLayout(null);

		JButton btnCreateFolder = new JButton("Create Folder");
		btnCreateFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCreateFolder.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		btnCreateFolder.setBounds(0, 0, 127, 32);
		panelOperations.add(btnCreateFolder);

		JButton btnCreateFile = new JButton("Create File");
		btnCreateFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnCreateFile.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		btnCreateFile.setBounds(137, 0, 107, 32);
		panelOperations.add(btnCreateFile);

		JButton btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnDeleteSelected.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		btnDeleteSelected.setBounds(254, 0, 147, 32);
		panelOperations.add(btnDeleteSelected);

		JButton btnRename = new JButton("Rename");
		btnRename.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnRename.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
		btnRename.setBounds(411, 0, 91, 32);
		panelOperations.add(btnRename);

		JMenuBar menuBar = prepareMenuBar(processor);
		panelOperations.add(menuBar);

		JButton btnReprocess = new JButton("Reprocess");
		btnReprocess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnReprocess.setFont(new Font("SansSerif", Font.PLAIN, 15));
		btnReprocess.setBounds(512, 0, 107, 32);
		panelOperations.add(btnReprocess);
	}

	private JMenuBar prepareMenuBar(Processor processor) {
		ActionListener menutItemActionListener = actionEvent -> {
			System.out.println("Menu clicked");
			String actionCommand = actionEvent.getActionCommand();
			MenuItems menuItems = MenuItems.getMenuItemEnum(actionCommand);
			switch (menuItems) {
			case PROCESS_ANOTHER_DIRECTORY:
				// TODO
				break;
			case PROCESS_OTHER_DIRECTORY:
				// TODO
				break;
			case VIEW_ALL_FILES:
				this.updateTableData(this.guiData.getAllFileDetails());
				break;
			case VIEW_ALL_FOLDERS:
				this.updateTableData(this.guiData.getAllFoldersDetails());
				break;
			case VIEW_CONTENT:
				this.updateTableData(this.guiData.getFolderContent(processor.getParentDirectory().getAbsolutePath()));
				break;
			default:
				System.out.println("Invalid menu type. Report this issue!!");
				break;
			}
		};

		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		menuBar.setBackground(Color.LIGHT_GRAY);
		menuBar.setBounds(1114, 0, 65, 32);
		menuBar.setToolTipText("Select View");

		JMenu mnMenu = new JMenu("Menu");
		mnMenu.setBackground(Color.LIGHT_GRAY);
		mnMenu.setHorizontalAlignment(SwingConstants.CENTER);
		mnMenu.setBounds(1114, 0, 65, 32);
		mnMenu.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

		JMenuItem mntmViewContent = new JMenuItem(MenuItems.VIEW_CONTENT.getItemValue());
		mntmViewContent.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		mntmViewContent.addActionListener(menutItemActionListener);
		mnMenu.add(mntmViewContent);

		JMenuItem mntmViewAllFiles = new JMenuItem(MenuItems.VIEW_ALL_FILES.getItemValue());
		mntmViewAllFiles.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		mntmViewAllFiles.addActionListener(menutItemActionListener);
		mnMenu.add(mntmViewAllFiles);

		JMenuItem mntmViewAllFolders = new JMenuItem(MenuItems.VIEW_ALL_FOLDERS.getItemValue());
		mntmViewAllFolders.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		mntmViewAllFolders.addActionListener(menutItemActionListener);
		mnMenu.add(mntmViewAllFolders);

		JMenuItem mntmProcessOther = new JMenuItem(MenuItems.PROCESS_OTHER_DIRECTORY.getItemValue());
		mntmProcessOther.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		mntmProcessOther.addActionListener(menutItemActionListener);
		mnMenu.add(mntmProcessOther);

		JMenuItem mntmProcessAnother = new JMenuItem(MenuItems.PROCESS_ANOTHER_DIRECTORY.getItemValue());
		mntmProcessAnother.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
		mntmProcessAnother.addActionListener(menutItemActionListener);
		mnMenu.add(mntmProcessAnother);

		menuBar.add(mnMenu);
		return menuBar;
	}

	private void updateTableData(List<String[]> listNewData) {
		DefaultTableModel model = (DefaultTableModel) getJTable().getModel();
		Iterator<String[]> iterator = listNewData.iterator();
		if (model.getRowCount() > 0) {
			model.setRowCount(0);
		}
		while (iterator.hasNext()) {
			model.addRow(iterator.next());
		}
		this.jTable.setModel(model);
		model.fireTableDataChanged();
	}

	private JTable getJTable() {
		if (this.jTable == null) {
			this.jTable = new JTable() {
				private static final long serialVersionUID = 7739801596675122989L;

				@Override
				public boolean isCellEditable(int nRow, int nCol) {
					return false;
				}
			};
		}
		DefaultTableModel contactTableModel = (DefaultTableModel) this.jTable.getModel();
		contactTableModel.setColumnIdentifiers(COLUMN);
		this.jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
		defaultTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		this.jTable.getColumn("Type").setCellRenderer(defaultTableCellRenderer);
		this.jTable.getColumn("Size").setCellRenderer(defaultTableCellRenderer);
		this.jTable.getColumn("File Count").setCellRenderer(defaultTableCellRenderer);
		return this.jTable;
	}
}
