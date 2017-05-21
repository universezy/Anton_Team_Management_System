import java.util.Vector;

import javax.swing.table.AbstractTableModel;

class Table_Model1 extends AbstractTableModel {
		private static final long serialVersionUID = -3094977414157589758L;
		private String[] title_name = { "", "Captain", "Member", "Member", "Member" };

		@SuppressWarnings("rawtypes")
		private Vector content = null;

		@SuppressWarnings("rawtypes")
		public Table_Model1() {
			content = new Vector();
		}

		/**
		 * 加入一行内容
		 */
		@SuppressWarnings("unchecked")
		public void addRow(String str1, String str2, String str3, String str4, String str5) {
			@SuppressWarnings("rawtypes")
			Vector v = new Vector(5);
			v.add(0, str1);
			v.add(1, str2);
			v.add(2, str3);
			v.add(3, str4);
			v.add(4, str5);
			content.add(v);
		}

		public void removeRow(int row) {
			content.remove(row);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if (columnIndex == 0) {
				return false;
			}
			return true;
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		public void setValueAt(Object value, int row, int col) {
			((Vector) content.get(row)).remove(col);
			((Vector) content.get(row)).add(col, value);
			this.fireTableCellUpdated(row, col);
		}

		@Override
		public String getColumnName(int col) {
			return title_name[col];
		}

		@Override
		public int getColumnCount() {
			return title_name.length;
		}

		@Override
		public int getRowCount() {
			return content.size();
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Object getValueAt(int row, int col) {
			return ((Vector) content.get(row)).get(col);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Class getColumnClass(int col) {
			return getValueAt(0, col).getClass();
		}

	}