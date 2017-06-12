
/*
 * @date 2016/5/23
 * @author  ZengYu
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Main extends JFrame implements ActionListener {

	private JFrame jf;
	public String username;
	private String[] ButtonString = { "Member Information", "         Team Edit         ",
			"        Check  List        " };

	// 时间格式
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private JLabel jl2 = new JLabel(sdf.format(new Date()));
	private JPanel imagePanel;
	private ImageIcon ii_bg;

	public Main(String string_username) {
		username = string_username;
		jf = new JFrame("Anton Team Management System —— Main");
		((JPanel) jf.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		ii_bg = new ImageIcon("res/layout/background_main.png");// 背景图片
		JLabel label = new JLabel(ii_bg);
		label.setBounds(0, 0, ii_bg.getIconWidth(), ii_bg.getIconHeight());
		imagePanel = (JPanel) jf.getContentPane();
		imagePanel.setOpaque(false);
		imagePanel.setLayout(new BorderLayout());

		JPanel jp_all = new JPanel();
		jp_all.setLayout(new GridLayout(0, 1));
		jp_all.setOpaque(false);

		JLabel jl0 = new JLabel("");
		jl0.setFont(new Font("Dialog", 1, 15));
		jl0.setForeground(Color.WHITE);
		JPanel jp0 = new JPanel();
		jp0.setOpaque(false);
		jp0.add(jl0);
		jp_all.add(jp0);

		JLabel jl1 = new JLabel("Username : " + username + "       ");
		jl1.setFont(new Font("Dialog", 1, 15));
		jl1.setForeground(Color.WHITE);
		jl2.setFont(new Font("Dialog", 1, 15));
		jl2.setForeground(Color.WHITE);
		JPanel jp1 = new JPanel();
		jp1.setOpaque(false);
		jp1.add(jl1);
		jp1.add(jl2);
		jp_all.add(jp1);

		JButton jb1 = new JButton(ButtonString[0]);
		jb1.addActionListener(this);
		jb1.setContentAreaFilled(false);
		jb1.setFont(new Font("Dialog", 1, 15));
		jb1.setForeground(Color.WHITE);
		jb1.setFocusable(false);
		JPanel jp2 = new JPanel();
		jp2.setOpaque(false);
		jp2.add(jb1);
		jp_all.add(jp2);

		JButton jb2 = new JButton(ButtonString[1]);
		jb2.addActionListener(this);
		jb2.setContentAreaFilled(false);
		jb2.setFont(new Font("Dialog", 1, 15));
		jb2.setForeground(Color.WHITE);
		jb2.setFocusable(false);
		JPanel jp3 = new JPanel();
		jp3.setOpaque(false);
		jp3.add(jb2);
		jp_all.add(jp3);

		JButton jb3 = new JButton(ButtonString[2]);
		jb3.addActionListener(this);
		jb3.setContentAreaFilled(false);
		jb3.setFont(new Font("Dialog", 1, 15));
		jb3.setForeground(Color.WHITE);
		jb3.setFocusable(false);
		JPanel jp4 = new JPanel();
		jp4.setOpaque(false);
		jp4.add(jb3);
		jp_all.add(jp4);

		JPanel jp_copyright = new JPanel();
		jp_copyright.setOpaque(false);
		JLabel jlcr = new JLabel("Copyright@ZengYu of UESTC");
		jlcr.setFont(new Font("Dialog", 1, 15));
		jlcr.setForeground(Color.YELLOW);
		jp_copyright.add(jlcr);
		jp_all.add(jp_copyright);

		imagePanel.add(jp_all, BorderLayout.CENTER);

		jf.getLayeredPane().setLayout(null);
		jf.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		jf.setResizable(false);
		jf.setVisible(true);
		jf.setSize(ii_bg.getIconWidth(), ii_bg.getIconHeight());
		jf.setLocation(300, 200);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Timer t = new Timer();
		t.schedule(new MyTask(), 1000, 1000);
	}

	class MyTask extends TimerTask {
		@Override
		public void run() {
			String s = sdf.format(new Date());
			jl2.setText(s);
		}
	}

	public static void main(String string_username) {
		new Main(string_username);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comm = e.getActionCommand();

		if (comm.equals(ButtonString[0])) {
			new MemberInformation(username);
		} else if (comm.equals(ButtonString[1])) {
			new TeamEdit(username);
		} else if (comm.equals(ButtonString[2])) {
			new checkList(username);
		}
	}
}
