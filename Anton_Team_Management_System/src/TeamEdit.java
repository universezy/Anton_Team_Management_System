
/*
 * @date 2016/7/27
 * @author  ZengYu
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class TeamEdit extends JFrame implements ActionListener {
	private JFrame jf;
	public String username, StringName, StringDate, StringTime, StringChannel,StringYY, StringCommander, blank = "          ",
			noMember = "                         ";
	private JTable table, table1 = null, table2 = null, table3 = null, table4 = null;
	private MyTableModel model;
	private Object[][] data;
	@SuppressWarnings("rawtypes")
	private JComboBox jcb, jcb_team1, jcb_team2;
	private JScrollPane jsp_all_member, jsp_table1 = new JScrollPane(), jsp_table2 = new JScrollPane();
	private JTextField jtfName, jtfDate, jtfTime, jtfChannel,jtfYY, jtfCommander;
	private JButton jb_check1, jb_check2, jb_Reset1, jb_Reset2, jb_Reset3, jb_Reset4;
	private String[] ButtonString = { "  Open  ", "   New   ", "Delete", "★   Submit   ★", "Reset", "Check" };
	private String[] InterceptString = { blank, "黑雾之源", "擎天之柱 A", "擎天之柱 B", "震颤的大地 A", "震颤的大地 B", "震颤的大地 A+B", "舰炮防御战","舰炮防御战+黑雾之源" };
	private String[] DestroyString = { blank, "能量阻截战", "普通孵化场", "感染孵化场", "黑色火山", "安徒恩心脏" };
	private JPanel jp_jcb = new JPanel(),jp_null = new JPanel(),jp_all_member = new JPanel();
	private JPanel jp_table, jp_top, jp_button;
	private JPanel jp_table_top_left, jp_table_top_center, jp_table_top_right, jp_table_top;
	private JPanel jp_table_buttom_left, jp_table_buttom_center, jp_table_buttom_right, jp_table_buttom;
	private JPanel_Background jp_detail_mission;
	private int jf_width = 2200, jf_height = 1500, MemberString_count = 1;
	private String[] string_raid_list, MemberString;
	private TableModel1 model1 = null, model2 = null;
	private TableModel2 model3 = null, model4 = null;
	private boolean toCommit = false;
	private ImageIcon background_Intercept, background_Destroy;

	private String[][] Table_Data1 = new String[5][4];
	private String[][] Table_Data2 = new String[5][4];
	private String[][] Table_Data3 = new String[5][3];
	private String[][] Table_Data4 = new String[5][3];

	public TeamEdit(String string_username) {
		username = string_username;

		// 数据装载
		Load_Raid_List();

		// 布局装载
		setLayoutAll();
	}

	public static void main(String string_username) {
		new TeamEdit(string_username);
	}

	public String[] Load_Raid_List() {
		File file = new File("res/" + username + "/TeamList");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			file.mkdirs();
		}
		File[] file_raid_list = file.listFiles();
		String tempString = new String();
		string_raid_list = new String[file_raid_list.length + 1];
		string_raid_list[0] = noMember;
		for (int i = 0; i < file_raid_list.length; i++) {
			if (file_raid_list[i].isFile()) {
				System.out.println("File：" + file_raid_list[i]);
				tempString = file_raid_list[i].toString();
				string_raid_list[i + 1] = tempString.substring(
						tempString.indexOf(file.toString()) + file.toString().length() + 1,
						tempString.lastIndexOf(".md"));
			}
		}
		return string_raid_list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setLayoutAll() {
		jf = new JFrame("Anton Team Management System —— Team Edit");
		jf.setLayout(new BorderLayout());

		// 设置jp_all_member布局
		// 采用自定义数据模型
		model = new MyTableModel();
		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); // 不可整列移动
		table.getTableHeader().setResizingAllowed(false); // 不可拉动表格
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		ButtonColumn buttonAddColumn = new ButtonColumn(table, 4);
		ButtonColumn buttonDeleteColumn = new ButtonColumn(table, 5);

		buttonAddColumn.text = "+";
		buttonAddColumn.ID = "Add";
		buttonDeleteColumn.text = "×";
		buttonDeleteColumn.ID = "Delete";

		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		/* 用JScrollPane装载JTable，这样超出范围的列就可以通过滚动条来查看 */
		jsp_all_member = new JScrollPane(table);
		jsp_all_member.setPreferredSize(new Dimension(450, 455));
		jp_all_member.add(jsp_all_member, BorderLayout.SOUTH);

		// 设置jp_detail_mission布局
		// 顶部组件
		jp_button = new JPanel();
		jp_button.setOpaque(false);
		jcb = new JComboBox(string_raid_list);
		jcb.setOpaque(false);
		jp_jcb.add(jcb);
		jp_jcb.setOpaque(false);
		jp_null.setOpaque(false);
		JButton jb_open = new JButton(ButtonString[0]);
		jb_open.setFont(new Font("Dialog", 1, 13));
		jb_open.addActionListener(this);
		jb_open.setContentAreaFilled(false);
		JButton jb_new = new JButton(ButtonString[1]);
		jb_new.setFont(new Font("Dialog", 1, 13));
		jb_new.addActionListener(this);
		jb_new.setContentAreaFilled(false);
		JButton jb_delete = new JButton(ButtonString[2]);
		jb_delete.setFont(new Font("Dialog", 1, 13));
		jb_delete.addActionListener(this);
		jb_delete.setContentAreaFilled(false);
		JButton jb_commit = new JButton(ButtonString[3]);
		jb_commit.setFont(new Font("Dialog", 1, 13));
		jb_commit.addActionListener(this);
		jb_commit.setContentAreaFilled(false);

		jp_button.setLayout(new FlowLayout());
		jp_button.add(jp_jcb);
		jp_button.add(jb_open);
		jp_button.add(jb_new);
		jp_button.add(jb_delete);
		jp_button.add(jp_null);
		jp_button.add(jb_commit);

		// 标题属性
		JPanel jp_title = new JPanel();
		jp_title.setOpaque(false);
		JLabel jlName = new JLabel("Name：");
		jtfName = new JTextField(10);
		jtfName.setOpaque(false);
		JLabel jlDate = new JLabel("Date：");
		jtfDate = new JTextField(7);
		jtfDate.setOpaque(false);
		JLabel jlTime = new JLabel("Time：");
		jtfTime = new JTextField(7);
		jtfTime.setOpaque(false);
		JLabel jlChannel = new JLabel("Channel：");
		jtfChannel = new JTextField(5);
		jtfChannel.setOpaque(false);
		JLabel jlYY = new JLabel("YY：");
		jtfYY = new JTextField(7);
		jtfYY.setOpaque(false);
		JLabel jlCommander = new JLabel("Commander：");
		jtfCommander = new JTextField(7);
		jtfCommander.setOpaque(false);

		jp_title.add(jlName);
		jp_title.add(jtfName);
		jp_title.add(jlDate);
		jp_title.add(jtfDate);
		jp_title.add(jlTime);
		jp_title.add(jtfTime);
		jp_title.add(jlChannel);
		jp_title.add(jtfChannel);
		jp_title.add(jlYY);
		jp_title.add(jtfYY);
		jp_title.add(jlCommander);
		jp_title.add(jtfCommander);

		// 上部
		jp_top = new JPanel();
		jp_top.setLayout(new BorderLayout());
		jp_top.setOpaque(false);
		jp_top.add(jp_button, BorderLayout.NORTH);
		jp_top.add(jp_title, BorderLayout.SOUTH);

		// 上表格
		jp_table_top = new JPanel();
		jp_table_top.setLayout(new FlowLayout());
		jp_table_top.setOpaque(false);
		jp_table_top_left = new JPanel();
		jp_table_top_left.setOpaque(false);
		jp_table_top_center = new JPanel();
		jp_table_top_center.setOpaque(false);
		jp_table_top_right = new JPanel();
		jp_table_top_right.setOpaque(false);

		MemberString = new String[MemberString_count];
		MemberString[0] = noMember;
		tableData();

		// 左边 imagePanel_Intercept
		jp_table_top_left.setLayout(new BorderLayout());
		jp_table_top_left.setOpaque(false);
		background_Intercept = new ImageIcon("res/layout/Intercept.png");// 背景图片
		JLabel jlTeam1 = new JLabel(background_Intercept);
		jlTeam1.setBounds(0, 0, background_Intercept.getIconWidth(), background_Intercept.getIconHeight());

		jb_Reset1 = new JButton(ButtonString[4]);
		jb_Reset1.addActionListener(this);
		jb_Reset1.setContentAreaFilled(false);
		jb_check1 = new JButton(ButtonString[5]);
		jb_check1.addActionListener(this);
		jb_check1.setContentAreaFilled(false);
		JPanel jp_table_top_left_button = new JPanel();
		jp_table_top_left_button.setLayout(new FlowLayout());
		jp_table_top_left_button.setOpaque(false);
		jp_table_top_left_button.add(jb_Reset1);
		jp_table_top_left_button.add(jb_check1);
		jp_table_top_left.add(jlTeam1, BorderLayout.NORTH);
		jp_table_top_left.add(jsp_table1, BorderLayout.CENTER);
		jp_table_top_left.add(jp_table_top_left_button, BorderLayout.SOUTH);

		// 中间
		JLabel jlTeam_center = new JLabel("  Team  ");
		jlTeam_center.setFont(new Font("Dialog", 1, 15));
		jp_table_top_center.add(jlTeam_center);

		// 右边
		jp_table_top_right.setLayout(new BorderLayout());
		jp_table_top_right.setOpaque(false);
		background_Destroy = new ImageIcon("res/layout/Destroy.png");// 背景图片
		JLabel jlTeam2 = new JLabel(background_Destroy);
		jlTeam2.setBounds(0, 0, background_Destroy.getIconWidth(), background_Destroy.getIconHeight());

		jb_Reset2 = new JButton(ButtonString[4]);
		jb_Reset2.addActionListener(this);
		jb_Reset2.setContentAreaFilled(false);
		jb_check2 = new JButton(ButtonString[5]);
		jb_check2.addActionListener(this);
		jb_check2.setContentAreaFilled(false);
		JPanel jp_table_top_right_button = new JPanel();
		jp_table_top_right_button.setLayout(new FlowLayout());
		jp_table_top_right_button.setOpaque(false);
		jp_table_top_right_button.add(jb_Reset2);
		jp_table_top_right_button.add(jb_check2);
		jp_table_top_right.add(jlTeam2, BorderLayout.NORTH);
		jp_table_top_right.add(jsp_table2, BorderLayout.CENTER);
		jp_table_top_right.add(jp_table_top_right_button, BorderLayout.SOUTH);

		jp_table_top.add(jp_table_top_left);
		jp_table_top.add(jp_table_top_center);
		jp_table_top.add(jp_table_top_right);

		// 下表格
		jp_table_buttom = new JPanel();
		jp_table_buttom.setLayout(new FlowLayout());
		jp_table_buttom.setOpaque(false);
		jp_table_buttom_left = new JPanel();
		jp_table_buttom_left.setOpaque(false);
		jp_table_buttom_center = new JPanel();
		jp_table_buttom_center.setOpaque(false);
		jp_table_buttom_right = new JPanel();
		jp_table_buttom_right.setOpaque(false);

		// 左边
		jp_table_buttom_left.setLayout(new BorderLayout());
		JLabel jlMission1 = new JLabel(background_Intercept);
		jlMission1.setBounds(0, 0, background_Intercept.getIconWidth(), background_Intercept.getIconHeight());

		model3 = new TableModel2();
		table3 = new JTable(model3) { // 设置jtable的单元格为透明的
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (c instanceof JComponent) {
					((JComponent) c).setOpaque(false);
				}
				return c;
			}
		};
		table3.setOpaque(false);
		table3.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		table3.getTableHeader().setReorderingAllowed(false); // 不可整列移动
		table3.getTableHeader().setResizingAllowed(false); // 不可拉动表格
		table3.getColumnModel().getColumn(0).setPreferredWidth(20);
		JComboBox jcb_mission1 = new JComboBox(InterceptString);
		TableColumnModel tcm_mission1 = table3.getColumnModel();
		tcm_mission1.getColumn(1).setCellEditor(new DefaultCellEditor(jcb_mission1)); // 设置某列采用JComboBox组件
		tcm_mission1.getColumn(2).setCellEditor(new DefaultCellEditor(jcb_mission1));
		tcm_mission1.getColumn(3).setCellEditor(new DefaultCellEditor(jcb_mission1));

		model3.addRow("Team 1", blank, blank, blank);
		model3.addRow("Team 2", blank, blank, blank);
		model3.addRow("Team 3", blank, blank, blank);
		model3.addRow("Team 4", blank, blank, blank);
		model3.addRow("Team 5", blank, blank, blank);
		table3.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		jb_Reset3 = new JButton(ButtonString[4]);
		jb_Reset3.addActionListener(this);
		jb_Reset3.setContentAreaFilled(false);
		JPanel jp_table_buttom_left_button = new JPanel();
		jp_table_buttom_left_button.setLayout(new FlowLayout());
		jp_table_buttom_left_button.setOpaque(false);
		jp_table_buttom_left_button.add(jb_Reset3);
		jp_table_buttom_left.add(jlMission1, BorderLayout.NORTH);
		JScrollPane jsp_table3 = new JScrollPane(table3);
		jsp_table3.getViewport().setOpaque(false);
		jsp_table3.setOpaque(false);
		jsp_table3.setPreferredSize(new Dimension(400, 105));
		jp_table_buttom_left.add(jsp_table3, BorderLayout.CENTER);
		jp_table_buttom_left.add(jp_table_buttom_left_button, BorderLayout.SOUTH);

		// 中间
		JLabel jlMission_center = new JLabel("Mission");
		jlMission_center.setFont(new Font("Dialog", 1, 15));
		jp_table_buttom_center.add(jlMission_center);

		// 右边
		jp_table_buttom_right.setLayout(new BorderLayout());
		JLabel jlMission2 = new JLabel(background_Destroy);
		jlMission2.setBounds(0, 0, background_Destroy.getIconWidth(), background_Destroy.getIconHeight());

		model4 = new TableModel2();
		table4 = new JTable(model4) { // 设置jtable的单元格为透明的
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (c instanceof JComponent) {
					((JComponent) c).setOpaque(false);
				}
				return c;
			}
		};
		table4.setOpaque(false);
		table4.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		table4.getTableHeader().setReorderingAllowed(false); // 不可整列移动
		table4.getTableHeader().setResizingAllowed(false); // 不可拉动表格
		table4.getColumnModel().getColumn(0).setPreferredWidth(20);
		JComboBox jcb_mission2 = new JComboBox(DestroyString);
		TableColumnModel tcm_mission2 = table4.getColumnModel();
		tcm_mission2.getColumn(1).setCellEditor(new DefaultCellEditor(jcb_mission2)); // 设置某列采用JComboBox组件
		tcm_mission2.getColumn(2).setCellEditor(new DefaultCellEditor(jcb_mission2));
		tcm_mission2.getColumn(3).setCellEditor(new DefaultCellEditor(jcb_mission2));

		model4.addRow("Team 1", blank, blank, blank);
		model4.addRow("Team 2", blank, blank, blank);
		model4.addRow("Team 3", blank, blank, blank);
		model4.addRow("Team 4", blank, blank, blank);
		model4.addRow("Team 5", blank, blank, blank);
		table4.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		jb_Reset4 = new JButton(ButtonString[4]);
		jb_Reset4.addActionListener(this);
		jb_Reset4.setContentAreaFilled(false);
		JPanel jp_table_buttom_right_button = new JPanel();
		jp_table_buttom_right_button.setLayout(new FlowLayout());
		jp_table_buttom_right_button.setOpaque(false);
		jp_table_buttom_right_button.add(jb_Reset4);
		jp_table_buttom_right.add(jlMission2, BorderLayout.NORTH);
		JScrollPane jsp_table4 = new JScrollPane(table4);
		jsp_table4.getViewport().setOpaque(false);
		jsp_table4.setOpaque(false);
		jsp_table4.setPreferredSize(new Dimension(400, 105));
		jp_table_buttom_right.add(jsp_table4, BorderLayout.CENTER);
		jp_table_buttom_right.add(jp_table_buttom_right_button, BorderLayout.SOUTH);

		jp_table_buttom.add(jp_table_buttom_left);
		jp_table_buttom.add(jp_table_buttom_center);
		jp_table_buttom.add(jp_table_buttom_right);

		// 下部
		jp_table = new JPanel();
		jp_table.setLayout(new GridLayout(2, 1));
		jp_table.setOpaque(false);
		jp_table.setSize(1000, 100);
		jp_table.add(jp_table_top);
		jp_table.add(jp_table_buttom);

		// 整个右部
		Image image = new ImageIcon("res/layout/background_team_edit.png").getImage();
		jp_detail_mission = new JPanel_Background(image);
		jp_detail_mission.setLayout(new BorderLayout());
		jp_detail_mission.setOpaque(false);
		jp_detail_mission.add(jp_top, BorderLayout.NORTH);
		jp_detail_mission.add(jp_table, BorderLayout.CENTER);

		jf.add(jp_all_member, BorderLayout.WEST);
		jf.add(jp_detail_mission, BorderLayout.EAST);

		jf.setResizable(false);
		jf.setVisible(true);
		jf.setSize(jf_width, jf_height);
		jf.setLocation(0, 0);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comm = e.getActionCommand();

		if (comm.equals(ButtonString[0])) { // open
			openData();
		} else if (comm.equals(ButtonString[1])) { // new
			NewData();
		} else if (comm.equals(ButtonString[2])) { // delete
			deleteData();
		} else if (comm.equals(ButtonString[3])) { // commit
			commitData();
		} else if (comm.equals(ButtonString[4])) { // reset
			resetData(e);
		} else if (comm.equals(ButtonString[5])) { // check
			checkData(e);
		}
	}

	public void openFile(File file) {
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		String tempString = new String("");
		String[][] tempStringtable1 = new String[5][4];
		String[][] tempStringtable2 = new String[5][4];
		String[][] tempStringtable3 = new String[5][3];
		String[][] tempStringtable4 = new String[5][3];
		String[] tempStringmember, tempStringtable;
		try {
			while ((tempString = br.readLine()) != null) {
				if (tempString.indexOf("<Name>") == 0) {
					tempString = tempString.substring(tempString.indexOf("<Name>") + "<Name>".length(),
							tempString.lastIndexOf("</Name>"));
					jtfName.setText(tempString);
				} else if (tempString.indexOf("<Date>") == 0) {
					tempString = tempString.substring(tempString.indexOf("<Date>") + "<Date>".length(),
							tempString.lastIndexOf("</Date>"));
					jtfDate.setText(tempString);
				} else if (tempString.indexOf("<Time>") == 0) {
					tempString = tempString.substring(tempString.indexOf("<Time>") + "<Time>".length(),
							tempString.lastIndexOf("</Time>"));
					jtfTime.setText(tempString);
					
				} else if (tempString.indexOf("<Channel>") == 0) {
					tempString = tempString.substring(tempString.indexOf("<Channel>") + "<Channel>".length(),
							tempString.lastIndexOf("</Channel>"));
					jtfChannel.setText(tempString);
				}else if (tempString.indexOf("<YY>") == 0) {
					tempString = tempString.substring(tempString.indexOf("<YY>") + "<YY>".length(),
							tempString.lastIndexOf("</YY>"));
					jtfYY.setText(tempString);
				} else if (tempString.indexOf("<Commander>") == 0) {
					tempString = tempString.substring(tempString.indexOf("<Commander>") + "<Commander>".length(),
							tempString.lastIndexOf("</Commander>"));
					jtfCommander.setText(tempString);
				} else if (tempString.indexOf("<table1>") == 0) {
					for (int i = 0; i < 5; i++) {
						tempString = br.readLine();
						if (!tempString.equals("</table1>")) {
							tempStringtable = tempString.split("\t");
							for (int j = 0; j < tempStringtable.length; j++) {
								tempStringtable1[i][j] = tempStringtable[j];
							}
						}
					}
				} else if (tempString.indexOf("<table2>") == 0) {
					for (int i = 0; i < 5; i++) {
						tempString = br.readLine();
						if (!tempString.equals("</table2>")) {
							tempStringtable = tempString.split("\t");
							for (int j = 0; j < tempStringtable.length; j++) {
								tempStringtable2[i][j] = tempStringtable[j];
							}
						}
					}
				} else if (tempString.indexOf("<table3>") == 0) {
					for (int i = 0; i < 5; i++) {
						tempString = br.readLine();
						if (!tempString.equals("</table3>")) {
							tempStringtable = tempString.split("\t");
							for (int j = 0; j < tempStringtable.length; j++) {
								tempStringtable3[i][j] = tempStringtable[j];
							}
						}
					}
				} else if (tempString.indexOf("<table4>") == 0) {
					for (int i = 0; i < 5; i++) {
						tempString = br.readLine();
						if (!tempString.equals("</table4>")) {
							tempStringtable = tempString.split("\t");
							for (int j = 0; j < tempStringtable.length; j++) {
								tempStringtable4[i][j] = tempStringtable[j];
							}
						}
					}
				} else if (tempString.indexOf("<Member>") == 0) {
					while ((tempString = br.readLine()) != null) {
						if (!tempString.equals("</Member>")) {
							tempStringmember = tempString.split("\t");
							MemberString = new String[tempStringmember.length + 1];
							MemberString[0] = noMember;
							for (int i = 0; i < tempStringmember.length; i++) {
								MemberString[i + 1] = tempStringmember[i];
							}
							MemberString_count = MemberString.length;
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jp_table_top_left.remove(jsp_table1);
		jp_table_top_right.remove(jsp_table2);
		tableData();
		jp_table_top_left.add(jsp_table1, BorderLayout.CENTER);
		jp_table_top_right.add(jsp_table2, BorderLayout.CENTER);
		jp_table_top_left.validate();
		jp_table_top_right.validate();
		jp_table_top.validate();
		jp_table.validate();
		jp_detail_mission.validate();
		jf.validate();
		for (int i = 0; i < 5; i++) {
			for (int j = 1; j <= 4; j++) {
				table1.setValueAt(tempStringtable1[i][j - 1], i, j);
				table2.setValueAt(tempStringtable2[i][j - 1], i, j);
			}
			for (int j = 1; j <= 3; j++) {
				table3.setValueAt(tempStringtable3[i][j - 1], i, j);
				table4.setValueAt(tempStringtable4[i][j - 1], i, j);
			}
		}
		try {
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void openData() {
		String Open_path = "res/" + username + "/TeamList/" + jcb.getSelectedItem().toString() + ".md";
		File file = new File(Open_path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			JOptionPane.showMessageDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
					"File doesn't exit !", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			if (!jtfName.getText().equals("")) {
				int a = JOptionPane.showConfirmDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
						"Do you want to commit current task ?", "Warnning", JOptionPane.YES_NO_CANCEL_OPTION);
				if (a == JOptionPane.YES_OPTION) {
					commitData();
					if (toCommit == true) {
						openFile(file);
					}
				} else if (a == JOptionPane.NO_OPTION) {
					openFile(file);
				} else if (a == JOptionPane.CANCEL_OPTION) {
				}
			} else {
				openFile(file);
			}
		}
	}

	public void NewData() {
		int a = JOptionPane.showConfirmDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
				"Do you want to commit current task ?", "Warnning", JOptionPane.YES_NO_CANCEL_OPTION);
		if (a == JOptionPane.YES_OPTION) {
			commitData();
			if (toCommit == true) {
				jf.dispose();
				new TeamEdit(username);
			}
		} else if (a == JOptionPane.NO_OPTION) {
			jf.dispose();
			new TeamEdit(username);
		} else if (a == JOptionPane.CANCEL_OPTION) {
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void deleteData() {
		String Delete_path = "res/" + username + "/TeamList/" + jcb.getSelectedItem().toString() + ".md";
		File file = new File(Delete_path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if (!file.exists()) {
			JOptionPane.showMessageDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
					"File doesn't exit !", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			int a = JOptionPane.showConfirmDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
					"Are you sure to delete this task ?", "Delete", JOptionPane.OK_CANCEL_OPTION);
			if (a == JOptionPane.OK_OPTION) {
				file.delete();
				jp_jcb.remove(jcb);
				Load_Raid_List();
				jcb = new JComboBox(string_raid_list);
				jp_jcb.add(jcb);
				jp_jcb.validate();
				jf.validate();
				System.out.println("Delete : " + Delete_path);
			} else if (a == JOptionPane.CANCEL_OPTION) {

			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void commitData() {
		StringName = jtfName.getText();
		StringDate = jtfDate.getText();
		StringTime = jtfTime.getText();
		StringChannel = jtfChannel.getText();
		StringYY = jtfYY.getText();
		StringCommander = jtfCommander.getText();

		for (int i = 0; i < 5; i++) {
			for (int j = 1; j <= 4; j++) {
				Table_Data1[i][j - 1] = (String) table1.getValueAt(i, j);
				Table_Data2[i][j - 1] = (String) table2.getValueAt(i, j);
			}
		}
		for (int i = 0; i < 5; i++) {
			for (int j = 1; j <= 3; j++) {
				Table_Data3[i][j - 1] = (String) table3.getValueAt(i, j);
				Table_Data4[i][j - 1] = (String) table4.getValueAt(i, j);
			}
		}

		if (StringName.equals("") || StringDate.equals("") || StringTime.equals("") || StringChannel.equals("") ||StringYY.equals("")
				|| StringCommander.equals("")) {
			JOptionPane.showMessageDialog(jf, "Name, Date, Time, Channel, YY and Commander can't be empty!", "Error",
					JOptionPane.ERROR_MESSAGE);
			toCommit = false;
		} else {
			toCommit = true;
			int a = JOptionPane.showConfirmDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
					"Are you sure to commit ?", "Commit", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (a == 0) {
				File file = new File(
						"res/" + username + "/TeamList/" + StringName + "_" + StringDate + "_" + StringTime + ".md");
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
				FileWriter fw = null;
				try {
					fw = new FileWriter(file);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				BufferedWriter bw = new BufferedWriter(fw);
				try {
					bw.write("<Name>" + StringName + "</Name>");
					bw.newLine();
					bw.write("<Date>" + StringDate + "</Date>");
					bw.newLine();
					bw.write("<Time>" + StringTime + "</Time>");
					bw.newLine();
					bw.write("<Channel>" + StringChannel + "</Channel>");
					bw.newLine();
					bw.write("<YY>" + StringYY + "</YY>");
					bw.newLine();
					bw.write("<Commander>" + StringCommander + "</Commander>");
					bw.newLine();
					bw.write("<table1>");
					bw.newLine();
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 4; j++) {
							bw.write(Table_Data1[i][j] + "\t");
						}
						bw.newLine();
					}
					bw.write("</table1>");
					bw.newLine();
					bw.write("<table2>");
					bw.newLine();
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 4; j++) {
							bw.write(Table_Data2[i][j] + "\t");
						}
						bw.newLine();
					}
					bw.write("</table2>");
					bw.newLine();
					bw.write("<table3>");
					bw.newLine();
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 3; j++) {
							bw.write(Table_Data3[i][j] + "\t");
						}
						bw.newLine();
					}
					bw.write("</table3>");
					bw.newLine();
					bw.write("<table4>");
					bw.newLine();
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 3; j++) {
							bw.write(Table_Data4[i][j] + "\t");
						}
						bw.newLine();
					}
					bw.write("</table4>");
					bw.newLine();
					bw.write("<Member>");
					bw.newLine();
					for (int i = 1; i < MemberString.length; i++) {
						bw.write(MemberString[i] + "\t");
					}
					bw.newLine();
					bw.write("</Member>");
					bw.newLine();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					bw.flush();
					bw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("Save at : " + file.getAbsolutePath());
				jp_jcb.remove(jcb);
				Load_Raid_List();
				jcb = new JComboBox(string_raid_list);
				jp_jcb.add(jcb);
				jp_jcb.validate();
				jp_button.validate();
				jp_top.validate();
				jp_detail_mission.validate();
				jf.validate();
				JOptionPane.showConfirmDialog(jf, "Commit successful !", "Result", JOptionPane.OK_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	public void resetData(ActionEvent e) {
		if (e.getSource() == jb_Reset1) {
			for (int i = 0; i < 5; i++) {
				for (int j = 1; j <= 4; j++) {
					table1.setValueAt(blank, i, j);
				}
			}
		} else if (e.getSource() == jb_Reset2) {
			for (int i = 0; i < 5; i++) {
				for (int j = 1; j <= 4; j++) {
					table2.setValueAt(blank, i, j);
				}
			}
		} else if (e.getSource() == jb_Reset3) {
			for (int i = 0; i < 5; i++) {
				for (int j = 1; j <= 3; j++) {
					table3.setValueAt(blank, i, j);
				}
			}
		} else if (e.getSource() == jb_Reset4) {
			for (int i = 0; i < 5; i++) {
				for (int j = 1; j <= 3; j++) {
					table4.setValueAt(blank, i, j);
				}
			}
		}
	}

	public void checkData(ActionEvent e) {
		if (e.getSource() == jb_check1) {
			String tableString1 = new String("");
			boolean isExit = true;
			for (int k = 0; k < 5; k++) {
				for (int l = 0; l < 4; l++) {
					tableString1 += table1.getValueAt(k, l + 1).toString();
				}
			}
			for (int m = 0; m < MemberString.length; m++) {
				if (tableString1.indexOf(MemberString[m]) < 0) {
					isExit = false;
					break;
				}
			}
			if (!isExit) {
				JOptionPane.showMessageDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
						"There are members not in table !", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		} else if (e.getSource() == jb_check2) {
			String tableString2 = new String("");
			boolean isExit = true;
			for (int k = 0; k < 5; k++) {
				for (int l = 0; l < 4; l++) {
					tableString2 += table2.getValueAt(k, l + 1).toString();
				}
			}
			for (int m = 0; m < MemberString.length; m++) {
				if (tableString2.indexOf(MemberString[m]) < 0) {
					isExit = false;
					break;
				}
			}
			if (!isExit) {
				JOptionPane.showMessageDialog(jf, // 如果为null，此框架显示在中央，为jf则显示为jf的中央
						"There are members not in table !", "Warning", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void tableData() {
		model1 = new TableModel1();
		table1 = new JTable(model1) { // 设置jtable的单元格为透明的
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (c instanceof JComponent) {
					((JComponent) c).setOpaque(false);
				}
				return c;
			}
		};
		table1.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		table1.getTableHeader().setReorderingAllowed(false); // 不可整列移动
		table1.getTableHeader().setResizingAllowed(false); // 不可拉动表格
		table1.getColumnModel().getColumn(0).setPreferredWidth(40);
		table1.setOpaque(false);
		jcb_team1 = new JComboBox(MemberString);
		jcb_team1.setOpaque(false);
		TableColumnModel tcm_team1 = table1.getColumnModel();
		tcm_team1.getColumn(1).setCellEditor(new DefaultCellEditor(jcb_team1)); // 设置某列采用JComboBox组件
		tcm_team1.getColumn(2).setCellEditor(new DefaultCellEditor(jcb_team1));
		tcm_team1.getColumn(3).setCellEditor(new DefaultCellEditor(jcb_team1));
		tcm_team1.getColumn(4).setCellEditor(new DefaultCellEditor(jcb_team1));

		model1.addRow("Team 1", blank, blank, blank, blank);
		model1.addRow("Team 2", blank, blank, blank, blank);
		model1.addRow("Team 3", blank, blank, blank, blank);
		model1.addRow("Team 4", blank, blank, blank, blank);
		model1.addRow("Team 5", blank, blank, blank, blank);
		table1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jsp_table1.getViewport().setOpaque(false);
		jsp_table1.setOpaque(false);
		jsp_table1.setViewportView(table1);// 装载表格
		jsp_table1.setPreferredSize(new Dimension(400, 105));

		model2 = new TableModel1();
		table2 = new JTable(model2) { // 设置jtable的单元格为透明的
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (c instanceof JComponent) {
					((JComponent) c).setOpaque(false);
				}
				return c;
			}
		};
		table2.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		table2.getTableHeader().setReorderingAllowed(false); // 不可整列移动
		table2.getTableHeader().setResizingAllowed(false); // 不可拉动表格
		table2.getColumnModel().getColumn(0).setPreferredWidth(40);
		table2.setOpaque(false);
		jcb_team2 = new JComboBox(MemberString);
		jcb_team2.setOpaque(false);
		TableColumnModel tcm_team2 = table2.getColumnModel();
		tcm_team2.getColumn(1).setCellEditor(new DefaultCellEditor(jcb_team2)); // 设置某列采用JComboBox组件
		tcm_team2.getColumn(2).setCellEditor(new DefaultCellEditor(jcb_team2));
		tcm_team2.getColumn(3).setCellEditor(new DefaultCellEditor(jcb_team2));
		tcm_team2.getColumn(4).setCellEditor(new DefaultCellEditor(jcb_team2));

		model2.addRow("Team 1", blank, blank, blank, blank);
		model2.addRow("Team 2", blank, blank, blank, blank);
		model2.addRow("Team 3", blank, blank, blank, blank);
		model2.addRow("Team 4", blank, blank, blank, blank);
		model2.addRow("Team 5", blank, blank, blank, blank);
		table2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		jsp_table2.getViewport().setOpaque(false);
		jsp_table2.setOpaque(false);
		jsp_table2.setViewportView(table2);// 装载表格
		jsp_table2.setPreferredSize(new Dimension(400, 105));
	}

	class MyTableModel extends AbstractTableModel {
		// 单元格元素类型
		@SuppressWarnings("rawtypes")
		private Class[] cellType = { String.class, String.class, String.class, String.class, JButton.class,
				JButton.class };
		// 表头
		private String title[] = { "ID", "Profession", "Attribution", "Note", "Add", "Delete" };

		public MyTableModel() {
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
			FileReader count_fr = null;
			FileReader fr = null;
			try {
				count_fr = new FileReader(file);
				fr = new FileReader(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			BufferedReader count_br = new BufferedReader(count_fr);
			BufferedReader br = new BufferedReader(fr);
			int count = 0;
			try {
				while (count_br.readLine() != null)
					count++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 定义二维数组作为表格数据
			data = new Object[count][6];

			String tempString = new String("");
			for (int i = 0; i < count; i++) {
				int index1, index2, index3, index4, index5, index6, index7, index8;
				try {
					tempString = br.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				index1 = tempString.indexOf("<ID>");
				index2 = tempString.indexOf("</ID>");
				index3 = tempString.indexOf("<Profession>");
				index4 = tempString.indexOf("</Profession>");
				index5 = tempString.indexOf("<Attribution>");
				index6 = tempString.indexOf("</Attribution>");
				index7 = tempString.indexOf("<Note>");
				index8 = tempString.indexOf("</Note>");
				data[i][0] = tempString.substring(index1 + "<ID>".length(), index2);
				data[i][1] = tempString.substring(index3 + "<Profession>".length(), index4);
				data[i][2] = tempString.substring(index5 + "<Attribution>".length(), index6);
				data[i][3] = tempString.substring(index7 + "<Note>".length(), index8);
				data[i][4] = new JButton("Add");
				data[i][5] = new JButton("Delete");
			}
			try {
				count_fr.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public Class<?> getColumnClass(int arg0) {
			// TODO Auto-generated method stub
			return cellType[arg0];
		}

		@Override
		public String getColumnName(int arg0) {
			// TODO Auto-generated method stub
			return title[arg0];
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return title.length;
		}

		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return data.length;
		}

		@Override
		public Object getValueAt(int r, int c) {
			// TODO Auto-generated method stub
			return data[r][c];
		}

		// 重写isCellEditable方法
		public boolean isCellEditable(int r, int c) {
			if (c == 0 || c == 1 || c == 2) {
				return false;
			} else
				return true;
		}

		// 重写setValueAt方法
		public void setValueAt(Object value, int r, int c) {
			data[r][c] = value;
			this.fireTableCellUpdated(r, c);
		}
	}

	class ButtonColumn extends AbstractCellEditor implements ActionListener, TableCellEditor, TableCellRenderer {
		private JButton rb, eb;;
		@SuppressWarnings("unused")
		private int Row;
		private JTable Table;
		private String text = "";
		private String ID = "";

		public ButtonColumn() {
		}

		public ButtonColumn(JTable table, int column) {
			super();
			this.Table = table;
			rb = new JButton();
			eb = new JButton();
			eb.setFocusPainted(false);
			eb.addActionListener(this);
			// 设置该单元格渲染和编辑样式
			TableColumnModel columnModel = Table.getColumnModel();
			columnModel.getColumn(column).setCellRenderer(this);
			columnModel.getColumn(column).setCellEditor(this);

			Table.getColumnModel().getColumn(4).setPreferredWidth(50);
			Table.getColumnModel().getColumn(5).setPreferredWidth(50);
		}

		@Override
		public Object getCellEditorValue() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Component getTableCellRendererComponent(JTable arg0, Object value, boolean arg2, boolean arg3, int arg4,
				int arg5) {
			rb.setText(text);
			return rb;
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			eb.setText(text);
			this.Row = row;
			return eb;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (this.ID.equals("Add")) {
				String tempString = table.getValueAt(table.getSelectedRow(), 0).toString();
				boolean isExit = false;
				for (int i = 0; i < MemberString.length; i++) {
					if (MemberString[i].equals(tempString)) {
						isExit = true;
					}
				}
				if (!isExit) {
					// 列表更新
					String[] newString = new String[MemberString.length];
					for (int i = 0; i < MemberString.length; i++) {
						newString[i] = MemberString[i];
					}
					MemberString_count++;
					MemberString = new String[MemberString_count];
					for (int i = 0; i < newString.length; i++) {
						MemberString[i] = newString[i];
					}
					MemberString[MemberString_count - 1] = tempString;

					// 缓存表格
					String[][] tableString1 = new String[5][4];
					String[][] tableString2 = new String[5][4];
					for (int k = 0; k < 5; k++) {
						for (int l = 0; l < 4; l++) {
							tableString1[k][l] = table1.getValueAt(k, l + 1).toString();
							tableString2[k][l] = table2.getValueAt(k, l + 1).toString();
						}
					}

					// 刷新表格
					jp_table_top_left.remove(jsp_table1);
					jp_table_top_right.remove(jsp_table2);
					tableData();
					jp_table_top_left.add(jsp_table1, BorderLayout.CENTER);
					jp_table_top_right.add(jsp_table2, BorderLayout.CENTER);
					jp_table_top_left.validate();
					jp_table_top_right.validate();
					jp_table_top.validate();
					jp_table.validate();
					jp_detail_mission.validate();
					jf.validate();

					// 重新加载表格信息
					for (int k = 0; k < 5; k++) {
						for (int l = 0; l < 4; l++) {
							table1.setValueAt(tableString1[k][l], k, l + 1);
							table2.setValueAt(tableString2[k][l], k, l + 1);
						}
					}
				}
			} else if (this.ID.equals("Delete")) {
				String tempString = table.getValueAt(table.getSelectedRow(), 0).toString();
				boolean isExit = false;
				for (int i = 0; i < MemberString.length; i++) {
					if (MemberString[i].equals(tempString)) {
						isExit = true;
					}
				}
				if (isExit) {
					for (int i = 0; i < MemberString.length; i++) {
						if (MemberString[i].equals(tempString)) {
							// 列表更新
							String[] newString = new String[MemberString.length - 1];
							for (int j = 0; j < i; j++) {
								newString[j] = MemberString[j];
							}
							for (int j = i; j < newString.length; j++) {
								newString[j] = MemberString[j + 1];
							}
							MemberString_count--;
							MemberString = new String[MemberString_count];
							for (int j = 0; j < newString.length; j++) {
								MemberString[j] = newString[j];
							}
						}
					}
					// 缓存表格
					String[][] tableString1 = new String[5][4];
					String[][] tableString2 = new String[5][4];
					for (int k = 0; k < 5; k++) {
						for (int l = 0; l < 4; l++) {
							tableString1[k][l] = table1.getValueAt(k, l + 1).toString();
							tableString2[k][l] = table2.getValueAt(k, l + 1).toString();
							if (tableString1[k][l].equals(tempString)) {
								tableString1[k][l] = noMember;
							}
							if (tableString2[k][l].equals(tempString)) {
								tableString2[k][l] = noMember;
							}
						}
					}

					// 刷新表格
					jp_table_top_left.remove(jsp_table1);
					jp_table_top_right.remove(jsp_table2);
					tableData();
					jp_table_top_left.add(jsp_table1, BorderLayout.CENTER);
					jp_table_top_right.add(jsp_table2, BorderLayout.CENTER);
					jp_table_top_left.validate();
					jp_table_top_right.validate();
					jp_table_top.validate();
					jp_table.validate();
					jp_detail_mission.validate();
					jf.validate();

					// 重新加载表格信息
					for (int k = 0; k < 5; k++) {
						for (int l = 0; l < 4; l++) {
							table1.setValueAt(tableString1[k][l], k, l + 1);
							table2.setValueAt(tableString2[k][l], k, l + 1);
						}
					}
				}
			}
		}
	}
}
