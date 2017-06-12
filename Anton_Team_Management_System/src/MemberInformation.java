
/*
 * @date 2016/5/23
 * @author  ZengYu
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class MemberInformation extends JFrame implements ActionListener {

	private JFrame jf;
	public String username;
	private String StringSearch = null;
	private JTextField jtfSearch;
	private JTable table;
	private JPanel jp_s = new JPanel();
	private MyTableModel model;
	private JScrollPane scroll;
	private Object[][] data;
	private String[] ButtonString = { "Search", "    Add    ", "Refresh" };
	private JPanel imagePanel;
	private ImageIcon ii_bg;

	public MemberInformation(String string_username) {
		username = string_username;

		jf = new JFrame("Anton Team Management System —— Member Information");
		ii_bg = new ImageIcon("res/layout/background.png");// 背景图片
		JLabel label = new JLabel(ii_bg);
		label.setBounds(0, -10, ii_bg.getIconWidth(), ii_bg.getIconHeight());
		imagePanel = (JPanel) jf.getContentPane();
		imagePanel.setOpaque(false);
		imagePanel.setLayout(new BorderLayout());

		jtfSearch = new JTextField(10);
		jtfSearch.setOpaque(false);
		jtfSearch.setFont(new Font("Dialog", 1, 15));
		jtfSearch.setForeground(Color.WHITE);
		JButton jb1 = new JButton(ButtonString[0]);
		jb1.addActionListener(this);
		jb1.setContentAreaFilled(false);
		jb1.setFont(new Font("Dialog", 1, 15));
		jb1.setForeground(Color.WHITE);
		jb1.setFocusable(false);

		JPanel jp1 = new JPanel();
		jp1.setOpaque(false);

		JButton jb2 = new JButton(ButtonString[1]);
		jb2.addActionListener(this);
		jb2.setContentAreaFilled(false);
		jb2.setFont(new Font("Dialog", 1, 15));
		jb2.setForeground(Color.WHITE);
		jb2.setFocusable(false);

		JPanel jp2 = new JPanel();
		jp2.setOpaque(false);

		JButton jb3 = new JButton(ButtonString[2]);
		jb3.addActionListener(this);
		jb3.addActionListener(this);
		jb3.setContentAreaFilled(false);
		jb3.setFont(new Font("Dialog", 1, 15));
		jb3.setForeground(Color.WHITE);
		jb3.setFocusable(false);

		JPanel jp4 = new JPanel();
		jp4.setOpaque(false);
		jp4.add(jtfSearch);
		jp4.add(jb1);
		jp4.add(jp1);
		jp4.add(jb2);
		jp4.add(jp2);
		jp4.add(jb3);

		imagePanel.add(jp4, BorderLayout.NORTH);

		tableData();

		jp_s.add(scroll);
		jp_s.setOpaque(false);

		imagePanel.add(jp_s, BorderLayout.CENTER);

		jf.getLayeredPane().setLayout(null);
		jf.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));
		jf.setResizable(false);
		jf.setVisible(true);
		jf.setSize(ii_bg.getIconWidth(), ii_bg.getIconHeight() + 10);
		jf.setLocation(200, 100);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public static void main(String string_username) {
		new MemberInformation(string_username);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String comm = e.getActionCommand();

		StringSearch = jtfSearch.getText();

		if (comm.equals(ButtonString[0])) {
			if (!StringSearch.equals("")) {
				new Search(StringSearch, username);
				jtfSearch.setText(null);
			}
		} else if (comm.equals(ButtonString[1])) {
			new Add(username);
		} else if (comm.equals(ButtonString[2])) {
			jp_s.remove(scroll);
			tableData();
			jp_s.add(scroll);
			jp_s.validate();
			jf.validate();
		}
	}

	public void tableData() {
		// 采用自定义数据模型
		model = new MyTableModel();
		table = new JTable(model);
		table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		table.getTableHeader().setReorderingAllowed(false); // 不可整列移动
		table.getTableHeader().setResizingAllowed(false); // 不可拉动表格

		ButtonColumn buttonModifyColumn = new ButtonColumn(table, 4);
		ButtonColumn buttonDeleteColumn = new ButtonColumn(table, 5);

		buttonModifyColumn.text = "√";
		buttonModifyColumn.ID = "Modify";
		buttonDeleteColumn.text = "×";
		buttonDeleteColumn.ID = "Delete";

		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		/* 用JScrollPane装载JTable，这样超出范围的列就可以通过滚动条来查看 */
		scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(540, 500));
	}

	class MyTableModel extends AbstractTableModel {
		// 单元格元素类型
		@SuppressWarnings("rawtypes")
		private Class[] cellType = { String.class, String.class, String.class, String.class, JButton.class,
				JButton.class };
		// 表头
		private String title[] = { "ID", "Profession", "Attribution", "Note", "Modify", "Delete" };

		public MyTableModel() {
			File file = new File("res/" + username + "//" + username + ".md");
			// 如果路径不存在,则创建
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
				data[i][4] = new JButton("Modify");
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
			if (this.ID.equals("Modify")) {
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
				// 记录行数
				FileReader count_fr = null;
				try {
					count_fr = new FileReader(file);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				BufferedReader count_br = new BufferedReader(count_fr);
				// 行数记录thisCount1
				int thisCount1 = 0;
				try {
					while (count_br.readLine() != null) {
						thisCount1++;
					}
				} catch (IOException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				}
				// 由行数确定缓存数组长度thisCount1
				String[] tempString = new String[thisCount1];
				// 写入信息时用于读取行数
				FileReader fr = null;
				try {
					fr = new FileReader(file);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				BufferedReader br = new BufferedReader(fr);
				try {
					// 开始缓存全文
					for (int i = 0; i < thisCount1; i++) {
						tempString[i] = br.readLine();
					}
				} catch (IOException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				}
				// 用于末尾追加
				FileWriter fw1 = null;
				// 用于全文覆盖
				FileWriter fw2 = null;
				try {
					// 设置末尾追加
					fw1 = new FileWriter(file, true);
					// 设置全文覆盖
					fw2 = new FileWriter(file);
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				// 末尾追加bw1
				BufferedWriter bw1 = new BufferedWriter(fw1);
				// 全文覆盖bw2
				BufferedWriter bw2 = new BufferedWriter(fw2);
				// 获取选择单元格的行号
				int SelectedRow = table.getSelectedRow();

				// bw2覆盖写入选择单元格之前信息
				if (SelectedRow > 0) {
					for (int i = 0; i < SelectedRow; i++) {
						try {
							bw2.write(tempString[i]);
							bw2.newLine();
						} catch (IOException e6) {
							// TODO Auto-generated catch block
							e6.printStackTrace();
						}
					}
				}
				try {
					bw2.flush();
					fw2.close();
					bw2.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// bw1追加写入选择单元格修改后信息
				try {
					// 更新需要修改的这一行数据
					bw1.write("<ID>" + data[SelectedRow][0] + "</ID><Profession>" + data[SelectedRow][1]
							+ "</Profession><Attribution>" + data[SelectedRow][2] + "</Attribution><Note>"
							+ data[SelectedRow][3] + "</Note>");
					bw1.newLine();
				} catch (IOException e6) {
					// TODO Auto-generated catch block
					e6.printStackTrace();
				}

				// bw1追加写入选择单元格后面的信息
				for (int i = SelectedRow + 1; i < thisCount1; i++) {
					try {
						bw1.write(tempString[i]);
						bw1.newLine();
					} catch (IOException e6) {
						// TODO Auto-generated catch block
						e6.printStackTrace();
					}
				}
				try {
					bw1.flush();
					count_fr.close();
					fr.close();
					fw1.close();
					bw1.close();
				} catch (IOException e7) {
					// TODO Auto-generated catch block
					e7.printStackTrace();
				}
			} else if (this.ID.equals("Delete")) {
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
				// 记录行数
				FileReader count_fr = null;
				try {
					count_fr = new FileReader(file);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				BufferedReader count_br = new BufferedReader(count_fr);
				// 行数记录thisCount1
				int thisCount1 = 0;
				try {
					while (count_br.readLine() != null) {
						thisCount1++;
					}
				} catch (IOException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				}
				// 由行数确定缓存数组长度thisCount1
				String[] tempString = new String[thisCount1];
				// 写入信息时用于读取行数
				FileReader fr = null;
				try {
					fr = new FileReader(file);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				BufferedReader br = new BufferedReader(fr);
				try {
					// 开始缓存全文
					for (int i = 0; i < thisCount1; i++) {
						tempString[i] = br.readLine();
					}
				} catch (IOException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				}
				// 用于末尾追加
				FileWriter fw1 = null;
				// 用于全文覆盖
				FileWriter fw2 = null;
				try {
					// 设置末尾追加
					fw1 = new FileWriter(file, true);
					// 设置全文覆盖
					fw2 = new FileWriter(file);
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				// 末尾追加bw1
				BufferedWriter bw1 = new BufferedWriter(fw1);
				// 全文覆盖bw2
				BufferedWriter bw2 = new BufferedWriter(fw2);
				// 获取选择单元格的行号
				int SelectedRow = table.getSelectedRow();

				// bw2覆盖写入选择单元格之前信息
				if (SelectedRow > 0) {
					for (int i = 0; i < SelectedRow; i++) {
						try {
							bw2.write(tempString[i]);
							bw2.newLine();
						} catch (IOException e6) {
							// TODO Auto-generated catch block
							e6.printStackTrace();
						}
					}
				}
				try {
					bw2.flush();
					fw2.close();
					bw2.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// bw1追加写入选择单元格后面的信息
				for (int i = SelectedRow + 1; i < thisCount1; i++) {
					try {
						bw1.write(tempString[i]);
						bw1.newLine();
					} catch (IOException e6) {
						// TODO Auto-generated catch block
						e6.printStackTrace();
					}
				}
				try {
					bw1.flush();
					count_fr.close();
					fr.close();
					fw1.close();
					bw1.close();
				} catch (IOException e7) {
					// TODO Auto-generated catch block
					e7.printStackTrace();
				}
				jp_s.remove(scroll);
				tableData();
				jp_s.add(scroll);
				jp_s.validate();
				jf.validate();
			}
		}
	}
}
