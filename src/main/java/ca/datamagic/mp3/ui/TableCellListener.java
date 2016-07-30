/**
 * 
 */
package ca.datamagic.mp3.ui;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

/**
 * @author Greg
 *
 */
public class TableCellListener implements PropertyChangeListener, Runnable {
	private JTable _table = null;
	private Action _action = null;
	private int _row = 0;
	private int _column = 0;
	private Object _oldValue = null;
	private Object _newValue = null;
	
	/**
	 *  Create a TableCellListener.
	 *
	 *  @param table   the table to be monitored for data changes
	 *  @param action  the Action to invoke when cell data is changed
	 */
	public TableCellListener(JTable table, Action action)
	{
		_table = table;
		_action = action;
		_table.addPropertyChangeListener( this );
	}

	/**
	 *  Create a TableCellListener with a copy of all the data relevant to
	 *  the change of data for a given cell.
	 *
	 *  @param row  the row of the changed cell
	 *  @param column  the column of the changed cell
	 *  @param oldValue  the old data of the changed cell
	 *  @param newValue  the new data of the changed cell
	 */
	private TableCellListener(JTable table, int row, int column, Object oldValue, Object newValue)
	{
		_table = table;
		_row = row;
		_column = column;
		_oldValue = oldValue;
		_newValue = newValue;
	}

	/**
	 *  Get the column that was last edited
	 *
	 *  @return the column that was edited
	 */
	public int getColumn() {
		return _column;
	}

	/**
	 *  Get the new value in the cell
	 *
	 *  @return the new value in the cell
	 */
	public Object getNewValue() {
		return _newValue;
	}

	/**
	 *  Get the old value of the cell
	 *
	 *  @return the old value of the cell
	 */
	public Object getOldValue(){
		return _oldValue;
	}

	/**
	 *  Get the row that was last edited
	 *
	 *  @return the row that was edited
	 */
	public int getRow() {
		return _row;
	}

	/**
	 *  Get the table of the cell that was changed
	 *
	 *  @return the table of the cell that was changed
	 */
	public JTable getTable() {
		return _table;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		//  A cell has started/stopped editing
		if ("tableCellEditor".compareToIgnoreCase(e.getPropertyName()) == 0)
		{
			if (_table.isEditing()) {
				processEditingStarted();
			} else {
				processEditingStopped();
			}
		}
	}

	/*
	 *  Save information of the cell about to be edited
	 */
	private void processEditingStarted() {
		//  The invokeLater is necessary because the editing row and editing
		//  column of the table have not been set when the "tableCellEditor"
		//  PropertyChangeEvent is fired.
		//  This results in the "run" method being invoked
		SwingUtilities.invokeLater( this );
	}

	@Override
	public void run() {
		_oldValue = null;
		_newValue = null;
		int rowIndex = _table.getEditingRow();
		int columnIndex = _table.getEditingColumn();
		if ((rowIndex > -1) && (rowIndex < _table.getRowCount()) &&
			(columnIndex > -1) && (columnIndex < _table.getColumnCount())) {
			_row = _table.convertRowIndexToModel(rowIndex);
			_column = _table.convertColumnIndexToModel(columnIndex);
			TableModel tableModel = _table.getModel();
			if ((_row > -1) && (_row < tableModel.getRowCount()) &&
				(_column > -1) && (_column < tableModel.getColumnCount())) {
				_oldValue = tableModel.getValueAt(_row, _column);
				_newValue = null;
			}
		}
	}
	
	/*
	 *	Update the Cell history when necessary
	 */
	private void processEditingStopped() {
		TableModel tableModel = _table.getModel();
		if ((_row > -1) && (_row < tableModel.getRowCount()) &&
			(_column > -1) && (_column < tableModel.getColumnCount())) {
			_newValue = tableModel.getValueAt(_row, _column);
			//  The data has changed, invoke the supplied Action
			if (!_newValue.equals(_oldValue)) {
				//  Make a copy of the data in case another cell starts editing
				//  while processing this change
				TableCellListener tcl = new TableCellListener(getTable(), getRow(), getColumn(), getOldValue(), getNewValue());
				ActionEvent event = new ActionEvent(tcl, ActionEvent.ACTION_PERFORMED, "");
				_action.actionPerformed(event);
			}
		}
	}
}
