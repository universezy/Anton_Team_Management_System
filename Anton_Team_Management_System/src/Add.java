
/*
 * @date 2016/5/23
 * @author  ZengYu
 */

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Add extends JFrame implements ActionListener, KeyListener {

	public String username;
	public JFrame jf;

	public String StringID = null, StringProfession = null, StringAttribution = null, StringNote = null;
	public JTextField jtfID, jtfProfession, jtfAttribution, jtfNote;

	public Add(String string_username) {
		// TODO Auto-generated constructor stub
		username = string_username;

		jf = new JFrame("Add Item");
		jf.setLayout(new GridLayout(0, 1));

		JLabel jlID = new JLabel("         ID        ：");
		jtfID = new JTextField(10);
		JPanel jpID = new JPanel();
		jpID.add(jlID);
		jpID.add(jtfID);

		JLabel jlProfession = new JLabel("Profession：");
		jtfProfession = new JTextField(10);
		JPanel jpProfession = new JPanel();
		jpProfession.add(jlProfession);
		jpProfession.add(jtfProfession);

		JLabel jlAttribution = new JLabel(" Attribution：");
		jtfAttribution = new JTextField(10);
		JPanel jpAttribution = new JPanel();
		jpAttribution.add(jlAttribution);
		jpAttribution.add(jtfAttribution);

		JLabel jlNote = new JLabel("       Note     ：");
		jtfNote = new JTextField(10);
		jtfNote.addKeyListener(this);
		JPanel jpNote = new JPanel();
		jpNote.add(jlNote);
		jpNote.add(jtfNote);

		JButton jbAdd = new JButton("Add");
		jbAdd.addActionListener(this);
		JPanel jpAdd = new JPanel();
		jpAdd.add(jbAdd);

		JPanel jpNULL1 = new JPanel();
		JPanel jpNULL2 = new JPanel();

		jf.add(jpNULL1);
		jf.add(jpID);
		jf.add(jpProfession);
		jf.add(jpAttribution);
		jf.add(jpNote);
		jf.add(jpAdd);
		jf.add(jpNULL2);

		jf.setResizable(false);
		jf.setVisible(true);
		jf.setSize(400, 300);
		jf.setLocation(300, 200);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void add() {
		StringID = jtfID.getText();
		StringProfession = jtfProfession.getText();
		StringAttribution = jtfAttribution.getText();
		StringNote = jtfNote.getText();

		if (StringID.equals("") || StringProfession.equals("") || StringAttribution.equals("")) {
			JOptionPane.showConfirmDialog(jf, "ID, Profession and Attribution can't be empty!", "Error",
					JOptionPane.DEFAULT_OPTION);
		} else {
			int a = JOptionPane.showConfirmDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
					"Comfirm :", "Comfirm", JOptionPane.OK_CANCEL_OPTION);
			if (a == 0) {
				File file = new File("res/" + username + "//" + username + ".md");
				// 如果路径不存在,则创建
				if (!file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				FileReader fr = null;
				try {
					fr = new FileReader(file);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				BufferedReader br = new BufferedReader(fr);
				String tempString = new String("");
				int index1, index2;
				boolean ifExit = false;
				try {
					while ((tempString = br.readLine()) != null) {
						index1 = tempString.indexOf("<ID>");
						index2 = tempString.indexOf("</ID>");
						if (tempString.substring(index1 + "<ID>".length(), index2).equals(StringID)) {
							ifExit = true;
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (!ifExit) {
					// 从末尾行写入
					FileWriter fw = null;
					try {
						fw = new FileWriter(file, true);
					} catch (IOException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					BufferedWriter bw = new BufferedWriter(fw);
					try {
						bw.write("<ID>" + StringID + "</ID><Profession>" + StringProfession
								+ "</Profession><Attribution>" + StringAttribution + "</Attribution><Note>" + StringNote
								+ "</Note>");
						bw.newLine();
					} catch (IOException e6) {
						// TODO Auto-generated catch block
						e6.printStackTrace();
					}
					try {
						bw.flush();
						fw.close();
						bw.close();

						JOptionPane.showConfirmDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
								"Add successful !\n", "Result", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
						jtfID.setText(null);
						jtfProfession.setText(null);
						jtfAttribution.setText(null);
						jtfNote.setText(null);
						jtfID.requestFocus();
					} catch (IOException e7) {
						// TODO Auto-generated catch block
						e7.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
							"Member exits !", "Error", JOptionPane.ERROR_MESSAGE);
					jtfID.setText(null);
					jtfProfession.setText(null);
					jtfAttribution.setText(null);
					jtfNote.setText(null);
					jtfID.requestFocus();
				}
			}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comm = e.getActionCommand();
		if (comm.equals("Add")) {
			add();
		}
	}

	public static void main(String string_username) {
		new Add(string_username);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (((KeyEvent) e).getKeyChar() == KeyEvent.VK_ENTER) {
			add();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
