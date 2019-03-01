package org.eclipse.wb.swt;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;



import javax.swing.JOptionPane;


public class Chooser {

	protected Shell shlPleaseChooseA;
	private Text text;

	private Quantizer ob = new Quantizer();
	private boolean rb = false; // Comp.
	private boolean rb_1 = false; // Decomp.
	private Text text_1;


	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Chooser window = new Chooser();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlPleaseChooseA.open();
		shlPleaseChooseA.layout();
		while (!shlPleaseChooseA.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlPleaseChooseA = new Shell();
		shlPleaseChooseA.setImage(SWTResourceManager.getImage(Chooser.class, "/org/eclipse/jface/dialogs/images/message_info.png"));
		shlPleaseChooseA.setText("Quantizer");
		shlPleaseChooseA.setModified(true);
		shlPleaseChooseA.setSize(430, 322);
		
		Button btnNewButton = new Button(shlPleaseChooseA, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(fileChooser);
				if (result == JFileChooser.APPROVE_OPTION) {
				    File selectedFile = fileChooser.getSelectedFile();
				    
				    ob.set_path(selectedFile.getAbsolutePath());
				    text.setText(ob.get_path());
				    
				    
				}
				
				
				
			}
		});
		btnNewButton.setBounds(150, 125, 75, 25);
		btnNewButton.setText("Browse");
		
		Label lblPleaseChooseA = new Label(shlPleaseChooseA, SWT.NONE);
		lblPleaseChooseA.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblPleaseChooseA.setBounds(10, 10, 175, 32);
		lblPleaseChooseA.setText("How Many Levels?");
		
		Label lblFile = new Label(shlPleaseChooseA, SWT.NONE);
		lblFile.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		lblFile.setBounds(10, 80, 39, 32);
		lblFile.setText("File:");
		
		Button btnRadioButton = new Button(shlPleaseChooseA, SWT.RADIO);
		btnRadioButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				rb = true;
				rb_1 = false;
			}
		});
		btnRadioButton.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		btnRadioButton.setBounds(10, 198, 122, 25);
		btnRadioButton.setText("Compress");
		
		Button btnRadioButton_1 = new Button(shlPleaseChooseA, SWT.RADIO);
		btnRadioButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				rb_1 = true;
				rb = false;
			}
		});
		btnRadioButton_1.setFont(SWTResourceManager.getFont("Segoe UI", 14, SWT.BOLD));
		btnRadioButton_1.setBounds(234, 198, 141, 25);
		btnRadioButton_1.setText("Decompress");
		
		Button btnNewButton_1 = new Button(shlPleaseChooseA, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if (rb) {
					try {
						ob.set_level(text_1.getText());
						ob.Compress();
					}
					catch(IOException excp) {
						JOptionPane.showMessageDialog(null, "Please Select a Valid File", ""
								+ "Error", JOptionPane.INFORMATION_MESSAGE);
					}
					JOptionPane.showMessageDialog(null, "Done!", ""
					+ "Message", JOptionPane.INFORMATION_MESSAGE);
				}
				else if (rb_1) {
					try {
						ob.Decompress();
					}
					catch (IOException excp) {
						JOptionPane.showMessageDialog(null, "Please Select a Valid File", ""
								+ "Error", JOptionPane.INFORMATION_MESSAGE);
					}
					
					JOptionPane.showMessageDialog(null, "Done!", ""
					+ "Message", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null, "Please Select one option!", ""
							+ "Error", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnNewButton_1.setBounds(150, 236, 75, 25);
		btnNewButton_1.setText("GO");
		
		text = new Text(shlPleaseChooseA, SWT.BORDER);
		text.setEditable(false);
		text.setToolTipText("");
		text.setBounds(55, 85, 355, 21);
		
		text_1 = new Text(shlPleaseChooseA, SWT.BORDER);
		text_1.setToolTipText("");
		text_1.setBounds(193, 15, 217, 21);
		

	}
}