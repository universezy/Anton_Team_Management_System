
/*
 * @date 2016/5/23
 * @author  ZengYu
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class Search extends JFrame implements ActionListener {
	private JFrame jf;
	public String String_Search, username;
	private JTable table;
	private JPanel jp_s = new JPanel();
	private MyTableModel model;
	private JScrollPane scroll;
	private Object[][] data;
	private int[] rowRecord = null;

	public Search(String StringSearch, String string_username) {
		String_Search = StringSearch;
		username = string_username;
		jf = new JFrame("Search : " + StringSearch);
		jf.setLayout(new BorderLayout());

		tableData();

		jp_s.add(scroll);
		jf.add(jp_s, BorderLayout.SOUTH);

		jf.setResizable(false);
		jf.setVisible(true);
		jf.setSize(800, 600);
		jf.setLocation(300, 100);
		jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jf.pack();
	}

	public static void main(String StringSearch, String string_username) {
		new Search(StringSearch, string_username);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	public void tableData() {
		// 采用自定义数据模型
		model = new MyTableModel();
		table = new JTable(model);

		ButtonColumn buttonModifyColumn = new ButtonColumn(table, 4);
		ButtonColumn buttonDeleteColumn = new ButtonColumn(table, 5);

		buttonModifyColumn.text = "√";
		buttonModifyColumn.ID = "Modify";
		buttonDeleteColumn.text = "×";
		buttonDeleteColumn.ID = "Delete";

		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		/* 用JScrollPane装载JTable，这样超出范围的列就可以通过滚动条来查看 */
		scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(540, 420));
	}

	class MyTableModel extends AbstractTableModel {
		// 单元格元素类型
		@SuppressWarnings("rawtypes")
		private Class[] cellType = { String.class, String.class, String.class, String.class, JButton.class,
				JButton.class };
		// 表头
		private String title[] = { "ID", "Profession", "Attribution", "Note", "Modify", "Delete" };
		private int count = 0, tempcount = 0;

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
			try {
				while (count_br.readLine() != null)
					tempcount++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 定义临时二维数组
			Object[][] tempobject = new Object[tempcount][4];

			// 定义行数记录数组
			rowRecord = new int[tempcount];

			String tempString = new String("");
			for (int i = 0; i < tempcount; i++) {
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
				tempobject[i][0] = tempString.substring(index1 + "<ID>".length(), index2);
				tempobject[i][1] = tempString.substring(index3 + "<Profession>".length(), index4);
				tempobject[i][2] = tempString.substring(index5 + "<Attribution>".length(), index6);
				tempobject[i][3] = tempString.substring(index7 + "<Note>".length(), index8);

				if (tempobject[i][0].equals(String_Search) || tempobject[i][1].equals(String_Search)
						|| tempobject[i][2].equals(String_Search) || tempobject[i][3].equals(String_Search)) {
					rowRecord[count] = i;
					count++;
				}
			}
			// 定义二维数组作为表格数据
			data = new Object[count][6];
			for (int i = 0; i < count; i++) {
				data[i][0] = tempobject[rowRecord[i]][0];
				data[i][1] = tempobject[rowRecord[i]][1];
				data[i][2] = tempobject[rowRecord[i]][2];
				data[i][3] = tempobject[rowRecord[i]][3];
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
				if (rowRecord[SelectedRow] > 0) {
					for (int i = 0; i < rowRecord[SelectedRow]; i++) {
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
				for (int i = rowRecord[SelectedRow] + 1; i < thisCount1; i++) {
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
				if (rowRecord[SelectedRow] > 0) {
					for (int i = 0; i < rowRecord[SelectedRow]; i++) {
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
				for (int i = rowRecord[SelectedRow] + 1; i < thisCount1; i++) {
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
