
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.*;

public class Login implements ActionListener, KeyListener {

	private JFrame jf;
	private JTextField jtf;
	private JPasswordField jpf;
	public String username, password;
	private String[] ButtonString = { "   Login   ", "Register" };
	private JPanel imagePanel;
	private ImageIcon ii_bg;

	public Login() {
		jf = new JFrame("Anton Team Management System —— Login");
		ii_bg = new ImageIcon("res/layout/background_login_register.png");// 背景图片
		JLabel label = new JLabel(ii_bg);
		label.setBounds(0, 0, ii_bg.getIconWidth(), ii_bg.getIconHeight());
		imagePanel = (JPanel) jf.getContentPane();
		imagePanel.setOpaque(false);
		imagePanel.setLayout(new BorderLayout());

		JPanel jp_all = new JPanel();
		jp_all.setLayout(new GridLayout(0, 1));
		jp_all.setOpaque(false);

		JLabel jl0 = new JLabel("");
		jl0.setFont(new Font("Dialog", 1, 14));
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

		JLabel jl2 = new JLabel("Password： ");
		jl2.setFont(new Font("Dialog", 1, 18));
		jl2.setForeground(Color.BLACK);
		jpf = new JPasswordField(12);
		jpf.addKeyListener(this);
		jpf.setOpaque(false);
		jpf.setFont(new Font("Dialog", 1, 18));
		jpf.setForeground(Color.BLACK);
		JPanel jp2 = new JPanel();
		jp2.setOpaque(false);
		jp2.add(jl2);
		jp2.add(jpf);
		jp_all.add(jp2);

		JButton jb1 = new JButton(ButtonString[0]);
		jb1.addActionListener(this);
		jb1.setContentAreaFilled(false);
		jb1.setFont(new Font("Dialog", 1, 18));
		jb1.setForeground(Color.BLACK);
		jb1.setFocusable(false);
		JPanel jp3 = new JPanel();
		jp3.setLayout(new FlowLayout());
		jp3.setOpaque(false);
		jp3.add(jb1);
		jp_all.add(jp3);

		JButton jb2 = new JButton(ButtonString[1]);
		jb2.addActionListener(this);
		jb2.setContentAreaFilled(false);
		jb2.setFont(new Font("Dialog", 1, 18));
		jb2.setForeground(Color.BLACK);
		jb2.setFocusable(false);
		JPanel jp4 = new JPanel();
		jp4.setLayout(new FlowLayout());
		jp4.setOpaque(false);
		jp4.add(jb2);
		jp_all.add(jp4);

		imagePanel.add(jp_all, BorderLayout.CENTER);

		jf.getLayeredPane().setLayout(null);
		jf.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		jf.setResizable(false);
		jf.setVisible(true);
		jf.setSize(ii_bg.getIconWidth() + 6, ii_bg.getIconHeight() + 29);
		jf.setLocation(300, 200);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		new Login();
	}

	public void login() {
		username = jtf.getText();
		password = new String(jpf.getPassword());

		if ("".equals(username) || "".equals(password) || jpf.getPassword() == null) {
			JOptionPane.showMessageDialog(jf, "User and password can't be empty!", "Error", JOptionPane.ERROR_MESSAGE);
			jtf.setText(null);
			jpf.setText(null);
			jtf.requestFocus();
		} else {
			File file = new File("res/user_list.md");
			try {
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
				int index1, index2, index3, index4;
				boolean ifExit = false;
				try {
					for (int i = 0; i < count; i++) {
						tempString = br.readLine();
						index1 = tempString.indexOf("<user>");
						index2 = tempString.indexOf("</user>");
						// 用户存在，执行登录操作
						if (tempString.substring(index1 + "<user>".length(), index2).equals(username)) {
							ifExit = true;
							index3 = tempString.indexOf("<password>");
							index4 = tempString.indexOf("</password>");

							if (tempString.substring(index3 + "<password>".length(), index4).equals(password)) { // 密码正确
								JOptionPane.showConfirmDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
										"Hello ： " + username, "Result", JOptionPane.OK_OPTION,
										JOptionPane.INFORMATION_MESSAGE);
								jf.dispose();
								new Main(username);
							} else { // 密码错误
								JOptionPane.showMessageDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
										"Wrong password !", "Error", JOptionPane.ERROR_MESSAGE);
								jpf.setText(null);
								jpf.requestFocus();
								break;
							}
						}
					}
					// 未找到用户
					if (ifExit == false) {
						JOptionPane.showMessageDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
								"User doesn't exit!", "Error", JOptionPane.ERROR_MESSAGE);
						jtf.setText(null);
						jpf.setText(null);
						jtf.requestFocus();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					count_fr.close();
					fr.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				int a = JOptionPane.showConfirmDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
						"The specified project file could not be found or is corrupted!\nContinue?", "Error",
						JOptionPane.YES_NO_OPTION);
				if (a == JOptionPane.YES_OPTION) {
					jtf.setText(null);
					jpf.setText(null);
					jtf.requestFocus();
				} else {
					jf.dispose();
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String comm = e.getActionCommand();

		if (comm.equals(ButtonString[1])) {
			jf.dispose();
			new Register("Register");
		} else if (comm.equals(ButtonString[0])) {
			login();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (((KeyEvent) e).getKeyChar() == KeyEvent.VK_ENTER) {
			login();
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