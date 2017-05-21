
/*
 * @date 2016/5/23
 * @author  ZengYu
 */

import java.awt.*;
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

import javax.swing.*;

public class Register implements ActionListener, KeyListener {

	private JFrame jf;
	private JTextField jtf;
	private JPasswordField jpf1, jpf2;
	private String register_username, register_password, confirm_password;
	private String[] ButtonString = { "Register", "   Login   " };
	private JPanel imagePanel;
	private ImageIcon ii_bg;

	public Register(String args) {
		jf = new JFrame("Anton Team Management System —— Register");
		ii_bg = new ImageIcon("res/layout/background_login_register.png");// 背景图片
		JLabel label = new JLabel(ii_bg);
		label.setBounds(0, 0, ii_bg.getIconWidth(), ii_bg.getIconHeight());
		imagePanel = (JPanel) jf.getContentPane();
		imagePanel.setOpaque(false);
		imagePanel.setLayout(new BorderLayout());

		JPanel jp_all = new JPanel();
		jp_all.setLayout(new GridLayout(0, 1));
		jp_all.setOpaque(false);

		JLabel jl0 = new JLabel("New User");
		jl0.setFont(new Font("Dialog", 1, 18));
		jl0.setForeground(Color.BLACK);
		JPanel jp0 = new JPanel();
		jp0.setOpaque(false);
		jp0.add(jl0);
		jp_all.add(jp0);

		JLabel jl1 = new JLabel("Username：");
		jl1.setFont(new Font("Dialog", 1, 18));
		jl1.setForeground(Color.BLACK);
		jtf = new JTextField(12);
		jtf.setOpaque(false);
		jtf.setFont(new Font("Dialog", 1, 18));
		jtf.setForeground(Color.BLACK);
		JPanel jp1 = new JPanel();
		jp1.setOpaque(false);
		jp1.add(jl1);
		jp1.add(jtf);
		jp_all.add(jp1);

		JLabel jl2 = new JLabel("Password：");
		jl2.setFont(new Font("Dialog", 1, 18));
		jl2.setForeground(Color.BLACK);
		jpf1 = new JPasswordField(12);
		jpf1.setOpaque(false);
		jpf1.setFont(new Font("Dialog", 1, 18));
		jpf1.setForeground(Color.BLACK);
		JPanel jp2 = new JPanel();
		jp2.setOpaque(false);
		jp2.add(jl2);
		jp2.add(jpf1);
		jp_all.add(jp2);

		JLabel jl3 = new JLabel(" Confirm  ：");
		jl3.setFont(new Font("Dialog", 1, 18));
		jl3.setForeground(Color.BLACK);
		jpf2 = new JPasswordField(12);
		jpf2.addKeyListener(this);
		jpf2.setOpaque(false);
		jpf2.setFont(new Font("Dialog", 1, 18));
		jpf2.setForeground(Color.BLACK);
		JPanel jp3 = new JPanel();
		jp3.setOpaque(false);
		jp3.add(jl3);
		jp3.add(jpf2);
		jp_all.add(jp3);

		JButton jb1 = new JButton(ButtonString[0]);
		jb1.addActionListener(this);
		jb1.setContentAreaFilled(false);
		jb1.setFont(new Font("Dialog", 1, 18));
		jb1.setForeground(Color.BLACK);
		jb1.setFocusable(false);
		JPanel jp4 = new JPanel();
		jp4.setOpaque(false);
		jp4.add(jb1);
		jp_all.add(jp4);

		JButton jb2 = new JButton(ButtonString[1]);
		jb2.addActionListener(this);
		jb2.setContentAreaFilled(false);
		jb2.setFont(new Font("Dialog", 1, 18));
		jb2.setForeground(Color.BLACK);
		jb2.setFocusable(false);
		JPanel jp5 = new JPanel();
		jp5.setOpaque(false);
		jp5.add(jb2);
		jp_all.add(jp5);

		imagePanel.add(jp_all, BorderLayout.CENTER);

		jf.getLayeredPane().setLayout(null);
		jf.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		jf.setResizable(false);
		jf.setVisible(true);
		jf.setSize(ii_bg.getIconWidth() + 6, ii_bg.getIconHeight() + 29);
		jf.setLocation(300, 200);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String args) {
		if (args.equals("Register"))
			new Register(args);
	}

	public void register() {
		register_username = jtf.getText();
		register_password = new String(jpf1.getPassword());
		confirm_password = new String(jpf2.getPassword());

		// 增加字符串长度限制:
		if ("".equals(register_username) || "".equals(register_password) || jpf1.getPassword() == null
				|| "".equals(confirm_password) || jpf2.getPassword() == null) {
			JOptionPane.showMessageDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
					"Information is not whole !", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			String user_massage = "<user>" + register_username + "</user><password>" + register_password
					+ "</password>";
			File file = new File("res/user_list.md");
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
			// 密码相同
			if (register_password.equals(confirm_password)) {
				FileReader count_fr = null;
				FileReader fr = null;
				try {
					count_fr = new FileReader(file);
					fr = new FileReader(file);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				BufferedReader count_br = new BufferedReader(count_fr);
				BufferedReader br = new BufferedReader(fr);
				int count = 0;
				try {
					while (count_br.readLine() != null)
						count++;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String tempString = new String("");
				int index1, index2;
				boolean ifExit = false;
				// 遍历用户数据，判断是否已注册该用户
				try {
					for (int i = 0; i < count; i++) {
						tempString = br.readLine();
						index1 = tempString.indexOf("<user>");
						index2 = tempString.indexOf("</user>");

						// 用户存在
						if (tempString.substring(index1 + "<user>".length(), index2).equals(register_username)) {
							JOptionPane.showMessageDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
									"User exits !\nPlease try others !", "Error", JOptionPane.ERROR_MESSAGE);
							ifExit = true;
							jtf.setText(null);
							jpf1.setText(null);
							jpf2.setText(null);
							jtf.requestFocus();
							break;
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (ifExit == false) {
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
						bw.write(user_massage);
						bw.newLine();
					} catch (IOException e6) {
						// TODO Auto-generated catch block
						e6.printStackTrace();
					}
					try {
						bw.flush();
						count_fr.close();
						fw.close();
						bw.close();
					} catch (IOException e7) {
						// TODO Auto-generated catch block
						e7.printStackTrace();
					}
					// 新建用户成功
					int a = JOptionPane.showConfirmDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
							"Register successful !\n" + "Username ： " + register_username + "\nTurn to Login ?",
							"Result", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
					if (a == JOptionPane.YES_OPTION) {
						jf.dispose();
						new Login();
					} else if (a == JOptionPane.NO_OPTION) {
						jpf1.setText(null);
						jpf2.setText(null);
						jpf1.requestFocus();
					}
				}
			} else {
				// 重置编辑框
				JOptionPane.showMessageDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
						"Tow passwords are different !", "Error", JOptionPane.ERROR_MESSAGE);
				jpf1.setText(null);
				jpf2.setText(null);
				jpf1.requestFocus();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String comm = e.getActionCommand();

		if (comm.equals(ButtonString[0])) {
			register();
		} else if (comm.equals(ButtonString[1])) {
			jf.dispose();
			new Login();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (((KeyEvent) e).getKeyChar() == KeyEvent.VK_ENTER) {
			register();
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